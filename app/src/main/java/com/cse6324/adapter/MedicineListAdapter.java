package com.cse6324.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cse6324.bean.ContactBean;
import com.cse6324.bean.MedicineBean;
import com.cse6324.dialog.AddMedicineDialog;
import com.cse6324.dialog.CaloriePlanDialog;
import com.cse6324.dialog.SetReminderDialog;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;
import com.cse6324.service.MedicineReceiver;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuCustomItem;
import com.shehabic.droppy.DroppyMenuItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Jarvis on 2017/3/1.
 */

public class MedicineListAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<MedicineBean> medicineList;
    private SetReminderDialog.MyListener setReminderListener;
    PendingIntent pi;
    AlarmManager am;
    Intent alarmIntent;

    private MyListener refreshListener;


    public MedicineListAdapter(Context context, SetReminderDialog.MyListener setReminderListener, MyListener refreshListener) {
        this.context = context;
        this.medicineList = new ArrayList<>();
        this.setReminderListener = setReminderListener;
        this.refreshListener = refreshListener;
    }

    public interface MyListener {
        public void refreshActivity();
    }

    public void setList(List<MedicineBean> medicineList) {
        this.medicineList = medicineList;
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_medicine, null);
        view.setLayoutParams(lp);
        return new ItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        final ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
        final MedicineBean bean = medicineList.get(i);

        itemViewHolder.bean = bean;
        itemViewHolder.tvName.setText(bean.getName());
        itemViewHolder.tvQuantity.setText(bean.getQuantity() + " " + bean.getUnit());

        if (!isEmpty(bean.getTimes()))
            itemViewHolder.tvTime.setText(bean.getTimes());
        else
            itemViewHolder.tvTime.setText("No reminder");

        if (bean.getReminder() != null && bean.getReminder().equals("1")) {
            itemViewHolder.ivAlarm.setImageResource(R.mipmap.ic_alarm_on_48dp);
            itemViewHolder.ivAlarm.setTag("1");
        } else {
            itemViewHolder.ivAlarm.setImageResource(R.mipmap.ic_alarm_off_48dp);
            itemViewHolder.ivAlarm.setTag("0");
        }


        itemViewHolder.ivAlarm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (itemViewHolder.ivAlarm.getTag().equals("0")) {

                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            Date date;
                            try {
                                date = sdf.parse(bean.getTimes());
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR_OF_DAY, date.getHours());
                                c.set(Calendar.MINUTE, date.getMinutes());
                                am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                alarmIntent = new Intent(context, MedicineReceiver.class);
                                alarmIntent.putExtra("days", bean.getDays());
                                bean.setAlarmNumber(c.getTimeInMillis() + "");
                                Random random = new Random();
                                int num = random.nextInt(100000);
                                bean.setAlarmNumber(num + "");

                                pi = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                                am.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);
                                Toast.makeText(context, c.getTime().toString(), Toast.LENGTH_SHORT).show();

                                itemViewHolder.ivAlarm.setImageResource(R.mipmap.ic_alarm_on_48dp);
                                itemViewHolder.ivAlarm.setTag("1");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            itemViewHolder.ivAlarm.setImageResource(R.mipmap.ic_alarm_off_48dp);
                            itemViewHolder.ivAlarm.setTag("0");

                            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
                            am.cancel(pi);
                            pi.cancel();
                        }
                    }
                }
        );
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        MedicineBean bean;
        TextView tvName, tvQuantity, tvTime;
        ImageView ivAlarm;

        private ItemViewHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivAlarm = (ImageView) itemView.findViewById(R.id.iv_alarm);

            itemView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new SetReminderDialog(context, bean, setReminderListener).show();
                        }
                    }
            );

            itemView.setOnLongClickListener(
                    new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            new HttpUtil(HttpUtil.NORMAL_PARAMS)
                                    .add("uid", MyApplication.getPreferences(UserUtil.UID))
                                    .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                                    .add("mid", bean.getMid())
                                    .get(Constant.URL_DELETEMEDICINE, callback);
                            return false;
                        }
                    }
            );
        }
    }

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            if (headers.get("Status-Code").equals("1")) {
                refreshListener.refreshActivity();
            } else {
                Toast.makeText(context, headers.get("summary"), Toast.LENGTH_SHORT).show();
            }
        }
    };
}
