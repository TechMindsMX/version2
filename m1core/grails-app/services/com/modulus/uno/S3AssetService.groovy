package com.modulus.uno

import grails.transaction.Transactional
import com.amazonaws.services.s3.model.*
import java.security.MessageDigest

@Transactional
class S3AssetService {

  def amazonWebService
  def grailsApplication

  def createTempFilesOfMultipartsFiles(document,type) {
    def nameFile = document.filename
    def file = File.createTempFile(nameFile,".${nameFile.tokenize('.').last()}")
    document.transferTo(file)
    def s3AsseetProperties = createElementsOfS3Asset(file,type)
    amazonWebService.s3.putObject(new PutObjectRequest(s3AsseetProperties.bucket, "${s3AsseetProperties.title}.${s3AsseetProperties.mimeType}",file).withCannedAcl(CannedAccessControlList.PublicRead))
    saveS3Asset(s3AsseetProperties)
  }

  def createFileToUpload(File file, def type){
    def s3AsseetProperties = createElementsOfS3Asset(file,type)
    amazonWebService.s3.putObject(new PutObjectRequest(s3AsseetProperties.bucket, "${s3AsseetProperties.title}.${s3AsseetProperties.mimeType}",file).withCannedAcl(CannedAccessControlList.PublicRead))
    saveS3Asset(s3AsseetProperties)
  }

  private def createElementsOfS3Asset(file,type) {
    def name = file.name
    def extention = name.tokenize('.').last()
    def hash = MessageDigest.getInstance("MD5").digest(name.bytes).encodeHex().toString()
    [title:hash,mimeType:extention,bucket:grailsApplication.config.grails.plugin.awssdk.bucketName,localPath:grailsApplication.config.grails.plugin.awssdk.url.content,protocol:grailsApplication.config.grails.plugin.awssdk.protocol,originalName:name,type:type]
  }

  private def saveS3Asset(properties) {
    def s3Asset = new S3Asset()
    s3Asset.bucket = properties.bucket
    s3Asset.title = properties.title
    s3Asset.mimeType = properties.mimeType
    s3Asset.localPath = properties.localPath
    s3Asset.localUrl = "${properties.protocol}${properties.bucket}.${properties.localPath}${properties.title}.${properties.mimeType}"
    s3Asset.originalName = properties.originalName
    s3Asset.type = properties.type
    s3Asset.status = true
    s3Asset.save()
  }



}
