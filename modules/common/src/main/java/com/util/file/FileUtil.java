package com.util.file;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class FileUtil {

    private static Logger logger  =  LoggerFactory.getLogger(FileUtil.class);

    /**
     * 读取指定文件的数据，并返回字符串
     */
    public static List<String> readFile(String fileName){
        List<String> result = new LinkedList<String>();
        BufferedReader reader =null;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String line;
            while((line = reader.readLine())!=null){
                result.add(line);
            }
            return result;
        }catch(FileNotFoundException e){
            logger.error("File ["+fileName+"] not found, read failure,return null");
            return null;
        }catch(IOException e){
            logger.error("IOException: "+e.getMessage()+" during reading file ["+fileName+"],return null");
            return null;
        }finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            }catch(Exception e)
            {
                logger.error("reader close error,",e);
            }
        }

    }

    /**
     * 将content写入指定文件，模式为追加
     */
    public static void writeFile(String fileName,String content,boolean isAppend) {
        logger.debug("write content ["+content+"] to file ["+fileName);
        if(!isAppend){
            File file = new File(fileName);
            if(file.exists()){
                logger.debug("mode is not append");
                logger.debug("file ["+fileName+"] exists, delete the file...");
                file.delete();
            }
        }
        BufferedWriter writer =null;
        try{
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName,true)));
            writer.write(content);
            writer.flush();
        }catch(FileNotFoundException e){
            logger.error("File ["+fileName+"] not found, write data failure");
        }catch(IOException e){
            logger.error("IOException: "+e.getMessage()+" during write to file ["+fileName+"]");
        }finally{
            try {
                if (writer != null) {
                    writer.close();
                }
            }catch(Exception e)
            {
                logger.error("writer close error,",e);
            }
        }
    }

    public static void writeFile(File file, String content) {
        String filePath = file.getPath();
        FileOutputStream out = null;
        try {
            out =new FileOutputStream(file);
            IOUtils.write(content, out, "utf-8");
        } catch(IOException e) {
            logger.error("PythonExecutor setFileContent error,filePath={},",filePath,e);
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * 创建文件夹
     */
    public static void createFolder(String folderPathAndName) {
        try {

            File newFolder = new File(folderPathAndName);
            if (newFolder.exists()) {
                return;
            }

            if (newFolder.getParentFile().exists()) {
                newFolder.mkdir();
            } else {
                createFolder(newFolder.getParentFile().getPath());
                newFolder.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除文件
    public static void delFile(String filePathAndName) {
        try {
            File delFile = new File(filePathAndName);
            delFile.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除文件夹
    public static void deleteFileFolder(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete();
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    FileUtil.deleteFileFolder(files[i]); // 把每个文件 用这个方法进行迭代
                }
                file.delete();
            }
        } else {
            System.out.println("this filePath is not exist！" + '\n');
        }
    }

    //复制文件夹与文件夹下的文件
    public static void copyFolder(String rawFolderPathAndName,
                                  String newFolderPathAndName) {
        try {
            (new File(newFolderPathAndName)).mkdirs();
            File a = new File(rawFolderPathAndName);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (rawFolderPathAndName.endsWith(File.separator)) {
                    temp = new File(rawFolderPathAndName + file[i]);
                } else {
                    temp = new File(rawFolderPathAndName + File.separator
                            + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(
                            newFolderPathAndName + "/"
                                    + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if (temp.isDirectory()) {
                    copyFolder(rawFolderPathAndName + "/" + file[i],
                            newFolderPathAndName + "/" + file[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    //文件复制
    public static void copyFile(String inputFile,String outputFile) throws FileNotFoundException{
        File sFile = new File(inputFile);
        File tFile = new File(outputFile);
        FileInputStream fis = new FileInputStream(sFile);
        FileOutputStream fos = new FileOutputStream(tFile);
        int temp = 0;
        byte[] buf = new byte[10240];
        try {
            while((temp = fis.read(buf))!=-1){
                fos.write(buf, 0, temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void copyFile2( File from, File to ) throws IOException
    {
        FileChannel inChannel = new FileInputStream( from ).getChannel();
        FileChannel outChannel = new FileOutputStream( to ).getChannel();
        try
        {
            long maxCount = Long.MAX_VALUE;
            long size = inChannel.size();
            long position = 0;
            while ( position < size )
            {
                position += inChannel.transferTo( position, maxCount, outChannel );
            }
        }
        finally
        {
            if (inChannel!=null) {
                inChannel.close();
            }
            if (outChannel!=null) {
                outChannel.close();
            }
        }
    }

    /**
     * key-value数据写properties文件
     */
    public static void writePropertiesFile(String filePathAndName , Map<String,String> keyValueMap) throws Exception{
        if(filePathAndName==null || keyValueMap==null){
            return;
        }
        File file = new File(filePathAndName);
        if(!file.exists()){
            file.createNewFile();
        }


        FileOutputStream fos = null;
        FileInputStream fis = null;
        Properties prop = new Properties();

        try {
            fis = new FileInputStream(file);
            prop.load(fis);
            fos = new FileOutputStream(file);

            for(Map.Entry<String,String> entry : keyValueMap.entrySet()){
                prop.setProperty(entry.getKey(), entry.getValue());
            }
            prop.store(fos,null);
            fos.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                }
                if (null!=fis){
                    fis.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }
    }

	/**
	 * 带点, 如 : .java
	 * @return
	 */
	public static String getFileExtension(File file) {
		if (null != file) {
			String fileName = file.getName();
			return extensionCutter(fileName, true);
		}
		return "";
	}
	
	public static String getFileExtension(String filePath) {
		return extensionCutter(filePath, false);
	}
	
	public static String getFileExtensionWithPoint(String filePath) {
		return extensionCutter(filePath, true);
	}
	
	private static String extensionCutter(String filePath, boolean withPoint) {
		int beginIndex = filePath.lastIndexOf(".");
		if (beginIndex != -1) {
			beginIndex = withPoint ? beginIndex : beginIndex + 1;
			return filePath.substring(beginIndex);
		}
		return "";
	}

    public static String readFile(String file, String charset) throws UnsupportedEncodingException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream is = null;
        try {
            is = new FileInputStream(file);
            byte[] bs = new byte[1024];
            int n = is.read(bs, 0, bs.length);
            while (n > 0) {
                baos.write(bs, 0, n);
                n = is.read(bs, 0, bs.length);
            }
        } catch (Exception e) {
            e.printStackTrace();

            if (is != null)
                try {
                    is.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
        }
        finally
        {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
        return new String(baos.toByteArray(), charset);
    }
}
