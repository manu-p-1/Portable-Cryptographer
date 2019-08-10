package projects.encryptor.model;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The Cryptographer which manages and converts the Secret Key accordingly.
 * 
 * @author Manu Puduvalli
 *
 */
public class Cryptographer implements BasicCryptosystem {

	private boolean encrpytionStatus;
 	private String key = "PLACEHOLDER"; 
 	private Key secretKey;
 	private Cipher cipher;

 	/**
 	 * Returns whether a key is encrypted or not
 	 * 
 	 * @return whether the key is encrypted or not.
 	 */
	public boolean getEncryptionStatus() {
		return encrpytionStatus;
	}
	
	/**
	 * Sets the encryption status.
	 * 
	 * @param status the encryption status
	 */
	public void setEncryptionStatus(boolean status) {
		this.encrpytionStatus = status;
	}
	
	@Override
	public void setSecretKeyFromString(String keystr, String algo, String messageDigest, String charset) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		this.secretKey = generateKeySpec(keystr, algo, messageDigest, charset);
	}
	
	@Override
	public Key getSecretKeySpec() {
		return secretKey;
	}
	
	@Override
	public Cipher getCipher() {
		return cipher;
	}
	
	@Override
	public void setCipher(Cipher cipher) {
		this.cipher = cipher;
	}
	
	@Override
	public void setKey(String key) {
		this.key = key;
	}
	
	@Override
	public String getKey() {
		return this.key;
	}
	
	private static SecretKeySpec generateKeySpec(String keystr, String algo, String messageDigest, String charset) 
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] btMess = keystr.getBytes(charset);
		MessageDigest md = MessageDigest.getInstance(messageDigest);
		byte[] btDig = md.digest(btMess);
		return new SecretKeySpec(btDig, algo);
	}

}
