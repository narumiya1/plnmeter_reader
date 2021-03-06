package com.example.myapplicationpln.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.MHistory;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GHistory;
import com.example.myapplicationpln.roomDb.GhistoryMeter;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<MHistory> listData;
    private ArrayList<GHistory> listDataHistory;
    private ArrayList<GhistoryMeter> listDataHistoryMeter;
    private String imageHistrory;
    private AppDatabase appDatabase;
    private Context context;
    private String fileNameLocal;
    Dialog dialog;
    Button button ;

    public MyAdapter(List<MHistory> listData, Context context, String imageHistrory, ArrayList<GhistoryMeter> gHistories) {
        this.listData = listData;
        this.context = context;
        this.imageHistrory = imageHistrory;
        this.listDataHistoryMeter = gHistories;
        /*
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();

         */
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MHistory ld = listData.get(position);
        MHistory mHistory = listData.get(position);

//        GHistory gHistory = imageHistrory.get(position);
        double m = mHistory.getMeter();
        //dateTime & getMeter
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date timeCreated = listData.get(position).getCreatedAt();
        String strTimeCreated = formatter.format(timeCreated);

        showVisibility(mHistory, holder, strTimeCreated, m);
    }

    private void showVisibility(MHistory mHistory, ViewHolder holder, String strTimeCreated, double m) {

        AppDatabase db;
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();
        List<GHistory> mImageFileLocation;

//        fileNameLocal = db.gHistorySpinnerDao().selectHistoryfromRoomImage();
        fileNameLocal = db.gHistorySpinnerDao().getImageHistoryId(Integer.parseInt(String.valueOf(mHistory.getId())));
//        File imgFile = new File(fileNameLocal);
        Log.d("image makeover", "" + fileNameLocal);
        Bitmap bitmap;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(fileNameLocal,
                bitmapOptions);
//        bitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
        holder.imgv.setImageBitmap(BitmapFactory.decodeFile(fileNameLocal));

        holder.rMeter.setText(String.valueOf(m));
        holder.txtIdentification.setText(String.valueOf(mHistory.getScoreIdentification()));
        holder.txtClassification.setText(String.valueOf(mHistory.getScoreClassification()));
        holder.DateCreated.setText(strTimeCreated);
        holder.done.setVisibility(View.GONE);
        holder.doneAll.setVisibility(View.VISIBLE);
        holder.txtMeter.setVisibility(View.VISIBLE);
        holder.status0.setVisibility(View.GONE);
        holder.status1.setVisibility(View.GONE);
        holder.DateCreated.setGravity(Gravity.LEFT);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30, 80, 0, 0);
        holder.DateCreated.setLayoutParams(params);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                View view=LayoutInflater.from(context).inflate(R.layout.dialof_image,null);
                ImageView dimg=(ImageView)view.findViewById(R.id.imageDetailRoom);

                TextView detailCreate = view.findViewById(R.id.createDateDetail);
                builder.setView(view);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm dd-MM-yyyy ");
                Date timeCreated = mHistory.getCreatedAt();
                String strTimeCreated = formatter.format(timeCreated);
                detailCreate.setText(strTimeCreated);
                detailCreate.setTextColor(Color.BLUE);

                fileNameLocal = db.gHistorySpinnerDao().getImageHistoryId(Integer.parseInt(String.valueOf(mHistory.getId())));
                dimg.setImageBitmap(BitmapFactory.decodeFile(fileNameLocal));

                builder.setNegativeButton("back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }

        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rMeter, txtMeter, txtIdentification, txtClassification, status0, status1, DateCreated;
        ImageView done, doneAll;
        private PhotoView imgv;

        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMeter = (TextView) itemView.findViewById(R.id.rTextMeter);
            rMeter = (TextView) itemView.findViewById(R.id.rMeter);
            txtIdentification = (TextView) itemView.findViewById(R.id.rIdentify);
            txtClassification = (TextView) itemView.findViewById(R.id.rClassify);
            status0 = (TextView) itemView.findViewById(R.id.status0);
            status1 = (TextView) itemView.findViewById(R.id.status1);
            DateCreated = (TextView) itemView.findViewById(R.id.txtDates);
            done = (ImageView) itemView.findViewById(R.id.img_check_done);
            doneAll = (ImageView) itemView.findViewById(R.id.img_check_done_all);
            imgv = itemView.findViewById(R.id.imgvx);
            cardView = itemView.findViewById(R.id.cvMain_history);
        }
    }


}