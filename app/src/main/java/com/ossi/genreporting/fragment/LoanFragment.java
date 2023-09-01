package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ossi.genreporting.R;

public class LoanFragment extends Fragment implements View.OnClickListener {
View view;
TextView loan_status_limit,loan_status_applied,loan_status;
String user_id,Loan_Ammoun,Loan_Status;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_loan, container, false);
        find_view_by_id();
        set_on_click();

        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);
        user_id = pref.getString("user_id", null);
        Loan_Ammoun=pref.getString("Loan_Ammoun","");
        Loan_Status=pref.getString("Loan_Status","");


        loan_status_limit.setText(Loan_Ammoun);
        loan_status.setText(Loan_Status);

        return view;
    }

    public void find_view_by_id(){
        loan_status_limit = view.findViewById(R.id.loan_status_limit);
        loan_status_applied = view.findViewById(R.id.loan_status_applied);
        loan_status = view.findViewById(R.id.loan_status);
    }

    public void set_on_click(){

    }

    @Override
    public void onClick(View v) {

    }
}