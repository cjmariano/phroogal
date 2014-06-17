/**
 * 
 */
package com.phroogal.core.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class that provides file upload services
 * @author Christopher Mariano
 *
 */
public interface FileUploadService {

	/**
	 * Uploads a file on a data store
	 * @param fileKeyName - unique identifier for the file
	 * @param file - A representation of an uploaded file received in a multipart request. 
	 * @return the file url location
	 * @throws IOException 
	 */
	public String uploadFile(String fileKeyName, MultipartFile file) throws IOException;
	
	/**
	 * Retruns the upload directory path of the upload location
	 * @return the upload directory path
	 */
	public String getUploadDirectory();
}
