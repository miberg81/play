package com.example.play.callnative;

import com.example.play.callnative.ffm.library.bankacount.NativeLibraryBankAcount;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("bank-native")
public class NativeLibraryBankAccountController {

    @GetMapping("/test-ffm")
    public String testNativeLibrary() {
        try {
            // Create input structure
            NativeLibraryBankAcount.Salary input = new NativeLibraryBankAcount.Salary();
            
            // Set up test data
            input.ID = 1;
            input.asset.X = 100.0;
            input.asset.Y = 200.0;
            input.asset.Z = 300.0;
            
            for (int i = 0; i < 16; i++) {
                input.x[i] = i;
                input.y[i] = i * 2;
                input.z[i] = i * 3;
            }
            
            input.T0 = 1.0;
            input.NumOfCoeff = 5;
            input.ValidityTimeStart = 0.0;
            input.ValidityTimeEnd = 10.0;
            input.department = 42;
            
            // Call the native method using FFM
            NativeLibraryBankAcount.Expense result = NativeLibraryBankAcount.processData(input);
            
            // Return the results
            return String.format("FFM call successful! Result: ID=%d, is=%b, Score=%f",
                    result.ID, result.is, result.Score);
        } catch (Exception e) {
            return "Error calling native library: " + e.getMessage() + "\n" + 
                   Arrays.toString(e.getStackTrace());
        }
    }

    // Remove JNI test for now since we haven't implemented JNI version yet
} 