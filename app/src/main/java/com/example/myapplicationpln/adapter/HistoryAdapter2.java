package com.example.myapplicationpln.adapter;

import android.content.Context;
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
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GHistory;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class HistoryAdapter2 extends RecyclerView.Adapter<HistoryAdapter2.ViewHolder> {

    //Deklarasi Variable
    private ArrayList<GHistory> GHistory;
    private AppDatabase appDatabase;
    private Context context;

    public HistoryAdapter2(ArrayList<GHistory> GHistory, Context context) {

        //Inisialisasi data yang akan digunakan
        this.GHistory = GHistory;
        this.context = context;
        appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();
    }

    class MenuItemHolder extends ViewHolder{
        private TextView Classifiy;
        private TextView Meter, DateCreated, Classify, Identify;
        private PhotoView imgv;

        public MenuItemHolder(@NonNull View itemView) {
            super(itemView);
            Meter = itemView.findViewById(R.id.rMeter2);
            DateCreated = itemView.findViewById(R.id.txtDate2);
            Classify = itemView.findViewById(R.id.rClassify2);
            Identify = itemView.findViewById(R.id.rIdentify2);

        }
    }
    class MenuHeaderHolder extends ViewHolder{
        private TextView Header;
        public MenuHeaderHolder(@NonNull View itemView) {
            super(itemView);
            Header = itemView.findViewById(R.id.tvHeaderItem);
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder {

        //Deklarasi View yang akan digunakan
        private TextView Meter, DateCreated, Classify, Identify, txtrIdentifiy2;
        private PhotoView imgv;
        private ImageView done,doneAll,pending;
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
            done =itemView.findViewById(R.id.chekclist_done);
            pending=itemView.findViewById(R.id.pending);
            doneAll =itemView.findViewById(R.id.chekclist_done_all);
            txtrIdentifiy2=itemView.findViewById(R.id.txtrIdentifiy2);
            cv_clicked=itemView.findViewById(R.id.cvMain_historiy);
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
        /*
        View v;
        if (viewType == ITEM_HEADER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            MenuHeaderHolder holder = new MenuHeaderHolder(v);
            Log.d("holder", "holder secildi VIDEO");
            return holder;

        } else if (viewType == ITEM_MENU) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history2, parent, false);
            MenuItemHolder holder = new MenuItemHolder(v);
            Log.d("holder", "holder secildi PHOTO");
            return holder;
        }
        return null;
        */
        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row_history2, parent, false);
        return new HistoryAdapter2.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String getDate = GHistory.get(position).getCreated_at();
        String getMeter = GHistory.get(position).getMeter();
        String getClasfy = GHistory.get(position).getScore_classfy();
        String getIdtfy = GHistory.get(position).getScore_identfy();
        String getImg = GHistory.get(position).getImagez();
        Log.d("historiyi", " " + getImg);
        Log.d("historiyi", " " + getClasfy);
        Log.d("historiyi", " " + getMeter);
        /*
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
         */

        //Menampilkan data berdasarkan posisi Item dari RecyclerView
        if (GHistory.get(position).getStatus()==1){
            holder.DateCreated.setText(getDate);
            holder.Meter.setText(GHistory.get(position).getMeter());
            holder.Classify.setText(GHistory.get(position).getScore_classfy());
            holder.Identify.setText(GHistory.get(position).getScore_identfy());
            holder.done.setVisibility(View.VISIBLE);
            holder.doneAll.setVisibility(View.GONE);
            holder.pending.setVisibility(View.GONE);
            holder.DateCreated.setGravity(Gravity.LEFT);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
            params.setMargins(16,90,0,0);
            holder.DateCreated.setLayoutParams(params);
        }else if (GHistory.get(position).getStatus()==2){
            holder.DateCreated.setText(getDate);
            holder.Meter.setText(GHistory.get(position).getMeter());
            holder.Classify.setText(GHistory.get(position).getScore_classfy());
            holder.Identify.setText(GHistory.get(position).getScore_identfy());
            holder.done.setVisibility(View.GONE);
            holder.doneAll.setVisibility(View.GONE);
            holder.pending.setVisibility(View.VISIBLE);
            holder.DateCreated.setGravity(Gravity.LEFT);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
            params.setMargins(16,90,0,0);
            holder.DateCreated.setLayoutParams(params);

        }
        else {
            holder.Meter.setText(String.valueOf(getMeter));
            holder.DateCreated.setText(getDate);
            holder.Classify.setText(getClasfy);
            holder.Identify.setText(getIdtfy);
            holder.done.setVisibility(View.GONE);
            holder.pending.setVisibility(View.GONE);
            holder.doneAll.setVisibility(View.VISIBLE);
            holder.txtrIdentifiy2.setVisibility(View.VISIBLE);
            holder.DateCreated.setGravity(Gravity.RIGHT);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT );
            params.setMargins(0,90,16,0);
            holder.DateCreated.setLayoutParams(params);
        }

    }


    @Override
    public int getItemCount() {
        //Menghitung data / ukuran dari Array
        return GHistory.size();
    }
}