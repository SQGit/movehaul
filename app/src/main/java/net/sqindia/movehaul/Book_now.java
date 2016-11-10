package net.sqindia.movehaul;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rey.material.widget.Button;
import com.sloop.fonts.FontsManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import static net.sqindia.movehaul.R.id.msg;
import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;
import nl.changer.polypicker.utils.ImageInternalFetcher;
/**
 * Created by sqindia on 25-10-2016.
 */

public class Book_now extends Activity {
    String truck, goods;
    LinearLayout lt_goodsType, lt_truckType;
    EditText et_delivery_address, et_goodstype, et_trucktype, et_description;
    TextInputLayout flt_delivery_address, flt_goodstype, flt_trucktype, flt_description;
    com.rey.material.widget.LinearLayout btn_back;
    Button btn_post, btn_ok;
    Dialog dialog1;
    ImageView btn_close;
    TextView jobtv1,jobtv2,jobtv3,jobtv4,msg;
    Typeface type;
    ArrayList<String> mdatas;
    private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;
    private ViewGroup mSelectedImagesContainer;
    HashSet<Uri> mMedia = new HashSet<Uri>();
    ArrayList<Uri> image_path = new ArrayList<>();
    String[] imagearray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_now);
        FontsManager.initFormAssets(this, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(this);
        mdatas = new ArrayList<>();
         type = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        et_delivery_address = (EditText) findViewById(R.id.editTextDelieveryAddress);
        et_goodstype = (EditText) findViewById(R.id.editTextGoodsType);
        et_trucktype = (EditText) findViewById(R.id.editTextTruck_type);
        et_description = (EditText) findViewById(R.id.editTextDescription);
        msg=(TextView) findViewById(R.id.msg);
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);

        flt_delivery_address = (TextInputLayout) findViewById(R.id.float_deliveryaddress);
        flt_goodstype = (TextInputLayout) findViewById(R.id.float_goodstype);
        flt_trucktype = (TextInputLayout) findViewById(R.id.float_trucktype);
        flt_description = (TextInputLayout) findViewById(R.id.float_description);

        lt_goodsType = (LinearLayout) findViewById(R.id.layout_goodstype);
        lt_truckType = (LinearLayout) findViewById(R.id.layout_truckType);

        btn_back = (com.rey.material.widget.LinearLayout) findViewById(R.id.layout_back);
        btn_post = (Button) findViewById(R.id.btn_post);
        flt_delivery_address.setTypeface(type);
        flt_goodstype.setTypeface(type);
        flt_trucktype.setTypeface(type);
        flt_description.setTypeface(type);

        lt_goodsType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goods_type();
            }
        });
        lt_truckType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                truck_type();

            }
        });
        View getImages = findViewById(R.id.get_images);
        getImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getImagesView();
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Book_now.this, DashboardNavigation.class);
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


        dialog1 = new Dialog(Book_now.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(R.layout.dialogue_job_posting);
        btn_ok = (Button) dialog1.findViewById(R.id.button_ok);
        jobtv1 = (TextView) dialog1.findViewById(R.id.textView_1);
        jobtv2 = (TextView) dialog1.findViewById(R.id.textView_2);
        jobtv3 = (TextView) dialog1.findViewById(R.id.textView_3);
        jobtv4 = (TextView) dialog1.findViewById(R.id.textView_4);
        btn_close = (ImageView) dialog1.findViewById(R.id.button_close);

        jobtv1.setTypeface(type);
        jobtv2.setTypeface(type);
        jobtv3.setTypeface(type);
        jobtv4.setTypeface(type);
        btn_ok.setTypeface(type);

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

    }

    private void truck_type() {

        LayoutInflater layoutInflater = LayoutInflater.from(Book_now.this);
        View promptView = layoutInflater.inflate(R.layout.truck_selecting, null);
        final AlertDialog alertD = new AlertDialog.Builder(Book_now.this).create();
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


        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
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
                et_trucktype.setText(truck);
            }

        });
        alertD.setView(promptView);
        alertD.show();
    }

    private void goods_type() {

        LayoutInflater layoutInflater = LayoutInflater.from(Book_now.this);
        View promptView = layoutInflater.inflate(R.layout.goods_selecting, null);
        final AlertDialog alertD = new AlertDialog.Builder(Book_now.this).create();
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


        Typeface tf = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/lato.ttf");
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
                et_goodstype.setText(goods);
            }

        });
        alertD.setView(promptView);
        alertD.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(Book_now.this, DashboardNavigation.class);
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

    private void getImagesView() {

        Intent intent = new Intent(getApplicationContext(), ImagePickerActivity.class);
        Config config = new Config.Builder()
                .setTabBackgroundColor(R.color.white)    // set tab background color. Default white.

                .setSelectionLimit(6)    // set photo selection limit. Default unlimited selection.
                .build();
        ImagePickerActivity.setConfig(config);
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

       /* Intent intent_image = new Intent(PostHouse.this, ImagePickerActivity.class);
        startActivityForResult(intent_image, INTENT_REQUEST_GET_IMAGES);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);
        msg.setVisibility(View.GONE);

        if (resuleCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES || requestCode == INTENT_REQUEST_GET_N_IMAGES) {
                Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (parcelableUris == null) {
                    return;
                }

                // Java doesn't allow array casting, this is a little hack
                Uri[] uris = new Uri[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);

                if (uris != null) {

                    for (Uri uri : uris) {
                        Log.e("tag", " uri: " + uri);
                       String path = uri.toString();
                        mMedia.add(uri);
                        mdatas.add(String.valueOf(uri));
                        //path=String.valueOf(uri);
                        Log.d("tag", "choosed file" + mMedia);
                        StringBuilder builder = new StringBuilder();
                        for (Uri value : mMedia) {
                            builder.append(value + "#####");

                        }
                        String text = builder.toString();
                        imagearray=text.split("\\#\\#\\#\\#\\#");




                    }
                    showMedia();
                }
            }
        }
    }



    private void showMedia() {
        // Remove all views before
        // adding the new ones.
        mSelectedImagesContainer.removeAllViews();

        Iterator<Uri> iterator = mMedia.iterator();
        ImageInternalFetcher imageFetcher = new ImageInternalFetcher(this, 500);
        while (iterator.hasNext()) {
            Uri uri = iterator.next();

            // showImage(uri);
            Log.i("tah", " uri: " + uri);
            if (mMedia.size() >= 1) {
                mSelectedImagesContainer.setVisibility(View.VISIBLE);
            }

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.media_layout, null);
            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

            if (!uri.toString().contains("content://")) {
                uri = Uri.fromFile(new File(uri.toString()));
            }

            imageFetcher.loadImage(uri, thumbnail);

            mSelectedImagesContainer.addView(imageHolder);


            int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
            int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, getResources().getDisplayMetrics());
            //thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
            thumbnail.getLayoutParams().width = 250;
            thumbnail.getLayoutParams().height = 250;
            thumbnail.setAdjustViewBounds(true);
        }
    }
}
