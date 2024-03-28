/* File: tq.h
 * Author: Kareem Khalidi
 * Class: CSC 352
 *
 * Tq.h is a header file for the tree question decision tree. Tq.h contains the structs and function prototypes for
 * the tree question decision tree. Tq.h also contains the function prototypes for the functions that build and
 * populate the tree. Tq.h also contains the function prototypes for the functions that print and free the tree.
 * */

typedef struct TQDecisionTreeNode {
    char text[50];
    int num_answers;
    char** answers;
    struct TQDecisionTreeNode* yes;
    struct TQDecisionTreeNode* no;
} TQDecisionTreeNode;

typedef struct TQDecisionTree {
    TQDecisionTreeNode *root;
} TQDecisionTree;

/* Prints the tree.
 *
 * @param root The root of the tree to print.
 * */
void TQ_print_tree(TQDecisionTree* root);

/* Builds a tree from a file.
 *
 * @param file_name The name of the file to build the tree from.
 * @return The tree built from the file.
 * */
TQDecisionTree* TQ_build_tree(char* file_name);

/* Populates the answers of a tree.
 *
 * @param tree The tree to populate.
 * @param file_name The name of the file to populate the tree from.
 * */
void TQ_populate_tree(TQDecisionTree* tree, char* file_name);

/* Frees the memory allocated to a tree.
 *
 * @param tree The tree to free.
 * */
void TQ_free_tree(TQDecisionTree* tree);