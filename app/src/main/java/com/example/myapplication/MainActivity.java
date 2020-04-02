package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.WidgetContainer;

import android.app.ProgressDialog;
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

public class MainActivity extends AppCompatActivity {



    private EditText Name;
    private TextView forgotpasswd;
    private EditText Password;
    private TextView Info;
    private TextView signup;
    private Button Login;
    private int counter = 5;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String name,password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText)findViewById(R.id.etName);
        Password = (EditText)findViewById(R.id.etPassword);
        Info = (TextView) findViewById(R.id.etInfo);
        Login = (Button) findViewById(R.id.etlogin);
        signup = (TextView) findViewById(R.id.tvRegister);
        forgotpasswd = (TextView)findViewById(R.id.etforgotpassword);


        Info.setText("No of attempts remaining: 5");
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != (null)){
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(),Password.getText().toString());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,RegistrationActivity.class));
            }
        });

        forgotpasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,forgotpassword.class));
            }
        });

      /*   if (validatee()) {
            Login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast toast = Toast.makeText(MainActivity.this, "Please fill all Details", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 200);
                    toast.show();
                }
            });
        } */

    }

    private void validate(String userName,String userPassword){
        progressDialog.setMessage("Processing");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    checkEmailVerification();
                }
                else {

                    Toast toast = Toast.makeText(MainActivity.this,"Login Failed",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,200);
                    toast.show();
                    progressDialog.dismiss();
                    counter--;
                    Info.setText("No. of attemts remaining: " + counter);
                    if (counter == 0){
                        Login.setEnabled(false);
                    }

                }
            }
        });

    }

    private void checkEmailVerification(){
        FirebaseUser firebaseUser = mAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if (emailflag){
            sendUserData();
            progressDialog.dismiss();
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));

        }
        else{
            Toast toast = Toast.makeText(MainActivity.this,"verify your email",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,200);
            toast.show();
            progressDialog.dismiss();
            mAuth.signOut();

        }
    }

    private Boolean validatee(){
        Boolean result = true;



        if (name.isEmpty() || password.isEmpty()) {
            Toast toast = Toast.makeText(MainActivity.this,"Please Enter All Details",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,200);
            toast.show();
        }
        else {
            result = false;

        }
        return result;
    }





    private void sendUserData(){
        name = Name.getText().toString();
        password = Password.getText().toString();

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mDatabase.getReference(mAuth.getUid());
        UserProfile userProfile = new UserProfile(name,password);
        myRef.setValue(userProfile);
    }

}
