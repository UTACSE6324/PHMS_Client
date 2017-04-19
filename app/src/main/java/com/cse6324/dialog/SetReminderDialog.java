package com.cse6324.dialog;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.view.menu.MenuView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cse6324.bean.MedicineBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.FormatUtil;
import com.cse6324.util.UserUtil;
import com.cunoraz.tagview.Tag;
import com.cunoraz.tagview.TagView;
import com.github.channguyen.rsv.RangeSliderView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Jarvis on 2017/3/18.
 */

public class SetReminderDialog {

    private Context context;
    private MaterialDialog dialog;
    private MedicineBean bean;

    private SetReminderDialog.MyListener myListener;

    private TextView tvStartDate, tvEndDate, tvAddTime, tvRepeat, tvCancel, tvSubmit;
    private TagView timeTag;
    private RangeSliderView sliderView;
    private String startDate,endDate;

    private int repeatDays;

    public interface MyListener {
        public void refreshActivity();
    }

    public SetReminderDialog(Context context, MedicineBean bean, final SetReminderDialog.MyListener myListener) {
        this.context = context;
        this.bean = bean;
        this.myListener = myListener;
    }

    public void show() {
        dialog = new MaterialDialog.Builder(context)
                .title("Set Reminder")
                .customView(R.layout.dialog_set_reminder, false)
                .build();

        dialog.show();

        View view = dialog.getCustomView();
        tvStartDate = (TextView) view.findViewById(R.id.tv_start_date);
        tvEndDate = (TextView) view.findViewById(R.id.tv_end_date);
        tvAddTime = (TextView) view.findViewById(R.id.tv_add_time);
        timeTag = (TagView) view.findViewById(R.id.tag_group);
        tvRepeat = (TextView) view.findViewById(R.id.tv_repeat);
        sliderView = (RangeSliderView) view.findViewById(R.id.rsv);
        tvCancel = (TextView) view.findViewById(R.id.btn_cancel);
        tvSubmit = (TextView) view.findViewById(R.id.btn_submit);

        /**Set Date**/
        if (isEmpty(bean.getStart_date())) {
            tvStartDate.setText(FormatUtil.getCurrentDateOffset(0));
            startDate = FormatUtil.getCurrentDateOffset(0);
        }else {
            tvStartDate.setText(bean.getStart_date());
            startDate = bean.getStart_date();
        }

        if (isEmpty(bean.getEnd_date())) {
            tvEndDate.setText(FormatUtil.getCurrentDateOffset(0));
            endDate = FormatUtil.getCurrentDateOffset(0);
        }else{
            tvEndDate.setText(bean.getEnd_date());
            endDate = bean.getEnd_date();
        }

        /**Set Repeat**/
        if(!isEmpty(bean.getDays())){
            int days = Integer.parseInt(bean.getDays());
            tvRepeat.setText("Repeat every " + days + " day");
            sliderView.setInitialIndex(days);
        }else
            tvRepeat.setText("Repeat every day");

        sliderView.setOnSlideListener(
                new RangeSliderView.OnSlideListener() {
                    @Override
                    public void onSlide(int index) {
                        tvRepeat.setText("Repeat every " + (index+1) + " day");
                        repeatDays = index + 1;
                    }
                }
        );

        /**Set Time**/
        try {
            String[] list = bean.getTimes().split(",");

            for(int i = 0; i< list.length; i++){
                if(!isEmpty(list[i]))
                timeTag.addTag(new Tag(list[i]));
            }
        } catch (Exception e) {
        }

        tvAddTime.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Tag tag = new Tag(new DecimalFormat("00").format(hourOfDay)
                                + ":" + new DecimalFormat("00").format(minute));
                        timeTag.addTag(tag);
                    }
                }, 9, 0, true).show();
            }
        });

        tvStartDate.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker timePicker, int year, int month, int day) {
                        startDate = year + "-" + (month + 1) + "-" + day;
                        tvStartDate.setText(startDate);
                    }
                },
                        FormatUtil.getYearOfStr(startDate),
                        FormatUtil.getMonthOfStr(startDate) - 1,
                        FormatUtil.getDayOfStr(startDate)).show();
            }
        });

        tvEndDate.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View view) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker timePicker, int year, int month, int day) {
                        endDate = year + "-" + (month + 1) + "-" + day;
                        tvEndDate.setText(endDate);
                    }
                },
                        FormatUtil.getYearOfStr(endDate),
                        FormatUtil.getMonthOfStr(endDate) - 1,
                        FormatUtil.getDayOfStr(endDate)).show();
            }
        });

        tvCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }
        );

        tvSubmit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String times = "";
                        List<Tag> list = timeTag.getTags();

                        for(int i = 0; i< list.size() - 1; i++){
                            if(!isEmpty(list.get(i).text))
                                times = times + list.get(i).text + ",";
                        }
                        if(list.size() > 0 && !isEmpty(list.get(list.size()-1).text))
                            times = times + list.get(list.size()-1).text;

                        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                                .add("uid", MyApplication.getPreferences(UserUtil.UID))
                                .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                                .add("mid", bean.getMid())
                                .add("times",times)
                                .add("days", repeatDays + "")
                                .add("start_date", startDate)
                                .add("end_date", endDate)
                                .get(Constant.URL_SETREMINDER, callback);
                    }
                }
        );
    }

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {

            if (response == null || response.length() == 0) {
                Toast.makeText(context, "Connect fail", Toast.LENGTH_SHORT).show();
            } else if (headers.get("Status-Code").equals("1")) {
                Toast.makeText(context, headers.get("summary"), Toast.LENGTH_SHORT).show();
                myListener.refreshActivity();
                dialog.dismiss();
            } else {
                Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
            }
        }
    };

}
