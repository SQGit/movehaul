package com.movhaul.customer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.Button;
import com.sloop.fonts.FontsManager;
import com.systemspecs.remita.remitapaymentgateway.RemitaMainActivity;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class DriversListAdapter extends ArrayAdapter<MV_Datas> {

    Context context;
    ArrayList<MV_Datas> ar_drv_list;
    Activity act;
    FoldingCell cell;
    ImageView btn_confirm;
    Dialog dialog1, dialog2;
    ImageView btn_close;
    Button btn_ok;
    TextView tv_dialog1, tv_dialog2, tv_dialog3, tv_dialog4;
    Typeface type;
    ImageView iv_driver_image, iv_back, iv_front;
    ViewPager viewPager;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    com.rey.material.widget.TextView tv_title_truck,tv_title_truck_txt,tv_title_driver_name,tv_title_bidding,tv_content_bidding,tv_content_driver_name,tv_content_damage_control,tv_content_truck,tv_title_driver_name_txt,tv_content_driver_name_txt,tv_content_damage_control_txt,tv_content_truck_txt,tv_content_job_comp,tv_content_break_hist,tv_content_job_completed,tv_content_break_history;
    MV_Datas mv_datas;
    String tr_front,tr_side,tr_back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String bidding;
    int doid ;
    Typeface tf;
    private static final int REQUEST_CODE_PAYMENT = 545;

    public DriversListAdapter(Context context, Activity acti, ArrayList<MV_Datas> objects, int ss) {
        super(context, 0, objects);
        this.act = acti;
        this.context = context;
        this.ar_drv_list = objects;
        this.doid = ss;

    }

    @Override
    public int getCount() {
        return ar_drv_list.size();
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        // get item for selected view
        //Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        tf = Typeface.createFromAsset(act.getAssets(), "fonts/lato.ttf");

        FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
        FontsManager.changeFonts(act);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(act);
        editor = sharedPreferences.edit();

        type = Typeface.createFromAsset(getContext().getAssets(), "fonts/lato.ttf");

        mv_datas = ar_drv_list.get(position);

        if (cell == null) {
            viewHolder = new ViewHolder();

            FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
            FontsManager.changeFonts(act);

            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(com.movhaul.customer.R.layout.drivers_list_adapter, parent, false);


            cell.setTag(viewHolder);
        } else {

            FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
            FontsManager.changeFonts(act);

            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (unfoldedIndexes.contains(position)) {

            FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
            FontsManager.changeFonts(act);

            cell.unfold(true);
        } else {

            FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
            FontsManager.changeFonts(act);

            cell.fold(true);
        }

        if(doid == 0){
            layouts = new int[]{
                    com.movhaul.customer.R.layout.truck_front,
                    com.movhaul.customer.R.layout.truck_back,};
        }
        else {

            layouts = new int[]{
                    com.movhaul.customer.R.layout.truck_front,
                    com.movhaul.customer.R.layout.truck_back,
                    com.movhaul.customer.R.layout.truck_side};
        }



        RatingBar ratingBar = (RatingBar) cell.findViewById(com.movhaul.customer.R.id.ratingBsdar);
        RatingBar ratingBar1 = (RatingBar) cell.findViewById(com.movhaul.customer.R.id.ratingbar);
      /*  LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
*/
        LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
        LayerDrawable layerDrawable1 = (LayerDrawable) ratingBar1.getProgressDrawable();
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable1.getDrawable(0)),
                context.getResources().getColor(com.movhaul.customer.R.color.light_grey));  // Empty star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable1.getDrawable(1)),
                context.getResources().getColor(com.movhaul.customer.R.color.gold)); // Partial star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable1.getDrawable(2)),
                context.getResources().getColor(com.movhaul.customer.R.color.gold));


        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(0)),
                context.getResources().getColor(com.movhaul.customer.R.color.shadowColor));  // Empty star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(1)),
                context.getResources().getColor(com.movhaul.customer.R.color.gold)); // Partial star
        DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(2)),
                context.getResources().getColor(com.movhaul.customer.R.color.gold));




        tv_title_bidding = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_title_bidding);
        tv_title_truck = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_title_truck_type);
        tv_title_driver_name = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_title_driver_name);
        tv_title_truck_txt = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_title_truck_type_txt);
        tv_title_driver_name_txt = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_title_driver_name_txt);

        tv_title_bidding.setTypeface(tf);
        tv_title_truck.setTypeface(tf);
        tv_title_driver_name.setTypeface(tf);
        tv_title_truck_txt.setTypeface(tf);
        tv_title_driver_name_txt.setTypeface(tf);

        if(doid == 0){
            tv_title_truck_txt.setText("Bus");
        }
        else{
            tv_title_truck_txt.setText("Truck");
        }



        tv_content_bidding = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_bidding);

        tv_content_driver_name = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_driver_name);
        tv_content_damage_control = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_damage_control);
        tv_content_truck = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_truck_type);

        tv_content_driver_name_txt = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_driver_name_txt);
        tv_content_damage_control_txt = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_damage_control_txt);
        tv_content_truck_txt = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_truck_type_txt);

        tv_content_job_comp = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_jobs_completed);
        tv_content_break_hist = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_breakdown);
        tv_content_job_completed = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_jobs_compl);
        tv_content_break_history = (com.rey.material.widget.TextView) cell.findViewById(com.movhaul.customer.R.id.textview_content_break);

        tv_content_bidding.setTypeface(tf);
        tv_content_driver_name.setTypeface(tf);
        tv_content_damage_control.setTypeface(tf);
        tv_content_truck.setTypeface(tf);
        tv_content_driver_name_txt.setTypeface(tf);
        tv_content_damage_control_txt.setTypeface(tf);
        tv_content_truck_txt.setTypeface(tf);
        tv_content_job_comp.setTypeface(tf);
        tv_content_break_hist.setTypeface(tf);
        tv_content_job_completed.setTypeface(tf);
        tv_content_break_history.setTypeface(tf);

        iv_driver_image = (ImageView) cell.findViewById(com.movhaul.customer.R.id.driver_image);


      /*  Glide.with(ProfileActivity.this).load(Config.WEB_URL+"vehicle_details/"+img).error(R.drawable.truck_front_ico).into(iv_vec_front);
        Glide.with(ProfileActivity.this).load(Config.WEB_URL+"vehicle_details/"+img1).error(R.drawable.truck_side_ico).into(iv_vec_side);
        Glide.with(ProfileActivity.this).load(Config.WEB_URL+"vehicle_details/"+img2).error(R.drawable.truck_back_ico).into(iv_vec_back);
        Glide.with(ProfileActivity.this).load(Config.WEB_URL+"vehicle_details/"+img3).into(iv_vec_rc);
        Glide.with(ProfileActivity.this).load(Config.WEB_URL+"vehicle_details/"+img4).into(iv_vec_ins);*/

        mv_datas = ar_drv_list.get(position);


        bidding = mv_datas.getBidding();
        Log.e("tag","ss: "+mv_datas.getBidding());

        tv_title_bidding.setText(bidding+" ₦");
        tv_title_truck.setText(mv_datas.getTruck_type());
        tv_title_driver_name.setText(mv_datas.getName());




        tv_content_bidding.setText(bidding+" ₦");
        tv_content_truck.setText(mv_datas.getTruck_type());
        tv_content_driver_name.setText(mv_datas.getName());
        tv_content_damage_control.setText(mv_datas.getDamage_control());

        Glide.with(act).load(Config.WEB_URL_IMG+"driver_details/"+mv_datas.getDriver_image()).into(iv_driver_image);



        iv_driver_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "clik");
                dialog2.show();



                if(doid ==0) {
                    tr_front = mv_datas.getTruck_front();
                    tr_back = mv_datas.getTruck_back();
                    Log.e("tag","bb:|+"+tr_back);
                }
                else{
                    tr_front = mv_datas.getTruck_front();
                    tr_back = mv_datas.getTruck_back();
                    tr_side  = mv_datas.getTruck_side();
                }


            }
        });


        btn_confirm = (ImageView) cell.findViewById(com.movhaul.customer.R.id.imageView_doubletick);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.show();
            }
        });




        dialog2 = new Dialog(DriversListAdapter.this.getContext());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(true);
        dialog2.setContentView(com.movhaul.customer.R.layout.dialog_viewpager);

        viewPager = (ViewPager) dialog2.findViewById(com.movhaul.customer.R.id.view_pager);
        iv_back = (ImageView) dialog2.findViewById(com.movhaul.customer.R.id.imageview_back);
        iv_front = (ImageView) dialog2.findViewById(com.movhaul.customer.R.id.imageview_front);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(doid ==0) {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    }  else {
                        viewPager.setCurrentItem(0);
                    }
                }
                else{
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(2);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(0);
                    } else {
                        viewPager.setCurrentItem(1);
                    }
                }

            }
        });

        iv_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(doid ==0) {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    }  else {
                        viewPager.setCurrentItem(0);
                    }
                }
                else{
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1) {
                        viewPager.setCurrentItem(2);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                }

            }
        });


        dialog1 = new Dialog(DriversListAdapter.this.getContext());
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setCancelable(false);
        dialog1.setContentView(com.movhaul.customer.R.layout.dialogue_job_posting);
        btn_ok = (Button) dialog1.findViewById(com.movhaul.customer.R.id.button_ok);
        btn_close = (ImageView) dialog1.findViewById(com.movhaul.customer.R.id.button_close);
        tv_dialog1 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_1);
        tv_dialog2 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_2);
        tv_dialog3 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_3);
        tv_dialog4 = (TextView) dialog1.findViewById(com.movhaul.customer.R.id.textView_4);
        tv_dialog1.setText("Selected Driver");
        tv_dialog2.setText("Successfully!!");
        tv_dialog3.setText("Are you sure that");
        tv_dialog4.setText("this driver is suitable for your job?");

        tv_dialog1.setTypeface(type);
        tv_dialog2.setTypeface(type);
        tv_dialog3.setTypeface(type);
        tv_dialog4.setTypeface(type);
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

               mv_datas.getBooking_id();
                mv_datas.getDriver_id();

                editor.putString("payment_amount",bidding);
                editor.putString("booking_id",mv_datas.getBooking_id());
                editor.putString("driver_id",mv_datas.getDriver_id());
                editor.putString("bidding_id",mv_datas.getBidding_id());
                editor.commit();
/*
                Intent i = new Intent(DriversListAdapter.this.getContext(), Payment_Details.class);
                getContext().startActivity(i);*/


                Intent intent = new Intent(DriversListAdapter.this.getContext(), RemitaMainActivity.class);
                intent.putExtra("amount", bidding);
                intent.putExtra("testMode", true);
                intent.putExtra("apiKey", "U1lTUC4xNUhPMTIkMTIzLjR8U1lTUA==");
                intent.putExtra("txnToken", "55316C54554334784E5568504D54496B4D54497A4C6A523855316C5455413D3D7C3932333737633266613035313135306337363534386636376266623131303165383831366464343834666234363064653062343731663538643461323835303537333638653232313135363366383334666337613166333265333336653834626539656566393465396363356131363739353463646239333434363164313732");
                ((Activity) context).startActivityForResult(intent, REQUEST_CODE_PAYMENT);
                //getContext().startActivityForResult(intent, REQUEST_CODE_PAYMENT);


                //finish();
            }
        });



        return cell;

    }

    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("MyAdapter", "onActivityResult");
    }



    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        Button btn_cancel;

    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);

            if(doid ==1) {
                if (position == 0) {
                    ImageView iv_trk = (ImageView) view.findViewById(com.movhaul.customer.R.id.image);
                    Glide.with(act).load(Config.WEB_URL_IMG + "vehicle_details/" + tr_front).into(iv_trk);
                } else if (position == 1) {
                    ImageView iv_trk = (ImageView) view.findViewById(com.movhaul.customer.R.id.image);
                    Glide.with(act).load(Config.WEB_URL_IMG + "vehicle_details/" + tr_side).into(iv_trk);
                } else {
                    ImageView iv_trk = (ImageView) view.findViewById(com.movhaul.customer.R.id.image);
                    Glide.with(act).load(Config.WEB_URL_IMG + "vehicle_details/" + tr_back).into(iv_trk);
                }
            }
            else{

                if (position == 0) {
                    ImageView iv_trk = (ImageView) view.findViewById(com.movhaul.customer.R.id.image);
                    Glide.with(act).load(Config.WEB_URL_IMG + "vehicle_details/" + tr_front).into(iv_trk);
                }  else {
                    ImageView iv_trk = (ImageView) view.findViewById(com.movhaul.customer.R.id.image);
                    Glide.with(act).load(Config.WEB_URL_IMG + "vehicle_details/" + tr_back).into(iv_trk);
                }


            }

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }




    }


}