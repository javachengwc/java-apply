/**
 * 
 */
package com.mina.server;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Map.Entry;

import com.util.encrypt.MD5;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.mina.core.session.IoSession;

/**
 * 管理员信息保存处
 */
public class AdminHolder {

	private static Map<String, ExpiredAdminMember> admins = new HashMap<String, ExpiredAdminMember>();
	private static Map<String, IoSession> sessions = new HashMap<String, IoSession>();
	private static ThreadLocal<String> local = new ThreadLocal<String>();

	static {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				List<String> removeKeys = new ArrayList<String>();
				for (Entry<String, ExpiredAdminMember> entry : admins.entrySet()) {
					ExpiredAdminMember admin = entry.getValue();
					if (admin == null || admin.isExpried()) {
						removeKeys.add(entry.getKey());
						continue;
					}
					long restTime = admin.getExpiredTime() - System.currentTimeMillis();
					if (restTime < 180000) {
						IoSession session=sessions.get(entry.getKey());
						if(session!=null&&session.isConnected()){
							session.write("您离本次登录超时只剩下" + restTime / 1000+ "秒了，还在发呆！");
						}
					}
				}
				for (String key : removeKeys) {
					ExpiredAdminMember admin = removeAdmin(key);
					System.out.println("**Admin expired:" + admin);
				}
			}
		}, 60000l, 60000l);
	}

	public static String getAdmin(String token) {
		ExpiredAdminMember admin = admins.get(token);
		if (admin != null && !admin.isExpried()) {
			admin.flush();
			String adminMember = admin.getAdmin();
			local.set(adminMember);
			return adminMember;
		}
		return null;
	}
	
	public static IoSession getAdminSession(String token) {
		return sessions.get(token);
	}
	
	public static void removeAdminSession(String token) {
		IoSession session = sessions.remove(token);
		if (session != null) {
			session.close();
		}
	}
	
	public static void putAdminSession(String token, IoSession session) {
		IoSession oldSession = sessions.get(token);
		if (oldSession != null) {
			oldSession.close();
		}
		sessions.put(token, session);
	}

	public static String putAdmin(String admin) {

		String token = createToken(admin);
		admins.put(token, new ExpiredAdminMember(admin));
		local.set(admin);
		return token;
	}

	public static ExpiredAdminMember removeAdmin(String token) {
		ExpiredAdminMember admin = admins.remove(token);
		removeAdminSession(token);
		return admin;
	}

	public static String getCurrentAdmin() {
		return local.get();
	}

	public static String createToken(String admin) {
		return MD5.getMD5("ccc_" + admin);
	}
	
	public static List<String> getOnlineAdmins() {
		List<String> onlineAdmins = new ArrayList<String>(admins.size());
		for (Entry<String, ExpiredAdminMember> entry : admins.entrySet()) {
			ExpiredAdminMember eadmin = entry.getValue();
			String admin = eadmin.getAdmin();
			onlineAdmins.add(admin);
		}
		return onlineAdmins;
	}
	
	public static void sendMessage(String message, String... loginNames) {
		for (Entry<String, ExpiredAdminMember> entry : admins.entrySet()) {
			ExpiredAdminMember admin = entry.getValue();
			if (admin == null) {
				continue;
			}
			if (ArrayUtils.indexOf(loginNames, admin.getAdmin()) >= 0) {
				sendMessage(sessions.get(entry.getKey()), message);
			}
				
		}
	}

	public static void sendMessage2All(String message) {
		for (Entry<String, IoSession> entry : sessions.entrySet()) {
			sendMessage(entry.getValue(), message);
		}
	}

	private static void sendMessage(IoSession session, String message) {
		if (session != null && session.isConnected()) {
			session.write(message);
		}
	}

	private static class ExpiredAdminMember {

		private static final long expiredDurationMs = 60 * 60 * 1000;// 60 minutes
		private long expiredTime;
		private long lastFlushTime;
		private final String admin;

		public ExpiredAdminMember(String admin) {
			this.expiredTime = System.currentTimeMillis() + expiredDurationMs;
			this.admin = admin;
		}

		public boolean isExpried() {
			return expiredTime <= System.currentTimeMillis();
		}
		
		public long getExpiredTime() {
			return expiredTime;
		}

		public long getLastFlushTime() {
			return lastFlushTime;
		}

		public void flush() {
			this.expiredTime = System.currentTimeMillis() + expiredDurationMs;
			this.lastFlushTime = System.currentTimeMillis();
		}

		String getAdmin() {
			return admin;
		}
		
		@Override
		public String toString() {
			return admin + ":" + ((expiredTime - System.currentTimeMillis()) / 60000) + ":"
					+ new Timestamp(lastFlushTime);
		}
	}
}
