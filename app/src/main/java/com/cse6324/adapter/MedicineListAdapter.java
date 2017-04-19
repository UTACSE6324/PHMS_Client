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
import com.cse6324.phms.R;
import com.cse6324.service.MedicineReceiver;
import com.cse6324.service.MyApplication;
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


    public MedicineListAdapter(Context context, SetReminderDialog.MyListener setReminderListener) {
        this.context = context;
        this.medicineList = new ArrayList<>();
        this.setReminderListener = setReminderListener;
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
        ItemViewHolder itemViewHolder = (ItemViewHolder) viewHolder;
        MedicineBean bean = medicineList.get(i);

        itemViewHolder.bean = bean;
        itemViewHolder.tvName.setText(bean.getName());
        itemViewHolder.tvQuantity.setText(bean.getQuantity() + " " + bean.getUnit());
        itemViewHolder.tvInstraction.setText(bean.getInstructions());

        if (!isEmpty(bean.getTimes()))
            itemViewHolder.tvTime.setText(bean.getTimes());
        else
            itemViewHolder.tvTime.setText("No reminder");

        if (bean.getReminder() != null && bean.getReminder().equals("1"))
            itemViewHolder.pinReminder.setSelected(true);
        else
            itemViewHolder.pinReminder.setSelected(false);

    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        MedicineBean bean;
        TextView tvName, tvQuantity, tvInstraction, tvTime;
        ImageView ivMore;
        MaterialAnimatedSwitch pinReminder;

        private ItemViewHolder(View itemView) {
            super(itemView);


            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tvInstraction = (TextView) itemView.findViewById(R.id.tv_instruction);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);

            ivMore = (ImageView) itemView.findViewById(R.id.iv_more);

            DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, ivMore);

            droppyBuilder.addMenuItem(new DroppyMenuItem("Edit", R.mipmap.ic_create_black_24dp));
            droppyBuilder.addMenuItem(new DroppyMenuItem("Delete", R.mipmap.ic_delete_black_24dp));

            droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
                @Override
                public void call(View v, int id) {

                    switch (id) {
                        case 0:
                            new SetReminderDialog(context, bean, setReminderListener).show();
                            break;
                        case 1:
                            break;
                    }
                }
            });

            droppyBuilder.setPopupAnimation(new DroppyFadeInAnimation());

            DroppyMenuPopup droppyMenu = droppyBuilder.build();

            pinReminder = (MaterialAnimatedSwitch) itemView.findViewById(R.id.pin_reminder);

            pinReminder.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(boolean b) {
                    try {
                        if (b == true) {
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
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
                            am.cancel(pi);
                            pi.cancel();
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

}
