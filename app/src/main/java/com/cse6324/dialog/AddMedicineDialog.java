package com.cse6324.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.cse6324.adapter.AddMedicineAdapter;
import com.cse6324.bean.DietBean;
import com.cse6324.bean.MedicineAPIBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.UserUtil;
import com.mypopsy.widget.FloatingSearchView;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Jarvis on 2017/3/18.
 */

public class AddMedicineDialog {

    private Context context;
    private MaterialDialog dialog;

    private MaterialEditText name, instruction;
    private Spinner quantity;
    private TextView submit, cancel, tvUnit;

    private AddMedicineAdapter adapter;

    private com.mypopsy.widget.FloatingSearchView searchView;

    MyListener myListener;

    public interface MyListener {
        public void refreshActivity();
    }

    public AddMedicineDialog(Context context, final MyListener myListener) {
        this.context = context;
        this.myListener = myListener;
    }

    public void show() {
        dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_add_medicine, false)
                .build();

        dialog.show();

        View view = dialog.getCustomView();

        name = (MaterialEditText) view.findViewById(R.id.txt_medicine_name);
        instruction = (MaterialEditText) view.findViewById(R.id.txt_med_instructions);

        quantity = (Spinner) view.findViewById(R.id.sp_quantity);
        tvUnit = (TextView) view.findViewById(R.id.tv_unit);

        submit = (TextView) view.findViewById(R.id.btn_submit);
        cancel = (TextView) view.findViewById(R.id.btn_cancel);

        searchView = (FloatingSearchView) view.findViewById(R.id.search);

        cancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }
        );

        submit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isEmpty(name.getText())) {
                            new HttpUtil(HttpUtil.NORMAL_PARAMS)
                                    .add("uid", MyApplication.getPreferences(UserUtil.UID))
                                    .add("token", MyApplication.getPreferences(UserUtil.TOKEN))
                                    .add("name", name.getText().toString())
                                    .add("quantity", quantity.getSelectedItem().toString())
                                    .add("unit", tvUnit.getText().toString())
                                    .add("api_id", API_ID)
                                    .add("instructions", instruction.getText().toString())
                                    .get(Constant.URL_ADDMEDICINE, callback);
                        }
                    }
                }
        );

        adapter = new AddMedicineAdapter(context, this);
        searchView.setAdapter(adapter);

        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString();
                new HttpUtil(HttpUtil.NON_TOKEN_PARAMS)
                        .get(Constant.URL_SEARCHMEDICINE + "terms=" + query + "&ef=STRENGTHS_AND_FORMS,RXCUIS", search_callback);

            }
        });
    }

    private BaseHttpRequestCallback search_callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            try {
                com.alibaba.fastjson.JSONArray jsonArray = JSON.parseArray(response);
                com.alibaba.fastjson.JSONArray array = jsonArray.getJSONArray(1);
                com.alibaba.fastjson.JSONObject object = jsonArray.getJSONObject(2);
                com.alibaba.fastjson.JSONArray arr = object.getJSONArray("STRENGTHS_AND_FORMS");
                com.alibaba.fastjson.JSONArray arr1 = object.getJSONArray("RXCUIS");
                List<MedicineAPIBean> list = new ArrayList<>();
                int count = 0;
                for (Object obj : array) {
                    MedicineAPIBean bean = new MedicineAPIBean();
                    com.alibaba.fastjson.JSONArray formArr = arr.getJSONArray(count);
                    com.alibaba.fastjson.JSONArray idArr = arr1.getJSONArray(count);
                    bean.setName(obj.toString());
                    bean.setApi_id(idArr.getString(0));
                    bean.setStrength_And_Forms(formArr.getString(0));
                    list.add(bean);
                    count++;
                }
                adapter.setList(list);
                adapter.notifyDataSetChanged();
            }catch (Exception e){}
        }
    };

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

    String API_ID;

    public void getSearchResult(MedicineAPIBean bean) {
        searchView.clearFocus();
        name.setText(bean.getName());
        tvUnit.setText(bean.getStrength_And_Forms());

        API_ID = bean.getApi_id();
    }
}
