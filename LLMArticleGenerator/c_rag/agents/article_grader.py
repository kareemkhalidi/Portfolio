from langchain_core.prompts import ChatPromptTemplate
from langchain_core.pydantic_v1 import BaseModel, Field
from langchain_huggingface import HuggingFacePipeline
from c_rag.constants import LLM_MODEL


'''Checks whether an article fully responds to a prompt.'''

# build expected output format
class GradeArticle(BaseModel):
    """Binary score for whether an LLM generated article fully responds to a prompt."""

    binary_score: bool = Field(
        description='Article fully responds to the prompt, "yes" or "no"'
    )

# build prompt
prompt = ChatPromptTemplate.from_messages(
    [
        ('system', '''You are a grader assessing whether an LLM generated article fully responds to a prompt. \n
                    Give a binary score "yes" or "no". "Yes" means that the article fully responds to the prompt.
                    If you are unsure, just say "no".'''),
        ('human', 'Prompt: \n\n {prompt} \n\n LLM generated article: {article}'),
    ]
)

# build llm using expected output format and build a pipe from the prompt to the llm
llm = HuggingFacePipeline.from_model_id(
    model_id=LLM_MODEL,
    task='text-generation'
)#.with_structured_output(GradeArticle)
article_grader = prompt | llm
