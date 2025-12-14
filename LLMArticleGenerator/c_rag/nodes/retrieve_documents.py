from typing import Any, Dict
from c_rag.agents.retriever import retriever
from c_rag.state import State


# Retrieves relevant documents from the vectorstore.
def retrieve_documents(state: State) -> Dict[str, Any]:
    print('Retrieving documents...')

    # run the retriever and return the retrieved documents
    prompt = state['prompt']
    documents = retriever.invoke(prompt)
    return {'documents': documents, 'question': prompt}
