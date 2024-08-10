import sys
from langchain_community.document_loaders import WebBaseLoader
from langchain.text_splitter import RecursiveCharacterTextSplitter
from langchain_huggingface import HuggingFaceEmbeddings
from langchain_chroma import Chroma
from c_rag.constants import EMBEDDING_MODEL

'''
Ingests a url into the vectorstore.

Usage:
  python ingest_urls_helper.py [URL] [chunk size] [chunk overlap]

Parameters:
  [URL]: The URL to ingest.

  -s[chunk size]: The size that each chunk should be when splitting the URL.

  -o[chunk overlap]: The chunk size to use when splitting the URL.
'''

# load docs from url
docs = WebBaseLoader(sys.argv[1]).load()

# split docs
text_splitter = RecursiveCharacterTextSplitter.from_tiktoken_encoder(
    chunk_size=int(sys.argv[2]), chunk_overlap=int(sys.argv[3])
)
doc_splits = text_splitter.split_documents(docs)

print()
print()
print('docs:')
for doc in docs:
    print(doc)
    print()
print()
print()
print('doc splits:')
for doc in doc_splits:
    print(doc)
    print()

# build embedding function
embedding_func = HuggingFaceEmbeddings(model_name=EMBEDDING_MODEL)

# create/load vectorstore and add documents
vs = Chroma(
    collection_name='nfl_data',
    embedding_function=embedding_func,
    persist_directory='./.nfl_data'
)

# add documents to vectorstore
vs.add_documents(docs)
