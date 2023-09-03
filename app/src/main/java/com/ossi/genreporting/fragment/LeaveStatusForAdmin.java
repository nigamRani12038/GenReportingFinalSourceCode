package com.ossi.genreporting.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ossi.genreporting.Adapter.HolidayAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.ApplyLeaveResponseItem;
import com.ossi.genreporting.model.AttendanceStatusResponse;
import com.ossi.genreporting.model.HolidayListResponse;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveStatusForAdmin extends Fragment implements View.OnClickListener {
    View view;
    TextView present_employee, absentemployee, workfromhomeemployee, onleaveemployee;
    PieChart mPieChart;

    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String res;
    int leave,absent,present,wfh,biometric,field;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leave_status_for_admin, container, false);
        find_view_by_id();
        set_on_click_litioner();
         if(Util.isNetworkAvailable(getActivity())) {
             get_attendance_list();
         }else {
             Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
         }

        return view;
    }

    public void find_view_by_id() {
        mPieChart = (PieChart) view.findViewById(R.id.piechart);
        present_employee = view.findViewById(R.id.present_employee);
        absentemployee = view.findViewById(R.id.absentemployee);
        workfromhomeemployee = view.findViewById(R.id.workfromhomeemployee);
        onleaveemployee = view.findViewById(R.id.onleaveemployee);

    }

    private void setData() {

        // Set the percentage of language used
        present_employee.setText("Present: " + Integer.toString(present));
        absentemployee.setText("Absent: " + Integer.toString(absent));
        workfromhomeemployee.setText("WFH: " + Integer.toString(wfh));
        onleaveemployee.setText("Leave: " + Integer.toString(leave));

        // Set the data and color to the pie chart
        mPieChart.addPieSlice(
                new PieModel(
                        "Present",
                        Integer.parseInt(Integer.toString(present)),
                        Color.parseColor("#CFF09F")));
        mPieChart.addPieSlice(
                new PieModel(
                        "work from home employee",
                        Integer.parseInt(Integer.toString(wfh)),
                        Color.parseColor("#A9DBA8")));
        mPieChart.addPieSlice(
                new PieModel(
                        "on leave employee",
                        Integer.parseInt(Integer.toString(leave)),
                        Color.parseColor("#255152")));
        mPieChart.addPieSlice(
                new PieModel(
                        "absent employee",
                        Integer.parseInt(Integer.toString(absent)),
                        Color.parseColor("#0C486C")));

        // To animate the pie chart
        mPieChart.startAnimation();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.present_employee:
                EmployeeCurrentstatusFragment employeeCurrentstatusFragment = new EmployeeCurrentstatusFragment();
                Bundle bundle=new Bundle();
                bundle.putString("type","present");
                bundle.putString("totalpresent", String.valueOf(present));
                bundle.putString("biometric", String.valueOf(biometric));
                bundle.putString("wfh", String.valueOf(wfh));
                bundle.putString("absent", String.valueOf(absent));
                bundle.putString("leave", String.valueOf(leave));
                bundle.putString("field", String.valueOf(field));
                employeeCurrentstatusFragment.setArguments(bundle);
                openFragment1(employeeCurrentstatusFragment);
                //openMyDialog();
                break;

            case R.id.absentemployee:
                EmployeeCurrentstatusFragment employeeCurrentstatusFragment1 = new EmployeeCurrentstatusFragment();
                Bundle bundle1=new Bundle();
                bundle1.putString("type","absent");
                bundle1.putString("totalpresent", String.valueOf(present));
                bundle1.putString("biometric", String.valueOf(biometric));
                bundle1.putString("wfh", String.valueOf(wfh));
                bundle1.putString("absent", String.valueOf(absent));
                bundle1.putString("leave", String.valueOf(leave));
                bundle1.putString("field", String.valueOf(field));
                employeeCurrentstatusFragment1.setArguments(bundle1);
                openFragment1(employeeCurrentstatusFragment1);
                break;
            case R.id.workfromhomeemployee:
                EmployeeCurrentstatusFragment employeeCurrentstatusFragment2 = new EmployeeCurrentstatusFragment();
                Bundle bundle2=new Bundle();
                bundle2.putString("type","wfh");
                bundle2.putString("totalpresent", String.valueOf(present));
                bundle2.putString("biometric", String.valueOf(biometric));
                bundle2.putString("wfh", String.valueOf(wfh));
                bundle2.putString("absent", String.valueOf(absent));
                bundle2.putString("leave", String.valueOf(leave));
                bundle2.putString("field", String.valueOf(field));
                employeeCurrentstatusFragment2.setArguments(bundle2);
                openFragment1(employeeCurrentstatusFragment2);
                break;
            case R.id.onleaveemployee:
                EmployeeCurrentstatusFragment employeeCurrentstatusFragment3 = new EmployeeCurrentstatusFragment();
                Bundle bundle3=new Bundle();
                bundle3.putString("type","onleave");
                bundle3.putString("totalpresent", String.valueOf(present));
                bundle3.putString("biometric", String.valueOf(biometric));
                bundle3.putString("wfh", String.valueOf(wfh));
                bundle3.putString("absent", String.valueOf(absent));
                bundle3.putString("leave", String.valueOf(leave));
                bundle3.putString("field", String.valueOf(field));
                employeeCurrentstatusFragment3.setArguments(bundle3);
                openFragment1(employeeCurrentstatusFragment3);
                break;
        }
    }

public void set_on_click_litioner(){
    present_employee.setOnClickListener(this);
    absentemployee.setOnClickListener(this);
    workfromhomeemployee.setOnClickListener(this);
    onleaveemployee.setOnClickListener(this);
}
    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    private void get_attendance_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<AttendanceStatusResponse>> call1 = apiInterface.get_attendance_status();
        call1.enqueue(new Callback<List<AttendanceStatusResponse>>() {
            @Override
            public void onResponse(Call<List<AttendanceStatusResponse>> call, Response<List<AttendanceStatusResponse>> response) {
                List<AttendanceStatusResponse> applyLeaveResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (applyLeaveResponseItem != null && applyLeaveResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < applyLeaveResponseItem.size(); i++) {
                        res = applyLeaveResponseItem.get(i).getResponse();


                        if (res.equalsIgnoreCase("success")) {
                            leave = Integer.parseInt(applyLeaveResponseItem.get(i).getLeave());
                            absent = Integer.parseInt(applyLeaveResponseItem.get(i).getAbsent());
                            present = Integer.parseInt(applyLeaveResponseItem.get(i).getPresent());
                            wfh = Integer.parseInt(applyLeaveResponseItem.get(i).getWFH());
                               biometric = Integer.parseInt(applyLeaveResponseItem.get(i).getBiometric());
                            field = Integer.parseInt(applyLeaveResponseItem.get(i).getField());
                            setData();
                           /* Toast.makeText(getActivity(), " Apply Leave Success", Toast.LENGTH_SHORT).show();
                            TabLayoutFragment tabLayoutFragment = new TabLayoutFragment();
                            openFragment1(tabLayoutFragment);*/
                        }else {

                        }




                    }


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<AttendanceStatusResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

}