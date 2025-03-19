#ifndef COMMON_H
#define COMMON_H

#include <stdbool.h>  // Use the standard stdbool.h

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

// Need to add this function declaration
struct OUT process_data(struct IN input);

#endif