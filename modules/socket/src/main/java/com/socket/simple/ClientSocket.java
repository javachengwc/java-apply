package com.socket.simple;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeoutException;

import com.socket.simple.vo.BaseRequest;
import com.socket.simple.vo.BaseResponse;
import com.socket.simple.vo.ErrorResponse;
import com.util.file.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * TCPProtocol的实现类
 * 
 */
public class ClientSocket {

    private static Logger logger = LoggerFactory.getLogger(ClientSocket.class);

    // 缓冲区大小
	private static final int BufferSize = 1024;

	public ClientSocket() {

	}

	public void handleAccept(SelectionKey key) throws IOException {
		SocketChannel clientChannel = ((ServerSocketChannel) key.channel())
				.accept();
		clientChannel.configureBlocking(false);
		clientChannel.register(key.selector(), SelectionKey.OP_READ,
				ByteBuffer.allocate(BufferSize));
	}

	public void handleRead(SelectionKey key) throws IOException,
			TimeoutException {
		SocketChannel clientChannel = (SocketChannel) key.channel();

		int objLen = NoBlockSocketUtil.readInt(clientChannel);

		System.out.println("objLen:" + objLen);
		if(objLen>30000){
			handleWrite(key, new ErrorResponse(-1,"objLen too long:"+objLen),-1);
			return;
		}
		ByteBuffer objBufRead = ByteBuffer.allocate(objLen);
		NoBlockSocketUtil.read(clientChannel, objBufRead.array());

//		SerializationContext serializationContext = new SerializationContext();
//		Amf3Input amfIn = new Amf3Input(serializationContext);
//		amfIn.setInputStream(new ByteArrayInputStream(objBufRead.array()));
		try {
			//Object as3Obj = amfIn.readObject()
			Object as3Obj=null;
			if (as3Obj instanceof BaseRequest) {
				BaseRequest bReq = (BaseRequest)as3Obj;
				//Object response = new RequestHandler().hander(bReq, key);
                Object response =null;
                handleWrite(key, response, bReq.getSerialId());
			}else{
				System.err.println("unkown object!!!" + as3Obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void handleWrite(SelectionKey key, Object ret, int serialId)
			throws IOException, TimeoutException {
		BaseResponse response = new BaseResponse(serialId, ret);
		response.setServerTime(System.currentTimeMillis());

		SocketChannel clientChannel = (SocketChannel) key.channel();

		//byte[] bytes = Amf3Util.getAmf3ByteArray(response);
        byte[] bytes = null;

		long t1 = System.currentTimeMillis();
		byte[] compressBytes = ZipUtil.compressBytes(bytes);//压缩

		long t2 = System.currentTimeMillis() - t1;
		logger.info("ClientSocket.handleWrite(),bytesLen=" + bytes.length + ",compressBytes=" + compressBytes.length +
                ",compressTime=" + t2 + ",%=" + (int) (100 * compressBytes.length / bytes.length) + "%,obj:" + ret);

		NoBlockSocketUtil.writeInt(clientChannel, compressBytes.length);

		NoBlockSocketUtil.write(clientChannel, compressBytes);

		// 设置为下一次读取或是写入做准备
		key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
	}

}
