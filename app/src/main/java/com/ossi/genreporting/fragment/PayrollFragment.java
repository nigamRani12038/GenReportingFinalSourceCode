package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.R;


public class PayrollFragment extends Fragment implements View.OnClickListener {

View view;
    TextView text_header1, employee_name, text_for_select;
    Button approval_attendance,approval_salary;
    TextView login_time;
    private String user_id;
    RoundedImageView img_profile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_payroll, container, false);
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

        text_header1.setText("Payroll System");
        text_for_select.setText("");


        return view;
    }

    public void find_view_by_id(View view) {

        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);
        text_for_select = view.findViewById(R.id.text_for_select);

        approval_attendance = view.findViewById(R.id.approval_attendance);
        approval_salary = view.findViewById(R.id.approval_salary);



    }

    public void set_on_click_litioner() {
   approval_salary.setOnClickListener(this);
   approval_attendance.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.approval_attendance:
                PayrollAttendanceApprovalFragment payrollAttendanceApprovalFragment = new PayrollAttendanceApprovalFragment();
                openFragment1(payrollAttendanceApprovalFragment);
                break;
            case R.id.approval_salary:
                PayrollAprovalSalaryFragment payrollAprovalSalaryFragment = new PayrollAprovalSalaryFragment();
                openFragment1(payrollAprovalSalaryFragment);
                break;
        }
    }

    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment, ""); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }

}