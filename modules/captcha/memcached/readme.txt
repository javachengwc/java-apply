说明:memcached.zip是windows 32位的memcached安装压缩包

memcached.exe -d install                 -->安装
memcached.exe -d start                   -->启动
memcached.exe -p 10000 -m 512 -d start
memcached.exe -d shutdown/stop           -->关闭

以上命令最好在管理员命令行下执行
---------------------------------
memcached常用命令(telnet到memcache服务器 exp: telnet 192.168.1.1 11211)

stats                         -->查看基本信息
get key                       -->获取键为key的值
set key 100 0 3\r\n123\r\n    -->设置键为key的值为3个字符，过期时间为100秒
delete key                    -->删除key值
