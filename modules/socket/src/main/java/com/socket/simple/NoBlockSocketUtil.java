package com.socket.simple;

import com.util.DataTransUtil;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeoutException;

public class NoBlockSocketUtil {

    private static int socketOverTime=10*1000;//10秒

	/**
	 * 写int到niosocket
	 * 
	 * @param channel
	 * @param n
	 * @throws java.io.IOException
	 * @throws java.util.concurrent.TimeoutException
	 */
	public static void writeInt(SocketChannel channel, int n)
			throws IOException, TimeoutException {
		ByteArrayOutputStream u = new ByteArrayOutputStream();
		DataOutputStream d = new DataOutputStream(u);
		d.writeInt(n);
		byte[] writeByte = u.toByteArray();
		write(channel, writeByte);
	}

	public static void write(SocketChannel channel, byte[] writeByte)
			throws IOException, TimeoutException {
		int writeNowLen = 0;
		int finishLen = writeByte.length;
		long t1 = System.currentTimeMillis();
		long t2 = -1;
		while (writeNowLen != finishLen) {
			writeNowLen += channel.write(ByteBuffer.wrap(writeByte));
			t2 = System.currentTimeMillis();
			if (t2 - t1 > socketOverTime) {
				throw new TimeoutException("NoBlockSocketRW write time out");
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public static int readInt(SocketChannel channel) throws IOException,
			TimeoutException {
		ByteBuffer bufRead = ByteBuffer.allocate(4); // integer is 4 bytes
		read(channel, bufRead.array());
		return DataTransUtil.byte2int(bufRead.array());
	}

	public static void read(SocketChannel channel, byte[] readToHere)
			throws IOException, TimeoutException {
		int readNowLen = 0;
		int finishLen = readToHere.length;

		long t1 = System.currentTimeMillis();
		long t2 = -1;
		while (readNowLen != finishLen) {
			readNowLen += channel.read(ByteBuffer.wrap(readToHere));
			t2 = System.currentTimeMillis();
			if (t2 - t1 > socketOverTime) {
				throw new TimeoutException("NoBlockSocketRW read time out");
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
