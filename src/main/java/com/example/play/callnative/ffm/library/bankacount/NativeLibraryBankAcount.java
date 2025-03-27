package com.example.play.callnative.ffm.library.bankacount;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;

public class NativeLibraryBankAcount {
    // Memory layouts for all C structures
    private static final MemoryLayout EF_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_DOUBLE.withName("X"),
        ValueLayout.JAVA_DOUBLE.withName("Y"),
        ValueLayout.JAVA_DOUBLE.withName("Z")
    );
    
    private static final MemoryLayout PAGE_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_DOUBLE.withName("White"),
        ValueLayout.JAVA_DOUBLE.withName("black")
    );

    private static final MemoryLayout PF_LAYOUT = MemoryLayout.structLayout(
        MemoryLayout.sequenceLayout(10, PAGE_LAYOUT).withName("pages"),
        ValueLayout.JAVA_INT.withName("numOfpages"),
        ValueLayout.JAVA_BOOLEAN.withName("applyFilter"),
        MemoryLayout.paddingLayout(3)  // Padding for alignment
    );

    private static final MemoryLayout LF_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_DOUBLE.withName("lowerLimit"),
        ValueLayout.JAVA_DOUBLE.withName("upperLimit"),
        ValueLayout.JAVA_BOOLEAN.withName("applyFilter"),
        MemoryLayout.paddingLayout(7)  // Padding for alignment
    );

    private static final MemoryLayout LP_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_BOOLEAN.withName("enable"),
        ValueLayout.JAVA_BOOLEAN.withName("records"),
        ValueLayout.JAVA_BOOLEAN.withName("display"),
        MemoryLayout.paddingLayout(5)  // Padding for alignment
    );

    private static final MemoryLayout TAPARAMS_LAYOUT = MemoryLayout.structLayout(
        PF_LAYOUT.withName("pf"),
        LF_LAYOUT.withName("upFilter"),
        LF_LAYOUT.withName("dopplerFilter"),
        LF_LAYOUT.withName("rangeFilter"),
        LF_LAYOUT.withName("azFilter"),
        LF_LAYOUT.withName("elFilter"),
        LP_LAYOUT.withName("loggerParams")
    );

    private static final MemoryLayout SALARY_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("ID"),
        MemoryLayout.paddingLayout(4),  // Align for EF
        EF_LAYOUT.withName("asset"),
        MemoryLayout.sequenceLayout(16, ValueLayout.JAVA_DOUBLE).withName("x"),
        MemoryLayout.sequenceLayout(16, ValueLayout.JAVA_DOUBLE).withName("y"),
        MemoryLayout.sequenceLayout(16, ValueLayout.JAVA_DOUBLE).withName("z"),
        ValueLayout.JAVA_DOUBLE.withName("T0"),
        ValueLayout.JAVA_INT.withName("NumOfCoeff"),
        MemoryLayout.paddingLayout(4),  // Align for double
        ValueLayout.JAVA_DOUBLE.withName("ValidityTimeStart"),
        ValueLayout.JAVA_DOUBLE.withName("ValidityTimeEnd"),
        ValueLayout.JAVA_INT.withName("department"),
        MemoryLayout.paddingLayout(4)   // Final alignment
    );

    private static final MemoryLayout EXPENSE_LAYOUT = MemoryLayout.structLayout(
        ValueLayout.JAVA_INT.withName("ID"),
        ValueLayout.JAVA_BOOLEAN.withName("is"),
        MemoryLayout.paddingLayout(3),  // Align for double
        ValueLayout.JAVA_DOUBLE.withName("Score")
    );
    
    // Get field offsets for easier access
    private static final long X_OFFSET = EF_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("X"));
    private static final long Y_OFFSET = EF_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("Y"));
    private static final long Z_OFFSET = EF_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("Z"));
    
    private static final long SALARY_ID_OFFSET = SALARY_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("ID"));
    private static final long SALARY_ASSET_OFFSET = SALARY_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("asset"));
    private static final long SALARY_X_OFFSET = SALARY_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("x"));
    private static final long SALARY_Y_OFFSET = SALARY_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("y"));
    private static final long SALARY_Z_OFFSET = SALARY_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("z"));
    private static final long SALARY_T0_OFFSET = SALARY_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("T0"));
    private static final long SALARY_NUMOFCOEFF_OFFSET = SALARY_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("NumOfCoeff"));
    private static final long SALARY_VALIDITYTIMESTART_OFFSET = SALARY_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("ValidityTimeStart"));
    private static final long SALARY_VALIDITYTIMEEND_OFFSET = SALARY_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("ValidityTimeEnd"));
    private static final long SALARY_DEPARTMENT_OFFSET = SALARY_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("department"));
    
    private static final long EXPENSE_ID_OFFSET = EXPENSE_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("ID"));
    private static final long EXPENSE_IS_OFFSET = EXPENSE_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("is"));
    private static final long EXPENSE_SCORE_OFFSET = EXPENSE_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("Score"));
    
    // Method handle for the native function
    private static final MethodHandle PROCESS_DATA;
    
    // Load the native library and get the function handle
    static {
        try {
            String libraryPath = System.getProperty("user.dir");
            System.setProperty("java.library.path", libraryPath);
            
            try {
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    System.load(libraryPath + "/bankAcount.dll");
                } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
                    System.load(libraryPath + "/libbankAcount.dylib");
                } else {
                    System.load(libraryPath + "/libbankAcount.so");
                }
            } catch (UnsatisfiedLinkError e) {
                System.loadLibrary("bankAcount");
            }
            
            SymbolLookup symbolLookup = SymbolLookup.loaderLookup();
            Linker linker = Linker.nativeLinker();
            
            FunctionDescriptor descriptor = FunctionDescriptor.of(
                EXPENSE_LAYOUT,  // Return type
                SALARY_LAYOUT    // Parameter type
            );
            
            PROCESS_DATA = linker.downcallHandle(
                symbolLookup.find("process_data").orElseThrow(),
                descriptor
            );
        } catch (Throwable e) {
            throw new RuntimeException("Failed to load native library", e);
        }
    }
    
    // Java representation of all C structures
    public static class EF {
        public double X;
        public double Y;
        public double Z;
    }
    
    public static class Page {
        public double White;
        public double black;
    }
    
    public static class PF {
        public Page[] pages = new Page[10];
        public int numOfpages;
        public boolean applyFilter;
    }
    
    public static class LF {
        public double lowerLimit;
        public double upperLimit;
        public boolean applyFilter;
    }
    
    public static class LP {
        public boolean enable;
        public boolean records;
        public boolean display;
    }
    
    public static class TAparams {
        public PF pf = new PF();
        public LF upFilter = new LF();
        public LF dopplerFilter = new LF();
        public LF rangeFilter = new LF();
        public LF azFilter = new LF();
        public LF elFilter = new LF();
        public LP loggerParams = new LP();
    }
    
    public static class Salary {
        public int ID;
        public EF asset = new EF();
        public double[] x = new double[16];
        public double[] y = new double[16];
        public double[] z = new double[16];
        public double T0;
        public int NumOfCoeff;
        public double ValidityTimeStart;
        public double ValidityTimeEnd;
        public int department;
    }
    
    public static class Expense {
        public int ID;
        public boolean is;
        public double Score;
    }
    
    // Method to call the native function
    public static Expense processData(Salary input) {
        try (Arena arena = Arena.ofConfined()) {
            // Allocate memory for input struct
            MemorySegment inSegment = arena.allocate(SALARY_LAYOUT);
            
            // Fill the input struct
            inSegment.set(ValueLayout.JAVA_INT, SALARY_ID_OFFSET, input.ID);
            MemorySegment assetSegment = inSegment.asSlice(SALARY_ASSET_OFFSET, EF_LAYOUT.byteSize());
            assetSegment.set(ValueLayout.JAVA_DOUBLE, X_OFFSET, input.asset.X);
            assetSegment.set(ValueLayout.JAVA_DOUBLE, Y_OFFSET, input.asset.Y);
            assetSegment.set(ValueLayout.JAVA_DOUBLE, Z_OFFSET, input.asset.Z);
            MemorySegment xArray = inSegment.asSlice(SALARY_X_OFFSET, 16 * Double.BYTES);
            for (int i = 0; i < 16; i++) {
                xArray.setAtIndex(ValueLayout.JAVA_DOUBLE, i, input.x[i]);
            }
            MemorySegment yArray = inSegment.asSlice(SALARY_Y_OFFSET, 16 * Double.BYTES);
            for (int i = 0; i < 16; i++) {
                yArray.setAtIndex(ValueLayout.JAVA_DOUBLE, i, input.y[i]);
            }
            MemorySegment zArray = inSegment.asSlice(SALARY_Z_OFFSET, 16 * Double.BYTES);
            for (int i = 0; i < 16; i++) {
                zArray.setAtIndex(ValueLayout.JAVA_DOUBLE, i, input.z[i]);
            }
            inSegment.set(ValueLayout.JAVA_DOUBLE, SALARY_T0_OFFSET, input.T0);
            inSegment.set(ValueLayout.JAVA_INT, SALARY_NUMOFCOEFF_OFFSET, input.NumOfCoeff);
            inSegment.set(ValueLayout.JAVA_DOUBLE, SALARY_VALIDITYTIMESTART_OFFSET, input.ValidityTimeStart);
            inSegment.set(ValueLayout.JAVA_DOUBLE, SALARY_VALIDITYTIMEEND_OFFSET, input.ValidityTimeEnd);
            inSegment.set(ValueLayout.JAVA_INT, SALARY_DEPARTMENT_OFFSET, input.department);
            
            // Allocate memory for the output struct
            MemorySegment outSegment = arena.allocate(EXPENSE_LAYOUT);
            
            try {
                // Cast arena to SegmentAllocator for exact type matching
                outSegment = (MemorySegment) PROCESS_DATA.invokeExact((SegmentAllocator)arena, inSegment);
            } catch (Throwable e) {
                throw new RuntimeException("Error invoking native function", e);
            }
            
            // Convert the result to Java object
            Expense result = new Expense();
            result.ID = outSegment.get(ValueLayout.JAVA_INT, EXPENSE_ID_OFFSET);
            result.is = outSegment.get(ValueLayout.JAVA_BOOLEAN, EXPENSE_IS_OFFSET);
            result.Score = outSegment.get(ValueLayout.JAVA_DOUBLE, EXPENSE_SCORE_OFFSET);
            
            return result;
        } catch (Throwable e) {
            throw new RuntimeException("Error calling native function", e);
        }
    }
} 