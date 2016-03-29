package com.flower.multipart;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;


/**
 * 封装了Multipart的请求,参考自Struts的MultiPartRequest类。
 */
public class MultipartRequest extends HttpServletRequestWrapper implements IMultiPartRequest{

	protected final Logger logger = Logger.getLogger(this.getClass());
	
    // maps parameter name -> List of FileItem objects
    protected Map<String,List<FileItem>> files = new HashMap<String,List<FileItem>>();

    // maps parameter name -> List of param values
    protected Map<String,List<String>> params = new HashMap<String,List<String>>();
    
    // any errors while processing this request
    protected List<String> errors = new ArrayList<String>();
    
    protected long maxSize = Long.MAX_VALUE;
    
    public void setMaxSize(String maxSize) {
        this.maxSize = Long.parseLong(maxSize);
    }
	/**
	 * @param request
	 */
	public MultipartRequest(HttpServletRequest request, long maxSize) {
		super(request);
		this.maxSize = maxSize;  
	}
	
	public void parse(HttpServletRequest request, String saveDir) throws IOException {
        try {
            processUpload(request, saveDir);
        } catch (FileUploadException e) {
        	logger.error("MultipartRequest parse fail", e);
        	errors.add(e.getMessage());
        }
    }
	
	private void processUpload(HttpServletRequest request, String saveDir) throws FileUploadException, UnsupportedEncodingException {
        for (FileItem item : parseRequest(request, saveDir)) {
            logger.debug("Found item " + item.getFieldName());
            if (item.isFormField()) {
                processNormalFormField(item, request.getCharacterEncoding());
            } else {
                processFileField(item);
            }
        }
    }
	
	@SuppressWarnings("unchecked")
	private List<FileItem> parseRequest(HttpServletRequest servletRequest, String saveDir) throws FileUploadException {
        DiskFileItemFactory fac = createDiskFileItemFactory(saveDir);
        ServletFileUpload upload = new ServletFileUpload(fac);
        upload.setSizeMax(maxSize);
        return upload.parseRequest(createRequestContext(servletRequest));
    }

	private DiskFileItemFactory createDiskFileItemFactory(String saveDir) {
        DiskFileItemFactory fac = new DiskFileItemFactory();
        // Make sure that the data is written to file
        fac.setSizeThreshold(0);
        if (saveDir != null) {
            fac.setRepository(new File(saveDir));
        }
        return fac;
    }
	
	private RequestContext createRequestContext(final HttpServletRequest req) {
        return new RequestContext() {
            public String getCharacterEncoding() {
                return req.getCharacterEncoding();
            }

            public String getContentType() {
                return req.getContentType();
            }

            public int getContentLength() {
                return req.getContentLength();
            }

            public InputStream getInputStream() throws IOException {
                InputStream in = req.getInputStream();
                if (in == null) {
                    throw new IOException("Missing content in the request");
                }
                return req.getInputStream();
            }
        };
    }
	
	private void processNormalFormField(FileItem item, String charset) throws UnsupportedEncodingException {
        logger.debug("Item is a normal form field");
        List<String> values;
        if (params.get(item.getFieldName()) != null) {
            values = params.get(item.getFieldName());
        } else {
            values = new ArrayList<String>();
        }

        // note: see http://jira.opensymphony.com/browse/WW-633
        // basically, in some cases the charset may be null, so
        // we're just going to try to "other" method (no idea if this
        // will work)
        if (charset != null) {
            values.add(item.getString(charset));
        } else {
            values.add(item.getString());
        }
        params.put(item.getFieldName(), values);
    }

	private void processFileField(FileItem item) {
        logger.debug("Item is a file upload");

        // Skip file uploads that don't have a file name - meaning that no file was selected.
        if (item.getName() == null || item.getName().trim().length() < 1) {
            logger.debug("No file has been uploaded for the field: " + item.getFieldName());
            return;
        }

        List<FileItem> values;
        if (files.get(item.getFieldName()) != null) {
            values = files.get(item.getFieldName());
        } else {
            values = new ArrayList<FileItem>();
        }

        values.add(item);
        files.put(item.getFieldName(), values);
    }
	
    public Enumeration<String> getFileParameterNames() {
        return Collections.enumeration(files.keySet());
    }

    /* (non-Javadoc)
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getContentType(java.lang.String)
     */
    public String[] getContentType(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<String> contentTypes = new ArrayList<String>(items.size());
        for (FileItem fileItem : items) {
            contentTypes.add(fileItem.getContentType());
        }

        return contentTypes.toArray(new String[contentTypes.size()]);
    }

    /* (non-Javadoc)
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getFile(java.lang.String)
     */
    public File[] getFile(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<File> fileList = new ArrayList<File>(items.size());
        for (FileItem fileItem : items) {
            File storeLocation = ((DiskFileItem) fileItem).getStoreLocation();
            if(fileItem.isInMemory() && storeLocation!=null && !storeLocation.exists()) {
                try {
                    storeLocation.createNewFile();
                } catch (IOException e) {
                    logger.error("Cannot write uploaded empty file to disk: " + storeLocation.getAbsolutePath(),e);
                }
            }
            fileList.add(storeLocation);
        }

        return fileList.toArray(new File[fileList.size()]);
    }

    /* (non-Javadoc)
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getFileNames(java.lang.String)
     */
    public String[] getFileNames(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<String> fileNames = new ArrayList<String>(items.size());
        for (FileItem fileItem : items) {
            fileNames.add(getCanonicalName(fileItem.getName()));
        }

        return fileNames.toArray(new String[fileNames.size()]);
    }

    /* (non-Javadoc)
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getFilesystemName(java.lang.String)
     */
    public String[] getFilesystemName(String fieldName) {
        List<FileItem> items = files.get(fieldName);

        if (items == null) {
            return null;
        }

        List<String> fileNames = new ArrayList<String>(items.size());
        for (FileItem fileItem : items) {
            fileNames.add(((DiskFileItem) fileItem).getStoreLocation().getName());
        }

        return fileNames.toArray(new String[fileNames.size()]);
    }

    /* (non-Javadoc)
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getParameter(java.lang.String)
     */
    public String getParameter(String name) {
        List<String> v = params.get(name);
        if (v != null && v.size() > 0) {
            return v.get(0);
        }

        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getParameterNames()
     */
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(params.keySet());
    }

    /* (non-Javadoc)
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequest#getParameterValues(java.lang.String)
     */
    public String[] getParameterValues(String name) {
        List<String> v = params.get(name);
        if (v != null && v.size() > 0) {
            return v.toArray(new String[v.size()]);
        }

        return null;
    }

    /**
     * Returns the canonical name of the given file.
     *
     * @param filename  the given file
     * @return the canonical name of the given file
     */
    private String getCanonicalName(String filename) {
        int forwardSlash = filename.lastIndexOf("/");
        int backwardSlash = filename.lastIndexOf("\\");
        if (forwardSlash != -1 && forwardSlash > backwardSlash) {
            filename = filename.substring(forwardSlash + 1, filename.length());
        } else if (backwardSlash != -1 && backwardSlash >= forwardSlash) {
            filename = filename.substring(backwardSlash + 1, filename.length());
        }

        return filename;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    // key是入参名（注意不是文件名），value是该入参名对应的文件集合
    private Map<String, File[]> acceptFiles = new HashMap<String, File[]>();
	/**
	 * 获取通过interceptor要求的文件map，key是入参名（注意不是文件名），value是该入参名对应的文件集合
	 */
	public Map<String, File[]> getAcceptFiles() {
		return acceptFiles;
	}
	
	// key是入参名,value是该入参对应文件的content type集合
	private Map<String, String[]> acceptContentTypes = new HashMap<String, String[]>();
	
	public Map<String, String[]> getAcceptContentTypes(){
		return acceptContentTypes;
	}
	// key是入参名，value是入参对应文件的文件名
	private Map<String, String[]> acceptFileFileNames = new HashMap<String, String[]>();
	
	public Map<String, String[]>  getAcceptFileFileNames(){
		return acceptFileFileNames;
	}
	
    /**
     * @see javax.servlet.http.HttpServletRequest#getParameterMap()
     */
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> map = new HashMap<String, String[]>();
        Enumeration<?> enumeration = getParameterNames();

        while (enumeration.hasMoreElements()) {
            String name = (String) enumeration.nextElement();
            map.put(name, this.getParameterValues(name));
        }

        return map;
    }

}
