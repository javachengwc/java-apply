package com.cache;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class JedisClusterMain {

    public static void main(String[] args) throws Exception {
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();
        nodes.add(new HostAndPort("127.0.0.1", 7001));
        nodes.add(new HostAndPort("127.0.0.1", 7002));
        nodes.add(new HostAndPort("127.0.0.1", 7003));
        nodes.add(new HostAndPort("127.0.0.1", 7004));
        nodes.add(new HostAndPort("127.0.0.1", 7005));
        nodes.add(new HostAndPort("127.0.0.1", 7006));

        JedisCluster jedisCluster = new JedisCluster(nodes);

        String key = "name";
        String rt = jedisCluster.set(key, "what");
        System.out.println(rt);

        String value = jedisCluster.get(key);
        System.out.println(value);

        jedisCluster.close();
    }
}
