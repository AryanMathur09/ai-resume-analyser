package com.aryan.resume_analyzer.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import java.io.*;
import  java.io.IOException;


@Service
public class PdfTextExtractor {
    public String extractText(String filePath) throws IOException {
        try(PDDocument document=PDDocument.load(new File(filePath))) {

            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            return text.replaceAll("\\s+", " ").trim();
        }
    }
}
