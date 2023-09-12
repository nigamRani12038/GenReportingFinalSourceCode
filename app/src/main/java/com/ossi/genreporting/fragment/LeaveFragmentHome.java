package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.DetailsResponseItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveFragmentHome extends Fragment implements View.OnClickListener {
    View view;
    String user_id,remaining_leave,apply_leave,aply_leave_status;
    String Leave_Approved,Leave_Decline,Leave_Pending;
   // TextView leave_remaining, leave_status_applied, leave_status;
    TextView leave_remaining;
    ImageView leave_ll;
    private APIInterface apiInterface;
    private SharedPreferences.Editor editor;
    String Total_Expense_bill, Expense_Approved, Expense_Decline, Expense_Pending, WFH_Used, WFH_Approved, WFH_Decline, WFH_Pending, Loan_Ammoun, Loan_Status, EL, CL, SL,Remaining_WFH_leave;

    private ProgressDialog mProgressDialog;
    private String prof_img;
    public static final String ARG_PAGE = "ARG_PAGE";
    String total_sl,total_cl,total_el,total_all;
    private int mPage;
    TextView leave_approved,leave_rejected,leave_pending,total_lev;
    LinearLayout leave_approved_ll,leave_rejected_ll,leave_pending_ll;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_leave_home, container, false);

        editor = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE).edit();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);
        remaining_leave=pref.getString("remaining_leave","");
        apply_leave=pref.getString("apply_leave","");
        aply_leave_status=pref.getString("aply_leave_status","");
        Leave_Approved=pref.getString("Leave_Approved","");
        Leave_Decline=pref.getString("Leave_Decline","");
        Leave_Pending=pref.getString("Leave_Pending","");


        find_view_by_id();
        set_on_click_litioner();

         if (!remaining_leave.equalsIgnoreCase("")) {
             leave_remaining.setText(remaining_leave);
             leave_rejected.setText(Leave_Decline);
             leave_approved.setText(Leave_Approved);
             leave_pending.setText(Leave_Pending);
         }

        if (Util.isNetworkAvailable(getActivity())) {
            get_all_detail(user_id);

        } else {
            Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    public void find_view_by_id() {
        leave_remaining=view.findViewById(R.id.leave_remaining);
       // leave_status_applied=view.findViewById(R.id.leave_status_applied);
       // leave_status=view.findViewById(R.id.leave_status);
        leave_ll=view.findViewById(R.id.leave_ll);
        //leave_approved_ll,leave_rejected_ll,leave_pending_ll;
        leave_approved=view.findViewById(R.id.leave_approved);
        leave_rejected=view.findViewById(R.id.leave_rejected);
        leave_pending=view.findViewById(R.id.leave_pending);
        leave_approved_ll=view.findViewById(R.id.leave_approved_ll);
        leave_rejected_ll=view.findViewById(R.id.leave_rejected_ll);
        leave_pending_ll=view.findViewById(R.id.leave_pending_ll);
        total_lev=view.findViewById(R.id.total_lev);


    }

    public void set_on_click_litioner() {
        leave_ll.setOnClickListener(this);
        leave_pending_ll.setOnClickListener(this);
        leave_rejected_ll.setOnClickListener(this);
        leave_approved_ll.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.leave_ll:

                LeaveViewFragment leaveViewFragment = new LeaveViewFragment();
                openFragment(leaveViewFragment);
                break;

            case R.id.leave_approved_ll:
                int approve= Integer.parseInt(Leave_Approved);
                if(approve>0){
                    LeaveViewFragment leaveviewFragment = new LeaveViewFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("leave approve type","Approved");
                    leaveviewFragment.setArguments(bundle);
                    openFragment(leaveviewFragment);
                }else {
                    Toast.makeText(getActivity(), "No any leave Aproved", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.leave_rejected_ll:
                int decline= Integer.parseInt(Leave_Decline);
                if(decline>0){
                    LeaveViewFragment leaveviewFragment = new LeaveViewFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("leave approve type","Decline");
                    leaveviewFragment.setArguments(bundle);
                    openFragment(leaveviewFragment);
                }else {
                    Toast.makeText(getActivity(), "No any leave Rejeccted", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.leave_pending_ll:
                int pending= Integer.parseInt(Leave_Pending);
                if(pending>0){
                    LeaveViewFragment leaveviewFragment = new LeaveViewFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("leave approve type","Pending");
                    leaveviewFragment.setArguments(bundle);
                    openFragment(leaveviewFragment);
                }else {
                    Toast.makeText(getActivity(), "No any leave Pending", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    private void get_all_detail(String userid) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<DetailsResponseItem>> call1 = apiInterface.get_all_detail(userid);
        call1.enqueue(new Callback<List<DetailsResponseItem>>() {
            @Override
            public void onResponse(Call<List<DetailsResponseItem>> call, Response<List<DetailsResponseItem>> response) {
                List<DetailsResponseItem> DetailsResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (DetailsResponseItem != null && DetailsResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < DetailsResponseItem.size(); i++) {
                        Expense_Approved = DetailsResponseItem.get(i).getApprovedExpenses();
                        Expense_Pending = DetailsResponseItem.get(i).getPendingExpenses();
                        Expense_Decline = DetailsResponseItem.get(i).getRejectExpenses();
                        Total_Expense_bill = DetailsResponseItem.get(i).getTotalExpenses();
                        WFH_Approved = DetailsResponseItem.get(i).getApprovedWFH();
                        WFH_Pending = DetailsResponseItem.get(i).getPendingWFH();
                        WFH_Decline = DetailsResponseItem.get(i).getRejectWFH();
                        WFH_Used = DetailsResponseItem.get(i).getUsedWFH();
                        Loan_Ammoun = DetailsResponseItem.get(i).getLoanAmount();
                        Loan_Status = DetailsResponseItem.get(i).getLoanStatus();
                        CL = DetailsResponseItem.get(i).getAvailable_CL();
                        EL = DetailsResponseItem.get(i).getAvailable_EL();
                        SL = DetailsResponseItem.get(i).getAvailable_SL();
                        Remaining_WFH_leave=DetailsResponseItem.get(i).getRemainingWFHLeave();

                        prof_img=DetailsResponseItem.get(i).getImage();
                        apply_leave=DetailsResponseItem.get(i).getApply();
                        aply_leave_status=DetailsResponseItem.get(i).getStatus();

                        Leave_Approved=DetailsResponseItem.get(i).getApprovedLeave();
                        Leave_Decline=DetailsResponseItem.get(i).getRejectLeave();
                        Leave_Pending=DetailsResponseItem.get(i).getPendingLeave();

                        total_cl = DetailsResponseItem.get(i).getTotal_CL();
                        total_el=DetailsResponseItem.get(i).getTotal_EL();
                        total_sl=DetailsResponseItem.get(i).getTotal_SL();

                        total_all=" CL: "+total_cl+" EL: "+total_el+" SL: "+total_sl;


                        String remaining_leave=" CL: "+CL+" EL: "+EL+" SL: "+SL;
                        leave_remaining.setText(remaining_leave);
                        leave_rejected.setText(Leave_Decline);
                        leave_approved.setText(Leave_Approved);
                        leave_pending.setText(Leave_Pending);
                        total_lev.setText(total_all);
                       // leave_status.setText(aply_leave_status);
                      /*  expense_approved.setText(Expense_Approved);
                        expense_decline.setText(Expense_Decline);
                        expense_pending.setText(Expense_Pending);
                        total_expense_bill.setText(Total_Expense_bill);
                        wfm_approved.setText(WFH_Approved);
                        wfm_decline.setText(WFH_Decline);
                        wfm_pending.setText(WFH_Pending);
                        wfm_used_leave.setText(WFH_Used);
                        loan_status_limit.setText(Loan_Ammoun);
                        loan_status.setText(Loan_Status);

                        leave_status_applied.setText(apply_leave);
                        leave_status.setText(aply_leave_status);*/



                        editor.putString("Expense_Approved", Expense_Approved);
                        editor.putString("Expense_Decline", Expense_Decline);
                        editor.putString("Expense_Pending", Expense_Pending);
                        editor.putString("Total_Expense_bill", Total_Expense_bill);
                        editor.putString("WFH_Approved", WFH_Approved);
                        editor.putString("WFH_Decline", WFH_Decline);
                        editor.putString("WFH_Pending", WFH_Pending);
                        editor.putString("WFH_Used", WFH_Used);
                        editor.putString("Loan_Ammoun", Loan_Ammoun);
                        editor.putString("Loan_Status", Loan_Status);
                        editor.putString("EL", EL);
                        editor.putString("CL", CL);
                        editor.putString("SL", SL);
                        editor.putString("SL", SL);
                        editor.putString("Remaining_WFH_leave",Remaining_WFH_leave);
                        editor.putString("img_url",prof_img);
                        editor.putString("remaining_leave",remaining_leave);
                        editor.putString("Leave_Approved", Leave_Approved);
                        editor.putString("Leave_Decline", Leave_Decline);
                        editor.putString("Leave_Pending", Leave_Pending);
                        editor.putString("Total_Leave",total_all);
                       // editor.putString("aply_leave_status",aply_leave_status);
                        editor.apply();

                    }

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<DetailsResponseItem>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
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