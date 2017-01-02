package net.sqindia.movehaul;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.ramotion.foldingcell.FoldingCell;
import com.rey.material.widget.Button;
import com.rey.material.widget.TextView;
import com.sloop.fonts.FontsManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class UpcomingAdapter extends ArrayAdapter<MV_Datas> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    Context context;
    ArrayList<MV_Datas> up_lists;
    Activity act;
    FoldingCell cell;
    MV_Datas mv_datas;
    TextView tv_title_date,tv_title_booking_id,tv_title_pickup,tv_title_drop;


    public UpcomingAdapter(Context context,Activity acti, ArrayList<MV_Datas> objects) {
        super(context, 0, objects);
        this.act = acti;
        up_lists = objects;

    }

    @Override
    public int getCount() {
        return up_lists.size()-1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        //Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
         cell = (FoldingCell) convertView;
        ViewHolder viewHolder;

       FontsManager.initFormAssets(act, "fonts/lato.ttf");       //initialization
       FontsManager.changeFonts(act);

        mv_datas = up_lists.get(position+1);

        Log.e("tag","ds: "+mv_datas.getDate());

        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.upcoming_trip_adapter, parent, false);

            viewHolder.btn_cancel = (Button) cell.findViewById(R.id.button_cancel);

            tv_title_date = (TextView) cell.findViewById(R.id.textview_title_date);
            tv_title_booking_id = (TextView) cell.findViewById(R.id.textview_title_booking_id);
            tv_title_pickup = (TextView) cell.findViewById(R.id.textview_title_pickup);
            tv_title_drop = (TextView) cell.findViewById(R.id.textview_title_drop);

            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        if (unfoldedIndexes.contains(position)) {
            cell.unfold(true);
        } else {
            cell.fold(true);
        }


        tv_title_date.setText(mv_datas.getDate());
        tv_title_booking_id.setText(mv_datas.getBooking_id());
        tv_title_pickup.setText(mv_datas.getPickup());
        tv_title_drop.setText(mv_datas.getDrop());






        viewHolder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("tag","buttonclick");
                cell.unfold((true));
            }
        });





        return cell;
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
}
