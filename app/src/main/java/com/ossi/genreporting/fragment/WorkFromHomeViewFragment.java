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

import android.util.Log;
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

import com.ossi.genreporting.Adapter.ExpenseViewAdapter;
import com.ossi.genreporting.Adapter.WfhViewAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.ViewWorkFromHomeResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkFromHomeViewFragment extends Fragment implements View.OnClickListener {
    View view;
   // Button apply_wfh_;
    Spinner date_spin, approved_type_wfh;
    String month1, approved_type;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private ViewWorkFromHomeResponse model;
    private ArrayList<ViewWorkFromHomeResponse> wfh_list;
    WfhViewAdapter wfh_adapter;
    RecyclerView wfh_view;
    private String user_id;
   // private String approve_type_wfh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_work_from_home_view, container, false);

        find_view_by_id(view);
        set_on_click_litioner();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);

        Bundle bundle = getArguments();
        if (bundle != null) {
            approved_type = bundle.getString("wfh approve type");
            //Toast.makeText(getActivity(), ""+approve_type_exp, Toast.LENGTH_SHORT).show();
            DateFormat dateFormat = new SimpleDateFormat("MM");
            Date date = new Date();
            Log.d("Month",dateFormat.format(date));
            month1=dateFormat.format(date);
            ArrayList<String> arrayList = new ArrayList<>();

            arrayList.add(month1);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            date_spin.setAdapter(arrayAdapter);

            /*ArrayList<String> arrayList = new ArrayList<>();
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
            date_spin.setAdapter(arrayAdapter);
            date_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    month1 = date_spin.getSelectedItem().toString();
                        }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
*/

            ArrayList<String> arrayList1 = new ArrayList<>();
            arrayList1.add(approved_type);

            ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList1);
            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            approved_type_wfh.setAdapter(arrayAdapter1);

            if(isNetworkAvailable()) {
                Get_wfh_data_();
            }else {
                Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

       else {
           ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Select Approved Type");
        arrayList1.add("Approved");
        arrayList1.add("Pending");
        arrayList1.add("Rejected");


        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        approved_type_wfh.setAdapter(arrayAdapter1);
        approved_type_wfh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                approved_type = approved_type_wfh.getSelectedItem().toString();
                month1 = date_spin.getSelectedItem().toString();

                if (date_spin.getSelectedItem().equals("Select Months")) {
                    Toast.makeText(getActivity(), "Please Select Months", Toast.LENGTH_SHORT).show();
                } else if (approved_type_wfh.getSelectedItem().equals("Select Approved Type")) {
                    Toast.makeText(getActivity(), "Please Select Approved Type", Toast.LENGTH_SHORT).show();
                } else {
                    if(isNetworkAvailable()) {
                        Get_wfh_data_();
                    }else {
                        Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


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
        date_spin.setAdapter(arrayAdapter);
        date_spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month1 = date_spin.getSelectedItem().toString();
                if (date_spin.getSelectedItem().equals("Select Months")) {
                    Toast.makeText(getActivity(), "Please Select Months", Toast.LENGTH_SHORT).show();
                } else if (approved_type_wfh.getSelectedItem().equals("Select Approved Type")) {
                    Toast.makeText(getActivity(), "Please Select Approved Type", Toast.LENGTH_SHORT).show();
                } else {
                    if(isNetworkAvailable()) {
                        Get_wfh_data_();
                    }else {
                        Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                //Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }
        return view;
    }

    public void find_view_by_id(View view) {
       // apply_wfh_ = view.findViewById(R.id.apply_wfh_);
        date_spin = view.findViewById(R.id.date_spin);
        wfh_view = view.findViewById(R.id.wfh_view);
        approved_type_wfh = view.findViewById(R.id.approved_type_wfh);
    }

    public void set_on_click_litioner() {
        //apply_wfh_.setOnClickListener(this);
        //date_spin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
           /* case R.id.apply_wfh_:

                ApplyWorkFromHomeFragment workFromHomeFragment = new ApplyWorkFromHomeFragment();
                openFragment(workFromHomeFragment);

                break;
*/
   /* case R.id.date_spin:
        onDateSet();
        picker = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date_spin.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);

                        String date_set=date_spin.getText().toString();
                        if(!date_set.equalsIgnoreCase("Please Select Month")){
                            // Toast.makeText(getActivity(), "Please Select Month First ", Toast.LENGTH_SHORT).show();
                            String date = date_set;
                            String[] dateParts = date.split("/");
                             year1 = dateParts[0];
                             month1 = dateParts[1];
                             date1 = dateParts[1];
                            Get_wfh_data_();
                            
                        }
                    }
                }, year, month, day);
        picker.show();
        break ;*/
        }

    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        // transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    private void Get_wfh_data_() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ViewWorkFromHomeResponse>> call1 = apiInterface.get_view_work_from_home(user_id, month1,approved_type);
        call1.enqueue(new Callback<List<ViewWorkFromHomeResponse>>() {
            @Override
            public void onResponse(Call<List<ViewWorkFromHomeResponse>> call, Response<List<ViewWorkFromHomeResponse>> response) {
                List<ViewWorkFromHomeResponse> ViewWorkFromHomeResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (ViewWorkFromHomeResponse != null && ViewWorkFromHomeResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    wfh_list = new ArrayList<>();
                    for (int i = 0; i < ViewWorkFromHomeResponse.size(); i++) {
                        String res = ViewWorkFromHomeResponse.get(i).getResponse();
                        model = new ViewWorkFromHomeResponse();
                        if (res.equalsIgnoreCase("success")) {

                            String response1 = ViewWorkFromHomeResponse.get(i).getResponse();
                            String leave_status = ViewWorkFromHomeResponse.get(i).getLeaveStatus();
                            String purpose = ViewWorkFromHomeResponse.get(i).getPurpose();
                            String data = ViewWorkFromHomeResponse.get(i).getFDate();
                            String approvebyname = ViewWorkFromHomeResponse.get(i).getApprovedName();

                            model.setResponse(response1);
                            model.setLeaveStatus(leave_status);
                            model.setPurpose(purpose);
                            model.setFDate(data);
                            model.setApprovedName(approvebyname);

                            wfh_list.add(model);


                        } else if (res.equalsIgnoreCase("Fail")) {
                            Toast.makeText(getActivity(), "No Any WFH Leave Apply", Toast.LENGTH_SHORT).show();
                        }


                    }

                    wfh_adapter = new WfhViewAdapter(wfh_list, getActivity());
                    wfh_view.setAdapter(wfh_adapter);
                    wfh_view.setLayoutManager(new LinearLayoutManager(getActivity()));


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<ViewWorkFromHomeResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }
    public boolean isNetworkAvailable() {
        final android.net.ConnectivityManager connectivityManager = ((android.net.ConnectivityManager) getActivity().getSystemService(android.content.Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}