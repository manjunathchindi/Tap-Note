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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccountActivity extends AppCompatActivity {


    EditText emailEdittext, passwordEdittext , confirmPasswordEdittext;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView alreadyLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        emailEdittext = findViewById(R.id.email_edittext);
        passwordEdittext = findViewById(R.id.password_edittext);
        confirmPasswordEdittext = findViewById(R.id.confirmmail_edittext);

        createAccountBtn = findViewById(R.id.create_account_button);
        progressBar= findViewById(R.id.progressbar);
        alreadyLogin = findViewById(R.id.already_haveaccount);


        createAccountBtn.setOnClickListener(v -> createAccount() );
        alreadyLogin.setOnClickListener(v-> startActivity (new Intent(CreateAccountActivity.this, LoginActivity.class)));

    }

    void createAccount(){
        String email = emailEdittext.getText().toString();
        String password = passwordEdittext.getText().toString();
        String confirmPassword = confirmPasswordEdittext.getText().toString();

        boolean isValidated = validateData(email,password,confirmPassword);

        if(!isValidated){
            return;
        }
        createAccountInFirebase(email,password);

    }

     void createAccountInFirebase(String email, String password) {
        changeInProgress(true);

         FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
         firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                    changeInProgress(false);
                 if(task.isSuccessful()){  //Account created successful

                     Utility.showToastMessage(CreateAccountActivity.this,"Account created successfully, Check your email to verify");
                     firebaseAuth.getCurrentUser().sendEmailVerification();
                     firebaseAuth.signOut();
                     finish();
                 }
                 else{ //Account not created
                     Utility.showToastMessage(CreateAccountActivity.this,task.getException().getLocalizedMessage());

                 }

             }
         });


    }

    void changeInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }


    boolean validateData(String email, String password, String confirmPassword){
        //To validate the data that input is given by the user
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdittext.setError("Email is invalid ");
            return false;
        }
        if(password.length()<6){
            passwordEdittext.setError("Password must be at least 6 characters");
            return  false;
        }
        if(!password.equals(confirmPassword)){
            confirmPasswordEdittext.setError("Password does not match ");
        }

        return true;

    }
}