package com.example.procurement.assistant;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiTestController {

    private final GeminiService gemini;

    public AiTestController(GeminiService gemini) {
        this.gemini = gemini;
    }

        @GetMapping("/ai-test")
    public String test(@RequestParam String q) {
        try {
            return gemini.ask(q);
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}