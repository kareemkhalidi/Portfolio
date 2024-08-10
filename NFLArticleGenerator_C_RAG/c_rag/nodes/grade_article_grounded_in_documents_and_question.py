from c_rag.agents.hallucination_grader import hallucination_grader
from c_rag.agents.article_grader import article_grader
from c_rag.state import State


# Verifies that generated article has no hallucinations and fully responds to the prompt.
def grade_article_grounded_in_documents_and_question(state: State) -> str:
    print("Checking article for hallucinations...")

    # get necessary variables from state
    prompt = state['prompt']
    documents = state['documents']
    generation = state['generation']

    # check for hallucinations
    score = hallucination_grader.invoke(
        {'documents': documents, 'generation': generation}
    )

    if score.lower() == 'yes':
        # check that article responds to prompt
        print('  Article does not contain any hallucinations.')
        print('Checking if article fully responds to prompt...')
        score = article_grader.invoke({'prompt': prompt, 'article': generation})
        if score.lower() == 'yes':
            print('  Article fully responds to prompt')
            return 'useful'
        else:
            print('  Article does not fully respond to prompt. Generate a new article.')
            return 'not useful'
    else:
        print('  Article contains hallucinations. Generate a new article.')
        return 'not supported'
