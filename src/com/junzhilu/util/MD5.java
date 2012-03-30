package com.junzhilu.util;

import java.security.MessageDigest;

/*
 * 该类用于计算字符串或者文件的MD5值
 */
public class MD5 {
	/*
	 * 计算给定字符串的md5
	 */
	static public String md5_string(String s) {
		byte[] unencodedPassword = s.getBytes();

		MessageDigest md = null;

		try {
			md = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			return s;
		}

		md.reset();
		md.update(unencodedPassword);
		byte[] encodedPassword = md.digest();
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}

		return buf.toString();
	}

	/*
	 * 对给定的文件进行MD5计算
	 * 
	 * @param strFileName 文件的名称
	 * 
	 * @param iBeginOffset 起始偏移
	 * 
	 * @param iEndOffset 终止偏移
	 */
	static public String md5_file(String strFileName, Integer iBeginOffset,
			Integer iEndOffset) {

		return "";
	}

	/**
	 * 将给定的32位MD5转换为16位的MD5
	 * 
	 * @param strSrc
	 * @return
	 */
	static public String md5_convert16(String in) {
		return in.substring(8, 24);
	}

	static public byte[] md5_convert16(byte[] in) {
		byte[] out = new byte[16];

		int i = 0;
		for (i = 0; i < 16; i++) {
			out[i] = (byte) (Util.charToInt((char) in[2 * i]) * 16 + Util
					.charToInt((char) in[2 * i + 1]));
		}

		return out;
	}

}
