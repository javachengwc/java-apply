package com.socket.demo.cp5;

/**
 * 当HTTP请求无法正确解析时，抛出此异常
 *
 */
public class MalformedRequestException extends Exception {

    MalformedRequestException() { }

    MalformedRequestException(String msg) {
	super(msg);
    }

    MalformedRequestException(Exception x) {
	super(x);
    }
}
