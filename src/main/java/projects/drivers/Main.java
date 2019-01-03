package projects.drivers;

//Would be exceptions for catch statement
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import projects.encryptor.Cryptographer;

public class Main {

	public static void main(String[] args) {

		try {
			Cryptographer encryptifier = new Cryptographer();
			String string = encryptifier.encrypt("Manohar");
			System.out.println(string);
			System.out.println(encryptifier.decrypt(string));	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
