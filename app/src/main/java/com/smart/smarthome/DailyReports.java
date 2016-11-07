package com.smart.smarthome;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
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

public class DailyReports extends ActionBarActivity {



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

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            FirebaseUser user = firebaseAuth.getCurrentUser();
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("report");
            Query queryRef = mDatabase.orderByChild("Time").startAt(SummaryreportActivity.begindate).endAt(SummaryreportActivity.untildate);
            queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int numSubcolumns = 1;
                    int numColumns = 7;
                    float temp[] ={0,0,0,0,0,0,0,0,0,0,0,0,0,0} ;

                    double price1=0;
                    double price2=0;
                    double price3=0;
                    double price4=0;
                    double price5=0;
                    double price6=0;
                    double price7=0;



                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                        Report report = postSnapshot.getValue(Report.class);

                        if(report.getMonth().equals(SummaryreportActivity.month)){

                            String date = report.getTime().substring(6);
                            int date2 = Integer.parseInt(date);

                            if(WeeklyReports.week == 1){
                                if(date2 == 1){
                                    temp[0] += report.getTotalBuyPrice();
                                    price1  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 2){
                                    temp[2] += report.getTotalBuyPrice();
                                    price2  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 3){
                                    temp[4] += report.getTotalBuyPrice();
                                    price3  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 4){
                                    temp[6] += report.getTotalBuyPrice();
                                    price4  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 5){
                                    temp[8] += report.getTotalBuyPrice();
                                    price5  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 6){
                                    temp[10] += report.getTotalBuyPrice();
                                    price6  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 7){
                                    temp[12] += report.getTotalBuyPrice();
                                    price7  += Double.parseDouble(report.getTotalPrice());
                                }
                            }else if(WeeklyReports.week == 2){
                                if(date2 == 8){
                                    temp[0] += report.getTotalBuyPrice();
                                    price1  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 9){
                                    temp[2] += report.getTotalBuyPrice();
                                    price2  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 10){
                                    temp[4] += report.getTotalBuyPrice();
                                    price3  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 11){
                                    temp[6] += report.getTotalBuyPrice();
                                    price4  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 12){
                                    temp[8] += report.getTotalBuyPrice();
                                    price5  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 13){
                                    temp[10] += report.getTotalBuyPrice();
                                    price6  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 14){
                                    temp[12] += report.getTotalBuyPrice();
                                    price7  += Double.parseDouble(report.getTotalPrice());
                                }
                            }else if(WeeklyReports.week == 3){
                                if(date2 == 15){
                                    temp[0] += report.getTotalBuyPrice();
                                    price1  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 16){
                                    temp[2] += report.getTotalBuyPrice();
                                    price2  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 17){
                                    temp[4] += report.getTotalBuyPrice();
                                    price3  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 18){
                                    temp[6] += report.getTotalBuyPrice();
                                    price4  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 19){
                                    temp[8] += report.getTotalBuyPrice();
                                    price5  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 20){
                                    temp[10] += report.getTotalBuyPrice();
                                    price6  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 21){
                                    temp[12] += report.getTotalBuyPrice();
                                    price7  += Double.parseDouble(report.getTotalPrice());
                                }
                            }else if(WeeklyReports.week == 4){
                                if(date2 == 22){
                                    temp[0] += report.getTotalBuyPrice();
                                    price1  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 23){
                                    temp[2] += report.getTotalBuyPrice();
                                    price2  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 24){
                                    temp[4] += report.getTotalBuyPrice();
                                    price3  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 25){
                                    temp[6] += report.getTotalBuyPrice();
                                    price4  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 26){
                                    temp[8] += report.getTotalBuyPrice();
                                    price5  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 27){
                                    temp[10] += report.getTotalBuyPrice();
                                    price6  += Double.parseDouble(report.getTotalPrice());
                                }else if (date2 == 28){
                                    temp[12] += report.getTotalBuyPrice();
                                    price7  += Double.parseDouble(report.getTotalPrice());
                                }
                            }else if(WeeklyReports.week == 5) {
                                if (date2 == 29) {
                                    temp[0] += report.getTotalBuyPrice();
                                    price1 += Double.parseDouble(report.getTotalPrice());
                                } else if (date2 == 30) {
                                    temp[2] += report.getTotalBuyPrice();
                                    price2 += Double.parseDouble(report.getTotalPrice());
                                } else if (date2 == 31) {
                                    temp[4] += report.getTotalBuyPrice();
                                    price3 += Double.parseDouble(report.getTotalPrice());
                                }
                            }


                        }

                    }

                    int priceF1 =(int) price1;
                    int priceF2 =(int) price2;
                    int priceF3 =(int) price3;
                    int priceF4 =(int) price4;
                    int priceF5 =(int) price5;
                    int priceF6 =(int) price6;
                    int priceF7 =(int) price7;

                    temp[1] += priceF1;
                    temp[3] += priceF2;
                    temp[5] += priceF3;
                    temp[7] += priceF4;
                    temp[9] += priceF5;
                    temp[11] += priceF6;
                    temp[13] += priceF7;

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

                        if(WeeklyReports.week == 1) {
                            vals.add(new AxisValue(0, "1".toCharArray()));
                            vals.add(new AxisValue(1, "2".toCharArray()));
                            vals.add(new AxisValue(2, "3".toCharArray()));
                            vals.add(new AxisValue(3, "4".toCharArray()));
                            vals.add(new AxisValue(4, "5".toCharArray()));
                            vals.add(new AxisValue(5, "6".toCharArray()));
                            vals.add(new AxisValue(6, "7".toCharArray()));

                        }else if(WeeklyReports.week == 2) {
                            vals.add(new AxisValue(0, "8".toCharArray()));
                            vals.add(new AxisValue(1, "9".toCharArray()));
                            vals.add(new AxisValue(2, "10".toCharArray()));
                            vals.add(new AxisValue(3, "11".toCharArray()));
                            vals.add(new AxisValue(4, "12".toCharArray()));
                            vals.add(new AxisValue(5, "13".toCharArray()));
                            vals.add(new AxisValue(6, "14".toCharArray()));

                        }else if(WeeklyReports.week == 3) {
                            vals.add(new AxisValue(0, "15".toCharArray()));
                            vals.add(new AxisValue(1, "16".toCharArray()));
                            vals.add(new AxisValue(2, "17".toCharArray()));
                            vals.add(new AxisValue(3, "18".toCharArray()));
                            vals.add(new AxisValue(4, "19".toCharArray()));
                            vals.add(new AxisValue(5, "20".toCharArray()));
                            vals.add(new AxisValue(6, "21".toCharArray()));

                        }else if(WeeklyReports.week == 4) {
                            vals.add(new AxisValue(0, "22".toCharArray()));
                            vals.add(new AxisValue(1, "23".toCharArray()));
                            vals.add(new AxisValue(2, "24".toCharArray()));
                            vals.add(new AxisValue(3, "25".toCharArray()));
                            vals.add(new AxisValue(4, "26".toCharArray()));
                            vals.add(new AxisValue(5, "27".toCharArray()));
                            vals.add(new AxisValue(6, "28".toCharArray()));

                        }else if(WeeklyReports.week == 5) {
                            vals.add(new AxisValue(0, "29".toCharArray()));
                            vals.add(new AxisValue(1, "30".toCharArray()));
                            vals.add(new AxisValue(2, "31".toCharArray()));

                        }

                        Axis axisX = new Axis(vals);
                        Axis axisY = new Axis().setHasLines(true);



                        String month2 = "";


                        if (hasAxesNames) {
                            if(SummaryreportActivity.month.equals("1")){
                                month2 = "January";
                            }else if(SummaryreportActivity.month.equals("2")){
                                month2 = "February";
                            }else if(SummaryreportActivity.month.equals("3")){
                                month2 = "March";
                            }else if(SummaryreportActivity.month.equals("4")){
                                month2 = "April";
                            }else if(SummaryreportActivity.month.equals("5")){
                                month2 = "May";
                            }else if(SummaryreportActivity.month.equals("6")){
                                month2 = "June";
                            }else if(SummaryreportActivity.month.equals("7")){
                                month2 = "July";
                            }else if(SummaryreportActivity.month.equals("8")){
                                month2 = "August";
                            }else if(SummaryreportActivity.month.equals("9")){
                                month2 = "September";
                            }else if(SummaryreportActivity.month.equals("10")){
                                month2 = "October";
                            }else if(SummaryreportActivity.month.equals("11")){
                                month2 = "November";
                            }else if(SummaryreportActivity.month.equals("12")){
                                month2 = "December";
                            }

                            System.out.println(SummaryreportActivity.month);
                            System.out.println(month2);

                            axisX.setName(month2+ " : week :" + WeeklyReports.week);
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
            int numSubcolumns = 1;
            int numColumns = 7;
            // Column can have many subcolumns, here by default I use 1 subcolumn in each of 8 columns.
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            int temp[]={50,60,70,80,55,76,46,50,60,70,80,55,76,46}; //set value HERE!!
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
                vals.add(new AxisValue(0,"1".toCharArray()));
                vals.add(new AxisValue(1,"2".toCharArray()));
                vals.add(new AxisValue(2,"3".toCharArray()));
                vals.add(new AxisValue(3,"4".toCharArray()));
                vals.add(new AxisValue(4,"5".toCharArray()));
                vals.add(new AxisValue(5,"6".toCharArray()));
                vals.add(new AxisValue(6,"7".toCharArray()));
                Axis axisX = new Axis(vals);
                Axis axisY = new Axis().setHasLines(true);
                if (hasAxesNames) {
                    axisX.setName("Day");
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






        private int getSign() {
            int[] sign = new int[]{-1, 1};
            return sign[Math.round((float) Math.random())];
        }

        private void generateData() {
            switch (dataType) {
                case DEFAULT_DATA:
                    generateDefaultData();
                    break;

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
                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }

    }
}
