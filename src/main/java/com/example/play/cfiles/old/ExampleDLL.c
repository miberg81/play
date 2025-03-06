#include <stdio.h>
#include "Example.h"

// Export directive for Windows DLL
#ifdef _WIN32
    #define EXPORT __declspec(dllexport)
#else
    #define EXPORT __attribute__((visibility("default")))
#endif

// The exported function that will be called from Java
EXPORT struct OUT process_data(struct IN input) {
    printf("Inside the method\n");
    
    // Create an empty OUT struct to return
    struct OUT result;
    result.poz.X = 0.0;
    result.poz.Y = 0.0;
    result.poz.Z = 0.0;
    result.field2 = false;
    result.field3 = 0.0;
    
    return result;
} 