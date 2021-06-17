package com.example.myapplicationpln.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.Gspinner;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    //Deklarasi Variable
    private ArrayList<Gspinner> gspinners;
    private AppDatabase appDatabase;
    private Context context;

    public AddressAdapter(ArrayList<Gspinner> gspinners, Context context) {

        //Inisialisasi data yang akan digunakan
        this.gspinners = gspinners;
        this.context = context;
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_6)
                .build();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //Deklarasi View yang akan digunakan
        private TextView Nim, Nama;

        ViewHolder(View itemView) {
            super(itemView);
            Nim = itemView.findViewById(R.id.rTitleTv2);
            Nama = itemView.findViewById(R.id.rDescriptionTv2);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inisialisasi Layout Item untuk RecyclerView
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_address2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        String getNIM = gspinners.get(position).getAddress_update();
        int getNama = gspinners.get(position).getId_pelanggan();
        String getNamatoString = String.valueOf(getNama);
        Log.d("adapterz address", " "+getNIM+" "+getNama);
        Log.d("adapterz address2", " "+getNamatoString);
        //Menampilkan data berdasarkan posisi Item dari RecyclerView
        holder.Nim.setText(getNIM);
        holder.Nama.setText(getNamatoString);
    }


    @Override
    public int getItemCount() {
        //Menghitung data / ukuran dari Array
        return gspinners.size();
    }
}