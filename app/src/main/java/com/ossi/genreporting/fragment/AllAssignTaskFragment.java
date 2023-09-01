package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.Adapter.AssignTaskAdapter;
import com.ossi.genreporting.Adapter.MyTaskAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.decorators.RangeDayDecorator;
import com.ossi.genreporting.model.DetailsResponseItem;
import com.ossi.genreporting.model.MyProjectListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllAssignTaskFragment extends Fragment implements View.OnClickListener {
    TextView text_header1, employee_name,text_for_select;
    TextView login_time;
    RecyclerView assign_task_list_rv;
    View view;
    private String user_id;
    RoundedImageView img_profile;
    Button add_new_task_assigned_btn;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private ArrayList<MyProjectListResponse> task_list;
    AssignTaskAdapter task_adapter;
    private String Project_Name,Task_Name,Approve_status,Deadline,Submit_Date,Task_Detail,Status,Emp_Name,project_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_all_assign_task, container, false);

        find_view_by_id(view);
        set_on_click_litioner();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);

        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");

        if (img_profile1 != null) {
            Glide.with(this).load(img_profile1).into(img_profile);
        }

        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: " + login_Time);

        text_header1.setText("Assigned Task");
        text_for_select.setText("");


        if (isNetworkAvailable()){
            get_assign_project_list();
        }
        else {
            Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public void find_view_by_id(View view){
        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);
        add_new_task_assigned_btn=view.findViewById(R.id.add_new_task_assigned_btn);
        text_for_select=view.findViewById(R.id.text_for_select);

        assign_task_list_rv=view.findViewById(R.id.assign_task_list_rv);

    }
    public void set_on_click_litioner(){
        add_new_task_assigned_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_new_task_assigned_btn:
                 ProjectFragments projectFragments = new ProjectFragments();
                 openFragment(projectFragments);
                break;
        }
    }

    private void get_assign_project_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<MyProjectListResponse>> call1 = apiInterface.get_assign_task_lists(user_id);
        call1.enqueue(new Callback<List<MyProjectListResponse>>() {
            @Override
            public void onResponse(Call<List<MyProjectListResponse>> call, Response<List<MyProjectListResponse>> response) {
                List<MyProjectListResponse> my_task_res = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (my_task_res != null && my_task_res.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    task_list = new ArrayList<>();
                    for (int i = 0; i < my_task_res.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        MyProjectListResponse   model=new MyProjectListResponse();
                        String res=my_task_res.get(i).getResponse();
                        if(!res.equalsIgnoreCase("Fail")) {
                            Project_Name = my_task_res.get(i).getProject_Name();
                            project_id = my_task_res.get(i).getProject_id();

                            String duday = my_task_res.get(i).getDuedays();
                            String timline = my_task_res.get(i).getLastdeadline();
                            String task_count = my_task_res.get(i).getTask_Count();
                            String complete_task = my_task_res.get(i).getCompleted_Tasks();
                            String status_per = my_task_res.get(i).getTStatus();
                            String status=my_task_res.get(i).getTask_Status();
                            String task_details=my_task_res.get(i).getTask_Detail();
                            String task_assigned_date=my_task_res.get(i).getAssign_By_Date();

                            Emp_Name = my_task_res.get(i).getEmpName();


                            model.setProject_Name(Project_Name);
                            model.setProject_id(project_id);
                            model.setDuedays(duday);
                            model.setLastdeadline(timline);
                            model.setTask_Count(task_count);
                            model.setCompleted_Tasks(complete_task);
                            model.setTStatus(status_per);
                            model.setTask_Status(status);
                            model.setEmpName(Emp_Name);
                            model.setTask_Detail(task_details);
                            model.setAssign_By_Date(task_assigned_date);
                            task_list.add(model);
                        }else {
                            Toast.makeText(getActivity(), "No yet assigned task by you", Toast.LENGTH_SHORT).show();
                        }
                    }


                    task_adapter = new AssignTaskAdapter(task_list, getActivity());
                    assign_task_list_rv.setAdapter(task_adapter);
                    assign_task_list_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<List<MyProjectListResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
    public boolean isNetworkAvailable() {
        final android.net.ConnectivityManager connectivityManager = ((android.net.ConnectivityManager) getActivity().getSystemService(android.content.Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}