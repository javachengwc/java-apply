package com.util.base;

import java.io.*;
import java.nio.CharBuffer;

/**
 * 数据流工具类
 */
public class StreamUtil {

    public final static int BUFFER_SIZE = 4096;

    public static void transStream(InputStream fis,OutputStream fos)
    {
        BufferedOutputStream bos=null;
        BufferedInputStream bis=null;
        try{
            bis=new BufferedInputStream(fis);
            bos=new BufferedOutputStream(fos);

            byte[] buf=new byte[4096];

            int len=-1;
            while((len=bis.read(buf))!=-1){
                bos.write(buf,0,len);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try{
                if(null!=bis){
                    bis.close();
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
            try{
                if(null!=bos){
                    bos.close();
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }

    }

    /**
     * 将InputStream转换成某种字符编码的String
     */
    public static String inputStream2String(InputStream in, String encoding) {
        String rt = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        try {
            while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
                outStream.write(data, 0, count);
        } catch (IOException e) {
            e.printStackTrace();
        }

        data = null;
        try {
            rt = new String(outStream.toByteArray(), encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return rt;
    }

    /**
     * 将String转换成InputStream
     */
    public static InputStream string2InputStream(String in) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(in.getBytes("UTF-8"));
        return is;
    }


    /**
     * 将InputStream转换成byte数组
     */
    public static byte[] inputStream2Byte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }


    /**
     * 将byte数组转换成InputStream
     */
    public static InputStream byte2InputStream(byte[] in) throws Exception {

        ByteArrayInputStream is = new ByteArrayInputStream(in);
        return is;
    }

    public static int copy(final Reader input, final Writer output) throws IOException {
        char[] buffer = new char[BUFFER_SIZE];
        int totalCount = 0;
        int count=-1;
        while ((count = input.read(buffer, 0, BUFFER_SIZE)) !=-1) {
            output.write(buffer, 0, count);
            totalCount += count;
        }
        output.flush();
        return totalCount;
    }

    public static int copy(final InputStream input, final OutputStream output) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int totalCount = 0;
        int count=-1;

        while ((count = input.read(buffer, 0, BUFFER_SIZE)) !=-1) {
            output.write(buffer, 0, count);
            totalCount += count;
        }
        output.flush();
        return totalCount;
    }

    public static byte []  readBytes(Reader reader,String encoding) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        Writer writer =  new OutputStreamWriter(out, encoding);
        copy(reader, writer);
        return out.toByteArray();
    }

    public static String reader2Str(Reader reader) throws IOException {
        if (reader == null) {
            return null;
        }
        try {
            StringBuilder to = new StringBuilder();
            CharBuffer buf = CharBuffer.allocate(BUFFER_SIZE);
            while (reader.read(buf) != -1) {
                buf.flip();
                to.append(buf);
                buf.clear();
            }
            return to.toString();
        } finally {
            ensureClosed(reader);
        }
    }

    public static void ensureClosed(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) { // NOPMD
            }
        }
    }
}
