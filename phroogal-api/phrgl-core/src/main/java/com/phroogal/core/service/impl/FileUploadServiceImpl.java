package com.phroogal.core.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.phroogal.core.exception.FileUploadFailedException;
import com.phroogal.core.service.FileUploadService;

/**
 * Default implementation of the {@link FileUploadService} interface.
 * This implementation stores data on an Amazon S3 bucket
 * @author Christopher Mariano
 *
 */
@Service
public class FileUploadServiceImpl implements FileUploadService {

private static final Logger log = Logger.getLogger(FileUploadServiceImpl.class);
	
	@Value(value="${aws.accessKeyID}")
	private String awsAccessKeyID;
	
	@Value(value="${aws.secretKey}")
	private String awsSecretKey;
	
	@Value(value="${aws.s3.endpoint}")
	private String awsS3Endpoint;
	
	@Value(value="${aws.s3.bucketname.images}")
	private String awsBucketNameForImages;
	
	@Override
	public String uploadFile(String fileKeyName, MultipartFile file) throws IOException {
		InputStream stream = null;
		try {
			AWSCredentials awsCredentials = new BasicAWSCredentials(awsAccessKeyID, awsSecretKey);
			ClientConfiguration config = new ClientConfiguration();
			config.setSocketTimeout(0);
			AmazonS3 s3Client = new AmazonS3Client(awsCredentials, config);
			s3Client.setEndpoint(awsS3Endpoint);
			stream = file.getInputStream();
			ObjectMetadata metadata = new ObjectMetadata();
			PutObjectRequest putObjectRequest = new PutObjectRequest(awsBucketNameForImages + "/", fileKeyName, stream, metadata);
			s3Client.putObject(putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead));
		} catch (Exception ex) {
			log.error(ex.getMessage());
			throw new FileUploadFailedException(ex);
		} 
		
		return String.format("%s/%s//%s", awsS3Endpoint, awsBucketNameForImages, fileKeyName);
	}

	@Override
	public String getUploadDirectory() {
		return String.format("%s/%s", awsS3Endpoint, awsBucketNameForImages);
	}
}
