package com.apkglobal.helpapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailreg,et_password,et_repassword;
    private FirebaseAuth auth;//declare firebase authentication
    private ProgressDialog dialog; // for loading
    private String email;
    private String password;
    private String repassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailreg=(EditText)findViewById(R.id.editText2);
        et_password=(EditText)findViewById(R.id.editText3);
        et_repassword=(EditText)findViewById(R.id.editText4);

        auth=FirebaseAuth.getInstance();//type cast firebase authentication
        dialog= new ProgressDialog(this);


        }

    public void gotopasswordActivity(View v)
    {
        email = emailreg.getText().toString().trim();
        password = et_password.getText().toString().trim();
        repassword= et_repassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailreg.setError("Enter Email");
            return;
        }

        else if (TextUtils.isEmpty(password)) {
            et_password.setError("Enter Password");
            return;
        }
        else if (TextUtils.isEmpty(repassword)) {
            et_repassword.setError("Enter Confirm Password");
            return;
        }

        else if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (repassword.length() < 6) {
            Toast.makeText(getApplicationContext(), "Confirm Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!password.equals(repassword)) {
            Toast.makeText(getApplicationContext(), "Password Mismatched", Toast.LENGTH_SHORT).show();
            return;
        }



        else
        {
            dialog.setMessage("Checking Your Email Address");

            dialog.show();
            // check if this email is already registered or not
            //authentication for register email
            auth.fetchProvidersForEmail(emailreg.getText().toString()).
                    addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                boolean check = !task.getResult().getProviders().isEmpty();

                                if (!check) {
                                    // email does not exist, so we can create this email with user
                                    Intent myIntent = new Intent(RegisterActivity.this, NextActivity.class);
                                    myIntent.putExtra("email", emailreg.getText().toString());//pass email to next activity
                                    myIntent.putExtra("repassword", et_repassword.getText().toString());
                                    startActivity(myIntent);


                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "This Email is already Registered",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } else if (emailreg.equals("")) {
                                Toast.makeText(getApplicationContext(), "You did not enter a username", Toast.LENGTH_SHORT).show();
                                return;
                            }


                        }
                    });
        }
    }
}
