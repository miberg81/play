#ifndef COMMON_H
#define COMMON_H

#include <stdbool.h>

#define CONST_10 10
#define CONST_16 16

// Define EF struct first
typedef struct EF {                                 
    double X;   
    double Y;   
    double Z;   
} EF;

// Define Page struct
typedef struct Page {
    double White;   
    double black;    
} Page;

// Define PF struct
typedef struct PF {
    Page pages[CONST_10];   
    int   numOfpages;        
    bool  applyFilter;          
} PF;

// Define LF struct
typedef struct LF {
    double lowerLimit;  
    double upperLimit;  
    bool   applyFilter; 
} LF;

// Define LP (LoggerParams) struct
typedef struct LP {
    bool enable; 
    bool records;     
    bool display;    
} LoggerParams;  // Using LoggerParams as the type name to match usage

typedef struct TAparams {
    PF pf;    
    LF upFilter;         
    LF dopplerFilter;    
    LF rangeFilter;      
    LF azFilter;         
    LF elFilter;         
    LoggerParams loggerParams;     
} TAparams;

typedef struct salary {
    int    ID;						
    EF     asset;            		
    double x[CONST_16]; 		
    double y[CONST_16]; 		
    double z[CONST_16]; 		
    double T0;           		
    int    NumOfCoeff;   		
    double ValidityTimeStart;	
    double ValidityTimeEnd;     
    int    department; 			
} salary;

typedef struct expense {
    int    ID;			
    bool   is;   
    double Score; 
} expense;

#endif  //COMMON_H
