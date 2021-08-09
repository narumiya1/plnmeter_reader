package com.example.myapplicationpln.fragment;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.myapplicationpln.R;
import com.example.myapplicationpln.adapter.HistoryAdapter2;
import com.example.myapplicationpln.preference.SessionPrefference;
import com.example.myapplicationpln.roomDb.AppDatabase;
import com.example.myapplicationpln.roomDb.GHistory;
import com.example.myapplicationpln.roomDb.GhistoryMeter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class HistoryFragment2 extends Fragment {
    RecyclerView mRecyclerview;
    private RecyclerView.LayoutManager layoutManager;
    private AppDatabase db;
    private ArrayList<GhistoryMeter> listHistory;
    private RecyclerView.Adapter adapter;
    SessionPrefference session;
    private LineChart mChart;
    GraphView graphView;
    LineGraphSeries<DataPoint> series ;
    SimpleDateFormat sdf = new SimpleDateFormat("HH: mm");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historyi2, container, false);
        listHistory = new ArrayList<>();
        db = Room.databaseBuilder(getActivity(), AppDatabase.class, "tbGrainHistory")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .addMigrations(AppDatabase.MIGRATION_1_7)
                .build();
        graphView = view.findViewById(R.id.graph);
        series = new LineGraphSeries<> (getDataPoint());
        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX){
                    return sdf.format(new Date((long)value));
                }else {
                    return super.formatLabel(value, isValueX);

                }
            }
        });
        session = new SessionPrefference(getActivity());
        graphView.addSeries(series);
        mChart = view.findViewById(R.id.line_chart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view);
        mv.setChartView(mChart);
        mChart.setMarker(mv);
        GhistoryMeter[] gHistory = db.gHistorySpinnerDao().readDataHistory(session.getPhone());
        showData(gHistory);

        mRecyclerview = view.findViewById(R.id.recyclerViewHistory2);
        mRecyclerview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerview.setLayoutManager(layoutManager);
        listHistory.addAll(Arrays.asList(db.gHistorySpinnerDao().readDataHistory(session.getPhone())));
        Log.d("historiyi "," list : "+listHistory);
        //Mamasang adapter pada RecyclerView
        adapter= new HistoryAdapter2(listHistory, getActivity());
        mRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        String idUserAddress = session.getUserAddressId();
        Log.d("get idUserAddress2 ", " : " + idUserAddress);
        return view;
    }

    private DataPoint[] getDataPoint() {
        DataPoint[] dataPoints = new DataPoint[]{
                new DataPoint(new Date().getTime(),1),
                new DataPoint(new Date().getTime(),22),
                new DataPoint(new Date().getTime(),3),
                new DataPoint(new Date().getTime(),14),
                new DataPoint(new Date().getTime(),9),
                new DataPoint(new Date().getTime(),16),
                new DataPoint(new Date().getTime(),8),
                new DataPoint(new Date().getTime(),2),
                new DataPoint(new Date().getTime(),11),
                new DataPoint(new Date().getTime(),7)
        };
        return dataPoints;
    }

    private void showData(GhistoryMeter[] gHistory) {

        double x, y;
        for (int i = 0 ; i<gHistory.length; i++){

            Date itemDate = gHistory[i].getDate_time();
            String myDateStr = new SimpleDateFormat("dd-MM-yyyy").format(itemDate);
            System.out.println(myDateStr);

            y = gHistory[i].getMeter();
            x = gHistory[i].getScoreClassfification();
            float c = (float)y;

            YAxis leftAxis = mChart.getAxisLeft();
            leftAxis.removeAllLimitLines();

            leftAxis.setAxisMaximum(300);
            leftAxis.setAxisMinimum(90);
            leftAxis.enableGridDashedLine(c, c, 0f);
            leftAxis.setDrawZeroLine(false);
            leftAxis.setDrawLimitLinesBehindData(false);
            leftAxis.setValueFormatter(new MyValueFormatter());
        }

        /*
        LimitLine llXAxis = new LimitLine(20f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(400f, 400f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        llXAxis.setTextSize(10f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.enableGridDashedLine(400f, 400f, 0f);
        xAxis.setValueFormatter(new MyAxisValueFormatter());
        xAxis.setAxisMaximum(400f);
        xAxis.setAxisMinimum(70f);
        xAxis.setDrawLimitLinesBehindData(true);

         */


        mChart.getAxisRight().setEnabled(false);
        setData(gHistory);
    }

    private void setData(GhistoryMeter[] listHistory) {
        GHistory gHistory = new GHistory();
        ArrayList<Entry> values = new ArrayList<>();
        int historyCount = listHistory.length;
        LineDataSet set1;
        double x;
        double y;
        float f = (float) 50;

        for (int i = 0 ; i<historyCount; i++){
            y = listHistory[i].getMeter();
            x = listHistory[i].getScoreClassfification();
            float c = (float)y;
            f= f+50;
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

    private class MyAxisValueFormatter extends ValueFormatter implements IAxisValueFormatter{

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            axis.setLabelCount(3,true);
            return " Day " +value;
        }
    }
}
