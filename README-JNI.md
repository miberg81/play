# JNI Implementation Guide

This guide explains how to build and use the JNI implementation of the native library.

## File Structure
src/main/java/com/example/play/callnative/
├── cfiles/
│ ├── Example.h # Original C header file
│ └── Example.c # Original C implementation
└── jni/library/
├── JniLibrary.java # Java JNI wrapper class
└── JniLibrary.c # JNI implementation

****

## Build Process

### 1. Compile Java Class
First, compile the Java class to generate the JNI header file:

bash:src/main/java/com/example/play/callnative/jni/library/JniLibrary.java
javac -h src/main/java/com/example/play/callnative/jni/library src/main/java/com/example/play/callnative/jni/library/JniLibrary.java

bash
gcc -I"%JAVA_HOME%\include" ^
-I"%JAVA_HOME%\include\win32" ^
-I"src/main/java/com/example/play/callnative/cfiles" ^
-shared ^
-Wl,--add-stdcall-alias ^
src/main/java/com/example/play/callnative/jni/library/JniLibrary.c ^
src/main/java/com/example/play/callnative/cfiles/Example.c ^
-o ExampleJni.dll

bash
gcc -I"$JAVA_HOME/include" \
-I"$JAVA_HOME/include/linux" \
-I"src/main/java/com/example/play/callnative/cfiles" \
-shared -fPIC \
src/main/java/com/example/play/callnative/jni/library/JniLibrary.c \
src/main/java/com/example/play/callnative/cfiles/Example.c \
-o libExampleJni.so

bash
gcc -I"$JAVA_HOME/include" \
-I"$JAVA_HOME/include/darwin" \
-I"src/main/java/com/example/play/callnative/cfiles" \
-shared -fPIC \
src/main/java/com/example/play/callnative/jni/library/JniLibrary.c \
src/main/java/com/example/play/callnative/cfiles/Example.c \
-o libExampleJni.dylib

This will generate a header file with the naming convention based on the fully qualified class name:
`com_example_play_callnative_jni_library_JniLibrary.h`

### 2. Build Native Library

#### Windows (using GCC)
bash
gcc -I"$JAVA_HOME/include" \
-I"$JAVA_HOME/include/darwin" \
-I"src/main/java/com/example/play/callnative/cfiles" \
-shared -fPIC \
src/main/java/com/example/play/callnative/jni/library/JniLibrary.c \
src/main/java/com/example/play/callnative/cfiles/Example.c \
-o libExampleJni.dylib


### 3. Library Placement
Place the generated library file (ExampleJni.dll/libExampleJni.so/libExampleJni.dylib) in your project's root directory.

## Important Notes

1. **Environment Setup**
    - Ensure JAVA_HOME environment variable is set correctly
    - Make sure you have a C compiler (gcc/clang) installed
    - For Windows, you might need MinGW or similar for gcc

2. **Common Issues**
    - UnsatisfiedLinkError: Make sure the library name and path match exactly what's in JniLibrary.java
    - Missing symbols: Ensure both Example.c and JniLibrary.c are compiled together
    - Compilation errors: Check include paths and make sure all headers are found

3. **Testing**
    - Use the endpoint: http://localhost:8080/native/test-jni
    - Compare results with FFM implementation at: http://localhost:8080/native/test

4. **Library Naming**
    - Windows: ExampleJni.dll
    - Linux: libExampleJni.so
    - macOS: libExampleJni.dylib

## Implementation Details

1. **JniLibrary.java**
    - Defines the Java interface to the native code
    - Contains structure definitions matching C structs
    - Handles library loading

2. **JniLibrary.c**
    - Implements the JNI bridge between Java and C
    - Converts Java objects to C structs and back
    - Calls the original C implementation

3. **Example.h/c**
    - Original C implementation
    - Used by both JNI and FFM implementations

## Build Flow Explanation

1. **Header File (.h) Generation**
    - Java code is compiled first
    - JNI header is generated from Java class
    - Contains function prototypes for native methods

2. **JNI Implementation (.c)**
    - Implements the functions declared in the header
    - Handles data conversion between Java and C
    - Links with the original C implementation

3. **Compilation**
    - Both JNI wrapper and original C code are compiled together
    - Creates a single shared library
    - Platform-specific flags and options are used

4. **Usage**
    - Library is loaded by Java code at runtime
    - Native methods can be called like regular Java methods
    - Results are automatically converted between Java and C types

## Debugging Tips

1. **Library Loading**
   ```java
   System.load(absolutePath) // Use for full path
   System.loadLibrary(name)  // Uses java.library.path
   ```

2. **Common JNI Signatures**
    - `()V` - void method with no parameters
    - `(I)V` - void method with one int parameter
    - `(Ljava/lang/String;)V` - void method with String parameter

3. **Error Checking**
    - Always check for NULL after JNI calls
    - Handle exceptions appropriately
    - Use proper memory management

4. **Build Verification**
    - Use `nm` (Linux/macOS) or `dumpbin` (Windows) to verify symbols
    - Check library dependencies with `ldd` (Linux) or `depends.exe` (Windows)