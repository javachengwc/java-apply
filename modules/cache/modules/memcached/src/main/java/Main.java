import com.cache.memclient.MemClientUtil;

import java.util.List;

/**
 *   Memcached的Java客户端目前有3个
 *   Memcached Client for Java      官方提供的基于传统阻塞io的客户端,最早且很稳定的一套api。
 *   SpyMemcached                   基于java nio实现，比Memcached Client for Java更高效。
 *   XMemcached                     基于java nio实现，比肩SpyMemcached，应用广泛。
 */
public class Main {

    public static void main(String args [])
    {
        MemClientUtil.add("a",100);
        MemClientUtil.add("b","bs");
        MemClientUtil.add("c","cc");
        System.out.println(MemClientUtil.get("b"));
        MemClientUtil.delete("b");
        List<String> keys = MemClientUtil.getAllKeys();
        System.out.println("keys count="+keys.size());
        for(String key:keys)
        {
            System.out.println(key+"-->"+MemClientUtil.get(key));
        }
    }
}
