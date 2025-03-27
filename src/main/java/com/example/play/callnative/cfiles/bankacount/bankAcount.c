#include <stdio.h>
#include <stdbool.h>
#include <math.h>
#include "bankAcount.h"

// Function prototype
struct expense process_data(struct salary input);

int main() {
    // Create a test salary struct
    struct salary input;
    input.ID = 1;
    input.asset.X = 100.0;
    input.asset.Y = 200.0;
    input.asset.Z = 300.0;
    for (int i = 0; i < CONST_16; i++) {
        input.x[i] = i;
        input.y[i] = i * 2;
        input.z[i] = i * 3;
    }
    input.T0 = 1.0;
    input.NumOfCoeff = 5;
    input.ValidityTimeStart = 0.0;
    input.ValidityTimeEnd = 10.0;
    input.department = 42;
    
    // Call the function
    struct expense result = process_data(input);
    
    // Print results
    printf("Result: ID=%d, is=%d, Score=%f\n", 
           result.ID, result.is, result.Score);
    
    return 0;
}

// Function implementation
struct expense process_data(struct salary input) {
    printf("Processing salary data for ID: %d\n", input.ID);
    
    struct expense result;
    
    // Example processing logic
    result.ID = input.ID;
    result.is = (input.department > 40);  // Example condition
    
    // Calculate score based on some metrics
    double assetValue = sqrt(
        input.asset.X * input.asset.X +
        input.asset.Y * input.asset.Y +
        input.asset.Z * input.asset.Z
    );
    
    double coeffSum = 0;
    for (int i = 0; i < input.NumOfCoeff && i < CONST_16; i++) {
        coeffSum += input.x[i] + input.y[i] + input.z[i];
    }
    
    result.Score = assetValue + coeffSum + input.T0;
    
    printf("Calculated expense: ID=%d, is=%d, Score=%f\n", 
           result.ID, result.is, result.Score);
    
    return result;
}