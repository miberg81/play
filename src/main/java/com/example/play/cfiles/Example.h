#ifndef COMMON_H
#define COMMON_H

#define CONST_10 10
#define CONST_16 16

typedef struct Pos {
    double X;
    double Y;
    double Z;
};

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