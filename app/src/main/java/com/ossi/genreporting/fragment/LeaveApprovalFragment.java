package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.Adapter.RequestAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.AllRequestResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveApprovalFragment extends Fragment {
    View view;
    TextView text_header1, employee_name,text_for_select;
    TextView login_time;
    String user_id,request_type;
    RoundedImageView img_profile;

    RecyclerView comimg_req_admin;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private ArrayList<AllRequestResponse> request_list;
    RequestAdapter request_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_leave_approval, container, false);
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

        text_header1.setText("Request Leave");
        text_for_select.setText("");


        if (Util.isNetworkAvailable(getActivity())){
            get_my_request_list();
        }
        else {
            Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    public void find_view_by_id(View view){
        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);
        text_for_select=view.findViewById(R.id.text_for_select);
        comimg_req_admin=view.findViewById(R.id.comimg_req_admin);
    }
    public void set_on_click_litioner(){

    }
    private void get_my_request_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
        request_type="leave";
        Call<List<AllRequestResponse>> call1 = apiInterface.get_all_req_lists(user_id,request_type);
        call1.enqueue(new Callback<List<AllRequestResponse>>() {
            @Override
            public void onResponse(Call<List<AllRequestResponse>> call, Response<List<AllRequestResponse>> response) {
                List<AllRequestResponse> my_req_res = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (my_req_res != null && my_req_res.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    request_list = new ArrayList<>();
                    for (int i = 0; i < my_req_res.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        AllRequestResponse   model=new AllRequestResponse();

                        String resPonse = my_req_res.get(i).getResponse();
                        if (resPonse.equalsIgnoreCase("success")){
                            String emp_name = my_req_res.get(i).getEmpName();
                            String type = my_req_res.get(i).getType();


                            String total_count = my_req_res.get(i).getCount();
                            String id = my_req_res.get(i).getId();
                            String tdate = my_req_res.get(i).getTDate();
                            String fdate = my_req_res.get(i).getFDate();
                            String reason = my_req_res.get(i).getPurpose();
                            String leaveType = my_req_res.get(i).getTypeLeave();
                            String timeLeave = my_req_res.get(i).getLeaveTime();
                            String availCl = my_req_res.get(i).getAvailableCL();
                            String avaiEl = my_req_res.get(i).getAvailableEL();
                            String avaiSl = my_req_res.get(i).getAvailableSL();
                            String avaiWfh = my_req_res.get(i).getAvailableWFH();
                            model.setTypeLeave(leaveType);
                            model.setLeaveTime(timeLeave);

                            model.setResponse(resPonse);
                            model.setEmpName(emp_name);
                            model.setType(type);

                            model.setCount(total_count);
                            model.setId(id);
                            model.setTDate(tdate);
                            model.setFDate(fdate);
                            model.setPurpose(reason);
                            model.setAvailableCL(availCl);
                            model.setDepartment("");
                            model.setAvailableEL(avaiEl);
                            model.setAvailableSL(avaiSl);
                            model.setAvailableWFH(avaiWfh);

                            request_list.add(model);
                        } else if(resPonse.equalsIgnoreCase("Fail")){
                            Toast.makeText(getActivity(), "No Reuest for approval", Toast.LENGTH_SHORT).show();
                        }
                    }


                    request_adapter = new RequestAdapter(request_list, getActivity());
                    comimg_req_admin.setAdapter(request_adapter);
                    comimg_req_admin.setLayoutManager(new LinearLayoutManager(getActivity()));

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<List<AllRequestResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }



}