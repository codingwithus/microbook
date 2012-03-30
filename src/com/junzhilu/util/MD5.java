package com.junzhilu.util;

import java.security.MessageDigest;

/*
 * �������ڼ����ַ��������ļ���MD5ֵ
 */
public class MD5 {
	/*
	 * ��������ַ�����md5
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
	 * �Ը������ļ�����MD5����
	 * 
	 * @param strFileName �ļ�������
	 * 
	 * @param iBeginOffset ��ʼƫ��
	 * 
	 * @param iEndOffset ��ֹƫ��
	 */
	static public String md5_file(String strFileName, Integer iBeginOffset,
			Integer iEndOffset) {

		return "";
	}

	/**
	 * ��������32λMD5ת��Ϊ16λ��MD5
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
