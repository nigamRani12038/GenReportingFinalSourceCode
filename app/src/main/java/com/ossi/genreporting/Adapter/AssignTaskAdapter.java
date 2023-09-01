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
import com.ossi.genreporting.fragment.TaskSubmitFragment;
import com.ossi.genreporting.model.MyProjectListResponse;

import java.util.ArrayList;

public class AssignTaskAdapter extends RecyclerView.Adapter<AssignTaskAdapter.MyViewHolder> {

    FragmentActivity context;
    private ArrayList<MyProjectListResponse> object;
    MyProjectListResponse exp;

    public AssignTaskAdapter(ArrayList<MyProjectListResponse> list_task, Activity context) {
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
    public AssignTaskAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_task_layout, parent, false);

        return new AssignTaskAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssignTaskAdapter.MyViewHolder holder, int position) {
        exp = object.get(position);
        String Project_Name = exp.getProject_Name();
        String Project_id = exp.getProject_id();
        String Task_count = exp.getTask_Count();
        String Completed_Task=exp.getCompleted_Tasks();
        String TimeLine=exp.getLastdeadline();
        String status=exp.getTask_Status();
        String assign_to=exp.getEmpName();
        String Status_per=exp.getTStatus();
        String task_details=exp.getTask_Detail();
        String start_date=exp.getAssign_By_Date();

        holder.project_nam.setText(Project_Name);

      /*  holder.task_nam.setText(Project_Name);
        holder.submit_proj_dat.setText(status);
        holder.deadline_proj.setText(TimeLine);*/
        holder.task_nam.setVisibility(View.GONE);
        holder.submit_proj_dat.setVisibility(View.GONE);
        holder.deadline_proj.setVisibility(View.GONE);

        holder.next_icn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskSubmitFragment taskSubmitFragment = new TaskSubmitFragment();
                String flag="assign_task";
                Bundle bundle=new Bundle();
                bundle.putString("flag",flag);
                bundle.putString("Project_Name",Project_Name);
                bundle.putString("Project_id", exp.getProject_id());
                bundle.putString("assign", exp.getEmpName());
                bundle.putString("task_count",exp.getTask_Count());
                bundle.putString("completed_task",exp.getCompleted_Tasks());
                bundle.putString("TimeLine",exp.getLastdeadline());
                bundle.putString("Task_status",exp.getTask_Status());
                bundle.putString("task_detail",exp.getTask_Detail());
                bundle.putString("start_date",exp.getAssign_By_Date());
                bundle.putString("Status_per",exp.getTStatus());
                bundle.putString("delay",exp.getDuedays());
                taskSubmitFragment.setArguments(bundle);
                openFragment1(taskSubmitFragment);
            }
        });

        /*if (Emp_Event.equalsIgnoreCase("DOB")) {
            holder.event.setText("It is " + event_name + " Birthday Today");
            holder.wish.setText("write best wishes on his wall");
            //It is Utkarsh's Birthday Today
        } else if (Emp_Event.equalsIgnoreCase("Married")) {
            holder.event.setText("It is " + event_name + " Work Aniversary Today");
            holder.wish.setText("write best wishes on his wall");
        } else if (Emp_Event.equalsIgnoreCase("HoliDay")) {

            String[] dateParts = event_name.split(",");
            String eventname = dateParts[0];
            String date = dateParts[1];
            holder.event.setText("It is " + eventname + " You have a Holiday on the " + date);
            holder.wish.setText("Today your Enjoy day");
        }*/


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
