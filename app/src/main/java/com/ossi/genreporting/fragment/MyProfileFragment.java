package com.ossi.genreporting.fragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.ProfileDetailsResponseItem;
import com.ossi.genreporting.model.UpdateResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyProfileFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView personal_btn, profesional_btn;
    LinearLayout personal_detail, profesional_detail;
    TextView emp_name1;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String user_id, user_NAme;
    private String Official_mail;
    TextView emp_name, f_name, m_name, gender, emp_code, emp_desgination,mail_id;
    TextView doj, official_mail, department;
    Button update_prof;
    String Mobile;
    EditText mobile_no, marital_status, pass;
    LinearLayout profile_pics;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String pro_Img;
    RoundedImageView pro_img_set;
    private SharedPreferences.Editor editor;
    private String login_type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
    /*    RelativeLayout homeHeaderLayout = getActivity().findViewById(R.id.homeHeader);
        homeHeaderLayout.setVisibility(View.GONE);*/
        find_view_by_id(view);
        set_on_click();
        personal_detail.setVisibility(View.VISIBLE);
        profesional_detail.setVisibility(View.GONE);

        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);
        user_NAme = pref.getString("User_name", null);
         login_type = pref.getString("login_type", " ");


            emp_name1.setText(user_NAme);
        if (isNetworkAvailable()) {
            get_all_profile_detail(user_id);
        } else {
            Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    public void find_view_by_id(View view) {
        personal_btn = view.findViewById(R.id.personal_btn);
        profesional_btn = view.findViewById(R.id.profesional_btn);
        profesional_detail = view.findViewById(R.id.profesional_detail);
        personal_detail = view.findViewById(R.id.personal_detail);
        emp_name = view.findViewById(R.id.emp_name);
        f_name = view.findViewById(R.id.f_name);
        m_name = view.findViewById(R.id.m_name);
        mobile_no = view.findViewById(R.id.mobile_no);
        mail_id = view.findViewById(R.id.mail_id);
        marital_status = view.findViewById(R.id.marital_status);
        gender = view.findViewById(R.id.gender);

        emp_code = view.findViewById(R.id.emp_code);
        doj = view.findViewById(R.id.doj);
        department = view.findViewById(R.id.department);
        emp_desgination = view.findViewById(R.id.emp_desgination);
        official_mail = view.findViewById(R.id.official_mail);
        update_prof = view.findViewById(R.id.update_prof);
        pass = view.findViewById(R.id.pass);
        emp_name1 = view.findViewById(R.id.emp_name1);
        profile_pics = view.findViewById(R.id.profile_pics);
        pro_img_set = view.findViewById(R.id.pro_img_set);
    }

    public void set_on_click() {
        personal_btn.setOnClickListener(this);
        profesional_btn.setOnClickListener(this);
        update_prof.setOnClickListener(this);
        profile_pics.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_btn:
                personal_detail.setVisibility(View.VISIBLE);
                profesional_detail.setVisibility(View.GONE);
                break;

            case R.id.profesional_btn:
                personal_detail.setVisibility(View.GONE);
                profesional_detail.setVisibility(View.VISIBLE);
                break;

            case R.id.update_prof:
                String Mobile = mobile_no.getText().toString();
               // String imag = "null";
                String Password = pass.getText().toString();
                String Mail_id=mail_id.getText().toString();
                String Marital_status=marital_status.getText().toString();
                if (Password.equalsIgnoreCase("")){
                    Password="NA";
                }
                if (Mobile.equalsIgnoreCase("")) {
                    Mobile="NA";
                }
                if (Marital_status.equalsIgnoreCase("")) {
                    Marital_status="NA";
                }


                   else if (isNetworkAvailable()) {
                        update_profile_(user_id, Mobile, Marital_status,Password);
                    } else {
                        Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                break;
            case R.id.profile_pics:
                selectImage();
                break;
        }
    }

    private void get_all_profile_detail(String userid) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ProfileDetailsResponseItem>> call1 = apiInterface.get_all_profile_details(userid);
        call1.enqueue(new Callback<List<ProfileDetailsResponseItem>>() {
            @Override
            public void onResponse(Call<List<ProfileDetailsResponseItem>> call, Response<List<ProfileDetailsResponseItem>> response) {
                List<ProfileDetailsResponseItem> ProfileDetailsResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (ProfileDetailsResponseItem != null && ProfileDetailsResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < ProfileDetailsResponseItem.size(); i++) {
                        Official_mail = ProfileDetailsResponseItem.get(i).getOffiEmail();
                        String AdharNo = ProfileDetailsResponseItem.get(i).getAdharNo();
                        String JobProfile = ProfileDetailsResponseItem.get(i).getJobProfile();
                        String Department = ProfileDetailsResponseItem.get(i).getDepartment();
                        String DOB = ProfileDetailsResponseItem.get(i).getDOB();
                        String DOJ = ProfileDetailsResponseItem.get(i).getDOJ();
                        String BloodGroup = ProfileDetailsResponseItem.get(i).getBloodGroup();
                        String Branch = ProfileDetailsResponseItem.get(i).getBranch();
                        String City = ProfileDetailsResponseItem.get(i).getCity();
                        String EmpName = ProfileDetailsResponseItem.get(i).getEmpName();
                        String Fname = ProfileDetailsResponseItem.get(i).getFname();
                        String Married = ProfileDetailsResponseItem.get(i).getMarried();
                        String DBtime = ProfileDetailsResponseItem.get(i).getDBtime();
                        String Mobile = ProfileDetailsResponseItem.get(i).getMobile();
                        String Mname = ProfileDetailsResponseItem.get(i).getMname();
                        String Email = ProfileDetailsResponseItem.get(i).getEmail();
                        String Gender = ProfileDetailsResponseItem.get(i).getGender();
                        String OffiEmail = ProfileDetailsResponseItem.get(i).getOffiEmail();
                        String empcode = ProfileDetailsResponseItem.get(i).getEmpCode();
                        String img = ProfileDetailsResponseItem.get(i).getImage();
                        emp_name.setText(EmpName);
                        f_name.setText(Fname);
                        m_name.setText(Mname);
                        mobile_no.setText(Mobile);
                        mail_id.setText(Email);
                        marital_status.setText(Married);
                        gender.setText(Gender);

                        emp_code.setText(empcode);
                        department.setText(Department);
                        doj.setText(DOJ);
                        official_mail.setText(OffiEmail);
                        emp_desgination.setText(JobProfile);
                        if(img!=null){
                            Glide.with(getActivity()).load(img).into(pro_img_set);
                            editor = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE).edit();
                            editor.putString("img_url", img);
                            editor.apply();
                        }

                    }


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<ProfileDetailsResponseItem>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    private void update_profile_(String userid, String Mobile,String marital_status, String pass) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<UpdateResponse>> call1 = apiInterface.Update_profile(userid, Mobile, marital_status,pass);
        call1.enqueue(new Callback<List<UpdateResponse>>() {
            @Override
            public void onResponse(Call<List<UpdateResponse>> call, Response<List<UpdateResponse>> response) {
                List<UpdateResponse> updateResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (updateResponse != null && updateResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < updateResponse.size(); i++) {
                        String res = updateResponse.get(i).getResponse();
                        if (res.equalsIgnoreCase("Success")) {
                            Toast.makeText(getActivity(), "update profile Success", Toast.LENGTH_SHORT).show();

                            if(login_type.equalsIgnoreCase("101")){
                                AdminTabLayoutFragment tabLayoutFragment = new AdminTabLayoutFragment();
                                openFragment(tabLayoutFragment);
                            }else {

                                TabLayoutFragment tabLayoutFragment = new TabLayoutFragment();
                                openFragment(tabLayoutFragment);
                            }
                        }

                    }


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<UpdateResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responds", Toast.LENGTH_SHORT).show();
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

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    checkPermissions();

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permission) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            } else {


                ClickImageFromCamera();

            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_CAMERA_PERMISSION_CODE);
            return false;
        }
        return true;
    }

    String[] permission = new String[]{
            android.Manifest.permission.CAMERA
    };

    public void ClickImageFromCamera() {


        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 0);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                ClickImageFromCamera();

            } else {
                Toast.makeText(getActivity(), "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:

                if (resultCode == -1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //this is above marshmellow
                        previewCapturedImage(data);
                    }

                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getActivity(),
                            "User cancelled image capture", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(getActivity(),
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }

                break;


            case 2:

                if (resultCode == -1) {
                    Uri selectedImage = data.getData();
                    // set_img1.setImageURI(uri);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                        Log.e("bitmap.", String.valueOf(bitmap));
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();
                        pro_Img = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                        pro_img_set.setImageBitmap(bitmap);
                        update_profile_image(pro_Img);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getActivity(),
                            "User cancelled image capture", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(getActivity(),
                            "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                            .show();
                }


                break;

        }
    }

    public String previewCapturedImage(Intent data) {
        try {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            photo.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            pro_Img = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

            Log.e("image1", "" + pro_Img);
            pro_img_set.setImageBitmap(photo);
            update_profile_image(pro_Img);

        } catch (NullPointerException exeption) {
            exeption.printStackTrace();
        }
        return pro_Img;
    }


    private void update_profile_image(String image) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<UpdateResponse>> call1 = apiInterface.Update_profile_img(user_id, image);
        call1.enqueue(new Callback<List<UpdateResponse>>() {
            @Override
            public void onResponse(Call<List<UpdateResponse>> call, Response<List<UpdateResponse>> response) {
                List<UpdateResponse> updateResponse = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (updateResponse != null && updateResponse.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < updateResponse.size(); i++) {
                        String res = updateResponse.get(i).getResponse();
                        String img_url1 = updateResponse.get(i).getImage();


                        if (res.equalsIgnoreCase("success")) {
                            Toast.makeText(getActivity(), "Profile Image Sucess", Toast.LENGTH_SHORT).show();
                            if(img_url1!=null){
                                Glide.with(getActivity()).load(img_url1).into(pro_img_set);
                                editor = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE).edit();
                                editor.putString("img_url", img_url1);
                                editor.apply();
                            }
                        }

                    }


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<UpdateResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment); // give your fragment container id in first parameter
       // transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
}