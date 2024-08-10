from c_rag.constants import WEB_SEARCH, GENERATE


# Decides whether a web search is necessary.
def decide_to_generate(state):
    print("Assessing graded documents...")

    if state['web_search']:
        print(
            '  Not all documents are relevant to the prompt, include web search.'
        )
        return WEB_SEARCH
    else:
        print('  All documents are relevant to the prompt, generate an article.')
        return GENERATE
