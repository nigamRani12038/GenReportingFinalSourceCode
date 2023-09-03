package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.ossi.genreporting.Adapter.AllProductListAdapter;
import com.ossi.genreporting.Adapter.AssignTaskAdapter;
import com.ossi.genreporting.Adapter.SalaryApprovalShowAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.activity.HomeActivity;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIClientProduct;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.AllProductResponseItem;
import com.ossi.genreporting.model.ApplyLeaveResponseItem;
import com.ossi.genreporting.model.CreateProductModelResponse;
import com.ossi.genreporting.model.LoginResponse;
import com.ossi.genreporting.model.MyProjectListResponse;
import com.ossi.genreporting.model.PayrollSalaryShowResponse;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class ProductPlannerFragment extends Fragment implements View.OnClickListener {
    View view;
    TextView create_product;
    Dialog dialog;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    String user_id;
    //ToggleButton active_inactive;
    //TextView product_name;
    RecyclerView product_recycler;
    AllProductListAdapter allProductListAdapter;
    private ArrayList<AllProductResponseItem> product_list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_planner, container, false);
        find_view_by_id();
        set_on_click_litioner();
        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);

       if(Util.isNetworkAvailable(getActivity())) {
           get_all_products(user_id);
       }else {
           Toast.makeText(getActivity(), "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
       }
        return view;
    }


    public void find_view_by_id() {
        create_product = view.findViewById(R.id.create_product);
        //active_inactive=view.findViewById(R.id.active_inactive);
        product_recycler=view.findViewById(R.id.product_recycler);
    }

    public void set_on_click_litioner() {
        create_product.setOnClickListener(this);
       // active_inactive.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_product:

               /* CreateProduct createProduct = new CreateProduct();
                openFragment1(createProduct);
*/
                dialog = new Dialog(getActivity(), R.style.TransparentDialog);

                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                } else {
                    dialog.setContentView(R.layout.fragment_create_product);
                    dialog.getWindow().setGravity(Gravity.BOTTOM);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.color.bgdrop));
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setCancelable(true);


                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                    layoutParams.copyFrom(dialog.getWindow().getAttributes());
                    layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
                    layoutParams.gravity = Gravity.BOTTOM;
                    dialog.getWindow().setAttributes(layoutParams);
                    dialog.show();




                    Button create_product_btn = dialog.findViewById(R.id.create_product_btn);
                  TextInputEditText product_name=dialog.findViewById(R.id.product_name);
                  TextInputEditText description=dialog.findViewById(R.id.description);
                  TextView close_icon=dialog.findViewById(R.id.close_icon);
                  TextInputEditText product_progress=dialog.findViewById(R.id.product_progress);
                    Spinner product_progress_spinner=dialog.findViewById(R.id.product_progress_spinner);



                    create_product_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String productName=product_name.getText().toString();
                            String Description=description.getText().toString();

                           if (productName.equalsIgnoreCase("")){
                               Toast.makeText(getActivity(), "Please Enter Product Name", Toast.LENGTH_SHORT).show();
                           }else if (Description.equalsIgnoreCase("")){
                               Toast.makeText(getActivity(), "Please Enter Description", Toast.LENGTH_SHORT).show();
                           }
                           else {

                               create_Product(user_id,productName,Description);
                           }
                        }
                    });
                    close_icon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    product_progress.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            product_progress_spinner.performClick();
                        }
                    });

                    product_progress_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            //selectedCity= (City) parent.getAdapter().getItem(position);

                            product_progress.setText("In Progress");

                           // RDALogger.debug("selectedObject " + selectedCity);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });



                }
                break;

            case R.id.active_inactive:
                //toggleState(view);
                break;
        }
    }



private void create_Product(String userId,String Product_Name,String DesCription) {
    apiInterface = APIClientProduct.getClient().create(APIInterface.class);
    mProgressDialog = new ProgressDialog(getActivity());
    mProgressDialog.setIndeterminate(true);
    mProgressDialog.setMessage("Please wait...");
    mProgressDialog.show();


    MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain");

    RequestBody user_id = RequestBody.create(MEDIA_TYPE_TEXT, userId);
    RequestBody product_name = RequestBody.create(MEDIA_TYPE_TEXT,       Product_Name);
    RequestBody Description = RequestBody.create(MEDIA_TYPE_TEXT,       DesCription);
    RequestBody Employee_id = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody Assign = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody view = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody member_active = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody progress = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody link = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody version_count = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody version_name = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody start_date = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody Due_date = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody release_date = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody Attachment = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody title = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");
    RequestBody file_url = RequestBody.create(MEDIA_TYPE_TEXT,       "Testing");


    Map<String, RequestBody> map = new HashMap<>();
    map.put("Sno", user_id);
    map.put("product_name", product_name);
    map.put("product_description", Description);
    Call<CreateProductModelResponse> call1 = apiInterface.create_product(map);
    call1.enqueue(new Callback<CreateProductModelResponse>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onResponse(Call<CreateProductModelResponse> call, Response<CreateProductModelResponse> response) {
            CreateProductModelResponse createProductModelResponse = response.body();
            if (response.isSuccessful()) {
                Toast.makeText(getActivity(), "Create Product Successfully", Toast.LENGTH_SHORT).show();
                Log.e("res","res:"+createProductModelResponse.getProduct_id());
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                dialog.dismiss();
            }
        }
        @Override    public void onFailure(Call<CreateProductModelResponse> call, Throwable t) {
            Log.e("TAG", "CountAssigned: " + t.getLocalizedMessage());
        }
    });
}

    private void get_all_products(String userid) {
        apiInterface = APIClientProduct.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<AllProductResponseItem>> call1 = apiInterface.get_all_products(userid);
        call1.enqueue(new Callback<List<AllProductResponseItem>>() {
            @Override
            public void onResponse(Call<List<AllProductResponseItem>> call, Response<List<AllProductResponseItem>> response) {
                List<AllProductResponseItem> allProductResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                Log.e("allProductResponseItem", "allProductResponseItem 1 --> " + allProductResponseItem);

                if (allProductResponseItem != null && allProductResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    product_list = new ArrayList<>();
                    for (int i = 0; i < allProductResponseItem.size(); i++) {
                        AllProductResponseItem model=new AllProductResponseItem();
                        String Product_name = allProductResponseItem.get(i).getProductName();
                        if (!Product_name.equals("")) {
                           String Emp_Name = allProductResponseItem.get(i).getEmpname();
                            String assign_count = String.valueOf(allProductResponseItem.get(i).getAssignedCount());
                            String unassign_count  = String.valueOf(allProductResponseItem.get(i).getUnassignedCount());
                            String module_count  = String.valueOf(allProductResponseItem.get(i).getModuleCount());
                            String attachment_count  = String.valueOf(allProductResponseItem.get(i).getAttachementCount());
                           /* String attachment_count  = String.valueOf(allProductResponseItem.get(i).getAttachementCount());
                            String attachment_count  = String.valueOf(allProductResponseItem.get(i).getAttachementCount());
                            String attachment_count  = String.valueOf(allProductResponseItem.get(i).getAttachementCount());*/



                            model.setProductName(Product_name);
                            model.setEmpname(Emp_Name);
                            model.setAssignedCount(Integer.parseInt(assign_count));
                            model.setUnassignedCount(Integer.parseInt(unassign_count));
                            model.setModuleCount(Integer.parseInt(module_count));
                            model.setAttachementCount(Integer.parseInt(attachment_count));


                            product_list.add(model);

                        }
                    }
                    //  Log.e("status", status);

                    allProductListAdapter = new AllProductListAdapter(product_list, getActivity());
                    product_recycler.setAdapter(allProductListAdapter);
                    product_recycler.setLayoutManager(new LinearLayoutManager(getActivity()));

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(Call<List<AllProductResponseItem>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }


    private void openFragment1(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment); // give your fragment container id in first parameter
        transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        transaction.isAddToBackStackAllowed();
        transaction.commit();
    }
}