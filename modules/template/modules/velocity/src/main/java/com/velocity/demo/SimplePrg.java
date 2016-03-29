package com.velocity.demo;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.StringWriter;
import java.util.Properties;

/**
 * 简单的一个velocity例子
 * 使用org.apache.velocity.app.Velocity类来应用单例模式，
 * 使用org.apache.velocity.app.VelocityEngine类来应用非单例模式（分离实例模式）
 */
public class SimplePrg {

    public static void main(String args []) {

        single();
        split();
    }

    public static void split()
    {
        VelocityEngine ve = new VelocityEngine();

        Properties p = new Properties();
        p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
        p.setProperty(Velocity.INPUT_ENCODING, "UTF-8");

        String path= Thread.currentThread().getContextClassLoader().getResource("").getPath();
        p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);

        ve.init(p);

        VelocityContext context = new VelocityContext();
        context.put("name", new String("haha"));

        Template template = null;
        try {

            template = ve.getTemplate("test.vm");
        }  catch (ResourceNotFoundException rnfe) {
            System.out.println("couldn't find the template");
        } catch (ParseErrorException pee) {
            System.out.println("syntax error : problem parsing the template");
        } catch (MethodInvocationException mie) {
            System.out.println("something invoked in the template throw an exception");
        } catch (Exception e) {
            System.out.println("exception:"+e.getMessage());
        }
        StringWriter sw = new StringWriter();
        template.merge(context, sw);

        String rt = sw.toString();

        System.out.println("--------split---------:\r\n"+rt);
    }

    public static void single()
    {
        String path= Thread.currentThread().getContextClassLoader().getResource("").getPath();
        System.out.println("path is "+path);

        Velocity.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, path);
        Velocity.init();

        VelocityContext context = new VelocityContext();
        context.put("name", new String("Velocity"));

        Template template = null;
        try {
            template = Velocity.getTemplate("test.vm");

        } catch (ResourceNotFoundException rnfe) {
            System.out.println("couldn't find the template");
        } catch (ParseErrorException pee) {
            System.out.println("syntax error : problem parsing the template");
        } catch (MethodInvocationException mie) {
            System.out.println("something invoked in the template throw an exception");
        } catch (Exception e) {
            System.out.println("exception:"+e.getMessage());
        }

        StringWriter sw = new StringWriter();
        template.merge(context, sw);
        String rt = sw.toString();
        System.out.println("--------single---------:\r\n"+rt);
    }
}
