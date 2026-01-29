package com.aryan.resume_analyzer.service;

import com.aryan.resume_analyzer.dto.ResumeAnalysisResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Primary;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class OpenAIResumeAnalysisService implements ResumeAnalysisService {
    @Value("${openai.api.key")
    private String apiKey;

    private WebClient webClient;

    @PostConstruct
            public void init() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1/responses")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
    @Override
    public ResumeAnalysisResult analyze(String resumeText, String jobDescription) {

        String prompt = """
                    You are an AI resume screening system.
                
                    Analyze the resume against the job description.
                    Respond ONLY in valid JSON with these fields:
                    {
                        "score": number (0-100), 
                        "skillsFound": [string],
                        "missingSkills": [string],
                        "summary": string
                }
                
                Resume:
                %s
                
                Job Description:
                %s
                """.formatted(resumeText, jobDescription);

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4.1-mini",
                "input", List.of(
                        Map.of(
                                "role", "user",
                                "content", List.of(
                                        Map.of(
                                                "type", "text",
                                                "text", prompt
                                        )
                                )
                        )
                )
        );


        Map response = webClient.post()
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();



        return parseResponse(response);

    }
    private ResumeAnalysisResult parseResponse(Map response) {

        var output = (List<Map<String, Object>>) response.get("output");
        var content = (List<Map<String, Object>>) output.get(0).get("content");
        String json = (String) content.get(0).get("text");

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, ResumeAnalysisResult.class);
        } catch (Exception e) {
            throw new RuntimeException("AI response parsing failed", e);
        }
    }

}
