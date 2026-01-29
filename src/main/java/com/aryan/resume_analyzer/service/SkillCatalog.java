package com.aryan.resume_analyzer.service;

import java.util.List;
import java.util.Map;

public class SkillCatalog {

    public static final Map<String, List<String>> SKILL_CATEGORIES = Map.of(
            "Backend", List.of("java", "spring", "spring boot", "hibernate", "jwt", "rest", "api"),
            "Database", List.of("sql", "mysql", "mongodb", "postgresql"),
            "Frontend", List.of("javascript", "react", "html", "css"),
            "DevOps", List.of("docker", "kubernetes", "aws", "ci/cd")
    );
}
