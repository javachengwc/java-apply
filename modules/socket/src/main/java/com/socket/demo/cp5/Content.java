package com.socket.demo.cp5;

/**
 * 表示服务器发送给客户的正文内容
 */
public interface Content extends Sendable {
  //内容的类型
  String type();

  //在内容还没有准备之前，即还没有调用prepare()方法之前，length()方法返回-1。
  long length();
}
