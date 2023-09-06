package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ossi.genreporting.Adapter.ViewLeaveAdapter;
import com.ossi.genreporting.Adapter.WfhViewAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.ViewLeaveStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeaveViewFragment extends Fragment implements View.OnClickListener {
   // Button apply_leave_;
    View view;
    Spinner leave_spin, leave_approve_type, leave_typ;

    String month1, approved_type, Leave_type;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String user_id;
    private ViewLeaveStatus model;
    private ArrayList<ViewLeaveStatus> wfh_list;
    private ViewLeaveAdapter leave_adapter;
    RecyclerView leave_rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leave_view, container, false);
        find_view_by_id(view);
        set_on_click_litioner();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);


        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Select Months");
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("4");
        arrayList.add("5");
        arrayList.add("6");
        arrayList.add("7");
        arrayList.add("8");
        arrayList.add("9");
        arrayList.add("10");
        arrayList.add("11");
        arrayList.add("12");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leave_spin.setAdapter(arrayAdapter);
        leave_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month1 = leave_spin.getSelectedItem().toString();
                if (leave_spin.getSelectedItem().equals("Select Months")) {
                   // Toast.makeText(getActivity(), "Please Select Months", Toast.LENGTH_SHORT).show();
                } else if (leave_approve_type.getSelectedItem().equals("Select Approved Type")) {
                    Toast.makeText(getActivity(), "Please Select Approved Type", Toast.LENGTH_SHORT).show();
                } else if (leave_typ.getSelectedItem().equals("Select Leave Type")) {
                    Toast.makeText(getActivity(), "Please Select Leave Type", Toast.LENGTH_SHORT).show();
                } else {
                    Get_leave_data_();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Select Approved Type");
        arrayList1.add("Approved");
        arrayList1.add("Pending");
        arrayList1.add("Reject");


        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leave_approve_type.setAdapter(arrayAdapter1);
        leave_approve_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                approved_type = leave_approve_type.getSelectedItem().toString();
                if (leave_spin.getSelectedItem().equals("Select Months")) {
                   // Toast.makeText(getActivity(), "Please Select Months", Toast.LENGTH_SHORT).show();
                } else if (leave_approve_type.getSelectedItem().equals("Select Approved Type")) {
                    Toast.makeText(getActivity(), "Please Select Approved Type", Toast.LENGTH_SHORT).show();
                } else if (leave_typ.getSelectedItem().equals("Select Leave Type")) {
                    Toast.makeText(getActivity(), "Please Select Leave Type", Toast.LENGTH_SHORT).show();
                } else {
                    Get_leave_data_();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add("Select Leave Type");
        arrayList2.add("Casual Leave");
        arrayList2.add("Earned Leave");
        arrayList2.add("Sick Leave");
        arrayList2.add("Short Leave");
        arrayList2.add("Halfday Leave");


        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leave_typ.setAdapter(arrayAdapter2);
        leave_typ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Leave_type = leave_typ.getSelectedItem().toString();
                if (leave_spin.getSelectedItem().equals("Select Months")) {
                    Toast.makeText(getActivity(), "Please Select Months", Toast.LENGTH_SHORT).show();
                } else if (leave_approve_type.getSelectedItem().equals("Select Approved Type")) {
                    Toast.makeText(getActivity(), "Please Select Approved Type", Toast.LENGTH_SHORT).show();
                } else if (leave_typ.getSelectedItem().equals("Select Leave Type")) {
                    Toast.makeText(getActivity(), "Please Select Leave Type", Toast.LENGTH_SHORT).show();
                } else {
                    Get_leave_data_();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        return view;
    }

    public void find_view_by_id(View view) {
   leave_spin = view.findViewById(R.id.leave_spin);
        leave_rv = view.findViewById(R.id.leave_rv);
        leave_approve_type = view.findViewById(R.id.leave_approve_type);
        leave_typ = view.findViewById(R.id.leave_typ);

    }

    public void set_on_click_litioner() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }



    private void Get_leave_data_() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ViewLeaveStatus>> call1 = apiInterface.get_view_leave(user_id, month1, approved_type, Leave_type);
        call1.enqueue(new Callback<List<ViewLeaveStatus>>() {
            @Override
            public void onResponse(Call<List<ViewLeaveStatus>> call, Response<List<ViewLeaveStatus>> response) {
                List<ViewLeaveStatus> ViewLeaveStatus = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (ViewLeaveStatus != null && ViewLeaveStatus.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    wfh_list = new ArrayList<>();

                    for (int i = 0; i < ViewLeaveStatus.size(); i++) {
                        String res = ViewLeaveStatus.get(i).getResponse();
                        model = new ViewLeaveStatus();
                        if (res.equalsIgnoreCase("success")) {
                            String response1 = ViewLeaveStatus.get(i).getResponse();
                            String leave_status = ViewLeaveStatus.get(i).getStatus();
                            String purpose = ViewLeaveStatus.get(i).getPurpose();
                            String data = ViewLeaveStatus.get(i).getFromDate();
                            String approvebyname = ViewLeaveStatus.get(i).getApprove_name();
                            String leave_type = ViewLeaveStatus.get(i).getTypeLeave();
                            model.setResponse(response1);
                            model.setStatus(leave_status);
                            model.setPurpose(purpose);
                            model.setFromDate(data);
                            model.setApprove_name(approvebyname);
                            model.setTypeLeave(leave_type);
                            wfh_list.add(model);


                        } else if (res.equalsIgnoreCase("Fail")) {
                            Toast.makeText(getActivity(), "No Any Leave Apply", Toast.LENGTH_SHORT).show();
                        }


                    }
                    leave_adapter = new ViewLeaveAdapter(wfh_list, getActivity());
                    leave_rv.setAdapter(leave_adapter);
                    leave_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<ViewLeaveStatus>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


}