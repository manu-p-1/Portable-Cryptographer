package projects.encryptor.model;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Cryptographer implements BasicCryptosystem {

	private boolean isEncrpyted;
 	private final String key = "2s5v8x/A?D(G+KbPeShVmYq3t6w9z$B&"; //Randomly generated keys to come later
 	private final Key aesKey;
 	private final Cipher cipher;

	public Cryptographer() throws NoSuchAlgorithmException, NoSuchPaddingException {
		isEncrpyted = false;
		aesKey = new SecretKeySpec(key.getBytes(), "AES");
		cipher = Cipher.getInstance("AES");
	}

	public String encrypt(String plainTxt) throws InvalidKeyException, IllegalBlockSizeException,
		BadPaddingException, UnsupportedEncodingException {
		isEncrpyted = true;
		cipher.init(Cipher.ENCRYPT_MODE, aesKey);
		byte[] encrypted = cipher.doFinal(plainTxt.getBytes("UTF-8"));
		String encodedVal = new BASE64Encoder().encode(encrypted);
		return new String(encodedVal);
	}

	public String decrypt(String encrypted) throws InvalidKeyException, IllegalBlockSizeException,
		BadPaddingException, IOException {
		isEncrpyted = false;
		cipher.init(Cipher.DECRYPT_MODE, aesKey);
		byte[] decodedVal = new BASE64Decoder().decodeBuffer(encrypted);
		return new String(cipher.doFinal(decodedVal));
	}

	public boolean isEncrypted() {
		return isEncrpyted;
	}
}
