/* File: 3d.h
 * Author: Kareem Khalidi
 * Class: CSC 352
 *
 * 3d.h is a header file for 3d scenes. 3d.h contains the structs and function prototypes for the methods to create and
 * destroy a scene. 3d.h also contains the function prototypes for the functions that write a scene to an STL file, add
 * a pyramid to the scene, and add a cuboid to the scene.
 * */

typedef struct Coordinate3D {
    double x;
    double y;
    double z;
} Coordinate3D;

typedef struct Triangle3D {
    Coordinate3D a;
    Coordinate3D b;
    Coordinate3D c;
} Triangle3D;

typedef struct Triangle3DNode {
    Triangle3D triangle;
    struct Triangle3DNode * next;
} Triangle3DNode;

typedef struct Scene3D {
    long count;
    Triangle3DNode * root;
} Scene3D;

/* Creates a new empty 3d scene object
 *
 * @return Scene3D* - a pointer to the new scene object
 * */
Scene3D* Scene3D_create();

/* Destroys a 3d scene object
 *
 * @param Scene3D* scene - a pointer to the scene object to destroy
 * */
void Scene3D_destroy(Scene3D* scene);

/* Writes all the objects in the scene to an STL file
 *
 * @param Scene3D* scene - a pointer to the scene object to write
 * @param char* file_name - the name of the file to write to
 * */
void Scene3D_write_stl_text(Scene3D* scene, char* file_name);

/* Adds a pyramid to the scene
 *
 * @param Scene3D* scene - a pointer to the scene object to add the pyramid to
 * @param Coordinate3D origin - the origin of the pyramid
 * @param double width - the width of the pyramid
 * @param double height - the height of the pyramid
 * @param char* orientation - the orientation of the pyramid
 * */
void Scene3D_add_pyramid(Scene3D* scene,
Coordinate3D origin, double width, double height, char* orientation);

/* Adds a cuboid to the scene
 *
 * @param Scene3D* scene - a pointer to the scene object to add the cuboid to
 * @param Coordinate3D origin - the origin of the cuboid
 * @param double width - the width of the cuboid
 * @param double height - the height of the cuboid
 * @param double depth - the depth of the cuboid
 * */
void Scene3D_add_cuboid(Scene3D* scene,
Coordinate3D origin, double width, double height, double depth);