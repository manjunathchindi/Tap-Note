package com.example.tapnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailEdittext, passwordEdittext ;
    Button loginButton;
    ProgressBar progressBar;
    TextView CreateAccountTextviewButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEdittext = findViewById(R.id.email_edittext);
        passwordEdittext = findViewById(R.id.password_edittext);

        loginButton = findViewById(R.id.login_button);
        progressBar= findViewById(R.id.progressbar);
        CreateAccountTextviewButton = findViewById(R.id.create_account_textview_button);


        loginButton.setOnClickListener(v -> loginUser() );
        CreateAccountTextviewButton.setOnClickListener(v-> startActivity (new Intent(LoginActivity.this, CreateAccountActivity.class)));

    }

     void loginUser() {

         String email = emailEdittext.getText().toString();
         String password = passwordEdittext.getText().toString();
         boolean isValidated = validateData(email,password);

         if(!isValidated){
             return;
         }
         loginAccountInFirebase(email,password);

     }
//-------------------------------------------------------------------------------------------------------------------------

     void loginAccountInFirebase(String email,String password){

        changeInProgress(true);

         FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
         firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 changeInProgress(false);

                 if(task.isSuccessful()){
                     //login is successful
                     if(firebaseAuth.getCurrentUser().isEmailVerified()){
                         //go to mainactivity
                         startActivity(new Intent(LoginActivity.this, MainActivity.class));
                         finish();

                     }
                     else{
                         Utility.showToastMessage(LoginActivity.this,"OOps! Email not verified, Please verify your email");
                     }



                 }
                 else{
                     //login failed

                     Utility.showToastMessage(LoginActivity.this,task.getException().getLocalizedMessage());
                 }



             }
         });

     }


//-----------------------------------------------------------------------------------------------------------------
    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

//    ----------------------------------------------------------------------------------------------------------


    boolean validateData(String email, String password){
        //To validate the data that input is given by the user
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdittext.setError("Email is invalid ");
            return false;
        }
        if(password.length()<6){
            passwordEdittext.setError("Enter Valid Password");
            return  false;
        }

        return true;

    }
}