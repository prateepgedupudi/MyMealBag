package info.prateep.android.mymealbag.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

import info.prateep.android.mymealbag.R;
import info.prateep.android.mymealbag.model.User;
import info.prateep.android.mymealbag.util.Constants;

/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends AppCompatActivity  {
    private static final String LOG_TAG = SignupActivity.class.getSimpleName();

    // UI references.
    private String email,password,name,mobile;
    private EditText mNameView, mMobileView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mSignUpFormView;
    private Firebase mFirebaseRef;
    private ProgressDialog mAuthProgressDialog;
    private boolean cancel = false;
    private View focusView = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Set up the login form.
        mNameView = (EditText)findViewById(R.id.sign_up_name);
        mMobileView = (EditText)findViewById(R.id.sign_up_mobile);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.sign_up_email);
        mPasswordView = (EditText) findViewById(R.id.sign_up_password);
        /**
         * Create Firebase references
         */
        mFirebaseRef = new Firebase(Constants.FIREBASE_URL);

        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        mSignUpFormView = findViewById(R.id.sign_up_form);
    }

    /**
     * Attempts to register the account specified by the sign up form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptSignUp() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        name = mNameView.getText().toString();
        mobile = mMobileView.getText().toString();
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();



        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid mobile, if the user entered one.
        if (!TextUtils.isEmpty(mobile) && !isPhoneValid(mobile)) {
            mMobileView.setError(getString(R.string.error_invalid_mobile));
            focusView = mMobileView;
            cancel = true;
        }

        // Check for a name required
        if (TextUtils.isEmpty(name)) {
            mNameView.setError(getString(R.string.error_field_required));
            focusView = mNameView;
            cancel = true;
        }
        // Check for a mobile required
        if (TextUtils.isEmpty(mobile)) {
            mMobileView.setError(getString(R.string.error_field_required));
            focusView = mMobileView;
            cancel = true;
        }

        // Check for a password required
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        //Perform firebase signup call only after passing all local validation on the fields.
        if(!cancel){
            //TODO perform firebase sign up call here.
            mFirebaseRef.createUser(email,password, new Firebase.ValueResultHandler<Map<String, Object>>() {
                @Override
                public void onSuccess(Map<String, Object> stringObjectMap) {
                    //Actual firebase /users insertion here.
                    createUserInFireBase((String)stringObjectMap.get("uid"));
                    //Hide the progess dialog
                    mAuthProgressDialog.dismiss();
                    //Create new intent and forward to LoginActivity
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }


                @Override
                public void onError(FirebaseError firebaseError) {
                    mAuthProgressDialog.dismiss();
                    //TODO Set the error to the fields based on the provided firebaseError
                    mEmailView.setError(firebaseError.getMessage());
                    focusView = mEmailView;
                    cancel = true;
                /* Error occurred, log the error and dismiss the progress dialog */
                    Log.d(LOG_TAG, firebaseError.getMessage());

                }
            });


        }
        // There was an error; don't attempt sign up and focus the first
        // form field with an error.
        if (cancel) {
            mAuthProgressDialog.dismiss();
            cancel = false;
            focusView.requestFocus();
        }
    }

    private boolean isPhoneValid(String phoneNum) {
        //TODO: Replace this with your own logic
        return phoneNum.length() == 10;
    }
    private boolean isEmailValid(String emailAdd) {
        //TODO: Replace this with your own logic
        return emailAdd.contains("@");
    }

    private boolean isPasswordValid(String pwd) {
        //TODO: Replace this with your own logic
        return pwd.length() > 4;
    }

    private void createUserInFireBase(final String uid) {
        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(uid);
        User user = new User(uid, name, email, mobile);
        userLocation.setValue(user);
    }


}

