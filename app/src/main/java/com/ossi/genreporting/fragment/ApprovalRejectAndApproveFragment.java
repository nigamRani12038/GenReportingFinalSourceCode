package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


public class ApprovalRejectAndApproveFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView text_header1, employee_name, text_for_select;
    TextView login_time;
    private String user_id, login_type;
    RoundedImageView img_profile;
    TextView emp_name, request_type, req_reason, from_date, to_date, total_day;
    //Spinner status;
    private String status_type,Emp_count;
    Button submit_status, submit_status_reject;
    String id, type;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String res;
    private SharedPreferences pref;
    String check_for_hr_req;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_approval_reject_and_approve, container, false);

        find_view_by_id(view);
        set_on_click_litioner();
        pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);
        user_id = pref.getString("user_id", null);
       // check_for_hr_req = pref.getString("show_req_from_hr", null);

        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");

        if (img_profile1 != null) {
            Glide.with(this).load(img_profile1).into(img_profile);
        }


        Bundle bundle = getArguments();
        if (bundle != null) {

            String Emp_Name = bundle.getString("Emp_Name");
            type = bundle.getString("type");
            Emp_count = bundle.getString("Emp_count");
            id = bundle.getString("id");
            String purpose = bundle.getString("purpose");
            String fdate = bundle.getString("fdate");
            String tdate = bundle.getString("tdate");
            String department = bundle.getString("department");
            String reporting_manager=bundle.getString("reporting_manager");
            String leaveType = bundle.getString("leaveType");
            String timeLeave=bundle.getString("timeLeave");

            emp_name.setText(Emp_Name);
            if (type.equalsIgnoreCase("Leave")) {
                request_type.setText("Reuest Type: " + type + "\n" + "Leave Type:  " + leaveType + "\n" + "Time of Leave: " + timeLeave);
            }else {
                request_type.setText("Reuest Type: " + type);

            }
            total_day.setText("Total Days: " + Emp_count);
            req_reason.setText("Purpose: " + purpose);
            from_date.setText("From: " + fdate);
            to_date.setText("To: " + tdate);

            if(type.equalsIgnoreCase("Expense")){
                from_date.setText("Ammount: " + tdate);
                to_date.setText(" " );
                total_day.setText("Link : " + Emp_count);
            }

            if (department.equalsIgnoreCase("Managment")) {
                submit_status.setVisibility(View.VISIBLE);
                submit_status_reject.setVisibility(View.VISIBLE);
            }else if (department.equalsIgnoreCase("HR")){
                submit_status.setVisibility(View.GONE);
                submit_status_reject.setVisibility(View.GONE);

                if(type.equalsIgnoreCase("Expense")){
                    from_date.setText("Ammount: " + tdate);
                    to_date.setText(" " );
                    total_day.setText("Link : " + Emp_count +"\n" +"Reporting Manager "+ reporting_manager);
                }else {
                    total_day.setText("Total Days: " + Emp_count+"\n" +"Reporting Manager:- "+ reporting_manager);
                }
            }

        }
        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: " + login_Time);

        text_header1.setText("Request For Approval");
        text_for_select.setText("");


        return view;
    }

    public void find_view_by_id(View view) {

        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);
        text_for_select = view.findViewById(R.id.text_for_select);

        emp_name = view.findViewById(R.id.emp_name);
        request_type = view.findViewById(R.id.request_type);
        req_reason = view.findViewById(R.id.req_reason);
        from_date = view.findViewById(R.id.from_date);
        to_date = view.findViewById(R.id.to_date);
        total_day = view.findViewById(R.id.total_day);
        //status = view.findViewById(R.id.status);

        submit_status = view.findViewById(R.id.submit_status);
        submit_status_reject = view.findViewById(R.id.submit_status_reject);
    }

    public void set_on_click_litioner() {
        submit_status.setOnClickListener(this);
        submit_status_reject.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_status:
                status_type = "Approved";
                if (type.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "No Any Requeste for Approval", Toast.LENGTH_SHORT).show();

                } else if (id.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Select Status", Toast.LENGTH_SHORT).show();

                } else {
                    if (Util.isNetworkAvailable(getActivity())) {
                        Submit_Request();
                    } else {
                        Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

                break;

            case R.id.submit_status_reject:
                status_type = "Reject";
                if (type.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "No Any Requeste for Approval", Toast.LENGTH_SHORT).show();

                } else if (id.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Select Status", Toast.LENGTH_SHORT).show();

                } else {
                    if (Util.isNetworkAvailable(getActivity())) {
                        Submit_Request();
                    } else {
                        Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    private void Submit_Request() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ApplyLeaveResponseItem>> call1 = apiInterface.req_submit(id, user_id, type, status_type,Emp_count);
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
                            Toast.makeText(getActivity(), "Request Accept Successfully", Toast.LENGTH_SHORT).show();
                            login_type = pref.getString("login_type", " ");
                            if (login_type.equalsIgnoreCase("1")) {
                                AdminTabLayoutFragment tabLayoutFragment = new AdminTabLayoutFragment();
                                openFragment1Tab(tabLayoutFragment);
                            } else {
                                TabLayoutFragment tabLayoutFragment = new TabLayoutFragment();
                                openFragment1Tab(tabLayoutFragment);
                            }

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

    private void openFragment1Tab(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment, "Tablayout"); // give your fragment container id in first parameter
       // transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        //transaction.isAddToBackStackAllowed();
        transaction.commit();
    }

}