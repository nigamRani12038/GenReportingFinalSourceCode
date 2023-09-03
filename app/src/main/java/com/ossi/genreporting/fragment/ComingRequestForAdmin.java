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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ossi.genreporting.Adapter.RequestAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.AllRequestResponse;
import com.ossi.genreporting.model.AllRequestResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ComingRequestForAdmin extends Fragment {
    RecyclerView comimg_req_admin;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private ArrayList<AllRequestResponse> request_list;
    RequestAdapter request_adapter;
    View view;
   // private SharedPreferences pref;
    String user_id,request_type;
    LinearLayout view_all_req;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_coming_for_admin, container, false);



        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);
        user_id = pref.getString("user_id", " ");
           find_view_by_id();

        if (Util.isNetworkAvailable(getActivity())){
            get_my_request_list();
        }
        else {
            Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }

        view_all_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewAllCommingRequestAdmin viewAllCommingRequestAdmin =new ViewAllCommingRequestAdmin();
                Bundle bundle = new Bundle();
                bundle.putString("show_req_from_hr", "Managment");
                viewAllCommingRequestAdmin.setArguments(bundle);
                openFragment(viewAllCommingRequestAdmin);
            }
        });

        return view;
    }

    public void find_view_by_id() {
        comimg_req_admin=view.findViewById(R.id.comimg_req_admin);
        view_all_req=view.findViewById(R.id.view_all_req);
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }

    private void get_my_request_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();
        request_type="all";
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
                        if (resPonse.equalsIgnoreCase("success")) {
                            String emp_name = my_req_res.get(i).getEmpName();
                            String type = my_req_res.get(i).getType();

                            String total_count = my_req_res.get(i).getCount();
                            String id = my_req_res.get(i).getId();
                            String tdate = my_req_res.get(i).getTDate();
                            String fdate = my_req_res.get(i).getFDate();
                            String reason = my_req_res.get(i).getPurpose();

                            model.setResponse(resPonse);
                            model.setEmpName(emp_name);
                            model.setType(type);

                            model.setCount(total_count);
                            model.setId(id);
                            model.setTDate(tdate);
                            model.setFDate(fdate);
                            model.setPurpose(reason);
                            model.setDepartment("Managment");

                            request_list.add(model);
                        }else if(resPonse.equalsIgnoreCase("Fail")){
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