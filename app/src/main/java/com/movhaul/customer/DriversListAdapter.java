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
import android.support.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
@SuppressWarnings({"UnusedAssignment", "deprecation", "ConstantConditions"})
class DriversListAdapter extends ArrayAdapter<MV_Datas> {
    Context context;
    private ArrayList<MV_Datas> ar_drv_list;
    private Activity act;
    private Dialog dialog1, dialog2;
    Typeface type;
    private ViewPager viewPager;
    MV_Datas mv_datas;
    private String tr_front, tr_side, tr_back;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String bidding;
    private int doid;
    Typeface tf;
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private int[] layouts;
    DriversListAdapter(Context context, Activity acti, ArrayList<MV_Datas> objects, int ss) {
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
    @NonNull
    @Override
    public View getView(final int position, final View convertView, @NonNull ViewGroup parent) {
        FoldingCell cell = (FoldingCell) convertView;
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
        if (doid == 0) {
            layouts = new int[]{
                    com.movhaul.customer.R.layout.truck_front,
                    com.movhaul.customer.R.layout.truck_back,};
        } else {
            layouts = new int[]{
                    com.movhaul.customer.R.layout.truck_front,
                    com.movhaul.customer.R.layout.truck_back,
                    com.movhaul.customer.R.layout.truck_side};
        }
        RatingBar ratingBar = (RatingBar) cell.findViewById(com.movhaul.customer.R.id.ratingBsdar);
        RatingBar ratingBar1 = (RatingBar) cell.findViewById(com.movhaul.customer.R.id.ratingbar);
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
        com.rey.material.widget.TextView tv_title_bidding = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_bidding);
        com.rey.material.widget.TextView tv_title_truck = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_truck_type);
        com.rey.material.widget.TextView tv_title_driver_name = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_driver_name);
        com.rey.material.widget.TextView tv_title_truck_txt = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_truck_type_txt);
        com.rey.material.widget.TextView tv_title_driver_name_txt = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_title_driver_name_txt);
        tv_title_bidding.setTypeface(tf);
        tv_title_truck.setTypeface(tf);
        tv_title_driver_name.setTypeface(tf);
        tv_title_truck_txt.setTypeface(tf);
        tv_title_driver_name_txt.setTypeface(tf);
        if (doid == 0) {
            tv_title_truck_txt.setText("Bus");
        } else {
            tv_title_truck_txt.setText("Truck");
        }
        com.rey.material.widget.TextView tv_content_bidding = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_bidding);
        com.rey.material.widget.TextView tv_content_driver_name = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_driver_name);
        com.rey.material.widget.TextView tv_content_damage_control = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_damage_control);
        com.rey.material.widget.TextView tv_content_truck = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_truck_type);
        com.rey.material.widget.TextView tv_content_driver_name_txt = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_driver_name_txt);
        com.rey.material.widget.TextView tv_content_damage_control_txt = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_damage_control_txt);
        com.rey.material.widget.TextView tv_content_truck_txt = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_truck_type_txt);
        com.rey.material.widget.TextView tv_content_job_comp = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_jobs_completed);
        com.rey.material.widget.TextView tv_content_break_hist = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_breakdown);
        com.rey.material.widget.TextView tv_content_break_history = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_content_break);
        com.rey.material.widget.TextView tv_content_tot_jobs = (com.rey.material.widget.TextView) cell.findViewById(R.id.textview_context_total_jobs);
        tv_content_bidding.setTypeface(tf);
        tv_content_driver_name.setTypeface(tf);
        tv_content_damage_control.setTypeface(tf);
        tv_content_truck.setTypeface(tf);
        tv_content_driver_name_txt.setTypeface(tf);
        tv_content_damage_control_txt.setTypeface(tf);
        tv_content_truck_txt.setTypeface(tf);
        tv_content_job_comp.setTypeface(tf);
        tv_content_break_hist.setTypeface(tf);
        tv_content_break_history.setTypeface(tf);
        tv_content_tot_jobs.setTypeface(tf);
        ImageView iv_driver_image = (ImageView) cell.findViewById(R.id.driver_image);
        mv_datas = ar_drv_list.get(position);
        bidding = mv_datas.getBidding();
        Log.e("tag", "ss: " + mv_datas.getBidding());
        tv_title_bidding.setText(bidding + " ₦");
        tv_title_truck.setText(mv_datas.getTruck_type());
        tv_title_driver_name.setText(mv_datas.getName());
        tv_content_bidding.setText(bidding + " ₦");
        tv_content_truck.setText(mv_datas.getTruck_type());
        tv_content_driver_name.setText(mv_datas.getName());
        tv_content_damage_control.setText(mv_datas.getDamage_control());
        tv_content_tot_jobs.setText(mv_datas.getTotJobs());
        Glide.with(act).load(Config.WEB_URL_IMG + "driver_details/" + mv_datas.getDriver_image()).into(iv_driver_image);
        iv_driver_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag", "clik");
                dialog2.show();
                if (doid == 0) {
                    tr_front = mv_datas.getTruck_front();
                    tr_back = mv_datas.getTruck_back();
                    Log.e("tag", "bb:|+" + tr_back);
                } else {
                    tr_front = mv_datas.getTruck_front();
                    tr_back = mv_datas.getTruck_back();
                    tr_side = mv_datas.getTruck_side();
                }
            }
        });
        ImageView btn_confirm = (ImageView) cell.findViewById(R.id.imageView_doubletick);
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  dialog1.show();
                mv_datas = ar_drv_list.get(position);
                editor.putString("payment_amount", bidding);
                editor.putString("booking_id", mv_datas.getBooking_id());
                editor.putString("driver_id", mv_datas.getDriver_id());
                editor.putString("bidding_id", mv_datas.getBidding_id());
                editor.putString("book_for","truck");
                editor.apply();
                Intent i = new Intent(DriversListAdapter.this.getContext(), Payment_Details.class);
                getContext().startActivity(i);
            }
        });
        dialog2 = new Dialog(DriversListAdapter.this.getContext());
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setCancelable(true);
        dialog2.setContentView(com.movhaul.customer.R.layout.dialog_viewpager);
        viewPager = (ViewPager) dialog2.findViewById(com.movhaul.customer.R.id.view_pager);
        ImageView iv_back = (ImageView) dialog2.findViewById(R.id.imageview_back);
        ImageView iv_front = (ImageView) dialog2.findViewById(R.id.imageview_front);
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doid == 0) {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                } else {
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
                if (doid == 0) {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else {
                        viewPager.setCurrentItem(0);
                    }
                } else {
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
        Button btn_ok = (Button) dialog1.findViewById(R.id.button_ok);
        ImageView btn_close = (ImageView) dialog1.findViewById(R.id.button_close);
        TextView tv_dialog1 = (TextView) dialog1.findViewById(R.id.textView_1);
        TextView tv_dialog2 = (TextView) dialog1.findViewById(R.id.textView_2);
        TextView tv_dialog3 = (TextView) dialog1.findViewById(R.id.textView_3);
        TextView tv_dialog4 = (TextView) dialog1.findViewById(R.id.textView_4);
        tv_dialog1.setText(R.string.awex);
        tv_dialog2.setText(R.string.zxwea);
        tv_dialog3.setText(R.string.czwa);
        tv_dialog4.setText(R.string.azec);
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
                editor.putString("payment_amount", bidding);
                editor.putString("booking_id", mv_datas.getBooking_id());
                editor.putString("driver_id", mv_datas.getDriver_id());
                editor.putString("bidding_id", mv_datas.getBidding_id());
                editor.apply();
                Intent i = new Intent(DriversListAdapter.this.getContext(), Payment_Details.class);
                getContext().startActivity(i);
            }
        });
        return cell;
    }

    // simple methods for register cell state changes
    void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }
    private void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }
    private void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }
   /* public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }
    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
    }*/
    // View lookup cache
    @SuppressWarnings("unused")
    private static class ViewHolder {
        Button btn_cancel;
    }
    private class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        MyViewPagerAdapter() {
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            if (doid == 1) {
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
            } else {
                if (position == 0) {
                    ImageView iv_trk = (ImageView) view.findViewById(com.movhaul.customer.R.id.image);
                    Glide.with(act).load(Config.WEB_URL_IMG + "vehicle_details/" + tr_front).into(iv_trk);
                } else {
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