package com.yanyi.dateselect;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.yanyi.datelib.SelectPeriod;

/**
 * @author Administrator
 * @date 2018/5/19 16:38
 * @e-mail love@yanyi.red
 * @overview
 */
public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        mContext = this;
        findViewById(R.id.but).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SelectData selectData = new SelectData(mContext, SelectType.MIN);
//                selectData.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//                selectData.setDateClickListener(new SelectData.OnDateClickListener() {
//                    @Override
//                    public void onClick(String year, String month, String day, String hour, String minute) {
//                        Log.v("sss", year + "-" + month + "-" + day + " " + hour + ":" + minute);
//                    }
//                });
                select(view);
            }
        });
    }

    private void select(View view) {
        SelectPeriod selectPeriod = new SelectPeriod(mContext);
        selectPeriod.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        selectPeriod.setOnDateClickListener(new SelectPeriod.OnDateClickListener() {
            @Override
            public void onDateClickListener(String startTime, String endTime) {
                log(startTime + "\t" + endTime);
            }
        });
    }

    private void log(Object log) {
        Log.v(TAG, log.toString());
    }
}
