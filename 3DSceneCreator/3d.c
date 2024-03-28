/* File: 3d.c
 * Author: Kareem Khalidi
 * Class: CSC 352
 *
 * 3d.c is a source file for the 3d functions. 3d.c contains the functions to create a new 3d scene, add a pyramid and
 * cuboid to the scene, print the scene to a file, and free the scene. It also contains private helper methods to assist
 * with these functions.
 * */

#include "3d.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

/* Gets the corners of the base of a pyramid with orientation up/down.
 * The base for a pyramid with orientation up/down is a square in the x-y plane.
 *
 * @param Coordinate3D origin - the origin of the pyramid
 * @param double width - the width of the pyramid
 * @param Coordinate3D* baseCorners - a pointer to an array of 4 Coordinate3D objects
 * */
void get_pyramid_base_corners_up_down(Coordinate3D origin, double width, Coordinate3D* baseCorners){
    baseCorners[0].x = origin.x + width/2;
    baseCorners[0].y = origin.y + width/2;
    baseCorners[0].z = origin.z;

    baseCorners[1].x = origin.x - width/2;
    baseCorners[1].y = origin.y + width/2;
    baseCorners[1].z = origin.z;

    baseCorners[2].x = origin.x - width/2;
    baseCorners[2].y = origin.y - width/2;
    baseCorners[2].z = origin.z;

    baseCorners[3].x = origin.x + width/2;
    baseCorners[3].y = origin.y - width/2;
    baseCorners[3].z = origin.z;
}

/* Gets the corners of the base of a pyramid with orientation forward/backward.
 * The base for a pyramid with orientation forward/backward is a square in the x-z plane.
 *
 * @param Coordinate3D origin - the origin of the pyramid
 * @param double width - the width of the pyramid
 * @param Coordinate3D* baseCorners - a pointer to an array of 4 Coordinate3D objects
 * */
void get_pyramid_base_corners_forward_backward(Coordinate3D origin, double width, Coordinate3D* baseCorners){
    baseCorners[0].x = origin.x + width/2;
    baseCorners[0].y = origin.y;
    baseCorners[0].z = origin.z + width/2;

    baseCorners[1].x = origin.x - width/2;
    baseCorners[1].y = origin.y;
    baseCorners[1].z = origin.z + width/2;

    baseCorners[2].x = origin.x - width/2;
    baseCorners[2].y = origin.y;
    baseCorners[2].z = origin.z - width/2;

    baseCorners[3].x = origin.x + width/2;
    baseCorners[3].y = origin.y;
    baseCorners[3].z = origin.z - width/2;
}

/* Gets the corners of the base of a pyramid with orientation left/right.
 * The base for a pyramid with orientation left/right is a square in the y-z plane.
 *
 * @param Coordinate3D origin - the origin of the pyramid
 * @param double width - the width of the pyramid
 * @param Coordinate3D* baseCorners - a pointer to an array of 4 Coordinate3D objects
 * */
void get_pyramid_base_corners_left_right(Coordinate3D origin, double width, Coordinate3D* baseCorners){
    baseCorners[0].x = origin.x;
    baseCorners[0].y = origin.y + width/2;
    baseCorners[0].z = origin.z + width/2;

    baseCorners[1].x = origin.x;
    baseCorners[1].y = origin.y - width/2;
    baseCorners[1].z = origin.z + width/2;

    baseCorners[2].x = origin.x;
    baseCorners[2].y = origin.y - width/2;
    baseCorners[2].z = origin.z - width/2;

    baseCorners[3].x = origin.x;
    baseCorners[3].y = origin.y + width/2;
    baseCorners[3].z = origin.z - width/2;
}

/* Gets the point on the top of a pyramid with orientation left.
 * The point on the top of a pyramid with orientation left is in the x direction.
 *
 * @param Coordinate3D origin - the origin of the pyramid
 * @param double height - the height of the pyramid
 * @param Coordinate3D* point - a pointer to a Coordinate3D object
 * */
void get_pyramid_point_left(Coordinate3D origin, double height, Coordinate3D* point){
    point->x = origin.x - height;
    point->y = origin.y;
    point->z = origin.z;
}

/* Gets the point on the top of a pyramid with orientation right.
 * The point on the top of a pyramid with orientation right is in the x direction.
 *
 * @param Coordinate3D origin - the origin of the pyramid
 * @param double height - the height of the pyramid
 * @param Coordinate3D* point - a pointer to a Coordinate3D object
 * */
void get_pyramid_point_right(Coordinate3D origin, double height, Coordinate3D* point){
    point->x = origin.x + height;
    point->y = origin.y;
    point->z = origin.z;
}

/* Gets the point on the top of a pyramid with orientation forward.
 * The point on the top of a pyramid with orientation forward is in the y direction.
 *
 * @param Coordinate3D origin - the origin of the pyramid
 * @param double height - the height of the pyramid
 * @param Coordinate3D* point - a pointer to a Coordinate3D object
 * */
void get_pyramid_point_forward(Coordinate3D origin, double height, Coordinate3D* point){
    point->x = origin.x;
    point->y = origin.y + height;
    point->z = origin.z;
}

/* Gets the point on the top of a pyramid with orientation backward.
 * The point on the top of a pyramid with orientation backward is in the y direction.
 *
 * @param Coordinate3D origin - the origin of the pyramid
 * @param double height - the height of the pyramid
 * @param Coordinate3D* point - a pointer to a Coordinate3D object
 * */
void get_pyramid_point_backward(Coordinate3D origin, double height, Coordinate3D* point){
    point->x = origin.x;
    point->y = origin.y - height;
    point->z = origin.z;
}

/* Gets the point on the top of a pyramid with orientation up.
 * The point on the top of a pyramid with orientation up is in the z direction.
 *
 * @param Coordinate3D origin - the origin of the pyramid
 * @param double height - the height of the pyramid
 * @param Coordinate3D* point - a pointer to a Coordinate3D object
 * */
void get_pyramid_point_up(Coordinate3D origin, double height, Coordinate3D* point){
    point->x = origin.x;
    point->y = origin.y;
    point->z = origin.z + height;
}

/* Gets the point on the top of a pyramid with orientation down.
 * The point on the top of a pyramid with orientation down is in the z direction.
 *
 * @param Coordinate3D origin - the origin of the pyramid
 * @param double height - the height of the pyramid
 * @param Coordinate3D* point - a pointer to a Coordinate3D object
 * */
void get_pyramid_point_down(Coordinate3D origin, double height, Coordinate3D* point){
    point->x = origin.x;
    point->y = origin.y;
    point->z = origin.z - height;
}

/* Adds a triangle to a scene
 *
 * @param Scene3D* scene - a pointer to the scene
 * @param Coordinate3D a - the first corner of the triangle
 * @param Coordinate3D b - the second corner of the triangle
 * @param Coordinate3D c - the third corner of the triangle
 * */
void Scene3D_add_triangle(Scene3D* scene, Coordinate3D a, Coordinate3D b, Coordinate3D c){
    //create the triangle to add
    Triangle3D triangle;
    triangle.a = a;
    triangle.b = b;
    triangle.c = c;
    //if the scene is empty, add the triangle to the root
    if(scene->root == NULL){
        scene->root = malloc(sizeof(Triangle3DNode));
        scene->root->triangle = triangle;
        scene->root->next = NULL;
    }
    //if the scene is not empty, add the triangle to the end of the list
    else{
        Triangle3DNode* current = scene->root;
        while(current->next != NULL){
            current = current->next;
        }
        current->next = malloc(sizeof(Triangle3DNode));
        current->next->triangle = triangle;
        current->next->next = NULL;
    }
    //increment the count
    scene->count++;
}

/* Adds a quadrilateral to a scene
 *
 * @param Scene3D* scene - a pointer to the scene
 * @param Coordinate3D a - the first corner of the quadrilateral
 * @param Coordinate3D b - the second corner of the quadrilateral
 * @param Coordinate3D c - the third corner of the quadrilateral
 * @param Coordinate3D d - the fourth corner of the quadrilateral
 * */
void Scene3D_add_quadrilateral(Scene3D* scene, Coordinate3D a, Coordinate3D b, Coordinate3D c, Coordinate3D d){
    Scene3D_add_triangle(scene, a, b, c);
    Scene3D_add_triangle(scene, b, c, d);
    Scene3D_add_triangle(scene, c, d, a);
    Scene3D_add_triangle(scene, d, a, b);
}

Scene3D* Scene3D_create(){
    Scene3D* scene = malloc(sizeof(Scene3D));
    scene->count = 0;
    scene->root = NULL;
    return scene;
}

void Scene3D_destroy(Scene3D* scene){
    Triangle3DNode* current = scene->root;
    Triangle3DNode* next = NULL;
    while(current != NULL){
        next = current->next;
        free(current);
        current = next;
    }
    free(scene);
}

void Scene3D_write_stl_text(Scene3D* scene, char* file_name){
    //Open the file
    FILE* file = fopen(file_name, "w");
    //Write the header
    fprintf(file, "solid scene\n");
    //Write the triangles
    Triangle3DNode* current = scene->root;
    while(current != NULL){
        fprintf(file, "  facet normal 0.0 0.0 0.0\n");
        fprintf(file, "    outer loop\n");
        fprintf(file, "      vertex %.5f %.5f %.5f\n", current->triangle.a.x, current->triangle.a.y, current->triangle.a.z);
        fprintf(file, "      vertex %.5f %.5f %.5f\n", current->triangle.b.x, current->triangle.b.y, current->triangle.b.z);
        fprintf(file, "      vertex %.5f %.5f %.5f\n", current->triangle.c.x, current->triangle.c.y, current->triangle.c.z);
        fprintf(file, "    endloop\n");
        fprintf(file, "  endfacet\n");
        current = current->next;
    }
    //Write the footer
    fprintf(file, "endsolid scene\n");
    //Close the file
    fclose(file);
}

void Scene3D_add_pyramid(Scene3D* scene,
                         Coordinate3D origin, double width, double height, char* orientation){
    //get base corners
    Coordinate3D* baseCorners = malloc(sizeof(Coordinate3D) * 4);
    if(strcmp(orientation, "left") == 0 || strcmp(orientation, "right") == 0) {
        get_pyramid_base_corners_left_right(origin, width, baseCorners);
    }
    else if(strcmp(orientation, "forward") == 0 || strcmp(orientation, "backward") == 0) {
        get_pyramid_base_corners_forward_backward(origin, width, baseCorners);
    }
    else if(strcmp(orientation, "up") == 0 || strcmp(orientation, "down") == 0) {
        get_pyramid_base_corners_up_down(origin, width, baseCorners);
    }
    else{
        printf("Invalid orientation");
        return;
    }

    //get point (tip of pyramid)
    Coordinate3D* point = malloc(sizeof(Coordinate3D));
    if(strcmp(orientation, "left") == 0){
        get_pyramid_point_left(origin, height, point);
    }
    else if(strcmp(orientation, "right") == 0){
        get_pyramid_point_right(origin, height, point);
    }
    else if(strcmp(orientation, "forward") == 0){
        get_pyramid_point_forward(origin, height, point);
    }
    else if(strcmp(orientation, "backward") == 0){
        get_pyramid_point_backward(origin, height, point);
    }
    else if(strcmp(orientation, "up") == 0){
        get_pyramid_point_up(origin, height, point);
    }
    else if(strcmp(orientation, "down") == 0){
        get_pyramid_point_down(origin, height, point);
    }

    //add the pyramids triangles to scene
    Scene3D_add_triangle(scene, baseCorners[0], baseCorners[1], baseCorners[2]);
    Scene3D_add_triangle(scene, baseCorners[1], baseCorners[2], baseCorners[3]);
    Scene3D_add_triangle(scene, baseCorners[2], baseCorners[3], baseCorners[0]);
    Scene3D_add_triangle(scene, baseCorners[3], baseCorners[0], baseCorners[1]);
    Scene3D_add_triangle(scene, baseCorners[0], baseCorners[1], *point);
    Scene3D_add_triangle(scene, baseCorners[1], baseCorners[2], *point);
    Scene3D_add_triangle(scene, baseCorners[2], baseCorners[3], *point);
    Scene3D_add_triangle(scene, baseCorners[3], baseCorners[0], *point);

    //free memory
    free(baseCorners);
    free(point);
}

void Scene3D_add_cuboid(Scene3D* scene,
                        Coordinate3D origin, double width, double height, double depth){
    //get corners
    Coordinate3D* corners = malloc(sizeof(Coordinate3D) * 8);
    corners[0].x = origin.x + width / 2;
    corners[0].y = origin.y - height / 2;
    corners[0].z = origin.z + depth / 2;

    corners[1].x = origin.x - width / 2;
    corners[1].y = origin.y - height / 2;
    corners[1].z = origin.z + depth / 2;

    corners[2].x = origin.x - width / 2;
    corners[2].y = origin.y - height / 2;
    corners[2].z = origin.z - depth / 2;

    corners[3].x = origin.x + width / 2;
    corners[3].y = origin.y - height / 2;
    corners[3].z = origin.z - depth / 2;

    corners[4].x = origin.x + width / 2;
    corners[4].y = origin.y + height / 2;
    corners[4].z = origin.z + depth / 2;

    corners[5].x = origin.x - width / 2;
    corners[5].y = origin.y + height / 2;
    corners[5].z = origin.z + depth / 2;

    corners[6].x = origin.x - width / 2;
    corners[6].y = origin.y + height / 2;
    corners[6].z = origin.z - depth / 2;

    corners[7].x = origin.x + width / 2;
    corners[7].y = origin.y + height / 2;
    corners[7].z = origin.z - depth / 2;

    //add triangles to scene
    //front face
    Scene3D_add_quadrilateral(scene, corners[0], corners[1], corners[2], corners[3]);
    //back face
    Scene3D_add_quadrilateral(scene, corners[4], corners[5], corners[6], corners[7]);

    //left face
    Scene3D_add_quadrilateral(scene, corners[1], corners[2], corners[5], corners[6]);

    //right face
    Scene3D_add_quadrilateral(scene, corners[0], corners[3], corners[4], corners[7]);

    //top face
    Scene3D_add_quadrilateral(scene, corners[0], corners[1], corners[4], corners[5]);

    //bottom face
    Scene3D_add_quadrilateral(scene, corners[3], corners[2], corners[7], corners[6]);

    //free memory
    free(corners);
}