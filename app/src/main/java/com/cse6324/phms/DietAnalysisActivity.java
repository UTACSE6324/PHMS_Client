package com.cse6324.phms;

import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.DietListAdapter;
import com.cse6324.bean.DietBean;
import com.cse6324.dialog.ContactDialog;
import com.cse6324.fragment.DietFragment;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.service.MyApplication;
import com.cse6324.util.FormatUtil;
import com.cse6324.util.UserUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
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
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

import static com.cse6324.service.MyApplication.getContext;

/**
 * Created by Jarvis on 2017/3/25.
 */

public class DietAnalysisActivity extends AppCompatActivity {
    private Context context;
    private TabLayout tabs;
    private TextView tvDate, startDate, endDate;
    private ImageView left, right;

    private Calendar cal, startCal, endCal;

    private CardView shareCard;

    private TextView tvCalorie;
    private LineChart chartCalorie;

    private PieChart chartPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_diet_analysis);

        context = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Analysis");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);

        cal = (Calendar) getIntent().getSerializableExtra("date");

        initViews();
        initData();
    }

    public void initViews() {
        tvDate = (TextView) findViewById(R.id.tv_date);
        startDate = (TextView) findViewById(R.id.start_date);
        endDate = (TextView) findViewById(R.id.end_date);
        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Week"));
        tabs.addTab(tabs.newTab().setText("Month"));

        shareCard = (CardView) findViewById(R.id.card_share);
        shareCard.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ContactDialog(DietAnalysisActivity.this,
                                startDate.getText().toString(), endDate.getText().toString()).show();
                    }
                }
        );

        tabs.addOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        initData();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                }
        );

        tvDate.setText(FormatUtil.getDate(cal));
        tvDate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(context,
                                new DatePickerDialog.OnDateSetListener() {
                                    @Override
                                    public void onDateSet(DatePicker timePicker, int year, int month, int day) {
                                        cal.set(year, month, day);
                                        tvDate.setText(FormatUtil.getDate(cal));
                                        initData();
                                    }
                                },
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DATE)).show();
                    }
                }
        );

        left.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cal.add(Calendar.DAY_OF_MONTH, -1);
                        tvDate.setText(FormatUtil.getDate(cal));
                        initData();
                    }
                }
        );

        right.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cal.add(Calendar.DAY_OF_MONTH, 1);
                        tvDate.setText(FormatUtil.getDate(cal));
                        initData();
                    }
                }
        );

        tvCalorie = (TextView) findViewById(R.id.tv_cal);
        chartCalorie = (LineChart) findViewById(R.id.cal_chart);
        chartCalorie.setDescription("");
        chartCalorie.setNoDataTextDescription("You need to provide data for the chart.");
        chartCalorie.setTouchEnabled(false);
        chartCalorie.setDragEnabled(true);
        chartCalorie.setScaleEnabled(true);
        chartCalorie.setPinchZoom(true);

        chartCalorie.getAxisRight().setEnabled(false);
        chartCalorie.getAxisLeft().setEnabled(false);
        chartCalorie.getXAxis().setEnabled(false);

        YAxis leftAxis = chartCalorie.getAxisLeft();
        leftAxis.setDrawLimitLinesBehindData(false);
        XAxis topAxis = chartCalorie.getXAxis();
        topAxis.setDrawLimitLinesBehindData(false);

        chartPercent = (PieChart) findViewById(R.id.percent_chart);

        chartPercent.setUsePercentValues(true);
        chartPercent.setExtraOffsets(5, 10, 5, 5);

        chartPercent.setDragDecelerationFrictionCoef(0.95f);

        chartPercent.setCenterText("Percent of intake");

        chartPercent.setDrawHoleEnabled(true);
        chartPercent.setHoleColor(Color.WHITE);

        chartPercent.setTransparentCircleColor(Color.WHITE);
        chartPercent.setTransparentCircleAlpha(110);

        chartPercent.setHoleRadius(58f);
        chartPercent.setTransparentCircleRadius(61f);

        chartPercent.setDrawCenterText(true);

        chartPercent.setRotationAngle(0);
        chartPercent.setRotationEnabled(true);
        chartPercent.setHighlightPerTapEnabled(true);
        chartPercent.setDescription("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initData() {
        if (tabs.getTabAt(tabs.getSelectedTabPosition()).getText().equals("Week")) {
            startCal = FormatUtil.getWeekOfDate(cal)[0];
            endCal = FormatUtil.getWeekOfDate(cal)[1];

            startDate.setText(FormatUtil.getDate(startCal));
            endDate.setText(FormatUtil.getDate(endCal));
        } else {
            startCal = FormatUtil.getMonthOfDate(cal)[0];
            endCal = FormatUtil.getMonthOfDate(cal)[1];

            startDate.setText(FormatUtil.getDate(startCal));
            endDate.setText(FormatUtil.getDate(endCal));
        }

        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                .add("uid", MyApplication.getPreferences(UserUtil.UID))
                .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                .add("startdate", FormatUtil.getDate(startCal))
                .add("enddate", FormatUtil.getDate(endCal))
                .get(Constant.URL_GETDIETHISTORY, callback);
    }

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

            if (response == null || response.length() == 0) {
                Toast.makeText(getContext(), "Connect fail", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    List<DietBean> dietList = JSON.parseArray(response, DietBean.class);
                    setAvgCal(dietList);
                    setPercent(dietList);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    public void setPercent(List<DietBean> dietList) {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();


        float breakfast = 0, lunch = 0, dinner = 0, snack = 0;


        for (int i = 0; i < dietList.size(); i++) {
            switch (dietList.get(i).getType()) {
                case 0:
                    breakfast += Float.parseFloat(dietList.get(i).getCalorie());
                    break;
                case 1:
                    lunch += Float.parseFloat(dietList.get(i).getCalorie());
                    break;
                case 2:
                    dinner += Float.parseFloat(dietList.get(i).getCalorie());
                    break;
                case 3:
                    snack += Float.parseFloat(dietList.get(i).getCalorie());
                    break;
            }
        }

        entries.add(new PieEntry(breakfast, "breakfast"));
        entries.add(new PieEntry(lunch, "lunch"));
        entries.add(new PieEntry(dinner, "dinner"));
        entries.add(new PieEntry(snack, "snack"));

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chartPercent.setData(data);
        chartPercent.highlightValues(null);
        chartPercent.invalidate();
    }

    public void setAvgCal(List<DietBean> dietList) {
        ArrayList<Entry> values = new ArrayList<>();

        int num;
        if (tabs.getSelectedTabPosition() == 0)
            num = 7;
        else
            num = 30;

        float total = 0;
        for (int i = 0; i < num; i++) {
            String date = FormatUtil.getDateOffset(startCal, i);
            float calorie = 0;

            for (int j = 0; j < dietList.size(); j++) {
                if (dietList.get(j).getDate().equals(date)) {
                    calorie += Float.parseFloat(dietList.get(j).getCalorie());
                }
            }

            total += calorie;
            values.add(new Entry(i, calorie));
        }

        total = total / dietList.size();
        tvCalorie.setText(total + "");

        LineDataSet set1;
        if (chartCalorie.getData() != null &&
                chartCalorie.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chartCalorie.getData().getDataSetByIndex(0);
            set1.setValues(values);
        } else {
            set1 = new LineDataSet(values, "Calorie intake is below average");

            set1.setColor(R.color.colorPrimaryLight);
            set1.setCircleColor(R.color.colorPrimaryDark);
            set1.setLineWidth(2f);
            set1.setCircleRadius(4f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(14f);
            set1.setValueTextColor(R.color.colorPrimary);
            set1.setDrawFilled(true);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            if (Utils.getSDKInt() >= 18) {
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_green);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            dataSets.add(set1);

            LineData data = new LineData(dataSets);
            chartCalorie.setData(data);
        }

        chartCalorie.getData().notifyDataChanged();
        chartCalorie.notifyDataSetChanged();
        chartCalorie.invalidate();
    }
}
