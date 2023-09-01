package com.ossi.genreporting.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.model.ViewWorkFromHomeResponse;

import java.util.List;

public class WfhViewAdapter extends RecyclerView.Adapter<WfhViewAdapter.MyViewHolder> {

    FragmentActivity context;
    private List<ViewWorkFromHomeResponse> object;
    ViewWorkFromHomeResponse exp;

    public WfhViewAdapter(List<ViewWorkFromHomeResponse> list_meter, Activity context) {
        this.object = list_meter;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date_wfh, purpuse_wfh,status_wfh;

        public MyViewHolder(View view) {
            super(view);
            date_wfh = view.findViewById(R.id.date_wfh);
            purpuse_wfh =view.findViewById(R.id.purpuse_wfh);
            status_wfh =view.findViewById(R.id.status_wfh);

        }
    }


    @Override
    public WfhViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_wfhview_item, parent, false);

        return new WfhViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WfhViewAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String name1 = exp.getResponse();
        holder.purpuse_wfh.setText("Purpose: "+exp.getPurpose());
        holder.status_wfh.setText("Status: "+exp.getLeaveStatus());
        holder.date_wfh.setText("Date: "+exp.getFDate());
    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(ViewWorkFromHomeResponse s) {
        object.add(s);
        //notifyDataSetChanged();
    }

    private void openFragment(Fragment fragment){
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout,  fragment); // give your fragment container id in first parameter
        //transaction.addToBackStack(null);  //fr if written, this transaction will be added to backstack
        transaction.commit();
    }
}
