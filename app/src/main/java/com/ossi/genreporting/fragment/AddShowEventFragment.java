package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.Adapter.ShowAllEventAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.AllEventsShow;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddShowEventFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView text_header1, employee_name,text_for_select;
    TextView login_time;
    RecyclerView event_list_rv;
    private String user_id;
    RoundedImageView img_profile;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    Button add_new_event_btn;
    private ArrayList<AllEventsShow> event_list;
    ShowAllEventAdapter showAllEventAdapter;
    String res_success,Event_Date,Event_Name,Description,id,S_No,db_Time;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add__show__event_, container, false);

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

        text_header1.setText("All Events");
        text_for_select.setText("");


        if (isNetworkAvailable()) {
            get_all_event_list();
        } else {
            Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return view;

    }

    public void find_view_by_id(View view){
        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);
        add_new_event_btn=view.findViewById(R.id.add_new_event_btn);
        text_for_select=view.findViewById(R.id.text_for_select);

        event_list_rv=view.findViewById(R.id.event_list_rv);

    }
    public void set_on_click_litioner(){
        add_new_event_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_new_event_btn:
                AddEventFragment addEventFragment = new AddEventFragment();
                openFragment(addEventFragment);
                break;
        }
    }

    private void get_all_event_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<AllEventsShow>> call1 = apiInterface.get_all_events_lists();
        call1.enqueue(new Callback<List<AllEventsShow>>() {
            @Override
            public void onResponse(Call<List<AllEventsShow>> call, Response<List<AllEventsShow>> response) {
                List<AllEventsShow> my_task_res = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (my_task_res != null && my_task_res.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    event_list = new ArrayList<>();
                    for (int i = 0; i < my_task_res.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        AllEventsShow model = new AllEventsShow();

                        res_success = my_task_res.get(i).getResponse();
                        if (res_success.equalsIgnoreCase("")) {
                            Event_Date = my_task_res.get(i).getEventDate();
                            Event_Name = my_task_res.get(i).getEventName();
                            Description = my_task_res.get(i).getEventDescription();
                            id = my_task_res.get(i).getId();
                            db_Time = my_task_res.get(i).getDBTime();
                            S_No = my_task_res.get(i).getSno();


                            model.setResponse(res_success);
                            model.setEventName(Event_Name);
                            model.setEventDate(Event_Date);
                            model.setEventDescription(Description);
                            model.setId(id);
                            model.setDBTime(db_Time);
                            model.setSno(S_No);
                            event_list.add(model);

                        }
                        else if(res_success.equalsIgnoreCase("Fail")){
                            Toast.makeText(getActivity(), "No Any event Please Add First Event", Toast.LENGTH_SHORT).show();
                        }
                    }

                    showAllEventAdapter = new ShowAllEventAdapter(event_list, getActivity());
                    event_list_rv.setAdapter(showAllEventAdapter);
                    event_list_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<List<AllEventsShow>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
    public boolean isNetworkAvailable() {
        final android.net.ConnectivityManager connectivityManager = ((android.net.ConnectivityManager) getActivity().getSystemService(android.content.Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}