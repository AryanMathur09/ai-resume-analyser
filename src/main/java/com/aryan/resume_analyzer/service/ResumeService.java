package com.aryan.resume_analyzer.service;

import com.aryan.resume_analyzer.dto.ResumeAnalysisResult;
import com.aryan.resume_analyzer.dto.ResumeResponse;
import com.aryan.resume_analyzer.model.Resume;
import com.aryan.resume_analyzer.model.User;
import com.aryan.resume_analyzer.repository.ResumeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final FileStorageService fileStorageService;
    private final PdfTextExtractor pdfTextExtractor;
    private final ResumeAnalysisService resumeAnalysisService;

    public ResumeResponse uploadResume(MultipartFile file, User user,String jobDescription) throws Exception {

        String path = fileStorageService.storeFile(file);

        String extractedText = pdfTextExtractor.extractText(path);

        var analysis = resumeAnalysisService.analyze(extractedText,jobDescription);

        Resume resume = Resume.builder()
                .fileName(file.getOriginalFilename())
                .filePath(path)
                .extractedText(extractedText)
                .resumeScore(analysis.getScore())
                .skillsFound(String.join("," ,analysis.getSkillsFound()))
                .missingSkills(String.join("," , analysis.getMissingSkills()))
                .uploadedAt(LocalDateTime.now())
                .user(user)
                .build();
        Resume saved = resumeRepository.save(resume);

        return mapToResponse(saved,analysis);
    }
    private ResumeResponse mapToResponse(Resume resume, ResumeAnalysisResult analysisResult) {

        return ResumeResponse.builder()
                .resumeId(resume.getId())
                .fileName(resume.getFileName())
                .resumeScore(resume.getResumeScore())
                .skillsFound(analysisResult.getSkillsFound())
                .missingSkills(analysisResult.getMissingSkills())
                .summary(analysisResult.getSummary())
                .uploadedAt(resume.getUploadedAt())
                .build();
    }
}
