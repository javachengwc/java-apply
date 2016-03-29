package com.flower.multipart;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * 摘自Struts MultiPartRequest interface，用于封装Multipart/form的表单
 */
/**
 * Abstract wrapper class HTTP requests to handle multi-part data. <p>
 *
 */
public interface IMultiPartRequest extends HttpServletRequest{

    public void parse(HttpServletRequest request, String saveDir) throws IOException;
    
    /**
     * Returns an enumeration of the parameter names for uploaded files
     *
     * @return an enumeration of the parameter names for uploaded files
     */
    public Enumeration<String> getFileParameterNames();

    /**
     * Returns the content type(s) of the file(s) associated with the specified field name
     * (as supplied by the client browser), or <tt>null</tt> if no files are associated with the
     * given field name.
     *
     * @param fieldName input field name
     * @return an array of content encoding for the specified input field name or <tt>null</tt> if
     *         no content type was specified.
     */
    public String[] getContentType(String fieldName);

    /**
     * Returns a {@link java.io.File} object for the filename specified or <tt>null</tt> if no files
     * are associated with the given field name.
     *
     * @param fieldName input field name
     * @return a File[] object for files associated with the specified input field name
     */
    public File[] getFile(String fieldName);

    /**
     * Returns a String[] of file names for files associated with the specified input field name
     *
     * @param fieldName input field name
     * @return a String[] of file names for files associated with the specified input field name
     */
    public String[] getFileNames(String fieldName);

    /**
     * Returns the file system name(s) of files associated with the given field name or
     * <tt>null</tt> if no files are associated with the given field name.
     *
     * @param fieldName input field name
     * @return the file system name(s) of files associated with the given field name
     */
    public String[] getFilesystemName(String fieldName);

    /**
     * Returns the specified request parameter.
     *
     * @param name the name of the parameter to get
     * @return the parameter or <tt>null</tt> if it was not found.
     */
    public String getParameter(String name);

    /**
     * Returns an enumeration of String parameter names.
     *
     * @return an enumeration of String parameter names.
     */
    public Enumeration<String> getParameterNames();

    /**
     * Returns a list of all parameter values associated with a parameter name. If there is only
     * one parameter value per name the resulting array will be of length 1.
     *
     * @param name the name of the parameter.
     * @return an array of all values associated with the parameter name.
     */
    public String[] getParameterValues(String name);

    /**
     * Returns a list of error messages that may have occurred while processing the request.
     * If there are no errors, an empty list is returned. If the underlying implementation
     * (ie: pell, cos, jakarta, etc) cannot support providing these errors, an empty list is
     * also returned. This list of errors is repoted back to the
     *
     * @return a list of Strings that represent various errors during parsing
     */
    public List<String> getErrors();
    
    /**
     * 
     * @return  key是入参名（注意不是文件名），value是该入参名对应的文件集合
     */
    public Map<String, File[]> getAcceptFiles() ;
    
    /**
     * @return key是入参名,value是该入参对应文件的content type集合
     */
    public Map<String, String[]> getAcceptContentTypes();
    
    /**
     * key是入参名，value是入参对应文件的文件名
     * @return
     */
    public Map<String, String[]>  getAcceptFileFileNames();
}
