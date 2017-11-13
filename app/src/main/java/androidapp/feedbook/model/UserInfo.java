package androidapp.feedbook.model;

import android.annotation.TargetApi;
import android.os.Build;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import android.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import androidapp.feedbook.exceptions.AuthenticationException;

/**
 * Class used for generating the encrypted password for user
 * @author Kiruthiga
 *
 */
public class UserInfo {

	private Map<String, User> userDatabase = new HashMap<String, User>();

	/**
	 * Method used for creating new user account for a user
	 * @param userName - input user name for account creation 
	 * @param password - input password for account creation
	 * @return - returns a Map with all usernames, salt and encrypted passwords
	 * @throws AuthenticationException - when error occurs while creating encrypted password
	 */
	public Map<String, User> signUp(String userName, String password) throws AuthenticationException {
		
		try {
			String salt = getNewSalt();
			String encryptedPassword = getEncryptedPassword(password, salt);
			User user = new User();
			user.setUserEncryptedPassword(encryptedPassword);
			user.setUserName(userName);
			user.setUserSalt(salt);
			saveUser(user);
		} catch (Exception e) {
			throw new AuthenticationException("An exception occured while creating user account.");
			
		}
		return userDatabase;

	}


	// Method to get base64 encoded salt generated randomly
	private String getNewSalt() throws Exception {
		
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		byte[] salt = new byte[8];
		random.nextBytes(salt);
		return Base64.encodeToString(salt,Base64.DEFAULT);

	}

	private void saveUser(User user) {
		userDatabase.put(user.getUserName(), user);		

	}


	// Get a encrypted password using PBKDF2 (Password Based Key Derivation Function) hash algorithm, along with the random salt
	private String getEncryptedPassword(String password, String salt) throws Exception {
		String algorithm = "PBKDF2WithHmacSHA1";
		int derivedKeyLength = 160; // for SHA1
		int iterations = 20000; 
		//byte[] saltBytes = Base64.getDecoder().decode(salt);
		byte[] saltBytes = Base64.decode(salt,Base64.DEFAULT);

		KeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes, iterations, derivedKeyLength);
		SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);
		byte[] encBytes = f.generateSecret(spec).getEncoded();

		return Base64.encodeToString(encBytes,Base64.DEFAULT);

	}
	
	/**
	 * Method to validate the login and password of existing users
	 * @param inputUser - userid of existing user
	 * @param inputPass- password of existing user
	 * @param prevSalt - salt used previously for encryption obtained from json file
	 * @param prevPwd - encrypted password obtained from json file
	 * @return - true if the input userid and password match the value sin users.json
	 * @throws AuthenticationException - when error occurs while encrypting the password.
	 */
	public boolean authenticateUser(String inputUser, String inputPass, String prevSalt, String prevPwd) throws AuthenticationException  {
		
		boolean retValue = false;
			try {
				
				String calculatedHash = getEncryptedPassword(inputPass, prevSalt);
				if(calculatedHash.equals(prevPwd)) {
					retValue = true;
				}
			} catch (Exception e) {
				
				throw new AuthenticationException("An exception occured while authenticating user.");
			}
			return retValue;

	}

}
