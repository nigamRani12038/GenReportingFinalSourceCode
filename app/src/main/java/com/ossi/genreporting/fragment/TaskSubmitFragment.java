package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.ApplyLeaveResponseItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskSubmitFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView text_header1, employee_name, text_for_select;
    TextView login_time;
    TextView project_name, task_name, sub_dat, dead_date, emp_nam,delay_status;
    private String user_id;
    RoundedImageView img_profile;
    Button my_task_submit_btn;
    EditText task_detail;
    Spinner status;
    String remark,Status;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String res;
    String Task_id,Project_id;
    private String Task_status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task_submit, container, false);

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

        Bundle bundle = getArguments();
        if (bundle != null) {
            String flag = bundle.getString("flag");
            String Project_Name = bundle.getString("Project_Name");
             Project_id = bundle.getString("Project_id");
            String assign_to_and_by = bundle.getString("assign");
            String Task_count=bundle.getString("task_count");
            String Completed_task=bundle.getString("completed_task");
            String TimeLine=bundle.getString("TimeLine");
            String Task_status = bundle.getString("Task_status");
            String Task_details = bundle.getString("task_detail");
            String start_date = bundle.getString("start_date");
            String Status_per = bundle.getString("Status_per");
            String delay=bundle.getString("delay");

            project_name.setText(Project_Name);

            sub_dat.setText("Start Date"+"\n"+start_date);
            dead_date.setText("TimeLine"+"\n"+TimeLine);
            //emp_nam.setText("Assign To"+"\n"+assign_to_and_by);

            //status.setHint("Status");
            //status.setTag("Status");
            task_detail.setHint("Remark");



            if (flag.equalsIgnoreCase("assign_task")) {
                my_task_submit_btn.setVisibility(View.GONE);
                //status.setText(Task_status);
                status.setVisibility(View.GONE);
                //status.setTag(Task_status);
                task_detail.setText("Task Details"+"\n"+Task_details);
                task_name.setText("Task  Status"+"\n"+Status_per+"%");
                text_header1.setText("Task Status");
                emp_nam.setText("Assign To"+"\n"+assign_to_and_by);
                delay_status.setText(delay);
            } else if (flag.equalsIgnoreCase("my_task")) {
                 Task_id=bundle.getString("taskId");
                my_task_submit_btn.setVisibility(View.VISIBLE);
                status.setVisibility(View.VISIBLE);
               // status.setTag("Status");
                if(Task_status.equalsIgnoreCase("Complete")) {
                    task_detail.setHint(Task_details);
                    my_task_submit_btn.setVisibility(View.GONE);
                }else {
                    task_detail.setHint("Remark");
                }
                task_name.setText("Task  Status"+"\n"+Task_status);
                emp_nam.setText("Assign By"+"\n"+assign_to_and_by);
                text_header1.setText("Task Submit");
                delay_status.setText(delay);

            }
        }

        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: " + login_Time);

      //  text_header1.setText("Task Submit");
        text_for_select.setText("");

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select Status");
        arrayList.add("Work In Progress");
        arrayList.add("Complete");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status.setAdapter(arrayAdapter);
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Task_status = parent.getItemAtPosition(position).toString();
                // Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });



        return view;
    }

    public void find_view_by_id(View view) {
        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);
        text_for_select = view.findViewById(R.id.text_for_select);
        my_task_submit_btn = view.findViewById(R.id.my_task_submit_btn);

        project_name = view.findViewById(R.id.project_name);
        task_name = view.findViewById(R.id.task_name);
        sub_dat = view.findViewById(R.id.sub_dat);
        dead_date = view.findViewById(R.id.dead_date);
        emp_nam = view.findViewById(R.id.emp_nam);

        task_detail = view.findViewById(R.id.task_detail);
        status = view.findViewById(R.id.status);
        delay_status=view.findViewById(R.id.delay_status);
    }

    public void set_on_click_litioner() {
        my_task_submit_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_task_submit_btn:
                remark=task_detail.getText().toString();
                //Status=status.getText().toString();
                Status=status.getSelectedItem().toString();
                if (remark.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Remark", Toast.LENGTH_SHORT).show();
                }else if (Status.equalsIgnoreCase("Select Status")){
                    Toast.makeText(getActivity(), "Please Select Status", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (Util.isNetworkAvailable(getActivity())) {
                        Submit_Task();
                    } else {
                        Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }

    private void Submit_Task() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ApplyLeaveResponseItem>> call1 = apiInterface.submit_task(user_id, Task_status, remark,Project_id,Task_id);
        call1.enqueue(new Callback<List<ApplyLeaveResponseItem>>() {
            @Override
            public void onResponse(Call<List<ApplyLeaveResponseItem>> call, Response<List<ApplyLeaveResponseItem>> response) {
                List<ApplyLeaveResponseItem> applyLeaveResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (applyLeaveResponseItem != null && applyLeaveResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < applyLeaveResponseItem.size(); i++) {
                        res = applyLeaveResponseItem.get(i).getResponse();

                        if (res.equalsIgnoreCase("success")) {
                            Toast.makeText(getActivity(), " Submit Task", Toast.LENGTH_SHORT).show();
                            TabLayoutFragment tabLayoutFragment = new TabLayoutFragment();
                            openFragment(tabLayoutFragment);
                        } else {
                            Toast.makeText(getActivity(), "" + res, Toast.LENGTH_SHORT).show();

                        }


                    }


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<ApplyLeaveResponseItem>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment); // give your fragment container id in first parameter
         transaction.addToBackStack(null);  // if written, this transaction will be added to backstack

        transaction.commit();
    }

}