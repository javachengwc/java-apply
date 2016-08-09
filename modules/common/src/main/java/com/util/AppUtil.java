package com.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Pattern;

/**
 * 应用工具类
 */
public class AppUtil {

    private static Logger logger = LoggerFactory.getLogger(AppUtil.class);

    private static int PID = -1;

    private static final String LOCALHOST = "127.0.0.1";

    private static final String ANYHOST = "0.0.0.0";

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    private static volatile InetAddress LOCAL_ADDRESS = null;

    /**
     * 获取程序的进程号
     */
    public static int getPid() {
        if (PID <= 0) {
            try {
                RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
                String name = runtime.getName(); // format: "pid@hostname"
                PID = Integer.parseInt(name.substring(0, name.indexOf('@')));
            } catch (Throwable e) {
                PID = 0;
            }
        }
        return PID;
    }

    /**
     * 获取cpu核数
     */
    public static int getCpuCoreNumber() {

        return Runtime.getRuntime().availableProcessors();
    }

    /**
     * 获取系统名称
     */
    public static String getOsName()
    {
        return System.getProperty("os.name");
    }

    public static String getSysPath()
    {
        String path= Thread.currentThread().getContextClassLoader().getResource("").toString();
        String temp=path.replaceFirst("file:/", "").replaceFirst("WEB-INF/classes/", "");
        String separator= System.getProperty("file.separator");
        String resultPath=temp.replaceAll("/", separator+separator);
        return resultPath;
    }

    public static String getClassPath()
    {
        String path= Thread.currentThread().getContextClassLoader().getResource("").toString();
        String temp=path.replaceFirst("file:/", "");
        String separator= System.getProperty("file.separator");
        String resultPath=temp.replaceAll("/", separator+separator);
        return resultPath;
    }
    public static String getSystempPath()
    {
        return System.getProperty("java.io.tmpdir");
    }

    public static String getSeparator()
    {
        return System.getProperty("file.separator");
    }

    /**
     * 获取本机的ip
     * @return
     */
    public static String getLocalHost(){
        InetAddress address = getLocalAddress();
        return address == null ? LOCALHOST : address.getHostAddress();
    }


    /**
     * 遍历本地网卡，返回第一个合理的IP。
     *
     * @return 本地网卡IP
     */
    public static InetAddress getLocalAddress() {
        if (LOCAL_ADDRESS != null)
            return LOCAL_ADDRESS;
        InetAddress localAddress = getLocalAddress0();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }


    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Throwable e) {
            logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    try {
                        NetworkInterface network = interfaces.nextElement();
                        Enumeration<InetAddress> addresses = network.getInetAddresses();
                        if (addresses != null) {
                            while (addresses.hasMoreElements()) {
                                try {
                                    InetAddress address = addresses.nextElement();
                                    if (isValidAddress(address)) {
                                        return address;
                                    }
                                } catch (Throwable e) {
                                    logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

    private static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress())
            return false;
        String name = address.getHostAddress();
        return (name != null
                && ! ANYHOST.equals(name)
                && ! LOCALHOST.equals(name)
                && IP_PATTERN.matcher(name).matches());
    }

    /**
     * 获取MAC地址的方法
     */
    private static String getMACAddress(InetAddress ia) throws Exception {
        // 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
        byte[] mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
        // 下面代码是把mac地址拼装成String
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append("-");
            }
            // mac[i] & 0xFF 是为了把byte转化为正整数
            String s = Integer.toHexString(mac[i] & 0xFF);
            sb.append(s.length() == 1 ? 0 + s : s);
        }
        // 把字符串所有小写字母改为大写成为正规的mac地址并返回
        return sb.toString().toUpperCase();
    }

    /**
     * 获取Unix网卡的mac地址 不靠谱，谨用
     */
    public static String getUnixMACAddress()
    {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        try
        {
            /**
             * Unix下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
             */
            process = Runtime.getRuntime().exec("ifconfig eth0");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null)
            {
                /**
                 * 寻找标示字符串[hwaddr]
                 */
                index = line.toLowerCase().indexOf("hwaddr");
                /**
                 * 找到了
                 */
                if (index != -1)
                {
                    /**
                     * 取出mac地址并去除2边空格
                     */
                    mac = line.substring(index + "hwaddr".length() + 1).trim();
                    break;
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (bufferedReader != null)
                {
                    bufferedReader.close();
                }
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
            bufferedReader = null;
            process = null;
        }
        return mac;
    }

    /**
     * 获取Linux网卡的mac地址. 不靠谱，谨用
     */
    public static String getLinuxMACAddress()
    {
        String mac = null;
        BufferedReader bufferedReader = null;
        Process process = null;
        try
        {
            /**
             * linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
             */
            process = Runtime.getRuntime().exec("ifconfig eth0");
            bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            int index = -1;
            while ((line = bufferedReader.readLine()) != null)
            {
                index = line.toLowerCase().indexOf("硬件地址");
                /**
                 * 找到了
                 */
                if (index != -1)
                {
                    /**
                     * 取出mac地址并去除2边空格
                     */
                    mac = line.substring(index + 4).trim();
                    break;
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (bufferedReader != null)
                {
                    bufferedReader.close();
                }
            } catch (IOException e1)
            {
                e1.printStackTrace();
            }
            bufferedReader = null;
            process = null;
        }
        return mac;
    }

    public static void main(String[] args) throws Exception
    {

        InetAddress ia = InetAddress.getLocalHost();// 获取本地IP对象
        System.out.println(ia);
        System.out.println("MAC ......... " + getMACAddress(ia));


        int pid =getPid();
        String address = getLocalHost();

        System.out.println("pid:"+pid+",address:"+address);

        String os = getOsName();
        System.out.println("os:"+os);

        if(StringUtils.isBlank(os))
        {
            return;
        }
        if (os.toLowerCase().startsWith("linux"))
        {
            String mac = getLinuxMACAddress();
            System.out.println("本地是Linux系统,MAC地址是:" + mac);
        } else
        {
            String mac = getUnixMACAddress();
            System.out.println("本地是Unix系统 MAC地址是:" + mac);
        }

    }
}
