package com.modulus.uno.utils

class TempFileService {

  File getTempFileFromMultipart(def multipartFile, String extension) {
    File tmpFile = File.createTempFile("tmp${extension.toUpperCase()}${new Date().getTime()}",".${extension}")
    FileOutputStream fos = new FileOutputStream(tmpFile)
    fos.write(multipartFile.getBytes())
    fos.close()
    tmpFile
  }
}
