/* File: game.c
 * Author: Kareem Khalidi
 * Class: CSC 352
 *
 * Game.c is a simple game that asks the user a series of questions and tries to guess what the user is thinking of.
 * Game.c reads in a file that contains a series of questions and answers. Game.c then builds a decision tree
 * from the questions and answers. Game.c then asks the user a series of questions and tries to guess what the user
 * is thinking of.
 * */

#include "tq.h"
#include "malloc.h"
#include "stdio.h"

int main(int argc, char* argv[]) {
    //build and populate the tree
    TQDecisionTree* tq = TQ_build_tree(argv[1]);
    TQ_populate_tree(tq, argv[1]);
    //allocate memory for user input and set the current node to the root
    TQDecisionTreeNode* cur = tq->root;
    char* s = malloc(sizeof(char) * 100);
    //while the current node is not a leaf, ask the user a question and move to the appropriate node
    while(cur->num_answers == 0 && cur->yes != NULL && cur->no != NULL){
        //print the question and get user input
        printf("%s (y/n)\n", cur->text);
        fgets(s, 100, stdin);
        //if the user enters y, move to the yes node, if the user enters n, move to the no node
        if(s[0] == 'y' && cur->yes != NULL){
            cur = cur->yes;
        }
        else if(s[0] == 'n' && cur->no != NULL){
            cur = cur->no;
        }
    }
    //if the current node is a leaf, ask the user if the answer is one of the answers
    for(int i = 0; i < cur->num_answers; i++){
        //print the answer and get user input
        printf("is it a %s? (y/n)\n", cur->answers[i]);
        fgets(s, 100, stdin);
        //if the answer is one of the answers, print "I guessed it!", free the tree and ui string memory, and exit
        if(s[0] == 'y'){
            printf("I guessed it!");
            free(s);
            TQ_free_tree(tq);
            return 0;
        }
    }
    //if the answer is not one of the answers, print "You got me :)", free the tree and ui string memory, and exit
    printf("You got me :)");
    free(s);
    TQ_free_tree(tq);
    return 0;
}