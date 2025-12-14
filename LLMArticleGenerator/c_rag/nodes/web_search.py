from typing import Any, Dict
from langchain.schema import Document
from langchain_community.tools.tavily_search import TavilySearchResults
from c_rag.state import State
from c_rag.agents.web_search_builder import web_search_builder
import os


os.environ['TAVILY_API_KEY'] = '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'

web_search_tool = TavilySearchResults(k=3)

# Performs a web search to get relevant documents for the prompt
def web_search(state: State) -> Dict[str, Any]:
    print('Searching the web...')

    # get necessary variables from state
    prompt = state['prompt']
    documents = state['documents']

    # build a web search query and then perform the web search
    query = web_search_builder.invoke({'prompt': prompt})
    docs = web_search_tool.invoke({"query": query})

    # add the web results to the existing list of documents
    web_results = "\n".join([d["content"] for d in docs])
    web_results = Document(page_content=web_results)
    if documents is not None:
        documents.append(web_results.page_content)
    else:
        documents = [web_results]
    return {"documents": documents, "prompt": prompt}
