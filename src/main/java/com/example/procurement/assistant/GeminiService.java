package com.example.procurement.assistant;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Base64;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model :gemini-3.5-flash-lite}")
    private String model;

    private final RestClient restClient = RestClient.create();

    public String ask(String prompt) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent";

        Map<String, Object> body = Map.of(
            "contents", List.of(Map.of("parts", List.of(Map.of("text", prompt))))
        );

        Map<?, ?> response = restClient.post()
                .uri(url)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(Map.class);

        List<?> candidates = (List<?>) response.get("candidates");
        Map<?, ?> content = (Map<?, ?>) ((Map<?, ?>) candidates.get(0)).get("content");
        List<?> parts = (List<?>) content.get("parts");
        return (String) ((Map<?, ?>) parts.get(0)).get("text");
    }
            public String askWithFile(String prompt, byte[] fileBytes, String mimeType) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent";
        String base64 = Base64.getEncoder().encodeToString(fileBytes);

        Map<String, Object> body = Map.of(
            "contents", List.of(Map.of("parts", List.of(
                Map.of("inline_data", Map.of("mime_type", mimeType, "data", base64)),
                Map.of("text", prompt)
            )))
        );

        Map<?, ?> response = restClient.post()
                .uri(url)
                .header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(Map.class);

        List<?> candidates = (List<?>) response.get("candidates");
        Map<?, ?> content = (Map<?, ?>) ((Map<?, ?>) candidates.get(0)).get("content");
        List<?> parts = (List<?>) content.get("parts");
        return (String) ((Map<?, ?>) parts.get(0)).get("text");
    
    }
}