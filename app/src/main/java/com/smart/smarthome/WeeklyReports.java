package com.smart.smarthome;



import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.ColumnChartView;


public class WeeklyReports  extends ActionBarActivity {

    public static int week = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_column_chart);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    /**
     * A fragment containing a column chart.
     */
    public static class PlaceholderFragment extends Fragment {

        private static final int DEFAULT_DATA = 0;
        private static final int SUBCOLUMNS_DATA = 1;
        private static final int STACKED_DATA = 2;
        private static final int NEGATIVE_SUBCOLUMNS_DATA = 3;
        private static final int NEGATIVE_STACKED_DATA = 4;

        private ColumnChartView chart;
        private ColumnChartData data;
        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private boolean hasLabels = false;
        private boolean hasLabelForSelected = false;
        private int dataType = DEFAULT_DATA;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_column_chart, container, false);

            chart = (ColumnChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            generateData();

            return rootView;
        }

        // MENU
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.column_chart, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_reset) {
                reset();
                generateData();
                return true;
            }
            if (id == R.id.action_subcolumns) {
                dataType = SUBCOLUMNS_DATA;
                generateData();
                return true;
            }
            if (id == R.id.action_stacked) {
                dataType = STACKED_DATA;
                generateData();
                return true;
            }
            if (id == R.id.action_negative_subcolumns) {
                dataType = NEGATIVE_SUBCOLUMNS_DATA;
                generateData();
                return true;
            }
            if (id == R.id.action_negative_stacked) {
                dataType = NEGATIVE_STACKED_DATA;
                generateData();
                return true;
            }
            if (id == R.id.action_toggle_labels) {
                toggleLabels();
                return true;
            }
            if (id == R.id.action_toggle_axes) {
                toggleAxes();
                return true;
            }
            if (id == R.id.action_toggle_axes_names) {
                toggleAxesNames();
                return true;
            }
            if (id == R.id.action_animate) {
                prepareDataAnimation();
                chart.startDataAnimation();
                return true;
            }
            if (id == R.id.action_toggle_selection_mode) {
                toggleLabelForSelected();

                Toast.makeText(getActivity(),
                        "Selection mode set to " + chart.isValueSelectionEnabled() + " select any point.",
                        Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.action_toggle_touch_zoom) {
                chart.setZoomEnabled(!chart.isZoomEnabled());
                Toast.makeText(getActivity(), "IsZoomEnabled " + chart.isZoomEnabled(), Toast.LENGTH_SHORT).show();
                return true;
            }
            if (id == R.id.action_zoom_both) {
                chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
                return true;
            }
            if (id == R.id.action_zoom_horizontal) {
                chart.setZoomType(ZoomType.HORIZONTAL);
                return true;
            }
            if (id == R.id.action_zoom_vertical) {
                chart.setZoomType(ZoomType.VERTICAL);
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        private void reset() {
            hasAxes = true;
            hasAxesNames = true;
            hasLabels = false;
            hasLabelForSelected = false;
            dataType = DEFAULT_DATA;
            chart.setValueSelectionEnabled(hasLabelForSelected);

        }

        private void generateDefaultData() {
            // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report");
            Query queryRef = mDatabase.orderByChild("Time").startAt(SummaryreportActivity.begindate).endAt(SummaryreportActivity.untildate);
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int numSubcolumns = 1;
                    int numColumns = 5;
                    float temp[] ={0,0,0,0,0,0,0,0,0,0} ;

                    double price1=0;
                    double price2=0;
                    double price3=0;
                    double price4=0;
                    double price5=0;


                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Report report = postSnapshot.getValue(Report.class);

                        if(report.getMonth().equals(SummaryreportActivity.month)){
                            String date = report.getTime().substring(6);
                            int date2 = Integer.parseInt(date);

                            if(date2 <= 7){
                                temp[0] += report.getTotalBuyPrice();
                                price1  += Double.parseDouble(report.getTotalPrice());
                               // temp[1] += Integer.parseInt(report.getTotalPrice());
                            }else if (date2 > 7 && date2 <= 14){
                                temp[2] += report.getTotalBuyPrice();
                                price2  += Double.parseDouble(report.getTotalPrice());

                                // temp[3] += Integer.parseInt(report.getTotalPrice());
                            }else if (date2 > 14 && date2 <= 21){
                                temp[4] += report.getTotalBuyPrice();
                                price3  += Double.parseDouble(report.getTotalPrice());

                                // temp[5] += Integer.parseInt(report.getTotalPrice());
                            }else if (date2 > 21 && date2 <= 28){
                                temp[6] += report.getTotalBuyPrice();
                                price4  += Double.parseDouble(report.getTotalPrice());

                                // temp[7] += Integer.parseInt(report.getTotalPrice());
                            }else{
                                temp[8] += report.getTotalBuyPrice();
                                price5  += Double.parseDouble(report.getTotalPrice());

                                // temp[9] += Integer.parseInt(report.getTotalPrice());
                            }
                        }

                    }

                    int priceF1 =(int) price1;
                    int priceF2 =(int) price2;
                    int priceF3 =(int) price3;
                    int priceF4 =(int) price4;
                    int priceF5 =(int) price5;

                    temp[1] += priceF1;
                    temp[3] += priceF2;
                    temp[5] += priceF3;
                    temp[7] += priceF4;
                    temp[9] += priceF5;







                    List<Column> columns = new ArrayList<Column>();
                    List<SubcolumnValue> values;
                    int c=0,c1=1;
                    for (int i = 0; i < numColumns; ++i) {

                        values = new ArrayList<SubcolumnValue>();
                        for (int j = 0; j < numSubcolumns; ++j) {
                            values.add(new SubcolumnValue(temp[c],  Color.parseColor("#EF5350")));
                            c=c+2;
                            values.add(new SubcolumnValue(temp[c1],  Color.parseColor("#42A5F5")));
                            c1=c1+2;
                        }




                        Column column = new Column(values);
                        column.setHasLabels(hasLabels);
                        column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                        columns.add(column);
                    }

                    data = new ColumnChartData(columns);

                    if (hasAxes) {
                        ArrayList<AxisValue> vals = new ArrayList<AxisValue>();
                        vals.add(new AxisValue(0,"Week1".toCharArray()));
                        vals.add(new AxisValue(1,"Week2".toCharArray()));
                        vals.add(new AxisValue(2,"Week3".toCharArray()));
                        vals.add(new AxisValue(3,"Week4".toCharArray()));
                        vals.add(new AxisValue(4,"Week5".toCharArray()));

                        Axis axisX = new Axis(vals);
                        Axis axisY = new Axis().setHasLines(true);
                        if (hasAxesNames) {

                            String month = "";
                            if(SummaryreportActivity.month.equals(1)){
                                month = "January";
                            }else if(SummaryreportActivity.month.equals(2)){
                                month = "February";
                            }else if(SummaryreportActivity.month.equals(3)){
                                month = "March";
                            }else if(SummaryreportActivity.month.equals(4)){
                                month = "April";
                            }else if(SummaryreportActivity.month.equals(5)){
                                month = "May";
                            }else if(SummaryreportActivity.month.equals(6)){
                                month = "June";
                            }else if(SummaryreportActivity.month.equals(7)){
                                month = "July";
                            }else if(SummaryreportActivity.month.equals(8)){
                                month = "August";
                            }else if(SummaryreportActivity.month.equals(9)){
                                month = "September";
                            }else if(SummaryreportActivity.month.equals(10)){
                                month = "October";
                            }else if(SummaryreportActivity.month.equals(11)){
                                month = "November";
                            }else if(SummaryreportActivity.month.equals(12)){
                                month = "December";
                            }

                            axisX.setName(month);
                            axisY.setName("Baht");

                        }

                        data.setAxisXBottom(axisX);
                        data.setAxisYLeft(axisY);
                    } else {
                        data.setAxisXBottom(null);
                        data.setAxisYLeft(null);
                    }

                    chart.setColumnChartData(data);


                    }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

/*
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            int temp[]={50,60,70,80,87,90,48,89,11,20}; //set value HERE!!
            int c=0,c1=1;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue(temp[c],  Color.parseColor("#EF5350")));
                    c=c+2;
                    values.add(new SubcolumnValue(temp[c1],  Color.parseColor("#42A5F5")));
                    c1=c1+2;
                }




                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }



            data = new ColumnChartData(columns);

            if (hasAxes) {
                ArrayList<AxisValue> vals = new ArrayList<AxisValue>();
                vals.add(new AxisValue(0,"Week1".toCharArray()));
                vals.add(new AxisValue(1,"Week2".toCharArray()));
                vals.add(new AxisValue(2,"Week3".toCharArray()));
                vals.add(new AxisValue(3,"Week4".toCharArray()));
                vals.add(new AxisValue(4,"Week5".toCharArray()));

                Axis axisX = new Axis(vals);
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Week");
                    axisY.setName("Baht");

                }

                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setColumnChartData(data);

*/
        }

        private void generateSubcolumnsData() {
            int numSubcolumns = 4;
            int numColumns = 4;
            // Column can have many subcolumns, here I use 4 subcolumn in each of 8 columns.
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
                }

                Column column = new Column(values);
                column.setHasLabels(hasLabels);
                column.setHasLabelsOnlyForSelected(hasLabelForSelected);
                columns.add(column);
            }

            data = new ColumnChartData(columns);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Axis X");
                    axisY.setName("Axis Y");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            chart.setColumnChartData(data);

        }



        private int getSign() {
            int[] sign = new int[]{-1, 1};
            return sign[Math.round((float) Math.random())];
        }

        private void generateData() {
            switch (dataType) {
                case DEFAULT_DATA:
                    generateDefaultData();
                    break;
               case SUBCOLUMNS_DATA:
                    generateSubcolumnsData();
                    break;
               /* case STACKED_DATA:
                    generateStackedData();
                    break;
                case NEGATIVE_SUBCOLUMNS_DATA:
                    generateNegativeSubcolumnsData();
                    break;
                case NEGATIVE_STACKED_DATA:
                    generateNegativeStackedData();
                    break;*/
                default:
                    generateDefaultData();
                    break;
            }
        }

        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);
            }

            generateData();
        }

        private void toggleLabelForSelected() {
            hasLabelForSelected = !hasLabelForSelected;
            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelForSelected) {
                hasLabels = false;
            }

            generateData();
        }

        private void toggleAxes() {
            hasAxes = !hasAxes;

            generateData();
        }

        private void toggleAxesNames() {
            hasAxesNames = !hasAxesNames;

            generateData();
        }

        /**
         * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
         * method(don't confuse with View.animate()).
         */
        private void prepareDataAnimation() {
            for (Column column : data.getColumns()) {
                for (SubcolumnValue value : column.getValues()) {
                    value.setTarget((float) Math.random() * 100);
                }
            }
        }

        private class ValueTouchListener implements ColumnChartOnValueSelectListener {

            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {

                if(columnIndex == 0){
                    week = 1;
                    Intent intent = new Intent(getActivity(), DailyReports.class);
                    startActivity(intent);

                }else if(columnIndex == 1){
                    week = 2;
                    Intent intent = new Intent(getActivity(), DailyReports.class);
                    startActivity(intent);

                }else if(columnIndex == 2){
                    week = 3;
                    Intent intent = new Intent(getActivity(), DailyReports.class);
                    startActivity(intent);

                }else if(columnIndex == 3){
                    week = 4;
                    Intent intent = new Intent(getActivity(), DailyReports.class);
                    startActivity(intent);

                }else if(columnIndex == 4){
                    week = 5;
                    Intent intent = new Intent(getActivity(), DailyReports.class);
                    startActivity(intent);

                }



            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }

    }
}
