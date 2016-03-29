package com.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class HelloImpl extends UnicastRemoteObject implements IHello {

	private static final long serialVersionUID = 1L;

    public HelloImpl() throws RemoteException {
    }

    public String helloWorld() throws RemoteException {
    	System.out.println("helloWorld  success ");
        return "Hello World!";
    }

    public String sayHelloToSomeBody(String someBodyName) throws RemoteException {
    	System.out.println("helloWorld  success ");
        return "你好，" + someBodyName + "!";
    }
}
