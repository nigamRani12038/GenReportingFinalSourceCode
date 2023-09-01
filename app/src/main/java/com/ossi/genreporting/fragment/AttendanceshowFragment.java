package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ossi.genreporting.R;

import java.util.ArrayList;

public class AttendanceshowFragment extends Fragment {
View view;
    TextView present_day,absent_day,adjust_day,adjust_by_name;
    private String user_id;
   String Absent_day,Present_day,Adjust_day,Adjust_by_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_attendanceshow, container, false);

        find_view_by_id(view);
        set_on_click_litioner();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);

        Bundle bundle = getArguments();
        if (bundle != null) {
            Absent_day = bundle.getString("Absent_day");
            Present_day = bundle.getString("Present_day");
            Adjust_day = bundle.getString("Adjust_day");
            Adjust_by_name = bundle.getString("Adjust_by_name");


            absent_day.setText(Absent_day);
            present_day.setText(Present_day);
            adjust_day.setText(Adjust_day);
            adjust_by_name.setText(Adjust_by_name);

        }



        return view;
    }

    public void find_view_by_id(View v){

        present_day=v.findViewById(R.id.present_day);
        absent_day=v.findViewById(R.id.absent_day);
        adjust_day=v.findViewById(R.id.adjust_day);
        adjust_by_name=v.findViewById(R.id.adjust_by_name);



    }

    public void set_on_click_litioner(){

    }

}