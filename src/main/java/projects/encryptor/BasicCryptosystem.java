package projects.encryptor;

public interface BasicCryptosystem {

	public String encrypt(String plainTxt) throws Exception;

	public String decrypt(String encrpyted) throws Exception;

	public boolean isEncrypted();
}
