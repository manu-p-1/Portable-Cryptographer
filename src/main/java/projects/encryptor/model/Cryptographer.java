package projects.encryptor.model;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cryptographer implements BasicCryptosystem {

	private boolean encrpytionStatus;
 	private String key = "PLACEHOLDER"; 
 	private Key secretKey;
 	private Cipher cipher;

	public boolean getEncryptionStatus() {
		return encrpytionStatus;
	}
	
	public void setEncryptionStatus(boolean status) {
		this.encrpytionStatus = status;
	}
	
	@Override
	public void setSecretKeyFromString(String keystr, String algo, String messageDigest) throws NoSuchAlgorithmException, IOException {
		this.secretKey = generateKeySpec(keystr, algo, messageDigest);
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
	
	private static SecretKeySpec generateKeySpec(String keystr, String algo, String messageDigest) throws NoSuchAlgorithmException, IOException {
		byte[] btMess = keystr.getBytes(BasicCryptosystem.SUPPORTED_CHARSET);
		MessageDigest md = MessageDigest.getInstance(messageDigest);
		byte[] btDig = md.digest(btMess);
		return new SecretKeySpec(btDig, algo);
	}

}
