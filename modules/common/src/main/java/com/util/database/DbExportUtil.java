package com.util.database;

import com.util.Constant;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 数据库工具类
 */
public class DbExportUtil {

    /**
     * 备份数据库
     * @param host               主机host
     * @param port               端口
     * @param db                 数据库名
     * @param user               账号
     * @param password           密码
     * @param backUpDir          保存的目录
     * @return
     * mysqldump  -h 127.0.0.1 -P 3306 --user=root --password=root --lock-all-tables=true  --result-file=E:/ttt\db_admin-20150703115322.sql --default-character-set=utf8 db_admin
     */
    public static String backUpDb(String host,Integer port,String user,String password,String db,String backUpDir) {

        // 生成临时备份文件
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
        String fineName = db+"-" + sd.format(new Date());
        String sqlName = fineName + Constant.FILE_SUFFIX_SQL;
        try {
            File dir = new File(backUpDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String savePath= backUpDir+File.separator+sqlName;
            StringBuffer sbs = new StringBuffer();
            sbs.append("mysqldump ");
            sbs.append(" -h ").append(host);
            if(port!=null)
            {
                sbs.append(" -P ").append(port);
            }
            sbs.append(" --user=").append(user);
            sbs.append(" --password=").append(password);
            sbs.append(" --lock-all-tables=true ");
            sbs.append(" --result-file=").append(savePath);
            sbs.append(" --default-character-set=utf8 ");
            sbs.append(db);
            Runtime runtime = Runtime.getRuntime();
            //System.out.println(sbs.toString());
            Process process = runtime.exec(sbs.toString());
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
        }
        return sqlName;
    }

    /**
     * 备份数据库表
     * @param host               主机host
     * @param port               端口
     * @param db                 数据库名
     * @param table              表名，多个表名依空格隔开
     * @param user               账号
     * @param password           密码
     * @param backUpDir          保存的目录
     * @return
     * mysqldump  -h 127.0.0.1 -P 3306 -uroot -proot --skip-lock-tables  db_admin admin_role admin_resource --result-file=E:/ttt\db_admin-table-20150703115203.sql --default-character-set=utf8
     */
    public static String backUpTable(String host,Integer port,String user,String password,String db,String table,String backUpDir) {

        // 生成临时备份文件
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
        String fineName = db+"-table-" + sd.format(new Date());
        String sqlName = fineName + Constant.FILE_SUFFIX_SQL;
        try {
            File dir = new File(backUpDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String savePath= backUpDir+File.separator+sqlName;
            StringBuffer sbs = new StringBuffer();
            sbs.append("mysqldump ");
            sbs.append(" -h ").append(host);
            if(port!=null)
            {
                sbs.append(" -P ").append(port);
            }
            sbs.append(" -u").append(user);
            sbs.append(" -p").append(password);
            sbs.append(" --skip-lock-tables ");
            sbs.append(" ");
            sbs.append(db);
            sbs.append(" ");
            sbs.append(table);
            sbs.append(" --result-file=").append(savePath);
            sbs.append(" --default-character-set=utf8 ");
            Runtime runtime = Runtime.getRuntime();
            //System.out.println(sbs.toString());
            Process process = runtime.exec(sbs.toString());
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
        }
        return sqlName;
    }

    /**
     * 备份数据库带条件查询的表部分数据
     * @param host               主机host
     * @param port               端口
     * @param db                 数据库名
     * @param table              表名
     * @param condition          查询条件
     * @param user               账号
     * @param password           密码
     * @param backUpDir          保存的目录
     * @return
     * mysqldump  -h 127.0.0.1 -P 3306 -uroot -proot --skip-lock-tables  db_admin  -w "id>1 and id<10" admin_role --result-file=E:/ttt\db_admin-table-20150703115203.sql --default-character-set=utf8
     */
    public static String backUpTableWithCdn(String host,Integer port,String user,String password,String db,String table,String condition,String backUpDir) {

        // 生成临时备份文件
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
        String fineName = db+"-table-" + sd.format(new Date());
        String sqlName = fineName + Constant.FILE_SUFFIX_SQL;
        try {
            File dir = new File(backUpDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String savePath= backUpDir+File.separator+sqlName;
            StringBuffer sbs = new StringBuffer();
            sbs.append("mysqldump ");
            sbs.append(" -h ").append(host);
            if(port!=null)
            {
                sbs.append(" -P ").append(port);
            }
            sbs.append(" -u").append(user);
            sbs.append(" -p").append(password);
            sbs.append(" --skip-lock-tables ");
            sbs.append(" ");
            sbs.append(db);
            sbs.append(" -w");
            sbs.append(" \"");
            sbs.append(condition);
            sbs.append(" \"");
            sbs.append(" ");
            sbs.append(table);
            sbs.append(" --result-file=").append(savePath);
            sbs.append(" --default-character-set=utf8 ");
            Runtime runtime = Runtime.getRuntime();
            System.out.println(sbs.toString());
            Process process = runtime.exec(sbs.toString());
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
        }
        return sqlName;
    }

    /**
     * 备份查询结果集
     * @param host               主机host
     * @param port               端口
     * @param db                 数据库名
     * @param select             查询语句
     * @param user               账号
     * @param password           密码
     * @param backUpDir          保存的目录
     * @return
     * mysql --default-character-set=utf8 -h 127.0.0.1 -P 3306 -uroot -proot --skip-lock-tables  db_admin  -e "select * from table" --result-file=E:/ttt\data.txt
     */
    public static String backUpSelectResult(String host,Integer port,String user,String password,String db,String select,String backUpDir) {

        // 生成临时备份文件
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmmss");
        String fineName = db+"-table-" + sd.format(new Date());
        String sqlName = fineName + Constant.FILE_SUFFIX_SQL;
        try {
            File dir = new File(backUpDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            String savePath= backUpDir+File.separator+sqlName;
            StringBuffer sbs = new StringBuffer();
            sbs.append("mysql ");
            sbs.append(" --default-character-set=utf8 ");
            sbs.append(" -h ").append(host);
            if(port!=null)
            {
                sbs.append(" -P ").append(port);
            }
            sbs.append(" -u").append(user);
            sbs.append(" -p").append(password);
            sbs.append(" ");
            sbs.append(db);
            sbs.append(" -e");
            sbs.append(" \"");
            sbs.append(select);
            sbs.append(" \"");
            sbs.append(" >").append(savePath);
            System.out.println(sbs.toString());
            //Process process = runtime.exec(sbs.toString()); 用此代码执行mysql查询是执行不了的，需按下面代码处理
            Process process = Runtime.getRuntime().exec(new String[] { "cmd.exe", "/c", sbs.toString() });
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
        }
        return sqlName;
    }

    public static void main(String args [])
    {
        //String rt = backUpDb("127.0.0.1",3306,"root","root","db_admin","E:/ttt");
        //System.out.println(rt);

        String rt =backUpSelectResult("127.0.0.1",3306,"root","tooy","db_admin","select id,name from db_table limit 10","E:/ttt");
        System.out.println(rt);
    }
}
