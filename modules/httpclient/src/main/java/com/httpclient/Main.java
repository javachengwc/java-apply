package com.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args []) throws Exception {
        testAccess();
        System.out.println(getHtml("http://www.baidu.com/"));
        downloadFile("http://c.hiphotos.baidu.com/image/pic/item/f2deb48f8c5494ee0a23a2182ff5e0fe99257eaa.jpg");
    }

    public static void testAccess() throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost=new HttpPost("http://localhost:8081/resource/category/page");
        String jsonStr="{\"data\": {\"entity\": {\"sysId\": 15},\"pageNo\": 1,\"pageSize\": 10},\"header\": {\"app\": \"string\"}}";
        StringEntity entity = new StringEntity(jsonStr,"UTF-8");
        httpPost.setEntity(entity);
        httpPost.setHeader("Content-Type","application/json;charset=utf8");
        CloseableHttpResponse response = httpClient.execute(httpPost);
        System.out.println(response.getStatusLine());
        System.out.println(EntityUtils.toString(response.getEntity()));
    }

    public static String getHtml(String url) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String html = EntityUtils.toString(entity);
        httpclient.close();
        return html;
    }

    /**下载文件**/
    public  static void downloadFile(String url) throws Exception {

        String destfilename = "E:\\tmp\\cat.jpg";
        //生成一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        InputStream in = entity.getContent();
        File file = new File(destfilename);
        try {
            FileOutputStream fout = new FileOutputStream(file);
            int l = -1;
            byte[] tmp = new byte[1024];
            while ((l = in.read(tmp)) != -1) {
                fout.write(tmp,0,l);
                //注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
            }
            fout.flush();
            fout.close();
        } finally {
            // 关闭低层流。
            in.close();
        }
        httpclient.close();
    }

    /**登陆人人**/
    public static void loginRr(String username,String password) throws Exception
    {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        //人人的登陆界面网址
        HttpPost httppost = new HttpPost("http://www.renren.com/PLogin.do");

        //打包将要传入的参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("email", username));
        params.add(new BasicNameValuePair("password",password));
        httppost.setEntity(new UrlEncodedFormEntity(params));

        try {
            //提交登录数据
            HttpResponse re = httpclient.execute(httppost);
            //获得跳转的网址
            Header locationHeader = re.getFirstHeader("Location");
            //登陆不成功
            if (locationHeader == null)
            {
                System.out.println("登陆不成功，请稍后再试!");
                return;
            }
            else//成功
            {
                String login_success=locationHeader.getValue();//获取登陆成功之后跳转链接
                System.out.println("成功之后跳转到的网页网址："+login_success);
                printText(login_success);
            }
        }catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally
        {
            httppost.abort();
            httpclient.close();
        }
    }

    public static void printText(String login_success) throws IOException
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(login_success);
        HttpResponse re2 = null;

        try {
            re2 = httpclient.execute(httpget);
            //输出登录成功后的页面
            String  str=EntityUtils.toString(re2.getEntity());
            System.out.println(str);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            httpget.abort();
            httpclient.close();
        }
    }

    //传送文件
    public static String transFile(String [] args) throws Exception
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://www.cc.com");
        File file = new File(args[0]);
        InputStreamEntity reqEntity = new InputStreamEntity(
                new FileInputStream(file), -1);
        reqEntity.setContentType("binary/octet-stream");
        reqEntity.setChunked(true);
        // FileEntity entity = new FileEntity(file, "binary/octet-stream");
        httppost.setEntity(reqEntity);
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String returnStr = EntityUtils.toString(entity, "UTF-8");
            return returnStr;
        }
        return null;
    }

    public static void getCookie () throws Exception
    {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建一个本地Cookie存储的实例
        CookieStore cookieStore = new BasicCookieStore();
        //创建一个本地上下文信息
        HttpContext localContext = new BasicHttpContext();
        //在本地上下问中绑定一个本地存储
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
        //设置请求的路径
        HttpGet httpget = new HttpGet("http://www.cc.com/");
        //传递本地的http上下文给服务器
        HttpResponse response = httpclient.execute(httpget, localContext);
        //获取本地信息
        HttpEntity entity = response.getEntity();
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
        }
        //获取cookie中的各种信息
        List<Cookie> cookies = cookieStore.getCookies();
        for (int i = 0; i < cookies.size(); i++) {
            System.out.println("Local cookie: " + cookies.get(i));
        }
        //获取消息头的信息
        Header[] headers = response.getAllHeaders();
        for (int i = 0; i<headers.length; i++) {
            System.out.println(headers[i]);
        }
    }
}
