#include <stdio.h>
#include <stdbool.h>
#include <math.h>
#include "Example.h"

// Function prototype - declare the function before using it
struct OUT process_data(struct IN input);

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

// Function implementation
struct OUT process_data(struct IN input) {
    printf("Inside the method\n");
    printf("Input values: field2=%d, field3=%f\n", input.field2, input.field3);
    
    // Create an OUT struct with values calculated from input
    struct OUT result;
    
    // Calculate X as sum of first 5 elements in polyX array
    result.poz.X = 0.0;
    for (int i = 0; i < 5 && i < CONST_16; i++) {
        result.poz.X += input.polyX[i];
    }
    
    result.poz.Y = input.field2 * 0.1;
    
    result.poz.Z = input.field3 * 2.0;
    
    // Set field2 based on whether field2 input is even or odd
    result.field2 = (input.field2 % 2 == 0);
    
    result.field3 = sqrt(input.field3);
    
    printf("Calculated output: X=%f, Y=%f, Z=%f\n", 
           result.poz.X, result.poz.Y, result.poz.Z);
    
    return result;
}