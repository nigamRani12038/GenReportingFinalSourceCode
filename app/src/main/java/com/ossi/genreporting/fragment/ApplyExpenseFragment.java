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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.ApplyExpenseResponseItem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplyExpenseFragment extends Fragment implements View.OnClickListener {
    View view;
    EditText ammount_expense, reason_expense;
    LinearLayout upload_expense_reciept;
    Button apply_expense_submit;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String user_id;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String ExpenseImage;
    ImageView expense_img;
    private String AMOUNT, Reason;
    private String res;
    TextView text_header1,employee_name;
    TextView login_time;
    RoundedImageView img_profile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_expense, container, false);


        find_view_by_id(view);
        set_on_click_litioner();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);

        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");

        if(img_profile1!=null){
            Glide.with(this).load(img_profile1).into(img_profile);
        }

        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: "+login_Time);

        text_header1.setText("Apply Expense");

        return view;
    }

    public void find_view_by_id(View view) {
        ammount_expense = view.findViewById(R.id.ammount_expense);
        reason_expense = view.findViewById(R.id.reason_expense);
        upload_expense_reciept = view.findViewById(R.id.upload_expense_reciept);
        apply_expense_submit = view.findViewById(R.id.apply_expense_submit);
        expense_img = view.findViewById(R.id.expense_img);

        employee_name=view.findViewById(R.id.employee_name);
        login_time=view.findViewById(R.id.login_time);
        text_header1=view.findViewById(R.id.text_header1);
        img_profile=view.findViewById(R.id.img_profile);

    }

    public void set_on_click_litioner() {
        apply_expense_submit.setOnClickListener(this);
        upload_expense_reciept.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.apply_expense_submit:
                AMOUNT = ammount_expense.getText().toString();
                Reason = reason_expense.getText().toString();


                if (AMOUNT.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Amount", Toast.LENGTH_SHORT).show();
                } else if (Reason.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please Enter Purpuse", Toast.LENGTH_SHORT).show();
                } else if (ExpenseImage == null) {
                    Toast.makeText(getActivity(), "Please Select Reciept", Toast.LENGTH_SHORT).show();
                } else {

                    if (Util.isNetworkAvailable(getActivity())) {
                        Expense_data_submit(user_id, AMOUNT, Reason, ExpenseImage);
                    } else {
                        Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.upload_expense_reciept:
                selectImage();
                break;
        }
    }


    private void Expense_data_submit(String userid, String Ammount, String Purpuse, String image) {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<ApplyExpenseResponseItem>> call1 = apiInterface.ApplyExpenses_submit(userid, Ammount, Purpuse, image);
        call1.enqueue(new Callback<List<ApplyExpenseResponseItem>>() {
            @Override
            public void onResponse(Call<List<ApplyExpenseResponseItem>> call, Response<List<ApplyExpenseResponseItem>> response) {
                List<ApplyExpenseResponseItem> applyExpenseResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (applyExpenseResponseItem != null && applyExpenseResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    for (int i = 0; i < applyExpenseResponseItem.size(); i++) {
                        res = applyExpenseResponseItem.get(i).getResponse();

                        if (res.equalsIgnoreCase("success")) {
                            Toast.makeText(getActivity(), "Expense Apply Success", Toast.LENGTH_SHORT).show();
                            TabLayoutFragment tabLayoutFragment = new TabLayoutFragment();
                            openFragment(tabLayoutFragment);
                        }else {
                            Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                        }


                    }


                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<ApplyExpenseResponseItem>> call, Throwable t) {
                Toast.makeText(getActivity(), "Please Try Again Server Not Responds", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
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
                        ExpenseImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
                        expense_img.setImageBitmap(bitmap);

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

            ExpenseImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

            Log.e("image1", "" + ExpenseImage);
            expense_img.setImageBitmap(photo);

        } catch (NullPointerException exeption) {
            exeption.printStackTrace();
        }
        return ExpenseImage;
    }
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment); // give your fragment container id in first parameter
       transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.commit();
    }
}