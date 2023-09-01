package com.ossi.genreporting.Adapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.fragment.MyTaskFragment;
import com.ossi.genreporting.fragment.TaskSubmitFragment;
import com.ossi.genreporting.model.MyProjectListResponse;

import java.util.ArrayList;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<MyProjectListResponse> object;
    MyProjectListResponse exp;

    public ProjectAdapter(ArrayList<MyProjectListResponse> list_task, Activity context) {
        this.object = list_task;
        this.context = (FragmentActivity) context;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView project_nam, task_nam,submit_proj_dat,deadline_proj;
        ImageView next_icn;

        public MyViewHolder(View view) {
            super(view);
            project_nam = view.findViewById(R.id.project_nam);
            task_nam = view.findViewById(R.id.task_nam);
            submit_proj_dat = view.findViewById(R.id.submit_proj_dat);
            deadline_proj = view.findViewById(R.id.deadline_proj);
            next_icn=view.findViewById(R.id.next_icn);

        }
    }


    @Override
    public ProjectAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_task_layout, parent, false);

        return new ProjectAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProjectAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String Project_Name = exp.getProject_Name();
        String project_id = exp.getProject_id();
        /*String submit_Date=exp.getTaskSubmetDate();
        String deadline=exp.getTaskDeadline();
        String status=exp.getStatus();
        String assign_by=exp.getEmpName();

        String task_detail=exp.getTaskDetails();*/



        holder.project_nam.setText(Project_Name);
        holder.task_nam.setVisibility(View.GONE);
        holder.submit_proj_dat.setVisibility(View.GONE);
        holder.deadline_proj.setVisibility(View.GONE);

        holder.next_icn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTaskFragment taskSubmitFragment = new MyTaskFragment();
                Bundle bundle=new Bundle();
                bundle.putString("Project_Name",Project_Name);
                bundle.putString("Project_id",project_id);
                taskSubmitFragment.setArguments(bundle);
                openFragment1(taskSubmitFragment);
            }
        });



    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(MyProjectListResponse s) {
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
