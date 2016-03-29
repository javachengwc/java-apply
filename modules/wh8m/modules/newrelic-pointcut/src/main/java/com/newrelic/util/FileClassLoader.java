package com.newrelic.util;

import com.util.JarUtil;
import com.util.PackageUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 自定义文件类加载器
 */
public class FileClassLoader extends ClassLoader {

    private String classPath;

    public FileClassLoader()
    {

    }

    public FileClassLoader(String classPath)
    {
        this.classPath=classPath;
    }
    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        FileClassLoader fileClsLoader = new FileClassLoader();
        fileClsLoader.setClassPath("F:\\project\\modules\\data-service\\target\\dependency");
        Class cls = fileClsLoader.loadClass("com..service.DataService");
        Method[] mthds = cls.getMethods();
        for(Method mthd : mthds){
            String methodName = mthd.getName();
            System.out.println("mthd.name="+methodName);
        }

    }

    /**
     * 根据类名字符串从指定的目录查找类，并返回类对象
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classData = null;
        try {
            classData = loadClassData(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.defineClass(name, classData, 0, classData.length);
    }

    /**
     * 根据类名字符串加载类 byte 数据流
     * @param name   类名字符串  例如： com.cmw.entity.SysEntity
     * @return   返回类文件 byte 流数据
     * @throws IOException
     */
    private byte[] loadClassData(String name) throws IOException{

        String paths [] = classPath.split(",");
        for(String path:paths)
        {
           byte [] rt = getClassByte(path,name);
           if(rt!=null)
           {
               return rt;
           }
        }
        throw new RuntimeException("not find resource "+name);

    }

    private byte[] getClassByte(String path,String name) throws IOException{

        byte [] rt = getClassFromClass(path,name);
        if(rt==null)
        {
            rt=getClassFromJar(path,name);
        }
        return rt;
    }

    private byte[] getClassFromClass(String path,String name) throws IOException{

        File file = getFile(path,name);
        if(file!=null && file.exists() ) {
            FileInputStream fis = new FileInputStream(file);
            byte[] arrData = new byte[(int) file.length()];
            fis.read(arrData);
            return arrData;
        }
        return null;
    }

    private File getFile(String classPath,String name) throws FileNotFoundException {
        File dir = new File(classPath);
        if(!dir.exists())
        {
            return null;
        }
        String _classPath = classPath.replaceAll("[\\\\]", "/");
        System.out.println("_classPath:"+_classPath);
        int offset = _classPath.lastIndexOf("/");
        name = name.replaceAll("[.]", "/");
        if(offset != -1 && offset < _classPath.length()-1){
            _classPath += "/";
        }
        _classPath += name +".class";
        dir = new File(_classPath);
        if(!dir.exists()) {
            return null;
        }
        return dir;
    }

    private byte[] getClassFromJar(String classPath,String name) throws IOException
    {
        File file = new File(classPath);

        if(!file.exists())
        {
            return null;
        }
        File files [] =null;
        if(file.isDirectory())
        {
            files=file.listFiles();
        }else if(classPath.endsWith(".jar"))
        {
            files = new File[]{file};
        }
        if(files==null ||files.length<=0)
        {
            return null;
        }
        File findFile =null;
        for(File perFile:files) {
            if(!perFile.getPath().endsWith(".jar"))
            {
                continue;
            }
            String[] clazzs = PackageUtil.findClassesInJarFile(perFile.getPath());
            if (clazzs != null && clazzs.length>0) {
                for (String clazz : clazzs) {
                    if(clazz.equals(name))
                    {
                        findFile=perFile;
                        break;
                    }
                }
            }
            if(findFile!=null)
            {
                break;
            }
        }
        if(findFile==null)
        {

              return null;
        }
        String path = findFile.getPath();
        if(path.endsWith(".jar"))
        {
            return JarUtil.readJarEntry(path, className2jarEntryName(name));
        }
        if(path.endsWith(".class"))
        {
            FileInputStream fis = new FileInputStream(findFile);
            byte[] arrData = new byte[(int) file.length()];
            fis.read(arrData);
            return arrData;
        }
        return null;
    }

    public static String className2jarEntryName(String name)
    {
        if(StringUtils.isBlank(name))
        {
            return name;
        }
        name = name.replaceAll("[.]", "/");
        name +=".class";
        return name;
    }
}