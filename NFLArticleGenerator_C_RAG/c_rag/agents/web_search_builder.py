from langchain_core.prompts import ChatPromptTemplate
from langchain_core.output_parsers import StrOutputParser
from langchain_huggingface import HuggingFacePipeline
from c_rag.constants import LLM_MODEL


'''Builds a web search query that can be searched to gain more information about the prompt'''

# build prompt
prompt = ChatPromptTemplate.from_messages(
    [
        ('system', '''You are a researcher tasked with writing a simple web search query to get more information about an article prompt.\n
                Given the prompt, provide a web search query that will lead to information about the prompt.'''),
        ('human', 'Prompt: {prompt}'),
    ]
)

# build llm using expected output format and build a pipe from the prompt to the llm
llm = HuggingFacePipeline.from_model_id(
    model_id=LLM_MODEL,
    task='text-generation'
)
web_search_builder = prompt | llm | StrOutputParser
