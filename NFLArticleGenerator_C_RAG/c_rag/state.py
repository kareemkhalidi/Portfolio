from typing import List, TypedDict


class State(TypedDict):
    '''
    Represents the state of the C-RAG.

    Attributes:
        prompt: prompt
        generation: LLM generated article
        web_search: whether to add search
        documents: list of documents
    '''

    prompt: str
    generation: str
    web_search: bool
    documents: List[str]
