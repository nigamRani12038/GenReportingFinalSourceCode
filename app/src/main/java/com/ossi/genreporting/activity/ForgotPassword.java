

package com.ossi.genreporting.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.LoginResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    EditText forgot_email_id, create_new_pass;
    Button set_pass;
    String Forgot_Email_id, Create_New_pass;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String status;
    private SharedPreferences.Editor editor;
    private String User_Id;

    private SharedPreferences pref;
    private String User_name,employee_code,login_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        find_view_by_id();
        set_on_click_litioner();
       

    }

    public void find_view_by_id() {
        forgot_email_id = findViewById(R.id.forgot_email_id);
        create_new_pass = findViewById(R.id.create_new_pass);
        set_pass = findViewById(R.id.set_pass);
    }

    public void set_on_click_litioner() {

        set_pass.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.set_pass:
                Forgot_Email_id = forgot_email_id.getText().toString();
                Create_New_pass = create_new_pass.getText().toString();

                if (Forgot_Email_id.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please Enter User Mail Id", Toast.LENGTH_SHORT).show();
                } else if (Create_New_pass.equalsIgnoreCase("")) {
                    Toast.makeText(this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (Util.isNetworkAvailable(this)) {
                        forgot_pass(Forgot_Email_id, Create_New_pass);
                    } else {
                        Toast.makeText(this, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void forgot_pass(String userid, String pwd) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<LoginResponse>> call1 = apiInterface.get_forgot_password(userid, pwd);
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
                        User_name=loginResponse.get(i).getName();
                        employee_code=loginResponse.get(i).getEmpCode();
                        login_time=loginResponse.get(i).getLoginTime();
                    }
                    //  Log.e("status", status);

                    if (status.equalsIgnoreCase("success")) {

                        String flag = "success";
                        Toast.makeText(ForgotPassword.this, "Password changed Successful", Toast.LENGTH_SHORT).show();

                        Intent in = new Intent(ForgotPassword.this, HomeActivity.class);
                        editor = getSharedPreferences("my_pref", MODE_PRIVATE).edit();
                        editor.putString("status_check", flag);
                        editor.putString("user_id", User_Id);
                        editor.putString("User_name", User_name);
                        editor.putString("employee_code", employee_code);
                        editor.putString("login_time", login_time);
                        editor.apply();
                        startActivity(in);
                        finish();
                    } else if (status.equalsIgnoreCase("Fail")) {
                        Toast.makeText(ForgotPassword.this, "Invalid User Id", Toast.LENGTH_SHORT).show();
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


}