#include <jni.h>
#include <stdlib.h>
#include <android/log.h>
#include <fcntl.h>
#include <stdio.h>


#ifdef __cplusplus
extern "C"
#endif
{
  JNIEXPORT bool       JNICALL Java_filetest_casualdrive_com_filetest_MainActivity_saveFromJNI( JNIEnv* env, jobject thiz, jstring fFileName, jbyteArray srcArray );
  JNIEXPORT jbyteArray JNICALL Java_filetest_casualdrive_com_filetest_MainActivity_loadFromJNI( JNIEnv* env, jobject thiz, jstring fFileName );
  JNIEXPORT int        JNICALL Java_filetest_casualdrive_com_filetest_MainActivity_DummyJNI( JNIEnv* env, jobject thiz );
}


extern "C"



bool IsFileExist( const char *cFileName )
{
  FILE* fp = fopen( cFileName, "rb" );

  if( fp == NULL ){
    return false;
  }

  fclose( fp );
  return true;
}


bool CreateFile( const char *cFileName )
{
  int fd = open(cFileName, O_CREAT | O_RDWR, 0600 );

  if( fd < 0 ){
    return false;
  }
  close(fd);
  return true;
}



JNIEXPORT bool JNICALL Java_filetest_casualdrive_com_filetest_MainActivity_saveFromJNI( JNIEnv* env, jobject thiz, jstring jFileName, jbyteArray srcArray )
{
  const char *cFileName = env->GetStringUTFChars(jFileName, 0);
  //
  jbyte*   arrSrc    = (env)->GetByteArrayElements(srcArray,NULL);
  int      dataSize  = (env)->GetArrayLength(srcArray);

  __android_log_print(ANDROID_LOG_DEBUG, "save", "%s[%dbyte]", cFileName, dataSize );

  if( !IsFileExist(cFileName) )
  {
    if(!CreateFile(cFileName) ){
      return false;
    }
  }

  FILE* fp = fopen( cFileName, "wb" );

  if( (fp == NULL) || (dataSize <=0) ){
    return false;
  }

//==================
// crash!!
//==================
    fclose(fp);
    fp = NULL;
//==================


  fwrite( arrSrc, 1, dataSize, fp );

  fclose( fp );

  return true;
}


JNIEXPORT jbyteArray JNICALL Java_filetest_casualdrive_com_filetest_MainActivity_loadFromJNI( JNIEnv* env, jobject thiz, jstring jFileName )
{
  const char *cFileName = env->GetStringUTFChars(jFileName, 0);
  //
  long      fileSize  = 0;

  FILE* fp = fopen( cFileName, "rb" );

  if( fp == NULL ){
    return NULL;
  }

//==================
// crash!!
//==================
fclose(fp);
fp = NULL;
//==================


  fseek(fp,0,SEEK_END);
  fgetpos(fp,&fileSize);

  __android_log_print(ANDROID_LOG_DEBUG, "load", "%s[%ldbyte]", cFileName, fileSize );

  if( fileSize == 0 )
  {
    fclose(fp);
    return NULL;
  }
  fseek(fp,0,SEEK_SET);

  jbyteArray retBuf = (env)->NewByteArray( fileSize );
  jbyte*     arrBuf = (env)->GetByteArrayElements(retBuf,NULL);

  fread( arrBuf, 1, fileSize, fp );

  fclose( fp );

  return retBuf;
}





//================================================================================
// dummy Code for CrackProof
//================================================================================


#define dummy_data_0x10 __asm__ __volatile__ (".long 0x00"); __asm__ __volatile__ (".long 0x00"); __asm__ __volatile__ (".long 0x00"); __asm__ __volatile__ (".long 0x00");

#define dummy_data_0x100 \
  dummy_data_0x10 dummy_data_0x10 dummy_data_0x10 dummy_data_0x10 \
  dummy_data_0x10 dummy_data_0x10 dummy_data_0x10 dummy_data_0x10 \
  dummy_data_0x10 dummy_data_0x10 dummy_data_0x10 dummy_data_0x10 \
  dummy_data_0x10 dummy_data_0x10 dummy_data_0x10 dummy_data_0x10

#define dummy_data_0x1000 \
  dummy_data_0x100 dummy_data_0x100 dummy_data_0x100 dummy_data_0x100 \
  dummy_data_0x100 dummy_data_0x100 dummy_data_0x100 dummy_data_0x100 \
  dummy_data_0x100 dummy_data_0x100 dummy_data_0x100 dummy_data_0x100 \
  dummy_data_0x100 dummy_data_0x100 dummy_data_0x100 dummy_data_0x100

JNIEXPORT int JNICALL Java_filetest_casualdrive_com_filetest_MainActivity_DummyJNI( JNIEnv* env, jobject thiz )
{
  void* p = NULL;

  if (p == fopen ) return 1;
  if (p == fclose) return 2;
  if (p == fread ) return 3;
  if (p == fwrite) return 4;
  if (p == fseek ) return 5;
  if (p == printf ) return 6;
  if (p == fprintf) return 7;
  if (p == sprintf) return 8;
  if (p == strlen) return  9;
  if (p == strcpy) return 10;
  if (p == strcmp) return 11;
  if (p == strcat) return 12;

  dummy_data_0x1000
  dummy_data_0x1000

  return 0;
}




