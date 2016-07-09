package info.prateep.android.mymealbag.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import info.prateep.android.mymealbag.R;
import info.prateep.android.mymealbag.model.User;

/**
 * A login screen that offers login via email/password.
 */
public class SignupActivity extends AppCompatActivity {
    private static final String LOG_TAG = SignupActivity.class.getSimpleName();
    // Firebase database
    FirebaseDatabase database;
    DatabaseReference myRef;
    // UI references.
    private String email, password, name, mobile;
    private EditText mNameView, mMobileView;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mSignUpFormView;
    private ProgressDialog mAuthProgressDialog;
    private boolean cancel = false;
    private View focusView = null;
    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Set up the login form.
        mNameView = (EditText) findViewById(R.id.sign_up_name);
        mMobileView = (EditText) findViewById(R.id.sign_up_mobile);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.sign_up_email);
        mPasswordView = (EditText) findViewById(R.id.sign_up_password);

        //Init Firebase Authentication
        mFirebaseAuth = FirebaseAuth.getInstance();

        Button mEmailSignUpButton = (Button) findViewById(R.id.email_sign_up_button);
        mEmailSignUpButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        mSignUpFormView = findViewById(R.id.sign_up_form);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //Create User Data
                    createUserInFireBase(user.getUid().toString());
                    //Hide the progess dialog
                    mAuthProgressDialog.dismiss();
                    //Create new intent and forward to LoginActivity
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Log.d(LOG_TAG, "onAuthStateChanged:signed_out");
                }

            }
        };
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
        if (!cancel) {
            //Perform firebase sign up call here.

            mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(LOG_TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                mAuthProgressDialog.dismiss();
                                //TODO Set the error to the fields based on the provided firebaseError
                                mEmailView.setError("Authentication failed");
                                focusView = mEmailView;
                                cancel = true;
                            }
                        }
                    });
            mFirebaseAuth.addAuthStateListener(mAuthListener);

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
        database = FirebaseDatabase.getInstance();
        User user = new User();
        user.setUid(uid);
        user.setName(name);
        user.setEmail(email);
        user.setMobile(mobile);
        //user.setIsAdmin("Y");

        Map<String, String> myItems = new HashMap<String, String>();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());

        myItems.put(formattedDate,"Sample Item by Chef Name");

        user.setItems(myItems);

        myRef = database.getReference("users");
        myRef.child(uid).setValue(user);
    }

}

