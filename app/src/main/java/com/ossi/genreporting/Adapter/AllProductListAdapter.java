package com.ossi.genreporting.Adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.fragment.TaskSubmitFragment;
import com.ossi.genreporting.model.AllProductResponseItem;
import com.ossi.genreporting.model.MyTaskListResponse;

import java.util.ArrayList;

public class AllProductListAdapter extends RecyclerView.Adapter<AllProductListAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<AllProductResponseItem> object;
    AllProductResponseItem exp;

    public AllProductListAdapter(ArrayList<AllProductResponseItem> list_task, Activity context) {
        this.object = list_task;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name,emp_name_product,module_count,doc_count,assign_count,not_assign_count;
        ToggleButton active_inactive;


        public MyViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.product_name);
           emp_name_product = view.findViewById(R.id.emp_name_product);
            module_count = view.findViewById(R.id.module_count);
            doc_count = view.findViewById(R.id.doc_count);
            assign_count=view.findViewById(R.id.assign_count);
            not_assign_count=view.findViewById(R.id.not_assign_count);
            active_inactive=view.findViewById(R.id.active_inactive);

        }
    }


    @Override
    public AllProductListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_list_row, parent, false);

        return new AllProductListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AllProductListAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String Project_Id = String.valueOf(exp.getProductId());
        String ProductName = exp.getProductName();
        String Emp_name = exp.getEmpname();
        String Assign_count= String.valueOf(exp.getAssignedCount());
        String Unassign_count= String.valueOf(exp.getUnassignedCount());
        String Module_count= String.valueOf(exp.getModuleCount());
        String Doc_count= String.valueOf(exp.getAttachementCount());


        holder.product_name.setText(ProductName);
        holder.emp_name_product.setText(Emp_name);
        holder.assign_count.setText(Assign_count);
        holder.not_assign_count.setText(Unassign_count);
        holder.module_count.setText(Module_count);
        holder.doc_count.setText(Doc_count);


       holder.active_inactive.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(holder.active_inactive.isChecked()){
                   holder.active_inactive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.toggleactive, 0, 0,0);
               }else {
                   holder.active_inactive.setCompoundDrawablesWithIntrinsicBounds(R.drawable.toggleinactive, 0, 0,0);


               }

           }
       });

    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(AllProductResponseItem s) {
        object.add(s);
        //notifyDataSetChanged();
    }


    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = context.getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

}
