#include <stdio.h>
#include "Example.h"

// Function that takes IN struct and returns OUT struct
struct OUT process_data(struct IN input) {
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

// Simple main function to test the code
int main() {
    // Create a test IN struct
    struct IN input;
    for (int i = 0; i < CONST_16; i++) {
        input.polyX[i] = i;
    }
    input.field2 = 42;
    input.field3 = 3.14;
    
    // Call the function
    struct OUT result = process_data(input);
    
    // Print some results to verify
    printf("Result: X=%f, Y=%f, Z=%f\n", result.poz.X, result.poz.Y, result.poz.Z);
    
    return 0;
} 