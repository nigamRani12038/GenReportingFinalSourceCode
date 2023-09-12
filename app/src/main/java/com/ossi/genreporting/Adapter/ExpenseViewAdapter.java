package com.ossi.genreporting.Adapter;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.model.ExpenseViewResponse;

import java.util.List;

public class ExpenseViewAdapter extends RecyclerView.Adapter<ExpenseViewAdapter.MyViewHolder> {

    FragmentActivity context;
    private List<ExpenseViewResponse> object;
    ExpenseViewResponse exp;

    public ExpenseViewAdapter(List<ExpenseViewResponse> list_meter, Activity context) {
        this.object = list_meter;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView rs, purpuse_expense,date_expense;

        public MyViewHolder(View view) {
            super(view);
            rs = view.findViewById(R.id.rs);
            purpuse_expense =view.findViewById(R.id.purpuse_expense);
            date_expense =view.findViewById(R.id.date_expense);

        }
    }


    @Override
    public ExpenseViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_expense_view_item, parent, false);

        return new ExpenseViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExpenseViewAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String name1 = exp.getAmount();
        holder.rs.setText("Rs: "+exp.getAmount());
        holder.purpuse_expense.setText("Purpose: "+exp.getRegin());
        holder.date_expense.setText("Date: "+exp.getDBTime());
    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(ExpenseViewResponse s) {
        object.add(s);
        //notifyDataSetChanged();
    }

    private void openFragment(Fragment fragment){
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout,  fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  //fr if written, this transaction will be added to backstack
        transaction.commit();
    }
}
