from langgraph.graph import END, StateGraph
from c_rag.nodes.decide_to_generate import decide_to_generate
from c_rag.nodes.generate import generate
from c_rag.nodes.grade_article_grounded_in_documents_and_question import grade_article_grounded_in_documents_and_question
from c_rag.nodes.grade_documents import grade_documents
from c_rag.nodes.retrieve_documents import retrieve_documents
from c_rag.nodes.web_search import web_search
from c_rag.state import State
from c_rag.constants import RETRIEVE, GRADE_DOCUMENTS, GENERATE, WEB_SEARCH


'''The C-Rag graph connecting the nodes'''

# add the nodes to the graph
nfl_article_generator = StateGraph(State)
nfl_article_generator.add_node(RETRIEVE, retrieve_documents)
nfl_article_generator.add_node(GRADE_DOCUMENTS, grade_documents)
nfl_article_generator.add_node(GENERATE, generate)
nfl_article_generator.add_node(WEB_SEARCH, web_search)

# build the edges of the graph
nfl_article_generator.set_entry_point(RETRIEVE)
nfl_article_generator.add_edge(RETRIEVE, GRADE_DOCUMENTS)
nfl_article_generator.add_conditional_edges(
    GRADE_DOCUMENTS,
    decide_to_generate,
    {
        WEB_SEARCH: WEB_SEARCH,
        GENERATE: GENERATE,
    },
)
nfl_article_generator.add_edge(WEB_SEARCH, GENERATE)
nfl_article_generator.add_conditional_edges(
    GENERATE,
    grade_article_grounded_in_documents_and_question,
    {
        "not supported": GENERATE,
        "useful": END,
        "not useful": WEB_SEARCH,
    },
)

# compile the C-RAG
nfl_article_generator = nfl_article_generator.compile()
nfl_article_generator.get_graph().draw_mermaid_png(output_file_path="graph.png")
