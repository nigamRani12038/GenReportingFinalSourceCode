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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.Adapter.MeetingListAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.ShowMeetingListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingFragment extends Fragment {
    View view;

    RecyclerView meeting_list_rv;
    LinearLayout view_all_eeting;
    private String user_id;
    //RoundedImageView img_profile;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    Button add_meeting_btn;
    private ArrayList<ShowMeetingListResponse> meeting_list;
    MeetingListAdapter meeting_adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_request_pending_for_admin, container, false);
        find_view_by_id(view);


        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);



        if (Util.isNetworkAvailable(getActivity())) {
            get_meeting_list();
        } else {
            Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

        view_all_eeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CurrentMeetingFragment add_Show_Meeting = new CurrentMeetingFragment();
                openFragment(add_Show_Meeting);

            }
        });

        return view;
    }

    public void find_view_by_id(View view) {
       /* employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);*/
        add_meeting_btn=view.findViewById(R.id.add_meeting_btn);
        view_all_eeting = view.findViewById(R.id.view_all_eeting);

        meeting_list_rv = view.findViewById(R.id.current_meeting_list_rv);

    }


    private void get_meeting_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ShowMeetingListResponse>> call1 = apiInterface.get_current_meting_lists(user_id);
        call1.enqueue(new Callback<List<ShowMeetingListResponse>>() {
            @Override
            public void onResponse(Call<List<ShowMeetingListResponse>> call, Response<List<ShowMeetingListResponse>> response) {
                List<ShowMeetingListResponse> my_task_res = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (my_task_res != null && my_task_res.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    meeting_list = new ArrayList<>();
                    for (int i = 0; i < my_task_res.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        ShowMeetingListResponse   model=new ShowMeetingListResponse();

                        String res = my_task_res.get(i).getResponse();
                        if(!res.equalsIgnoreCase("Fail")) {
                            String date = my_task_res.get(i).getMetdate();
                            String department = my_task_res.get(i).getMetDepament();
                            String heading = my_task_res.get(i).getMethead();
                            String details = my_task_res.get(i).getMetDetails();
                            String mode = my_task_res.get(i).getMetmode();
                            String sno = my_task_res.get(i).getSno();
                            String emp = my_task_res.get(i).getMetEmp();
                            String time = my_task_res.get(i).getMettime();
                            String assign_by = my_task_res.get(i).getAssign_By_Name();


                            model.setResponse(res);
                            model.setMetdate(date);
                            model.setMetDepament(department);
                            model.setMethead(heading);
                            model.setMetDetails(details);
                            model.setMetmode(mode);
                            model.setSno(sno);
                            model.setMetEmp(emp);
                            model.setMettime(time);
                            model.setAssign_By_Name(assign_by);
                            model.setCurrent_meet("yes");
                            meeting_list.add(model);
                        }else {
                            Toast.makeText(getActivity(), "Today is not Any Meeting", Toast.LENGTH_SHORT).show();
                        }

                    }


                    meeting_adapter = new MeetingListAdapter(meeting_list, getActivity());
                    meeting_list_rv.setAdapter(meeting_adapter);
                    meeting_list_rv.setLayoutManager(new LinearLayoutManager(getActivity()));

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<List<ShowMeetingListResponse>> call, Throwable t) {
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
       transaction.isAddToBackStackAllowed();
        transaction.commit();
    }
}