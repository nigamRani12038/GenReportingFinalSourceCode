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
import com.ossi.genreporting.model.ViewLeaveStatus;

import java.util.List;

public class ViewLeaveAdapter extends RecyclerView.Adapter<ViewLeaveAdapter.MyViewHolder> {

    FragmentActivity context;
    private List<ViewLeaveStatus> object;
    ViewLeaveStatus exp;

    public ViewLeaveAdapter(List<ViewLeaveStatus> list_meter, Activity context) {
        this.object = list_meter;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date_leave, purpose_leave_s,leave_staus_s,leave_type_;

        public MyViewHolder(View view) {
            super(view);
            date_leave = view.findViewById(R.id.date_leave);
            purpose_leave_s =view.findViewById(R.id.purpose_leave_s);
            leave_staus_s =view.findViewById(R.id.leave_staus_s);
            leave_type_=view.findViewById(R.id.leave_type_);

        }
    }


    @Override
    public ViewLeaveAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_leave_view_item, parent, false);

        return new ViewLeaveAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewLeaveAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String name1 = exp.getResponse();
        holder.date_leave.setText("Date:   "+exp.getFromDate());
        holder.purpose_leave_s.setText("Purpose:   "+exp.getPurpose());
        holder.leave_staus_s.setText("Status:   "+exp.getStatus());
        holder.leave_type_.setText("Leave Type:   "+exp.getTypeLeave()+"\n"+"By:   "+exp.getApprove_name());
    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(ViewLeaveStatus s) {
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
