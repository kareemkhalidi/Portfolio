from langchain_huggingface import HuggingFaceEmbeddings
from langchain_chroma import Chroma
from c_rag.constants import EMBEDDING_MODEL


'''Retrieves relevant documents from the vectorstore.'''

# build an embedding model
embedding_func = HuggingFaceEmbeddings(model_name=EMBEDDING_MODEL)

# build the retriever
retriever = Chroma(
    collection_name='nfl_data',
    embedding_function=embedding_func,
    persist_directory='./.nfl_data'
).as_retriever()
