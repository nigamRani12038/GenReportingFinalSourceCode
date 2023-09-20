package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ossi.genreporting.Adapter.SalaryApprovalShowAdapter;
import com.ossi.genreporting.Adapter.SalaryHistoryAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.PayrollSalaryHistoryResponse;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalaryHistoryFragment extends Fragment {
View view;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String res;
    LinearLayout data_not_available,ll_show_data;
    RecyclerView salary_status;
    Spinner year_select,month_select;
    private ArrayList<PayrollSalaryHistoryResponse> salary_list;
    private SalaryHistoryAdapter salaryHistoryAdapter ;
    String month1,year,user_id;
    LinearLayout waiting_hide;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_salary_history, container, false);
         find_view_by_id();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);

        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("Select Year");
        arrayList1.add("2022");
        arrayList1.add("2023");

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList1);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        year_select.setAdapter(arrayAdapter1);



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
        month_select.setAdapter(arrayAdapter);
        month_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month1 = month_select.getSelectedItem().toString();
                year=year_select.getSelectedItem().toString();
                 if (year_select.getSelectedItem().equals("Select Year")) {
                    Toast.makeText(getActivity(), "Please Select Year", Toast.LENGTH_SHORT).show();
                } else if (month_select.getSelectedItem().equals("Select Months")) {
                    Toast.makeText(getActivity(), "Please Select Months", Toast.LENGTH_SHORT).show();
                }
                 else {
                    if(Util.isNetworkAvailable(getActivity())) {
                        Request_approval_show_salary();
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


        return view;
    }

    public void find_view_by_id(){
        ll_show_data=view.findViewById(R.id.ll_show_data);
        data_not_available=view.findViewById(R.id.data_not_available);
        month_select=view.findViewById(R.id.month_select);
        year_select=view.findViewById(R.id.year_select);
        waiting_hide=view.findViewById(R.id.waiting_hide);
        salary_status=view.findViewById(R.id.salary_status);
    }
    private void Request_approval_show_salary() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<PayrollSalaryHistoryResponse>> call1 = apiInterface.history_salary(year,month1,"All",user_id);
        call1.enqueue(new Callback<List<PayrollSalaryHistoryResponse>>() {
            @Override
            public void onResponse(Call<List<PayrollSalaryHistoryResponse>> call, Response<List<PayrollSalaryHistoryResponse>> response) {
                List<PayrollSalaryHistoryResponse> payrollSalaryHistoryResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (payrollSalaryHistoryResponse != null && payrollSalaryHistoryResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    salary_list = new ArrayList<>();
                    for (int i = 0; i < payrollSalaryHistoryResponse.size(); i++) {
                        res = payrollSalaryHistoryResponse.get(i).getResponse();
                        PayrollSalaryHistoryResponse model=new PayrollSalaryHistoryResponse();
                        if (res.equalsIgnoreCase("data not available")) {
                            waiting_hide.setVisibility(View.GONE);
                            data_not_available.setVisibility(View.VISIBLE);
                            ll_show_data.setVisibility(View.GONE);
                        }
                        else if(res.equalsIgnoreCase("success")){
                            waiting_hide.setVisibility(View.GONE);
                            data_not_available.setVisibility(View.GONE);
                            ll_show_data.setVisibility(View.VISIBLE);

                            String Name=payrollSalaryHistoryResponse.get(i).getEmpName();
                            String net_salary=payrollSalaryHistoryResponse.get(i).getNetSalary();
                            String gross_total=payrollSalaryHistoryResponse.get(i).getGrossTotal();
                            String total_ctc=payrollSalaryHistoryResponse.get(i).getTotalCTC();
                            String Total_deduction=payrollSalaryHistoryResponse.get(i).getTotalDeducted();
                            String voucher=payrollSalaryHistoryResponse.get(i).getVoucher();


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
                    salaryHistoryAdapter = new SalaryHistoryAdapter(salary_list, getActivity());
                    salary_status.setAdapter(salaryHistoryAdapter);
                    salary_status.setLayoutManager(new LinearLayoutManager(getActivity()));

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<PayrollSalaryHistoryResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

  /*  public static List<YearMonth> getMonthsOfCurrentYear() {
        YearMonth currentMonth = YearMonth.now();
        List<YearMonth> yearMonths = new ArrayList<>();

        for (int month = 1; month <= currentMonth.getMonthValue(); month++) {
            yearMonths.add(YearMonth.of(currentMonth.getYear(), month));
        }

        return yearMonths;
    }*/
}