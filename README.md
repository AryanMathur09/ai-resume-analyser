- AI Resume Analyzer

An AI-powered Resume Analyzer built using Java 21 and Spring Boot that evaluates resumes against job descriptions, extracts skills, and provides ATS-style insights to help recruiters and candidates make better decisions.
 
Project Status: Actively under development (Ongoing)

- Problem Statement

Recruiters often receive hundreds of resumes for a single job role, making manual screening inefficient and error-prone. Candidates, on the other hand, struggle to understand why their resumes get rejected by ATS systems.

This project aims to automate resume screening using AI-based parsing and analysis techniques.

- Features (Current & Planned)
* Implemented

Secure authentication using JWT

RESTful API architecture

Modular backend structure (Controller–Service–Repository)

Resume upload support (PDF/DOC – configurable)

- In Progress / Planned

Resume parsing & skill extraction

Job Description vs Resume matching

ATS-style scoring system

Keyword gap analysis

Admin & recruiter roles

Downloadable analysis report

* Tech Stack

Backend

Java 21

Spring Boot 3.x

Spring Security + JWT

Maven

Database

MySQL / PostgreSQL (configurable)

AI / Processing

Resume text extraction (PDF/DOC)

NLP-based keyword matching (planned)

Tools

Git & GitHub

Postman

IntelliJ IDEA

📂 Project Structure
ai-resume-analyser/
├── src/main/java/
│   └── com/yourpackage/resumeanalyzer
│       ├── controller
│       ├── service
│       ├── repository
│       ├── security
│       ├── dto
│       └── model
├── src/main/resources/
│   ├── application-example.yml
├── uploads/
├── pom.xml
└── README.md

🔐 Configuration & Security

Sensitive configuration files are not committed to the repository.

- API Testing

APIs can be tested using:

Postman

REST Client

JWT-protected endpoints require authentication.

- Why This Project Matters

Demonstrates real-world backend development

Uses modern Java (21) and Spring Boot

Shows understanding of security, modularity, and scalability

Designed as a production-grade system, not a toy project

* Future Enhancements

AI-based resume ranking

Resume improvement suggestions

Frontend dashboard (React)

Cloud deployment (AWS)

- Author

Aryan Mathur
B.Tech | Backend Developer (Java & Spring Boot)
GitHub: https://github.com/AryanMathur09

- License

This project is licensed under the MIT License.