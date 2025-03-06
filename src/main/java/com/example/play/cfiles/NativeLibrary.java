package com.example.play.cfiles;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Field;

public class NativeLibrary {
    // Memory layouts for our C structures
    private static final MemoryLayout POS_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_DOUBLE.withName("X"),
        ValueLayout.JAVA_DOUBLE.withName("Y"),
        ValueLayout.JAVA_DOUBLE.withName("Z")
    );
    
    private static final MemoryLayout IN_LAYOUT = MemoryLayout.structLayout(
        MemoryLayout.sequenceLayout(16, ValueLayout.JAVA_DOUBLE).withName("polyX"),
        ValueLayout.JAVA_INT.withName("field2"),
        MemoryLayout.paddingLayout(4), // Add padding to ensure proper alignment for the double
        ValueLayout.JAVA_DOUBLE.withName("field3")
    );
    
    private static final MemoryLayout OUT_LAYOUT = MemoryLayout.structLayout(
        POS_LAYOUT.withName("poz"),
        ValueLayout.JAVA_BOOLEAN.withName("field2"),
        MemoryLayout.paddingLayout(7), // Add padding to ensure proper alignment for the double
        ValueLayout.JAVA_DOUBLE.withName("field3")
    );
    
    // Get field offsets for easier access
    private static final long X_OFFSET = POS_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("X"));
    private static final long Y_OFFSET = POS_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("Y"));
    private static final long Z_OFFSET = POS_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("Z"));
    
    private static final long POLYX_OFFSET = IN_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("polyX"));
    private static final long IN_FIELD2_OFFSET = IN_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("field2"));
    private static final long IN_FIELD3_OFFSET = IN_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("field3"));
    
    private static final long POZ_OFFSET = OUT_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("poz"));
    private static final long OUT_FIELD2_OFFSET = OUT_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("field2"));
    private static final long OUT_FIELD3_OFFSET = OUT_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("field3"));
    
    // Method handle for the native function
    private static final MethodHandle PROCESS_DATA;
    
    // Load the native library and get the function handle
    static {
        try {
            // Set the library path to include the project directory
            String libraryPath = System.getProperty("user.dir");
            System.setProperty("java.library.path", libraryPath);
            
            // This hack is needed to force Java to reload the library paths
            try {
                Field field = ClassLoader.class.getDeclaredField("sys_paths");
                field.setAccessible(true);
                field.set(null, null);
            } catch (Exception e) {
                System.err.println("Failed to reset library path: " + e.getMessage());
            }
            
            // Try to load the library with platform-specific naming
            try {
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    System.load(libraryPath + "/Example.dll");
                } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    System.load(libraryPath + "/libExample.dylib");
                } else {
                    System.load(libraryPath + "/libExample.so");
                }
            } catch (UnsatisfiedLinkError e) {
                // Fallback to loadLibrary which uses java.library.path
                System.loadLibrary("Example");
            }
            
            SymbolLookup symbolLookup = SymbolLookup.loaderLookup();
            Linker linker = Linker.nativeLinker();
            
            FunctionDescriptor descriptor = FunctionDescriptor.of(
                OUT_LAYOUT,  // Return type
                IN_LAYOUT    // Parameter type
            );
            
            PROCESS_DATA = linker.downcallHandle(
                symbolLookup.find("process_data").orElseThrow(),
                descriptor
            );
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load native library", e);
        }
    }
    
    // Java representation of the C structures
    public static class Pos {
        public double X;
        public double Y;
        public double Z;
    }
    
    public static class IN {
        public double[] polyX = new double[16];
        public int field2;
        public double field3;
    }
    
    public static class OUT {
        public Pos poz = new Pos();
        public boolean field2;
        public double field3;
    }
    
    // Method to call the native function
    public static OUT processData(IN input) {
        try (Arena arena = Arena.ofConfined()) {
            // Allocate memory for input struct
            MemorySegment inSegment = arena.allocate(IN_LAYOUT);
            
            // Fill the input struct
            MemorySegment polyXArray = inSegment.asSlice(POLYX_OFFSET, 16 * Double.BYTES);
            for (int i = 0; i < 16; i++) {
                polyXArray.setAtIndex(ValueLayout.JAVA_DOUBLE, i, input.polyX[i]);
            }
            inSegment.set(ValueLayout.JAVA_INT, IN_FIELD2_OFFSET, input.field2);
            inSegment.set(ValueLayout.JAVA_DOUBLE, IN_FIELD3_OFFSET, input.field3);
            
            // Allocate memory for the output struct
            MemorySegment outSegment = arena.allocate(OUT_LAYOUT);
            
            try {
                // Cast arena to SegmentAllocator for exact type matching
                outSegment = (MemorySegment) PROCESS_DATA.invokeExact((SegmentAllocator)arena, inSegment);
            } catch (Throwable e) {
                throw new RuntimeException("Error invoking native function", e);
            }
            
            // Convert the result to Java object
            OUT result = new OUT();
            MemorySegment pozSegment = outSegment.asSlice(POZ_OFFSET, POS_LAYOUT.byteSize());
            result.poz.X = pozSegment.get(ValueLayout.JAVA_DOUBLE, X_OFFSET);
            result.poz.Y = pozSegment.get(ValueLayout.JAVA_DOUBLE, Y_OFFSET);
            result.poz.Z = pozSegment.get(ValueLayout.JAVA_DOUBLE, Z_OFFSET);
            result.field2 = outSegment.get(ValueLayout.JAVA_BOOLEAN, OUT_FIELD2_OFFSET);
            result.field3 = outSegment.get(ValueLayout.JAVA_DOUBLE, OUT_FIELD3_OFFSET);
            
            return result;
        } catch (Throwable e) {
            throw new RuntimeException("Error calling native function", e);
        }
    }
} 