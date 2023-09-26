package com.ossi.genreporting.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ossi.genreporting.Adapter.EmployeeAbsentAdapter;
import com.ossi.genreporting.Adapter.EmployeeCurrentAdapter;
import com.ossi.genreporting.Adapter.EmployeeLeaveAdapter;
import com.ossi.genreporting.Adapter.EmployeeWFHAdapter;
import com.ossi.genreporting.Adapter.HolidayAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.AbsentStatusResponse;
import com.ossi.genreporting.model.AttendanceStatusResponse;
import com.ossi.genreporting.model.EmployeeListResponse;
import com.ossi.genreporting.model.HolidayListResponse;
import com.ossi.genreporting.model.LeaveStatusREsponse;
import com.ossi.genreporting.model.PresentStatusItem;
import com.ossi.genreporting.model.WFHStatusResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EmployeeCurrentstatusFragment extends Fragment {
    View view;
    TextView type, ofc_count, wfh_count, working_from_reason;
    String type_status, totalpresent, biometric, wfh, absent, leave, field;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String res;
    RecyclerView employee_status_rv;

    private ArrayList<PresentStatusItem> present_list;
    EmployeeCurrentAdapter employeeCurrentAdapter;

    private ArrayList<AbsentStatusResponse> absent_list;
    EmployeeAbsentAdapter employeeAbsentAdapter;

    private ArrayList<LeaveStatusREsponse> leaves_list;
    EmployeeLeaveAdapter employeeLeaveAdapter;

    private ArrayList<WFHStatusResponse> wfh_list;
    EmployeeWFHAdapter employeeWFHAdapter;
    EditText search;
    Spinner working_type;
    String workType;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.employees_current_status_view, container, false);
        find_view_by_id();
        set_on_click_litioner();


        Bundle bundle = getArguments();
        if (bundle != null) {
            type_status = bundle.getString("type");
            totalpresent = bundle.getString("totalpresent");
            biometric = bundle.getString("biometric");
            wfh = bundle.getString("wfh");
            absent = bundle.getString("absent");
            leave = bundle.getString("leave");
            field = bundle.getString("field");

            //Toast.makeText(getActivity(), ""+approve_type_exp, Toast.LENGTH_SHORT).show();
            if (Util.isNetworkAvailable(getActivity())) {
                if (type_status.equalsIgnoreCase("present")) {
                    filterEmploye();
                    ofc_count.setText("Office: " + biometric + ", Field: " + field);
                    wfh_count.setText("WFH: " + wfh);
                } else if (type_status.equalsIgnoreCase("absent")) {
                    type.setText("Absent");
                    ofc_count.setText("");
                    wfh_count.setText(absent);
                    working_from_reason.setText("");
                   // working_type.setVisibility(View.GONE);
                    filterEmploye();
                    // get_allabsent_list();
                } else if (type_status.equalsIgnoreCase("wfh")) {
                    type.setText("WFH");
                    wfh_count.setText("WFH: " + wfh);
                    ofc_count.setText("");
                    working_from_reason.setText("Reason");
                    working_type.setVisibility(View.GONE);

                    get_wfh_list();
                } else if (type_status.equalsIgnoreCase("onleave")) {
                    type.setText("Leave");
                    ofc_count.setText("");
                    wfh_count.setText(leave);
                    working_from_reason.setText("Leave Type");
                    //working_type.setVisibility(View.GONE);
                    filterEmploye();
                    //get_allleave_list();
                }
            } else {
                Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }


        return view;
    }


    public void find_view_by_id() {
        type = view.findViewById(R.id.type);
        ofc_count = view.findViewById(R.id.ofc_count);
        wfh_count = view.findViewById(R.id.wfh_count);
        working_from_reason = view.findViewById(R.id.working_from_reason);
        employee_status_rv = view.findViewById(R.id.employee_status_rv);
        search = view.findViewById(R.id.search);
        working_type = view.findViewById(R.id.working_type);
    }

    public void set_on_click_litioner() {

    }


    private void get_Total_present_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<PresentStatusItem>> call1 = apiInterface.get_present_list(workType);
        call1.enqueue(new Callback<List<PresentStatusItem>>() {
            @Override
            public void onResponse(Call<List<PresentStatusItem>> call, Response<List<PresentStatusItem>> response) {
                List<PresentStatusItem> applyLeaveResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (applyLeaveResponseItem != null && applyLeaveResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    present_list = new ArrayList<>();
                    for (int i = 0; i < applyLeaveResponseItem.size(); i++) {
                        res = applyLeaveResponseItem.get(i).getResponse();


                        if (res.equalsIgnoreCase("success")) {
                            PresentStatusItem model = new PresentStatusItem();
                            String Emp_Name = applyLeaveResponseItem.get(i).getEmpname();
                            String Response = applyLeaveResponseItem.get(i).getResponse();
                            String Employee_image = applyLeaveResponseItem.get(i).getImage();
                            String attendance_type = applyLeaveResponseItem.get(i).getTypeAttendance();
                            String login = applyLeaveResponseItem.get(i).getLoginTime();
                            String logout = applyLeaveResponseItem.get(i).getLogouttime();
                            model.setEmpname(Emp_Name);
                            model.setResponse(Response);
                            model.setImage(Employee_image);
                            model.setTypeAttendance(attendance_type);
                            model.setLoginTime(login);
                            model.setLogouttime(logout);

                            present_list.add(model);

                        }
                    }
                    employeeCurrentAdapter = new EmployeeCurrentAdapter(present_list, getActivity());
                    employee_status_rv.setAdapter(employeeCurrentAdapter);
                    employee_status_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            filter_present(s.toString());
                        }
                    });


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<PresentStatusItem>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void get_allabsent_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<AbsentStatusResponse>> call1 = apiInterface.get_absent_list(workType);
        call1.enqueue(new Callback<List<AbsentStatusResponse>>() {
            @Override
            public void onResponse(Call<List<AbsentStatusResponse>> call, Response<List<AbsentStatusResponse>> response) {
                List<AbsentStatusResponse> applyLeaveResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (applyLeaveResponseItem != null && applyLeaveResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    absent_list = new ArrayList<>();
                    for (int i = 0; i < applyLeaveResponseItem.size(); i++) {
                        res = applyLeaveResponseItem.get(i).getResponse();


                        if (res.equalsIgnoreCase("success")) {
                            AbsentStatusResponse model = new AbsentStatusResponse();
                            String Emp_Name = applyLeaveResponseItem.get(i).getEmpname();
                            String Response = applyLeaveResponseItem.get(i).getResponse();
                            String Employee_image = applyLeaveResponseItem.get(i).getImage();
                            String attendance_type = applyLeaveResponseItem.get(i).getTypeAttendance();
                            model.setEmpname(Emp_Name);
                            model.setResponse(Response);
                            model.setImage(Employee_image);
                            model.setTypeAttendance(attendance_type);

                            absent_list.add(model);

                        }
                    }
                    employeeAbsentAdapter = new EmployeeAbsentAdapter(absent_list, getActivity());
                    employee_status_rv.setAdapter(employeeAbsentAdapter);
                    employee_status_rv.setLayoutManager(new LinearLayoutManager(getActivity()));


                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            filter(s.toString());
                        }
                    });

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<AbsentStatusResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void get_allleave_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<LeaveStatusREsponse>> call1 = apiInterface.get_leave_list(workType);
        call1.enqueue(new Callback<List<LeaveStatusREsponse>>() {
            @Override
            public void onResponse(Call<List<LeaveStatusREsponse>> call, Response<List<LeaveStatusREsponse>> response) {
                List<LeaveStatusREsponse> applyLeaveResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (applyLeaveResponseItem != null && applyLeaveResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    leaves_list = new ArrayList<>();
                    for (int i = 0; i < applyLeaveResponseItem.size(); i++) {
                        res = applyLeaveResponseItem.get(i).getResponse();


                        if (res.equalsIgnoreCase("success")) {
                            LeaveStatusREsponse model = new LeaveStatusREsponse();
                            String Emp_Name = applyLeaveResponseItem.get(i).getEmpname();
                            String Response = applyLeaveResponseItem.get(i).getResponse();
                            String Employee_image = applyLeaveResponseItem.get(i).getImage();
                            String attendance_type = applyLeaveResponseItem.get(i).getTypeAttendance();
                            model.setEmpname(Emp_Name);
                            model.setResponse(Response);
                            model.setImage(Employee_image);
                            model.setTypeAttendance(attendance_type);

                            leaves_list.add(model);

                        }
                    }

                    employeeLeaveAdapter = new EmployeeLeaveAdapter(leaves_list, getActivity());
                    employee_status_rv.setAdapter(employeeLeaveAdapter);
                    employee_status_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            filter_leave(s.toString());
                        }
                    });


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<LeaveStatusREsponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void get_wfh_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<WFHStatusResponse>> call1 = apiInterface.get_wfh_list();
        call1.enqueue(new Callback<List<WFHStatusResponse>>() {
            @Override
            public void onResponse(Call<List<WFHStatusResponse>> call, Response<List<WFHStatusResponse>> response) {
                List<WFHStatusResponse> applyLeaveResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                wfh_list = new ArrayList<>();
                if (applyLeaveResponseItem != null && applyLeaveResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < applyLeaveResponseItem.size(); i++) {
                        res = applyLeaveResponseItem.get(i).getResponse();


                        if (res.equalsIgnoreCase("success")) {
                            WFHStatusResponse model = new WFHStatusResponse();
                            String Emp_Name = applyLeaveResponseItem.get(i).getEmpname();
                            String Response = applyLeaveResponseItem.get(i).getResponse();
                            String Employee_image = applyLeaveResponseItem.get(i).getImage();
                            String attendance_type = applyLeaveResponseItem.get(i).getTypeAttendance();
                            model.setEmpname(Emp_Name);
                            model.setResponse(Response);
                            model.setImage(Employee_image);
                            model.setTypeAttendance(attendance_type);

                            wfh_list.add(model);

                        }
                    }
                    employeeWFHAdapter = new EmployeeWFHAdapter(wfh_list, getActivity());
                    employee_status_rv.setAdapter(employeeWFHAdapter);
                    employee_status_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            filter_wfh(s.toString());
                        }
                    });

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<WFHStatusResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<AbsentStatusResponse> filteredlist = new ArrayList();
        // running a for loop to compare elements.
        for (int i = 0; i < absent_list.size(); i++) {
            // checking if the entered string matched with any item of our recycler view.
            if (absent_list.get(i).getEmpname().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(absent_list.get(i));
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            //  Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            employeeAbsentAdapter.updateList(filteredlist);
        }
    }

    private void filter_leave(String text) {
        // creating a new array list to filter our data.
        ArrayList<LeaveStatusREsponse> filteredlist = new ArrayList();
        // running a for loop to compare elements.
        for (int i = 0; i < leaves_list.size(); i++) {
            // checking if the entered string matched with any item of our recycler view.
            if (leaves_list.get(i).getEmpname().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(leaves_list.get(i));
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            //  Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            employeeLeaveAdapter.updateList(filteredlist);
        }
    }

    private void filter_present(String text) {
        // creating a new array list to filter our data.
        ArrayList<PresentStatusItem> filteredlist = new ArrayList();
        // running a for loop to compare elements.
        for (int i = 0; i < present_list.size(); i++) {
            // checking if the entered string matched with any item of our recycler view.
            if (present_list.get(i).getEmpname().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(present_list.get(i));
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            //  Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            employeeCurrentAdapter.updateList(filteredlist);
        }
    }

    private void filter_wfh(String text) {
        // creating a new array list to filter our data.
        ArrayList<WFHStatusResponse> filteredlist = new ArrayList();
        // running a for loop to compare elements.
        for (int i = 0; i < wfh_list.size(); i++) {
            // checking if the entered string matched with any item of our recycler view.
            if (wfh_list.get(i).getEmpname().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(wfh_list.get(i));
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            //  Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            employeeWFHAdapter.updateList(filteredlist);
        }
    }

    public void filterEmploye() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Office");
        arrayList.add("Field");
        arrayList.add("All");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        working_type.setAdapter(arrayAdapter);

        working_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                workType = working_type.getSelectedItem().toString();
                if (type_status.equalsIgnoreCase("present")) {
                    get_Total_present_list();
                } else if (type_status.equalsIgnoreCase("absent")) {
                    get_allabsent_list();
                } else if (type_status.equalsIgnoreCase("onleave")) {
                    get_allleave_list();
                } /*else if (type_status.equalsIgnoreCase("wfh")) {
                    get_wfh_list();
                }*/


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

}