package com.util.lang;

public class SystemUtil {

    //java版本号
    public static String JAVA_VERSION="java.version";
    //jre目录
    public static String JAVA_HOME="java.home";
    //Java虚拟机版本号
    public static String JAVA_VM_VERSION="java.vm.version";
    //Java类版本号
    public static String JAVA_CLASS_VERSION="java.class.version";
    //Java类路径
    public static String JAVA_CLASS_PATH="java.class.path";
    //Java lib路径
    public static String JAVA_LIBRARY_PATH="java.library.path";
    //Java输入输出临时路径
    public static String JAVA_IO_TMPDIR="java.io.tmpdir";
    //Java编译器
    public static String JAVA_COMPILER="java.compiler";
    // Java执行路径
    public static String JAVA_EXT_DIRS="java.ext.dirs";
    // 操作系统名称
    public static String OS_NAME="os.name";
    // 操作系统的架构
    public static String OS_ARCH="os.arch";
    // 操作系统版本号
    public static String OS_VERSION="os.version";
    // 文件分隔符
    public static String FILE_SEPARATOR="file.separator";
    // 路径分隔符
    public static String PATH_SEPARATOR="path.separator";
    // 直线分隔符
    public static String LINE_SEPARATOR="line.separator";
    // 系统用户名
    public static String USER_NAME="user.name";
    // 用户的主目录
    public static String USER_HOME="user.home";
    // 当前程序所在目录
    public static String USER_DIR="user.dir";

    public static void printSystemProp() {
        System.out.println(JAVA_VERSION+":" + System.getProperty(JAVA_VERSION));
        System.out.println(JAVA_HOME+":" + System.getProperty(JAVA_HOME));
        System.out.println(JAVA_VM_VERSION+":" + System.getProperty(JAVA_VM_VERSION)); // Java虚拟机版本号
        System.out.println(JAVA_CLASS_VERSION+":" + System.getProperty(JAVA_CLASS_VERSION)); // Java类版本号
        System.out.println(JAVA_CLASS_PATH+":" + System.getProperty(JAVA_CLASS_PATH)); // Java类路径
        System.out.println(JAVA_LIBRARY_PATH+":" + System.getProperty(JAVA_LIBRARY_PATH)); // Java lib路径
        System.out.println(JAVA_IO_TMPDIR+":" + System.getProperty(JAVA_IO_TMPDIR)); // Java输入输出临时路径
        System.out.println(JAVA_COMPILER+":" + System.getProperty(JAVA_COMPILER)); // Java编译器
        System.out.println(JAVA_EXT_DIRS+":" + System.getProperty(JAVA_EXT_DIRS)); // Java执行路径
        System.out.println(OS_NAME+":" + System.getProperty(OS_NAME)); // 操作系统名称
        System.out.println(OS_ARCH+":" + System.getProperty(OS_ARCH)); // 操作系统的架构
        System.out.println(OS_VERSION+":" + System.getProperty(OS_VERSION)); // 操作系统版本号
        System.out.println(FILE_SEPARATOR+":" + System.getProperty(FILE_SEPARATOR)); // 文件分隔符
        System.out.println(PATH_SEPARATOR+":" + System.getProperty(PATH_SEPARATOR)); // 路径分隔符
        System.out.println(LINE_SEPARATOR+":" + System.getProperty(LINE_SEPARATOR)); // 直线分隔符
        System.out.println(USER_NAME+":" + System.getProperty(USER_NAME)); // 用户名
        System.out.println(USER_HOME+":" + System.getProperty(USER_HOME)); // 用户的主目录
        System.out.println(USER_DIR+":" + System.getProperty(USER_DIR)); // 当前程序所在目录
    }

    public static void main(String args []) {
        printSystemProp();
    }
}
