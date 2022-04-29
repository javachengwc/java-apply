package com.util.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.util.regex.CommonRegex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetUtil {

    private static Logger logger = LoggerFactory.getLogger(NetUtil.class);

	public static final Pattern PATTERN_OF_IP = Pattern.compile( CommonRegex.REG_EXP_OF_IP );

    public static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

    public static final String LOCALHOST = "127.0.0.1";

    public static final String ANYHOST = "0.0.0.0";

    //是否合理的端口
	public static boolean isLegalPort( int port ) {
		if ( port <= 0 || port > 65535 ) {
			return false;
		}
		return true;
	}

	public static boolean isLegalPort( String port ) {
		try {
			return isLegalPort( Integer.parseInt( port ) );
		} catch ( NumberFormatException e ) {
			return false;
		}
	}

    //是否合理的ip
	public static boolean isLegalIP( String ip ) {
		Matcher match = PATTERN_OF_IP.matcher( ip );
		return match.matches();
	}

    //是否有效的地址
    public static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) {
            return false;
        }
        String name = address.getHostAddress();
        if(StringUtils.isBlank(name))
        {
            return false;
        }
        return (! ANYHOST.equals(name) && ! LOCALHOST.equals(name) && PATTERN_OF_IP.matcher(name).matches());
    }

    //主机端口是否开启
	public static boolean isHostOpenPort( String hostIp, int port ) {

		if ( StringUtils.isBlank(hostIp) ) {
			return false;
		}

        try {

            InetAddress address = InetAddress.getByName(hostIp);
            if (!address.isReachable(2000)) {
                //throw new Exception( "Can't connect host in 2000 ms: " + hostIp );
                return false;
            }
        }catch(Exception e)
        {
            return false;
        }

		Socket socket = null;
		try {
			socket = new Socket( hostIp, port );
			return true;
		} catch ( UnknownHostException e ) {
			//throw new Exception( "UnknownHost: " + hostIp );
            return false;
		} catch ( IOException e ) {
			return false;
		} finally {
            try {
                if (null != socket)
                    socket.close();
            }catch(Exception eee)
            {

            }
		}
	}

    public static String getIpByHost(String hostName) {
        try{
            return InetAddress.getByName(hostName).getHostAddress();
        }catch (UnknownHostException e) {
            return hostName;
        }
    }

    public static boolean isLocalHost(String host) {
        return (host != null) && ( LOCAL_IP_PATTERN.matcher(host).matches() || host.equalsIgnoreCase("localhost") );
    }

    public static String getLocalHost(){

        InetAddress address = getLocalAddress();
        String localHost = (address == null)?"":address.getHostAddress();
        return localHost;
    }

    /**
     * 获取IP地址
     */
    public static String getHostIp()
    {
        try
        {
            return InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e)
        {
        }
        return "127.0.0.1";
    }

    /**
     * 获取主机名
     */
    public static String getHostName()
    {
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException e)
        {
        }
        return "未知";
    }

    /**
     * 遍历本地网卡，返回第一个合理的IP
     */
    public static InetAddress getLocalAddress() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress)) {
                return localAddress;
            }
        } catch (Exception e) {
            logger.error("NetUtil getLocalAddress getLocalHost error,", e);
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces == null)
            {
                return null;
            }
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
                            } catch (Exception e) {
                                logger.error("NetUtil getLocalAddress interfaces error,", e);
                            }
                        }
                    }
                } catch (Throwable e) {
                    logger.error("NetUtil getLocalAddress interfaces error, ", e);
                }
            }

        } catch (Exception e) {
            logger.error("NetUtil getLocalAddress error,", e);
        }
        return localAddress;
    }


    public static void main(String args [])
    {
        String hostName="www.ccc.com";
        System.out.println(NetUtil.getIpByHost(hostName));
        String ipStr="127.0.0.1";
        String ipStr2="192.168.2.100";
        System.out.println(NetUtil.isLegalIP(ipStr));
        System.out.println(NetUtil.isLegalIP(ipStr2));
        System.out.println(NetUtil.isHostOpenPort("127.0.0.1",8081));
        System.out.println(NetUtil.getLocalHost());
    }
}