from langchain_core.prompts import ChatPromptTemplate
from langchain_huggingface import HuggingFacePipeline
from langchain_core.pydantic_v1 import BaseModel, Field
from c_rag.constants import LLM_MODEL


'''Grades relevance of documents to an article prompt.'''

# build expected output format
class GradeDocuments(BaseModel):
    '''Binary score for relevance check on retrieved documents.'''

    binary_score: bool = Field(
        description='Documents are relevant to the article prompt, "yes" or "no"'
    )

# build prompt
prompt = ChatPromptTemplate.from_messages(
    [
        ('system', '''You are a grader assessing relevance of a retrieved document to an article prompt. \n 
                If the document contains keyword(s) or semantic meaning related to the article prompt, grade it as relevant. \n
                Give a binary score "yes" or "no" score to indicate whether the document is relevant to the article prompt.
                "Yes" means the document is relevant to the prompt. If you are unsure, then just respond "no".'''),
        ('human', 'Retrieved document: \n\n {document} \n\n Prompt: {prompt}'),
    ]
)

# build llm using expected output format and build a pipe from the prompt to the llm
llm = HuggingFacePipeline.from_model_id(
    model_id=LLM_MODEL,
    task='text-generation'
)#.with_structured_output(GradeDocuments)
retrieval_grader = prompt | llm
