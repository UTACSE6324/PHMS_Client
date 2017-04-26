package com.cse6324.dialog;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cse6324.adapter.FoodListAdapter;
import com.cse6324.bean.DietBean;
import com.cse6324.bean.FoodAPIBean;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.MainActivity;
import com.cse6324.phms.R;
import com.cse6324.service.MyApplication;
import com.cse6324.util.FormatUtil;
import com.cse6324.util.UserUtil;
import com.mypopsy.widget.FloatingSearchView;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.Calendar;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import okhttp3.Headers;
import okhttp3.Response;


/**
 * Created by Jarvis on 2017/2/26.
 */

public class AddDietDialog {

    private final String appKey = "8aa879546b2064de87ebc15334754bab";
    private final String appId = "b871bf7e";

    private Context context;
    private int type;
    private MaterialDialog dialog;

    private com.mypopsy.widget.FloatingSearchView searchView;
    private FoodListAdapter adapter;

    private TextView unit;
    private MaterialEditText etName,etQuantity,etCal;

    private Shimmer shimmer;
    private ShimmerTextView submit;
    private TextView cancel;

    private MyListener myListener;

    private DietBean dietBean;
    private Calendar cal;

    public interface MyListener {
        public void refreshActivity(DietBean bean, int type);
    }

    public AddDietDialog(Context context,int type,MyListener myListener, Calendar cal){
        this.context = context;
        this.type = type;
        this.myListener = myListener;
        this.cal = cal;
    }

    public void show(){
        dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.dialog_add_diet,false)
                .build();

        dialog.show();

        View view = dialog.getCustomView();

        searchView = (FloatingSearchView) view.findViewById(R.id.search);

        unit = (TextView) view.findViewById(R.id.tv_unit);
        etName = (MaterialEditText) view.findViewById(R.id.et_name);
        etQuantity = (MaterialEditText) view.findViewById(R.id.et_quantity);
        etCal = (MaterialEditText) view.findViewById(R.id.et_calorie);

        submit = (ShimmerTextView) view.findViewById(R.id.btn_submit);
        cancel = (TextView) view.findViewById(R.id.btn_cancel);

        shimmer = new Shimmer();

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
                        shimmer.start(submit);
                        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                                .add("uid", MyApplication.getPreferences(UserUtil.UID))
                                .add("token",MyApplication.getPreferences(UserUtil.TOKEN))
                                .add("date", FormatUtil.getDate(cal))
                                .add("type", type +"")
                                .add("name", etName.getText().toString())
                                .add("quantity", etQuantity.getText().toString())
                                .add("unit",unit.getText().toString())
                                .add("calorie",etCal.getText().toString())
                                .get(Constant.URL_ADDDIETHISTORY,addDietCallback);

                        dietBean = new DietBean();
                        dietBean.setType(type);
                        dietBean.setName(etName.getText().toString());
                        dietBean.setQuantity(etQuantity.getText().toString());
                        dietBean.setUnit(unit.getText().toString());
                        dietBean.setCalorie(etCal.getText().toString());
                    }
                }
        );

        adapter = new FoodListAdapter(context,this);
        searchView.setAdapter(adapter);

        searchView.addTextChangedListener(
                new TextWatcher(){
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String query = s.toString();

                        JSONArray array = new JSONArray();
                        array.add("item_name");
                        array.add("nf_calories");

                        JSONObject obj = new JSONObject();
                        obj.put("appId",appId);
                        obj.put("appKey",appKey);
                        obj.put("query",query);
                        obj.put("fields",array);

                        new HttpUtil(HttpUtil.NORMAL_PARAMS)
                                .addJSON(obj)
                                .post(Constant.URL_SEARCHFOOD,callback);
                    }
                }
        );

        searchView.setOnSearchFocusChangedListener(
                new FloatingSearchView.OnSearchFocusChangedListener() {
                    @Override
                    public void onFocusChanged(boolean b) {
                        if(!b){
                            InputMethodManager imm = (InputMethodManager) context.
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                        }
                    }
                }
        );

    }

    private BaseHttpRequestCallback callback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            try{
                FoodAPIBean bean = JSON.parseObject(response,FoodAPIBean.class);
                adapter.setList(bean.getResult());
                adapter.notifyDataSetChanged();

            }catch (Exception e){}
        }
    };

    private BaseHttpRequestCallback addDietCallback = new BaseHttpRequestCallback() {
        @Override
        public void onResponse(Response httpResponse, String response, Headers headers) {
            Toast.makeText(context,headers.get("summary"),Toast.LENGTH_SHORT).show();

            if(headers.get("Status-Code").equals("1") && dietBean != null){
                dietBean.setDietid(Integer.parseInt(response));
                myListener.refreshActivity(dietBean,AddDietDialog.this.type);
                dialog.dismiss();
            }

            shimmer.cancel();
        }
    };

    public void getSearchResult(final FoodAPIBean.FoodResultBean.FoodBean bean){
        searchView.clearFocus();

        int index = bean.getItem_name().indexOf("-");
        String name;

        if(index == -1) {
            name = bean.getItem_name();
            unit.setText("");
        }else {
            name = bean.getItem_name().substring(0,index);
            unit.setText(bean.getItem_name().substring(index+1,
                    bean.getItem_name().length()));
        }


        etName.setText(name.replaceAll("'","''"));
        etQuantity.setText("1");
        etCal.setText(bean.getNf_calories());

        etQuantity.addTextChangedListener(
                new TextWatcher(){
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        try{
                            double quantity = Double.parseDouble(etQuantity.getText().toString());
                            double calorie = quantity * Double.parseDouble(bean.getNf_calories());
                            etCal.setText(calorie + "");

                        }catch (Exception e){
                            Toast.makeText(context, "Invalid insert", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}
