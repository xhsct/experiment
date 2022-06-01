package com.example.keshe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class statistic extends AppCompatActivity {
    public ImageButton data_behind,data_before;
    public Calendar calendar;
    public TextView show_data_before,show_data_behind;
    public String da_before,da_behind;
    public int start,postpone,done;
    private BarChart barChart;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MySqliteOpenHelper helper = new MySqliteOpenHelper(this, MySqliteOpenHelper.SQlite.DB_NAME, null, 1);
        //获得数据库对象helper.get...
        db = helper.getReadableDatabase();
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        da_behind = dateFormat.format(date);
        StringBuffer show_behind = new StringBuffer(da_behind);
        show_behind.insert(8,"日");
        show_behind.insert(6,"月");
        show_behind.insert(4,"年");
        int a = Integer.parseInt(da_behind)-100;
        da_before = a+"";
        StringBuffer show_before = new StringBuffer(da_before);
        show_before.insert(8,"日");
        show_before.insert(6,"月");
        show_before.insert(4,"年");
        data_behind = findViewById(R.id.data_behind);
        data_before = findViewById(R.id.data_before);
        show_data_before = findViewById(R.id.show_data_before);
        show_data_behind = findViewById(R.id.show_data_behind);
        show_data_before.setText(show_before);
        show_data_behind.setText(show_behind);
        count(da_before,da_behind);
        calendar = Calendar.getInstance();
        data_behind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "onClick: ");
                new DatePickerDialog(statistic.this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;
                        setTitle(year + "-" + monthOfYear + "-" + dayOfMonth);
                        String curdata = "";
                        String data = year+"年";
                        curdata += year+"";
                        Log.e("data", "0"+monthOfYear);
                        if (monthOfYear/10 == 0){
                            data += "0"+monthOfYear+"月";
                            curdata += "0"+monthOfYear;
                        }
                        else {
                            data += monthOfYear+"月";
                            curdata += monthOfYear+"";
                        }
                        if (dayOfMonth/10 == 0){
                            data += "0"+dayOfMonth+"日";
                            curdata += "0"+dayOfMonth;
                        }
                        else {
                            data += dayOfMonth+"日";
                            curdata += dayOfMonth+"";
                        }
                        da_behind = curdata;
                        show_data_behind.setText(data);
                        count(da_before,da_behind);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        data_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(statistic.this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;
                        setTitle(year + "-" + monthOfYear + "-" + dayOfMonth);
                        String curdata = "";
                        String data = year+"年";
                        curdata += year+"";
                        Log.e("data", "0"+monthOfYear);
                        if (monthOfYear/10 == 0){
                            data += "0"+monthOfYear+"月";
                            curdata += "0"+monthOfYear;
                        }
                        else {
                            data += monthOfYear+"月";
                            curdata += monthOfYear+"";
                        }
                        if (dayOfMonth/10 == 0){
                            data += "0"+dayOfMonth+"日";
                            curdata += "0"+dayOfMonth;
                        }
                        else {
                            data += dayOfMonth+"日";
                            curdata += dayOfMonth+"";
                        }
                        show_data_before.setText(data);
                        da_before = curdata;
                        count(da_before,da_behind);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
    }

    private void initBarChart(int start,int done,int postpone) {
        barChart = findViewById(R.id.bar_chart1);
        barChart.getDescription().setEnabled(false); // 不显示描述
        barChart.setExtraOffsets(20, 20, 20, 300); // 设置饼图的偏移量，类似于内边距 ，设置视图窗口大小
        setAxis(); // 设置坐标轴
        setData(start,done,postpone);  // 设置数据
    }

    private void setData(int start,int done,int postpone) {
        List<IBarDataSet> sets = new ArrayList<>();
        // 此处有两个DataSet，所以有两条柱子，BarEntry（）中的x和y分别表示显示的位置和高度
        // x是横坐标，表示位置，y是纵坐标，表示高度
        List<BarEntry> barEntries1 = new ArrayList<>();
        Log.d("TAGf", start +""+done);
        barEntries1.add(new BarEntry(0,(float)start));
        barEntries1.add(new BarEntry(1, (float)done));
        barEntries1.add(new BarEntry(2, (float)postpone));
        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
        barDataSet1.setValueTextColor(Color.RED); // 值的颜色
        barDataSet1.setValueTextSize(15f); // 值的大小
        barDataSet1.setColor(Color.parseColor("#1AE61A")); // 柱子的颜色
        // 设置柱子上数据显示的格式
        barDataSet1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                // 此处的value默认保存一位小数
                return (int)value+"";
            }
        });

        sets.add(barDataSet1);

        BarData barData = new BarData(sets);
        barData.setBarWidth(0.3f); // 设置柱子的宽度
        barChart.setData(barData);
    }

    private void setAxis() {
        // 设置x轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // 设置x轴显示在下方，默认在上方
        xAxis.setDrawGridLines(false); // 将此设置为true，绘制该轴的网格线。
        xAxis.setLabelCount(3);  // 设置x轴上的标签个数
        xAxis.setTextSize(15f); // x轴上标签的大小
        final String labelName[] = {"创建","完成","延期"};
        // 设置x轴显示的值的格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if ((int) value < labelName.length) {
                    return labelName[(int) value];
                } else {
                    return "";
                }
            }
        });
        xAxis.setYOffset(15); // 设置标签对x轴的偏移量，垂直方向

        // 设置y轴，y轴有两条，分别为左和右
        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(10f);  // 设置y轴的最大值
        yAxis_right.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxis_right.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(10f);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setTextSize(10f);// 设置y轴的标签大小
    }

    private void count(String data_before,String data_behind) {
        start = 0;
        done = 0;
        postpone = 0;
        Cursor cursor = db.query(MySqliteOpenHelper.SQlite.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range")
            String time = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.time));
//            Log.d("TAG", (time.compareTo(data_before) == 1)+"");
            @SuppressLint("Range")
            String state = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.SQlite.state));
            if (Integer.parseInt(data_before)<=Integer.parseInt(time) && Integer.parseInt(time)<=Integer.parseInt(data_behind)){
                Log.d("TAG", state+"");
                if (state.equals("start")){
                    start += 1;
                }else if(state.equals("done")){
                    done += 1;
                }else if(state.equals("postpone")){
                    postpone += 1;
                }
            }
        }
        Log.d("TAG", start+""+done+""+postpone);
        initBarChart(start,done,postpone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exchange) {
            startActivity(new Intent(this,show_list.class));
            return true;
        }
        if (id == R.id.statist) {
            startActivity(new Intent(this,statistic.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}