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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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



        text_header1.setText("Salary Slip");
        text_for_select.setText("");

        ArrayList<String> month_List = new ArrayList<>();
        month_List.add("Select Month");
        month_List.add("01");
        month_List.add("02");
        month_List.add("03");
        month_List.add("04");
        month_List.add("05");
        month_List.add("06");
        month_List.add("07");
        month_List.add("08");
        month_List.add("09");
        month_List.add("10");
        month_List.add("11");
        month_List.add("12");
        /*month_List.add("January");
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
        month_List.add("December");*/

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, month_List);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_month.setAdapter(arrayAdapter);
        select_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Month_str = select_month.getSelectedItem().toString();

                //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        ArrayList<String> year_List = new ArrayList<>();
        year_List.add("Select Year");
        year_List.add("2022");
        year_List.add("2023");

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, year_List);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        select_year.setAdapter(arrayAdapter1);
        select_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year_str = select_year.getSelectedItem().toString();
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


}