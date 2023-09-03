package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.decorators.RangeDayDecorator;
import com.ossi.genreporting.model.ApplyLeaveResponseItem;
import com.ossi.genreporting.model.EmployeeListResponse;
import com.ossi.genreporting.model.EventResponse;
import com.ossi.genreporting.model.MyProjectListResponse;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectFragments extends Fragment implements View.OnClickListener, OnDateSelectedListener, OnRangeSelectedListener {
    TextView text_header1, employee_name, text_for_select;
    TextView login_time;
    View view;
    private String user_id;
    RoundedImageView img_profile;
    private String res;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    EditText  task_name, task_description;
    Spinner project_name_spin;
    Spinner task_assigned_name;
    String project_name_string, task_name_string, task_deadline_string, task_submit_date_string, task_description_string, task_assigned_name_string,task_assigned_name_id;
    Button task_assigned_btn;
    MaterialCalendarView range;
    private RangeDayDecorator decorator;
    private String Employee_Name,Employee_id;
    private ArrayList<String> employee_list;
    private ArrayList<String> project_list;
    private String Project_Name,Project_id;
    //private String task_assigned_project_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_project_fragments, container, false);


        find_view_by_id(view);
        set_on_click_litioner();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);
        decorator = new RangeDayDecorator(getActivity());

        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");

        if (img_profile1 != null) {
            Glide.with(this).load(img_profile1).into(img_profile);
        }

        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: " + login_Time);

        text_header1.setText("Assigned Task");
        text_for_select.setText("Select Duration");
        if(Util.isNetworkAvailable(getActivity())){
            //get_employees();
            get_ProjectList();
        }else {
            Toast.makeText(getActivity(), "Please Check Internet Connetion", Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    public void find_view_by_id(View view) {

        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        text_for_select = view.findViewById(R.id.text_for_select);
        img_profile = view.findViewById(R.id.img_profile);

        project_name_spin = view.findViewById(R.id.project_name_spin);
        task_name = view.findViewById(R.id.task_name);
        //task_deadline = view.findViewById(R.id.task_deadline);
        //  task_submit_date = view.findViewById(R.id.task_submit_date);
        task_description = view.findViewById(R.id.task_description);
        task_assigned_name = view.findViewById(R.id.task_assigned_name);


        task_assigned_btn = view.findViewById(R.id.task_assigned_btn);

        range = view.findViewById(R.id.calendar_view_range);
        range.setOnDateChangedListener(this);
        range.setOnRangeSelectedListener(this);
        range.addDecorator(decorator);
    }

    public void set_on_click_litioner() {
        task_assigned_btn.setOnClickListener(this);
        // task_submit_date.setOnClickListener(this);
        //  task_deadline.setOnClickListener(this);
    }


    private void Assigned_Task_By_Me() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ApplyLeaveResponseItem>> call1 = apiInterface.Assigned_task_by_me(task_assigned_name_id, Project_id,task_name_string,task_submit_date_string, task_description_string, user_id);
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

                        if (res.equalsIgnoreCase("success ")) {
                            Toast.makeText(getActivity(), " Task Assigned Success", Toast.LENGTH_SHORT).show();
                            TabLayoutFragment tabLayoutFragment = new TabLayoutFragment();
                            openFragment(tabLayoutFragment);
                        } else {
                            Toast.makeText(getActivity(), "" + res, Toast.LENGTH_SHORT).show();
                            TabLayoutFragment tabLayoutFragment = new TabLayoutFragment();
                            openFragment(tabLayoutFragment);
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
        // transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.task_assigned_btn:
               project_name_string = project_name_spin.getSelectedItem().toString();
                task_name_string = task_name.getText().toString();
                //task_deadline_string = task_deadline.getText().toString();
                //task_submit_date_string = task_submit_date.getText().toString();
                task_description_string = task_description.getText().toString();
                task_assigned_name_string = task_assigned_name.getSelectedItem().toString();

                if (task_deadline_string == null) {
                    Toast.makeText(getActivity(), "Please Select Deadline", Toast.LENGTH_SHORT).show();
                } else if (task_submit_date_string == null) {
                    Toast.makeText(getActivity(), "Please Select Submit Date", Toast.LENGTH_SHORT).show();
                } else if (project_name_string.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Projct Name", Toast.LENGTH_SHORT).show();
                } else if (task_name_string.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Task Name", Toast.LENGTH_SHORT).show();
                } else if (task_description_string.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Descroption", Toast.LENGTH_SHORT).show();
                } else if (task_assigned_name_string.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Select Name For Task Assigned", Toast.LENGTH_SHORT).show();
                } else {

                    if (Util.isNetworkAvailable(getActivity())) {
                        Assigned_Task_By_Me();
                    } else {
                        Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }


    @Override
    public void onDateSelected(
            @NonNull final MaterialCalendarView widget,
            @NonNull final CalendarDay date,
            final boolean selected) {
        // final String text = selected ? FORMATTER.format(date.getDate()) : "No Selection";
        //Toast.makeText(SelectionModesActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRangeSelected(
            @NonNull final MaterialCalendarView widget,
            @NonNull final List<CalendarDay> dates) {


        if (dates.size() > 0) {
            CalendarDay calendarDay = dates.get(0);
            CalendarDay calendarDay2 = dates.get(dates.size() - 1);
            task_deadline_string = calendarDay.getYear() + "/" + calendarDay.getMonth() + "/" + calendarDay.getDay();
            task_submit_date_string = calendarDay2.getYear() + "/" + calendarDay2.getMonth() + "/" + calendarDay2.getDay();
           // Toast.makeText(getActivity(), "start date:: " + task_submit_date_string + " end date:: " + task_submit_date_string, Toast.LENGTH_SHORT).show();
            Log.i("Datest", "start date:: " + dates.get(0));
            Log.i("Datest", "end date:: " + dates.get(dates.size() - 1));
            decorator.addFirstAndLast(dates.get(0), dates.get(dates.size() - 1));
            range.invalidateDecorators();
        }
    }

    private void get_employees() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<EmployeeListResponse>> call1 = apiInterface.get_employee_list();
        call1.enqueue(new Callback<List<EmployeeListResponse>>() {
            @Override
            public void onResponse(Call<List<EmployeeListResponse>> call, Response<List<EmployeeListResponse>> response) {
                List<EmployeeListResponse> employeeListResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (employeeListResponse != null && employeeListResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    employee_list = new ArrayList<>();
                    for (int i = 0; i < employeeListResponse.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();

                        Employee_Name = employeeListResponse.get(i).getEmpName();
                        Employee_id = employeeListResponse.get(i).getSno();
                        employee_list.add(Employee_Name);
                       // employee_list.add(Employee_id);

                    }
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getActivity(),  android.R.layout.simple_spinner_dropdown_item, employee_list);
                    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                    task_assigned_name.setAdapter(adapter);
                    task_assigned_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            task_assigned_name_id = employeeListResponse.get(position).getSno();
                            //get_ProjectList();
                          //  Toast.makeText(getActivity(), "id "+task_assigned_name_id, Toast.LENGTH_SHORT).show();
                            /*if(selectedItem.equals("Add new category"))
                            {
                                // do your stuff
                            }*/
                        } // to close the onItemSelected
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<EmployeeListResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    private void get_ProjectList() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<MyProjectListResponse>> call1 = apiInterface.get_assign_project_lists(user_id);
        call1.enqueue(new Callback<List<MyProjectListResponse>>() {
            @Override
            public void onResponse(Call<List<MyProjectListResponse>> call, Response<List<MyProjectListResponse>> response) {
                List<MyProjectListResponse> myProjectListResponse = response.body();

                if (myProjectListResponse != null && myProjectListResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    project_list = new ArrayList<>();
                    for (int i = 0; i < myProjectListResponse.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        String res=myProjectListResponse.get(i).getResponse();
                        if(res.equalsIgnoreCase("Success ")) {

                            Project_Name = myProjectListResponse.get(i).getProject_Name();
                            Project_id = myProjectListResponse.get(i).getProject_id();
                            project_list.add(Project_Name);
                            //project_list.add(Project_id);
                            get_employees();
                        }


                    }
                    ArrayAdapter<String> adapter1 =
                            new ArrayAdapter<>(getActivity(),  android.R.layout.simple_spinner_dropdown_item, project_list);
                    adapter1.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);

                    project_name_spin.setAdapter(adapter1);
                    project_name_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                    {
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                        {
                            Project_id = myProjectListResponse.get(position).getProject_id();

                        } // to close the onItemSelected
                        public void onNothingSelected(AdapterView<?> parent)
                        {

                        }
                    });

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
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



}