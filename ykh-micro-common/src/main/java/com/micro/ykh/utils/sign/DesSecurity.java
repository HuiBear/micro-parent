package com.micro.ykh.utils.sign;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;

public class DesSecurity {

	public DesSecurity(String key, String iv) throws Exception {
		if (key == null) {
			throw new NullPointerException("Parameter is null!");
		} else {
			InitCipher(key.getBytes(), iv.getBytes());
			return;
		}
	}

	public static String toHex(byte data[], int start, int len) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < len; i++)
			sb.append(toHex(data[start + i]));

		return sb.toString();
	}

	public static String toHex(String data) {
		return toHex(data.getBytes(), 0, data.getBytes().length);
	}

	public static final String toHex(byte b) {
		return (new StringBuilder())
				.append("0123456789ABCDEF".charAt(15 & b >> 4))
				.append("0123456789ABCDEF".charAt(b & 15)).toString();
	}

	public static String toHex(byte data[]) {
		return toHex(data, 0, data.length);
	}

	public static byte[] hexToBytes(String data) {
		try {
			byte b[] = new byte[data.length() / 2];
			for (int i = 0; i < data.length() / 2; i++)
				b[i] = (byte) Integer.parseInt(
						data.substring(i * 2, (i + 1) * 2), 16);

			return b;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void InitCipher(byte secKey[], byte secIv[]) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(secKey);
		DESKeySpec dsk = new DESKeySpec(md.digest());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		javax.crypto.SecretKey key = keyFactory.generateSecret(dsk);
		IvParameterSpec iv = new IvParameterSpec(secIv);
		java.security.spec.AlgorithmParameterSpec paramSpec = iv;
		enCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		deCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		enCipher.init(1, key, paramSpec);
		deCipher.init(2, key, paramSpec);
		//encoder = new BASE64Encoder();
		//decoder = new BASE64Decoder();
	}

	public String encrypt64(byte data[]) throws Exception {
		//return encoder.encode(enCipher.doFinal(data));
		return Base64.encodeBase64String(enCipher.doFinal(data));
	}

	public byte[] decrypt64(String data) throws Exception {
		//return deCipher.doFinal(decoder.decodeBuffer(data));
		return deCipher.doFinal(Base64.decodeBase64(data));
		//return Base64.decodeBase64(data);
	}

	public String encrypt16(byte data[]) throws Exception {
		return toHex(enCipher.doFinal(data));
	}

	public byte[] decrypt16(String data) throws Exception {
		return deCipher.doFinal(hexToBytes(data));
	}

	public static void main(String args[]) {
		
	}

	//BASE64Encoder encoder;
	//BASE64Decoder decoder;
	Cipher enCipher;
	Cipher deCipher;
}
