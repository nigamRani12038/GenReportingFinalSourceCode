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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.decorators.RangeDayDecorator;
import com.ossi.genreporting.model.ApplyWFHResponseItem;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;

import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ApplyWorkFromHomeFragment extends Fragment implements View.OnClickListener, OnDateSelectedListener, OnRangeSelectedListener {
    View view;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    String user_id, Remaining_WFH_leave;
    TextView remain_wfh, wfh_purpose;
    Button aply_wfh_submit;
    private String res;
    private String wfh_date_from, wfh_date_to, wfh_leave_purpose;
    int day;
    int month;
    int year;
    private DatePickerDialog picker;
    TextView text_header1, employee_name;
    TextView login_time;
    RoundedImageView img_profile;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE, d MMM yyyy");
    MaterialCalendarView range;
    private RangeDayDecorator decorator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_work_from_home, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);
        user_id = pref.getString("user_id", null);
        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");


        Remaining_WFH_leave = pref.getString("Remaining_WFH_leave", null);

        find_view_by_id();
        set_on_click_litioner();


        if (img_profile1 != null) {
            Glide.with(this).load(img_profile1).into(img_profile);
        }

        decorator = new RangeDayDecorator(getActivity());
        text_header1.setText("Apply WFH");
        remain_wfh.setText("Remaining WFH Leaves :" + Remaining_WFH_leave);
        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: " + login_Time);

        return view;
    }

    public void find_view_by_id() {
        remain_wfh = view.findViewById(R.id.remain_wfh);
        //  date_from_wfh=view.findViewById(R.id.date_from_wfh);
        img_profile = view.findViewById(R.id.img_profile);
        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        wfh_purpose = view.findViewById(R.id.wfh_purpose);
        aply_wfh_submit = view.findViewById(R.id.aply_wfh_submit);
        text_header1 = view.findViewById(R.id.text_header1);
        range = view.findViewById(R.id.calendar_view_range);
        range.setOnDateChangedListener(this);
        range.setOnRangeSelectedListener(this);
        range.addDecorator(decorator);


    }

    public void set_on_click_litioner() {
        aply_wfh_submit.setOnClickListener(this);
        //date_from_wfh.setOnClickListener(this);
        // date_to_wfh.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.aply_wfh_submit:
                // wfh_date_from = date_from_wfh.getText().toString();
                // wfh_date_to = date_to_wfh.getText().toString();
                Log.i("date", "selectedDate::" + range.getSelectedDates().toString());
                wfh_leave_purpose = wfh_purpose.getText().toString();
                int reamin_Leave = Integer.parseInt(Remaining_WFH_leave);
                if (reamin_Leave != 0) {
//                    if (wfh_date_from == null) {
//                        Toast.makeText(getActivity(), "Please Enter Date From", Toast.LENGTH_SHORT).show();
 //                   } else
                        if (range.getSelectedDates().size()==0) {
                     Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
                 } else if (wfh_leave_purpose.equalsIgnoreCase("")) {
                        Toast.makeText(getActivity(), "Please Enter Purpose", Toast.LENGTH_SHORT).show();
                    } else {

                        if (isNetworkAvailable()) {
                           // Log.e("start", wfh_date_from);
                           // Log.e("end", wfh_date_to);
                            if (range.getSelectedDates().size() > 1) {
                                CalendarDay cal1=range.getSelectedDates().get(0);
                                wfh_date_from = cal1.getYear() + "-" + cal1.getMonth() + "-" + cal1.getDay();

                                CalendarDay cal2=range.getSelectedDates().get(range.getSelectedDates().size()-1);
                                wfh_date_to=cal2.getYear() + "-" + cal2.getMonth() + "-" + cal2.getDay();

                                Log.i("date::", "wfh_date_from::" +wfh_date_from);
                                Log.i("date2::", "wfh_date_to::" +wfh_date_to);
                                Apply_WFH_leave_method(user_id, wfh_date_from, wfh_date_to, wfh_leave_purpose);
                            } else if (range.getSelectedDates().size() > 0 && range.getSelectedDates().size() == 1) {
                                CalendarDay cal1=range.getSelectedDates().get(0);
                                wfh_date_from=cal1.getYear() + "-" + cal1.getMonth() + "-" + cal1.getDay();
                                Log.i("date::", "wfh_date_from::" +wfh_date_from);
                                Apply_WFH_leave_method(user_id, wfh_date_from, wfh_date_from, wfh_leave_purpose);
                            }
                        }else {
                            Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "You Already used All WFH Leave", Toast.LENGTH_SHORT).show();

                }
                break;

    /*case R.id.date_from_wfh:
        onDateSet();
        picker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_from_wfh.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                }, year, month, day);
        picker.show();
        break;

    case R.id.date_to_wfh:

        onDateSet();
        picker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_to_wfh.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    }
                }, year, month, day);
        picker.show();
        break;*/
        }
    }

    private void Apply_WFH_leave_method(String userid, String fdate, String tdate, String purpose) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ApplyWFHResponseItem>> call1 = apiInterface.ApplyWFH_submit(userid, fdate, tdate, purpose);
        call1.enqueue(new Callback<List<ApplyWFHResponseItem>>() {
            @Override
            public void onResponse(Call<List<ApplyWFHResponseItem>> call, Response<List<ApplyWFHResponseItem>> response) {
                List<ApplyWFHResponseItem> applyWFHResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (applyWFHResponseItem != null && applyWFHResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < applyWFHResponseItem.size(); i++) {
                        res = applyWFHResponseItem.get(i).getResponse();


                    }

                    if (res.equalsIgnoreCase("success ")) {
                        Toast.makeText(getActivity(), " Applied Success", Toast.LENGTH_SHORT).show();
                        TabLayoutFragment tablayoutFragment = new TabLayoutFragment();
                        openFragment(tablayoutFragment);
                    } else {
                        Toast.makeText(getActivity(), "" + res, Toast.LENGTH_SHORT).show();
                        TabLayoutFragment tablayoutFragment = new TabLayoutFragment();
                        openFragment(tablayoutFragment);
                    }

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<ApplyWFHResponseItem>> call, Throwable t) {
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

    public boolean isNetworkAvailable() {
        final android.net.ConnectivityManager connectivityManager = ((android.net.ConnectivityManager) getActivity().getSystemService(android.content.Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public void onDateSet() {
        final Calendar cldr = Calendar.getInstance();
        day = cldr.get(Calendar.DAY_OF_MONTH);
        month = cldr.get(Calendar.MONTH);
        year = cldr.get(Calendar.YEAR);
        // date picker dialog

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
            wfh_date_from = calendarDay.getYear() + "-" + calendarDay.getMonth() + "-" + calendarDay.getDay();
            wfh_date_to = calendarDay2.getYear() + "-" + calendarDay2.getMonth() + "-" + calendarDay2.getDay();
            // Toast.makeText(getActivity(), "start date:: "+wfh_date_from+" end date:: "+wfh_date_to, Toast.LENGTH_SHORT).show();
            Log.i("Datest", "start date:: " + dates.get(0));
            Log.i("Datest", "end date:: " + dates.get(dates.size() - 1));
            decorator.addFirstAndLast(dates.get(0), dates.get(dates.size() - 1));
            range.invalidateDecorators();
        }
    }
}