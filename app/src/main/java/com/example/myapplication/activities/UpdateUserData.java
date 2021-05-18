package com.example.myapplication.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUserData extends AppCompatActivity {

    EditText usernametxt, email;
    Button update_user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_update);

        usernametxt = findViewById(R.id.id_UsernameUpdated);
        email = findViewById(R.id.id_email_Update);

        Intent intent = getIntent();
        String id = intent.getStringExtra("userEmail");
        Log.d("DATA intent userEmails ", "userEmail: " +id);

        String username = intent.getStringExtra("userName");
        Log.d("DATA intent userNames ", "userName: " +username);

        String userPhone = intent.getStringExtra("userPhone");
        Log.d("DATA intent userPhonez ", "userPhone: " +userPhone);

        email.setText(id);
        usernametxt.setText(username);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference mDatabaseRef = database.getReference();

        update_user = findViewById(R.id.button_udpate_user);
        update_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String update_username = usernametxt.getText().toString();
                String update_email = email.getText().toString();

                mDatabaseRef.child("User2").child(userPhone).child("username").setValue(String.valueOf(update_username));
                mDatabaseRef.child("User2").child(userPhone).child("email").setValue(String.valueOf(update_email));

                Context context = view.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);

            }
        });

    }
}
