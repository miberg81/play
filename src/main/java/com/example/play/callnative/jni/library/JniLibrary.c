#include <jni.h>
#include "Example.h"
#include <string.h>

JNIEXPORT jobject JNICALL Java_com_example_play_callnative_jni_library_javaclass_JniLibrary_processData
  (JNIEnv *env, jobject thisObj, jobject input) {
    
    // Get the input class
    jclass inClass = (*env)->FindClass(env, "com/example/play/callnative/jni/library/javaclass/IN");
    
    // Get field IDs for IN class
    jfieldID polyXFieldId = (*env)->GetFieldID(env, inClass, "polyX", "[D");
    jfieldID field2FieldId = (*env)->GetFieldID(env, inClass, "field2", "I");
    jfieldID field3FieldId = (*env)->GetFieldID(env, inClass, "field3", "D");
    
    // Get the input values
    jdoubleArray polyXArray = (*env)->GetObjectField(env, input, polyXFieldId);
    jint field2 = (*env)->GetIntField(env, input, field2FieldId);
    jdouble field3 = (*env)->GetDoubleField(env, input, field3FieldId);
    
    // Create C struct IN
    struct IN cInput;
    jdouble *polyXElements = (*env)->GetDoubleArrayElements(env, polyXArray, NULL);
    memcpy(cInput.polyX, polyXElements, 16 * sizeof(double));
    (*env)->ReleaseDoubleArrayElements(env, polyXArray, polyXElements, JNI_ABORT);
    
    cInput.field2 = field2;
    cInput.field3 = field3;
    
    // Call the existing C function from Example.c
    struct OUT cOutput = process_data(cInput);
    
    // Create Java OUT object
    jclass outClass = (*env)->FindClass(env, "com/example/play/callnative/jni/library/javaclass/OUT");
    jmethodID outConstructor = (*env)->GetMethodID(env, outClass, "<init>", "()V");
    jobject result = (*env)->NewObject(env, outClass, outConstructor);
    
    // Get Pos class and create Pos object
    jclass posClass = (*env)->FindClass(env, "com/example/play/callnative/jni/library/javaclass/Pos");
    jmethodID posConstructor = (*env)->GetMethodID(env, posClass, "<init>", "()V");
    jobject pos = (*env)->NewObject(env, posClass, posConstructor);
    
    // Set Pos fields
    jfieldID xFieldId = (*env)->GetFieldID(env, posClass, "X", "D");
    jfieldID yFieldId = (*env)->GetFieldID(env, posClass, "Y", "D");
    jfieldID zFieldId = (*env)->GetFieldID(env, posClass, "Z", "D");
    
    (*env)->SetDoubleField(env, pos, xFieldId, cOutput.poz.X);
    (*env)->SetDoubleField(env, pos, yFieldId, cOutput.poz.Y);
    (*env)->SetDoubleField(env, pos, zFieldId, cOutput.poz.Z);
    
    // Set OUT fields
    jfieldID pozFieldId = (*env)->GetFieldID(env, outClass, "poz", "Lcom/example/play/callnative/jni/library/javaclass/Pos;");
    jfieldID outField2Id = (*env)->GetFieldID(env, outClass, "field2", "Z");
    jfieldID outField3Id = (*env)->GetFieldID(env, outClass, "field3", "D");
    
    (*env)->SetObjectField(env, result, pozFieldId, pos);
    (*env)->SetBooleanField(env, result, outField2Id, cOutput.field2);
    (*env)->SetDoubleField(env, result, outField3Id, cOutput.field3);
    
    return result;
} 