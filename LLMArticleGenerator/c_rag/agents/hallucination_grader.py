from langchain_core.prompts import ChatPromptTemplate
from langchain_core.pydantic_v1 import BaseModel, Field
from langchain_huggingface import HuggingFacePipeline
from c_rag.constants import LLM_MODEL


'''Checks an LLM generated article for hallucinations.'''

# build expected output format
class GradeHallucinations(BaseModel):
    '''Binary score for whether an LLM generated article has any hallucinations.'''

    binary_score: bool = Field(
        description='Article is grounded in the facts, "yes" or "no"'
    )

# build prompt
prompt = ChatPromptTemplate.from_messages(
    [
        ('system', '''You are a grader assessing whether an LLM generated article is grounded in / supported by a set of retrieved facts.\n
                Give a binary score "yes" or "no". "Yes" means that the article is grounded in / supported by the set of facts. If you
                are unsure, just say "no".'''),
        ('human', 'Set of facts: \n\n {documents} \n\n LLM generated article: {article}'),
    ]
)

# build llm using expected output format and build a pipe from the prompt to the llm
llm = HuggingFacePipeline.from_model_id(
    model_id=LLM_MODEL,
    task='text-generation'
)#.with_structured_output(GradeHallucinations)
hallucination_grader = prompt | llm
