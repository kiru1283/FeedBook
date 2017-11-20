package androidapp.feedbook.controller;

import android.content.Context;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Iterator;
import java.util.Map;


import androidapp.feedbook.R;
import androidapp.feedbook.exceptions.AuthenticationException;
import androidapp.feedbook.exceptions.JSONFileException;
import androidapp.feedbook.model.JSONReader;
import androidapp.feedbook.model.JSONWriter;
import androidapp.feedbook.model.User;
import androidapp.feedbook.model.UserInfo;

/**
 *
 * @author Kiruthiga
 * @category Controller class for User account maintenance
 */
public class LoginValidation {
    private final String userfilename = "users.json";
    private JSONArray arrUser;
    private String prevSalt;
    private String prevPwd;
    private Context context;

    public LoginValidation(Context context){
//       userfilename = Context.getResources().getAssets().open("filename");
        this.context = context;
    }
    /**
     * Method to validate the credentials of user from the values saved in users.json
     * @param userid - input userid to login
     * @param pwd - input password to login
     * @return - returns true if the input userid and password match the values in users.json
     * @throws AuthenticationException - when password encryption fails
     * @throws JSONFileException - when errors are encountered while reading values from users.json fails
     */
    public boolean validateUser(String userid, String pwd) throws AuthenticationException,JSONFileException {

        UserInfo userObj = new UserInfo();
        boolean validUser = false;

      //  boolean userExists = checkUser(userid);

      //  if (userExists) {
            validUser = userObj.authenticateUser(userid, pwd, prevSalt, prevPwd);
        //}

        return validUser;

    }

    /**
     * Method to stored userid and password of new user in users.json
     * @param userid - input userid to create new user
     * @param pwd - input password to create new user
     * @return - returns true if the userid and encrypted password are saved in users.json
     * @throws AuthenticationException - when password encryption fails
     * @throws JSONFileException -  when errors are encountered while writing values to users.json fails
     */
    public boolean createUser(String userid, String pwd) throws AuthenticationException,JSONFileException  {

        boolean retVal = false;

        UserInfo userObj = new UserInfo();

      //  boolean userExists = checkUser(userid);

     //   if (!userExists) {
            Map<String, User> newUser = userObj.signUp(userid, pwd);

            if (newUser != null) {

                JSONWriter jsonObj = new JSONWriter(userfilename,context);
                jsonObj.jsonUserWrite(arrUser, userid, newUser.get(userid).getUserSalt(),
                        newUser.get(userid).getUserEncryptedPassword());
                retVal = true;
            }
       // } else {
         //   throw new AuthenticationException("UserID Already Exists. Please create user with a different UserID.");

        //}

        return retVal;

    }



    /**
     * Method to check is user exists in users.json and fetch the previous salt and password
     * @param userid - username of the logged in user
     * @return - true if its username is aalready used
     * @throws JSONFileException - when there is an error reading the users.json file
     */
    @SuppressWarnings("unchecked")
    public boolean checkUser(String userid) throws JSONFileException {

        boolean userExists = false;

        JSONReader readObj = new JSONReader(userfilename, context);
        arrUser = readObj.jsonReader();

        if (arrUser != null) {
            Iterator<JSONObject> it = arrUser.iterator();

            while (!userExists && it.hasNext()) {
                JSONObject jsObj = it.next();
                if (jsObj.get("userid").equals(userid)) {
                    userExists = true;
                    prevSalt=jsObj.get("salt").toString();
                    prevPwd = jsObj.get("pwd").toString();

                }
            }
        }

        return userExists;

    }

}
