package com.example.mp8_qishu_zhang;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailText;
    private EditText passwordText;
    private Button createButton;
    private Button loginButton;

    //defining firebaseauth object
    FirebaseAuth firebaseAuth;
    //is a user logged in?
    FirebaseUser user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize firebase auth obj
        firebaseAuth = FirebaseAuth.getInstance();

        //initialize views
        createButton = findViewById(R.id.signup_button);
        loginButton = findViewById(R.id.login_button);
        emailText = findViewById(R.id.email_editTxt);
        passwordText = findViewById(R.id.password_editTxt);

        //attach listener to button
        createButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    private void registerUser() {

        //getting email and password from edit texts
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        //checking if email and passwords are empty
        if(email.isEmpty()){
            emailText.setError("Email is required");
            emailText.requestFocus();
            return;
        } else if (!isEmailValid(email)){
            emailText.setError("Invalid email format");
            emailText.requestFocus();
            return;
        } else if (password.isEmpty()){
            passwordText.setError("Password is required");
            passwordText.requestFocus();
            return;
        }else if (password.length()<6){
            passwordText.setError("Password must be 6 characters or more");
            passwordText.requestFocus();
            return;
        }

        //if the email and password are not empty
        //creating a new user
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Account has been successfully created", Toast.LENGTH_SHORT).show();
                            user = firebaseAuth.getCurrentUser(); //The newly created user is already signed in
                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(getApplicationContext(), "Error. Account already exists.", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void userLogin(){
        final String email = emailText.getText().toString().trim();
        final String password = passwordText.getText().toString().trim();

        if(email.isEmpty()){
            emailText.setError("Email is required");
            emailText.requestFocus();
            return;
        } else if (!isEmailValid(email)){
            emailText.setError("Invalid email format");
            emailText.requestFocus();
            return;
        } else if (password.isEmpty()){
            passwordText.setError("Password is required");
            passwordText.requestFocus();
            return;
        }else if (password.length()<6){
            passwordText.setError("Password must be 6 characters or more");
            passwordText.requestFocus();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                            user = firebaseAuth.getCurrentUser(); //The user is signed in
                            Intent intent  = new Intent(MainActivity.this, PullActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @Override
    public void onClick(View view) {
        //calling register/login method on click
        switch(view.getId()){
            case R.id.signup_button:
                registerUser();
                break;

            case R.id.login_button:
                userLogin();
                break;

        }

    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
