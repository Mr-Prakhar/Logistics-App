package com.apkglobal.helpapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerLoginActivity extends AppCompatActivity
{
    private FirebaseAuth auth;
    private EditText logemail, logpass;
    private FirebaseUser currents_user;
    private String uid;
    private ProgressDialog dialog;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        auth = FirebaseAuth.getInstance();
        logemail= findViewById(R.id.editText);
        logpass= findViewById(R.id.editText2);
        dialog = new ProgressDialog(this);




    }
    public void login(View view)
    {

        if(TextUtils.isEmpty(logemail.getText().toString().trim()))
        {
            logemail.setError("Field Empty");
        }
        else if (TextUtils.isEmpty(logpass.getText().toString().trim()))
        {
            logpass.setError("Field Empty");
        }
        else
        {
            dialog.setMessage("Logging In...");
            dialog.show();
            auth.signInWithEmailAndPassword(logemail.getText().toString(),logpass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                currents_user=auth.getCurrentUser();
                                uid=currents_user.getUid();

                                Toast.makeText(getApplicationContext(), "Successfully Logged in", Toast.LENGTH_SHORT).show();
                                mRef=FirebaseDatabase.getInstance().getReference().child("Customer").child(uid);
                                dialog.dismiss();
                                Intent intent=new Intent(CustomerLoginActivity.this,CustomersMapsActivity.class);
                                startActivity(intent);

                   /* mRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child("utype").exists()){

                                String utyps=dataSnapshot.child("utype").getValue().toString();
                                Log.i("itype",utyps);


                                //for Jump to Student Activity
                                if (utyps.equals("I M CUSTOMER"))
                                {
                                    dialog.dismiss();
                                    Intent intent=new Intent(LoginActivity.this,CustomersMapsActivity.class);
                                    startActivity(intent);
                                }
                                // For Jump to  Proctorial Board Activity
                                else if (utyps.equals("I M DRIVER"))
                                {
                                    dialog.dismiss();
                                    Intent intent=new Intent(LoginActivity.this,DriversMapsActivity.class);
                                    startActivity(intent);
                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/


                            }
                            else
                            {
                                dialog.dismiss();
                                Toast.makeText(CustomerLoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }


}
