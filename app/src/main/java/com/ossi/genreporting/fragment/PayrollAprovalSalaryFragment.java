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

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;

import com.ossi.genreporting.Adapter.SalaryApprovalShowAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.PayrollSalaryShowResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PayrollAprovalSalaryFragment extends Fragment {



    View view;
    TextView text_header1, employee_name, text_for_select;

    TextView login_time;
    private String user_id;
    RoundedImageView img_profile;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String res;
    LinearLayout generated_already_salary,ll_show_data;
    TextView set_msg_salary;
    Button salary_approved;
    RecyclerView salary_status;
    private ArrayList<PayrollSalaryShowResponse> salary_list;
    private SalaryApprovalShowAdapter salaryApprovalShowAdapter;
    LottieAnimationView animate_wait,animate_approved;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_payroll_aproval_salary, container, false);
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

        text_header1.setText("Request For Approval");
        text_for_select.setText("");
        if (Util.isNetworkAvailable(getActivity())) {
            Request_approval_show_salary();
        }else {
            Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        salary_approved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.isNetworkAvailable(getActivity())) {
                    Approved_Salary();
                }else {
                    Toast.makeText(getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }

    public void find_view_by_id(View view) {

        employee_name = view.findViewById(R.id.employee_name);
        login_time = view.findViewById(R.id.login_time);
        text_header1 = view.findViewById(R.id.text_header1);
        img_profile = view.findViewById(R.id.img_profile);
        text_for_select = view.findViewById(R.id.text_for_select);
        generated_already_salary = view.findViewById(R.id.generated_already_salary);
        set_msg_salary = view.findViewById(R.id.set_msg_salary);
        ll_show_data=view.findViewById(R.id.ll_show_data);
        salary_approved=view.findViewById(R.id.salary_approved);
        salary_status=view.findViewById(R.id.salary_status);
        animate_approved=view.findViewById(R.id.animate_approved);
        animate_wait=view.findViewById(R.id.animate_wait);



    }


    public void set_on_click_litioner() {



    }
    private void Request_approval_show_salary() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<PayrollSalaryShowResponse>> call1 = apiInterface.req_salary_approval(user_id);
        call1.enqueue(new Callback<List<PayrollSalaryShowResponse>>() {
            @Override
            public void onResponse(Call<List<PayrollSalaryShowResponse>> call, Response<List<PayrollSalaryShowResponse>> response) {
                List<PayrollSalaryShowResponse> payrollSalaryShowResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (payrollSalaryShowResponse != null && payrollSalaryShowResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    salary_list = new ArrayList<>();
                    for (int i = 0; i < payrollSalaryShowResponse.size(); i++) {
                        res = payrollSalaryShowResponse.get(i).getResponse();
                        PayrollSalaryShowResponse model=new PayrollSalaryShowResponse();
                        if (res.equalsIgnoreCase("Salary for January month has not been generated.")) {
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for January month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }else if(res.equalsIgnoreCase("Salary for February month has not been generated.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for February month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if(res.equalsIgnoreCase("Salary for March month has not been generated.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for March month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if(res.equalsIgnoreCase("Salary for April month has not been generated.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for April month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if(res.equalsIgnoreCase("Salary for May month has not been generated.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for May month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if(res.equalsIgnoreCase("Salary for Jun month has not been generated.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for Jun month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if(res.equalsIgnoreCase("Salary for July month has not been generated.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for July month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if(res.equalsIgnoreCase("Salary for August month has not been generated.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for August month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if(res.equalsIgnoreCase("Salary for September month has not been generated.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for September month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }else if(res.equalsIgnoreCase("Salary for October month has not been generated.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for October month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }else if(res.equalsIgnoreCase("Salary for November month has not been generated.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for November month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }
                        else if(res.equalsIgnoreCase("Salary for December month has not been generated.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Salary for December month has not been generated.");
                            animate_wait.setVisibility(View.VISIBLE);
                            animate_approved.setVisibility(View.GONE);
                        }

                        else if(res.equalsIgnoreCase("Invalid UserID")){
                            Toast.makeText(getActivity(), "" + res, Toast.LENGTH_SHORT).show();
                            generated_already_salary.setVisibility(View.GONE);
                            set_msg_salary.setVisibility(View.GONE);
                        }
                        else if(res.equalsIgnoreCase("January month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("January month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                              } else if(res.equalsIgnoreCase("February month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("February month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("March month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("March month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("April month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("April month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("May month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("May month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("Jun month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("Jun month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("July month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("July month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("August month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("August month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("September month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("September month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("October month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("October month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("November month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("November month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }
                        else if(res.equalsIgnoreCase("December month Salary has already been Approved.")){
                            generated_already_salary.setVisibility(View.VISIBLE);
                            set_msg_salary.setText("December month Salary has already been approved");
                            animate_wait.setVisibility(View.GONE);
                            animate_approved.setVisibility(View.VISIBLE);
                        }

                        else if(res.equalsIgnoreCase("success")){
                            //Toast.makeText(getActivity(), " Show data", Toast.LENGTH_SHORT).show();
                          //  salary_not_generate.setVisibility(View.GONE);
                            generated_already_salary.setVisibility(View.GONE);
                            ll_show_data.setVisibility(View.VISIBLE);

                            String Name=payrollSalaryShowResponse.get(i).getEmpName();
                            String net_salary=payrollSalaryShowResponse.get(i).getNetSalary();
                            String gross_total=payrollSalaryShowResponse.get(i).getGrossTotal();
                            String total_ctc=payrollSalaryShowResponse.get(i).getTotalCTC();
                            String Total_deduction=payrollSalaryShowResponse.get(i).getTotalDeducted();
                            String voucher=payrollSalaryShowResponse.get(i).getVoucher();


                            model.setEmpName(Name);
                            model.setNetSalary(net_salary);
                            model.setGrossTotal(gross_total);
                            model.setTotalCTC(total_ctc);
                            model.setTotalDeducted(Total_deduction);
                            model.setVoucher(voucher);


                           // model.setId(unique_id);




                            salary_list.add(model);

                        }


                    }
                    salaryApprovalShowAdapter = new SalaryApprovalShowAdapter(salary_list, getActivity());
                    salary_status.setAdapter(salaryApprovalShowAdapter);
                    salary_status.setLayoutManager(new LinearLayoutManager(getActivity()));

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<PayrollSalaryShowResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }
    private void Approved_Salary() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<PayrollSalaryShowResponse>> call1 = apiInterface.approved_salary(user_id,"yes");
        call1.enqueue(new Callback<List<PayrollSalaryShowResponse>>() {
            @Override
            public void onResponse(Call<List<PayrollSalaryShowResponse>> call, Response<List<PayrollSalaryShowResponse>> response) {
                List<PayrollSalaryShowResponse> payrollSalaryShowResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (payrollSalaryShowResponse != null && payrollSalaryShowResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    for (int i = 0; i < payrollSalaryShowResponse.size(); i++) {
                        res = payrollSalaryShowResponse.get(i).getResponse();
                        if (res.equalsIgnoreCase("success")) {
                            Toast.makeText(getActivity(), " Approved Success", Toast.LENGTH_SHORT).show();
                            AdminTabLayoutFragment tabLayoutFragment = new AdminTabLayoutFragment();
                            openFragment1Tab(tabLayoutFragment);

                        }else {
                            Toast.makeText(getActivity(), "res "+res, Toast.LENGTH_SHORT).show();
                        }


                    }

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<PayrollSalaryShowResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment, ""); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }
    private void openFragment1Tab(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment, "Tablayout"); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }
}