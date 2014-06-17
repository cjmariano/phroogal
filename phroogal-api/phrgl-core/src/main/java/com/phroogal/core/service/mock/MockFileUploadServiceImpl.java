package com.phroogal.core.service.mock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.phroogal.core.service.FileUploadService;

/**
 * Mock implementation of the {@link FileUploadService} interface.
 * This uploads the file on the local/network machine
 * @author Christopher Mariano
 *
 */
public class MockFileUploadServiceImpl implements FileUploadService {

	@Value(value="${mock.tomcat.deployment.directory}")
	private String uploadPath;
	
	private static final String IMG_LOCATION = "img/profile_picture/";
	
	private static final String PATH_SEPARATOR = "/";
	
	@Override
	public String uploadFile(String fileKeyName, MultipartFile file) throws IOException {
		StringBuffer sbDeployedDir = new StringBuffer(uploadPath).append(PATH_SEPARATOR);
		StringBuffer sbUrlpath = new StringBuffer(IMG_LOCATION).append(fileKeyName).append(PATH_SEPARATOR);
		sbDeployedDir.append(sbUrlpath.toString());
		File f1 = new File(sbDeployedDir.toString());
		if (!f1.exists())
		{
			f1.mkdirs();
		}
		if (!file.isEmpty())
		{
			try
			{
				InputStream tempInputStream = file.getInputStream();
				FileOutputStream tempFos = new FileOutputStream(sbDeployedDir  + file.getOriginalFilename());
				IOUtils.copy(tempInputStream, tempFos);
				tempFos.close();
				tempInputStream.close();		
			}
			catch (IOException e)
			{
				return null;
			}			
		}
		else{
			return null;
		}
		return sbUrlpath.toString() + file.getOriginalFilename();
	}

	@Override
	public String getUploadDirectory() {
		return uploadPath.concat(IMG_LOCATION);
	}
}
