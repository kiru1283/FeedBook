package androidapp.feedbook.model;

/**
 * Class representing user of the application
 * @author Kiruthiga
 *
 */
public class User {
	  	 private String userEncryptedPassword;
	     private String userSalt;
	     private String userName;
	     
	    /**
	     * Method to return the user name 
	     * @return user name of logged in user
	     */
		public String getUserName() {
			return userName;
		}
		
		/**
		 * Method to set the user name
		 * @param userName - input user name of the logged in user
		 */
		public void setUserName(String userName) {
			this.userName = userName;
		}
		
		/**
		 * Method returning the encrypted password of the user
		 * @return - encrypted password of logged in user
		 */
		public String getUserEncryptedPassword() {
			return userEncryptedPassword;
		}
		
		/**
		 * Method to set the encrypted password of the user
		 * @param userEncryptedPassword - encrypted password of logged in user
		 */
		public void setUserEncryptedPassword(String userEncryptedPassword) {
			this.userEncryptedPassword = userEncryptedPassword;
		}
		
		/**
		 * Method to get the salt used for encrypting password of user
		 * @return - salt used for encryption  
		 */
		public String getUserSalt() {
			return userSalt;
		}
		
		/**
		 * Method used to set the salt to be used for encrypting the password
		 * @param userSalt  - salt to be used for encrypting the password 
		 */
		public void setUserSalt(String userSalt) {
			this.userSalt = userSalt;
		}
		

}
