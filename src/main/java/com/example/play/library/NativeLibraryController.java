package com.example.play.library;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("native")
public class NativeLibraryController {

    @GetMapping("/test")
    public String testNativeLibrary() {
        try {
            // Create input structure
            NativeLibrary.IN input = new NativeLibrary.IN();
            for (int i = 0; i < 16; i++) {
                input.polyX[i] = i;
            }
            input.field2 = 42;
            input.field3 = 3.14;
            
            // Call the native method using FFM
            NativeLibrary.OUT result = NativeLibrary.processData(input);
            
            // Return the results
            return String.format("Native call successful! Result: X=%f, Y=%f, Z=%f", 
                    result.poz.X, result.poz.Y, result.poz.Z);
        } catch (Exception e) {
            return "Error calling native library: " + e.getMessage() + "\n" + 
                   Arrays.toString(e.getStackTrace());
        }
    }
} 