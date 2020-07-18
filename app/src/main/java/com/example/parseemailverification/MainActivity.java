package com.example.parseemailverification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private EditText edtEmail,edtUsername,edtPassword;
    private String email,username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ui
        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

        //notify download to parse dashboard
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }

    public void SignUpClick(View view){
        //var
        email = edtEmail.getText().toString();
        username = edtUsername.getText().toString();
        password = edtPassword.getText().toString();
        if(!email.contains("@") || !email.contains(".") || email.equals("") || email.length()<5){
            edtEmail.setError("Enter valid email");
        }else if (username.equals("")){
            edtUsername.setError("Enter username");
        }else if(checkCharacters(password)==false){
            edtPassword.setError("Password must contain number, symbol, lowercase letter, and capital letter!");
        }else {
            try{
                //Sign Up With Parse

                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setPassword(password);
                user.setEmail(email);
                Log.i("checkApp","username :"+ email);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            ParseUser.logOut();
                            alertDisplayer("Account Created","Please verify your email before login!",false);
                        }else{
                            ParseUser.logOut();
                            alertDisplayer("Account Creation Failed","Failed to create account : "
                                    +e.getMessage(),true);

                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public boolean checkCharacters(String password){
        Boolean result = false;
        Pattern pattern = Pattern.compile("^" +
                "(?=.*[a-z])" + //check lower case
                "(?=.*[A-Z])" + //check upper case
                "(?=.*\\d)" + //check number
                ".+$");
        Matcher matcher = pattern.matcher(password);
        Boolean check = matcher.find();

        //check symbol
        Pattern pSymbol = Pattern.compile("[^A-Za-z0-9]");
        Matcher mSymbol = pSymbol.matcher(password);
        Boolean cSymbol = mSymbol.find();
        //end check symbol

        if(check && cSymbol) result = true;
        return result;
    }

    public void alertDisplayer(String alertTitle, String alertMessage, final boolean error){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(alertTitle)
                .setMessage(alertMessage)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(!error){
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
