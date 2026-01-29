package com.aryan.resume_analyzer.dto;
import lombok.*;
import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ResumeAnalysisResult {

    private int score;
    private List<String> skillsFound;
    private List<String> missingSkills;
    private String summary;
}

