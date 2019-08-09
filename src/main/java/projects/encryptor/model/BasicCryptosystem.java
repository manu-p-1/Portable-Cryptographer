package projects.encryptor.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public interface BasicCryptosystem {
	
	String SUPPORTED_CHARSET = "UTF-8";
	String ALGORITHM_AES = "AES";
	String ALGORITHM_DES = "DES";
	String MESSAGE_DIGEST_SHA256 = "SHA-256";
	String MESSAGE_DIGEST_SHA1 = "SHA-1";
	
	String CIPHER_TSFM_ACN = "AES/CBC/NoPadding";
	String CIPHER_TSFM_ACP = "AES/CBC/PKCS5Padding";
	String CIPHER_TSFM_AEN = "AES/ECB/NoPadding";
	String CIPHER_TSFM_AEP = "AES/ECB/PKCS5Padding";
	
	String CIPHER_TSFM_DCN = "DES/CBC/NoPadding";
	String CIPHER_TSFM_DCP = "DES/CBC/PKCS5Padding";
	String CIPHER_TSFM_DEN = "DES/ECB/NoPadding";
	String CIPHER_TSFM_DEP = "DES/ECB/PKCS5Padding";

	default public String encrypt(String plainTxt, Key key, Cipher cipher) throws InvalidKeyException, IllegalBlockSizeException,
		BadPaddingException, UnsupportedEncodingException 
	{
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encrypted = cipher.doFinal(plainTxt.getBytes(SUPPORTED_CHARSET));
		String encodedVal = new BASE64Encoder().encode(encrypted);
		return new String(encodedVal);
	}

	default public String decrypt(String encrypted, Key key, Cipher cipher) throws InvalidKeyException, IllegalBlockSizeException,
		BadPaddingException, IOException 
	{
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decodedVal = new BASE64Decoder().decodeBuffer(encrypted);
		return new String(cipher.doFinal(decodedVal));
	}
	
	public static String generateSecureKey(String algo) throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(algo);
		keyGen.init(256); // for example
		SecretKey secretKey = keyGen.generateKey();
		return convertSecretKeyToString(secretKey);
	}
	
	static String convertSecretKeyToString(Key sk) {
		return Base64.getEncoder()
				.encodeToString(sk.getEncoded());
	}
	
	public Key getSecretKeySpec();
	
	public Cipher getCipher();
	
	public void setKey(String key);
	
	public String getKey();
	
	public boolean getEncryptionStatus();
	
	public void setEncryptionStatus(boolean status);

	void setSecretKeyFromString(String keystr, String algo, String messageDigest)
			throws NoSuchAlgorithmException, IOException;

	void setCipher(Cipher cipher);

}
