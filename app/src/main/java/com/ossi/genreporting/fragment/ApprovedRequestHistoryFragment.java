package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.Adapter.HistoryRequestAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.AbsentStatusResponse;
import com.ossi.genreporting.model.AllRequestResponse;
import com.ossi.genreporting.model.HistoryRequestResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ApprovedRequestHistoryFragment extends Fragment {
    TextView text_header1, employee_name, text_for_select;
    TextView login_time;
    RoundedImageView img_profile;
    RecyclerView history_req_admin;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private ArrayList<HistoryRequestResponse> request_history_list;
    HistoryRequestAdapter request_adapter;
    String Status_type,Request_type;
    View view;
    HistoryRequestResponse   model;
    Spinner requ_typ,status_typ;
    // private SharedPreferences pref;
    String user_id,request_type,status_type;
    TextView date_from,date_to;
    private String date;
    private String selected_date1,selected_date;
    EditText search_by_name;
    private String day_,month_;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_approved_request_history, container, false);


        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);
        user_id = pref.getString("user_id", " ");
        find_view_by_id();
        set_on_click_litioner();

        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        date_from.setText(currentDate);
        date_to.setText(currentDate);
        String currentDate1 = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());


        if (img_profile1 != null) {
            Glide.with(this).load(img_profile1).into(img_profile);
        }

        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: " + login_Time);

        text_header1.setText("Request History");
        text_for_select.setText("");

        selected_date=currentDate1;
        selected_date1=currentDate1;

        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Leave");
        arrayList1.add("WFH");
        arrayList1.add("Expense");
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requ_typ.setAdapter(arrayAdapter1);
        requ_typ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 Request_type=requ_typ.getSelectedItem().toString();
                if (!requ_typ.getSelectedItem().equals(" ")) {

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        ArrayList<String> arrayList2 = new ArrayList<>();

        arrayList2.add("Approved");
        arrayList2.add("Reject");
        arrayList2.add("All");
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList2);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        status_typ.setAdapter(arrayAdapter2);
        status_typ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 Status_type=status_typ.getSelectedItem().toString();
                if (!status_typ.getSelectedItem().equals(" ")) {
                    get_my_request_history_list();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });





        date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                date_dialog();
            }
        });

        date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    date_dialog1();
            }
        });


        return view;
    }

   public void date_dialog(){

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
                       date_from.setText(day_ + "-" + (month_) + "-" + year);
                       // date_from.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                       selected_date = year + "-" + (month_) + "-" + day_;
                       get_my_request_history_list();

                   }
               },

               year, month, day);

       datePickerDialog.show();
    }

    public void date_dialog1(){
        final Calendar c = Calendar.getInstance();

        // on below line we are getting
        // our day, month and year.
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);

        // on below line we are creating a variable for date picker dialog.
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                // on below line we are passing context.
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        day_ = "" + dayOfMonth;
                        month_=""+monthOfYear;
                        if (dayOfMonth < 10) {
                            day_ = "0" + dayOfMonth;
                        }
                        if (monthOfYear < 10) {
                            int i = monthOfYear+1;
                            month_ = "0" + i;
                        }
                        date_to.setText(day_ + "-" + (month_) + "-" + year);
                        selected_date = year + "-" + (month_) + "-" + day_;
                        get_my_request_history_list();
                    }
                },
                // on below line we are passing year,
                // month and day for selected date in our date picker.
                year, month, day);
        // at last we are calling show to
        // display our date picker dialog.
        datePickerDialog.show();
    }

    public void find_view_by_id() {
        history_req_admin=view.findViewById(R.id.history_req_admin);
        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);
        text_for_select = view.findViewById(R.id.text_for_select);
        requ_typ=view.findViewById(R.id.requ_typ);
        status_typ=view.findViewById(R.id.status_typ);
        date_from=view.findViewById(R.id.date_from);
        date_to=view.findViewById(R.id.date_to);
        search_by_name=view.findViewById(R.id.search_by_name);
    }

    public void set_on_click_litioner() {

    }

    private void get_my_request_history_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
       // request_type="leave";
        //status_type="all";

        Call<List<HistoryRequestResponse>> call1 = apiInterface.get_History_approved_request(user_id,Request_type,selected_date,selected_date1,Status_type);
        call1.enqueue(new Callback<List<HistoryRequestResponse>>() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onResponse(Call<List<HistoryRequestResponse>> call, Response<List<HistoryRequestResponse>> response) {
                List<HistoryRequestResponse> my_req_res = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                if (my_req_res != null && my_req_res.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    request_history_list = new ArrayList<>();
                    for (int i = 0; i < my_req_res.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                           model=new HistoryRequestResponse();
                        String res=my_req_res.get(i).getResponse();
                        if(!res.equalsIgnoreCase("Fail")) {


                            String emp_name = my_req_res.get(i).getEmpName();
                            String total_days = my_req_res.get(i).getTotalDay();
                            String fdate = my_req_res.get(i).getFromDate();
                            String tdate = my_req_res.get(i).getToDate();
                            String approved_by = my_req_res.get(i).getApprove_Name();
                            String status_approved_reject = my_req_res.get(i).getStatus();
                            String purpose = my_req_res.get(i).getPurpose();
                            String id = my_req_res.get(i).getId();


                            model.setEmpName(emp_name);
                            model.setResponse(res);
                            model.setTotalDay(total_days);
                            model.setFromDate(fdate);
                            model.setToDate(tdate);
                            model.setApprove_Name(approved_by);
                            model.setStatus(status_approved_reject);
                            model.setPurpose(purpose);
                            model.setId(id);

                            if(res.equalsIgnoreCase("Leave")){
                                String TypeLeave = my_req_res.get(i).getTypeLeave();
                                String LeaveTime = my_req_res.get(i).getLeaveTime();
                                model.setTypeLeave(TypeLeave);
                                model.setLeaveTime(LeaveTime);
                            }
                            //model.setDepartment("HR");


                            request_history_list.add(model);

                            if (mProgressDialog.isShowing())
                                mProgressDialog.dismiss();
                        }else if(res.equalsIgnoreCase("Fail")){
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                        }

                    }


                    request_adapter = new HistoryRequestAdapter(request_history_list, getActivity());
                    history_req_admin.setAdapter(request_adapter);
                    history_req_admin.setLayoutManager(new LinearLayoutManager(getActivity()));

                    search_by_name.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            // TODO Auto-generated method stub
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            filter(s.toString());
                        }
                    });

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<List<HistoryRequestResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<HistoryRequestResponse> filteredlist= new ArrayList();
        // running a for loop to compare elements.
        for (int i=0;i<request_history_list.size();i++) {
            // checking if the entered string matched with any item of our recycler view.
            if (request_history_list.get(i).getEmpName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(request_history_list.get(i));
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            //  Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            request_adapter.updateList(filteredlist);
        }
    }

    public boolean isNetworkAvailable() {
        final android.net.ConnectivityManager connectivityManager = ((android.net.ConnectivityManager) getActivity().getSystemService(android.content.Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}