package com.thrift.finagle.index;

import org.apache.thrift.TException;

public class IndexNewsOperatorServicesImpl implements IndexNewsOperatorServices.Iface{

	@Override
	public boolean deleteArtificiallyNews(int id) throws TException {
		// TODO Auto-generated method stub
		System.out.println("method success !!  id is :"+id);
		return true;
	}

	@Override
	public boolean indexNews(NewsModel indexNews) throws TException {
		// TODO Auto-generated method stub
		System.out.println("method success !!  data  is :");
		System.out.println(indexNews);
		return true;
	}

}
