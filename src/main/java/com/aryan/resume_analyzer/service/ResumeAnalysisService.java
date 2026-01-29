package com.aryan.resume_analyzer.service;
import com.aryan.resume_analyzer.dto.ResumeAnalysisResult;

public interface ResumeAnalysisService {
    ResumeAnalysisResult analyze(String resumeText,String jobDescription);
}
