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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.ossi.genreporting.Adapter.ExpenseViewAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.ExpenseViewResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExpenseViewFragment extends Fragment implements View.OnClickListener {
   // Button apply_expense_;
    Spinner select_expense;
    String Expense_type;
    View view;
    RecyclerView expense_rv;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    ExpenseViewResponse model;
    private ExpenseViewAdapter expense_adapter;
    private ArrayList<ExpenseViewResponse> Expense_list;
    String user_id;
    String approve_type_exp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_expense_view, container, false);
        find_view_by_id(view);
        set_on_click_itioner();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);
        user_id = pref.getString("user_id", null);


        Bundle bundle = getArguments();
        if (bundle != null) {
             approve_type_exp = bundle.getString("expense approve type");
            //Toast.makeText(getActivity(), ""+approve_type_exp, Toast.LENGTH_SHORT).show();
            if(Util.isNetworkAvailable(getActivity())) {
                Get_Expense_data_(user_id, approve_type_exp);
            }else {
                Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
            }
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(approve_type_exp);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            select_expense.setAdapter(arrayAdapter);


        }else {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("Select Expense Type");
            arrayList.add("Approved");
            arrayList.add("Pending");
            arrayList.add("Rejected");

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            select_expense.setAdapter(arrayAdapter);
            select_expense.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Expense_type = parent.getItemAtPosition(position).toString();
                    String str = select_expense.getSelectedItem().toString();
                    if (select_expense.getSelectedItem().equals("Select Expense Type")) {
                        Toast.makeText(getActivity(), "Please Select Expense Type", Toast.LENGTH_SHORT).show();
                    } else {
                      if (Util.isNetworkAvailable(getActivity())) {
                          Get_Expense_data_(user_id, str);
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
        //apply_expense_ = view.findViewById(R.id.apply_expense_);
        select_expense = view.findViewById(R.id.select_expense);
        expense_rv = view.findViewById(R.id.expense_rv);
    }

    public void set_on_click_itioner() {
        //apply_expense_.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    private void Get_Expense_data_(String userid, String status) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ExpenseViewResponse>> call1 = apiInterface.Get_Expenses_submit(userid, status);
        call1.enqueue(new Callback<List<ExpenseViewResponse>>() {
            @Override
            public void onResponse(Call<List<ExpenseViewResponse>> call, Response<List<ExpenseViewResponse>> response) {
                List<ExpenseViewResponse> ExpenseViewResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (ExpenseViewResponse != null && ExpenseViewResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    Expense_list = new ArrayList<>();
                    for (int i = 0; i < ExpenseViewResponse.size(); i++) {
                        String res = ExpenseViewResponse.get(i).getResponse();
                        model = new ExpenseViewResponse();
                      /*  if( Expense_list.size()>0){
                            Expense_list.clear();
                        }*/
                        if (res.equalsIgnoreCase("success")) {

                            String ammount = ExpenseViewResponse.get(i).getAmount();
                            String date = ExpenseViewResponse.get(i).getDBTime();
                            String reason = ExpenseViewResponse.get(i).getPurpose();


                            model.setRegin(reason);
                            model.setAmount(ammount);
                            model.setDBTime(date);


                            Expense_list.add(model);


                        } else if (res.equalsIgnoreCase("Fail")) {
                            Toast.makeText(getActivity(), "No Any Expense", Toast.LENGTH_SHORT).show();
                        }


                    }

                    expense_adapter = new ExpenseViewAdapter(Expense_list, getActivity());
                    expense_rv.setAdapter(expense_adapter);
                    expense_rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<ExpenseViewResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


}