package com.ossi.genreporting.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.R;
import com.ossi.genreporting.model.PresentStatusItem;

import java.util.ArrayList;

public class PreviousWeekHourAdapter extends RecyclerView.Adapter<PreviousWeekHourAdapter.MyViewHolder> implements Filterable {

    FragmentActivity context;
    private ArrayList<PresentStatusItem> object;
    PresentStatusItem exp;

    public PreviousWeekHourAdapter(ArrayList<PresentStatusItem> week_hour_list, Activity context) {
        this.object = week_hour_list;
        this.context = (FragmentActivity) context;
    }

    @Override
    public Filter getFilter() {
        return null;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView emp_name_current, attenancetype;
       // RoundedImageView imp_img;

        public MyViewHolder(View view) {
            super(view);
            emp_name_current = view.findViewById(R.id.emp_name_current);
            attenancetype = view.findViewById(R.id.attenancetype);
           // imp_img = view.findViewById(R.id.imp_img);


        }
    }


    @Override
    public PreviousWeekHourAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.total_previous_hour_show, parent, false);

        return new PreviousWeekHourAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PreviousWeekHourAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String img = exp.getImage();


        holder.emp_name_current.setText(exp.getEmpname());
        holder.attenancetype.setText(exp.getPreviousWeekHours());


       /* if (!img.equalsIgnoreCase("NA")) {

            Glide.with(context).load(img).into(holder.imp_img);
        } else if (img.equalsIgnoreCase("NA")) {
            Drawable myDrawable = context.getResources().getDrawable(R.drawable.user);
            holder.imp_img.setImageDrawable(myDrawable);
        }*/
    }

    public void updateList(ArrayList<PresentStatusItem> list) {
        this.object = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(PresentStatusItem s) {
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
