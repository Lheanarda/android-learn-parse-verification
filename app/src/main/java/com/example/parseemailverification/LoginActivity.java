package com.example.parseemailverification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText edtusername,edtpassword;
    private String username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtusername = findViewById(R.id.edtUsernameLogin);
        edtpassword = findViewById(R.id.edtPasswordLogin);
    }

    public void LoginClick(View view){
        //var
        username = edtusername.getText().toString();
        password = edtpassword.getText().toString();
        //login
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user!=null){
                    if(user.getBoolean("emailVerified")){
                        alertDisplayer("Login Successful","Welcome, "+username+ " !",false);
                    }else {
                        user.logOut();
                        alertDisplayer("Login failed","Please verify your email first!",true);
                    }
                }else{
                    user.logOut();
                    alertDisplayer("Login failed",e.getMessage()+"Please re-try",true);
                }
            }
        });

    }
    public void alertDisplayer(String alertTitle, String alertMessage, final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(alertTitle)
                .setMessage(alertMessage)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
    public void DontHaveAccountClick(View view){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
