package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.Adapter.DepartmentEmployeeAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.ApplyLeaveResponseItem;
import com.ossi.genreporting.model.DepartmentEmployeeListResponse;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubmitMeetingFragment extends Fragment implements View.OnClickListener, DepartmentEmployeeAdapter.Departmentinterface {
    Spinner department_spin, meeting_mod_spin;
    Button create_meet_btn;
    View view;

    TextView text_header1, employee_name, text_for_select;
    String heading, detail, date, time, name, meeting_mode, assign_by;
    TextView login_time;
    EditText meet_detail, meet_heading;
    TextView meet_time, meet_date;
    private String user_id;
    RoundedImageView img_profile;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    String department;
    Spinner spinner_name;
    DepartmentEmployeeAdapter departmentEmployeeAdapter;
    private ArrayList<DepartmentEmployeeListResponse> emp_list;

    private ArrayList<DepartmentEmployeeListResponse> department_list;
    TimePickerDialog picker;
    EditText meet_url_description;
    String name_employe, employee_id;
    private String day_, month_;
    RelativeLayout nameSpinLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_submit_meeting, container, false);
        find_view_by_id();
        set_on_click_litioner();

        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);
        user_id = pref.getString("user_id", null);

        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");
        Bundle bundle = getArguments();
        if (bundle != null) {
            heading = bundle.getString("heading");
            String res = bundle.getString("res");
            date = bundle.getString("date");
            time = bundle.getString("time");
            name = bundle.getString("emp");
            department = bundle.getString("department");
            detail = bundle.getString("details");
            meeting_mode = bundle.getString("mode");
            assign_by = bundle.getString("assign_by");
            if (heading.equalsIgnoreCase("")) {
                create_meet_btn.setVisibility(View.VISIBLE);


            } else {
                create_meet_btn.setVisibility(View.GONE);
                nameSpinLayout.setVisibility(View.GONE);
                meet_detail.setText(name);
                meet_heading.setText("Heading: " + heading);
                meet_time.setText("Time: " + time);
                meet_date.setText("Date: " + date);
                meet_url_description.setText("Meeting Room:"+detail);
                //department_spin.setPrompt(department);
                //  meeting_mod_spin.setSelection(meeting_mode);
                String[] items = new String[]{department};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                department_spin.setAdapter(adapter);
                /*ArrayList<String> arrayList1 = new ArrayList<>();
                arrayList1.add(department);
                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList1);
                arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                department_spin.setAdapter(arrayAdapter1);*/


                ArrayList<String> arrayList2 = new ArrayList<>();
                arrayList2.add(meeting_mode);
                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList2);
                arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                meeting_mod_spin.setAdapter(arrayAdapter2);


            }

        }


        if (img_profile1 != null) {
            Glide.with(this).load(img_profile1).into(img_profile);
        }

        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: " + login_Time);

        text_header1.setText("Create Meetings");
        text_for_select.setText("");


        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Meeting Mode");
        arrayList1.add("Offline");
        arrayList1.add("Online");
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        meeting_mod_spin.setAdapter(arrayAdapter1);


        // Get_department();

        ArrayList<String> arrayList2 = new ArrayList<>();
        arrayList2.add("Select Department");
        arrayList2.add("All");
        arrayList2.add("SAP ABAP CONSULTANT");
        arrayList2.add("ANDROID DEVELOPER");
        arrayList2.add("SENIOR EXECUTIVE ACCOUNTS");
        arrayList2.add("SOFTWARE ENGINEER");
        arrayList2.add("PRODUCT HEAD");
        arrayList2.add("PROJECT HEAD");
        arrayList2.add("SR SOFTWARE ENGINEER");
        arrayList2.add("PROJECT ADMINISTRATOR");
        arrayList2.add("WEB DESIGNER");
        arrayList2.add("Chief Executive Officer (CEO)");
        arrayList2.add("EMBEDDED ENGINEER");
        arrayList2.add("HR GENERALIST");
        arrayList2.add("FINANCE HEAD");
        arrayList2.add("WEB DESIGNER");


        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_spin.setAdapter(arrayAdapter2);
        department_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = department_spin.getSelectedItem().toString();
                if (department_spin.getSelectedItem().equals("Select Department")) {
                    Toast.makeText(getActivity(), "Please Select Department", Toast.LENGTH_SHORT).show();
                } else {
                    Get_employee_departmentwise();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }

    public void find_view_by_id() {
        meeting_mod_spin = view.findViewById(R.id.meeting_mod_spin);
        department_spin = view.findViewById(R.id.department_spin);
        create_meet_btn = view.findViewById(R.id.create_meet_btn);

        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);
        text_for_select = view.findViewById(R.id.text_for_select);

        spinner_name = view.findViewById(R.id.spinner_name);
        nameSpinLayout=view.findViewById(R.id.nameSpinLayout);


        meet_detail = view.findViewById(R.id.meet_detail);
        meet_heading = view.findViewById(R.id.meet_heading);
        meet_date = view.findViewById(R.id.meet_date);
        meet_time = view.findViewById(R.id.meet_time);
        meet_url_description = view.findViewById(R.id.meet_url_description);
    }

    public void set_on_click_litioner() {
        create_meet_btn.setOnClickListener(this);
        meet_time.setOnClickListener(this);
        meet_date.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create_meet_btn:
                heading = meet_heading.getText().toString();
                date = meet_date.getText().toString();
                time = meet_time.getText().toString();
                detail = meet_detail.getText().toString();
                //name=spinner_name.getSelectedItem().toString();
                meeting_mode = meeting_mod_spin.getSelectedItem().toString();
                if (heading.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Meeting Heading", Toast.LENGTH_SHORT).show();
                } else if (department_spin.getSelectedItem().equals("Select Department")) {
                    Toast.makeText(getActivity(), "Please Select Department First", Toast.LENGTH_SHORT).show();
                } else if (spinner_name.getSelectedItem().equals("")) {
                    Toast.makeText(getActivity(), "Please Select Name First", Toast.LENGTH_SHORT).show();
                } else if (detail.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Meeting Details", Toast.LENGTH_SHORT).show();
                } else if (date.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Select Date First", Toast.LENGTH_SHORT).show();
                } else if (time.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Select Time First", Toast.LENGTH_SHORT).show();
                } else if (meeting_mod_spin.getSelectedItem().equals("Meeting Mode")) {
                    Toast.makeText(getActivity(), "Please Select Meeting Mode First", Toast.LENGTH_SHORT).show();
                } else if (meet_url_description.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Description or url", Toast.LENGTH_SHORT).show();

                } else {
                    if (Util.isNetworkAvailable(getActivity())) {
                        add_meeting();
                    } else {
                        Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
            case R.id.meet_time:


                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);


                // time picker dialog
                picker = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                String am_pm = "";
                                  /*  tp.setIs24HourView(false);
                                tp.setCurrentHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));*/
                                Calendar datetime = Calendar.getInstance();
                                datetime.set(Calendar.HOUR_OF_DAY, sHour);
                                datetime.set(Calendar.MINUTE, sMinute);

                                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                                    am_pm = "AM";
                                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                                    am_pm = "PM";

                                meet_time.setText(sHour + ":" + sMinute + " " + am_pm);

                            }
                        }, hour, minutes, false);
                picker.show();


                break;

            case R.id.meet_date:
                final Calendar c = Calendar.getInstance();

                // on below line we are getting
                // our day, month and year.
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                // on below line we are creating a variable for date picker dialog.
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        // on below line we are passing context.
                        getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                day_ = "" + dayOfMonth;
                                month_ = "" + monthOfYear;
                                if (dayOfMonth < 10) {
                                    day_ = "0" + dayOfMonth;
                                }
                                if (monthOfYear < 10) {
                                    int i = monthOfYear + 1;
                                    month_ = "0" + i;
                                }
                                meet_date.setText(day_ + "-" + (month_) + "-" + year);


                            }
                        },
                        // on below line we are passing year,
                        // month and day for selected date in our date picker.
                        year, month, day);
                // at last we are calling show to
                // display our date picker dialog.
                datePickerDialog.show();
                break;
        }
    }


    private void Get_employee_departmentwise() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<DepartmentEmployeeListResponse>> call1 = apiInterface.employee_list_department_wise(department);
        call1.enqueue(new Callback<List<DepartmentEmployeeListResponse>>() {
            @Override
            public void onResponse(Call<List<DepartmentEmployeeListResponse>> call, Response<List<DepartmentEmployeeListResponse>> response) {
                List<DepartmentEmployeeListResponse> DepartmentEmployeeListResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (DepartmentEmployeeListResponse != null && DepartmentEmployeeListResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    emp_list = new ArrayList<>();

                    for (int i = 0; i < DepartmentEmployeeListResponse.size(); i++) {
                        String res = DepartmentEmployeeListResponse.get(i).getEmpName();
                        Log.e("hr","hr:"+res);
                        DepartmentEmployeeListResponse model = new DepartmentEmployeeListResponse();
                        if (!res.equalsIgnoreCase("")) {
                            String Name = DepartmentEmployeeListResponse.get(i).getEmpName();
                            String id = DepartmentEmployeeListResponse.get(i).getSno();

                            model.setEmpName(Name);
                            model.setSno(id);
                            model.setSelected(false);
                            emp_list.add(model);


                        } else if (res.equalsIgnoreCase("Fail")) {
                            Toast.makeText(getActivity(), "No Employee", Toast.LENGTH_SHORT).show();
                        }


                    }
                    departmentEmployeeAdapter = new DepartmentEmployeeAdapter(getActivity(), 0, emp_list, SubmitMeetingFragment.this);
                    spinner_name.setAdapter(departmentEmployeeAdapter);


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<DepartmentEmployeeListResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void Get_department() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<DepartmentEmployeeListResponse>> call1 = apiInterface.department_list();
        call1.enqueue(new Callback<List<DepartmentEmployeeListResponse>>() {
            @Override
            public void onResponse(Call<List<DepartmentEmployeeListResponse>> call, Response<List<DepartmentEmployeeListResponse>> response) {
                List<DepartmentEmployeeListResponse> DepartmentEmployeeListResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (DepartmentEmployeeListResponse != null && DepartmentEmployeeListResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    department_list = new ArrayList<>();

                    for (int i = 0; i < DepartmentEmployeeListResponse.size(); i++) {
                        String res = DepartmentEmployeeListResponse.get(i).getJobProfile();
                        DepartmentEmployeeListResponse model = new DepartmentEmployeeListResponse();
                        if (!res.equalsIgnoreCase("")) {
                            String department = DepartmentEmployeeListResponse.get(i).getJobProfile();
                            // String id = DepartmentEmployeeListResponse.get(i).getId();

                            model.setJobProfile(department);
                            //model.setId(id);
                            model.setSelected(false);
                            department_list.add(model);


                        } else if (res.equalsIgnoreCase("Fail")) {
                            Toast.makeText(getActivity(), "No Employee", Toast.LENGTH_SHORT).show();
                        }


                    }
                    ArrayAdapter<DepartmentEmployeeListResponse> arrayAdapter2 = new ArrayAdapter<DepartmentEmployeeListResponse>(getActivity(), android.R.layout.simple_spinner_item, department_list);
                    arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    department_spin.setAdapter(arrayAdapter2);
                    department_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            department = department_spin.getSelectedItem().toString();
                            if (department_spin.getSelectedItem().equals("Select Department")) {
                                Toast.makeText(getActivity(), "Please Select Department", Toast.LENGTH_SHORT).show();
                            } else {
                                Get_employee_departmentwise();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<DepartmentEmployeeListResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    private void add_meeting() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
        if (meeting_mode.equalsIgnoreCase("Offline")) {
            meeting_mode = "2";
        } else if (meeting_mode.equalsIgnoreCase("Online")) {
            meeting_mode = "1";
        }

        Call<List<ApplyLeaveResponseItem>> call1 = apiInterface.add_meeting(user_id, heading, date, time, department, meeting_mode, employee_id, detail, meet_url_description.getText().toString());
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
                        String res = applyLeaveResponseItem.get(i).getResponse();

                        if (res.equalsIgnoreCase("Success")) {
                            Toast.makeText(getActivity(), "Meeting added successfully", Toast.LENGTH_SHORT).show();
                            AddShowMeeting addShowMeeting = new AddShowMeeting();
                            openFragment(addShowMeeting);
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


    @Override
    public void clickCallback(String key, String Value) {
        name_employe = key;
        employee_id = Value;
        //Toast.makeText(getActivity(),"Okay button called "+employee_id,Toast.LENGTH_SHORT).show();
        hideSpinnerDropDown(spinner_name);

    }
    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}