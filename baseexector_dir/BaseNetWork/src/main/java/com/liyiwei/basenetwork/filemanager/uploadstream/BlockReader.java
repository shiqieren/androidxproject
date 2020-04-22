package com.liyiwei.basenetwork.filemanager.uploadstream;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BlockReader {

	public static final int BLOCK_SIZE = 1024 * 200;

	RandomAccessFile oSavedFile;

	public BlockReader(String sName) throws IOException {
		oSavedFile = new RandomAccessFile(sName, "r");// 创建随机存取文件
	}

	/**
	 * 读取一个块
	 * 
	 * @param blockNo
	 * @param fileId
	 * @return
	 * @throws IOException
	 */
	public byte[] readBlock(int blockNo, long fileId) throws IOException {

		byte[] result = new byte[BLOCK_SIZE]; //
		oSavedFile.seek(blockNo * BLOCK_SIZE);
		int len = oSavedFile.read(result);

		// byte[] arr = new byte[len + 20];
		byte[] arr = new byte[len + 16];

		ByteConvert.longToBytes(fileId, arr, 0); // 前8个byte为文件ID
		ByteConvert.intToBytes(blockNo, arr, 8); // 后四个byte为块号
		ByteConvert.intToBytes(len, arr, 8 + 4);// 再后面四个字节为块实际长度
		// ByteConvert.intToBytes(crc16(result), arr, 8 + 4 + 4); //

		System.arraycopy(result, 0, arr, 8 + 4 + 4, len); // 复制数组

		result = null;

		return arr;
	}

	// 产生CRC16校验码
	private static int crc16(byte[] buffer) {
		int crc = 0xFFFF;

		for (int j = 0; j < buffer.length; j++) {
			crc = ((crc >>> 8) | (crc << 8)) & 0xffff;
			crc ^= (buffer[j] & 0xff);// byte to int, trunc sign
			crc ^= ((crc & 0xff) >> 4);
			crc ^= (crc << 12) & 0xffff;
			crc ^= ((crc & 0xFF) << 5) & 0xffff;
		}
		crc &= 0xffff;
		return crc;
	}

	// 获取内容
	public Block getContent(long nStart) {
		Block Block = new Block();
		Block.b = new byte[BLOCK_SIZE];
		try {
			oSavedFile.seek(nStart);
			Block.length = oSavedFile.read(Block.b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Block;
	}

	public class Block {
		public byte[] b;
		public int length;
	}

	// 获取文件长度
	public long getFileLength() {
		Long length = 0l;
		try {
			length = oSavedFile.length();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return length;
	}

	/**
	 * 获取文件一共有多少块
	 * 
	 * @return
	 */
	public static int getBlockCount(File f) {
		long len = f.length();
		int result = (int) (len / BLOCK_SIZE);

		if (len % BLOCK_SIZE != 0) {
			result = result + 1;
		}
		return result;
	}

	/**
	 * 关闭文件
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		if (oSavedFile != null) {
			oSavedFile.close();
		}
	}

}
