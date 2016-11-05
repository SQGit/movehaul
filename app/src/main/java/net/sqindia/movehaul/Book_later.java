package net.sqindia.movehaul;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.rey.material.widget.Button;
import com.sloop.fonts.FontsManager;

import java.util.Calendar;

/**
 * Created by sqindia on 26-10-2016.
 */

public class Book_later extends Activity {
    Dialog dialog1;
    String truck, goods;
    LinearLayout lt_goodsType, lt_truckType;
    com.rey.material.widget.LinearLayout btn_back;
    ImageView btn_time, btn_date;
    EditText et_time, et_date, et_goodsType, et_truckType, et_deliveryAddress, et_description;
    TextInputLayout flt_time, flt_date, flt_goodsType, flt_truckType, flt_deliveryAddress, flt_description;
    Button btn_post,btn_ok;
    final static int DATE_PICKER_ID1 = 1111;
    int year;
    int month;
    int day;
    ImageView btn_close;
    TextView jobtv1,jobtv2,jobtv3,jobtv4;
    Typeface type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_later);
        FontsManager.initFormAssets(this, "fonts/CATAMARAN-REGULAR.TTF");       //initialization
        FontsManager.changeFonts(this);
        type = Typeface.createFromAsset(getAssets(), "fonts/CATAMARAN-REGULAR.TTF");

        btn_time = (ImageView) findViewById(R.id.iv_btn_time);
        btn_date = (ImageView) findViewById(R.id.iv_btn_date);

        et_time = (EditText) findViewById(R.id.editTextTime);
        et_goodsType = (EditText) findViewById(R.id.editTextGoodsType);
        et_date = (EditText) findViewById(R.id.editTextDate);
        et_truckType = (EditText) findViewById(R.id.editTextTruck_type);
        et_deliveryAddress = (EditText) findViewById(R.id.editTextDelieveryAddress);
        et_description = (EditText) findViewById(R.id.editTextDescription);

        lt_goodsType = (LinearLayout) findViewById(R.id.layout_goodstype);
        lt_truckType = (LinearLayout) findViewById(R.id.layout_truckType);

        btn_back = (com.rey.material.widget.LinearLayout) findViewById(R.id.layout_back);
        btn_post = (Button) findViewById(R.id.btn_post);

        flt_date = (TextInputLayout) findViewById(R.id.float_date);
        flt_time = (TextInputLayout) findViewById(R.id.float_time);
        flt_goodsType = (TextInputLayout) findViewById(R.id.float_goodstype);
        flt_truckType = (TextInputLayout) findViewById(R.id.float_trucktype);
        flt_deliveryAddress = (TextInputLayout) findViewById(R.id.float_deliveryaddress);
        flt_description = (TextInputLayout) findViewById(R.id.float_description);


        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/CATAMARAN-REGULAR.TTF");
        flt_date.setTypeface(type);
        flt_time.setTypeface(type);
        flt_deliveryAddress.setTypeface(type);
        flt_goodsType.setTypeface(type);
        flt_truckType.setTypeface(type);
        flt_description.setTypeface(type);


        lt_goodsType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //et_goodsType.setText("Computer Systems");
                goods_type();
            }
        });
        lt_truckType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // et_truckType.setText("Heavy Truck");
                truck_type();
            }
        });



        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Book_later.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                       // et_time.setText( selectedHour + ":" + selectedMinute);
                        updateTime(selectedHour,selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

               /* et_time.setText("23:35:00");
                Log.e("tag", "ttt");*/
            }
        });




//Getting current date//
        final Calendar c1 = Calendar.getInstance();
        year = c1.get(Calendar.YEAR);
        month = c1.get(Calendar.MONTH);
        day = c1.get(Calendar.DAY_OF_MONTH);
        // dt = cal.toLocaleString();
        //  txtDate.setText(dt.toString());
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_PICKER_ID1);
                // et_date.setText("23-10-2016");
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Book_later.this, DashboardNavigation.class);
                startActivity(i);
                finish();
            }
        });
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.show();
            }
        });
        dialog1 = new Dialog(Book_later.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.dialogue_job_posting);
        btn_ok = (Button) dialog1.findViewById(R.id.button_ok);
        btn_close = (ImageView) dialog1.findViewById(R.id.button_close);
        jobtv1 = (TextView) dialog1.findViewById(R.id.textView_1);
        jobtv2 = (TextView) dialog1.findViewById(R.id.textView_2);
        jobtv3 = (TextView) dialog1.findViewById(R.id.textView_3);
        jobtv4 = (TextView) dialog1.findViewById(R.id.textView_4);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        jobtv1.setTypeface(type);
        jobtv2.setTypeface(type);
        jobtv3.setTypeface(type);
        jobtv4.setTypeface(type);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(Book_later.this, Job_review.class);
                startActivity(i);
                finish();
            }
        });

    }


    // Used to convert 24hr format to 12hr format with AM/PM values
    private void updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        et_time.setText(aTime);
    }



    @Override

    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_PICKER_ID1:
                return new DatePickerDialog(this, pickerListener1, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            et_date.setText(new StringBuilder().append(day).append("-").append(month + 1).append("-").append(year).append(""));
        }
    };


    private void truck_type() {

        LayoutInflater layoutInflater = LayoutInflater.from(Book_later.this);
        View promptView = layoutInflater.inflate(R.layout.truck_selecting, null);
        final AlertDialog alertD = new AlertDialog.Builder(Book_later.this).create();
        alertD.setCancelable(true);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00FFFFFF")));


        final RadioGroup radioGroup = (RadioGroup) promptView.findViewById(R.id.radioGroup2);
        final RadioButton rb_truck1 = (RadioButton) promptView.findViewById(R.id.radio1);
        final RadioButton rb_truck2 = (RadioButton) promptView.findViewById(R.id.radio2);
        final RadioButton rb_truck3 = (RadioButton) promptView.findViewById(R.id.radio3);
        final RadioButton rb_truck4 = (RadioButton) promptView.findViewById(R.id.radio4);
        final RadioButton rb_truck5 = (RadioButton) promptView.findViewById(R.id.radio5);


        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/CATAMARAN-REGULAR.TTF");
        rb_truck1.setTypeface(tf);
        rb_truck2.setTypeface(tf);
        rb_truck3.setTypeface(tf);
        rb_truck4.setTypeface(tf);
        rb_truck5.setTypeface(tf);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertD.isShowing()) {
                    alertD.dismiss();
                }
            }
        };


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (checkedId == R.id.radio1) {
                    truck = "Heavy Truck, 8 Ton";
                } else if (checkedId == R.id.radio2) {
                    truck = "Light Truck, 1 Ton";
                } else if (checkedId == R.id.radio3) {
                    truck = "Lift Truck, 1 Ton";
                } else if (checkedId == R.id.radio4) {
                    truck = "Flatbed Truck, 3 Ton";
                } else if (checkedId == R.id.radio5) {
                    truck = "Openroof Truck, 5 Ton";
                }


                Log.e("TAG", "asd" + truck);
                //alertD.dismiss();
                handler.postDelayed(runnable, 700);
                et_truckType.setText(truck);
            }

        });
        alertD.setView(promptView);
        alertD.show();
    }

    private void goods_type() {

        LayoutInflater layoutInflater = LayoutInflater.from(Book_later.this);
        View promptView = layoutInflater.inflate(R.layout.goods_selecting, null);
        final AlertDialog alertD = new AlertDialog.Builder(Book_later.this).create();
        alertD.setCancelable(true);
        Window window = alertD.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        alertD.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#00FFFFFF")));


        final RadioGroup radioGroup = (RadioGroup) promptView.findViewById(R.id.radioGroup2);
        final RadioButton rb_goods1 = (RadioButton) promptView.findViewById(R.id.radio1);
        final RadioButton rb_goods2 = (RadioButton) promptView.findViewById(R.id.radio2);
        final RadioButton rb_goods3 = (RadioButton) promptView.findViewById(R.id.radio3);
        final RadioButton rb_goods4 = (RadioButton) promptView.findViewById(R.id.radio4);
        final RadioButton rb_goods5 = (RadioButton) promptView.findViewById(R.id.radio5);
        final RadioButton rb_goods6 = (RadioButton) promptView.findViewById(R.id.radio6);


        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/CATAMARAN-REGULAR.TTF");
        rb_goods1.setTypeface(tf);
        rb_goods2.setTypeface(tf);
        rb_goods3.setTypeface(tf);
        rb_goods4.setTypeface(tf);
        rb_goods5.setTypeface(tf);
        rb_goods6.setTypeface(tf);

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alertD.isShowing()) {
                    alertD.dismiss();
                }
            }
        };


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if (checkedId == R.id.radio1) {
                    goods = "Wooden boxes";
                } else if (checkedId == R.id.radio2) {
                    goods = "Hard steel";
                } else if (checkedId == R.id.radio3) {
                    goods = "Cardboard boxes";
                } else if (checkedId == R.id.radio4) {
                    goods = "Computers systems";
                } else if (checkedId == R.id.radio5) {
                    goods = "House Furniture";
                } else if (checkedId == R.id.radio6) {
                    goods = "Company items";
                }


                Log.e("TAG", "asd" + truck);
                //alertD.dismiss();
                handler.postDelayed(runnable, 700);
                et_goodsType.setText(goods);
            }

        });
        alertD.setView(promptView);
        alertD.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Book_later.this, DashboardNavigation.class);
        startActivity(i);
        finish();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
