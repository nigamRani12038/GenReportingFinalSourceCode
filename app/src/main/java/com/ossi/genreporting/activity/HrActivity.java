package com.ossi.genreporting.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
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
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.ossi.genreporting.LoginActivity;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.fragment.AddShowEventFragment;
import com.ossi.genreporting.fragment.AddShowMeeting;
import com.ossi.genreporting.fragment.ApplyExpenseFragment;
import com.ossi.genreporting.fragment.ApplyLeaveFragment;
import com.ossi.genreporting.fragment.ApplyWorkFromHomeFragment;
import com.ossi.genreporting.fragment.ApprovedRequestHistoryFragment;
import com.ossi.genreporting.fragment.CurrentMeetingFragment;
import com.ossi.genreporting.fragment.EmployeeRequestFragment;
import com.ossi.genreporting.fragment.HrTablayoutFragment;
import com.ossi.genreporting.fragment.InfoFragment;
import com.ossi.genreporting.fragment.MyProfileFragment;
import com.ossi.genreporting.fragment.SalaryFragment;
import com.ossi.genreporting.fragment.ViewAllCommingRequestAdmin;
import com.ossi.genreporting.model.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@TargetApi(Build.VERSION_CODES.DONUT)
public class HrActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private ImageView img_slide_gen;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences pref;
    private NavigationView navigationView;
    private TextView dashBoard, applyLeave, workhome, expense_frag, setting, my_profile, info,salary;
    TextView hr_events, meetings;
    TextView employee_name;
    TextView login_time;
    RoundedImageView img_profile;
    String login_id, user_id;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String res;
    private BottomNavigationView bottomNavigationView;
    FloatingActionButton add_fab, apply_leve, apply_wfm_float, expense, aply_loan;
    Boolean isAllFabsVisible;
    TextView text_header1, text_for_select;
    TextView request_show_hr,request_history_hr;
    private SharedPreferences.Editor editor;
    String Current_weekly_hour,Login_time_biometric,previous_weekly_hour;
    String login_Time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr);
        find_view_by_id();
        set_on_click();

        if (savedInstanceState == null) {

            HrTablayoutFragment tabLayoutFragment = new HrTablayoutFragment();
            openFragment1Tab(tabLayoutFragment);
        }




        pref = getSharedPreferences("my_pref", MODE_PRIVATE);

        String restoredText = pref.getString("status_check", null);
        if (restoredText != null) {
            String check = pref.getString("status_check", " ");//"No name defined" is the default value.
             login_Time = pref.getString("login_time", " ");
            String Employee_Name = pref.getString("User_name", " ");
            login_id = pref.getString("login_id", " ");
            user_id = pref.getString("user_id", " ");
            String img_profile1 = pref.getString("img_url", " ");

            if (img_profile1 != null) {
                Glide.with(this).load(img_profile1).into(img_profile);
            }
            if (Util.isNetworkAvailable(this)) {
                get_calculate_hour(user_id);
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

    @Override
    protected void onStart() {
        super.onStart();


        AppUpdateManager appUpdateManager;


        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(HrActivity.this);

        // Don't need to do this here anymore
        // Returns an intent object that you use to check for an update.
        //Task<AppUpdateInfo> appUpdateInfo = appUpdateManager.getAppUpdateInfo();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {

                            // Checks that the platform will allow the specified type of update.
                            if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
                            {
                                // Request the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            appUpdateInfo,
                                            AppUpdateType.IMMEDIATE,
                                            this,
                                            11);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 11) {
            if (resultCode != RESULT_OK) {
                Log.e(TAG, "onActivityResult: app download failed");
            }
            else{

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                int mPendingIntentId = 1;
                PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(HrActivity.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);

            }
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        // Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
    }
    public void find_view_by_id() {
        img_slide_gen = (ImageView) findViewById(R.id.img_slide_gen);
        mDrawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        dashBoard = findViewById(R.id.dashBoard);
        applyLeave = findViewById(R.id.applyLeave);
        workhome = findViewById(R.id.workhome);

       // attendance_frag = findViewById(R.id.attendance_frag);
        // salary_frag=findViewById(R.id.salary_frag);
        expense_frag = findViewById(R.id.expense_frag);
        setting = findViewById(R.id.setting);
        //text_header=findViewById(R.id.text_header);
        employee_name = findViewById(R.id.employee_name);
        login_time = findViewById(R.id.login_time);
        my_profile = findViewById(R.id.my_profile);
        info = findViewById(R.id.info);
        img_profile = findViewById(R.id.img_profile);
        salary=findViewById(R.id.salary);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        /*tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);*/


        add_fab = findViewById(R.id.add_fab);
        apply_leve = findViewById(R.id.apply_leve);
        apply_wfm_float = findViewById(R.id.apply_wfm_float);
        expense = findViewById(R.id.expense);
        aply_loan = findViewById(R.id.aply_loan);

        apply_leve.setVisibility(View.GONE);
        apply_wfm_float.setVisibility(View.GONE);
        expense.setVisibility(View.GONE);
        aply_loan.setVisibility(View.GONE);

        isAllFabsVisible = false;

        //add_fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.add));
        //add_fab.setImageResource(R.drawable.add);
        text_header1 = findViewById(R.id.text_header1);
        text_for_select = findViewById(R.id.text_for_select);
        meetings = findViewById(R.id.meetings);
        hr_events = findViewById(R.id.hr_events);
        request_history_hr = findViewById(R.id.request_history_hr);
        request_show_hr = findViewById(R.id.request_show_hr);

    }

    public void set_on_click() {
        img_slide_gen.setOnClickListener(this);
        dashBoard.setOnClickListener(this);
        applyLeave.setOnClickListener(this);
        workhome.setOnClickListener(this);
        //attendance_frag.setOnClickListener(this);
        // salary_frag.setOnClickListener(this);
        expense_frag.setOnClickListener(this);
        setting.setOnClickListener(this);
        my_profile.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        info.setOnClickListener(this);

        add_fab.setOnClickListener(this);
        apply_leve.setOnClickListener(this);
        apply_wfm_float.setOnClickListener(this);
        expense.setOnClickListener(this);
        aply_loan.setOnClickListener(this);

        meetings.setOnClickListener(this);
        hr_events.setOnClickListener(this);
        salary.setOnClickListener(this);
        request_history_hr.setOnClickListener(this);
        request_show_hr.setOnClickListener(this);
    }

    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment,""); // give your fragment container id in first parameter
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
    private void openFragment1Tab(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment,"Tablayout"); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_slide_gen:
                mDrawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.dashBoard:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("DashBoard");
                /*HomeFragment homeFragment = new HomeFragment();
                openFragment(homeFragment);*/


                HrTablayoutFragment tabLayoutFragment = new HrTablayoutFragment();
                openFragment1Tab(tabLayoutFragment);
                break;
            case R.id.applyLeave:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Apply Leave");
                ApplyLeaveFragment applyLeaveFragment = new ApplyLeaveFragment();
                openFragment1(applyLeaveFragment);

                break;
            case R.id.workhome:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Apply Work From Home");
                ApplyWorkFromHomeFragment applyWorkFromHomeFragment = new ApplyWorkFromHomeFragment();
                openFragment1(applyWorkFromHomeFragment);
                break;

            case R.id.salary:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Salary Slip");
                SalaryFragment salaryFragment = new SalaryFragment();
                openFragment1(salaryFragment);
                break;
            case R.id.expense_frag:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Apply Expense");
                ApplyExpenseFragment applyExpenseFragment = new ApplyExpenseFragment();

                openFragment1(applyExpenseFragment);
                break;
            case R.id.my_profile:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                openFragment1(myProfileFragment);
                break;
            case R.id.setting:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                get_logout_method();
                break;
            case R.id.img_profile:
                MyProfileFragment myProfileFragment1 = new MyProfileFragment();
                openFragment1(myProfileFragment1);
                break;

            case R.id.info:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Info");
                InfoFragment infoFragment = new InfoFragment();
                openFragment1(infoFragment);
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

            case R.id.request_show_hr:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Request");

                ViewAllCommingRequestAdmin viewAllCommingRequestAdmin =new ViewAllCommingRequestAdmin();
                Bundle bundle = new Bundle();
                bundle.putString("show_req_from_hr", "HR");
                viewAllCommingRequestAdmin.setArguments(bundle);
                openFragment1(viewAllCommingRequestAdmin);
                break;


            case R.id.request_history_hr:
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
                text_header1.setText("Request History");
                ApprovedRequestHistoryFragment approvedRequestHistoryFragment = new ApprovedRequestHistoryFragment();
                openFragment1(approvedRequestHistoryFragment);
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
                text_header1.setText("Apply Leave");
                ApplyLeaveFragment applyLeaveFragment1 = new ApplyLeaveFragment();
                openFragment(applyLeaveFragment1);
                if (isAllFabsVisible) {
                    apply_leve.hide();
                    apply_wfm_float.hide();
                    expense.hide();
                    aply_loan.hide();
                    add_fab.setImageResource(R.drawable.newadd);
                }
                break;
            case R.id.apply_wfm_float:
                text_header1.setText("Apply WFH");
                ApplyWorkFromHomeFragment applyWorkFromHomeFragment1 = new ApplyWorkFromHomeFragment();
                openFragment(applyWorkFromHomeFragment1);
                if (isAllFabsVisible) {
                    apply_leve.hide();
                    apply_wfm_float.hide();
                    expense.hide();
                    aply_loan.hide();
                    add_fab.setImageResource(R.drawable.newadd);
                }
                break;
            case R.id.expense:
                text_header1.setText("Apply Expense");
                ApplyExpenseFragment applyExpenseFragment1 = new ApplyExpenseFragment();
                openFragment(applyExpenseFragment1);
                if (isAllFabsVisible) {
                    apply_leve.hide();
                    apply_wfm_float.hide();
                    expense.hide();
                    aply_loan.hide();
                    add_fab.setImageResource(R.drawable.newadd);
                }
                break;
            case R.id.aply_loan:
                Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
                if (isAllFabsVisible) {
                    apply_leve.hide();
                    apply_wfm_float.hide();
                    expense.hide();
                    aply_loan.hide();
                    add_fab.setImageResource(R.drawable.newadd);
                }
                break;

        }
    }


    @SuppressLint("NewApi")
    public void get_logout_method() {


        Button button1=new Button(this);
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
                .setAnimationViewHeight(150)
                .setAnimationViewWidth(150)
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

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);

            Button button1=new Button(this);
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
        } else if (isAllFabsVisible) {
            apply_leve.hide();
            apply_wfm_float.hide();
            expense.hide();
            aply_loan.hide();
            add_fab.setImageResource(R.drawable.newadd);
        } else {
            super.onBackPressed();
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }
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
                    if (res.equalsIgnoreCase("success")) {

                        pref = getSharedPreferences("my_pref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        Intent in3 = new Intent(HrActivity.this, LoginActivity.class);
                        in3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // To clean up all activities
                        in3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in3);
                        finish();
                    } else {
                        Toast.makeText(HrActivity.this, "Logout Failed", Toast.LENGTH_SHORT).show();
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

                text_header1.setText("DashBoard");
                HrTablayoutFragment tabLayoutFragment = new HrTablayoutFragment();
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
    private void get_calculate_hour(String userid) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<LoginResponse>> call1 = apiInterface.get_hour_calculate(userid);
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
                        String res = loginResponse.get(i).getResponse();
                        if (res.equalsIgnoreCase("success")) {
                            Current_weekly_hour = loginResponse.get(i).getCurrentWeekHours();
                            previous_weekly_hour =loginResponse.get(i).getPreviousWeekHours();
                            Login_time_biometric = loginResponse.get(i).getLoginTime();
                            login_time.setText("Login Time: " + Login_time_biometric +"\n PWH- "+ previous_weekly_hour +", CWH- "+ Current_weekly_hour);
                            editor = getSharedPreferences("my_pref", MODE_PRIVATE).edit();
                            editor.putString("Current_weekly_hour", Current_weekly_hour);
                            editor.putString("previous_weekly_hour", previous_weekly_hour);
                            editor.putString("login_time", Login_time_biometric+"\n PWH- "+ previous_weekly_hour +", CWH- "+ Current_weekly_hour);

                            editor.apply();

                        }else {
                            login_time.setText("Login Time: "+login_Time);
                        }
                    }
                    //  Log.e("status", status);


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<LoginResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

}