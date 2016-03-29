package com.flower.interceptor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.flower.BaseAction;
import com.flower.DataFilter;
import com.flower.invocation.ActionInvocation;
import com.flower.multipart.IMultiPartRequest;
import com.flower.multipart.MultipartRequest;
import com.util.BlankUtil;


/**
 * 根据HTTP content-type字段重新封装request
 */
public abstract class AbstractFileUploadInterceptor implements Interceptor {

	protected final Logger logger = Logger.getLogger(this.getClass());
	
    protected Set<String> notAllowedExtensionsSet = Collections.emptySet();

	public AbstractFileUploadInterceptor() {
		notAllowedExtensionsSet = new HashSet<String>();
		notAllowedExtensionsSet.add("exe");
	}
	/* (non-Javadoc)
	 * @see com.flower.interceptor.Interceptor#intercept(com.flower.invocation.ActionInvocation)
	 */
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		
		HttpServletRequest request = DataFilter.getRequest();
		String content_type = request.getContentType();
		if (content_type != null && content_type.contains("multipart/form-data") && !(request instanceof IMultiPartRequest)) {
			MultipartRequest multipartRequest = new MultipartRequest(request, getMaximumSize());
			multipartRequest.parse(multipartRequest, getSaveDir());
			filter(multipartRequest, invocation);
			DataFilter.setRequest(multipartRequest);
			String result = invocation.invoke();
			cleanUpRequest(multipartRequest);
			return result;

		}else{
			return invocation.invoke();
		}
	}
	
	protected void filter(MultipartRequest multipartRequest, ActionInvocation invocation){
		Enumeration<String> fileParameterNames = multipartRequest.getFileParameterNames();
		BaseAction action = invocation.getAction();
		while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
            // get the value of this input tag
            String inputName = (String) fileParameterNames.nextElement();

            // get the content type
            String[] contentType = multipartRequest.getContentType(inputName);

            if (!BlankUtil.isBlank(contentType)) {
                // get the name of the file from the input tag
                String[] fileName = multipartRequest.getFileNames(inputName);

                if (!BlankUtil.isBlank(fileName)) {
                    // get a File object for the uploaded File
                    File[] files = multipartRequest.getFile(inputName);
                    if (files != null && files.length > 0) {
                        List<File> acceptedFiles = new ArrayList<File>(files.length);
                        List<String> acceptedContentTypes = new ArrayList<String>(files.length);
                        List<String> acceptedFileNames = new ArrayList<String>(files.length);

                        for (int index = 0; index < files.length; index++) {
                            if (validate(action, files[index], fileName[index], contentType[index], inputName, invocation)) {
                                acceptedFiles.add(files[index]);
                                acceptedContentTypes.add(contentType[index]);
                                acceptedFileNames.add(fileName[index]);
                            }
                        }

                        if (!acceptedFiles.isEmpty()) {
                        	multipartRequest.getAcceptFiles().put(inputName, acceptedFiles.toArray(new File[acceptedFiles.size()]));
                        	multipartRequest.getAcceptContentTypes().put(inputName, acceptedContentTypes.toArray(new String[acceptedContentTypes.size()]));
                        	multipartRequest.getAcceptFileFileNames().put(inputName, acceptedFileNames.toArray(new String[acceptedFileNames.size()]));
                        }
                    }
                } else {
                	logger.warn("invalid file name");
                }
            } else {
                    logger.warn("invalid content type");
            }
		}
	}
	
	
	protected boolean validate(Object action, File file, String filename, String contentType, String inputName, ActionInvocation invocation) {
        boolean fileIsAcceptable = false;

        // If it's null the upload failed
        if (file == null) { // file null
            logger.warn("uploading fail, file null");
        } else if (getMaximumSize() != 0 && getMaximumSize() < file.length()) { // size too large
            logger.warn("file too large");
        } else if ((!notAllowedExtensionsSet.isEmpty()) && (hasNotAllowedExtension(notAllowedExtensionsSet, filename))) {
            logger.warn("extension not allowed");
        } else {
            fileIsAcceptable = true;
        }

        return fileIsAcceptable;
    }
	
	   private boolean hasNotAllowedExtension(Collection<String> extensionCollection, String filename) {
	        if (filename == null) {
	            return false;
	        }

	        String lowercaseFilename = filename.toLowerCase();
	        for (String extension : extensionCollection) {
	            if (lowercaseFilename.endsWith(extension)) {
	                return true;
	            }
	        }

	        return false;
	    }
	
	/**
     * Return the path to save uploaded files to (this is configurable).
     *
     * @return the path to save uploaded files to
     */
    protected abstract String getSaveDir();

    protected abstract long getMaximumSize();
    
    public void cleanUpRequest(HttpServletRequest request) throws IOException {
        if (!(request instanceof MultipartRequest)) {
            return;
        }

        MultipartRequest multiWrapper = (MultipartRequest) request;

        Enumeration fileParameterNames = multiWrapper.getFileParameterNames();
        while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
            String inputValue = (String) fileParameterNames.nextElement();
            File[] files = multiWrapper.getFile(inputValue);

            for (File currentFile : files) {
                logger.info("com.flower.interceptor.AbstractFileUploadInterceptor.cleanUpRequest removing file : " + currentFile.getCanonicalPath() + " for input : " + inputValue);

                if ((currentFile != null) && currentFile.isFile()) {
                    if (!currentFile.delete()) {
                        logger.warn("Resource Leaking:  Could not remove uploaded file '" + currentFile.getCanonicalPath() + "'.");
                    }
                }
            }
        }
    }
}
