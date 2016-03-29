package com.cache.memcached.demo;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.command.BinaryCommandFactory;
import net.rubyeye.xmemcached.exception.MemcachedException;
import net.rubyeye.xmemcached.transcoders.SerializingTranscoder;
import net.rubyeye.xmemcached.utils.AddrUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MemCachePrg {

	public static void main(String[] args) throws IOException, MemcachedException, InterruptedException, TimeoutException {
		MemcachedClient memcachedClient = getCache("127.0.0.1:11211");
		memcachedClient.add("name", 20000, "zhang");
		Object obj = memcachedClient.get("name", new SerializingTranscoder());
		System.out.println("==========" + obj);

		memcachedClient.shutdown();
	}

	public static MemcachedClient getCache(String address) throws IOException{
		MemcachedClientBuilder builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(address));
		//默认使用的TextCommandFactory，也就是文本协议。
		builder.setCommandFactory(new BinaryCommandFactory());//use binary protocol 

		MemcachedClient memcachedClient = builder.build();

		return memcachedClient;
	}

}
