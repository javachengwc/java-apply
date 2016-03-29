package com.util.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.util.regex.CommonRegex;
import org.apache.commons.lang3.StringUtils;

public class NetUtil {

	public static final Pattern PATTERN_OF_IP = Pattern.compile( CommonRegex.REG_EXP_OF_IP );

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

	public static boolean isLegalIP( String ip ) {
		Matcher match = PATTERN_OF_IP.matcher( ip );
		return match.matches();
	}

	public static boolean isHostOpenPort( String hostIp, int port ) throws Exception {

		if ( StringUtils.isBlank(hostIp) ) {
			return false;
		}

		InetAddress address = InetAddress.getByName( hostIp );
		if ( !address.isReachable( 2000 ) ) {
			throw new Exception( "Can't connect host in 2000 ms: " + hostIp );
		}

		Socket socket = null;
		try {
			socket = new Socket( hostIp, port );
			return true;
		} catch ( UnknownHostException e ) {
			throw new Exception( "UnknownHost: " + hostIp );
		} catch ( IOException e ) {
			return false;
		} finally {
			if ( null != socket )
				socket.close();
		}
	}
}