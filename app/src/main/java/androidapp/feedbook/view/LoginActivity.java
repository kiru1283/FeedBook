package androidapp.feedbook.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidapp.feedbook.R;
import androidapp.feedbook.controller.LoginValidation;
import androidapp.feedbook.exceptions.AuthenticationException;
import androidapp.feedbook.exceptions.JSONFileException;

/**
 *  A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private LoginValidation loginObj;


    // UI references.
    private AutoCompleteTextView mUserView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    /**
     * Method to create the Activity and all the view elements in the activiy
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        loginObj = new LoginValidation(LoginActivity.this);
        mUserView = (AutoCompleteTextView) findViewById(R.id.user);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mUserView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUserView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            mUserView.setError(getString(R.string.error_field_required));
            focusView = mUserView;
            cancel = true;
        } else if (!isUserValid(username)) {
            mUserView.setError(getString(R.string.error_invalid_user));
            focusView = mUserView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            boolean validUser = false;
            //mAuthTask = new UserLoginTask(username, password);

          /*  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
                mAuthTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
            */
            //  mAuthTask.execute((Void) null);

            boolean userExists = false;
            try {
                userExists = loginObj.checkUser(username);
            } catch (JSONFileException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                showProgress(false);
                return;
            }

            if (userExists) {

                // password validation
                try {
                    if (loginObj.validateUser(username, password))

                    {
                        Toast.makeText(getApplicationContext(), "Logged In!!", Toast.LENGTH_SHORT).show();
                        validUser = true;

                    } else {
                        Toast.makeText(getApplicationContext(), "Incorrect Username or Password !!", Toast.LENGTH_LONG).show();

                    }
                } catch (AuthenticationException | JSONFileException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {

                // register the new account here.
                try {
                    if (loginObj.createUser(username, password)) {
                        Toast.makeText(getApplicationContext(), "User account created successfully!!", Toast.LENGTH_SHORT).show();
                        validUser = true;

                    } else {
                        Toast.makeText(getApplicationContext(), "Error while creating User account!!", Toast.LENGTH_LONG).show();
                    }
                } catch (AuthenticationException | JSONFileException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    //e.printStackTrace();
                }
            }


            showProgress(false);

            if(validUser == true) {
                Intent intent = new Intent(LoginActivity.this, NavActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, username);
                startActivity(intent);
            }


        }
    }

    private boolean isUserValid(String username) {
     //handle short username
        return username.length() > 4;
    }

    private boolean isPasswordValid(String password) {
        //handle short password
        return password.length() > 4;
//        return true;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

