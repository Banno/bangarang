package com.pongr.bangarang.s3

import com.amazonaws.services.s3._
import com.amazonaws.services.s3.model._
import akka.dispatch._
import java.io.{File, InputStream, ByteArrayInputStream}

class AmazonS3ClientAsync(s3: AmazonS3Client, implicit val context: ExecutionContext) {
  def putObjectAsync(request: PutObjectRequest): Future[PutObjectResult] = 
    Future { s3.putObject(request) }

  def putObjectAsync(bucket: String, key: String, file: File): Future[PutObjectResult] = 
    Future { s3.putObject(bucket, key, file) }

  def putObjectAsync(bucket: String, key: String, input: InputStream, metadata: ObjectMetadata): Future[PutObjectResult] = 
    Future { s3.putObject(bucket, key, input, metadata) }

  def putBytesAsync(bucket: String, key: String, bytes: Array[Byte], contentType: String): Future[PutObjectResult] = {
    val metadata = new ObjectMetadata() //TODO would be nice to have a case class version of ObjectMetadata, along with implicit conversions
    metadata.setContentType("image/jpeg")
    metadata.setContentLength(bytes.size)
    putObjectAsync(bucket, key, new ByteArrayInputStream(bytes), metadata)
  }

  def putJpegAsync(bucket: String, key: String, bytes: Array[Byte]): Future[PutObjectResult] = 
    putBytesAsync(bucket, key, bytes, "image/jpeg")
}

object AmazonS3ClientAsync {
  implicit def syncToAsync(s3: AmazonS3Client)(implicit context: ExecutionContext): AmazonS3ClientAsync = new AmazonS3ClientAsync(s3, context)
}