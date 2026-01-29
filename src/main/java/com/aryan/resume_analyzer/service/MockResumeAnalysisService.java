package com.aryan.resume_analyzer.service;

import com.aryan.resume_analyzer.dto.ResumeAnalysisResult;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Primary
public class MockResumeAnalysisService implements ResumeAnalysisService {

    private static final List<String> COMMON_SKILLS = List.of(
            "java", "sping", "spring boot", "hibernate", "sql",
            "mysql", "rest", "jwt", "api",
            "react", "javascript", "html", "css"
    );

    @Override
    public ResumeAnalysisResult analyze(String resumeText, String jobDescription) {

        String resume = resumeText.toLowerCase();
        String jd = jobDescription.toLowerCase();

        List<String> found = new ArrayList<>();
        List<String> missing = new ArrayList<>();

        int score = 0;

        for (var entry : SkillCatalog.SKILL_CATEGORIES.entrySet()) {
            for (String skill : entry.getValue()) {

                boolean inJD = jd.contains(skill);
                boolean inResume = resume.contains(skill);

                if (inResume) {
                    found.add(skill);
                    score += inJD ? 10 : 4; // weighted scoring
                }

                if (inJD && !inResume) {
                    missing.add(skill);
                }
            }
        }

        score = Math.min(score, 100);

        String summary = missing.isEmpty()
                ? "Strong match for the given job description."
                : "Resume is missing some job-specific skills: " + String.join(", ", missing);

        return ResumeAnalysisResult.builder()
                .score(score)
                .skillsFound(found)
                .missingSkills(missing)
                .summary(summary)
                .build();
    }

}