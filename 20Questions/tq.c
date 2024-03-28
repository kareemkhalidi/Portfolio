/* File: tq.c
 * Author: Kareem Khalidi
 * Class: CSC 352
 *
 * Tq.c is a source file for the tree question decision tree. Tq.c contains the functions that build and populate the tree.
 * Tq.c also contains the functions that print and free the tree, as well as the recursive helper functions for the print
 * and free functions, and the recursive helper function for the build function (TQ_fill_tree_level).
 * */

#include "tq.h"
#include "stdio.h"
#include "malloc.h"

/* Recursive helper function for TQ_build_tree.
 *
 * @param root The root node of the tree to build.
 * @param depth The depth of the tree at this iteration.
 * @param yesno Whether the node is a yes or no node.
 * */
void TQ_print_tree_recursive(TQDecisionTreeNode* root, int depth, int yesno){
    //if the node is the root, print the text and then recursively call the yes and no nodes
    if(depth == 0){
        printf("[%s]\n", root->text);
        if(root->yes != NULL){
            TQ_print_tree_recursive(root->yes, depth + 1, 1);
        }
        if(root->no != NULL){
            TQ_print_tree_recursive(root->no, depth + 1, 0);
        }
    }
    //if the node is an answer node, print the correct number of indents followed by either the y or n arrow and the answers
    else if(root->num_answers != 0){
        for(int i = 0; i < depth - 1; i++){
            printf("    ");
        }
        if(yesno == 1){
            printf("-y-> ");
        }
        else{
            printf("-n-> ");
        }
        for(int i = 0; i < root->num_answers; i++){
            printf("| %s ", root->answers[i]);
        }
        printf("|\n");
    }
    //if the node is a question node, print the correct number of indents followed by either the y or n arrow
    //then print the question and then recursively call the yes and no nodes
    else{
        for(int i = 0; i < depth - 1; i++){
            printf("    ");
        }
        if(yesno == 1){
            printf("-y-> ");
        }
        else{
            printf("-n-> ");
        }
        if(root->text[0] != '\0'){
            printf("[%s]\n", root->text);
        }
        else{
            printf("\n");
        }
        if(root->yes != NULL){
            TQ_print_tree_recursive(root->yes, depth + 1, 1);
        }
        if(root->no != NULL){
            TQ_print_tree_recursive(root->no, depth + 1, 0);
        }
    }
}

/* Fills the layer of depth "level" in the tree with nodes containing the question in "question".
 * ex: if level is 2, then all nodes at depth 2 will be filled with the question.
 *
 * @param tree The tree to fill.
 * @param cur The current node.
 * @param question The question to fill the tree with.
 * @param level The level to fill the tree with.
 * */
void TQ_fill_tree_level(TQDecisionTree* tree, TQDecisionTreeNode* cur, char* question, int level){
    //if the node is the correct level, fill it with the question, set all the default values for the node,
    //allocate space for the next layer, and set the default values for the next layer
    if(level == 0){
        int i = 0;
        while(question[i] != '\0'){
            cur->text[i] = question[i];
            i++;
        }
        cur->text[i] = '\0';
        cur->num_answers = 0;
        cur->yes = malloc(sizeof(TQDecisionTreeNode));
        cur->yes->text[0] = '\0';
        cur->yes->num_answers = 0;
        cur->yes->answers = NULL;
        cur->yes->yes = NULL;
        cur->yes->no = NULL;
        cur->no = malloc(sizeof(TQDecisionTreeNode));
        cur->no->text[0] = '\0';
        cur->no->num_answers = 0;
        cur->no->answers = NULL;
        cur->no->yes = NULL;
        cur->no->no = NULL;
    }
    //if the node is not the correct level, recursively call the yes and no nodes
    else{
        TQ_fill_tree_level(tree, cur->yes, question, level - 1);
        TQ_fill_tree_level(tree, cur->no, question, level - 1);
    }
}

/* Recursive helper function for TQ_free_tree.
 *
 * @param root The root node of the tree to free.
 * */
void TQ_free_tree_recursive(TQDecisionTreeNode* root){
    //if the node is not a leaf, recursively call the yes and no nodes
    if(root->yes != NULL){
        TQ_free_tree_recursive(root->yes);
    }
    if(root->no != NULL){
        TQ_free_tree_recursive(root->no);
    }
    //free the current nodes memory
    for(int i = 0; i < root->num_answers; i++){
        free(root->answers[i]);
    }
    if(root->num_answers != 0){
        free(root->answers);
    }
    free(root);
}

void TQ_print_tree(TQDecisionTree* root){
    TQ_print_tree_recursive(root->root, 0, 0);
}

TQDecisionTree* TQ_build_tree(char* file_name){
    //open the file
    FILE* file = fopen(file_name, "r");
    //allocate memory for the questions and fill it with the questions from the file
    char* questions = malloc(1100 * sizeof(char));
    fgets(questions, 1100, file);
    fgets(questions, 1100, file);
    fclose(file);
    //allocate memory for the tree and allocate space for the root
    TQDecisionTree* tree = malloc(sizeof(TQDecisionTree));
    tree->root = malloc(sizeof(TQDecisionTreeNode));
    tree->root->num_answers = 0;
    //iterate through the questions and fill the tree with them
    char* curQuestion = malloc(50 * sizeof(char));
    int i = 0;
    int numQuestions = 0;
    while(questions[i] != '\0' && questions[i] != '\n'){
        //extract the current question
        int j = 0;
        while(questions[i] != ',' && questions[i] != '\0' && questions[i] != '\n'){
            curQuestion[j] = questions[i];
            i++;
            j++;
        }
        curQuestion[j - 1] = '\0';
        //fill the next level in the tree with the current question
        TQ_fill_tree_level(tree, tree->root, curQuestion, numQuestions);
        numQuestions++;
        i++;
    }
    //free the memory and return the tree
    free(curQuestion);
    free(questions);
    return tree;
}

void TQ_populate_tree(TQDecisionTree* tree, char* file_name){
    //open the file
    FILE* file = fopen(file_name, "r");
    //extract the number of answers from the file
    int numAnswers = 0;
    fscanf(file, "%d", &numAnswers);
    //allocate memory for the current line and current answer
    char* curLine = malloc(1100 * sizeof(char));
    char* curAnswer = malloc(100 * sizeof(char));
    //skip the first two lines of the file
    fgets(curLine, 100, file);
    fgets(curLine, 1100, file);
    //iterate through the answers and add them to the tree in the correct place
    for(int i = 0; i < numAnswers; i++){
        //get the current line and extract the answer
        fgets(curLine, 1100, file);
        int j = 0;
        while(curLine[j] != ','){
            curAnswer[j] = curLine[j];
            j++;
        }
        curAnswer[j] = '\0';
        //iterate through the rest of the line, moving to the yes node if the next (non-comma) character is a 1 and the no
        //node if it is a 0
        TQDecisionTreeNode* curNode = tree->root;
        while(curLine[j] != '\0' && curLine[j] != '\n'){
            j++;
            if(curLine[j] == '1' && curNode != NULL){
                curNode = curNode->yes;
            }
            else if(curLine[j] == '0' && curNode != NULL){
                curNode = curNode->no;
            }
            j++;
        }
        //add the answer to the current node
        curNode->num_answers = curNode->num_answers + 1;
        curNode->answers = realloc(curNode->answers, curNode->num_answers * sizeof(char*));
        curNode->answers[curNode->num_answers - 1] = malloc(100 * sizeof(char));
        int k = 0;
        while(curAnswer[k] != '\0'){
            curNode->answers[curNode->num_answers - 1][k] = curAnswer[k];
            k++;
        }
        curNode->answers[curNode->num_answers - 1][k] = '\0';
    }
    //free the memory and close the file
    fclose(file);
    free(curLine);
    free(curAnswer);
}

void TQ_free_tree(TQDecisionTree* tree){
    TQ_free_tree_recursive(tree->root);
    free(tree);
}

