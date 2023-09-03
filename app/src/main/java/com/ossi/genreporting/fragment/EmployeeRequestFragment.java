package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.ApplyLeaveResponseItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeRequestFragment extends Fragment implements View.OnClickListener {
View view;
    TextView text_header1,employee_name;
    TextView login_time,text_bill_hide_show;
    private String user_id;
    EditText ammount_expense;
    RoundedImageView img_profile;
    TextView reason_request;
    String Quantity_req_str,Reason_request_str,Ammount_expense;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    Button req_btn;
    private String res;
    EditText quantity_req;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_employee_request, container, false);


        find_view_by_id(view);
        set_on_click_litioner();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);

        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");

        if(img_profile1!=null){
            Glide.with(this).load(img_profile1).into(img_profile);
        }

        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: "+login_Time);

        text_header1.setText("Employee Request");
        text_bill_hide_show.setVisibility(View.GONE);
        ammount_expense.setHint("Enter Asset Name");

        return view;
    }

    public void find_view_by_id(View view) {
        employee_name=view.findViewById(R.id.employee_name);
        login_time=view.findViewById(R.id.login_time);
        text_header1=view.findViewById(R.id.text_header1);
        text_bill_hide_show=view.findViewById(R.id.text_bill_hide_show);
        ammount_expense=view.findViewById(R.id.ammount_expense);
        img_profile=view.findViewById(R.id.img_profile);
        quantity_req=view.findViewById(R.id.quantity_req);
        reason_request=view.findViewById(R.id.reason_request);
        req_btn=view.findViewById(R.id.req_btn);

        ammount_expense.setInputType(InputType.TYPE_CLASS_TEXT);
    }

    public void set_on_click_litioner() {
        req_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.req_btn:
                Quantity_req_str = quantity_req.getText().toString();
                Reason_request_str = reason_request.getText().toString();
                Ammount_expense=ammount_expense.getText().toString();

                if (Ammount_expense.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Asset Name", Toast.LENGTH_SHORT).show();
                } else if (Quantity_req_str.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Select Quantity", Toast.LENGTH_SHORT).show();
                } else if (Reason_request_str.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Purpose", Toast.LENGTH_SHORT).show();
                } else {

                    if (Util.isNetworkAvailable(getActivity())) {
                        Employee_request_method();
                    } else {
                        Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
    }


    private void Employee_request_method() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ApplyLeaveResponseItem>> call1 = apiInterface.employee_request_send(user_id, Ammount_expense, Quantity_req_str, Reason_request_str);
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
                            Toast.makeText(getActivity(), " Send Employee Request", Toast.LENGTH_SHORT).show();
                            TabLayoutFragment tabLayoutFragment = new TabLayoutFragment();
                            openFragment(tabLayoutFragment);
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
        // transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

}