from typing import Any, Dict
from c_rag.agents.generator import generator
from c_rag.state import State


# Generates an article about the provided prompt using the provided documents.
def generate(state: State) -> Dict[str, Any]:
    print('Generating...')

    # get necessary variables from state
    prompt = state['prompt']
    documents = state['documents']

    # run the generator and return the result
    generation = generator.invoke({'prompt': prompt, 'context': documents})
    return {'documents': documents, 'prompt': prompt, 'generation': generation}
