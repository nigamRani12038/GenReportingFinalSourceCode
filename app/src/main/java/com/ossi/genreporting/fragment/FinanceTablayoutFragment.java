package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ossi.genreporting.Adapter.EventAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.DetailsResponseItem;
import com.ossi.genreporting.model.EventResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FinanceTablayoutFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private TabLayout fragmentTabLayout;
    private ViewPager2 tabPager1;
    String Leave_Approved, Leave_Decline, Leave_Pending;
    private String user_id, prof_img;
    ImageView open_holidaylist;
    RecyclerView event_get_horizon;
    private EventAdapter event_adapter;
    private ArrayList<EventResponse> event_list;
    String Emp_Name, Emp_Event;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    String Total_Expense_bill, Expense_Approved, Expense_Decline, Expense_Pending, WFH_Used, WFH_Approved, WFH_Decline, WFH_Pending, Loan_Ammoun, Loan_Status, EL, CL, SL, Remaining_WFH_leave;
    private SharedPreferences.Editor editor;
    private String apply_leave, aply_leave_status;
    String total_sl,total_cl,total_el,total_all;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_finance_tablayout, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);
        prof_img = pref.getString("img_url", null);
        find_view_by_id();
        setOnClick();

        if (isNetworkAvailable()) {
            get_all_detail(user_id);
            //get_event();
        } else {
            Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return mView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        // fragmentTabLayout.addTab(fragmentTabLayout.newTab().setText("Projects"));
        fragmentTabLayout.addTab(fragmentTabLayout.newTab().setText("Leaves"));
        fragmentTabLayout.addTab(fragmentTabLayout.newTab().setText("WFH"));
        fragmentTabLayout.addTab(fragmentTabLayout.newTab().setText("Expenses"));

       PagerAdapter pagerAdapter = new PagerAdapter(getActivity(), fragmentTabLayout.getTabCount());
        tabPager1.setAdapter(pagerAdapter);
        //   tabPager1.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(fragmentTabLayout));



        new TabLayoutMediator(fragmentTabLayout, tabPager1, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Leaves");
                    break;
                case 1:
                    tab.setText("WFH");
                    break;
                case 2:
                    tab.setText("Expenses");
                    break;

            }
        }).attach();

        fragmentTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPager1.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        // tabPager1.setCurrentItem(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_holidaylist:
                HolidayFragment holidayFragment = new HolidayFragment();
                openFragment1(holidayFragment);
                break;
        }
    }

    public class PagerAdapter extends FragmentStateAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentActivity fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }


        @Override
        public Fragment createFragment(int position) {

            switch (position) {
               /* case 0:
                    MyTaskFragment myTaskFragment = new MyTaskFragment();
                    return myTaskFragment;*/

                case 0:
                    LeaveFragmentHome leaveFragmentHome = new LeaveFragmentHome();
                    return leaveFragmentHome;
                case 1:
                    WorkFromHome workFromHome = new WorkFromHome();
                    return workFromHome;
                case 2:
                    ExpenseFragment expenseFragment = new ExpenseFragment();
                    return expenseFragment;
               /* case 3:
                    LoanFragment loanFragment = new LoanFragment();
                    return loanFragment;*/

                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return mNumOfTabs;
        }
    }

    public void find_view_by_id() {
        fragmentTabLayout = mView.findViewById(R.id.fragmentTabLayout);
        tabPager1 = mView.findViewById(R.id.tabPager);
        open_holidaylist = mView.findViewById(R.id.open_holidaylist);
        event_get_horizon = mView.findViewById(R.id.event_get_horizon);

    }

    public void setOnClick() {
        open_holidaylist.setOnClickListener(this);

    }

    private void get_event() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<EventResponse>> call1 = apiInterface.get_event_details();
        call1.enqueue(new Callback<List<EventResponse>>() {
            @Override
            public void onResponse(Call<List<EventResponse>> call, Response<List<EventResponse>> response) {
                List<EventResponse> EventResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (EventResponseItem != null && EventResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    event_list = new ArrayList<>();
                    for (int i = 0; i < EventResponseItem.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();

                        Emp_Name = EventResponseItem.get(i).getEmpName();
                        Emp_Event = EventResponseItem.get(i).getEvent();
                        EventResponse model = new EventResponse();
                        model.setEmpName(Emp_Name);
                        model.setEvent(Emp_Event);

                        event_list.add(model);

                    }
                    event_adapter = new EventAdapter(event_list, getActivity());
                    event_get_horizon.setAdapter(event_adapter);
                    event_get_horizon.setLayoutManager(new LinearLayoutManager(getActivity(),
                            LinearLayoutManager.HORIZONTAL,
                            false));


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<EventResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), " Please Try again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
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
                        Remaining_WFH_leave = DetailsResponseItem.get(i).getRemainingWFHLeave();

                        prof_img = DetailsResponseItem.get(i).getImage();
                        apply_leave = DetailsResponseItem.get(i).getApply();
                        aply_leave_status = DetailsResponseItem.get(i).getStatus();
                        Leave_Approved=DetailsResponseItem.get(i).getApprovedLeave();
                        Leave_Decline=DetailsResponseItem.get(i).getRejectLeave();
                        Leave_Pending=DetailsResponseItem.get(i).getPendingLeave();
                        total_cl = DetailsResponseItem.get(i).getTotal_CL();
                        total_el=DetailsResponseItem.get(i).getTotal_EL();
                        total_sl=DetailsResponseItem.get(i).getTotal_SL();

                        total_all=" CL: "+total_cl+" EL: "+total_el+" SL: "+total_sl;


                        String remaining_leave = " CL: " + CL + " EL: " + EL + " SL: " + SL;

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
                        leave_remaining.setText(remaining_leave);
                        leave_status_applied.setText(apply_leave);
                        leave_status.setText(aply_leave_status);*/


                        editor = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE).edit();
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
                        editor.putString("Remaining_WFH_leave", Remaining_WFH_leave);
                        editor.putString("img_url", prof_img);
                        editor.putString("remaining_leave", remaining_leave);
                        editor.putString("apply_leave", apply_leave);
                        editor.putString("aply_leave_status", aply_leave_status);
                        editor.putString("Leave_Approved", Leave_Approved);
                        editor.putString("Leave_Decline", Leave_Decline);
                        editor.putString("Leave_Pending", Leave_Pending);
                        editor.putString("Total_Leave",total_all);
                        editor.apply();

                    }

                    get_event();
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<DetailsResponseItem>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        //transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

    public boolean isNetworkAvailable() {
        final android.net.ConnectivityManager connectivityManager = ((android.net.ConnectivityManager) getActivity().getSystemService(android.content.Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }

   /* public void onBackPressed() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
            getActivity().finish();
        } else {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        }
    }*/
}
