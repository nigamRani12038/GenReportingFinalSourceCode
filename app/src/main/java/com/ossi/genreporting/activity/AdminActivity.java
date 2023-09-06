package com.ossi.genreporting.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amrdeveloper.lottiedialog.LottieDialog;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.ossi.genreporting.LoginActivity;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.fragment.AddShowEventFragment;
import com.ossi.genreporting.fragment.AddShowMeeting;
import com.ossi.genreporting.fragment.AdminTabLayoutFragment;
import com.ossi.genreporting.fragment.ApplyExpenseFragment;
import com.ossi.genreporting.fragment.ApplyLeaveFragment;
import com.ossi.genreporting.fragment.ApplyWorkFromHomeFragment;
import com.ossi.genreporting.fragment.ApprovedRequestHistoryFragment;
import com.ossi.genreporting.fragment.CurrentMeetingFragment;
import com.ossi.genreporting.fragment.EmployeeRequestFragment;
import com.ossi.genreporting.fragment.ExpenseViewFragment;
import com.ossi.genreporting.fragment.LeaveViewFragment;
import com.ossi.genreporting.fragment.MyProfileFragment;
import com.ossi.genreporting.fragment.PayrollFragment;
import com.ossi.genreporting.fragment.SalaryFragment;
import com.ossi.genreporting.fragment.SalaryHistoryFragment;
import com.ossi.genreporting.fragment.TabLayoutFragment;
import com.ossi.genreporting.fragment.WorkFromHomeViewFragment;
import com.ossi.genreporting.model.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    TextView text_header1, employee_name, text_for_select;
    TextView login_time;
    RoundedImageView img_profile;
    String login_id, user_id;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String res;
    private ImageView img_slide_gen;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences pref;
    private NavigationView navigationView;
    private TextView dashBoard, setting, my_profile, salary;
    TextView hr_events, meetings, payroll;
    private BottomNavigationView bottomNavigationView;
    FloatingActionButton add_fab, apply_leve, apply_wfm_float, expense, aply_loan;
    Boolean isAllFabsVisible;
    private String img_profile1;
    TextView apply_expense, apply_leave, apply_wfh;
    TextView statusLeave,statusWfh,statusExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        find_view_by_id();
        set_on_click();
        if (savedInstanceState == null) {
            AdminTabLayoutFragment tabLayoutFragment = new AdminTabLayoutFragment();
            openFragment1Tab(tabLayoutFragment);
        }


        pref = getSharedPreferences("my_pref", MODE_PRIVATE);

        String restoredText = pref.getString("status_check", null);
        if (restoredText != null) {
            String check = pref.getString("status_check", " ");//"No name defined" is the default value.
            String login_Time = pref.getString("login_time", " ");
            String Employee_Name = pref.getString("User_name", " ");
            login_id = pref.getString("login_id", " ");
            user_id = pref.getString("user_id", " ");
            img_profile1 = pref.getString("img_url", " ");
            if (img_profile1 != null) {
                Glide.with(this).load(img_profile1).into(img_profile);
            }

            employee_name.setText(Employee_Name);
            login_time.setText("Login Time: " + login_Time);
            text_header1.setText("Dashboard");
            text_for_select.setText("");


        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        pref = getSharedPreferences("my_pref", MODE_PRIVATE);
        String img_profile1 = pref.getString("img_url", " ");

        if (img_profile1 != null) {
            Glide.with(this).load(img_profile1).into(img_profile);
        }
    }

    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment, ""); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }

    private void openFragment1Tab(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment, "Tablayout"); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }

    public void find_view_by_id() {
        img_slide_gen = (ImageView) findViewById(R.id.img_slide_gen);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        dashBoard = findViewById(R.id.dashBoard);

        setting = findViewById(R.id.setting);
        text_header1 = findViewById(R.id.text_header1);
        employee_name = findViewById(R.id.employee_name);
        login_time = findViewById(R.id.login_time);
        my_profile = findViewById(R.id.my_profile);
        //info=findViewById(R.id.info);
        img_profile = findViewById(R.id.img_profile);
        text_for_select = findViewById(R.id.text_for_select);
        meetings = findViewById(R.id.meetings);
        hr_events = findViewById(R.id.hr_events);
        salary = findViewById(R.id.salary);
        payroll = findViewById(R.id.payroll);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        add_fab = findViewById(R.id.add_fab);
        apply_leve = findViewById(R.id.apply_leve);
        apply_wfm_float = findViewById(R.id.apply_wfm_float);
        expense = findViewById(R.id.expense);
        aply_loan = findViewById(R.id.aply_loan);
        //apply_expense

        apply_wfh = findViewById(R.id.apply_wfh);
        apply_leave = findViewById(R.id.apply_leave);
        apply_expense = findViewById(R.id.apply_expense);

        statusLeave = findViewById(R.id.statusLeave);
        statusWfh = findViewById(R.id.statusWfh);
        statusExpense = findViewById(R.id.statusExpense);

        apply_leve.setVisibility(View.GONE);
        apply_wfm_float.setVisibility(View.GONE);
        expense.setVisibility(View.GONE);
        aply_loan.setVisibility(View.GONE);

        isAllFabsVisible = false;

    }

    public void set_on_click() {
        img_slide_gen.setOnClickListener(this);
        dashBoard.setOnClickListener(this);
        setting.setOnClickListener(this);
        my_profile.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        meetings.setOnClickListener(this);
        hr_events.setOnClickListener(this);
        salary.setOnClickListener(this);
        payroll.setOnClickListener(this);

        add_fab.setOnClickListener(this);
        apply_leve.setOnClickListener(this);
        apply_wfm_float.setOnClickListener(this);
        expense.setOnClickListener(this);
        aply_loan.setOnClickListener(this);

        apply_leave.setOnClickListener(this);
        apply_expense.setOnClickListener(this);
        apply_wfh.setOnClickListener(this);

        statusLeave.setOnClickListener(this);
        statusExpense.setOnClickListener(this);
        statusWfh.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_slide_gen:
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.dashBoard:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("DashBoard");
                AdminTabLayoutFragment tabLayoutFragment = new AdminTabLayoutFragment();
                openFragment1Tab(tabLayoutFragment);
                break;
            case R.id.setting:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                get_logout_method();
                break;
            case R.id.my_profile:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                openFragment1(myProfileFragment);
                break;

            case R.id.img_profile:
                MyProfileFragment myProfileFragment1 = new MyProfileFragment();
                openFragment1(myProfileFragment1);
                break;

            case R.id.meetings:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Meetings");
                AddShowMeeting add_Show_Meeting = new AddShowMeeting();
                openFragment1(add_Show_Meeting);
                break;
            case R.id.hr_events:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Events");
                AddShowEventFragment add_Show_Event_Fragment = new AddShowEventFragment();
                openFragment1(add_Show_Event_Fragment);
                break;
            case R.id.salary:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Salary Slip");
                SalaryFragment salaryFragment = new SalaryFragment();
                openFragment1(salaryFragment);
                break;

            case R.id.payroll:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Payroll System");
                PayrollFragment payrollfragment = new PayrollFragment();
                openFragment1(payrollfragment);
                break;
            case R.id.apply_leave:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Apply Leave");
                ApplyLeaveFragment applyLeaveFragment = new ApplyLeaveFragment();
                openFragment1(applyLeaveFragment);
                break;
            case R.id.apply_expense:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Apply Expense");
                ApplyExpenseFragment applyExpenseFragment = new ApplyExpenseFragment();
                openFragment1(applyExpenseFragment);
                break;
            case R.id.apply_wfh:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Apply WFH");
                ApplyWorkFromHomeFragment applyWorkFromHomeFragment = new ApplyWorkFromHomeFragment();
                openFragment1(applyWorkFromHomeFragment);
                break;

            case R.id.statusLeave:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Leave Status");
                LeaveViewFragment leaveViewFragment = new LeaveViewFragment();
                openFragment1(leaveViewFragment);
                break;

            case R.id.statusWfh:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("WFH Status");
                WorkFromHomeViewFragment workFromHomeViewFragment = new WorkFromHomeViewFragment();
                openFragment1(workFromHomeViewFragment);
                break;

            case R.id.statusExpense:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Expense Status");
                ExpenseViewFragment expenseViewFragment = new ExpenseViewFragment();
                openFragment1(expenseViewFragment);
                break;


            case R.id.add_fab:
                if (!isAllFabsVisible) {

                    apply_leve.show();
                    apply_wfm_float.show();
                    expense.show();
                    aply_loan.show();

                    isAllFabsVisible = true;

                    add_fab.setImageResource(R.drawable.newcancel);
                } else {

                    apply_leve.hide();
                    apply_wfm_float.hide();
                    expense.hide();
                    aply_loan.hide();
                    isAllFabsVisible = false;
                    add_fab.setImageResource(R.drawable.newadd);
                }
                break;

            case R.id.apply_leve:

                text_header1.setText("Request History");
                ApprovedRequestHistoryFragment approvedRequestHistoryFragment = new ApprovedRequestHistoryFragment();
                openFragment(approvedRequestHistoryFragment);
                if (isAllFabsVisible) {
                    apply_leve.hide();
                    apply_wfm_float.hide();
                    expense.hide();
                    aply_loan.hide();
                    add_fab.setImageResource(R.drawable.newadd);
                }

                break;
            case R.id.apply_wfm_float:
//Approval Payrol
                text_header1.setText("Payroll System");
                PayrollFragment payrollFragment = new PayrollFragment();
                openFragment(payrollFragment);


                if (isAllFabsVisible) {
                    apply_leve.hide();
                    apply_wfm_float.hide();
                    expense.hide();
                    aply_loan.hide();
                    add_fab.setImageResource(R.drawable.newadd);
                }
                break;
            case R.id.expense:
                text_header1.setText("Payroll History");
                SalaryHistoryFragment salaryHistoryFragment = new SalaryHistoryFragment();
                openFragment(salaryHistoryFragment);

                if (isAllFabsVisible) {
                    apply_leve.hide();
                    apply_wfm_float.hide();
                    expense.hide();
                    aply_loan.hide();
                    add_fab.setImageResource(R.drawable.newadd);
                }
                break;
            case R.id.aply_loan://my task
                if (isAllFabsVisible) {
                    apply_leve.hide();
                    apply_wfm_float.hide();
                    expense.hide();
                    aply_loan.hide();
                    add_fab.setImageResource(R.drawable.newadd);
                }

                Toast toast2 = Toast.makeText(AdminActivity.this, "Under construction Recruitment", Toast.LENGTH_LONG);
                toast2.setGravity(Gravity.CENTER, 0, 0);
                toast2.show();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {

            mDrawerLayout.closeDrawer(Gravity.RIGHT);

            Button button1 = new Button(this);
            button1.setText("No");
            button1.setTextColor(Color.WHITE);
            button1.setAllCaps(false);

            Button button = new Button(this);
            button.setText("Yes");
            button.setTextColor(Color.WHITE);
            button.setAllCaps(false);
            int greenColor = ContextCompat.getColor(this, R.color.txtcolor);
            button.setBackgroundTintList(ColorStateList.valueOf(greenColor));

            button1.setBackgroundTintList(ColorStateList.valueOf(greenColor));

            LottieDialog dialog = new LottieDialog(this)
                    .setAnimation(R.raw.exitalert)
                    .setAnimationRepeatCount(LottieDialog.INFINITE)
                    .setAutoPlayAnimation(true)
                    .setDialogBackground(Color.WHITE)
                    .setMessage("Are you sure you want to Exit App?")
                    .setMessageColor(greenColor)
                    .setMessageTextSize(15)
                    .setAnimationViewHeight(170)
                    .setAnimationViewWidth(170)
                    .addActionButton(button)
                    .addActionButton(button1);


            dialog.show();
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });


        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }
    }

    public void get_logout_method() {

        Button button1 = new Button(this);
        button1.setText("No");
        button1.setTextColor(Color.WHITE);
        button1.setAllCaps(false);

        Button button = new Button(this);
        button.setText("Yes");
        button.setTextColor(Color.WHITE);
        button.setAllCaps(false);
        int greenColor = ContextCompat.getColor(this, R.color.txtcolor);
        button.setBackgroundTintList(ColorStateList.valueOf(greenColor));

        button1.setBackgroundTintList(ColorStateList.valueOf(greenColor));

        LottieDialog dialog = new LottieDialog(this)
                .setAnimation(R.raw.logout)
                .setAnimationRepeatCount(LottieDialog.INFINITE)
                .setAutoPlayAnimation(true)
                .setDialogBackground(Color.WHITE)
                .setMessage("Are you sure you want to Logout?")
                .setMessageColor(greenColor)
                .setMessageTextSize(15)
                .setAnimationViewHeight(140)
                .setAnimationViewWidth(140)
                .addActionButton(button)
                .addActionButton(button1);


        dialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutMethod();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



    }

    private void logoutMethod() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<LoginResponse>> call1 = apiInterface.get_Logout_report(user_id, login_id);
        call1.enqueue(new Callback<List<LoginResponse>>() {
            @Override
            public void onResponse(Call<List<LoginResponse>> call, Response<List<LoginResponse>> response) {
                List<LoginResponse> loginResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                Log.e("login", "loginResponse 1 --> " + loginResponse);

                if (loginResponse != null && loginResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < loginResponse.size(); i++) {
                        res = loginResponse.get(i).getResponse();

                    }
                    //  Log.e("status", status);
                    if (res.equalsIgnoreCase("success")) {

                        pref = getSharedPreferences("my_pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        Intent in3 = new Intent(AdminActivity.this, LoginActivity.class);
                        in3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        in3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in3);
                        finish();
                    } else {
                        // Toast.makeText(AdminActivity.this, "Logout Failed", Toast.LENGTH_SHORT).show();
                        Toast toast2 = Toast.makeText(AdminActivity.this, "Logout Failed", Toast.LENGTH_LONG);
                        toast2.setGravity(Gravity.CENTER, 0, 0);
                        toast2.show();
                    }
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<LoginResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_home:
                //Toast.makeText(this,"Clicked",Toast.LENGTH_LONG).show();
                text_header1.setText("DashBoard");
                AdminTabLayoutFragment tabLayoutFragment = new AdminTabLayoutFragment();
                openFragment1Tab(tabLayoutFragment);
                return true;

            case R.id.bottom_note:


                text_header1.setText("Meetings");
                CurrentMeetingFragment add_Show_Meeting = new CurrentMeetingFragment();
                openFragment(add_Show_Meeting);

                return true;

            case R.id.bottom_profile:
                text_header1.setText("Employee Request");
                EmployeeRequestFragment employeeRequestFragment = new EmployeeRequestFragment();
                openFragment(employeeRequestFragment);
                return true;
            case R.id.bottom_setting:
                get_logout_method();

                return true;
        }
        return false;
    }

}