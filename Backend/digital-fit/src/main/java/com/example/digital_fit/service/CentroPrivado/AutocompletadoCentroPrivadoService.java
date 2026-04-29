package com.example.digital_fit.service.CentroPrivado;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        dto.setDatosEncontrados(false);

        try {
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
                return dto;
            }

            JsonNode root = objectMapper.readTree(response.body());

            if (root == null || root.isMissingNode() || root.isEmpty()) {
                return dto;
            }

            JsonNode address = root.path("address");
            JsonNode extratags = root.path("extratags");

            String direccionCompuesta = construirDireccion(address, root.path("display_name").asText(""));
            String telefono = primerValorNoVacio(
                    extratags.path("phone").asText(""),
                    extratags.path("contact:phone").asText(""));
            String horarioOriginal = primerValorNoVacio(
                    extratags.path("opening_hours").asText(""),
                    extratags.path("opening_hours:description").asText(""));
            String horarioTraducido = traducirOpeningHours(horarioOriginal);
            Double precioMensual = extraerPrecioMensual(extratags);
            String descripcion = construirDescripcion(root, address);

            dto.setDireccion(direccionCompuesta);
            dto.setTelefono(telefono);
            dto.setHorario(horarioTraducido);
            dto.setPrecioMensual(precioMensual);
            dto.setDescripcion(descripcion);
            dto.setDatosEncontrados(direccionCompuesta != null && !direccionCompuesta.isBlank());

            return dto;

        } catch (IOException | InterruptedException e) {
            return dto;
        }
    }

    private String construirDireccion(JsonNode address, String displayName) {
        String road = primerValorNoVacio(
                address.path("road").asText(""),
                address.path("pedestrian").asText(""),
                address.path("footway").asText(""),
                address.path("path").asText(""),
                address.path("cycleway").asText(""));

        String houseNumber = address.path("house_number").asText("");
        String postcode = address.path("postcode").asText("");
        String city = primerValorNoVacio(
                address.path("city").asText(""),
                address.path("town").asText(""),
                address.path("village").asText(""),
                address.path("municipality").asText(""),
                address.path("suburb").asText(""));

        String state = address.path("state").asText("");
        String country = address.path("country").asText("");

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

    private String construirDescripcion(JsonNode root, JsonNode address) {
        String tipo = root.path("type").asText("");
        String city = primerValorNoVacio(
                address.path("city").asText(""),
                address.path("town").asText(""),
                address.path("village").asText(""),
                address.path("municipality").asText(""),
                address.path("suburb").asText(""));

        if (!tipo.isBlank() && !city.isBlank()) {
            return "Ubicación detectada automáticamente cerca de " + city + " (" + tipo.replace('_', ' ') + ").";
        }

        if (!city.isBlank()) {
            return "Ubicación detectada automáticamente cerca de " + city + ".";
        }

        return "";
    }

    private Double extraerPrecioMensual(JsonNode extratags) {
        String charge = primerValorNoVacio(
                extratags.path("charge").asText(""),
                extratags.path("charge:conditional").asText(""));

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

        texto = texto.replaceAll("\\bMo\\b", "Lun");
        texto = texto.replaceAll("\\bTu\\b", "Mar");
        texto = texto.replaceAll("\\bWe\\b", "Mié");
        texto = texto.replaceAll("\\bTh\\b", "Jue");
        texto = texto.replaceAll("\\bFr\\b", "Vie");
        texto = texto.replaceAll("\\bSa\\b", "Sáb");
        texto = texto.replaceAll("\\bSu\\b", "Dom");

        texto = texto.replaceAll("\\bPH\\b", "Festivos");
        texto = texto.replaceAll("\\bSH\\b", "Vacaciones escolares");
        texto = texto.replaceAll("\\boff\\b", "cerrado");

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