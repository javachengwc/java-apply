package com.mina.server;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineDecoder;
import org.apache.mina.filter.codec.textline.TextLineEncoder;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * 在线态服务端
 */
public class AdminScoketServer extends IoHandlerAdapter {

	private final String TOKEN_KEY = "session_token_key";

	private IoAcceptor acceptor = null;

	private static Set<IoSession> tmpSessions = new HashSet<IoSession>();

	public static void main(String[] args) {
		new AdminScoketServer(1234);
	}

	static {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				List<IoSession> removeSessions = new ArrayList<IoSession>();
				for (IoSession session : tmpSessions) {
					if (System.currentTimeMillis() - session.getCreationTime() > 60000l) {
						removeSessions.add(session);
					}
				}
				for (IoSession session : removeSessions) {
					session.close();
				}
			}
		}, 60000l, 60000l);
	}

	public AdminScoketServer(int port) {
		try {
			acceptor = new NioSocketAcceptor();
			acceptor.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(new TextLineEncoder(Charset.forName("utf-8"), "\0"), new TextLineDecoder(
							Charset.forName("utf-8"), "\0")));
			// acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.setHandler(this);
			acceptor.bind(new InetSocketAddress(port));
			System.out.println("AdminScoketServer:" + port);
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public void shutdown() {
		if (acceptor != null) {
			acceptor.dispose();
		}
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		System.out.println("session create:" + session);
		tmpSessions.add(session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		cause.printStackTrace(System.out);
		session.close();
	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		System.out.println("messageReceived:" + message);
		String msg = (String) message;
		if ("<policy-file-request/>".equals(msg)) {
			session.write("<?xml version=\"1.0\"?><cross-domain-policy><allow-http-request-headers-from domain=\"*\" headers=\"*\" /><allow-access-from to-ports=\"*\" domain=\"*\"/></cross-domain-policy>");
			return;
		}
		String[] tokens = msg.split(" ");
		if (tokens.length == 2 && "LG".equals(tokens[0])) {
			if (StringUtils.isNotBlank(tokens[1])) {
				session.setAttribute(TOKEN_KEY, tokens[1]);
				AdminHolder.putAdminSession(tokens[1], session);
				tmpSessions.remove(session);
				System.out.println(tokens[1] + " login success");
			}
		}
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		System.out.println("session close:" + session);
		String token = (String) session.getAttribute(TOKEN_KEY);
		if (StringUtils.isNotBlank(token)) {
			session.setAttribute(TOKEN_KEY, null);
			AdminHolder.removeAdminSession(token);
		}
		tmpSessions.remove(session);
	}
}
