package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

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
import com.ossi.genreporting.model.ApplyLeaveResponseItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEventFragment extends Fragment implements View.OnClickListener {
    View view;
    EditText header_event, description_event,locationEvent,organiserName;
    TextView date_event;
    Button add_event_btn;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    String heading, description, date, res,organiser,eventLocation,venueEvent;
    TextView text_header1, employee_name,text_for_select;
    TextView login_time;
    RecyclerView event_list_rv;
    private String user_id;
    private String day_,month_;
    RoundedImageView img_profile;
    Spinner venue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_event, container, false);
        find_view_by_id(view);
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

        text_header1.setText("Create Events");
        text_for_select.setText("");


        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Select Venue");
        arrayList1.add("Office");
        arrayList1.add("Outside");
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        venue.setAdapter(arrayAdapter1);

        venue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(venue.getSelectedItem().equals("Office")){
                    locationEvent.setVisibility(View.VISIBLE);
                    locationEvent.setHint("Enter Room No");

                }else if(venue.getSelectedItem().equals("Outside")){
                    locationEvent.setHint("Enter Location");
                    locationEvent.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    public void find_view_by_id(View view) {
        header_event = view.findViewById(R.id.header_event);
        description_event = view.findViewById(R.id.description_event);
        date_event = view.findViewById(R.id.date_event);
        add_event_btn = view.findViewById(R.id.add_event_btn);

        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);
        text_for_select=view.findViewById(R.id.text_for_select);
        venue=view.findViewById(R.id.venue);
        locationEvent=view.findViewById(R.id.locationEvent);
        organiserName=view.findViewById(R.id.organiserName);

    }

    public void set_on_click_litioner() {
        add_event_btn.setOnClickListener(this);
        date_event.setOnClickListener(this);
    }

    private void add_new_event() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
       // date="04/05/2023";
        Call<List<ApplyLeaveResponseItem>> call1 = apiInterface.submit_event(user_id,heading, date, description,venueEvent,organiser,eventLocation);
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

                        if (res.equalsIgnoreCase("success")) {
                            Toast.makeText(getActivity(), "Event Created SuccessFully", Toast.LENGTH_SHORT).show();
                            AddShowEventFragment addShowEventFragment = new AddShowEventFragment();
                            openFragment(addShowEventFragment);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_event_btn:
                heading = header_event.getText().toString();
                description = description_event.getText().toString();
                date = date_event.getText().toString();
                venueEvent=venue.getSelectedItem().toString();
                organiser=organiserName.getText().toString();
                eventLocation=locationEvent.getText().toString();

                if (heading.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Event Heading", Toast.LENGTH_SHORT).show();
                } else if (description.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Description", Toast.LENGTH_SHORT).show();
                }else if (organiser.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Description", Toast.LENGTH_SHORT).show();
                }
                else if (date.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
                } else if (venue.getSelectedItem().toString().equalsIgnoreCase("Select Venue")) {
                    Toast.makeText(getActivity(), "Please Select Venue", Toast.LENGTH_SHORT).show();
                }else if (locationEvent.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter location / Room No", Toast.LENGTH_SHORT).show();
                } else {
                    if (Util.isNetworkAvailable(getActivity())) {
                        add_new_event();
                    } else {
                        Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }


                break;

            case R.id.date_event:

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // on below line we are setting date to our edit text.

                        day_ = "" + dayOfMonth;
                        month_=""+monthOfYear;
                        if (dayOfMonth < 10) {
                            day_ = "0" + dayOfMonth;
                        }
                        if (monthOfYear < 10) {
                            int i=monthOfYear+1;
                            month_ = "0" + i ;
                        }
                        date_event.setText(day_ + "-" + (month_) + "-" + year);
                        // date_from.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);


                    }
                },

                        year, month, day);

                datePickerDialog.show();
                break;

        }
    }
}