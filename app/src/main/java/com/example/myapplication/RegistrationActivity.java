package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText username, userpassword, useremail;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth mAuth;
    String name,password,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();

        mAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (validate()){
                   //update data on firebase
                   String user_email = useremail.getText().toString().trim();
                   String user_password = userpassword.getText().toString().trim();

                   mAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {

                           if (task.isSuccessful() ){
                              sendEmailVerification();
                           }
                           else {
                               Toast toast = Toast.makeText(RegistrationActivity.this,"Registration Failed",Toast.LENGTH_SHORT);
                               toast.setGravity(Gravity.TOP,0,200);
                               toast.show();
                           }
                       }
                   });
               }
            }
        } );
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,MainActivity.class));
            }
        });

    }

    private void setupUIViews() {
        username = (EditText)findViewById(R.id.etusername);
        userpassword = (EditText)findViewById(R.id.etuserpassword);
        useremail = (EditText)findViewById(R.id.etuseremail);
        regButton = (Button)findViewById(R.id.etuserregister);
        userLogin = (TextView)findViewById(R.id.etuserlogin);

    }

    private Boolean validate(){
           Boolean result = false;
           name = username.getText().toString();
           password = userpassword.getText().toString();
           email = useremail.getText().toString();

           if (name.isEmpty() || password.isEmpty() || email.isEmpty()) {
               Toast toast = Toast.makeText(RegistrationActivity.this,"Please Enter All Details",Toast.LENGTH_SHORT);
               toast.setGravity(Gravity.TOP,0,200);
               toast.show();
           }
            else {
                result = true;

           }
            return result;
    }

    private void sendEmailVerification(){
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){

                        Toast toast = Toast.makeText(RegistrationActivity.this,"Sucessfully Registered,Verification Mail sent",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(RegistrationActivity.this,MainActivity.class));

                    }
                    else{
                        Toast toast = Toast.makeText(RegistrationActivity.this,"Verfication Mail not sent,try again",Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP,0,200);
                        toast.show();

                    }
                }
            });
        }
    }


}
