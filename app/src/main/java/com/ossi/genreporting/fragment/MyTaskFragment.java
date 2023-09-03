package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.Adapter.MyTaskAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.decorators.RangeDayDecorator;
import com.ossi.genreporting.model.MyTaskListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyTaskFragment extends Fragment {
    View view;
    TextView text_header1, employee_name, text_for_select;
    TextView login_time;
    private String user_id,Project_Id;
    RoundedImageView img_profile;
    RecyclerView my_task_list;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private ArrayList<MyTaskListResponse> task_list;
    MyTaskAdapter task_adapter;
    private String Project_Name,Task_Name,Approve_status,Deadline,Submit_Date,Task_Detail,Status,Emp_Name_assign_by,Project_id;
    String Project_Name1,project_id;
    private String Task_id,Delay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_task, container, false);
        find_view_by_id();
        set_on_click_litioner();

        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);
        Bundle bundle = getArguments();
        if (bundle != null) {

             Project_Name1 = bundle.getString("Project_Name");
            project_id=bundle.getString("Project_id");

        }


        user_id = pref.getString("user_id", null);

        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");

        if (img_profile1 != null) {
            Glide.with(this).load(img_profile1).into(img_profile);
        }
        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: " + login_Time);

        text_for_select.setText("");
        text_header1.setText("My Task");

        if (Util.isNetworkAvailable(getActivity())){
            get_my_task_list();
        }
        else {
            Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    public void find_view_by_id() {
        text_for_select = view.findViewById(R.id.text_for_select);
        text_header1 = view.findViewById(R.id.text_header1);
        employee_name = view.findViewById(R.id.employee_name);
       login_time = view.findViewById(R.id.login_time);
        img_profile=view.findViewById(R.id.img_profile);
        my_task_list=view.findViewById(R.id.my_task_list);
    }

    public void set_on_click_litioner() {

    }

    private void get_my_task_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<MyTaskListResponse>> call1 = apiInterface.get_my_task_lists(user_id,project_id);
        call1.enqueue(new Callback<List<MyTaskListResponse>>() {
            @Override
            public void onResponse(Call<List<MyTaskListResponse>> call, Response<List<MyTaskListResponse>> response) {
                List<MyTaskListResponse> my_task_res = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (my_task_res != null && my_task_res.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    task_list = new ArrayList<>();
                    for (int i = 0; i < my_task_res.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        MyTaskListResponse   model=new MyTaskListResponse();
                        String res=my_task_res.get(i).getResponse();
                        if(res.equalsIgnoreCase("Success")) {
                            Project_Id = my_task_res.get(i).getProject_id();
                            Task_Name = my_task_res.get(i).getTask_Module();
                            //Approve_status = my_task_res.get(i).getTask_Status();
                            Deadline = my_task_res.get(i).getTask_Deadline();
                            Submit_Date = my_task_res.get(i).getTask_Submit_Date();
                            Task_Detail = my_task_res.get(i).getTask_Detail();
                            Status = my_task_res.get(i).getTask_Status();
                            Emp_Name_assign_by = my_task_res.get(i).getEmpName();
                            Task_id=my_task_res.get(i).getTask_id();
                            Delay=my_task_res.get(i).getDueDate();
                            //model.setProject_Name(Project_Name);
                            model.setProject_id(Project_Id);
                            model.setTask_Module(Task_Name);
                            model.setTask_Deadline(Deadline);
                            model.setTask_Submit_Date(Submit_Date);
                            model.setTask_Detail(Task_Detail);
                            model.setTask_Status(Status);
                            model.setEmpName(Emp_Name_assign_by);
                            model.setTask_id(Task_id);
                            model.setDueDate(Delay);
                            task_list.add(model);
                        }else {
                            Toast.makeText(getActivity(), "Not Yet assign task for you", Toast.LENGTH_SHORT).show();
                        }
                    }


                    task_adapter = new MyTaskAdapter(task_list, getActivity());
                    my_task_list.setAdapter(task_adapter);
                    my_task_list.setLayoutManager(new LinearLayoutManager(getActivity()));

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<List<MyTaskListResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

}