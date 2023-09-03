package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.LoginActivity;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.activity.HomeActivity;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.decorators.RangeDayDecorator;
import com.ossi.genreporting.model.ViewAtendanceResponseItemItem;
import com.ossi.genreporting.model.LoginResponse;
import com.ossi.genreporting.model.ViewAtendanceResponseItemItem;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class AttendanceFragment extends Fragment implements View.OnClickListener , OnDateSelectedListener, OnRangeSelectedListener {
View view;
//TextView attendance_date_open,attendance_date_to;
//TextView attendance_to_month_date_open;
    private DatePickerDialog picker;
    private int year,month,day;
   // LinearLayout check_attend,show_attend_detail;
    Button view_attend;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    String user_id;
    TextView present_day,absent_day,adjust_day,adjust_by_name;
    Spinner attendance_type;
    private String attend_type;
    TextView text_header1,employee_name;
    TextView login_time;
    String date_from,date_to;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    MaterialCalendarView range;
    private RangeDayDecorator decorator;
RoundedImageView img_profile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_attendance, container, false);

        find_view_by_id(view);
        set_on_click_litioner();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);
        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");

        if(img_profile1!=null){
            Glide.with(this).load(img_profile1).into(img_profile);
        }

        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: "+login_Time);

        text_header1.setText("Attendance");

        decorator = new RangeDayDecorator(getActivity());
        return view;
    }

    public void find_view_by_id(View v){
        img_profile=v.findViewById(R.id.img_profile);
        //show_attend_detail=v.findViewById(R.id.show_attend_detail);
        //check_attend=v.findViewById(R.id.check_attend);
        view_attend=v.findViewById(R.id.view_attend);
        present_day=v.findViewById(R.id.present_day);
        absent_day=v.findViewById(R.id.absent_day);
        adjust_day=v.findViewById(R.id.adjust_day);
        adjust_by_name=v.findViewById(R.id.adjust_by_name);
       // attendance_date_to=v.findViewById(R.id.attendance_date_to);
        attendance_type=v.findViewById(R.id.attendance_type);
        employee_name=view.findViewById(R.id.employee_name);
        login_time=view.findViewById(R.id.login_time);
        text_header1=view.findViewById(R.id.text_header1);

        range=view.findViewById(R.id.calendar_view_range);
        range.setOnDateChangedListener(this);
        range.setOnRangeSelectedListener(this);
        range.addDecorator(decorator);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Attendance Type");
        arrayList.add("Monthly");
        arrayList.add("Work From Home");
        arrayList.add("Work From Office");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        attendance_type.setAdapter(arrayAdapter);
        attendance_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                attend_type = parent.getItemAtPosition(position).toString();
                // Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });
    }

    public void set_on_click_litioner(){
        //attendance_date_open.setOnClickListener(this);
        view_attend.setOnClickListener(this);
        //attendance_date_to.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.view_attend:
                if(date_from==null){
                    Toast.makeText(getActivity(), "Please select Date from and to", Toast.LENGTH_SHORT).show();

                }else if(date_to==null){
                    Toast.makeText(getActivity(), "Please select Date from and to", Toast.LENGTH_SHORT).show();

                }else if(attend_type.equalsIgnoreCase("Attendance Type")){
                    Toast.makeText(getActivity(), "Please Select Attendance Type", Toast.LENGTH_SHORT).show();

                }else {
                    if (Util.isNetworkAvailable(getActivity())) {
                        Attendance_view_method(user_id, date_from, date_to, attend_type);
                    }else {
                        Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                    }
                }

                break;
        }
    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }



    public void onDateSet() {
        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        // date picker dialog

    }
    private void Attendance_view_method(String user_Id,String from,String to,String attendance_typ) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ViewAtendanceResponseItemItem>> call1 = apiInterface.Apply_view_attendance(user_Id,from,to,attendance_typ);
        call1.enqueue(new Callback<List<ViewAtendanceResponseItemItem>>() {
            @Override
            public void onResponse(Call<List<ViewAtendanceResponseItemItem>> call, Response<List<ViewAtendanceResponseItemItem>> response) {
                List<ViewAtendanceResponseItemItem> ViewAtendanceResponseItemItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (ViewAtendanceResponseItemItem != null && ViewAtendanceResponseItemItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < ViewAtendanceResponseItemItem.size(); i++) {
                      String  res = ViewAtendanceResponseItemItem.get(i).getResponse();
                      if(res.equalsIgnoreCase("success")) {
                         // check_attend.setVisibility(View.GONE);
                         // show_attend_detail.setVisibility(View.VISIBLE);
                          String Absent_day = ViewAtendanceResponseItemItem.get(i).getTotalAbsent();
                          String Present_day = ViewAtendanceResponseItemItem.get(i).getTotalPresent();
                          String Adjust_day = ViewAtendanceResponseItemItem.get(i).getAdjustDay();
                          String Adjust_by_name = ViewAtendanceResponseItemItem.get(i).getAdjustBySno();
                          /*absent_day.setText(Absent_day);
                          present_day.setText(Present_day);
                          adjust_day.setText(Adjust_day);
                          adjust_by_name.setText(Adjust_by_name);*/

                          AttendanceshowFragment attendanceshowFragment = new AttendanceshowFragment();
                          Bundle bundle=new Bundle();
                          bundle.putString("Absent_day",Absent_day);
                          bundle.putString("Present_day",Present_day);
                          bundle.putString("Adjust_day",Adjust_day);
                          bundle.putString("Adjust_by_name",Adjust_by_name);

                          attendanceshowFragment.setArguments(bundle);
                          openFragment(attendanceshowFragment);
                      }else if(res.equalsIgnoreCase("Fail")){
                          Toast.makeText(getActivity(), "Attendance not Uploade yet", Toast.LENGTH_SHORT).show();
                      }

                    }


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<ViewAtendanceResponseItemItem>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

    @Override public void onDateSelected(
            @NonNull final MaterialCalendarView widget,
            @NonNull final CalendarDay date,
            final boolean selected) {
        // final String text = selected ? FORMATTER.format(date.getDate()) : "No Selection";
        //Toast.makeText(SelectionModesActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override public void onRangeSelected(
            @NonNull final MaterialCalendarView widget,
            @NonNull final List<CalendarDay> dates) {


        if (dates.size() > 0) {
            CalendarDay calendarDay=  dates.get(0);
            CalendarDay calendarDay2=  dates.get(dates.size() - 1);
            date_from=calendarDay.getYear()+"/"+calendarDay.getMonth()+"/"+calendarDay.getDay();
            date_to=calendarDay2.getYear()+"/"+calendarDay2.getMonth()+"/"+calendarDay2.getDay();
            //Toast.makeText(getActivity(), "start date:: "+date_from+" end date:: "+date_to, Toast.LENGTH_SHORT).show();
            Log.i("Datest","start date:: "+dates.get(0));
            Log.i("Datest","end date:: "+dates.get(dates.size() - 1));
            decorator.addFirstAndLast(dates.get(0), dates.get(dates.size() - 1));
            range.invalidateDecorators();
        }
    }
}