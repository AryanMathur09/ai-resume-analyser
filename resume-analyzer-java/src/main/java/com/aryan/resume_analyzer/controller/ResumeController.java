package com.aryan.resume_analyzer.controller;

import com.aryan.resume_analyzer.dto.ResumeResponse;
import com.aryan.resume_analyzer.model.Resume;
import com.aryan.resume_analyzer.model.User;
import com.aryan.resume_analyzer.repository.UserRepository;
import com.aryan.resume_analyzer.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final UserRepository userRepository;

    @PostMapping("/upload")
    public ResumeResponse uploadResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("jobDescription") String jobDescription,
            Authentication authentication) throws Exception {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return resumeService.uploadResume(file, user ,jobDescription);

    }
}
