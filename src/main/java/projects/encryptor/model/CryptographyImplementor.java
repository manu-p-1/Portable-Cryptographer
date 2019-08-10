package projects.encryptor.model;

/**
 * Functions that allow a class to interact with a
 * BasicCryptosystem object.
 * 
 * @author Manu Puduvalli
 *
 */
public interface CryptographyImplementor {

	/**
	 * Used when a GUI Controller or some other program
	 * directly interacts with a BasicCryptosystem. This
	 * forces the user to setup a BasicCryptosystem instance
	 * 
	 * @param bc the BasicCryptosystem
	 */
	public void setupBasicCryptoSystem(BasicCryptosystem bc) ;
	
}
