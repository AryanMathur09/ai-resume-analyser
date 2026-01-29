package com.aryan.resume_analyzer.service;

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

    public Resume uploadResume(MultipartFile file, User user) throws Exception {

        String path = fileStorageService.storeFile(file);

        Resume resume = Resume.builder()
                .fileName(file.getOriginalFilename())
                .filePath(path)
                .uploadedAt(LocalDateTime.now())
                .user(user)
                .build();

        return resumeRepository.save(resume);
    }
}
