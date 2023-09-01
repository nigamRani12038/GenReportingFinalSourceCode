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
import com.ossi.genreporting.fragment.HolidayFragment;
import com.ossi.genreporting.fragment.TaskSubmitFragment;
import com.ossi.genreporting.model.MyTaskListResponse;

import java.util.ArrayList;

public class MyTaskAdapter extends RecyclerView.Adapter<MyTaskAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<MyTaskListResponse> object;
    MyTaskListResponse exp;

    public MyTaskAdapter(ArrayList<MyTaskListResponse> list_task, Activity context) {
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
    public MyTaskAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_task_layout, parent, false);

        return new MyTaskAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyTaskAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String Project_Id = exp.getProject_id();
        String Task_id = exp.getTask_id();
        String Task_Name = exp.getTask_Module();
        String submit_Date=exp.getTask_Submit_Date();
        String deadline=exp.getTask_Deadline();
        String status=exp.getTask_Status();
        String assign_by=exp.getEmpName();
        String task_detail=exp.getTask_Detail();


        holder.project_nam.setText("Task Name-"+"\n"+Task_Name);
        holder.task_nam.setText("Task Details-"+"\n"+task_detail);
        //holder.task_nam.setText(Task_Name);
        holder.submit_proj_dat.setText("Submit Date"+"\n"+submit_Date);
        holder.deadline_proj.setText("TimeLine"+"\n"+deadline);

        holder.next_icn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskSubmitFragment taskSubmitFragment = new TaskSubmitFragment();
                String flag="my_task";
                Bundle bundle=new Bundle();
                bundle.putString("flag",flag);
                bundle.putString("taskId",Task_id);
                bundle.putString("Project_Name",Task_Name);
                bundle.putString("submit_Date",submit_Date);
                bundle.putString("TimeLine",deadline);
                bundle.putString("Task_status",status);
                bundle.putString("assign",assign_by);
                bundle.putString("task_detail",task_detail);
                bundle.putString("delay",exp.getDueDate());
                bundle.putString("start_date",exp.getTask_Submit_Date());
                taskSubmitFragment.setArguments(bundle);
                /* Bundle bundle=new Bundle();
                bundle.putString("flag",flag);
                bundle.putString("Project_Name",Task_Name);
                 bundle.putString("taskId",Task_id);
                bundle.putString("Project_id", exp.getProject_id());
                bundle.putString("assign", assign_by);
                bundle.putString("task_count",exp.getTask_Count());
               // bundle.putString("completed_task",exp.getCompleted_Tasks());
                bundle.putString("TimeLine",exp.getLastdeadline());
                bundle.putString("Task_status",exp.getTask_Status());
                bundle.putString("task_detail",exp.getTask_Detail());
                bundle.putString("start_date",exp.getAssign_By_Date());
                bundle.putString("Status_per",exp.getTStatus());
                bundle.putString("delay",exp.getDuedays());
                taskSubmitFragment.setArguments(bundle);*/
                openFragment1(taskSubmitFragment);
            }
        });



    }

    @Override
    public int getItemCount() {
        return object.size();
    }

    public void add(MyTaskListResponse s) {
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
