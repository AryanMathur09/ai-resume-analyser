package com.aryan.resume_analyzer.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResumeResponse {

    private Long resumeId;
    private String fileName;
    private int resumeScore;

    private List<String> skillsFound;
    private List<String> missingSkills;

    private String summary;
    private LocalDateTime uploadedAt;
}
