package com.example.digital_fit.service.CentroPrivado;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.CentroPrivado.AutocompletarCentroPrivadoDTO;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class AutocompletadoCentroPrivadoService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AutocompletarCentroPrivadoDTO autocompletar(Double latitud, Double longitud) {
        AutocompletarCentroPrivadoDTO dto = new AutocompletarCentroPrivadoDTO();
        dto.setLatitud(latitud);
        dto.setLongitud(longitud);
        dto.setDireccion("");
        dto.setTelefono("");
        dto.setHorario("");
        dto.setPrecioMensual(null);
        dto.setDescripcion("");
        dto.setDatosEncontrados(false);

        try {
            JsonNode lugarDeportivo = buscarLugarDeportivoCercano(latitud, longitud);

            if (lugarDeportivo != null && !lugarDeportivo.isMissingNode()) {
                Double latPoi = obtenerLat(lugarDeportivo, latitud);
                Double lonPoi = obtenerLon(lugarDeportivo, longitud);

                dto.setLatitud(latPoi);
                dto.setLongitud(lonPoi);

                JsonNode tags = lugarDeportivo.path("tags");

                String direccionDesdePoi = construirDireccionDesdeTags(tags);
                String telefono = primerValorNoVacio(
                        tags.path("phone").asString(""),
                        tags.path("contact:phone").asString(""),
                        tags.path("contact:mobile").asString(""));
                String horarioOriginal = primerValorNoVacio(
                        tags.path("opening_hours").asString(""),
                        tags.path("service_times").asString(""));
                String horarioTraducido = traducirOpeningHours(horarioOriginal);
                Double precioMensual = extraerPrecioMensual(tags);
                String descripcion = construirDescripcionDesdePoi(tags);

                dto.setDireccion(direccionDesdePoi);
                dto.setTelefono(telefono);
                dto.setHorario(horarioTraducido);
                dto.setPrecioMensual(precioMensual);
                dto.setDescripcion(descripcion);

                completarConReverseSiHaceFalta(dto, latPoi, lonPoi);
            } else {
                completarConReverse(dto, latitud, longitud);
            }

            dto.setDatosEncontrados(
                    !dto.getDireccion().isBlank()
                            || !dto.getTelefono().isBlank()
                            || !dto.getHorario().isBlank()
                            || dto.getPrecioMensual() != null
                            || !dto.getDescripcion().isBlank());

            return dto;

        } catch (Exception e) {
            return dto;
        }
    }

    private JsonNode buscarLugarDeportivoCercano(Double latitud, Double longitud)
            throws IOException, InterruptedException {

        String query = """
                [out:json][timeout:15];
                (
                  nwr(around:80,%f,%f)[leisure=fitness_centre];
                  nwr(around:80,%f,%f)[leisure=sports_centre];
                  nwr(around:80,%f,%f)[leisure=sports_hall];
                  nwr(around:80,%f,%f)[leisure=stadium];
                  nwr(around:80,%f,%f)[leisure=track];
                  nwr(around:80,%f,%f)[leisure=pitch];
                  nwr(around:80,%f,%f)[sport];
                  nwr(around:80,%f,%f)[amenity=gym];
                );
                out center tags;
                """.formatted(
                latitud, longitud,
                latitud, longitud,
                latitud, longitud,
                latitud, longitud,
                latitud, longitud,
                latitud, longitud,
                latitud, longitud,
                latitud, longitud);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://overpass-api.de/api/interpreter"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("User-Agent", "Digital-FIT/1.0 (deteccion-centros-deportivos)")
                .POST(HttpRequest.BodyPublishers.ofString("data=" + URLEncoder.encode(query, StandardCharsets.UTF_8)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300 || response.body() == null
                || response.body().isBlank()) {
            return null;
        }

        JsonNode root = objectMapper.readTree(response.body());
        JsonNode elements = root.path("elements");

        if (!elements.isArray() || elements.isEmpty()) {
            return null;
        }

        List<JsonNode> candidatos = new ArrayList<>();
        elements.forEach(candidatos::add);

        return candidatos.stream()
                .sorted(
                        Comparator
                                .comparingInt((JsonNode n) -> calcularPrioridad(n)).reversed()
                                .thenComparingDouble(n -> calcularDistancia(
                                        latitud,
                                        longitud,
                                        obtenerLat(n, latitud),
                                        obtenerLon(n, longitud))))
                .findFirst()
                .orElse(null);
    }

    private int calcularPrioridad(JsonNode elemento) {
        JsonNode tags = elemento.path("tags");

        String leisure = tags.path("leisure").asString("");
        String amenity = tags.path("amenity").asString("");
        String sport = tags.path("sport").asString("");

        if ("fitness_centre".equals(leisure))
            return 100;
        if ("sports_centre".equals(leisure))
            return 95;
        if ("sports_hall".equals(leisure))
            return 90;
        if ("stadium".equals(leisure))
            return 85;
        if ("track".equals(leisure))
            return 80;
        if ("pitch".equals(leisure))
            return 75;
        if ("gym".equals(amenity))
            return 65;
        if (!sport.isBlank())
            return 60;

        return 10;
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        double dLat = lat1 - lat2;
        double dLon = lon1 - lon2;
        return Math.sqrt(dLat * dLat + dLon * dLon);
    }

    private Double obtenerLat(JsonNode elemento, Double valorPorDefecto) {
        if (elemento.has("lat")) {
            return elemento.path("lat").asDouble();
        }

        if (elemento.has("center") && elemento.path("center").has("lat")) {
            return elemento.path("center").path("lat").asDouble();
        }

        return valorPorDefecto;
    }

    private Double obtenerLon(JsonNode elemento, Double valorPorDefecto) {
        if (elemento.has("lon")) {
            return elemento.path("lon").asDouble();
        }

        if (elemento.has("center") && elemento.path("center").has("lon")) {
            return elemento.path("center").path("lon").asDouble();
        }

        return valorPorDefecto;
    }

    private void completarConReverseSiHaceFalta(AutocompletarCentroPrivadoDTO dto, Double latitud, Double longitud)
            throws IOException, InterruptedException {

        if (!dto.getDireccion().isBlank()) {
            return;
        }

        completarConReverse(dto, latitud, longitud);
    }

    private void completarConReverse(AutocompletarCentroPrivadoDTO dto, Double latitud, Double longitud)
            throws IOException, InterruptedException {

        String url = "https://nominatim.openstreetmap.org/reverse?format=jsonv2"
                + "&lat=" + URLEncoder.encode(String.valueOf(latitud), StandardCharsets.UTF_8)
                + "&lon=" + URLEncoder.encode(String.valueOf(longitud), StandardCharsets.UTF_8)
                + "&addressdetails=1"
                + "&extratags=1";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Accept-Language", "es")
                .header("User-Agent", "Digital-FIT/1.0 (autocompletado-centros)")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() < 200 || response.statusCode() >= 300 || response.body() == null
                || response.body().isBlank()) {
            return;
        }

        JsonNode root = objectMapper.readTree(response.body());
        JsonNode address = root.path("address");
        JsonNode extratags = root.path("extratags");

        if (dto.getDireccion().isBlank()) {
            dto.setDireccion(construirDireccion(address, root.path("display_name").asString("")));
        }

        if (dto.getTelefono().isBlank()) {
            dto.setTelefono(primerValorNoVacio(
                    extratags.path("phone").asString(""),
                    extratags.path("contact:phone").asString("")));
        }

        if (dto.getHorario().isBlank()) {
            dto.setHorario(traducirOpeningHours(primerValorNoVacio(
                    extratags.path("opening_hours").asString(""),
                    extratags.path("opening_hours:description").asString(""))));
        }

        if (dto.getPrecioMensual() == null) {
            dto.setPrecioMensual(extraerPrecioMensual(extratags));
        }

        if (dto.getDescripcion().isBlank()) {
            dto.setDescripcion(construirDescripcion(root, address));
        }
    }

    private String construirDireccionDesdeTags(JsonNode tags) {
        String street = primerValorNoVacio(
                tags.path("addr:street").asString(""),
                tags.path("addr:place").asString(""),
                tags.path("addr:full").asString(""));

        String houseNumber = tags.path("addr:housenumber").asString("");
        String postcode = tags.path("addr:postcode").asString("");
        String city = primerValorNoVacio(
                tags.path("addr:city").asString(""),
                tags.path("addr:town").asString(""),
                tags.path("addr:village").asString(""),
                tags.path("addr:municipality").asString(""));

        String province = primerValorNoVacio(
                tags.path("addr:province").asString(""),
                tags.path("addr:state").asString(""));

        List<String> partes = new ArrayList<>();

        String calle = unirConEspacio(street, houseNumber);
        if (!calle.isBlank()) {
            partes.add(calle);
        }

        String cpCiudad = unirConEspacio(postcode, city);
        if (!cpCiudad.isBlank()) {
            partes.add(cpCiudad);
        }

        if (!province.isBlank()) {
            partes.add(province);
        }

        return String.join(", ", partes);
    }

    private String construirDireccion(JsonNode address, String displayName) {
        String road = primerValorNoVacio(
                address.path("road").asString(""),
                address.path("pedestrian").asString(""),
                address.path("footway").asString(""),
                address.path("path").asString(""),
                address.path("cycleway").asString(""));

        String houseNumber = address.path("house_number").asString("");
        String postcode = address.path("postcode").asString("");
        String city = primerValorNoVacio(
                address.path("city").asString(""),
                address.path("town").asString(""),
                address.path("village").asString(""),
                address.path("municipality").asString(""),
                address.path("suburb").asString(""));

        String state = address.path("state").asString("");
        String country = address.path("country").asString("");

        List<String> partes = new ArrayList<>();

        String calle = unirConEspacio(road, houseNumber);
        if (!calle.isBlank()) {
            partes.add(calle);
        }

        String cpCiudad = unirConEspacio(postcode, city);
        if (!cpCiudad.isBlank()) {
            partes.add(cpCiudad);
        }

        if (!state.isBlank()) {
            partes.add(state);
        }

        if (!country.isBlank()) {
            partes.add(country);
        }

        String resultado = String.join(", ", partes);

        if (!resultado.isBlank()) {
            return resultado;
        }

        return displayName == null ? "" : displayName;
    }

    private String construirDescripcionDesdePoi(JsonNode tags) {
        String leisure = tags.path("leisure").asString("");
        String sport = tags.path("sport").asString("");
        String name = tags.path("name").asString("");

        String tipo = switch (leisure) {
            case "fitness_centre" -> "gimnasio";
            case "sports_centre" -> "centro deportivo";
            case "sports_hall" -> "pabellón deportivo";
            case "stadium" -> "estadio";
            case "track" -> "pista deportiva";
            case "pitch" -> "instalación deportiva";
            default -> !sport.isBlank() ? "instalación deportiva" : "";
        };

        if (!name.isBlank() && !tipo.isBlank()) {
            return "Lugar deportivo detectado automáticamente: " + tipo + " cercano a " + name + ".";
        }

        if (!tipo.isBlank()) {
            return "Lugar deportivo detectado automáticamente: " + tipo + ".";
        }

        return "";
    }

    private String construirDescripcion(JsonNode root, JsonNode address) {
        String tipo = root.path("type").asString("");
        String city = primerValorNoVacio(
                address.path("city").asString(""),
                address.path("town").asString(""),
                address.path("village").asString(""),
                address.path("municipality").asString(""),
                address.path("suburb").asString(""));

        if (!tipo.isBlank() && !city.isBlank()) {
            return "Ubicación detectada automáticamente cerca de " + city + " (" + tipo.replace('_', ' ') + ").";
        }

        if (!city.isBlank()) {
            return "Ubicación detectada automáticamente cerca de " + city + ".";
        }

        return "";
    }

    private Double extraerPrecioMensual(JsonNode tags) {
        String charge = primerValorNoVacio(
                tags.path("charge").asString(""),
                tags.path("charge:conditional").asString(""));

        if (charge.isBlank()) {
            return null;
        }

        String chargeLower = charge.toLowerCase();

        boolean pareceMensual = chargeLower.contains("month")
                || chargeLower.contains("monthly")
                || chargeLower.contains("mes")
                || chargeLower.contains("/month")
                || chargeLower.contains("/mes");

        if (!pareceMensual) {
            return null;
        }

        Matcher matcher = Pattern.compile("(\\d+(?:[\\.,]\\d+)?)").matcher(charge);

        if (matcher.find()) {
            try {
                return Double.valueOf(matcher.group(1).replace(",", "."));
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    private String traducirOpeningHours(String openingHours) {
        if (openingHours == null || openingHours.isBlank()) {
            return "";
        }

        String texto = openingHours;

        // Días
        texto = texto.replaceAll("\\bMo\\b", "Lun");
        texto = texto.replaceAll("\\bTu\\b", "Mar");
        texto = texto.replaceAll("\\bWe\\b", "Mié");
        texto = texto.replaceAll("\\bTh\\b", "Jue");
        texto = texto.replaceAll("\\bFr\\b", "Vie");
        texto = texto.replaceAll("\\bSa\\b", "Sáb");
        texto = texto.replaceAll("\\bSu\\b", "Dom");

        // Meses
        texto = texto.replaceAll("\\bJan\\b", "Ene");
        texto = texto.replaceAll("\\bFeb\\b", "Feb");
        texto = texto.replaceAll("\\bMar\\b", "Mar");
        texto = texto.replaceAll("\\bApr\\b", "Abr");
        texto = texto.replaceAll("\\bMay\\b", "May");
        texto = texto.replaceAll("\\bJun\\b", "Jun");
        texto = texto.replaceAll("\\bJul\\b", "Jul");
        texto = texto.replaceAll("\\bAug\\b", "Ago");
        texto = texto.replaceAll("\\bSep\\b", "Sep");
        texto = texto.replaceAll("\\bOct\\b", "Oct");
        texto = texto.replaceAll("\\bNov\\b", "Nov");
        texto = texto.replaceAll("\\bDec\\b", "Dic");

        // Otros
        texto = texto.replaceAll("\\bPH\\b", "Festivos");
        texto = texto.replaceAll("\\bSH\\b", "Vacaciones escolares");
        texto = texto.replaceAll("\\boff\\b", "cerrado");
        texto = texto.replace("24/7", "24 horas");

        return texto;
    }

    private String unirConEspacio(String a, String b) {
        if ((a == null || a.isBlank()) && (b == null || b.isBlank())) {
            return "";
        }

        if (a == null || a.isBlank()) {
            return b.trim();
        }

        if (b == null || b.isBlank()) {
            return a.trim();
        }

        return a.trim() + " " + b.trim();
    }

    private String primerValorNoVacio(String... valores) {
        for (String valor : valores) {
            if (valor != null && !valor.isBlank()) {
                return valor.trim();
            }
        }
        return "";
    }
}