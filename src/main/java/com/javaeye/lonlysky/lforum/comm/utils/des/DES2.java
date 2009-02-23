package com.javaeye.lonlysky.lforum.comm.utils.des;

/**
 * @author tianya
 *
 * 简单的DES加密算法
 */
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * DES加密的，文件中共有两个方法,加密、解密
 * 
 */
@SuppressWarnings("unused")
public class DES2 {
	private String Algorithm = "DES";
	private KeyGenerator keygen;
	private SecretKey deskey;
	private Cipher cipher;
	private byte[] encryptorData;
	private byte[] decryptorData;

	/**
	 * 初始化 DES 实例
	 */
	public DES2() {
		init();
	}

	/**
	 * 初始化 DES 加密算法的一些参数
	 */
	public void init() {
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		try {
			keygen = KeyGenerator.getInstance(Algorithm);
			byte key[] = "12312345".getBytes();
			deskey = new SecretKeySpec(key, "DES");
			//  deskey = keygen.generateKey();
			for (int i = 0; i < deskey.getEncoded().length; i++) {
				System.out.println(deskey.getEncoded()[i]);
			}
			cipher = Cipher.getInstance(Algorithm);
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (NoSuchPaddingException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 对 byte[] 进行加密
	 * @param datasource 要加密的数据
	 * @return 返回加密后的 byte 数组
	 */
	public byte[] createEncryptor(byte[] datasource) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, deskey);
			encryptorData = cipher.doFinal(datasource);
		} catch (java.security.InvalidKeyException ex) {
			ex.printStackTrace();
		} catch (javax.crypto.BadPaddingException ex) {
			ex.printStackTrace();
		} catch (javax.crypto.IllegalBlockSizeException ex) {
			ex.printStackTrace();
		}
		return encryptorData;
	}

	/**
	 * 将字符串加密
	 * 
	* @param datasource
	* @return
	* @throws Exception
	*/
	public byte[] createEncryptor(String datasource) throws Exception {
		return createEncryptor(datasource.getBytes());
	}

	/**
	 * 对 datasource 数组进行解密
	 * @param datasource 要解密的数据
	 * @return 返回加密后的 byte[]
	 */
	public byte[] createDecryptor(byte[] datasource) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, deskey);
			decryptorData = cipher.doFinal(datasource);
		} catch (java.security.InvalidKeyException ex) {
			ex.printStackTrace();
		} catch (javax.crypto.BadPaddingException ex) {
			ex.printStackTrace();
		} catch (javax.crypto.IllegalBlockSizeException ex) {
			ex.printStackTrace();
		}
		return decryptorData;
	}

	/**
	 * 
	 * 将 DES 加密过的 byte数组转换为字符串
	 * 
	* @param dataByte
	* @return
	*/
	public String byteToString(byte[] dataByte) {
		String returnStr = null;
		BASE64Encoder be = new BASE64Encoder();
		returnStr = be.encode(dataByte);
		return returnStr;
	}

	/**
	 * 
	 * 将字符串转换为DES算法可以解密的byte数组
	 * 
	* @param dataByte
	* @return
	* @throws Exception
	*/
	public byte[] stringToByte(String datasource) throws Exception {
		BASE64Decoder bd = new BASE64Decoder();
		byte[] sorData = bd.decodeBuffer(datasource);
		return sorData;
	}

	/**
	 * 输出 byte数组
	 * 
	* @param data
	*/
	public void printByte(byte[] data) {
		System.out.println("*********开始输出字节流**********");
		System.out.println("字节流: " + data.toString());
		for (int i = 0; i < data.length; i++) {
			System.out.println("第 " + i + "字节为：" + data[i]);
		}
		System.out.println("*********结束输出字节流**********");
	}

	/**
	 * 解密指定加密数据
	 * @return 解密数据
	 */
	public static String decode(String code) {
		DES2 des = new DES2();
		try {
			byte[] stringToByte = des.stringToByte(code);
			byte[] decryptorByte = des.createDecryptor(stringToByte);
			return new String(decryptorByte);
		} catch (Exception e) {
			return code;
		}
	}

	/**
	 * 加密指定字符串
	 * @param str 字符串
	 * @return 加密数据
	 */
	public static String encode(String str) {
		DES2 des = new DES2();
		try {
			byte[] encryptorByte = des.createEncryptor(str);
			return des.byteToString(encryptorByte);
		} catch (Exception e) {
			return str;
		}
	}

	public static void main(String args[]) throws Exception {
		String string = "我是中国人";
		System.out.println("加密前："+string);
		String code = encode(string);
		System.out.println("加密后："+code);
		System.out.println("解密后："+decode(code));
	}

}
