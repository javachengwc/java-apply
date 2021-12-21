package com.email.server;

import com.caucho.hessian.server.HessianServlet;
import com.email.service.EmailService;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MailServer {

	public static void main(String[] args) {

        //Server server = new Server();
        //String serverConfig = "jetty.xml";
        //new XmlConfiguration(serverConfig).configure(server);
        //server.start();

		ApplicationContext applicationContext= new ClassPathXmlApplicationContext("ApplicationContext-context.xml");

		Server server = (Server) applicationContext.getBean("jettyServer");
		Context root = new Context(server, "/", Context.SESSIONS);

		HessianServlet servlet = new HessianServlet();
		servlet.setHomeAPI(EmailService.class);
		servlet.setHome(applicationContext.getBean("emailService"));
		root.addServlet(new ServletHolder(servlet), "/mail");
        //访问路径:http://127.0.0.1:10001/mail
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
