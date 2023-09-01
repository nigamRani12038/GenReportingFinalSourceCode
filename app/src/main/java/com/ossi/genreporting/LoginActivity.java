package com.ossi.genreporting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ossi.genreporting.activity.AdminActivity;
import com.ossi.genreporting.activity.FinanceActivity;
import com.ossi.genreporting.activity.ForgotPassword;
import com.ossi.genreporting.activity.HomeActivity;
import com.ossi.genreporting.activity.HrActivity;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText email_id, pass;
    Button login_btn;
    String user_str, pass_str;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String status;
    private SharedPreferences.Editor editor;
    private String User_Id;
    private SharedPreferences pref;
    private String User_name, employee_code, login_time, login_id, login_type,desgination_id,img_url1;
    TextView forgot_txt;

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    String lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLastLocation();
        find_view_by_id();
        set_on_click_litioner();
        pref = getSharedPreferences("my_pref", MODE_PRIVATE);

        String restoredText = pref.getString("status_check", null);
        if (restoredText != null) {
            String check = pref.getString("status_check", " ");//"No name defined" is the default value.
            String login_type = pref.getString("login_type", " ");
            if (check.equalsIgnoreCase("success")) {
                if(login_type.equalsIgnoreCase("1")) {
                    Intent in = new Intent(LoginActivity.this, AdminActivity.class);
                    startActivity(in);

                    finish();
                }else if(login_type.equalsIgnoreCase("2")) {
                    Intent in = new Intent(LoginActivity.this, HrActivity.class);
                    startActivity(in);

                    finish();
                }else if(login_type.equalsIgnoreCase("0")) {
                    Intent in = new Intent(LoginActivity.this, FinanceActivity.class);
                    startActivity(in);

                    finish();
                } else {
                    Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                    // in.putExtra("user_id",user_name);
                    startActivity(in);

                    finish();
                }
            }
        }

    }

    public void find_view_by_id() {
        email_id = (EditText) findViewById(R.id.email_id);
        pass = (EditText) findViewById(R.id.pass);
        login_btn = (Button) findViewById(R.id.login_btn);
        forgot_txt = findViewById(R.id.forgot_txt);
    }

    public void set_on_click_litioner() {
        login_btn.setOnClickListener(this);
        forgot_txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                user_str = email_id.getText().toString();
                pass_str = pass.getText().toString();

                if (user_str.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please Enter User Name", Toast.LENGTH_SHORT).show();
                } else if (pass_str.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (isNetworkAvailable()) {
                        loginRetrofit2Api(user_str, pass_str);
                    } else {
                        Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.forgot_txt:
                Intent in = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(in);
                break;
        }
    }

    private void loginRetrofit2Api(String userid, String pwd) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<LoginResponse>> call1 = apiInterface.get_Login_report(userid, pwd, lat, lon);
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
                        status = loginResponse.get(i).getResponse();
                        User_Id = loginResponse.get(i).getUserID();
                        User_name = loginResponse.get(i).getName();
                        employee_code = loginResponse.get(i).getEmpCode();
                        login_time = loginResponse.get(i).getLoginTime();
                        login_id = loginResponse.get(i).getLogoutID();
                        desgination_id = loginResponse.get(i).getDesignation_id();
                        login_type = loginResponse.get(i).getRoleId();
                        img_url1=loginResponse.get(i).getImage();
                        Log.e("status", status);
                    }
                    //  Log.e("status", status);

                    if (status.equalsIgnoreCase("success")) {


                        String flag = "success";
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        if (login_type.equalsIgnoreCase("1")) {
                            Intent in = new Intent(LoginActivity.this, AdminActivity.class);
                            editor = getSharedPreferences("my_pref", MODE_PRIVATE).edit();
                            editor.putString("status_check", flag);
                            editor.putString("user_id", User_Id);
                            editor.putString("User_name", User_name);
                            editor.putString("employee_code", employee_code);
                            editor.putString("login_time", login_time);
                            editor.putString("login_id", login_id);
                            editor.putString("login_type", login_type);
                            editor.putString("img_url", img_url1);
                            editor.apply();
                            startActivity(in);
                            finish();
                        }else if (login_type.equalsIgnoreCase("2")) {
                            Intent in = new Intent(LoginActivity.this, HrActivity.class);
                            editor = getSharedPreferences("my_pref", MODE_PRIVATE).edit();
                            editor.putString("status_check", flag);
                            editor.putString("user_id", User_Id);
                            editor.putString("User_name", User_name);
                            editor.putString("employee_code", employee_code);
                            editor.putString("login_time", login_time);
                            editor.putString("login_id", login_id);
                            editor.putString("login_type", login_type);
                            editor.putString("img_url", img_url1);
                            editor.apply();
                            startActivity(in);
                            finish();
                        } else if (login_type.equalsIgnoreCase("0")) {
                            Intent in = new Intent(LoginActivity.this, FinanceActivity.class);
                            editor = getSharedPreferences("my_pref", MODE_PRIVATE).edit();
                            editor.putString("status_check", flag);
                            editor.putString("user_id", User_Id);
                            editor.putString("User_name", User_name);
                            editor.putString("employee_code", employee_code);
                            editor.putString("login_time", login_time);
                            editor.putString("login_id", login_id);
                            editor.putString("login_type", login_type);
                            editor.putString("img_url", img_url1);
                            editor.apply();
                            startActivity(in);
                            finish();
                        } else {
                            Intent in = new Intent(LoginActivity.this, HomeActivity.class);
                            editor = getSharedPreferences("my_pref", MODE_PRIVATE).edit();
                            editor.putString("status_check", flag);
                            editor.putString("user_id", User_Id);
                            editor.putString("User_name", User_name);
                            editor.putString("employee_code", employee_code);
                            editor.putString("login_time", login_time);
                            editor.putString("login_id", login_id);
                            editor.putString("login_type", login_type);
                            editor.putString("img_url", img_url1);
                            editor.apply();
                            startActivity(in);
                            finish();
                        }
                    } else if (status.equalsIgnoreCase("Office Email Not Found ")) {
                        Toast.makeText(LoginActivity.this, "Invalid User Name and Password", Toast.LENGTH_SHORT).show();
                    }else if (status.equalsIgnoreCase("Enter Valid Password")) {
                        Toast.makeText(LoginActivity.this, "please Correct User id and password", Toast.LENGTH_SHORT).show();
                    }
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


    public boolean isNetworkAvailable() {
        final android.net.ConnectivityManager connectivityManager = ((android.net.ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }


    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(
                        new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                Location location = task.getResult();
                                if (location == null) {
                                    requestNewLocationData();
                                } else {
                                    // latTextView.setText(location.getLatitude()+"");
                                    lat = location.getLatitude() + "";
                                    lon = location.getLongitude() + "";
                                    //Toast.makeText(LoginActivity.this, ""+lat+"  , "+lon, Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                );
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();

            lat = mLastLocation.getLatitude() + "";
            lon = mLastLocation.getLongitude() + "";
            // Toast.makeText(LoginActivity.this, ""+lat+"  , "+lon, Toast.LENGTH_SHORT).show();

        }
    };

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSION_ID
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }

    }

}