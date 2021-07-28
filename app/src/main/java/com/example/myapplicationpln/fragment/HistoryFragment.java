package com.example.myapplicationpln.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.model.MHistory;
import com.example.myapplicationpln.model.PointValue;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GHistory;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {

    private View mView;
    RecyclerView mRecyclerview;
    private FirebaseDatabase mDatabase;
    private Query mUserDatabase, mUserDatabase2;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private LinearLayoutManager mManager;
    private static final String TAG = HistoryFragment.class.getSimpleName();
    private Query needsQuery;
    private Query offersQuery;
    SessionPrefference session;
    private Dialog dialog;
    LineChart lineChart;
    DatabaseReference databaseReference2;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    GraphView graphView;
    LineGraphSeries series;
    private List<MHistory> listData;
    private ArrayList<GHistory> listHistLocal;
    private MyAdapter adapter;
    private LineChart mChart;
    private AppDatabase db;
    LineDataSet lineDataSet = new LineDataSet(null, null);
    ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
    LineData lineData;

    public HistoryFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historyi, container, false);
        // Inflate the layout for this fragment
        DatabaseReference referenceHistor = FirebaseDatabase.getInstance().getReference();
        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();
        graphView = view.findViewById(R.id.jjoe);
        series = new LineGraphSeries();
        graphView.addSeries(series);
        listData = new ArrayList<>();
        listHistLocal = new ArrayList<>();
        mChart = view.findViewById(R.id.line_chartFbase);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        GHistory[] gHistory = db.gHistorySpinnerDao().readDataHistory3();
        // disable dismiss by tapping outside of the dialog
        showDataRoom(gHistory);
        dialog = new Dialog(getActivity());

        session = new SessionPrefference(getActivity());
        mRecyclerview = view.findViewById(R.id.recyclerViewHistory);
        mRecyclerview.setHasFixedSize(true);
        mManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(mManager);

        Log.e(TAG, "firebaseoption");

        //add string & get from session
        String userId = session.getUserId();
        Log.d("get userzId Historiy", " : " + userId);

        mDatabase = FirebaseDatabase.getInstance();
//        mUserDatabase = mDatabase.getReference().child("History").child(session.getPhone()).orderByChild("createdAt");
        mUserDatabase = mDatabase.getReference().child("HistoryMeter").child(session.getPhone()).child(String.valueOf("ElectricCity")).child(session.getIdPelanggan()).orderByChild("createdAt");
        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference("data");
        mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                float f = 50, d;
                double y;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        MHistory l = npsnapshot.getValue(MHistory.class);
                        listData.add(l);
                        GHistory[] gHistory = db.gHistorySpinnerDao().readDataHistory3();

                        // showData(listData,l,gHistory);

                        /*
                        ArrayList<Entry> datavals = new ArrayList<Entry>();
                        f = f + 50;
                        y =l.getMeter();
                        float c = (float) y;
                        datavals.add(new Entry(f,c));
                        showData(listData);
                       showChart(datavals,c);
                         */

                    }
                    //20210716
                    //ambil history dari room database untuk user tersebut
                    //masukkan ke list atau array dengan nama listHistoryLocal
                    // = new List<GHistory>();
                    List<GHistory> listHistJoin; //gabungan local dan firebase
                    //iterasi list firebase --> listData
                    int countHist = listData.size();
                    List<GHistory> listHistoryCount = db.gHistorySpinnerDao().selectHistoryfromRoom3();

                    String fileNameLocal = "";
                    for (int i = 0; i < countHist; i++) {
                        listHistJoin = new ArrayList<>();
                        //cari di room database lokasi file
                        long idHist = listData.get(i).getId();
                        double meter = listData.get(i).getMeter();
                        //find di local db
                        int countHistLocal = listHistLocal.size();
                        for (int j = 0; j < listHistoryCount.size(); j++) {
                            GHistory history = listHistoryCount.get(i);
//                            GHistory histLocal = listHistLocal.get(j);
                            int idHistLocal = history.getId();
//                            String mImageFileLocation = db.gHistorySpinnerDao().getImageHistory(String.valueOf(history.getMeter()));
                            if (idHist == idHistLocal) {
                                //amvil id tersebut
                                //ambil lokasi file tersebut
                                fileNameLocal = history.getImagez();
                                Log.d("", " " + fileNameLocal);
//                                showData(listData);


                                break;
                            }
                        }
                    }
                    adapter = new MyAdapter(listData, getActivity(), fileNameLocal, listHistLocal);
                    mRecyclerview.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*
        setListener();
        mUserDatabase2 = mDatabase.getReference().child("History");
        Query queryhistory = referenceHistor.child("History").child("33");
        queryhistory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getChildrenCount()==0){
                        Log.d("DATA < 0 Historyi", "onDataChange: " + dataSnapshot.getValue());
                    }else {
                        Log.d("DATA Historyi > 0 ", "onDataChange: \n" + dataSnapshot.getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Rumus Firebase one to many
//        mUserDatabase = mDatabase.getReference().child("User").orderByChild("address").equalTo("Kilmarnock");
//        firebase.database().ref('Address').orderBy('User').equalTo('id');
//        needsQuery = mUserDatabase.orderByChild("address").equalTo("Kilmarnock");
//        offersQuery = mUserDatabase.orderByChild("type").equalTo("offer");



        FirebaseRecyclerOptions<MHistory> options =
                new FirebaseRecyclerOptions.Builder<MHistory>().
                        setQuery(mUserDatabase, MHistory.class).
                        build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MHistory, UserViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull MHistory model) {
                Log.d("stringset"," "+model.getMeter());
                holder.setFname(model.getMeter());
                holder.setLname(model.getScoreClassification());
                holder.setAname(model.getScoreIdentification());
                Date timeCreated = model.getCreatedAt() ;
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String strTimeCreated = formatter.format(timeCreated);
                String dateVal = String.valueOf(model.getCreatedAt());
                holder.setDate(strTimeCreated);
                int status = 1;
                holder.setStatusName(1);
                Log.d("get getMeter ", " : " + model.getMeter());
                dialog = new Dialog(getActivity());
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.setTitle("Item");
                        dialog.setContentView(R.layout.dialog_chart_design);
                        TextView oke = dialog.findViewById(R.id.oke);
                        PieChart pieChart;
                        pieChart = dialog.findViewById(R.id.chart1);
                        pieChart.setUsePercentValues(true);
                        ArrayList<PieEntry> yvalues = new ArrayList<PieEntry>();
                        float f = Float.parseFloat(String.valueOf(model.getMeter()));
                        yvalues.add(new PieEntry((float) f, model.getMeter()));
                        PieDataSet dataSet = new PieDataSet(yvalues, getString(R.string.gcm_defaultSenderId));
                        PieData data = new PieData(dataSet);

                        data.setValueFormatter(new PercentFormatter());
                        pieChart.setData(data);
                        Description description = new Description();
                        description.setText(getString(R.string.project_id));
                        pieChart.setDescription(description);
                        pieChart.setDrawHoleEnabled(true);
                        pieChart.setTransparentCircleRadius(58f);
                        pieChart.setHoleRadius(58f);
                        pieChart.setEntryLabelColor(Color.BLACK);
                        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                        data.setValueTextSize(13f);
                        data.setValueTextColor(Color.BLACK);
                        Button button = dialog.findViewById(R.id.cancel);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();

                            }
                        });

                        dialog.show();
                    }
                });


//                    holder.cardView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                        Context context = view.getContext();
//                        Intent intent = new Intent(context, EventDetailActivity.class);
//                        intent.putExtra("userAddress", model.getAlamat_pelanggan());
//                        intent.putExtra("userDetailId", model.getId_pelanggan());
//                        intent.putExtra("userAddressId", model.getUser_address_id());
//
//                        Log.d("Body model model", "model: " + model.getAlamat_pelanggan());
//                        context.startActivity(intent);
//
//
//                    }
//                });


            }


            @Override
            public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_history, parent, false);
                Log.e(TAG, "oncreate is called");

                return new UserViewHolder(view);
            }
        };
        mRecyclerview.setAdapter(firebaseRecyclerAdapter);


         */

        return view;
    }

    /*
    @Override
    public void onStart() {
        super.onStart();
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataPoint[] dp = new DataPoint[(int) snapshot.getChildrenCount() ] ;
                int idx = 0 ;

                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    MHistory mHistory = snapshot1.getValue(MHistory.class);

                    SimpleDateFormat formatter = new SimpleDateFormat("MM");
                    Date timeCreated = mHistory.getCreatedAt();
                    double strTimeCreated = Double.parseDouble(formatter.format(timeCreated));

                    dp[idx] = new DataPoint(mHistory.getId(),mHistory.getMeter());
                    idx++;
                }

                series.resetData(dp);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

     */
    private void showChart(ArrayList<Entry> datavals) {
        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines();

        leftAxis.setAxisMaximum(300);
        leftAxis.setAxisMinimum(90);
//        leftAxis.enableGridDashedLine(c, c, 0f);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setValueFormatter(new MyValueFormatter());
        lineDataSet.setValues(datavals);
        lineDataSet.setLabel("Dataset 1");
//        lineDataSets.clear();
//        lineDataSets.add(lineDataSet);
//        lineData = new LineData(lineDataSets);
//        mChart.clear();
//        mChart.setData(lineData);
//        mChart.invalidate();

        if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(datavals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            lineDataSets.clear();
            lineDataSets.add(lineDataSet);
            lineData = new LineData(lineDataSets);

            lineDataSet.setDrawIcons(false);
            lineDataSet.setColor(Color.GREEN);
            lineDataSet.setCircleColor(Color.GREEN);
            lineDataSet.setLineWidth(1f);
            lineDataSet.setCircleRadius(3f);
            lineDataSet.setDrawCircleHole(false);
            lineDataSet.setValueTextSize(9f);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setFormLineWidth(1f);
            lineDataSet.setFormSize(15.f);
            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
                lineDataSet.setFillDrawable(drawable);
            } else {
                lineDataSet.setFillColor(Color.GREEN);
            }

        }
        mChart.clear();
        mChart.setData(lineData);
        mChart.invalidate();
    }

    private void showDataRoom(GHistory[] gHistory) {

        double x, y;
        for (int i = 0; i < gHistory.length; i++) {

            Date itemDate = gHistory[i].getDate_time();
            String myDateStr = new SimpleDateFormat("dd-MM-yyyy").format(itemDate);
            System.out.println(myDateStr);

            y = gHistory[i].getMeter();
            x = gHistory[i].getScoreClassfification();
            float c = (float) y;

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines();

            leftAxis.setAxisMaximum(300);
            leftAxis.setAxisMinimum(90);
            leftAxis.enableGridDashedLine(c, c, 0f);
            leftAxis.setDrawZeroLine(false);
            leftAxis.setDrawLimitLinesBehindData(false);
            leftAxis.setValueFormatter(new MyValueFormatter());
        }
        mChart.getAxisRight().setEnabled(false);
        setDataRoom(gHistory);
    }


    private void setDataRoom(GHistory[] listHistory) {

        GHistory gHistory = new GHistory();
        ArrayList<Entry> values = new ArrayList<>();
        int historyCount = listHistory.length;
        LineDataSet set1;
        double x;
        double y;
        float f = (float) 50;

        for (int i = 0; i < historyCount; i++) {
            y = listHistory[i].getMeter();
            x = listHistory[i].getScoreClassfification();
            float c = (float) y;
            f = f + 50;
            values.add(new Entry(f, c));
            if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                set1 = new LineDataSet(values, "History Data");
                set1.setDrawIcons(false);
                set1.setColor(Color.GREEN);
                set1.setCircleColor(Color.GREEN);
                set1.setLineWidth(1f);
                set1.setCircleRadius(3f);
                set1.setDrawCircleHole(false);
                set1.setValueTextSize(9f);
                set1.setDrawFilled(true);
                set1.setFormLineWidth(1f);
                set1.setFormSize(15.f);
                if (Utils.getSDKInt() >= 18) {
                    Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
                    set1.setFillDrawable(drawable);
                } else {
                    set1.setFillColor(Color.GREEN);
                }

            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }

    }

    private void showData(List<MHistory> listData, MHistory h, GHistory[] gHistory) {
        double x, y, z, a;
        for (int i = 0; i < listData.size(); i++) {

            Date itemDate = listData.get(i).getCreatedAt();
//            listData.get(i).getCreatedAt();
            String myDateStr = new SimpleDateFormat("dd-MM-yyyy").format(itemDate);
            System.out.println(myDateStr);
            y = listData.get(i).getMeter();
            x = listData.get(i).getMeter();
            z = listData.get(i).getId();

//            x = listData.get(i).getScoreClassification();
            float d = (float) x;

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines();

            leftAxis.setAxisMaximum(300);
            leftAxis.setAxisMinimum(90);
            leftAxis.enableGridDashedLine(d, d, 0f);
            leftAxis.setDrawZeroLine(false);
            leftAxis.setDrawLimitLinesBehindData(false);
            leftAxis.setValueFormatter(new MyValueFormatter());


        }
        mChart.getAxisRight().setEnabled(false);
        setData(listData);
    }

    private void setData(List<MHistory> listData) {
        ArrayList<Entry> values = new ArrayList<>();
        int historyCount = listData.size();
        LineDataSet set1;
        double x;
        double y, z, a;
        float f = (float) 50;

        for (int i = 0; i < historyCount; i++) {
            y = listData.get(i).getMeter();
            x = listData.get(i).getMeter();
            float d = (float) x;
            z = listData.get(i).getId();
            f = f + 50;
            values.add(new Entry(f, d));
            if (mChart.getData() != null && mChart.getData().getDataSetCount() > 0) {
                set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                mChart.getData().notifyDataChanged();
                mChart.notifyDataSetChanged();
            } else {
                set1 = new LineDataSet(values, "History Data");
                set1.setDrawIcons(false);
                set1.setColor(Color.GREEN);
                set1.setCircleColor(Color.GREEN);
                set1.setLineWidth(1f);
                set1.setCircleRadius(3f);
                set1.setDrawCircleHole(false);
                set1.setValueTextSize(9f);
                set1.setDrawFilled(true);
                set1.setFormLineWidth(1f);
                set1.setFormSize(15.f);
                if (Utils.getSDKInt() >= 18) {
                    Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.fade_blue);
                    set1.setFillDrawable(drawable);
                } else {
                    set1.setFillColor(Color.GREEN);
                }

            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);


        }
    }

    private void setListener() {

    }

    /*
    @Override
    public void onStart() {
        super.onStart();
        firebaseRecyclerAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseRecyclerAdapter.stopListening();
    }

    //ViewHolder class
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        View mView;
        CardView cardView;

        public UserViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            cardView = itemView.findViewById(R.id.cvMain_addresss);


//                mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, EventDetailActivity.class);
//
//                    context.startActivity(intent);
//                }
//              });


        }

        public void setStatusName(int status){
            TextView status0 = (TextView) mView.findViewById(R.id.status0);
            TextView status1 = (TextView) mView.findViewById(R.id.status1);
            ImageView imageView = mView.findViewById(R.id.img_check_done);
            ImageView imageView2 = mView.findViewById(R.id.img_check_done_all);
            if (status<=0){
                status0.setVisibility(View.GONE);
                status1.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView2.setVisibility(View.GONE);

            }else{
                status0.setVisibility(View.GONE);
                status1.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                imageView2.setVisibility(View.VISIBLE);
            }
        }
        public void setFname(double name) {
            TextView FName = (TextView) mView.findViewById(R.id.rMeter);
            String s=Double.toString(name);
            FName.setText(s);
        }


        public void setLname(double name) {
            TextView LName = (TextView) mView.findViewById(R.id.rIdentify);
            String s=Double.toString(name);
            Log.d("stringset"," "+s);
            LName.setText(s);
        }

        public void setAname(double name) {
            TextView AName = (TextView) mView.findViewById(R.id.rClassify);
            String s=Double.toString(name);
            Log.d("stringset"," "+s);
            AName.setText(s);
        }

        public void setDate(String d) {
            TextView DName = (TextView) mView.findViewById(R.id.txtDates);
            DName.setText(d);
        }

        public void setOrderSpinner(String status) {

        }

    }

     */


}