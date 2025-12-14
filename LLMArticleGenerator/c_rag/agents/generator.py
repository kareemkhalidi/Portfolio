from langchain import hub
from langchain_core.output_parsers import StrOutputParser
from langchain_huggingface import HuggingFacePipeline
from c_rag.constants import LLM_MODEL
import os


'''Generates an article based on the provided documents.'''

# pull prompt from langsmith
'''
You are an assistant for sports news article writing tasks.
Use the following pieces of retrieved context to write an article
about the provided prompt. Give the article a title and make it
as short or as long as necessary to include all information in the prompt
and any necessary details. If you cannot write an article about the provided
prompt, just say that you cannot write an article.
Prompt: {prompt} 
Context: {context} 
Generation:
'''
os.environ['LANGCHAIN_API_KEY'] = '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'
prompt = hub.pull('sports-article-generator-rag-prompt:26bb3220')

# build llm using expected output format and build a pipe from the prompt to the llm
llm = HuggingFacePipeline.from_model_id(
    model_id=LLM_MODEL,
    task='text-generation'
)
generator = prompt | llm | StrOutputParser()
