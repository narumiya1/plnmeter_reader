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
import com.example.myapplicationpln.roomDb.Ghistoryi;

import java.util.ArrayList;

public class HistoryAdapter2 extends RecyclerView.Adapter<HistoryAdapter2.ViewHolder> {

    //Deklarasi Variable
    private ArrayList<Ghistoryi> ghistoryi;
    private AppDatabase appDatabase;
    private Context context;

    public HistoryAdapter2(ArrayList<Ghistoryi> ghistoryi, Context context) {

        //Inisialisasi data yang akan digunakan
        this.ghistoryi = ghistoryi;
        this.context = context;
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_6)
                .build();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        //Deklarasi View yang akan digunakan
        private TextView Meter, DateCreated, Classify, Identify;

        ViewHolder(View itemView) {
            super(itemView);
            Meter = itemView.findViewById(R.id.rMeter2);
            DateCreated = itemView.findViewById(R.id.txtDate2);
            Classify = itemView.findViewById(R.id.rClassify2);
            Identify = itemView.findViewById(R.id.rIdentify2);

        }

    }

    @NonNull
    @Override
    public HistoryAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inisialisasi Layout Item untuk RecyclerView
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history2, parent, false);
        return new HistoryAdapter2.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String getDate = ghistoryi.get(position).getCreated_at();
        String getMeter = ghistoryi.get(position).getMeter();
        String getClasfy = ghistoryi.get(position).getScore_classfy();
        String getIdtfy = ghistoryi.get(position).getScore_identfy();
        Log.d("historiyi"," "+getClasfy);
        Log.d("historiyi"," "+getMeter);

        //Menampilkan data berdasarkan posisi Item dari RecyclerView
        holder.Meter.setText(String.valueOf(getMeter));
        holder.DateCreated.setText(getDate);
        holder.Classify.setText(getClasfy);
        holder.Identify.setText(getIdtfy);
    }


    @Override
    public int getItemCount() {
        //Menghitung data / ukuran dari Array
        return ghistoryi.size();
    }
}