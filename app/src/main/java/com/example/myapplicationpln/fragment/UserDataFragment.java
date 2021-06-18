package com.example.myapplicationpln.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.activities.AddressListActivity2;
import com.example.myapplicationpln.activities.FirebaseRecyclerAdapterAddress;
import com.example.myapplicationpln.activities.UpdateUserData;
import com.example.myapplicationpln.model.Connection;
import com.example.myapplicationpln.model.Toastr;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.model.DataUser;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GUserData;
import com.example.myapplicationpln.roomDb.Ghistoryi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDataFragment extends Fragment {
    private AppDatabase db;
    private ArrayList<GUserData> listuser;
    String name, getUserIdfSession, adress, numberPhone;
    TextView tvUserName, tvEmail, tvAddress, tvPhone, tv_realname, tv_id_pel;
    TextView tvUserEdit;
    ImageView imageView_address;

    SessionPrefference session;
    DataUser dataUser = new DataUser();

    public UserDataFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.example_prof, container, false);
        db = Room.databaseBuilder(getContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_6)
                .build();
        listuser = new ArrayList<>();
        tvUserName = rootView.findViewById(R.id.tvUser);
        tv_realname = rootView.findViewById(R.id.tv_realname);
        tvPhone = rootView.findViewById(R.id.tv_account_phone);
        tvEmail = rootView.findViewById(R.id.tvemail_account);
        tvAddress = rootView.findViewById(R.id.tv_account_alamat);
        tv_id_pel = rootView.findViewById(R.id.tv_id_pel);
        tvUserEdit = rootView.findViewById(R.id.tvUserEdit);

        imageView_address = rootView.findViewById(R.id.imageView_address);
        //checking connection here
        if (!Connection.isConnect(getActivity())){
            imageView_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toastr.showToast(getActivity(), "no internet connection");
                    Intent intent = new Intent(getActivity(), AddressListActivity2.class);
                    startActivity(intent);
                }
            });
        }else {
            imageView_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(getActivity(), FirebaseRecyclerAdapterAddress.class);
                    startActivity(intent);
                }
            });

        }

        /*
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String val = String.valueOf(dataSnapshot.child("id_user").getValue());
                    Log.d("get value id user", " : " +val );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        */
        session = new SessionPrefference(getActivity());
        numberPhone = session.getPhone();
        getUserIdfSession = session.getUserId();
        Log.d("get getUserIdfSession ph", " : " + getUserIdfSession);

        /*
         one to many
        firebase.database().ref('works').orderBy('customerKey').equalTo('customerKey2')
        */
        if (Connection.isConnect(getActivity())) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference.child("User").child(String.valueOf(numberPhone));
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d("DATA CHANGEt", "onDataChange: " + dataSnapshot.getValue());
                        dataUser = dataSnapshot.getValue(DataUser.class);
                        Log.d("DATA CHANGE detail", "onDataChange: " + dataUser.getAddress());
                        tvUserName.setText(dataUser.getUsername());
                        tv_realname.setText(dataUser.getName());
                        tvEmail.setText(dataUser.getEmail());
                        tvPhone.setText(dataUser.getPhone());
                        tvAddress.setText(dataUser.getAddress());
                        tv_id_pel.setText(dataUser.getId_pelanggan());

                        String id_pel = dataUser.getId_user();
                        session.setUserId(id_pel);
                        tvUserEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                selectImage();
                                Intent intent = new Intent(getActivity(), UpdateUserData.class);
                                intent.putExtra("userEmail", dataUser.getEmail());
                                intent.putExtra("userName", dataUser.getUsername());
                                intent.putExtra("userPhone", dataUser.getPhone());

                                startActivity(intent);
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            String selection = db.gHistorySpinnerDao().readDataUser() ;
            Log.d("selectionDb  ","name "+selection +" ? ");

            if (selection!=null){
                int iduser = Integer.parseInt(session.getUserId()) ;
                List<GUserData> selectionu = Arrays.asList(db.gHistorySpinnerDao().selectDataUser());
                GUserData gUserData = db.gHistorySpinnerDao().selectDetailUserData(session.getUserId());
                Log.d("selectionlistuser  ","name "+selectionu +" ? ");
                Log.d("selectionsx  ","name "+selection);
                Log.d("gUserData  ","gUserData "+gUserData.getUsername());
                tvUserName.setText(selection);
                tv_realname.setText(String.valueOf(gUserData.getUsername()));
                tvPhone.setText(String.valueOf(gUserData.getPhone()));
                tvEmail.setText(String.valueOf(gUserData.getEmail()));
                tvAddress.setText(String.valueOf(gUserData.getAddress()));
                tv_id_pel.setText(String.valueOf(gUserData.getId_pel()));
                tvUserEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toastr.showToast(getActivity(), "Cant edited, turn on connection");
                    }
                });
            }else {
                Toastr.showToast(getActivity(), "no connection");
            }

        }
        return rootView;
    }

    private void selectImage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Change User Name");
        alertDialog.setMessage("Enter Here");
        final EditText input = new EditText(getActivity());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setIcon(R.drawable.tickinsidecircle);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        Toast.makeText(getActivity(), "Oke", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}
