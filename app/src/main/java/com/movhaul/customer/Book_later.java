package com.movhaul.customer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rey.material.widget.Button;
import com.sloop.fonts.FontsManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by sqindia on 26-10-2016.
 */

public class Book_later extends Activity {
    final static int DATE_PICKER_ID1 = 1111;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;
    Dialog dialog1;
    LinearLayout lt_goodsType, lt_truckType;
    com.rey.material.widget.LinearLayout btn_back;
    ImageView btn_time, btn_date;
    EditText et_time, et_date, et_goodsType, et_truckType, et_deliveryAddress, et_description;
    TextInputLayout flt_time, flt_date, flt_goodsType, flt_truckType, flt_deliveryAddress, flt_description;
    Button btn_post, btn_ok;
    int year;
    String pickup_location, drop_location, pick_lati, pick_long, drop_lati, drop_long;
    String truck, goods, id, token, str_time;
    int month;
    int day;
    ImageView btn_close;
    Typeface type;
    ArrayList<String> mdatas;
    HashSet<Uri> mMedia = new HashSet<Uri>();
    ArrayList<Uri> image_path = new ArrayList<>();
    String[] imagearray;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<Uri> image_uris;
    TextView jobtv1, jobtv2, jobtv3, jobtv4, msg, tv_snack;
    Snackbar snackbar;
    ProgressDialog mProgressDialog;
    Typeface tf;
    ArrayList<Uri> goods_imgs;
    ArrayList<String> ar_goods_type = new ArrayList<>();
    ArrayList<String> ar_truck_type = new ArrayList<>();
    ArrayList<String> ar_truck_sstype = new ArrayList<>();
    ArrayList<String> ar_truck_imgs = new ArrayList<>();
    ArrayList<String> ar_truck_imgs1 = new ArrayList<>();
    ArrayList<String> ar_truck_type1 = new ArrayList<>();
    ArrayList<String> ar_truck_sstype1 = new ArrayList<>();
    HashMap<String, String> hash_subtype;
    HashMap<String, String> hash_truck_imgs = new HashMap<String, String>();
    int min, max, i;
    String date, time;
    FrameLayout fl_goods;
    LinearLayout lt_images;
    String str_delivery_address, str_v_type, str_drop, str_goods_type, str_truck_type, str_desc, str_goods_pic, str_profile_img, book_time;
    ArrayList<String> selectedPhotos = new ArrayList<>();
    HashMap<String, String> hash_subtype1;
    HashMap<String, String> hash_truck_imgs1 = new HashMap<String, String>();
    String vec_type;

    private ViewGroup mSelectedImagesContainer;
    private DatePickerDialog.OnDateSetListener pickerListener1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            //view.setMinDate(System.currentTimeMillis() - 1000);
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            date = day + "/" + month + 1 + "/" + year;
            et_date.setText(new StringBuilder().append(day).append("/").append(month + 1).append("/").append(year).append(""));
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.book_later);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        type = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        mdatas = new ArrayList<>();
        btn_time = (ImageView) findViewById(com.movhaul.customer.R.id.iv_btn_time);
        btn_date = (ImageView) findViewById(com.movhaul.customer.R.id.iv_btn_date);
        // msg=(TextView) findViewById(R.id.msg);
        mSelectedImagesContainer = (ViewGroup) findViewById(com.movhaul.customer.R.id.selected_photos_container);
        et_time = (EditText) findViewById(com.movhaul.customer.R.id.editTextTime);
        et_goodsType = (EditText) findViewById(com.movhaul.customer.R.id.editTextGoodsType);
        et_date = (EditText) findViewById(com.movhaul.customer.R.id.editTextDate);
        et_truckType = (EditText) findViewById(com.movhaul.customer.R.id.editTextTruck_type);
        et_deliveryAddress = (EditText) findViewById(com.movhaul.customer.R.id.editTextDelieveryAddress);
        et_description = (EditText) findViewById(com.movhaul.customer.R.id.editTextDescription);

        lt_goodsType = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_goodstype);
        lt_truckType = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_truckType);

        btn_back = (com.rey.material.widget.LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);
        btn_post = (Button) findViewById(com.movhaul.customer.R.id.btn_post);

        flt_date = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_date);
        flt_time = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_time);
        flt_goodsType = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_goodstype);
        flt_truckType = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_trucktype);
        flt_deliveryAddress = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_deliveryaddress);
        flt_description = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_description);


        Intent get_data = getIntent();

        vec_type = get_data.getStringExtra("vec_type");


        Typeface type = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        flt_date.setTypeface(type);
        flt_time.setTypeface(type);
        flt_deliveryAddress.setTypeface(type);
        flt_goodsType.setTypeface(type);
        flt_truckType.setTypeface(type);
        flt_description.setTypeface(type);

        // Log.e("tag","s:"+System.currentTimeMillis());

        View getImages = findViewById(com.movhaul.customer.R.id.camera);
        mSelectedImagesContainer = (ViewGroup) findViewById(com.movhaul.customer.R.id.selected_photos_container);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Book_later.this);
        editor = sharedPreferences.edit();


        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");
        pickup_location = sharedPreferences.getString("pickup", "");
        drop_location = sharedPreferences.getString("drop", "");
        pick_lati = sharedPreferences.getString("pickup_lati", "");
        pick_long = sharedPreferences.getString("pickup_long", "");
        drop_lati = sharedPreferences.getString("drop_lati", "");
        drop_long = sharedPreferences.getString("drop_long", "");


        min = 2;
        max = 4;
        goods_imgs = new ArrayList<>();
        image_uris = new ArrayList<>();

        fl_goods = (FrameLayout) findViewById(com.movhaul.customer.R.id.frame_goodstype);
        lt_images = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_photos);


        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), "Network Error! Please Try Again Later.", Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);


        mProgressDialog = new ProgressDialog(Book_later.this, com.movhaul.customer.R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setMessage("Please wait");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);


        if (!com.movhaul.customer.Config.isConnected(Book_later.this)) {
            snackbar.show();
            tv_snack.setText("Please Connect Internet and Try again");
        } else {
            new fetch_goods().execute();
            new fetch_trucks().execute();
        }

        if (vec_type.equals("Bus")) {
            et_truckType.setCompoundDrawablesWithIntrinsicBounds(com.movhaul.customer.R.drawable.bus_type, 0, 0, 0);
            flt_truckType.setHint(getString(com.movhaul.customer.R.string.ad));
            flt_deliveryAddress.setHint(getString(com.movhaul.customer.R.string.ava));
            fl_goods.setVisibility(View.GONE);
            lt_images.setVisibility(View.GONE);
        } else if (vec_type.equals("Truck")) {
            et_truckType.setCompoundDrawablesWithIntrinsicBounds(com.movhaul.customer.R.drawable.select_truck_type, 0, 0, 0);
            flt_truckType.setHint(getString(com.movhaul.customer.R.string.adsf));
            flt_deliveryAddress.setHint(getString(com.movhaul.customer.R.string.aev));
            fl_goods.setVisibility(View.VISIBLE);
            lt_images.setVisibility(View.VISIBLE);
        }





        getImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("tag", "s:" + goods_imgs.size());
                Log.e("tag", "i:" + image_uris.size());

                if (goods_imgs.isEmpty()) {
                    min = 2;
                    max = 4;
                } else if (goods_imgs.size() == 3) {
                    min = 1;
                    max = 1;
                } else if (goods_imgs.size() == 2) {
                    min = 1;
                    max = 2;
                } else {
                    min = 1;
                    max = 3;
                }

                com.gun0912.tedpicker.Config config = new com.gun0912.tedpicker.Config();
                config.setSelectionMin(min);
                config.setSelectionLimit(max);
                config.setCameraHeight(com.movhaul.customer.R.dimen.app_camera_height);

                config.setCameraBtnBackground(com.movhaul.customer.R.drawable.round_dr_red);

                config.setToolbarTitleRes(com.movhaul.customer.R.string.custom_title);
                config.setSelectedBottomHeight(com.movhaul.customer.R.dimen.bottom_height);

                com.gun0912.tedpicker.ImagePickerActivity.setConfig(config);
                Intent intent = new Intent(Book_later.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
            }
        });

        lt_goodsType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goods_type();
            }
        });
        lt_truckType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (vec_type.equals("Bus")) {
                   /* ar_truck_imgs.clear();
                    ar_truck_type.clear();
                    hash_subtype.clear();
                    hash_truck_imgs.clear();
                    ar_truck_sstype.clear();

                    ar_truck_type = ar_truck_type1;
                    hash_subtype = hash_subtype1;
                    hash_truck_imgs = hash_truck_imgs1;
                    ar_truck_sstype = ar_truck_sstype1;
                    ar_truck_imgs = ar_truck_imgs1;
                    Log.e("tag","siz"+ar_truck_type.size());
                    Log.e("tag","si2z"+ar_truck_type1.size());*/
                    truck_type(ar_truck_type1, hash_subtype1, hash_truck_imgs1, ar_truck_sstype1, ar_truck_imgs1);

                } else {
                    truck_type(ar_truck_type, hash_subtype, hash_truck_imgs, ar_truck_sstype, ar_truck_imgs);

                }

                // truck_type();
            }
        });




        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(et_date.getText().toString().trim().isEmpty())) {
                    if (!(et_time.getText().toString().trim().isEmpty())) {
                        //if (!(et_deliveryAddress.getText().toString().trim().isEmpty())) {
                        if (!(et_truckType.getText().toString().trim().isEmpty())) {
                            //if (!(et_description.getText().toString().trim().isEmpty())) {

                            if (vec_type.equals("Truck")) {
                            if (!(et_goodsType.getText().toString().trim().isEmpty())) {
                                str_goods_type = et_goodsType.getText().toString();
                                str_truck_type = et_truckType.getText().toString();
                                str_time = date + " T " + time;
                                new book_later_task().execute();
                            } else {
                                Toast.makeText(getApplicationContext(), "Choose Goods Type", Toast.LENGTH_LONG).show();
                            }
                            } else {

                                //str_delivery_address = et_delivery_address.getText().toString();
                                str_truck_type = et_truckType.getText().toString();
                                str_time = date + " T " + time;
                                new book_later_task().execute();

                            }

                               /* } else {
                                    et_description.setError("Enter Description");
                                    et_description.requestFocus();
                                }*/
                        } else {

                            Toast.makeText(getApplicationContext(), "Choose " + vec_type + " Type", Toast.LENGTH_LONG).show();
                        }


                       /* } else {
                            et_deliveryAddress.setError("Enter Delivery Address");
                            et_deliveryAddress.requestFocus();
                        }*/

                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select Time", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Choose Date First", Toast.LENGTH_LONG).show();
                }


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

                        Log.e("tag", "time:" + selectedHour + selectedMinute);

                        time = selectedHour + ":" + selectedMinute + ":00";
                        updateTime(selectedHour, selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


            }
        });

        final Calendar c1 = Calendar.getInstance();
        year = c1.get(Calendar.YEAR);
        month = c1.get(Calendar.MONTH);
        day = c1.get(Calendar.DAY_OF_MONTH);


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_PICKER_ID1);
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent i = new Intent(Book_later.this, DashboardNavigation.class);
                //startActivity(i);
                finish();
            }
        });

        dialog1 = new Dialog(Book_later.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(com.movhaul.customer.R.layout.dialogue_job_posting);
        btn_ok = (Button) dialog1.findViewById(com.movhaul.customer.R.id.button_ok);
        btn_close = (ImageView) dialog1.findViewById(com.movhaul.customer.R.id.button_close);
        jobtv1 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_1);
        jobtv2 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_2);
        jobtv3 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_3);
        jobtv4 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_4);
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
        btn_ok.setTypeface(type);
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, pickerListener1, year, month, day + 1);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() + 100000);


                // return new DatePickerDialog(this, pickerListener1, year, month, day);
                return datePickerDialog;

        }
        return null;
    }

    private void goods_type() {

        Dialog_GoodsType dialog_goodsType = new Dialog_GoodsType(Book_later.this, ar_goods_type);
        dialog_goodsType.getWindow().setBackgroundDrawable(getResources().getDrawable(com.movhaul.customer.R.drawable.choose));
        dialog_goodsType.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_goodsType.show();
        dialog_goodsType.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!(sharedPreferences.getString("goods", "").equals(""))) {
                    et_goodsType.setText(sharedPreferences.getString("goods", ""));
                }
            }
        });
    }

  /*  private void truck_type() {

        Dialog_VehicleType dialog_vehicleType = new Dialog_VehicleType(Book_later.this, ar_truck_type, hash_subtype, hash_truck_imgs, ar_truck_sstype, ar_truck_imgs);
        dialog_vehicleType.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.choose));
        dialog_vehicleType.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_vehicleType.show();
        dialog_vehicleType.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!(sharedPreferences.getString("sub_truck_type", "").equals(""))) {
                    et_truckType.setText(sharedPreferences.getString("sub_truck_type", ""));
                }
            }
        });
    }*/

    private void truck_type(ArrayList<String> ar_truck_typea, HashMap<String, String> hash_subtypea, HashMap<String, String> hash_truck_imgsa, ArrayList<String> ar_truck_sstypea, ArrayList<String> ar_truck_imgsa) {
        Dialog_VehicleType dialog_vehicleType = new Dialog_VehicleType(Book_later.this, ar_truck_typea, hash_subtypea, hash_truck_imgsa, ar_truck_sstypea, ar_truck_imgsa);
        dialog_vehicleType.getWindow().setBackgroundDrawable(getResources().getDrawable(com.movhaul.customer.R.drawable.choose));
        dialog_vehicleType.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // dialog_vehicleType.getWindow().getAttributes().height= 120;
        dialog_vehicleType.getWindow().getAttributes().windowAnimations = com.movhaul.customer.R.style.MaterialDialogSheetAnimation;
        dialog_vehicleType.show();
        dialog_vehicleType.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!(sharedPreferences.getString("sub_truck_type", "").equals(""))) {
                    et_truckType.setText(sharedPreferences.getString("sub_truck_type", ""));
                    str_v_type = sharedPreferences.getString("truck_type", "");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Intent i = new Intent(Book_later.this, DashboardNavigation.class);
        // startActivity(i);
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


    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (requestCode == INTENT_REQUEST_GET_IMAGES && resuleCode == Activity.RESULT_OK) {

            image_uris = intent.getParcelableArrayListExtra(com.gun0912.tedpicker.ImagePickerActivity.EXTRA_IMAGE_URIS);
            Log.e("tag", "12345" + image_uris);

            if (image_uris != null) {
                showMedia();
            }
        }
    }

    private void showMedia() {
        // Remove all views before
        // adding the new ones.

        if (goods_imgs.isEmpty()) {
            goods_imgs = image_uris;
        } else {

            if (goods_imgs.size() > 3) {
                for (int i = 0; i < image_uris.size(); i++) {
                    goods_imgs.add(image_uris.get(i));
                }
            } else if (goods_imgs.size() > 2) {

                for (int i = 0; i < image_uris.size(); i++) {
                    goods_imgs.add(image_uris.get(i));
                }
            } else {

                for (int i = 0; i < image_uris.size(); i++) {
                    goods_imgs.add(image_uris.get(i));
                }
            }
        }


        mSelectedImagesContainer.removeAllViews();
        if (goods_imgs.size() >= 1) {
            mSelectedImagesContainer.setVisibility(View.VISIBLE);
        }

        int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());


        for (i = 0; i < goods_imgs.size(); i++) {

            final View imageHolder = LayoutInflater.from(this).inflate(com.movhaul.customer.R.layout.image_item, null);
            final ImageView thumbnail = (ImageView) imageHolder.findViewById(com.movhaul.customer.R.id.media_image);

            Log.e("tag", "s:" + goods_imgs.get(i).toString());

            final String pa = goods_imgs.get(i).toString();

            Glide.with(this)
                    .load(goods_imgs.get(i).toString())
                    .fitCenter()
                    .into(thumbnail);
            mSelectedImagesContainer.addView(imageHolder);
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));


            imageHolder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    mSelectedImagesContainer.removeView(view);

                    for (int ik = 0; ik < goods_imgs.size(); ik++) {
                        if (goods_imgs.get(ik).toString().equals(pa)) {
                            goods_imgs.remove(ik);
                        }
                    }

                    return false;
                }
            });

      /*     imageHolder.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   Log.e("tag","as:"+pa);
                   Log.e("tag","count_before"+mSelectedImagesContainer.getChildCount());

                   max = mSelectedImagesContainer.getChildCount();
                   Log.e("tag",max+"count_after"+mSelectedImagesContainer.getChildCount());
               }
           });*/


        }

    }


    class fetch_goods extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            try {
                String virtual_url = com.movhaul.customer.Config.WEB_URL + "customer/goodstype";
                //  Log.e("tag","url: "+virtual_url);
                JSONObject jsonobject = HttpUtils.getData(virtual_url, id, token);
                // Log.e("tag_", "0" + jsonobject.toString());
                if (jsonobject.toString() == "sam") {
                    //  Log.e("tag_", "1" + jsonobject.toString());
                }
                json = jsonobject.toString();
                return json;
            } catch (Exception e) {
                //  Log.e("InputStream", "" + e.getLocalizedMessage());
                jsonStr = "";
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            // Log.e("tag", "<-----rerseres---->" + jsonStr);
            super.onPostExecute(jsonStr);
            mProgressDialog.dismiss();
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                //  String count = jo.getString("count");
                if (status.equals("true")) {
                    JSONArray goods_data = jo.getJSONArray("goods_type");
                    if (goods_data.length() > 0) {
                        for (int i = 0; i < goods_data.length(); i++) {
                            String datas = goods_data.getString(i);
                            // Log.e("tag","s: "+datas);
                            ar_goods_type.add(datas);
                        }
                    } else {
                    }
                } else {
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    class fetch_trucks extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            try {
                String virtual_url = com.movhaul.customer.Config.WEB_URL + "customer/vehicletype";
                //Log.e("tag","url: "+virtual_url);
                JSONObject jsonobject = HttpUtils.getData(virtual_url, id, token);
                // Log.e("tag_", "0" + jsonobject.toString());
                if (jsonobject.toString() == "sam") {
                    //     Log.e("tag_", "1" + jsonobject.toString());
                }
                json = jsonobject.toString();
                return json;
            } catch (Exception e) {
                //  Log.e("InputStream", "" + e.getLocalizedMessage());
                jsonStr = "";
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            Log.e("tag", "<-----rerseres---->" + jsonStr);
            super.onPostExecute(jsonStr);
            mProgressDialog.dismiss();
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                if (status.equals("true")) {
                    JSONArray truck_data = jo.getJSONArray("message");
                    if (truck_data.length() > 0) {

                        hash_subtype = new HashMap<String, String>();
                        hash_subtype1 = new HashMap<String, String>();
                        for (int i = 0; i < truck_data.length(); i++) {
                            String datas = truck_data.getString(i);
                            JSONObject subs = new JSONObject(datas);

                            if (subs.getString("vehicle_type").contains("truck")) {
                                ar_truck_type.add(subs.getString("vehicle_main_type"));
                                ar_truck_sstype.add(subs.getString("vehicle_sub_type"));
                                ar_truck_imgs.add(subs.getString("vehicle_image"));
                                hash_subtype.put(subs.getString("vehicle_sub_type"), subs.getString("vehicle_main_type"));
                                hash_truck_imgs.put(subs.getString("vehicle_image"), subs.getString("vehicle_main_type"));
                            } else if (subs.getString("vehicle_type").contains("bus")) {
                                ar_truck_type1.add(subs.getString("vehicle_main_type"));
                                ar_truck_sstype1.add(subs.getString("vehicle_sub_type"));
                                ar_truck_imgs1.add(subs.getString("vehicle_image"));
                                hash_subtype1.put(subs.getString("vehicle_sub_type"), subs.getString("vehicle_main_type"));
                                hash_truck_imgs1.put(subs.getString("vehicle_image"), subs.getString("vehicle_main_type"));
                            }


                        }
                    } else {
                    }
                } else {
                }
                Log.e("tag", "trk_siz_img" + ar_truck_imgs.size());
                Log.e("tag", "bus_siz" + ar_truck_type1.size());
                Log.e("tag", "bus_siz_img" + ar_truck_imgs1.size());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public class book_later_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "reg_preexe");
            mProgressDialog.show();


            if ((!(et_description.getText().toString().trim().isEmpty()))) {
                str_desc = et_description.getText().toString().trim();
            }

            if ((!(et_deliveryAddress.getText().toString().trim().isEmpty())))
                str_delivery_address = et_deliveryAddress.getText().toString();

        }

        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";

            if (goods_imgs.size() > 0) {

                Log.e("tag", "p : " + pickup_location);
                Log.e("tag", "pqr :" + drop_location);


                try {
                    //driver/driverupdate
                    String responseString = null;
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(com.movhaul.customer.Config.WEB_URL + "customer/booking");
                    httppost.setHeader("id", id);
                    httppost.setHeader("sessiontoken", token);
                    httppost.setHeader("pickup_location", pickup_location);
                    httppost.setHeader("drop_location", drop_location);

                    if (vec_type.equals("Truck")) {
                        httppost.setHeader("goods_type", str_goods_type);
                    } else {
                        httppost.setHeader("goods_type", "passenger");
                    }
                    //httppost.setHeader("truck_type", str_truck_type);
                    httppost.setHeader("vehicle_type", vec_type);
                    httppost.setHeader("vehicle_main_type", str_v_type);
                    httppost.setHeader("vehicle_sub_type", str_truck_type);
                    httppost.setHeader("booking_time", str_time);
                    httppost.setHeader("pickup_latitude", pick_lati);
                    httppost.setHeader("pickup_longitude", pick_long);
                    httppost.setHeader("drop_latitude", drop_lati);
                    httppost.setHeader("drop_longitude", drop_long);

                    if (str_desc != null)
                        httppost.setHeader("description", str_desc);

                    if (str_delivery_address != null)
                        httppost.setHeader("delivery_address", str_delivery_address);

                    HttpResponse response = null;
                    HttpEntity r_entity = null;


                    try {
                        Log.e("tag0", "img: if " + str_profile_img);
                        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);


                        for (Uri images : goods_imgs)
                            entity.addPart("bookinggoods", new FileBody(new File(images.toString()), "image/jpeg"));

                        /*for(int i =0;i<=1;i++) {

                            entity.addPart("bookinggoods", new FileBody(new File(goods_imgs.get(i).toString()), "image/jpeg"));
                        }*/

                        httppost.setEntity(entity);

                        try {
                            response = httpclient.execute(httppost);
                        } catch (Exception e) {
                            Log.e("tag", "ds:" + e.toString());
                        }


                        try {
                            r_entity = response.getEntity();
                        } catch (Exception e) {
                            Log.e("tag", "dsa:" + e.toString());
                        }


                        int statusCode = response.getStatusLine().getStatusCode();
                        Log.e("tag1", response.getStatusLine().toString());
                        if (statusCode == 200) {
                            responseString = EntityUtils.toString(r_entity);
                            Log.e("tag2", responseString);
                        } else {
                            responseString = "Error occurred! Http Status Code: "
                                    + statusCode;
                            Log.e("tag3", responseString);
                        }
                    } catch (ClientProtocolException e) {
                        responseString = e.toString();
                        Log.e("tag44", responseString);
                    } catch (IOException e) {
                        responseString = e.toString();
                        Log.e("tag45", responseString);
                    }
                    return responseString;
                } catch (Exception e) {
                    Log.e("tag_InputStream0", e.getLocalizedMessage());
                }
                return null;

            } else {
                Log.e("tag", "no poto");

                String s = "";
                JSONObject jsonObject = new JSONObject();
                try {


                    jsonObject.put("pickup_location", sharedPreferences.getString("pickup", ""));
                    jsonObject.put("drop_location", sharedPreferences.getString("drop", ""));
                    if (vec_type.equals("Truck")) {
                        jsonObject.put("goods_type", str_goods_type);
                    } else {
                        jsonObject.put("goods_type", "passenger");
                    }
                    //   jsonObject.put("truck_type", str_truck_type);
                    jsonObject.put("vehicle_type", vec_type);
                    jsonObject.put("vehicle_main_type", str_v_type);
                    jsonObject.put("vehicle_sub_type", str_truck_type);

                    jsonObject.put("booking_time", str_time);
                    jsonObject.put("pickup_latitude", pick_lati);
                    jsonObject.put("pickup_longitude", pick_long);
                    jsonObject.put("drop_latitude", drop_lati);
                    jsonObject.put("drop_longitude", drop_long);

                    if (str_desc != null) {
                        jsonObject.put("description", str_desc);
                    }
                    if (str_delivery_address != null)
                        jsonObject.put("delivery_address", str_delivery_address);


                    json = jsonObject.toString();

                    return s = HttpUtils.makeRequest1(com.movhaul.customer.Config.WEB_URL + "customer/booking", json, id, token);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;

            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("tag", "tag" + s);
            mProgressDialog.dismiss();

            if (s != null) {
                try {
                    JSONObject jo = new JSONObject(s);
                    String status = jo.getString("status");

                    Log.d("tag", "<-----Status----->" + status);
                    if (status.equals("true")) {

                        String msg = jo.getString("message");
                        String bookingid = jo.getString("booking_id");

                        Log.e("tag", "Location Updated");

                        editor.putString("job_id", bookingid);
                        editor.putString("book_time", book_time);
                        editor.commit();

                        Intent goReve = new Intent(getApplicationContext(), Job_review.class);
                        startActivity(goReve);
                        finish();

                    } else if (status.equals("false")) {

                        Log.e("tag", "Location not updated");
                        //has to check internet and location...
                        Toast.makeText(getApplicationContext(), "Network Errror. Please Try Again Later", Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    Toast.makeText(getApplicationContext(), "Network Errror. Please Try Again Later", Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(getApplicationContext(),"Network Errror1",Toast.LENGTH_LONG).show();
            }

        }

    }


}
