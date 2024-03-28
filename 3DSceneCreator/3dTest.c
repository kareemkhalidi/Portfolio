/* File: 3dTest.c
 * Author: Kareem Khalidi
 * Class: CSC 352
 *
 * 3dTest.c is a program that generates a 3d scene of a tree. 3dTest.c uses the 3d.h header file
 * to create a scene and add a tree stump and a tree to the scene. 3dTest.c then writes the scene to an STL
 * file and destroys the scene.
 * */

#include "3d.h"

int main(){
    //create scene and origin
    Scene3D* scene = Scene3D_create();
    Coordinate3D origin;
    origin.x = 0.0;
    origin.y = 0.0;
    origin.z = 0.0;
    //add tree stump to scene
    Scene3D_add_cuboid(scene, origin, 5.0, 5.0, 10.0);
    //add tree to scene
    Scene3D_add_pyramid(scene, origin, 10.0, 10.0, "up");
    origin.z = 4.0;
    Scene3D_add_pyramid(scene, origin, 8.0, 8.0, "up");
    origin.z = 8.0;
    Scene3D_add_pyramid(scene, origin, 6.0, 6.0, "up");
    origin.z = 11.0;
    Scene3D_add_pyramid(scene, origin, 4.0, 4.0, "up");
    origin.z = 14.0;
    Scene3D_add_pyramid(scene, origin, 2.0, 2.0, "up");
    //write to file and destroy scene
    Scene3D_write_stl_text(scene, "output.stl");
    Scene3D_destroy(scene);
    return 0;
}