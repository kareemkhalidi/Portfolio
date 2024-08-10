from typing import Any, Dict
from c_rag.agents.retrieval_grader import retrieval_grader
from c_rag.state import State


# Determines whether the retrieved documents are relevant to the question
# If any document is not relevant, set a flag to run web search.
def grade_documents(state: State) -> Dict[str, Any]:
    print('Grading documents...')

    # get necessary variables from state
    prompt = state['prompt']
    documents = state['documents']

    # iterate through documents
    filtered_docs = []
    web_search = False
    for doc in documents:
        # score each document
        print(prompt)
        print(doc)
        score = retrieval_grader.invoke(
            {'prompt': prompt, 'document': doc}
        )

        # if relevant, add to filtered docs
        if score.lower() == 'yes':
            print('  Relevant')
            filtered_docs.append(doc)

        # if any not relevant found, do not add to filter docs and indicate need for a web search
        else:
            print('  Not Relevant')
            web_search = True
            continue

    # return the filtered documents and updated web search state
    return {'documents': filtered_docs, 'prompt': prompt, 'web_search': web_search}
