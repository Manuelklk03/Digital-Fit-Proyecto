package com.example.digital_fit.service.LugarPublico;

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

import org.springframework.stereotype.Service;

import com.example.digital_fit.dto.LugarPublico.AutocompletarLugarPublicoDTO;
import com.example.digital_fit.model.Enums.TipoLugarPublico;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class AutocompletadoLugarPublicoService {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AutocompletarLugarPublicoDTO autocompletar(Double latitud, Double longitud) {
        AutocompletarLugarPublicoDTO dto = new AutocompletarLugarPublicoDTO();
        dto.setLatitud(latitud);
        dto.setLongitud(longitud);
        dto.setDireccion("");
        dto.setDescripcion("");
        dto.setTelefono("");
        dto.setHorario("");
        dto.setTipo(null);
        dto.setDatosEncontrados(false);

        try {
            JsonNode lugarDetectado = buscarLugarDeportivoCercano(latitud, longitud);

            if (lugarDetectado != null && !lugarDetectado.isMissingNode()) {
                Double latPoi = obtenerLat(lugarDetectado, latitud);
                Double lonPoi = obtenerLon(lugarDetectado, longitud);

                dto.setLatitud(latPoi);
                dto.setLongitud(lonPoi);

                JsonNode tags = lugarDetectado.path("tags");

                String direccion = construirDireccionDesdeTags(tags);
                String telefono = primerValorNoVacio(
                        tags.path("phone").asString(""),
                        tags.path("contact:phone").asString(""),
                        tags.path("contact:mobile").asString(""));
                String horarioOriginal = primerValorNoVacio(
                        tags.path("opening_hours").asString(""),
                        tags.path("service_times").asString(""));
                String horarioTraducido = traducirOpeningHours(horarioOriginal);
                TipoLugarPublico tipoDetectado = inferirTipo(tags);
                String descripcion = construirDescripcionDesdePoi(tags, tipoDetectado);

                dto.setDireccion(direccion);
                dto.setTelefono(telefono);
                dto.setHorario(horarioTraducido);
                dto.setTipo(tipoDetectado);
                dto.setDescripcion(descripcion);

                completarConReverseSiHaceFalta(dto, latPoi, lonPoi);
            } else {
                completarConReverse(dto, latitud, longitud);
            }

            dto.setDatosEncontrados(
                    !dto.getDireccion().isBlank()
                            || !dto.getDescripcion().isBlank()
                            || !dto.getTelefono().isBlank()
                            || !dto.getHorario().isBlank()
                            || dto.getTipo() != null);

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
                  nwr(around:120,%f,%f)[leisure=pitch];
                  nwr(around:120,%f,%f)[leisure=track];
                  nwr(around:120,%f,%f)[leisure=fitness_station];
                  nwr(around:120,%f,%f)[leisure=park];
                  nwr(around:120,%f,%f)[leisure=swimming_pool];
                  nwr(around:120,%f,%f)[highway=cycleway];
                  nwr(around:120,%f,%f)[highway=path];
                  nwr(around:120,%f,%f)[highway=footway];
                  nwr(around:120,%f,%f)[natural=beach];
                  nwr(around:120,%f,%f)[waterway=river];
                  nwr(around:120,%f,%f)[waterway=canal];
                  nwr(around:120,%f,%f)[sport];
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
                latitud, longitud,
                latitud, longitud,
                latitud, longitud,
                latitud, longitud,
                latitud, longitud);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://overpass-api.de/api/interpreter"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .header("User-Agent", "Digital-FIT/1.0 (deteccion-lugares-publicos)")
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
        TipoLugarPublico tipo = inferirTipo(tags);

        if (tipo == null)
            return 10;

        return switch (tipo) {
            case PISTA_PADEL -> 100;
            case PISTA_TENIS -> 98;
            case PISTA_FRONTON -> 95;
            case PISTA_FUTBOL -> 94;
            case PISTA_BALONCESTO -> 94;
            case PISTA_VOLEIBOL -> 94;
            case CAMPO_RUGBY -> 94;
            case ZONA_VOLEY_PLAYA -> 92;
            case PISCINA_PUBLICA -> 90;
            case PARQUE_CALISTENIA -> 88;
            case ZONA_BARRAS -> 86;
            case CIRCUITO_BIOSALUDABLE -> 84;
            case CARRIL_BICI -> 82;
            case CIRCUITO_CICLISMO -> 80;
            case RUTA_RUNNING -> 78;
            case RUTA_SENDERISMO -> 76;
            case PLAYA_DEPORTIVA -> 74;
            case ZONA_REMAR -> 72;
            case RUTA_FLUVIAL -> 70;
            case ZONA_MULTIDEPORTE -> 68;
            case PARQUE_PUBLICO -> 60;
            case ZONA_MONTAÑA -> 58;
        };
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

    private void completarConReverseSiHaceFalta(AutocompletarLugarPublicoDTO dto, Double latitud, Double longitud)
            throws IOException, InterruptedException {

        if (!dto.getDireccion().isBlank()) {
            return;
        }

        completarConReverse(dto, latitud, longitud);
    }

    private void completarConReverse(AutocompletarLugarPublicoDTO dto, Double latitud, Double longitud)
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
                .header("User-Agent", "Digital-FIT/1.0 (autocompletado-lugares)")
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

        if (dto.getDescripcion().isBlank()) {
            dto.setDescripcion(construirDescripcion(root, address));
        }

        if (dto.getTipo() == null) {
            dto.setTipo(inferirTipoDesdeReverse(root, address));
        }
    }

    private TipoLugarPublico inferirTipo(JsonNode tags) {
        String sport = tags.path("sport").asString("").toLowerCase();
        String leisure = tags.path("leisure").asString("").toLowerCase();
        String highway = tags.path("highway").asString("").toLowerCase();
        String natural = tags.path("natural").asString("").toLowerCase();
        String waterway = tags.path("waterway").asString("").toLowerCase();
        String surface = tags.path("surface").asString("").toLowerCase();
        String name = tags.path("name").asString("").toLowerCase();

        List<String> deportes = separarValores(sport);

        if (deportes.size() > 1) {
            if (deportes.contains("beachvolleyball") || deportes.contains("beach_volleyball")) {
                return TipoLugarPublico.ZONA_VOLEY_PLAYA;
            }
            return TipoLugarPublico.ZONA_MULTIDEPORTE;
        }

        if (natural.equals("beach"))
            return TipoLugarPublico.PLAYA_DEPORTIVA;

        if (contieneDeporte(deportes, "paddle_tennis", "padel", "paddle"))
            return TipoLugarPublico.PISTA_PADEL;
        if (contieneDeporte(deportes, "tennis"))
            return TipoLugarPublico.PISTA_TENIS;
        if (contieneDeporte(deportes, "fronton", "pelota", "jai_alai", "basque_pelota"))
            return TipoLugarPublico.PISTA_FRONTON;
        if (contieneDeporte(deportes, "soccer", "football", "futsal"))
            return TipoLugarPublico.PISTA_FUTBOL;
        if (contieneDeporte(deportes, "basketball"))
            return TipoLugarPublico.PISTA_BALONCESTO;

        if (contieneDeporte(deportes, "volleyball")) {
            if (natural.equals("beach") || surface.equals("sand") || name.contains("playa")) {
                return TipoLugarPublico.ZONA_VOLEY_PLAYA;
            }
            return TipoLugarPublico.PISTA_VOLEIBOL;
        }

        if (contieneDeporte(deportes, "rugby"))
            return TipoLugarPublico.CAMPO_RUGBY;
        if (contieneDeporte(deportes, "swimming"))
            return TipoLugarPublico.PISCINA_PUBLICA;
        if (contieneDeporte(deportes, "rowing", "canoe", "canoeing", "kayak"))
            return TipoLugarPublico.ZONA_REMAR;
        if (contieneDeporte(deportes, "cycling", "bicycle"))
            return TipoLugarPublico.CIRCUITO_CICLISMO;
        if (contieneDeporte(deportes, "running", "athletics", "jogging"))
            return TipoLugarPublico.RUTA_RUNNING;
        if (contieneDeporte(deportes, "hiking"))
            return TipoLugarPublico.RUTA_SENDERISMO;
        if (contieneDeporte(deportes, "fitness", "workout"))
            return TipoLugarPublico.PARQUE_CALISTENIA;

        if (highway.equals("cycleway"))
            return TipoLugarPublico.CARRIL_BICI;
        if (highway.equals("path") || highway.equals("footway"))
            return TipoLugarPublico.RUTA_SENDERISMO;

        if (waterway.equals("river") || waterway.equals("canal"))
            return TipoLugarPublico.RUTA_FLUVIAL;

        if (leisure.equals("swimming_pool"))
            return TipoLugarPublico.PISCINA_PUBLICA;
        if (leisure.equals("fitness_station")) {
            if (name.contains("barras"))
                return TipoLugarPublico.ZONA_BARRAS;
            return TipoLugarPublico.PARQUE_CALISTENIA;
        }
        if (leisure.equals("track"))
            return TipoLugarPublico.RUTA_RUNNING;
        if (leisure.equals("pitch"))
            return TipoLugarPublico.ZONA_MULTIDEPORTE;
        if (leisure.equals("park"))
            return TipoLugarPublico.PARQUE_PUBLICO;

        return null;
    }

    private TipoLugarPublico inferirTipoDesdeReverse(JsonNode root, JsonNode address) {
        String category = root.path("category").asString("").toLowerCase();
        String type = root.path("type").asString("").toLowerCase();
        String display = root.path("display_name").asString("").toLowerCase();
        String road = address.path("road").asString("").toLowerCase();

        if (type.contains("beach"))
            return TipoLugarPublico.PLAYA_DEPORTIVA;
        if (type.contains("park") || category.contains("leisure"))
            return TipoLugarPublico.PARQUE_PUBLICO;
        if (display.contains("bici") || road.contains("bici"))
            return TipoLugarPublico.CARRIL_BICI;
        if (display.contains("sender") || display.contains("trail"))
            return TipoLugarPublico.RUTA_SENDERISMO;
        if (display.contains("río") || display.contains("rio") || category.contains("waterway"))
            return TipoLugarPublico.RUTA_FLUVIAL;

        return null;
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

    private String construirDescripcionDesdePoi(JsonNode tags, TipoLugarPublico tipo) {
        String name = tags.path("name").asString("");

        if (tipo == null) {
            return "";
        }

        String tipoTexto = traducirTipo(tipo);

        if (name != null && !name.isBlank()) {
            return "Lugar detectado automáticamente: " + tipoTexto + " cercano a " + name + ".";
        }

        return "Lugar detectado automáticamente: " + tipoTexto + ".";
    }

    private String construirDescripcion(JsonNode root, JsonNode address) {
        String type = root.path("type").asString("");
        String city = primerValorNoVacio(
                address.path("city").asString(""),
                address.path("town").asString(""),
                address.path("village").asString(""),
                address.path("municipality").asString(""),
                address.path("suburb").asString(""));

        if (!type.isBlank() && !city.isBlank()) {
            return "Ubicación detectada automáticamente cerca de " + city + " (" + type.replace('_', ' ') + ").";
        }

        if (!city.isBlank()) {
            return "Ubicación detectada automáticamente cerca de " + city + ".";
        }

        return "";
    }

    private String traducirTipo(TipoLugarPublico tipo) {
        return switch (tipo) {
            case PISTA_PADEL -> "pista de pádel";
            case PISTA_TENIS -> "pista de tenis";
            case PISTA_FRONTON -> "pista de frontón";
            case PISTA_FUTBOL -> "pista de fútbol";
            case PISTA_BALONCESTO -> "pista de baloncesto";
            case PISTA_VOLEIBOL -> "pista de voleibol";
            case CAMPO_RUGBY -> "campo de rugby";
            case RUTA_RUNNING -> "ruta de running";
            case CIRCUITO_CICLISMO -> "circuito de ciclismo";
            case CARRIL_BICI -> "carril bici";
            case PARQUE_CALISTENIA -> "parque de calistenia";
            case ZONA_BARRAS -> "zona de barras";
            case CIRCUITO_BIOSALUDABLE -> "circuito biosaludable";
            case RUTA_SENDERISMO -> "ruta de senderismo";
            case ZONA_MONTAÑA -> "zona de montaña";
            case RUTA_FLUVIAL -> "ruta fluvial";
            case PLAYA_DEPORTIVA -> "playa deportiva";
            case ZONA_VOLEY_PLAYA -> "zona de vóley playa";
            case ZONA_MULTIDEPORTE -> "zona multideporte";
            case PARQUE_PUBLICO -> "parque público";
            case PISCINA_PUBLICA -> "piscina pública";
            case ZONA_REMAR -> "zona para remar";
        };
    }

    private String traducirOpeningHours(String openingHours) {
        if (openingHours == null || openingHours.isBlank()) {
            return "";
        }

        String texto = openingHours;

        texto = texto.replaceAll("\\bMo\\b", "Lun");
        texto = texto.replaceAll("\\bTu\\b", "Mar");
        texto = texto.replaceAll("\\bWe\\b", "Mié");
        texto = texto.replaceAll("\\bTh\\b", "Jue");
        texto = texto.replaceAll("\\bFr\\b", "Vie");
        texto = texto.replaceAll("\\bSa\\b", "Sáb");
        texto = texto.replaceAll("\\bSu\\b", "Dom");

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

        texto = texto.replaceAll("\\bPH\\b", "Festivos");
        texto = texto.replaceAll("\\bSH\\b", "Vacaciones escolares");
        texto = texto.replaceAll("\\boff\\b", "cerrado");
        texto = texto.replace("24/7", "24 horas");

        return texto;
    }

    private List<String> separarValores(String texto) {
        List<String> valores = new ArrayList<>();

        if (texto == null || texto.isBlank()) {
            return valores;
        }

        String[] partes = texto.toLowerCase().split("[;,]");

        for (String parte : partes) {
            String limpia = parte.trim();
            if (!limpia.isBlank()) {
                valores.add(limpia);
            }
        }

        return valores;
    }

    private boolean contieneDeporte(List<String> deportes, String... buscados) {
        for (String buscado : buscados) {
            if (deportes.contains(buscado.toLowerCase())) {
                return true;
            }
        }
        return false;
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