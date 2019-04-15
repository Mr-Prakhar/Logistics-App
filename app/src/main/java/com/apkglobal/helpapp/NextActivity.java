package com.apkglobal.helpapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.View;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NextActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText name,rollno;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseUser user ;
    private DatabaseReference reference,reference1,reference2;
    private Button mregisterbtn;
    private Spinner mspinner;
    private RadioGroup mradiogroup;
    private RadioButton mradiobtn;
    private int radioid;

    private String uemail,urepass,uname,uroll;
    private String userId,utype, radioval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);


        rollno=(EditText)findViewById(R.id.edroll);
        name=(EditText)findViewById(R.id.edname);
        mregisterbtn = (Button)findViewById(R.id.btregister);
        Intent myIntent = getIntent();
        auth= FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        mspinner = (Spinner)findViewById(R.id.spinner);
        mradiogroup = (RadioGroup)findViewById(R.id.radiogp);


        mspinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("User Type");
        categories.add("I M DRIVER");
        categories.add("I M CUSTOMER");



        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mspinner.setAdapter(dataAdapter);



        //for reference to the database
        //this root will point towards gps tracker database main root we us only root and child for child root
        //reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");

        reference = FirebaseDatabase.getInstance().getReference().child("Drivers");
        reference1 = FirebaseDatabase.getInstance().getReference().child("Customer");



            if (myIntent != null) {
                uemail = myIntent.getStringExtra("email");
                urepass = myIntent.getStringExtra("repassword");

            }

            mregisterbtn.setOnClickListener(new android.view.View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {

                    if (TextUtils.isEmpty(name.getText().toString().trim())) {
                        name.setError("Field Empty");
                    } else if (TextUtils.isEmpty(rollno.getText().toString().trim())) {
                        rollno.setError("Field Empty");
                    } else if (TextUtils.equals(utype, "User Type")) {
                        Toast.makeText(getApplicationContext(), "User Type not Selected", Toast.LENGTH_SHORT).show();

                    } else {
                        //for Radio Button
                        radioid = mradiogroup.getCheckedRadioButtonId();
                        mradiobtn = (RadioButton) findViewById(radioid);
                        radioval = mradiobtn.getText().toString();

                        progressDialog.setMessage("Please Wait Account is Creating");
                        progressDialog.show();
                        auth.createUserWithEmailAndPassword(uemail, urepass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    uname = name.getText().toString();
                                    uroll = rollno.getText().toString();


                                    CreateUser createuser = new CreateUser(uname, uroll, uemail, urepass, radioval, utype, "na", "na");
                                    user = auth.getCurrentUser();
                                    userId = user.getUid();

                                    if (utype == "I M DRIVER") {
                                        reference.child(userId).setValue(createuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();

                                                    Toast.makeText(getApplicationContext(), "User Registered Sucessfully, please login!", Toast.LENGTH_SHORT).show();
                                                    Intent myIntent = new Intent(NextActivity.this, LoginActivity.class);
                                                    startActivity(myIntent);

                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "User not Registered ", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });

                                    }
                                    else if (utype == "I M CUSTOMER") {
                                        reference1.child(userId).setValue(createuser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    progressDialog.dismiss();

                                                    Toast.makeText(getApplicationContext(), "User Registered Sucessfully, please login!", Toast.LENGTH_SHORT).show();
                                                    Intent myIntent = new Intent(NextActivity.this, CustomerLoginActivity.class);
                                                    startActivity(myIntent);

                                                } else {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(getApplicationContext(), "User not Registered ", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });

                                    }
                                }
                                else {
                                    Toast.makeText(NextActivity.this, "Account already exist...", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            }
                        });
                    }
                }
            });
        }




    @Override
    public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id)
    {
        utype = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
