package com.example.play.callnative;

import com.example.play.callnative.ffm.library.NativeLibrary;
import com.example.play.callnative.jni.library.JniLibrary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("native")
public class NativeLibraryController {

    @GetMapping("/test-ffm")
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
            return String.format("FFM call successful! Result: X=%f, Y=%f, Z=%f",
                    result.poz.X, result.poz.Y, result.poz.Z);
        } catch (Exception e) {
            return "Error calling native library: " + e.getMessage() + "\n" + 
                   Arrays.toString(e.getStackTrace());
        }
    }

    @GetMapping("/test-jni")
    public String testJniLibrary() {
        try {
            // Create input structure
            JniLibrary.IN input = new JniLibrary.IN();
            for (int i = 0; i < 16; i++) {
                input.polyX[i] = i;
            }
            input.field2 = 42;
            input.field3 = 3.14;
            
            // Call the native method using JNI
            JniLibrary jniLib = new JniLibrary();
            JniLibrary.OUT result = jniLib.processData(input);
            
            // Return the results
            return String.format("JNI call successful! Result: X=%f, Y=%f, Z=%f", 
                    result.poz.X, result.poz.Y, result.poz.Z);
        } catch (Exception e) {
            return "Error calling JNI library: " + e.getMessage() + "\n" + 
                   Arrays.toString(e.getStackTrace());
        }
    }
} 