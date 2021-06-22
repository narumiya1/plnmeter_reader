package com.example.myapplicationpln.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.Ghistoryi;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        //Deklarasi View yang akan digunakan
        private TextView Meter, DateCreated, Classify, Identify;
        private PhotoView imgv;
        private String mImageFileLocation = "";
        private AppDatabase db;

        ViewHolder(View itemView) {
            super(itemView);
            Meter = itemView.findViewById(R.id.rMeter2);
            DateCreated = itemView.findViewById(R.id.txtDate2);
            Classify = itemView.findViewById(R.id.rClassify2);
            Identify = itemView.findViewById(R.id.rIdentify2);
            imgv = itemView.findViewById(R.id.imgvx);
             /*
            String mImageFileLocation;
            AppDatabase db;
            db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addMigrations(AppDatabase.MIGRATION_1_7)
                    .build();

            mImageFileLocation = db.gHistorySpinnerDao().getImageHistory();
            Log.d("image makeover", ""+mImageFileLocation);
            File imgFile = new File(mImageFileLocation);
            if (imgFile.exists()) {
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(mImageFileLocation,
                        bitmapOptions);
                bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);


                PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imgv);
                photoViewAttacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

             */

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
        String getImg = ghistoryi.get(position).getImagez();
        Log.d("historiyi", " " + getImg);
        Log.d("historiyi", " " + getClasfy);
        Log.d("historiyi", " " + getMeter);
        AppDatabase db;
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();
        String mImageFileLocation;

        mImageFileLocation = db.gHistorySpinnerDao().getImageHistory(getMeter);
        File imgFile = new File(mImageFileLocation);
        Log.d("image makeover", ""+mImageFileLocation);
        Bitmap bitmap;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(mImageFileLocation,
                bitmapOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        holder.imgv.setImageBitmap(BitmapFactory.decodeFile(mImageFileLocation));

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