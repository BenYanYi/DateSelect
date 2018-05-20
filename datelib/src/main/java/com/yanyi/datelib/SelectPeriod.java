package com.yanyi.datelib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.yanyi.datelib.config.SelectType;
import com.yanyi.datelib.wheelview.AbstractWheelTextAdapter1;
import com.yanyi.datelib.wheelview.OnWheelChangedListener;
import com.yanyi.datelib.wheelview.OnWheelScrollListener;
import com.yanyi.datelib.wheelview.WheelView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Administrator
 * @date 2018/5/19 17:24
 * @email ben@yanyi.red
 * @overview
 */
public class SelectPeriod extends PopupWindow implements View.OnClickListener {
    private String TAG = "SelectPeriod";
    private Context mContext;
    private SelectType selectType;
    private boolean boo;

    public static boolean isDebug = true;

    private View mView;

    private TextView butCancel;//取消按钮
    private TextView butSend;//确定按钮
    private LinearLayout timeLinear;

    private LinearLayout startTimeLinear;
    private TextView startTimeTv;
    private LinearLayout endTimeLinear;
    private TextView endTimeTv;

    private LinearLayout startLinear;
    private WheelView startYearWv;
    private WheelView startMonthWv;
    private WheelView startDayWv;
    private LinearLayout startHourLinear;
    private WheelView startHourWv;
    private LinearLayout startMinuteLinear;
    private WheelView startMinuteWv;

    private LinearLayout endLinear;
    private WheelView endYearWv;
    private WheelView endMonthWv;
    private WheelView endDayWv;
    private LinearLayout endHourLinear;
    private WheelView endHourWv;
    private LinearLayout endMinuteLinear;
    private WheelView endMinuteWv;

    private String[] startYearData, startMonthData, startDayData, startHourData, startMinuteData;
    private String[] endYearData, endMonthData, endDayData, endHourData, endMinuteData;

    private Calendar calendar = Calendar.getInstance();

    private DateTextAdapter startYearAdapter;
    private DateTextAdapter startMonthAdapter;
    private DateTextAdapter startDayAdapter;
    private DateTextAdapter startHourAdapter;
    private DateTextAdapter startMinuteAdapter;

    private DateTextAdapter endYearAdapter;
    private DateTextAdapter endMonthAdapter;
    private DateTextAdapter endDayAdapter;
    private DateTextAdapter endHourAdapter;
    private DateTextAdapter endMinuteAdapter;

    private String startYear;
    private String startMonth;
    private String startDay;
    private String startHour;
    private String startMinute;

    private String endYear;
    private String endMonth;
    private String endDay;
    private String endHour;
    private String endMinute;

    private int maxSize = 14;
    private int minSize = 12;

    private OnDateClickListener onDateClickListener;

    public SelectPeriod(Context context) {
        this(context, SelectType.NONE, true);
    }

    public SelectPeriod(Context context, boolean judgmentTime) {
        this(context, SelectType.NONE, judgmentTime);
    }

    public SelectPeriod(Context context, SelectType selectType) {
        this(context, selectType, true);
    }

    public SelectPeriod(Context context, SelectType selectType, boolean judgmentTime) {
        super(context);
        this.mContext = context;
        this.selectType = selectType;
        this.boo = judgmentTime;
        mView = View.inflate(context, R.layout.select_period_pop_layout, null);
        init();
        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        initData();

        initStartWv();

        initEndWv();

    }

    private void init() {
        butCancel = $(R.id.but_cancel);
        butCancel.setOnClickListener(this);
        butSend = $(R.id.but_send);
        butSend.setOnClickListener(this);
        timeLinear = $(R.id.time_linear);
        timeLinear.setBackgroundResource(R.mipmap.begin_time_bg);
        startTimeLinear = $(R.id.start_time_linear);
        startTimeTv = $(R.id.start_time);
        startTimeLinear.setOnClickListener(this);
        endTimeLinear = $(R.id.end_time_linear);
        endTimeTv = $(R.id.end_time);
        endTimeLinear.setOnClickListener(this);

        startLinear = $(R.id.start_linear);
        startYearWv = $(R.id.start_year);
        startMonthWv = $(R.id.start_month);
        startDayWv = $(R.id.start_day);
        startHourLinear = $(R.id.start_hour_linear);
        startHourWv = $(R.id.start_hour);
        startMinuteLinear = $(R.id.start_minute_linear);
        startMinuteWv = $(R.id.start_minute);
        startLinear.setVisibility(View.VISIBLE);

        endLinear = $(R.id.end_linear);
        endYearWv = $(R.id.end_year);
        endMonthWv = $(R.id.end_month);
        endDayWv = $(R.id.end_day);
        endHourLinear = $(R.id.end_hour_linear);
        endHourWv = $(R.id.end_hour);
        endMinuteLinear = $(R.id.end_minute_linear);
        endMinuteWv = $(R.id.end_minute);
        endLinear.setVisibility(View.GONE);

        switch (selectType) {
            case HOUR:
                startHourLinear.setVisibility(View.GONE);
                endHourLinear.setVisibility(View.GONE);
            case MIN:
                startMinuteLinear.setVisibility(View.GONE);
                endMinuteLinear.setVisibility(View.GONE);
                break;
            case NONE:
            default:
                startHourLinear.setVisibility(View.VISIBLE);
                endHourLinear.setVisibility(View.VISIBLE);
                startMinuteLinear.setVisibility(View.VISIBLE);
                endMinuteLinear.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void initData() {
        int year, month, day, hour, minute;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DATE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        String strYear = String.valueOf(year);
        String strMonth = (month < 10 ? "0" : "") + String.valueOf(month);
        String strDay = (day < 10 ? "0" : "") + String.valueOf(day);
        startYear = strYear;
        endYear = strYear;
        startMonth = strMonth;
        endMonth = strMonth;
        startDay = strDay;
        endDay = strDay;

        String strHour;
        String strMinute;

        switch (selectType) {
            case NONE:
            default:
                strHour = (hour < 10 ? "0" : "") + String.valueOf(hour);
                strMinute = (minute < 10 ? "0" : "") + String.valueOf(minute);
                break;
            case HOUR:
                strHour = "00";
                strMinute = "00";
                break;
            case MIN:
                strHour = (hour < 10 ? "0" : "") + String.valueOf(hour);
                strMinute = "00";
                break;
        }
        startHour = strHour;
        startMinute = strMinute;
        endHour = strHour;
        endMinute = strMinute;

        startYearData = new String[150];
        endYearData = new String[150];
        //int year = calendar.get(Calendar.YEAR);
        for (int i = year - 100, j = 0; i < year + 50; i++, j++) {
            startYearData[j] = i + "";
            endYearData[j] = i + "";
        }

        startMonthData = new String[12];
        endMonthData = new String[12];
        for (int i = 0; i < 12; i++) {
            if (i < 9) {
                startMonthData[i] = "0" + (i + 1) + "";
                endMonthData[i] = "0" + (i + 1) + "";
            } else {
                startMonthData[i] = i + 1 + "";
                endMonthData[i] = i + 1 + "";
            }
        }

        int count = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //Log.e(TAG, "initDatas: "+ strYear+":"+strMonth+":"+strDay+":"+count);

        startDayData = new String[count];
        endDayData = new String[count];
        for (int i = 0; i < count; i++) {
            if (i < 9) {
                startDayData[i] = "0" + (i + 1);
                endDayData[i] = "0" + (i + 1);
            } else {
                startDayData[i] = i + 1 + "";
                endDayData[i] = i + 1 + "";
            }
        }

        startHourData = new String[24];
        endHourData = new String[24];
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                startHourData[i] = "0" + i;
                endHourData[i] = "0" + i;
            } else {
                startHourData[i] = i + "";
                endHourData[i] = i + "";
            }
        }

        startMinuteData = new String[60];
        endMinuteData = new String[60];
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                startMinuteData[i] = "0" + i;
                endMinuteData[i] = "0" + i;
            } else {
                startMinuteData[i] = i + "";
                endMinuteData[i] = i + "";
            }
        }
        startTimeTv.setText(getStartTimeStr());
        endTimeTv.setText(getEndTimeStr());
    }

    private void initStartWv() {
        startYearAdapter = new DateTextAdapter(mContext, startYearData, Integer.parseInt(startYear) - calendar.get(Calendar.YEAR) + 100,
                maxSize, minSize);
        startYearWv.setVisibleItems(5);
        startYearWv.setViewAdapter(startYearAdapter);
        startYearWv.setCurrentItem(Integer.parseInt(startYear) - calendar.get(Calendar.YEAR) + 100);

        startMonthAdapter = new DateTextAdapter(mContext, startMonthData, Integer.parseInt(startMonth) - 1, maxSize, minSize);
        startMonthWv.setVisibleItems(5);
        startMonthWv.setViewAdapter(startMonthAdapter);
        startMonthWv.setCurrentItem(Integer.parseInt(startMonth) - 1);

        startDayAdapter = new DateTextAdapter(mContext, startDayData, Integer.parseInt(startDay) - 1, maxSize, minSize);
        startDayWv.setVisibleItems(5);
        startDayWv.setViewAdapter(startDayAdapter);
        startDayWv.setCurrentItem(Integer.parseInt(startDay) - 1);

        startHourAdapter = new DateTextAdapter(mContext, startHourData, Integer.parseInt(startHour), maxSize, minSize);
        startHourWv.setVisibleItems(5);
        startHourWv.setViewAdapter(startHourAdapter);
        startHourWv.setCurrentItem(Integer.parseInt(startHour));

        startMinuteAdapter = new DateTextAdapter(mContext, startMinuteData, Integer.parseInt(startMinute), maxSize, minSize);
        startMinuteWv.setVisibleItems(5);
        startMinuteWv.setViewAdapter(startMinuteAdapter);
        startMinuteWv.setCurrentItem(Integer.parseInt(startMinute));

        startYearWv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) startYearAdapter.getItemText(wheel.getCurrentItem());
                startYear = currentText;
                setTextViewSize(currentText, startYearAdapter);


                startDayData = getDays(Integer.parseInt(startYear), Integer.parseInt(startMonth));
                startDayAdapter = new DateTextAdapter(mContext, startDayData, 0, maxSize, minSize);
                startDayWv.setVisibleItems(5);
                startDayWv.setViewAdapter(startDayAdapter);
                startDayWv.setCurrentItem(0);
                setTextViewSize("0", startDayAdapter);

                startTimeTv.setText(getStartTimeStr());
            }
        });

        startYearWv.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) startYearAdapter.getItemText(wheel.getCurrentItem());
                setTextViewSize(currentText, startYearAdapter);

            }
        });

        startMonthWv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) startMonthAdapter.getItemText(wheel.getCurrentItem());
                startMonth = currentText;
                setTextViewSize(currentText, startMonthAdapter);

                startDayData = getDays(Integer.parseInt(startYear), Integer.parseInt(startMonth));
                startDayAdapter = new DateTextAdapter(mContext, startDayData, 0, maxSize, minSize);
                startDayWv.setVisibleItems(5);
                startDayWv.setViewAdapter(startDayAdapter);
                startDayWv.setCurrentItem(0);
                setTextViewSize("0", startDayAdapter);

                startTimeTv.setText(getStartTimeStr());
            }
        });

        startMonthWv.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) startMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextViewSize(currentText, startMonthAdapter);
            }
        });

        startDayWv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) startDayAdapter.getItemText(wheel.getCurrentItem());
                startDay = currentText;
                setTextViewSize(currentText, startDayAdapter);

                startTimeTv.setText(getStartTimeStr());
            }
        });

        startDayWv.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) startDayAdapter.getItemText(wheel.getCurrentItem());
                setTextViewSize(currentText, startDayAdapter);
            }
        });

        startHourWv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) startHourAdapter.getItemText(wheel.getCurrentItem());
//                startMinute = currentText;
                startHour = currentText;
                setTextViewSize(currentText, startHourAdapter);

                startTimeTv.setText(getStartTimeStr());
            }
        });

        startHourWv.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) startHourAdapter.getItemText(wheel.getCurrentItem());
                setTextViewSize(currentText, startHourAdapter);
            }
        });
        startMinuteWv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) startMinuteAdapter.getItemText(wheel.getCurrentItem());
                startMinute = currentText;
                setTextViewSize(currentText, startMinuteAdapter);

                startTimeTv.setText(getStartTimeStr());
            }
        });

        startMinuteWv.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) startMinuteAdapter.getItemText(wheel.getCurrentItem());
                setTextViewSize(currentText, startMinuteAdapter);
            }
        });
    }

    private void initEndWv() {
        endYearAdapter = new DateTextAdapter(mContext, endYearData, Integer.parseInt(endYear) - calendar.get(Calendar.YEAR) + 100,
                maxSize, minSize);
        endYearWv.setVisibleItems(5);
        endYearWv.setViewAdapter(endYearAdapter);
        endYearWv.setCurrentItem(Integer.parseInt(endYear) - calendar.get(Calendar.YEAR) + 100);

        endMonthAdapter = new DateTextAdapter(mContext, endMonthData, Integer.parseInt(endMonth) - 1, maxSize, minSize);
        endMonthWv.setVisibleItems(5);
        endMonthWv.setViewAdapter(endMonthAdapter);
        endMonthWv.setCurrentItem(Integer.parseInt(endMonth) - 1);

        endDayAdapter = new DateTextAdapter(mContext, endDayData, Integer.parseInt(endDay) - 1, maxSize, minSize);
        endDayWv.setVisibleItems(5);
        endDayWv.setViewAdapter(endDayAdapter);
        endDayWv.setCurrentItem(Integer.parseInt(endDay) - 1);

        endHourAdapter = new DateTextAdapter(mContext, endHourData, Integer.parseInt(endHour), maxSize, minSize);
        endHourWv.setVisibleItems(5);
        endHourWv.setViewAdapter(endHourAdapter);
        endHourWv.setCurrentItem(Integer.parseInt(endHour));

        endMinuteAdapter = new DateTextAdapter(mContext, endMinuteData, Integer.parseInt(endMinute), maxSize, minSize);
        endMinuteWv.setVisibleItems(5);
        endMinuteWv.setViewAdapter(endMinuteAdapter);
        endMinuteWv.setCurrentItem(Integer.parseInt(endMinute));

        endYearWv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) endYearAdapter.getItemText(wheel.getCurrentItem());
                endYear = currentText;
                setTextViewSize(currentText, endYearAdapter);


                endDayData = getDays(Integer.parseInt(endYear), Integer.parseInt(endMonth));
                endDayAdapter = new DateTextAdapter(mContext, endDayData, 0, maxSize, minSize);
                endDayWv.setVisibleItems(5);
                endDayWv.setViewAdapter(endDayAdapter);
                endDayWv.setCurrentItem(0);
                setTextViewSize("0", endDayAdapter);

                endTimeTv.setText(getEndTimeStr());
            }
        });

        endYearWv.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) endYearAdapter.getItemText(wheel.getCurrentItem());
                setTextViewSize(currentText, endYearAdapter);
            }
        });

        endMonthWv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) endMonthAdapter.getItemText(wheel.getCurrentItem());
                endMonth = currentText;
                setTextViewSize(currentText, endMonthAdapter);

                endDayData = getDays(Integer.parseInt(endYear), Integer.parseInt(endMonth));
                endDayAdapter = new DateTextAdapter(mContext, endDayData, 0, maxSize, minSize);
                endDayWv.setVisibleItems(5);
                endDayWv.setViewAdapter(endDayAdapter);
                endDayWv.setCurrentItem(0);
                setTextViewSize("0", endDayAdapter);

                endTimeTv.setText(getEndTimeStr());
            }
        });

        endMonthWv.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) endMonthAdapter.getItemText(wheel.getCurrentItem());
                setTextViewSize(currentText, endMonthAdapter);
            }
        });

        endDayWv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) endDayAdapter.getItemText(wheel.getCurrentItem());
                endDay = currentText;
                setTextViewSize(currentText, endDayAdapter);

                endTimeTv.setText(getEndTimeStr());
            }
        });

        endDayWv.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) endDayAdapter.getItemText(wheel.getCurrentItem());
                setTextViewSize(currentText, endDayAdapter);
            }
        });

        endHourWv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) endHourAdapter.getItemText(wheel.getCurrentItem());
//                startMinute = currentText;
                endHour = currentText;
                setTextViewSize(currentText, endHourAdapter);

                endTimeTv.setText(getEndTimeStr());
            }
        });

        endHourWv.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) endHourAdapter.getItemText(wheel.getCurrentItem());
                setTextViewSize(currentText, endHourAdapter);
            }
        });
        endMinuteWv.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) endMinuteAdapter.getItemText(wheel.getCurrentItem());
                endMinute = currentText;
                setTextViewSize(currentText, endMinuteAdapter);

                endTimeTv.setText(getEndTimeStr());
            }
        });

        endMinuteWv.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) endMinuteAdapter.getItemText(wheel.getCurrentItem());
                setTextViewSize(currentText, endMinuteAdapter);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == butSend) {
            log("send");
            if (null != onDateClickListener) {
                String startTimeStr = getStartTimeStr();
                String endTimeStr = getEndTimeStr();
                log(getStringDate());
                if (boo) {
                    if (TimeComparison(startTimeStr, getStringDate())) {
                        if (TimeComparison(endTimeStr, getStringDate())) {
                            if (TimeComparison(startTimeStr, endTimeStr)) {
                                log(startTimeStr + "\t" + endTimeStr);
                                onDateClickListener.onDateClickListener(startTimeStr, endTimeStr);
                                dismiss();
                            } else {
                                toast("结束时间需要大于开始时间");
                            }
                        } else {
                            toast("结束时间不能超过当前时间");
                        }
                    } else {
                        toast("开始时间不能超过当前时间");
                    }
                } else {
                    onDateClickListener.onDateClickListener(startTimeStr, endTimeStr);
                    dismiss();
                }
            }
        } else if (view == butCancel) {
            log("cancel");
            dismiss();
        } else if (view == startTimeLinear) {
            timeLinear.setBackgroundResource(R.mipmap.begin_time_bg);
            startLinear.setVisibility(View.VISIBLE);
            endLinear.setVisibility(View.GONE);
        } else if (view == endTimeLinear) {
            timeLinear.setBackgroundResource(R.mipmap.end_time_bg);
            startLinear.setVisibility(View.GONE);
            endLinear.setVisibility(View.VISIBLE);
        }
    }

    private String getStartTimeStr() {
        String startTimeStr = "";
        switch (selectType) {
            case HOUR:
                startTimeStr = startYear + "-" + startMonth + "-" + startDay;
                break;
            case MIN:
                startTimeStr = startYear + "-" + startMonth + "-" + startDay + " " + startHour;
                break;
            case NONE:
            default:
                startTimeStr = startYear + "-" + startMonth + "-" + startDay + " " + startHour + ":" + startMinute;
                break;
        }
        return startTimeStr;
    }

    private String getEndTimeStr() {
        String endTimeStr = "";
        switch (selectType) {
            case HOUR:
                endTimeStr = endYear + "-" + endMonth + "-" + endDay;
                break;
            case MIN:
                endTimeStr = endYear + "-" + endMonth + "-" + endDay + " " + endHour;
                break;
            case NONE:
            default:
                endTimeStr = endYear + "-" + endMonth + "-" + endDay + " " + endHour + ":" + endMinute;
                break;
        }
        return endTimeStr;
    }

    private <T extends View> T $(int id) {
        return mView.findViewById(id);
    }

    /**
     * 得到当前的时间
     *
     * @return 例如：2015-01-06 22:56
     */
    @SuppressLint("SimpleDateFormat")
    private String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter;
        switch (selectType) {
            case HOUR:
                formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                break;
            case MIN:
                formatter = new SimpleDateFormat("yyyy-MM-dd HH", Locale.CHINA);
                break;
            case NONE:
            default:
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                break;
        }
        return formatter.format(currentTime);
    }

    /**
     * 结束日期是否大于开始日期
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return true 表示为真
     */
    @SuppressLint("SimpleDateFormat")
    private boolean TimeComparison(String startTime, String endTime) {
        boolean mAfter = true;
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat formatter;
            switch (selectType) {
                case HOUR:
                    formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                    break;
                case MIN:
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH", Locale.CHINA);
                    break;
                case NONE:
                default:
                    formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                    break;
            }
            // 获得两个时间的毫秒时间差异
            Date start = formatter.parse(startTime);
            Date end = formatter.parse(endTime);
            if (start.getTime() > end.getTime()) {
                mAfter = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mAfter;
    }

    private class DateTextAdapter extends AbstractWheelTextAdapter1 {
        String[] list;

        protected DateTextAdapter(Context context, String[] list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_date, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public int getItemsCount() {
            return list.length;
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list[index];
        }
    }

    /**
     * 设置字体大小
     *
     * @param currieItemText
     * @param adapter
     */
    private void setTextViewSize(String currieItemText, DateTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textView = (TextView) arrayList.get(i);
            currentText = textView.getText().toString();
            if (currieItemText.equals(currentText)) {
                textView.setTextSize(14);
            } else {
                textView.setTextSize(12);
            }
        }
    }

    private String[] getDays(int year, int month) {
        String[] datas;
        Calendar newCal = Calendar.getInstance();
        // newCal.s
        newCal.set(Calendar.YEAR, year);
        newCal.set(Calendar.MONTH, month - 1);
        int count = newCal.getActualMaximum(Calendar.DAY_OF_MONTH);//new Date(year,month,0).getDate();

        Log.e(TAG, "getDays: " + year + ":" + month + ":" + count);
        datas = new String[count];
        for (int i = 0; i < count; i++) {
            if (i < 9) {
                datas[i] = "0" + (i + 1);
            } else {
                datas[i] = i + 1 + "";
            }
        }
        return datas;
    }

    private void log(Object obj) {
        if (isDebug){
            Log.v(TAG, obj.toString());
        }
    }

    private void toast(Object msg) {
        Toast.makeText(mContext, msg.toString(), Toast.LENGTH_SHORT).show();
    }

    public void setOnDateClickListener(OnDateClickListener onDateClickListener) {
        this.onDateClickListener = onDateClickListener;
    }

    public interface OnDateClickListener {
        void onDateClickListener(String startTime, String endTime);
    }
}
