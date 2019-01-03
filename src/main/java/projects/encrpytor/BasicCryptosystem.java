package projects.encrpytor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

public interface BasicCryptosystem {
	
	public String encrypt(String plainTxt) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException;
	
	public String decrypt(String encrpyted) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException;
	
	public boolean isEncrypted();
	

}
