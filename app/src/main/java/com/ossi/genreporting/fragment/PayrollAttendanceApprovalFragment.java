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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.Adapter.AssignTaskAdapter;
import com.ossi.genreporting.Adapter.AttendanceAprovalShowAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.MyProjectListResponse;
import com.ossi.genreporting.model.PayrollAttendanceShowResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PayrollAttendanceApprovalFragment extends Fragment {


    View view;
    TextView text_header1, employee_name, text_for_select;

    TextView generated_already;
    TextView login_time;
    private String user_id;
    RoundedImageView img_profile;

    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String res;
    RecyclerView atandence_status;
    LinearLayout ll_show_data,ll_msg_show;
    AttendanceAprovalShowAdapter attendanceAprovalShowAdapter;
    private ArrayList<PayrollAttendanceShowResponse> attendance_list;
    Button attendance_approved;
    LottieAnimationView animate_wait,animate_approved;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_payroll_attendance_approval, container, false);
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

        text_header1.setText("Request For Approval Attendance");
        text_for_select.setText("");
        if (Util.isNetworkAvailable(getActivity())) {
            Request_approval_show_attendance();
        }else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        attendance_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkAvailable(getActivity())) {
                   Approved_Attendance();
                }else {
                    Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }

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
        generated_already=view.findViewById(R.id.generated_already);
        atandence_status=view.findViewById(R.id.atandence_status);
        ll_msg_show=view.findViewById(R.id.ll_msg_show);
        ll_show_data=view.findViewById(R.id.ll_show_data);
        attendance_approved=view.findViewById(R.id.attendance_approved);
        animate_approved=view.findViewById(R.id.animate_approved);
        animate_wait=view.findViewById(R.id.animate_wait);


    }

    public void set_on_click_litioner() {

    }


    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment, ""); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }
    private void Request_approval_show_attendance() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
        Call<List<PayrollAttendanceShowResponse>> call1 = apiInterface.req_attendance_approval(user_id);
        call1.enqueue(new Callback<List<PayrollAttendanceShowResponse>>() {
            @Override
            public void onResponse(Call<List<PayrollAttendanceShowResponse>> call, Response<List<PayrollAttendanceShowResponse>> response) {
                List<PayrollAttendanceShowResponse> payrollAttendanceShowResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (payrollAttendanceShowResponse != null && payrollAttendanceShowResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    attendance_list = new ArrayList<>();
                    for (int i = 0; i < payrollAttendanceShowResponse.size(); i++) {
                        res = payrollAttendanceShowResponse.get(i).getResponse();
                        PayrollAttendanceShowResponse model=new PayrollAttendanceShowResponse();
                        if (res.equalsIgnoreCase("January month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("January month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                       else if (res.equalsIgnoreCase("February month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("February month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if (res.equalsIgnoreCase("March month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("March month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if (res.equalsIgnoreCase("April month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("April month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if (res.equalsIgnoreCase("May month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("May month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if (res.equalsIgnoreCase("Jun month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("Jun month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if (res.equalsIgnoreCase("July month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("July month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if (res.equalsIgnoreCase("August month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("August month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }else if (res.equalsIgnoreCase("September month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("September month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }else if (res.equalsIgnoreCase("October month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("October month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }else if (res.equalsIgnoreCase("November month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("November month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }else if (res.equalsIgnoreCase("December month attendance not generated yet.")) {
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("December month attendance not generated yet.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }




                        else if(res.equalsIgnoreCase("January month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("January month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("February month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("February month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("March month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("March month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("April month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("April month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("May month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("May month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("Jun month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("Jun month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("July month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("July month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("August month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("August month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("September month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("September month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }else if(res.equalsIgnoreCase("October month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("October month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("November month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("November month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }else if(res.equalsIgnoreCase("December month attendance has already been approved")){
                            ll_msg_show.setVisibility(View.VISIBLE);
                            generated_already.setText("December month attendance has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }


                        else if(res.equalsIgnoreCase("Invalid UserID")){
                            Toast.makeText(getActivity(), "" + res, Toast.LENGTH_SHORT).show();
                            ll_msg_show.setVisibility(View.GONE);
                            ll_show_data.setVisibility(View.GONE);

                        }else if(res.equalsIgnoreCase("success")){
                           // Toast.makeText(getActivity(), " Show data", Toast.LENGTH_SHORT).show();
                            ll_show_data.setVisibility(View.VISIBLE);
                            ll_msg_show.setVisibility(View.GONE);
                            String Name=payrollAttendanceShowResponse.get(i).getEmpName();
                            String absent=payrollAttendanceShowResponse.get(i).getTotalAbsent();
                            String adjust_day=payrollAttendanceShowResponse.get(i).getAdjustDay();
                            String total_day=payrollAttendanceShowResponse.get(i).getTotalDay();
                            String Total_leave=payrollAttendanceShowResponse.get(i).getTotalLeave();
                            String Total_weekoff=payrollAttendanceShowResponse.get(i).getWeekOff();
                            String Total_present=payrollAttendanceShowResponse.get(i).getTotalPresent();
                            String unique_id=payrollAttendanceShowResponse.get(i).getId();

                            model.setId(unique_id);
                            model.setEmpName(Name);
                            model.setTotalAbsent(absent);
                            model.setAdjustDay(adjust_day);
                            model.setTotalDay(total_day);
                            model.setTotalLeave(Total_leave);
                            model.setWeekOff(Total_weekoff);
                            model.setTotalPresent(Total_present);





                            attendance_list.add(model);

                        }


                    }

                    attendanceAprovalShowAdapter = new AttendanceAprovalShowAdapter(attendance_list, getActivity());
                    atandence_status.setAdapter(attendanceAprovalShowAdapter);
                    atandence_status.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<PayrollAttendanceShowResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }
    private void Approved_Attendance() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<PayrollAttendanceShowResponse>> call1 = apiInterface.approved_attendance(user_id,"yes");
        call1.enqueue(new Callback<List<PayrollAttendanceShowResponse>>() {
            @Override
            public void onResponse(Call<List<PayrollAttendanceShowResponse>> call, Response<List<PayrollAttendanceShowResponse>> response) {
                List<PayrollAttendanceShowResponse> payrollAttendanceShowResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (payrollAttendanceShowResponse != null && payrollAttendanceShowResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    for (int i = 0; i < payrollAttendanceShowResponse.size(); i++) {
                        res = payrollAttendanceShowResponse.get(i).getResponse();
                        PayrollAttendanceShowResponse model=new PayrollAttendanceShowResponse();
                        if (res.equalsIgnoreCase("success")) {
                            Toast.makeText(getActivity(), " Approved Success", Toast.LENGTH_SHORT).show();
                            AdminTabLayoutFragment tabLayoutFragment = new AdminTabLayoutFragment();
                            openFragment1Tab(tabLayoutFragment);

                        }else {
                            Toast.makeText(getActivity(), "res "+res, Toast.LENGTH_SHORT).show();
                        }


                    }

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<PayrollAttendanceShowResponse>> call, Throwable t) {
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
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }

}