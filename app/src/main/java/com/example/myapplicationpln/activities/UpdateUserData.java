package com.example.myapplicationpln.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myapplicationpln.MainActivity;
import com.example.myapplicationpln.R;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GUserData;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUserData extends AppCompatActivity {

    EditText usernametxt, email;
    Button update_user;
    private AppDatabase db;
    SessionPrefference sessionPrefference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_update);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_6)
                .build();
        sessionPrefference = new SessionPrefference(getApplicationContext());
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

                mDatabaseRef.child("User").child(userPhone).child("username").setValue(String.valueOf(update_username));
                mDatabaseRef.child("User").child(userPhone).child("email").setValue(String.valueOf(update_email));
                GUserData gUserData = new GUserData();
                gUserData.setUsername(update_username);
                Log.d("DATA intent setUsername ", "update_username: " +update_username);
                gUserData.setEmail(update_email);
                int id_user = Integer.parseInt(sessionPrefference.getUserId());
//                updateTbUser(gUserData);
                db.gHistorySpinnerDao().updateDataUser(id_user,update_username,update_email);

                /*
                //not used
                int rowCount = db.gHistorySpinnerDao().getCounTbUser();
                if(rowCount==0){
                    gUserData.setUsername(update_username);
                    gUserData.setEmail(update_email);
                    insertTotbUser(gUserData);
                }
                else{
                    gUserData.setUsername(update_username);
                    gUserData.setEmail(update_email);
                    updateTbUser(gUserData);
                }
                 */


                Context context = view.getContext();
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);

            }
        });

    }
    private void insertTotbUser(GUserData gUserData) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().insertUserData(gUserData);
                return status;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Long status) {
//                Toast.makeText(getActivity().getApplicationContext(), "status row " + status, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), "history row added sucessfully" + status, Toast.LENGTH_SHORT).show();
                Log.d("Upload history row added sucessfullys", "String status  : " + status);
            }
        }.execute();

    }

    private void updateTbUser(GUserData gUserData) {
        new AsyncTask<Void, Void, Long>() {
            @Override
            protected Long doInBackground(Void... voids) {
                long status = db.gHistorySpinnerDao().updateUserData(gUserData);
                return status;
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(Long status) {
//                Toast.makeText(getActivity().getApplicationContext(), "status row " + status, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getActivity().getApplicationContext(), "history row added sucessfully" + status, Toast.LENGTH_SHORT).show();
                Log.d("Upload history row added sucessfullys", "String status  : " + status);
            }
        }.execute();

    }
}
