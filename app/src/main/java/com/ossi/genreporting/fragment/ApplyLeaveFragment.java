package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ApplyLeaveFragment extends Fragment implements View.OnClickListener , OnDateSelectedListener, OnRangeSelectedListener {

    Spinner leave_type;
    EditText purpose_leave;
    View view;
    String month_first,month_last,month_day,day_last,day_first;
    private String Spin_Name;
    TextView remain_leave;
    int day;
    int month;
    int year;
    String selectedDate;
    private DatePickerDialog picker;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String res;
    private String user_id,CL,SL,EL,remaining_leave;
    Button aply_leave;
    private String date_from,date_to,leave_purpose,type_leave;
    TextView text_header1,employee_name;
    TextView login_time;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    MaterialCalendarView range;
    private RangeDayDecorator decorator;
    RoundedImageView img_profile;
    Spinner halfDaySelect,shortLeaveSelect;
    String shortOrHalfType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_apply_leave, container, false);

        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);
        find_view_by_id(view);
        set_on_click_litioner();
        user_id = pref.getString("user_id", null);
        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");

        if(img_profile1!=null){
            Glide.with(this).load(img_profile1).into(img_profile);
        }

        EL = pref.getString("EL", null);
        CL = pref.getString("CL", null);
        SL = pref.getString("SL", null);
         remaining_leave="Remaining Leaves: CL: "+CL+" EL: "+EL+" SL: "+SL;
        decorator = new RangeDayDecorator(getActivity());


        employee_name.setText(Employee_Name);
            login_time.setText("Login Time: "+login_Time);




        remain_leave.setText(remaining_leave);
        text_header1.setText("Apply Leave");
        return view;
    }


    public void find_view_by_id(View view){
        leave_type=(Spinner) view.findViewById(R.id.leave_type);
       // date_from_leave_aply=(TextView)view.findViewById(R.id.date_from_leave_aply);
        img_profile=view.findViewById(R.id.img_profile);
        aply_leave=(Button)view.findViewById(R.id.aply_leave);
        employee_name=view.findViewById(R.id.employee_name);
        login_time=view.findViewById(R.id.login_time);
        remain_leave=view.findViewById(R.id.remain_leave);
        purpose_leave=view.findViewById(R.id.purpose_leave);
        text_header1=view.findViewById(R.id.text_header1);
        range=view.findViewById(R.id.calendar_view_range);
        shortLeaveSelect=view.findViewById(R.id.shortLeaveSelect);
        halfDaySelect=view.findViewById(R.id.halfDaySelect);

        range.setOnDateChangedListener(this);
        range.setOnRangeSelectedListener(this);
        range.addDecorator(decorator);





        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Leave Type");
        arrayList.add("Casual Leave");
        arrayList.add("Sick Leave");
        arrayList.add("Earned Leave");
        arrayList.add("Short Leave");
        arrayList.add("Halfday Leave");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leave_type.setAdapter(arrayAdapter);
        leave_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Spin_Name = parent.getItemAtPosition(position).toString();
                if(Spin_Name.equalsIgnoreCase("Short Leave")){
                    shortLeaveSelect.setVisibility(View.VISIBLE);
                    halfDaySelect.setVisibility(View.GONE);


                }else if(Spin_Name.equalsIgnoreCase("Halfday Leave")){
                    halfDaySelect.setVisibility(View.VISIBLE);
                    shortLeaveSelect.setVisibility(View.GONE);
                }



            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        ArrayList<String> shortLeave = new ArrayList<>();
        shortLeave.add("Select Time");
        shortLeave.add("10.00AM-12.00PM");
        shortLeave.add("12.00PM-02.00PM");
        shortLeave.add("02.00PM-04.00PM");
        shortLeave.add("04.00PM-06.00PM");
        ArrayAdapter<String> shortLeaveAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, shortLeave);
        shortLeaveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shortLeaveSelect.setAdapter(shortLeaveAdapter);


        ArrayList<String> halfDayLeave = new ArrayList<>();
        halfDayLeave.add("Select Halfday Leave");
        halfDayLeave.add("1st Halfday");
        halfDayLeave.add("2nd Halfday");
        ArrayAdapter<String> halfDayLeaveAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, halfDayLeave);
        halfDayLeaveAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        halfDaySelect.setAdapter(halfDayLeaveAdapter);
    }

    public void set_on_click_litioner(){

        aply_leave.setOnClickListener(this);
    }

    public void onDateSet() {
        final Calendar cldr = Calendar.getInstance();
         day = cldr.get(Calendar.DAY_OF_MONTH);
         month = cldr.get(Calendar.MONTH);
         year = cldr.get(Calendar.YEAR);
        // date picker dialog

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.aply_leave:

                Log.i("date", "selectedDate::" + range.getSelectedDates().toString());
                leave_purpose = purpose_leave.getText().toString();
                type_leave = leave_type.getSelectedItem().toString();
                String short_leave=shortLeaveSelect.getSelectedItem().toString();
                String half_leave=halfDaySelect.getSelectedItem().toString();


                if (range.getSelectedDates().size()==0) {
                    Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
                } else if (leave_purpose.equalsIgnoreCase("")) {
                Toast.makeText(getActivity(), "Please Enter Purpose", Toast.LENGTH_SHORT).show();
            } else if (type_leave.equalsIgnoreCase("Leave Type")) {
                Toast.makeText(getActivity(), "Please Select Leave Type", Toast.LENGTH_SHORT).show();
            } else if (shortLeaveSelect.getVisibility()==View.VISIBLE && short_leave.equalsIgnoreCase("Select Time")) {
                    Toast.makeText(getActivity(), "Please Select Time Slot", Toast.LENGTH_SHORT).show();
                } else if (halfDaySelect.getVisibility()==View.VISIBLE && half_leave.equalsIgnoreCase("Select Halfday Leave")) {
                    Toast.makeText(getActivity(), "Please Select Half Day Type", Toast.LENGTH_SHORT).show();
                }  else {

                    if (Util.isNetworkAvailable(getActivity())) {
                        if (shortLeaveSelect.getVisibility()==View.VISIBLE){
                            shortOrHalfType=shortLeaveSelect.getSelectedItem().toString() ;
                        }else if(halfDaySelect.getVisibility()==View.VISIBLE){
                            shortOrHalfType=halfDaySelect.getSelectedItem().toString();
                        }else {
                            shortOrHalfType="";
                        }


                        if (range.getSelectedDates().size() > 1) {
                            CalendarDay cal1=range.getSelectedDates().get(0);
                            if(cal1.getDay()<10){
                                day_first ="0"+cal1.getDay();
                            }
                             if(cal1.getMonth()<10){
                              month_first ="0"+cal1.getMonth();
                             }



                            date_from = cal1.getYear() + "-" + month_first + "-" + day_first;

                            CalendarDay cal2=range.getSelectedDates().get(range.getSelectedDates().size()-1);
                            if(cal2.getDay()<10){
                                day_last="0"+cal2.getDay();
                            }
                            if(cal2.getMonth()<10){
                                month_last="0"+cal2.getMonth();
                            }

                            date_to=cal2.getYear() + "-" + month_last + "-" + day_last;



                            Log.i("date::", "date_from::" +date_from);
                            Log.i("date::", "date_to::" +date_to);
                            Apply_leave_method(user_id, date_from, date_to,type_leave, leave_purpose,shortOrHalfType);
                        } else if (range.getSelectedDates().size() > 0 && range.getSelectedDates().size() == 1) {
                            CalendarDay cal1=range.getSelectedDates().get(0);
                            if(cal1.getDay()<10){
                                month_day ="0"+cal1.getDay();
                            }
                            if(cal1.getMonth()<10){
                                month_first ="0"+cal1.getMonth();
                            }

                            date_from = cal1.getYear() + "-" + month_first + "-" +month_day ;

                            Log.i("date::", "date_from::" +date_from);
                            Apply_leave_method(user_id, date_from, date_from,type_leave, leave_purpose,shortOrHalfType);
                        }
                    }else {
                        Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    private void Apply_leave_method(String userid, String fdate, String tdate, String type_leave,String purpose,String type) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ApplyLeaveResponseItem>> call1 = apiInterface.ApplyLeave_submit(userid, fdate, tdate, type_leave,purpose,type);
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

                        if (res.equalsIgnoreCase("successfully")) {
                            Toast.makeText(getActivity(), " Apply Leave Success", Toast.LENGTH_SHORT).show();
                            TabLayoutFragment tabLayoutFragment = new TabLayoutFragment();
                            openFragment(tabLayoutFragment);
                        }
                        else {
                            Toast.makeText(getActivity(), ""+res, Toast.LENGTH_SHORT).show();
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
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
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
                if (calendarDay.getMonth() < 10) {
                    month_first = "0" + calendarDay.getMonth();
                    day_first="0"+calendarDay.getDay();
                }
                if(calendarDay2.getMonth()<10){
                    month_last="0"+calendarDay2.getMonth();
                    day_last="0"+calendarDay2.getDay();
                }
            date_from=calendarDay.getYear()+"-"+month_first+"-"+day_first;
            date_to=calendarDay2.getYear()+"-"+month_last+"-"+day_last;
           // Toast.makeText(getActivity(), "start date:: "+date_from+" end date:: "+date_to, Toast.LENGTH_SHORT).show();
            Log.i("Datest","start date:: "+dates.get(0));
            Log.i("Datest","end date:: "+dates.get(dates.size() - 1));

            decorator.addFirstAndLast(dates.get(0), dates.get(dates.size() - 1));


            range.invalidateDecorators();
        }
    }
}