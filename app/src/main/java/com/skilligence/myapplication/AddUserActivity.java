package com.skilligence.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skilligence.myapplication.model.UserModel;

public class AddUserActivity extends AppCompatActivity {

    private EditText edtUsername, edtMobile, edtEmail, edtPassword;
    private Button addUserBtn;

    private UserModel user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        edtUsername = findViewById(R.id.aua_username);
        edtMobile = findViewById(R.id.aua_mobile);
        edtEmail = findViewById(R.id.aua_email);
        edtPassword = findViewById(R.id.aua_password);
        addUserBtn = findViewById(R.id.aua_add_user);

        user = new UserModel();

        addUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setName(edtUsername.getText().toString());
                user.setPassword(edtPassword.getText().toString());
                user.setMobile(edtMobile.getText().toString());
                user.setEmailId(edtEmail.getText().toString());

                database = FirebaseDatabase.getInstance();
                reference = database.getReference("users").push();

                user.setUserId(reference.getKey());

                reference.setValue(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AddUserActivity.this, "User added successfully", Toast.LENGTH_SHORT).show();

                                edtUsername.setText("");
                                edtPassword.setText("");
                                edtEmail.setText("");
                                edtMobile.setText("");
                            }
                        });

            }
        });
    }
}