package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ossi.genreporting.Adapter.EventAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.infiniteviewpager.InfiniteViewPager;
import com.ossi.genreporting.model.DetailsResponseItem;
import com.ossi.genreporting.model.EventResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment implements View.OnClickListener {
    TextView total_expense_bill, expense_approved, expense_decline, expense_pending;
    TextView wfm_used_leave, wfm_approved, wfm_decline, wfm_pending;
    TextView loan_status_limit, loan_status_applied, loan_status;
    TextView leave_remaining, leave_status_applied, leave_status;
    String user_id;
    InfiniteViewPager pager;
    String Total_Expense_bill, Expense_Approved, Expense_Decline, Expense_Pending, WFH_Used, WFH_Approved, WFH_Decline, WFH_Pending, Loan_Ammoun, Loan_Status, EL, CL, SL,Remaining_WFH_leave;
   String Emp_Name,Emp_Event;
    View view;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private SharedPreferences.Editor editor;
    int currentPage=0;
    private String apply_leave,aply_leave_status;
    private String prof_img;
    ImageView leave_ll;
    TextView create_expense;
    Button apply_expense_,apply_wfh_direct_,apply_leave_;
    ImageView open_detail_expense;
    ImageView wfh_ll;
    ImageView open_holidaylist;
    RecyclerView event_get_horizon;
    private EventAdapter event_adapter;
    private ArrayList<EventResponse> event_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        find_view_by_id();
        set_on_click_litioner();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);
        prof_img = pref.getString("img_url", null);



/*

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getParentFragmentManager()) {
            int[] colours = new int[]{Color.CYAN, Color.MAGENTA,Color.GREEN,Color.GRAY};

            @Override
            public int getCount() {
                return colours.length;



            }

            @Override
            public Fragment getItem(int position) {
                Fragment fragment = new EventFragment();
                Bundle args = new Bundle();
                args.putInt("colour", colours[position]);
                args.putInt("identifier", position);
                fragment.setArguments(args);
                return fragment;
            }

        };

        // wrap pager to provide a minimum of 4 pages
        MinFragmentPagerAdapter wrappedMinAdapter = new MinFragmentPagerAdapter(getParentFragmentManager());
        wrappedMinAdapter.setAdapter(adapter);

        // wrap pager to provide infinite paging with wrap-around
        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(wrappedMinAdapter);

        // actually an InfiniteViewPager

        pager.setAdapter(wrappedAdapter);*/

        if (isNetworkAvailable()) {
            get_all_detail(user_id);

        } else {
            Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }
        return view;
    }



    public void find_view_by_id() {
        total_expense_bill = view.findViewById(R.id.total_expense_bill);
        expense_approved = view.findViewById(R.id.expense_approved);
        expense_decline = view.findViewById(R.id.expense_decline);
        expense_pending = view.findViewById(R.id.expense_pending);

        wfm_used_leave = view.findViewById(R.id.wfm_used_leave);
        wfm_approved = view.findViewById(R.id.wfm_approved);
        wfm_decline = view.findViewById(R.id.wfm_decline);
        wfm_pending = view.findViewById(R.id.wfm_pending);

        loan_status_limit = view.findViewById(R.id.loan_status_limit);
        loan_status_applied = view.findViewById(R.id.loan_status_applied);
        loan_status = view.findViewById(R.id.loan_status);

        leave_remaining = view.findViewById(R.id.leave_remaining);
        leave_status_applied = view.findViewById(R.id.leave_status_applied);
        leave_status = view.findViewById(R.id.leave_status);

        //pager=view.findViewById(R.id.pager);
        leave_ll=view.findViewById(R.id.leave_ll);

        wfh_ll=view.findViewById(R.id.wfh_ll);
        //img_profile=view.findViewById(R.id.img_profile);

        create_expense=view.findViewById(R.id.create_expense);

        apply_expense_=view.findViewById(R.id.apply_expense_);
        open_detail_expense=view.findViewById(R.id.open_detail_expense);
        apply_wfh_direct_=view.findViewById(R.id.apply_wfh_direct_);
        apply_leave_=view.findViewById(R.id.apply_leave_);
        open_holidaylist=view.findViewById(R.id.open_holidaylist);
        event_get_horizon=view.findViewById(R.id.event_get_horizon);

    }

    public void set_on_click_litioner() {
        create_expense.setOnClickListener(this);
        wfh_ll.setOnClickListener(this);
        leave_ll.setOnClickListener(this);
        expense_approved.setOnClickListener(this);
        expense_pending.setOnClickListener(this);
        expense_decline.setOnClickListener(this);
        apply_expense_.setOnClickListener(this);
        open_detail_expense.setOnClickListener(this);
        apply_wfh_direct_.setOnClickListener(this);
        apply_leave_.setOnClickListener(this);
        open_holidaylist.setOnClickListener(this);
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
                        CL = DetailsResponseItem.get(i).getTotal_CL();
                        EL = DetailsResponseItem.get(i).getAvailable_EL();
                        SL = DetailsResponseItem.get(i).getTotal_SL();
                        Remaining_WFH_leave=DetailsResponseItem.get(i).getRemainingWFHLeave();

                        prof_img=DetailsResponseItem.get(i).getImage();
                        apply_leave=DetailsResponseItem.get(i).getApply();
                        aply_leave_status=DetailsResponseItem.get(i).getStatus();


                        String remaining_leave=" CL: "+CL+" EL: "+EL+" SL: "+SL;

                        expense_approved.setText(Expense_Approved);
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
                        leave_status.setText(aply_leave_status);



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
                        editor.putString("Remaining_WFH_leave",Remaining_WFH_leave);
                        editor.putString("img_url",prof_img);
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
                        EventResponse model=new EventResponse();
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
                Toast.makeText(getActivity(), "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    public boolean isNetworkAvailable() {
        final android.net.ConnectivityManager connectivityManager = ((android.net.ConnectivityManager) getActivity().getSystemService(android.content.Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_expense:

                ExpenseViewFragment expenseViewFragment = new ExpenseViewFragment();
                openFragment(expenseViewFragment);
                break;
            case R.id.wfh_ll:

                WorkFromHomeViewFragment workFromHomeViewFragment = new WorkFromHomeViewFragment();
                openFragment(workFromHomeViewFragment);
                break;

            case R.id.leave_ll:
                LeaveViewFragment leaveViewFragment = new LeaveViewFragment();
                openFragment(leaveViewFragment);
                break;
            case R.id.expense_approved:
                int approve= Integer.parseInt(Expense_Approved);
                if(approve>0){
                    ExpenseViewFragment expenseViewFragment1 = new ExpenseViewFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("expense approve type","Approved");
                    expenseViewFragment1.setArguments(bundle);
                    openFragment(expenseViewFragment1);
                }else {
                    Toast.makeText(getActivity(), "No any Expense Aproved", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.expense_pending:
                int pending= Integer.parseInt(Expense_Pending);
                if(pending>0){
                    ExpenseViewFragment expenseViewFragment1 = new ExpenseViewFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("expense approve type","Pending");
                    expenseViewFragment1.setArguments(bundle);
                    openFragment(expenseViewFragment1);
                }else {
                    Toast.makeText(getActivity(), "No any Expense Pending", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.expense_decline:
                int decline= Integer.parseInt(Expense_Decline);
                if(decline>0){
                    ExpenseViewFragment expenseViewFragment1 = new ExpenseViewFragment();
                    Bundle bundle=new Bundle();
                    bundle.putString("expense approve type","Rejected");
                    expenseViewFragment1.setArguments(bundle);
                    openFragment(expenseViewFragment1);
                }else {
                    Toast.makeText(getActivity(), "No any Expense Decline", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.apply_expense_:
                ApplyExpenseFragment applyExpenseFragment = new ApplyExpenseFragment();
                openFragment(applyExpenseFragment);

                break;

            case R.id.open_detail_expense:

                ExpenseViewFragment expenseViewFragment3 = new ExpenseViewFragment();
                openFragment(expenseViewFragment3);
                break;
            case R.id.apply_wfh_direct_:
                ApplyWorkFromHomeFragment applyWorkFromHomeFragment = new ApplyWorkFromHomeFragment();
                openFragment(applyWorkFromHomeFragment);
                break;

            case R.id.apply_leave_:

                ApplyLeaveFragment applyLeaveFragment = new ApplyLeaveFragment();
                openFragment(applyLeaveFragment);
                break;

            case R.id.open_holidaylist:
                HolidayFragment holidayFragment = new HolidayFragment();
                openFragment(holidayFragment);
                break;
        }

    }
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }



}