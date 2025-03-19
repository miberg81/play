package com.example.play.callnative.jni.library;

public class JniLibrary {
    // Load the native library
    static {
        try {
            String libraryPath = System.getProperty("user.dir");
            System.setProperty("java.library.path", libraryPath);

            try {
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    System.load(libraryPath + "/ExampleJni.dll");
                } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    System.load(libraryPath + "/libExampleJni.dylib");
                } else {
                    System.load(libraryPath + "/libExampleJni.so");
                }
            } catch (UnsatisfiedLinkError e) {
                System.loadLibrary("ExampleJni");
            }
        } catch (Exception e) {
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

    // Native method declaration
    public native OUT processData(IN input);
}