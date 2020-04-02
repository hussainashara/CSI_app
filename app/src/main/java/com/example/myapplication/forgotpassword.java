package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {

    private EditText passwordemail;
    private Button resetpassword;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);

        passwordemail = (EditText)findViewById(R.id.etpasswordemail);
        resetpassword = (Button)findViewById(R.id.resetpasswordbutton);
        mAuth = FirebaseAuth.getInstance();

        resetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            String useremail = passwordemail.getText().toString().trim();

            if (useremail.equals("")){
                Toast toast = Toast.makeText(forgotpassword.this,"Please Enter Your Email",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,200);
                toast.show();
            }
            else{
                mAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()){
                       Toast toast = Toast.makeText(forgotpassword.this,"Reset password email sent",Toast.LENGTH_SHORT);
                       toast.setGravity(Gravity.TOP,0,200);
                       toast.show();
                       finish();
                       startActivity(new Intent(forgotpassword.this,MainActivity.class));

                   }
                   else {
                       Toast toast = Toast.makeText(forgotpassword.this,"Error in sending mail",Toast.LENGTH_SHORT);
                       toast.setGravity(Gravity.TOP,0,200);
                       toast.show();
                   }
                    }
                });

            }


            }
        });
    }
}
