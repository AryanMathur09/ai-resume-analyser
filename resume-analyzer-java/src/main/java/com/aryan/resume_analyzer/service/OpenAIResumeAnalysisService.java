package com.aryan.resume_analyzer.service;

import com.aryan.resume_analyzer.dto.ResumeAnalysisResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary; // Needed for @Primary
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Primary // This tells Spring to use THIS service instead of the Mock one
@Service
@RequiredArgsConstructor
public class OpenAIResumeAnalysisService implements ResumeAnalysisService { // Fixed: implement the Service, not the DTO

    private WebClient webClient;

    @PostConstruct
    public void init() {
        // Now pointing to your local Python FastAPI Microservice
        this.webClient = WebClient.builder()
                .baseUrl("http://127.0.0.1:8000")
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Override
    public ResumeAnalysisResult analyze(String resumeText, String jobDescription) {
        // Prepare the request body to match the Python 'AnalysisRequest' Pydantic model
        Map<String, String> requestBody = Map.of(
                "resume_text", resumeText,
                "job_description", jobDescription
        );

        return webClient.post()
                .uri("/analyze")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(ResumeAnalysisResult.class)
                .block(); 
    }
}