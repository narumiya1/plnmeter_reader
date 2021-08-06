package com.example.myapplicationpln.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.MToastr;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GHistory;
import com.example.myapplicationpln.roomDb.GhistoryMeter;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HistoryAdapter2 extends RecyclerView.Adapter<HistoryAdapter2.ViewHolder> {

    //Deklarasi Variable
    private ArrayList<GhistoryMeter> listHistory; //salah, nama variable tidak oleh sama dengan nama class
    private AppDatabase appDatabase;
    private Context context;

    public HistoryAdapter2(ArrayList<GhistoryMeter> gHistories, Context context) {

        //Inisialisasi data yang akan digunakan
        this.listHistory = gHistories;
        this.context = context;
        /*
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();

         */
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        //Deklarasi View yang akan digunakan
        private TextView Meter, DateCreated, Classify, Identify, kurung, koma, kurung2;
        private PhotoView imgv;
        private ImageView done, doneAll, pending;
        private CardView cv_clicked;
        private String mImageFileLocation = "";
        private AppDatabase db;

        ViewHolder(View itemView) {
            super(itemView);
            Meter = itemView.findViewById(R.id.rMeter2);
            DateCreated = itemView.findViewById(R.id.txtDate2);
            Classify = itemView.findViewById(R.id.rClassify2);
            Identify = itemView.findViewById(R.id.rIdentify2);
            imgv = itemView.findViewById(R.id.imgvx);
            done = itemView.findViewById(R.id.chekclist_done);
            pending = itemView.findViewById(R.id.pending);
            doneAll = itemView.findViewById(R.id.chekclist_done_all);
            koma = itemView.findViewById(R.id.koma);
            kurung = itemView.findViewById(R.id.kurung);
            kurung2 = itemView.findViewById(R.id.kurung2);
            cv_clicked = itemView.findViewById(R.id.cvMain_historiy);
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history2, parent, false);
        return new HistoryAdapter2.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date timeCreated = listHistory.get(position).getDate_time();
        String strTimeCreated = formatter.format(timeCreated);
        GhistoryMeter history = listHistory.get(position);

        //Menampilkan data berdasarkan posisi Item dari RecyclerView
        if (history.getStatus() == 1) {

            showStatus1Visible(holder,strTimeCreated);

        } else if (history.getStatus() == 2) {

            showStatus2Visible(holder, strTimeCreated);

        } else {

            showStatus3Visible(history, holder, strTimeCreated);

        }

    }
    private void showStatus1Visible(ViewHolder holder, String strTimeCreated) {
        holder.DateCreated.setText(strTimeCreated);
        holder.done.setVisibility(View.VISIBLE);
        holder.doneAll.setVisibility(View.GONE);
        holder.pending.setVisibility(View.GONE);
        holder.DateCreated.setGravity(Gravity.LEFT);
        holder.koma.setVisibility(View.GONE);
        holder.kurung.setVisibility(View.GONE);
        holder.kurung2.setVisibility(View.GONE);
        holder.Meter.setVisibility(View.GONE);
        holder.Classify.setVisibility(View.GONE);
        holder.Identify.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 90, 16, 0);
        holder.DateCreated.setLayoutParams(params);
    }
    private void showStatus2Visible(ViewHolder holder, String strTimeCreated) {

        holder.DateCreated.setText(strTimeCreated);
            /*
            holder.Meter.setText(meter);
            holder.Classify.setText(classification);
            holder.Identify.setText(identification);
             */
        holder.done.setVisibility(View.GONE);
        holder.doneAll.setVisibility(View.GONE);
        holder.pending.setVisibility(View.VISIBLE);
        holder.DateCreated.setGravity(Gravity.LEFT);
        holder.koma.setVisibility(View.GONE);
        holder.kurung.setVisibility(View.GONE);
        holder.kurung2.setVisibility(View.GONE);
        holder.Meter.setVisibility(View.GONE);
        holder.Classify.setVisibility(View.GONE);
        holder.Identify.setVisibility(View.GONE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 90, 16, 0);
        holder.DateCreated.setLayoutParams(params);
    }
    private void showStatus3Visible(GhistoryMeter history, ViewHolder holder, String strTimeCreated) {
        AppDatabase db;
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();
        String mImageFileLocation;

        String ph = history.getPhone();
        mImageFileLocation = db.gHistorySpinnerDao().getImageHistory(ph);
//        File imgFile = new File(mImageFileLocation);
        if (mImageFileLocation==null) {
            MToastr.showToast(context.getApplicationContext(), "no image ");
        } else{
            Log.d("image makeover", "" + mImageFileLocation);
        Bitmap bitmap;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(mImageFileLocation,
                bitmapOptions);
//        bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        holder.imgv.setImageBitmap(BitmapFactory.decodeFile(mImageFileLocation));
        }
        double meter = history.getMeter();
        holder.Meter.setVisibility(View.VISIBLE);
        holder.Meter.setText(String.valueOf(meter));
        holder.DateCreated.setText(strTimeCreated);
        holder.Classify.setText(String.valueOf(history.getScoreClassfification()));
        holder.Classify.setVisibility(View.VISIBLE);
        holder.Identify.setText(String.valueOf(history.getScoreIdentification()));
        holder.Identify.setVisibility(View.VISIBLE);
        holder.done.setVisibility(View.GONE);
        holder.pending.setVisibility(View.GONE);
        holder.doneAll.setVisibility(View.VISIBLE);
        holder.koma.setVisibility(View.VISIBLE);
        holder.kurung.setVisibility(View.VISIBLE);
        holder.kurung2.setVisibility(View.VISIBLE);
        holder.DateCreated.setGravity(Gravity.LEFT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 90, 0, 0);
        holder.DateCreated.setLayoutParams(params);
    }


    @Override
    public int getItemCount() {
        //Menghitung data / ukuran dari Array
        return listHistory.size();
    }
}