package com.cse6324.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

/**
 * Created by hopelty on 2017/2/26.
 */

public class CaloriePlanDialog{

    Context context;
    MaterialDialog dialog;
    MaterialEditText editText;
    TextView btn_cancel,btn_submit,tv_calorie,tv_weight;
    MyListener myListener;

    public interface MyListener {
        public void refreshActivity();
    }

    public CaloriePlanDialog(Context context,final MyListener myListener){

        this.myListener = myListener;

        dialog = new MaterialDialog
                .Builder(context).title("Change Calorie Plan")
                .customView(R.layout.dialog_calorieplan,false)
                .build();

        View layout = dialog.getCustomView();

        editText = (MaterialEditText) layout.findViewById(R.id.new_plan);
        btn_cancel = (TextView) layout.findViewById(R.id.cancel);
        btn_submit = (TextView) layout.findViewById(R.id.submit);
        tv_calorie = (TextView) layout.findViewById(R.id.calorie);
        tv_weight = (TextView) layout.findViewById(R.id.weight);

        try{
            int weight = Integer.parseInt(MyApplication.getPreferences(UserUtil.WEIGHT));
            int calorie = weight * 33;

            tv_weight.setText(weight + "");
            tv_calorie.setText(calorie + "");
        }catch (Exception e){

        }

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApplication.setStringPref(UserUtil.CAL,editText.getText().toString());
                myListener.refreshActivity();
                new HttpUtil(HttpUtil.NORMAL_PARAMS)
                        .add("uid",MyApplication.getPreferences(UserUtil.UID))
                        .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                        .add("calorie",MyApplication.getPreferences(UserUtil.CAL));
                dialog.dismiss();
            }
        });

    }

    public void show(){
        dialog.show();
    }
}
