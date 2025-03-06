#ifndef COMMON_H
#define COMMON_H

// Define bool type if stdbool.h is not available
#ifndef __bool_true_false_are_defined
typedef int bool;
#define true 1
#define false 0
#define __bool_true_false_are_defined 1
#endif

#define CONST_10 10
#define CONST_16 16

// Define the Pos struct
typedef struct Pos {
    double X;
    double Y;
    double Z;
} Pos;  // Add the type name here

struct IN {
    double polyX[CONST_16];
    int field2;
    double field3;
};

struct OUT {
    Pos poz;
    bool field2;
    double field3;
};

#endif