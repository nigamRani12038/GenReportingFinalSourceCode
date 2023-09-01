package com.ossi.genreporting.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.model.HolidayListResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<HolidayListResponse> object;
    HolidayListResponse exp;
    String current_month,current_day;

    public HolidayAdapter(ArrayList<HolidayListResponse> list_holiday, Activity context) {
        this.object = list_holiday;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView holiday_name, holiday_date;
        LinearLayout ll_color;

        public MyViewHolder(View view) {
            super(view);
            holiday_name = view.findViewById(R.id.holiday_name);
            holiday_date = view.findViewById(R.id.holiday_date);
            ll_color=view.findViewById(R.id.ll_color);

        }
    }


    @Override
    public HolidayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holiday_list_item, parent, false);

        return new HolidayAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HolidayAdapter.MyViewHolder holder, int position) {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        current_month = dateFormat.format(date);

        DateFormat dateFormat1 = new SimpleDateFormat("DD");
        Date date1 = new Date();
        current_day = dateFormat.format(date1);

        exp = object.get(position);
        String name1 = exp.getHoliName();
        String holiday_date = exp.getHoliDate();

        String[] separated = holiday_date.split("-");
        String year = separated[0];
        String month = separated[1];
        String day = separated[2];

        if(current_month.equalsIgnoreCase(month)){
            holder.holiday_date.setText(exp.getHoliDate());
            holder.holiday_name.setText(exp.getHoliName());
            holder.holiday_date.setTextColor(Color.BLUE);
            holder.holiday_name.setTextColor(Color.BLUE);
           // holder.ll_color.setBackgroundColor(Color.LTGRAY);

        }
        if (current_day.equalsIgnoreCase(day)) {
            holder.holiday_date.setText(exp.getHoliDate());
            holder.holiday_name.setText(exp.getHoliName());
            holder.holiday_date.setTextColor(Color.GREEN);
            holder.holiday_name.setTextColor(Color.GREEN);
            // holder.ll_color.setBackgroundColor(Color.LTGRAY);
        }


        holder.holiday_date.setText(exp.getHoliDate());
        holder.holiday_name.setText(exp.getHoliName());

    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(HolidayListResponse s) {
        object.add(s);
        //notifyDataSetChanged();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        //transaction.addToBackStack(null);  //fr if written, this transaction will be added to backstack
        transaction.commit();
    }
}
