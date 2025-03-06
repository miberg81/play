//package com.example.play.library;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
//
//public class NativeLibraryLoader {
//
//    public static void loadLibrary(String libraryName) {
//        try {
//            // First try the standard way
//            System.loadLibrary(libraryName);
//        } catch (UnsatisfiedLinkError e) {
//            // If that fails, try to extract and load from resources
//            try {
//                loadFromResources(libraryName);
//            } catch (IOException ex) {
//                throw new RuntimeException("Failed to load native library: " + libraryName, ex);
//            }
//        }
//    }
//
//    private static void loadFromResources(String libraryName) throws IOException {
//        String osName = System.getProperty("os.name").toLowerCase();
//        String osArch = System.getProperty("os.arch").toLowerCase();
//        String libExtension;
//        String libPrefix = "";
//
//        if (osName.contains("win")) {
//            libExtension = ".dll";
//        } else if (osName.contains("mac")) {
//            libExtension = ".dylib";
//            libPrefix = "lib";
//        } else {
//            libExtension = ".so";
//            libPrefix = "lib";
//        }
//
//        String resourcePath = "/native/" + osName + "/" + osArch + "/" + libPrefix + libraryName + libExtension;
//
//        // Create temp directory if it doesn't exist
//        Path tempDir = Files.createTempDirectory("native-libs");
//        tempDir.toFile().deleteOnExit();
//
//        // Extract library to temp directory
//        Path tempFile = tempDir.resolve(libPrefix + libraryName + libExtension);
//        try (InputStream is = NativeLibraryLoader.class.getResourceAsStream(resourcePath)) {
//            if (is == null) {
//                throw new IOException("Library not found in resources: " + resourcePath);
//            }
//            Files.copy(is, tempFile, StandardCopyOption.REPLACE_EXISTING);
//        }
//
//        // Load the extracted library
//        System.load(tempFile.toAbsolutePath().toString());
//    }
//}