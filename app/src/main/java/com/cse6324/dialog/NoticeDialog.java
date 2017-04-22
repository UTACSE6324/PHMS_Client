package com.cse6324.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cse6324.http.Constant;
import com.cse6324.http.HttpUtil;
import com.cse6324.phms.R;

/**
 * Created by Jarvis on 2017/4/21.
 */

public class NoticeDialog {
    MaterialDialog dialog;

    public NoticeDialog(Context context, String summary, String description){

        dialog = new MaterialDialog
                .Builder(context)
                .customView(R.layout.dialog_conflict,false)
                .title("Notice Detail")
                .positiveText("OK")
                .onPositive(
                        new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }
                )
                .build();

        View layout = dialog.getCustomView();

        WebView webView = (WebView) layout.findViewById(R.id.webView);
        webView.loadUrl(Constant.URL_CONFLICT +
                "summary=" + summary +
                "&description=" + description);
    }

    public void show(){
        dialog.show();
    }
}
