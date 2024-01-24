package com.ossi.genreporting.fragment;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.decorators.RangeDayDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SalaryFragment extends Fragment implements View.OnClickListener {
    View view;
    Spinner select_year, select_month;
    Button download,download_sal;
    String user_id;
    TextView text_header1, employee_name;
    TextView login_time,text_for_select;
    String Month_str,year_str;
    RoundedImageView img_profile;
    WebView web;
    private String myUrl;
    ArrayList<String> month_List;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_salary, container, false);

        find_view_by_id();
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
       // getMonthNamesOfCurrentYear();


        text_header1.setText("Salary Slip");
        text_for_select.setText("");

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);




        ArrayList<String> year_List = new ArrayList<>();
        year_List.add("Select Year");
        year_List.add("2022");
        year_List.add("2023");
        year_List.add("2024");

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, year_List);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_year.setAdapter(arrayAdapter1);
        select_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year_str = select_year.getSelectedItem().toString();

                if(year_str.equalsIgnoreCase("2024")){
                    getMonthNamesOfCurrentYear();
                }else {
                    getallmonths();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        return view;
    }

    public void find_view_by_id() {
        select_month = view.findViewById(R.id.select_month);
        select_year = view.findViewById(R.id.select_year);
        download = view.findViewById(R.id.download);

        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);
        text_for_select=view.findViewById(R.id.text_for_select);


    }

    public void set_on_click_litioner() {
        download.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case  R.id.download:
                if(year_str.equalsIgnoreCase("Select Year")){
                    Toast.makeText(getActivity(), "Please Select Year", Toast.LENGTH_SHORT).show();
                }else if (Month_str.equalsIgnoreCase("Select Month")){
                    Toast.makeText(getActivity(), "Please Select Month", Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent=new Intent(getActivity(),ViewSalarySlip.class);
                    intent.putExtra("Year",year_str);
                    intent.putExtra("Month",Month_str);
                    startActivity(intent);


                }

                break;


        }

    }
    public ArrayList<String> getMonthNamesOfCurrentYear() {
        YearMonth currentMonth = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentMonth = YearMonth.now();
            Log.e("current month",currentMonth.toString());
        }
         month_List = new ArrayList<>();
        month_List.add("Select Month");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            for (int month = 1; month <= currentMonth.getMonthValue()-1; month++) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String mon="0"+month;
                    if (mon.equalsIgnoreCase("01")) {
                        month_List.add(String.valueOf("January"));
                    }
                    if (mon.equalsIgnoreCase("02")) {
                        month_List.add(String.valueOf("February"));
                    }
                    if (mon.equalsIgnoreCase("03")) {
                        month_List.add(String.valueOf("March"));
                    }
                    if (mon.equalsIgnoreCase("04")) {
                        month_List.add(String.valueOf("April"));
                    }
                    if (mon.equalsIgnoreCase("05")) {
                        month_List.add(String.valueOf("May"));
                    }
                    if (mon.equalsIgnoreCase("06")) {
                        month_List.add(String.valueOf("June"));
                    }
                    if (mon.equalsIgnoreCase("07")) {
                        month_List.add(String.valueOf("July"));
                    }
                    if (mon.equalsIgnoreCase("08")) {
                        month_List.add(String.valueOf("August"));
                    }
                    if (mon.equalsIgnoreCase("09")) {
                        month_List.add(String.valueOf("September"));
                    }
                    if (mon.equalsIgnoreCase("10")) {
                        month_List.add(String.valueOf("October"));
                    }
                    if (mon.equalsIgnoreCase("11")) {
                        month_List.add(String.valueOf("November"));
                    }
                    if (mon.equalsIgnoreCase("12")) {
                        month_List.add(String.valueOf("December"));
                    }

                }
            }
        }
        Log.e("months: ", String.valueOf(month_List));
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, month_List);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_month.setAdapter(arrayAdapter);
        select_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String mon = select_month.getSelectedItem().toString();
                if (mon.equalsIgnoreCase("January")) {
                    Month_str="01";

                }
                if (mon.equalsIgnoreCase("February")) {
                    Month_str="02";
                }
                if (mon.equalsIgnoreCase("March")) {
                    Month_str="03";
                }
                if (mon.equalsIgnoreCase("April")) {
                    Month_str="04";
                }
                if (mon.equalsIgnoreCase("May")) {
                    Month_str="05";
                }
                if (mon.equalsIgnoreCase("June")) {
                    Month_str="06";
                }
                if (mon.equalsIgnoreCase("July")) {
                    Month_str="07";
                }
                if (mon.equalsIgnoreCase("August")) {
                    Month_str="08";
                }
                if (mon.equalsIgnoreCase("September")) {
                    Month_str="09";
                }
                if (mon.equalsIgnoreCase("October")) {
                    Month_str="10";
                }
                if (mon.equalsIgnoreCase("November")) {
                    Month_str="11";
                }
                if (mon.equalsIgnoreCase("December")) {
                    Month_str="12";
                }


                //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return month_List;
    }

    public void getallmonths(){
        month_List = new ArrayList<>();
        month_List.add("Select Month");
        month_List.add("January");
        month_List.add("February");
        month_List.add("March");
        month_List.add("April");
        month_List.add("May");
        month_List.add("June");
        month_List.add("July");
        month_List.add("August");
        month_List.add("September");
        month_List.add("October");
        month_List.add("November");
        month_List.add("December");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, month_List);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_month.setAdapter(arrayAdapter);
        select_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               String mon = select_month.getSelectedItem().toString();

                if (mon.equalsIgnoreCase("January")) {
                    Month_str="01";

                }
                if (mon.equalsIgnoreCase("February")) {
                    Month_str="02";
                }
                if (mon.equalsIgnoreCase("March")) {
                    Month_str="03";
                }
                if (mon.equalsIgnoreCase("April")) {
                    Month_str="04";
                }
                if (mon.equalsIgnoreCase("May")) {
                    Month_str="05";
                }
                if (mon.equalsIgnoreCase("June")) {
                    Month_str="06";
                }
                if (mon.equalsIgnoreCase("July")) {
                    Month_str="07";
                }
                if (mon.equalsIgnoreCase("August")) {
                    Month_str="08";
                }
                if (mon.equalsIgnoreCase("September")) {
                    Month_str="09";
                }
                if (mon.equalsIgnoreCase("October")) {
                    Month_str="10";
                }
                if (mon.equalsIgnoreCase("November")) {
                    Month_str="11";
                }
                if (mon.equalsIgnoreCase("December")) {
                    Month_str="12";
                }


                //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

}