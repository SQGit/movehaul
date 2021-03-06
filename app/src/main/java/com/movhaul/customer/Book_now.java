package com.movhaul.customer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by sqindia on 25-10-2016.
 * book now
 * customer current shipments has to book here.
 */

@SuppressWarnings("deprecation")
public class Book_now extends Activity {

    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    public int i;
    String id, token, str_time;
    LinearLayout lt_goodsType, lt_truckType;
    EditText et_delivery_address, et_goodstype, et_trucktype, et_description;
    TextInputLayout flt_delivery_address, flt_goodstype, flt_trucktype, flt_description;
    com.rey.material.widget.LinearLayout btn_back;
    Button btn_post, btn_ok;
    Dialog dialog1;
    ImageView btn_close;
    TextView jobtv1, jobtv2, jobtv3, jobtv4, tv_snack;
    String pickup_location, drop_location, pick_lati, pick_long, drop_lati, drop_long;
    ArrayList<String> mdatas;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Snackbar snackbar;
    Typeface tf;
    int min, max;
    ArrayList<String> ar_goods_type = new ArrayList<>();
    ArrayList<String> ar_truck_type = new ArrayList<>();
    ArrayList<String> ar_truck_sstype = new ArrayList<>();
    ArrayList<String> ar_truck_imgs1 = new ArrayList<>();
    ArrayList<String> ar_truck_type1 = new ArrayList<>();
    ArrayList<String> ar_truck_sstype1 = new ArrayList<>();
    ArrayList<String> ar_truck_imgs = new ArrayList<>();
    HashMap<String, String> hash_subtype;
    HashMap<String, String> hash_truck_imgs = new HashMap<>();
    HashMap<String, String> hash_subtype1;
    HashMap<String, String> hash_truck_imgs1 = new HashMap<>();
    ProgressDialog mProgressDialog;
    LinearLayout lt_images;
    String str_delivery_address, str_v_type, str_goods_type, str_truck_type, str_desc, str_profile_img, book_time;
    ArrayList<Uri> image_uris;
    ArrayList<Uri> goods_imgs;
    FrameLayout fl_goods;
    String vec_type;
    private ViewGroup mSelectedImagesContainer;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.movhaul.customer.R.layout.book_now);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");
        FontsManager.changeFonts(this);
        Intent get_data = getIntent();
        //customer chooses service whether truck,bus or roadside assistance.
        vec_type = get_data.getStringExtra("vec_type");
        mdatas = new ArrayList<>();
        tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        et_delivery_address = (EditText) findViewById(com.movhaul.customer.R.id.editTextDelieveryAddress);
        et_goodstype = (EditText) findViewById(com.movhaul.customer.R.id.editTextGoodsType);
        et_trucktype = (EditText) findViewById(com.movhaul.customer.R.id.editTextTruck_type);
        et_description = (EditText) findViewById(com.movhaul.customer.R.id.editTextDescription);
        flt_delivery_address = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_deliveryaddress);
        flt_goodstype = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_goodstype);
        flt_trucktype = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_trucktype);
        flt_description = (TextInputLayout) findViewById(com.movhaul.customer.R.id.float_description);
        lt_goodsType = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_goodstype);
        lt_truckType = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_truckType);
        btn_back = (com.rey.material.widget.LinearLayout) findViewById(com.movhaul.customer.R.id.layout_back);
        btn_post = (Button) findViewById(com.movhaul.customer.R.id.btn_post);
        flt_delivery_address.setTypeface(tf);
        flt_goodstype.setTypeface(tf);
        flt_trucktype.setTypeface(tf);
        flt_description.setTypeface(tf);
        min = 2;
        max = 4;
        goods_imgs = new ArrayList<>();
        image_uris = new ArrayList<>();
        View getImages = findViewById(com.movhaul.customer.R.id.camera);
        mSelectedImagesContainer = (ViewGroup) findViewById(com.movhaul.customer.R.id.selected_photos_container);
        fl_goods = (FrameLayout) findViewById(com.movhaul.customer.R.id.frame_goodstype);
        lt_images = (LinearLayout) findViewById(com.movhaul.customer.R.id.layout_photos);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Book_now.this);
        editor = sharedPreferences.edit();
        id = sharedPreferences.getString("id", "");
        token = sharedPreferences.getString("token", "");
        pickup_location = sharedPreferences.getString("pickup", "");
        drop_location = sharedPreferences.getString("drop", "");
        pick_lati = sharedPreferences.getString("pickup_lati", "");
        pick_long = sharedPreferences.getString("pickup_long", "");
        drop_lati = sharedPreferences.getString("drop_lati", "");
        drop_long = sharedPreferences.getString("drop_long", "");
        Log.e("tag", "id: " + id);
        Log.e("tag", "tok: " + token);
        Log.e("tag", "type: " + vec_type);
        Log.e("tag", "pick: " + pickup_location);
        Log.e("tag", "drop: " + drop_location);
        Log.e("tag", "pi_l: " + pick_lati + ":::" + pick_long);
        Log.e("tag", "dr_l: " + drop_lati + ":::" + drop_long);
        //snack bar
        snackbar = Snackbar
                .make(findViewById(com.movhaul.customer.R.id.top), com.movhaul.customer.R.string.no_internet, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        tv_snack = (android.widget.TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv_snack.setTextColor(Color.WHITE);
        tv_snack.setTypeface(tf);
        //progress dialog
        mProgressDialog = new ProgressDialog(Book_now.this, com.movhaul.customer.R.style.AppCompatAlertDialogStyle);
        mProgressDialog.setTitle(com.movhaul.customer.R.string.loading);
        mProgressDialog.setMessage(getString(com.movhaul.customer.R.string.wait));
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        if (!com.movhaul.customer.Config.isConnected(Book_now.this)) {
            snackbar.show();
            tv_snack.setText(com.movhaul.customer.R.string.please_try_again);
        } else {
            //fetch goods and truck types from server.
            new fetch_goods().execute();
            new fetch_trucks().execute();
        }
        final Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        String[] parts = formattedDate.split(" ");
        String part1 = parts[0]; // 004
        String part2 = parts[1]; // 034556
        book_time = parts[1];
        str_time = part1 + " T " + part2;
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
                    truck_type(ar_truck_type1, hash_subtype1, hash_truck_imgs1, ar_truck_sstype1, ar_truck_imgs1);
                } else {
                    truck_type(ar_truck_type, hash_subtype, hash_truck_imgs, ar_truck_sstype, ar_truck_imgs);
                }
            }
        });
        if (vec_type.equals("Bus")) {
            et_trucktype.setCompoundDrawablesWithIntrinsicBounds(com.movhaul.customer.R.drawable.bus_type, 0, 0, 0);
            flt_trucktype.setHint(getString(com.movhaul.customer.R.string.ad));
            flt_delivery_address.setHint(getString(com.movhaul.customer.R.string.ava));
            fl_goods.setVisibility(View.GONE);
            lt_images.setVisibility(View.GONE);
        } else if (vec_type.equals("Truck")) {
            et_trucktype.setCompoundDrawablesWithIntrinsicBounds(com.movhaul.customer.R.drawable.select_truck_type, 0, 0, 0);
            flt_trucktype.setHint(getString(com.movhaul.customer.R.string.adsf));
            flt_delivery_address.setHint(getString(com.movhaul.customer.R.string.aev));
            fl_goods.setVisibility(View.VISIBLE);
            lt_images.setVisibility(View.VISIBLE);
        }
        getImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                Config config = new Config();
                config.setSelectionMin(min);
                config.setSelectionLimit(max);
                config.setCameraHeight(com.movhaul.customer.R.dimen.app_camera_height);
                config.setCameraBtnBackground(com.movhaul.customer.R.drawable.round_dr_red);
                config.setToolbarTitleRes(com.movhaul.customer.R.string.custom_title);
                config.setSelectedBottomHeight(com.movhaul.customer.R.dimen.bottom_height);
                ImagePickerActivity.setConfig(config);
                Intent intent = new Intent(Book_now.this, com.gun0912.tedpicker.ImagePickerActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if (!(et_delivery_address.getText().toString().trim().isEmpty())) {
                    if (!(et_trucktype.getText().toString().trim().isEmpty()) && !et_trucktype.getText().toString().contains("Bus Type") && !et_trucktype.getText().toString().contains("Truck Type")) {
                      //  if (!(et_description.getText().toString().trim().isEmpty())) {
                            if (vec_type.equals("Truck")) {
                                if (!(et_goodstype.getText().toString().trim().isEmpty())) {
                                    str_goods_type = et_goodstype.getText().toString();
                                    str_truck_type = et_trucktype.getText().toString();
                                   // str_desc = et_description.getText().toString();
                                    new book_now_task().execute();
                                } else {
                                    Toast.makeText(getApplicationContext(), com.movhaul.customer.R.string.cdaso, Toast.LENGTH_LONG).show();
                                }
                            } else {
                               //str_delivery_address = et_delivery_address.getText().toString();
                                str_truck_type = et_trucktype.getText().toString();
                                str_desc = et_description.getText().toString();
                                new book_now_task().execute();
                            }
                      /*  } else {
                            et_description.setError("Enter Description");
                            et_description.requestFocus();
                        }*/
                    } else {
                        Toast.makeText(getApplicationContext(), getString(com.movhaul.customer.R.string.asda) + vec_type + getString(com.movhaul.customer.R.string.asdv), Toast.LENGTH_LONG).show();
                    }
               /* } else {
                    et_delivery_address.setError("EnterNearby Landmark");
                    et_delivery_address.requestFocus();
                }*/
            }
        });
        dialog1 = new Dialog(Book_now.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //noinspection ConstantConditions
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(com.movhaul.customer.R.layout.dialogue_job_posting);
        btn_ok = (Button) dialog1.findViewById(com.movhaul.customer.R.id.button_ok);
        jobtv1 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_1);
        jobtv2 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_2);
        jobtv3 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_3);
        jobtv4 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_4);
        btn_close = (ImageView) dialog1.findViewById(com.movhaul.customer.R.id.button_close);
        jobtv1.setTypeface(tf);
        jobtv2.setTypeface(tf);
        jobtv3.setTypeface(tf);
        jobtv4.setTypeface(tf);
        btn_ok.setTypeface(tf);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(Book_now.this, Job_review.class);
                startActivity(i);
                finish();
            }
        });
        mSelectedImagesContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.e("tag","111");
                mSelectedImagesContainer.removeView(v);
                // Log.e("tag","111");
            }
        });
    }
    //goods type dialog
    private void goods_type() {
        Dialog_GoodsType dialog_goodsType = new Dialog_GoodsType(Book_now.this, ar_goods_type);
        //noinspection ConstantConditions
        dialog_goodsType.getWindow().setBackgroundDrawable(getResources().getDrawable(com.movhaul.customer.R.drawable.choose));
        dialog_goodsType.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog_goodsType.getWindow().getAttributes().windowAnimations = com.movhaul.customer.R.style.MaterialDialogSheetAnimation;
        dialog_goodsType.show();
        dialog_goodsType.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!(sharedPreferences.getString("goods", "").equals(""))) {
                    et_goodstype.setText(sharedPreferences.getString("goods", ""));
                }
            }
        });
    }
    //truck type dialog
    @SuppressWarnings("ConstantConditions")
    private void truck_type(ArrayList<String> ar_truck_typea, HashMap<String, String> hash_subtypea, HashMap<String, String> hash_truck_imgsa, ArrayList<String> ar_truck_sstypea, ArrayList<String> ar_truck_imgsa) {
        Dialog_VehicleType dialog_vehicleType = new Dialog_VehicleType(Book_now.this, ar_truck_typea, hash_subtypea, hash_truck_imgsa, ar_truck_sstypea, ar_truck_imgsa);
        dialog_vehicleType.getWindow().setBackgroundDrawable(getResources().getDrawable(com.movhaul.customer.R.drawable.choose));
        dialog_vehicleType.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // dialog_vehicleType.getWindow().getAttributes().height= 120;
        dialog_vehicleType.getWindow().getAttributes().windowAnimations = com.movhaul.customer.R.style.MaterialDialogSheetAnimation;
        dialog_vehicleType.show();
        dialog_vehicleType.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (!(sharedPreferences.getString("sub_truck_type", "").equals(""))) {
                    et_trucktype.setText(sharedPreferences.getString("sub_truck_type", ""));
                    str_v_type = sharedPreferences.getString("truck_type", "");
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent i = new Intent(Book_now.this, DashboardNavigation.class);
        startActivity(i);*/
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
        }
    }
    //fetch goods from server
    @SuppressWarnings("UnusedAssignment")
    private class fetch_goods extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            try {
                String virtual_url = com.movhaul.customer.Config.WEB_URL + "customer/goodstype";
                JSONObject jsonobject = HttpUtils.getData(virtual_url, id, token);
                json = jsonobject.toString();
                return json;
            } catch (Exception e) {
                jsonStr = "";
            }
            return jsonStr;
        }
        @Override
        protected void onPostExecute(String jsonStr) {
            super.onPostExecute(jsonStr);
            mProgressDialog.dismiss();
            try {
                JSONObject jo = new JSONObject(jsonStr);
                String status = jo.getString("status");
                if (status.equals("true")) {
                    JSONArray goods_data = jo.getJSONArray("goods_type");
                    if (goods_data.length() > 0) {
                        for (int i = 0; i < goods_data.length(); i++) {
                            String datas = goods_data.getString(i);
                            ar_goods_type.add(datas);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //get truck and subtypes from server
    @SuppressWarnings("UnusedAssignment")
    private class fetch_trucks extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }
        protected String doInBackground(String... params) {
            String json = "", jsonStr = "";
            try {
                String virtual_url = com.movhaul.customer.Config.WEB_URL + "customer/vehicletype";
                JSONObject jsonobject = HttpUtils.getData(virtual_url, id, token);
                json = jsonobject.toString();
                return json;
            } catch (Exception e) {
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
                        hash_subtype = new HashMap<>();
                        hash_subtype1 = new HashMap<>();
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
                    }
                }
                Log.e("tag", "trk_siz_img" + ar_truck_imgs.size());
                Log.e("tag", "bus_siz" + ar_truck_type1.size());
                Log.e("tag", "bus_siz_img" + ar_truck_imgs1.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //book now task
    @SuppressWarnings({"unused", "UnusedAssignment"})
    private class book_now_task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("tag", "booking_task");
            mProgressDialog.show();
            if((!(et_description.getText().toString().trim().isEmpty()))){
                str_desc = et_description.getText().toString().trim();
            }
            if((!(et_delivery_address.getText().toString().trim().isEmpty())))
            str_delivery_address = et_delivery_address.getText().toString();
        }
        @Override
        protected String doInBackground(String... strings) {
            String json = "", jsonStr = "";
            /*
            if for when shipment has images and
            else for when customer not taken any photos only upload shipment details.
             */
            if (goods_imgs.size() > 0) {
                Log.e("tag", "p : " + pickup_location);
                Log.e("tag", "pqr :" + drop_location);
                try {
                    String responseString = null;
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(com.movhaul.customer.Config.WEB_URL + "customer/booking");
                    Log.e("tag","addr: "+com.movhaul.customer.Config.WEB_URL + "customer/booking");
                    httppost.setHeader("id", id);
                    httppost.setHeader("sessiontoken", token);
                    httppost.setHeader("pickup_location", pickup_location);
                    httppost.setHeader("drop_location", drop_location);
                    //by default driver search radius will be 100 mile (in future we have to ask client to how to implement radius on screen
                    //whether button or default 100 mile is enough
                    httppost.setHeader("radius", "100");
                    if(str_delivery_address != null)
                    httppost.setHeader("delivery_address", str_delivery_address);
                    if (vec_type.equals("Truck")) {
                        httppost.setHeader("goods_type", str_goods_type);
                    } else {
                        httppost.setHeader("goods_type", "passenger");
                    }
                    httppost.setHeader("vehicle_type", vec_type);
                    httppost.setHeader("vehicle_main_type", str_v_type);
                    httppost.setHeader("vehicle_sub_type", str_truck_type);
                    if (!(str_desc == null)) {
                        httppost.setHeader("description", str_desc);
                    }
                    httppost.setHeader("booking_time", str_time);
                    httppost.setHeader("pickup_latitude", pick_lati);
                    httppost.setHeader("pickup_longitude", pick_long);
                    httppost.setHeader("drop_latitude", drop_lati);
                    httppost.setHeader("drop_longitude", drop_long);
                    HttpResponse response = null;
                    HttpEntity r_entity = null;
                    try {
                        Log.e("tag0", "img: if "+goods_imgs.get(0));
                        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                        Log.e("tag", "siz:" + goods_imgs.size());
                        for (Uri images : goods_imgs)
                            entity.addPart("bookinggoods", new FileBody(new File(images.toString()), "image/jpeg"));
                        httppost.setEntity(entity);
                        try {
                            response = httpclient.execute(httppost);
                        } catch (Exception e) {
                            Log.e("tag", "ds:" + e.toString());
                        }
                        try {
                            if (response != null) {
                                r_entity = response.getEntity();
                            }
                        } catch (Exception e) {
                            Log.e("tag", "dsa:" + e.toString());
                        }
                        int statusCode = 0;
                        if (response != null) {
                            statusCode = response.getStatusLine().getStatusCode();
                        }
                        if (response != null) {
                            Log.e("tag1", response.getStatusLine().toString());
                        }
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
                    if(str_delivery_address != null)
                    jsonObject.put("delivery_address", str_delivery_address);
                    if (vec_type.equals("Truck")) {
                        jsonObject.put("goods_type", str_goods_type);
                    } else {
                        jsonObject.put("goods_type", "passenger");
                    }
                    jsonObject.put("vehicle_type", vec_type);
                    jsonObject.put("vehicle_main_type", str_v_type);
                    jsonObject.put("vehicle_sub_type", str_truck_type);
                    if (!(str_desc == null)) {
                        jsonObject.put("description", str_desc);
                    }
                    jsonObject.put("booking_time", str_time);
                    jsonObject.put("pickup_latitude", pick_lati);
                    jsonObject.put("pickup_longitude", pick_long);
                    jsonObject.put("drop_latitude", drop_lati);
                    jsonObject.put("drop_longitude", drop_long);
                    jsonObject.put("radius","100");
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
                        String bookingid = jo.getString("booking_id");
                        Log.e("tag", "Job Posted");
                        editor.putString("job_id", bookingid);
                        editor.putString("book_time", book_time);
                        editor.commit();
                        Intent goReve = new Intent(getApplicationContext(), Job_review.class);
                        startActivity(goReve);
                        finish();
                    } else if (status.equals("false")) {

                        String msg = jo.getString("message");

                        if(msg.contains("Error Occured Error: ER_BAD_FIELD_ERROR: Unknown column 'undefined' in 'having clause'")){
                            Log.e("tag", "Job Posted");
                            Intent goReve = new Intent(getApplicationContext(), DashboardNavigation.class);
                            startActivity(goReve);
                            Toast.makeText(getApplicationContext(),"Job Posted Successfully", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        else {
                            Log.e("tag", "Job not posted");
                            Toast.makeText(getApplicationContext(), com.movhaul.customer.R.string.no_internet, Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("tag", "nt" + e.toString());
                    //has to check internet and location...
                    Toast.makeText(getApplicationContext(), com.movhaul.customer.R.string.ase, Toast.LENGTH_LONG).show();
                }
            } else {
                 Toast.makeText(getApplicationContext(),com.movhaul.customer.R.string.no_internet,Toast.LENGTH_LONG).show();
            }
        }
    }
}
