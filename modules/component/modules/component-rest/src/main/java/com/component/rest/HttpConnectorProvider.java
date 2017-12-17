package com.component.rest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import org.glassfish.jersey.client.HttpUrlConnectorProvider;

public class HttpConnectorProvider extends HttpUrlConnectorProvider {

	public static final ThreadLocal<AtomicReference> curHttpConnection = new ThreadLocal<AtomicReference>();

	public HttpConnectorProvider() {
		super();
		connectionFactory(new HttpConnectionFactory());
	}

	public static class HttpConnectionFactory implements ConnectionFactory{
		
		@Override
		public HttpURLConnection getConnection(URL url) throws IOException {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			if(curHttpConnection.get() != null){
                curHttpConnection.get().set(connection);
			}
			return connection;
		}
	}
}
