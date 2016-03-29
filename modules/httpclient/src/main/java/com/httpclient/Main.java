package com.httpclient;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args []) throws Exception
    {
        System.out.println(getHtml("http://www.baidu.com/"));

        downloadFile("http://c.hiphotos.baidu.com/image/pic/item/f2deb48f8c5494ee0a23a2182ff5e0fe99257eaa.jpg");
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
}
