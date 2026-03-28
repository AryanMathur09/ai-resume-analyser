import os
from fastapi import FastAPI
from pydantic import BaseModel, Field
from typing import List
from dotenv import load_dotenv
from langchain_google_genai import ChatGoogleGenerativeAI
from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import JsonOutputParser

load_dotenv()
api_key = os.getenv("google_api_key")

app = FastAPI()

# Define the structured output format for the LLM
class AnalysisResponse(BaseModel):
    score: int = Field(description="ATS score from 0 to 100")
    skillsFound: List[str] = Field(description="List of matching skills")
    missingSkills: List[str] = Field(description="List of skills required but not found")
    summary: str = Field(description="A 2-sentence professional feedback summary")

parser = JsonOutputParser(pydantic_object=AnalysisResponse)

llm = ChatGoogleGenerativeAI(
    model="gemini-2.5-flash", 
    google_api_key=api_key, 
    temperature=0
)

@app.post("/analyze", response_model=AnalysisResponse)
async def analyze_resume(request: dict):
    resume_text = request.get("resume_text")
    jd = request.get("job_description")

    prompt = ChatPromptTemplate.from_template(
        "You are an expert Technical Recruiter.\n"
        "Analyze the following Resume against the Job Description.\n"
        "{format_instructions}\n"
        "Resume: {resume}\n"
        "JD: {jd}"
    )

    chain = prompt | llm | parser

    result = chain.invoke({
        "resume": resume_text,
        "jd": jd,
        "format_instructions": parser.get_format_instructions()
    })
    
    return result