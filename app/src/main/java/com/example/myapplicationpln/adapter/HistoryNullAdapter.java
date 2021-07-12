package com.example.myapplicationpln.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GimageUploaded;

import java.util.ArrayList;

public class HistoryNullAdapter extends RecyclerView.Adapter<HistoryNullAdapter.ViewHolderz> {

    //Deklarasi Variable
    private ArrayList<GimageUploaded> gimageUploadeds;

    private AppDatabase appDatabase;
    private Context context;

    public HistoryNullAdapter(ArrayList<GimageUploaded> gimageUploadeds, Context context) {

        //Inisialisasi data yang akan digunakan
        this.gimageUploadeds = gimageUploadeds;
        this.context = context;
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();
    }

    class ViewHolderz extends RecyclerView.ViewHolder {

        //Deklarasi View yang akan digunakan
        private TextView Meter, DateCreated, Classify, Identify;


        ViewHolderz(View itemView) {
            super(itemView);
            Meter = itemView.findViewById(R.id.status);

        }

    }

    @NonNull
    @Override
    public HistoryNullAdapter.ViewHolderz onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inisialisasi Layout Item untuk RecyclerView
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history_null, parent, false);
        return new HistoryNullAdapter.ViewHolderz(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderz holder, int position) {

        holder.Meter.setText(gimageUploadeds.get(position).getStatus());
    }



    @Override
    public int getItemCount() {
        //Menghitung data / ukuran dari Array
        return gimageUploadeds.size();
    }
}