package projects.encryptor.model;

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

/**
 * Defines basic functions and methods for building a cryptographer.
 * 
 * @author Manu Puduvalli
 *
 */
public interface BasicCryptosystem {
	
	String CHARSET_UTF8 = "UTF-8";
	
	String ALGORITHM_AES = "AES";
	
	String MESSAGE_DIGEST_SHA256 = "SHA-256";
	String MESSAGE_DIGEST_SHA1 = "SHA-1";
	
	String CIPHER_TSFM_ACN = "AES/CBC/NoPadding";
	String CIPHER_TSFM_ACP = "AES/CBC/PKCS5Padding";
	String CIPHER_TSFM_AEN = "AES/ECB/NoPadding";
	String CIPHER_TSFM_AEP = "AES/ECB/PKCS5Padding";

	/**
	 * Encrypts a piece of text given a Key and a Cipher. The encrypted
	 * value is encoded to a string using Base64.
	 * 
	 * @param plainTxt the text to encrypt
	 * @param key the secret key
	 * @param cipher the cryptographic cipher
	 * @return a new string with the encrypted value
	 * @throws InvalidKeyException if the key is not recognized by the {@link javax.crypto.Cipher}
	 * @throws IllegalBlockSizeException if the length of data does not match the Cipher's block size
	 * @throws BadPaddingException if the String text is not padded properly
	 * @throws UnsupportedEncodingException if the encoding is not supported
	 */
	default String encrypt(String plainTxt, Key key, Cipher cipher) throws InvalidKeyException, IllegalBlockSizeException,
		BadPaddingException, UnsupportedEncodingException 
	{
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encrypted = cipher.doFinal(plainTxt.getBytes(CHARSET_UTF8));
		String encodedVal = Base64.getEncoder().encodeToString(encrypted);
		return new String(encodedVal);
	}

	/**
	 * Decrypts a piece of text given a Key and a Cipher. The decrypted
	 * value is encoded to a String using Base64.
	 * 
	 * @param encrypted the encrypted String to decrypt
	 * @param key the secret key
	 * @param cipher the cryptographic cipher
	 * @return a new String with the decrypted plain text
	 * @throws InvalidKeyException if the key is not recognized by the {@link javax.crypto.Cipher}
	 * @throws IllegalBlockSizeException if the length of data does not match the Cipher's block size
	 * @throws BadPaddingException if the String text is not padded properly
	 */
	default String decrypt(String encrypted, Key key, Cipher cipher) throws InvalidKeyException, IllegalBlockSizeException,
		BadPaddingException 
	{
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decodedVal = Base64.getDecoder().decode(encrypted);
		return new String(cipher.doFinal(decodedVal));
	}
	
	/**
	 * Generates a SecretKey as a Base64 encoded string, given an supported algorithm.
	 * 
	 * @param algo the algorithm recognized by {@link javax.crypto.KeyGenerator}
	 * @return a SecretKey instance as a String object
	 * @throws NoSuchAlgorithmException if the algorithm <code>algo</code> does not exist
	 */
	static String generateSecretKey(String algo) throws NoSuchAlgorithmException {
		KeyGenerator keyGen = KeyGenerator.getInstance(algo);
		keyGen.init(256); // for example
		SecretKey secretKey = keyGen.generateKey();
		return convertSecretKeyToString(secretKey);
	}
	
	/**
	 * Converts a Key to a Base64 encoded String.
	 * @param sk the {@link java.security.Key}
	 * @return a new String secret key
	 */
	static String convertSecretKeyToString(Key sk) {
		return Base64.getEncoder()
				.encodeToString(sk.getEncoded());
	}
	
	/*
	 * Forces and implementor to carry the following instances.
	 */
	
	/**
	 * Returns a {@link java.security.Key} instance
	 * 
	 * @return an instance of {@link java.security.Key}
	 */
	Key getSecretKeySpec();
	
	/**
	 * Returns a {@link javax.crypto.Cipher} instance
	 * 
	 * @return an instance of {@link javax.crypto.Cipher}
	 */
	Cipher getCipher();
	
	/**
	 * Sets a secret key as a String.
	 * 
	 * @param key the secret key as a String
	 */
	void setKey(String secretKey);
	
	/**
	 * Gets a secret key.
	 * 
	 * @return a secret key as a String.
	 */
	String getKey();

	/**
	 * Sets a Key given a String secret key, an algorithm recognized by a 
	 * {@link java.security.Key} instance, and a supported 
	 * {@link java.security.MessageDigest} instance.
	 * 
	 * @param keystr a secret key as a String
	 * @param algo an algorithm recognized by a {@link java.security.Key} instance
	 * @param messageDigest a message digest supported by a {@link java.security.MessageDigest} instance
	 * @throws NoSuchAlgorithmException if the algorithm <code>algo</code> does not exist
	 * @throws UnsupportedEncodingException if the charset is not recognized
	 */
	void setSecretKeyFromString(String keystr, String algo, String messageDigest, String charset)
			throws NoSuchAlgorithmException, UnsupportedEncodingException;

	/**
	 * Sets a Cipher.
	 * 
	 * @param cipher the Cipher instance
	 */
	void setCipher(Cipher cipher);

}
