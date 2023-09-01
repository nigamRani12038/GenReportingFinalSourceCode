package com.ossi.genreporting.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.ossi.genreporting.Adapter.MyTaskAdapter;
import com.ossi.genreporting.Adapter.ProjectAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.MyProjectListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AllMyProjectFragment extends Fragment {

    View view;
   /* TextView text_header1, employee_name, text_for_select;
    TextView login_time;
    RoundedImageView img_profile;*/
    RecyclerView my_project_list_list;
    private String user_id;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private ArrayList<MyProjectListResponse> task_list;
    ProjectAdapter task_adapter;
    private String Project_Name, project_id, Approve_status, Deadline, Submit_Date, Task_Detail, Status, Emp_Name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_my_project, container, false);
        find_view_by_id();
        set_on_click_litioner();

        SharedPreferences pref = getActivity().getSharedPreferences("my_pref", MODE_PRIVATE);

        user_id = pref.getString("user_id", null);

        String login_Time = pref.getString("login_time", " ");
        String Employee_Name = pref.getString("User_name", " ");
        String img_profile1 = pref.getString("img_url", " ");

       /* if (img_profile1 != null) {
            Glide.with(this).load(img_profile1).into(img_profile);
        }
        employee_name.setText(Employee_Name);
        login_time.setText("Login Time: " + login_Time);

        text_for_select.setText("");
        text_header1.setText("My Task");*/

        if (isNetworkAvailable()) {
            get_my_project_list();
        } else {
            Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    public void find_view_by_id() {
        //text_for_select = view.findViewById(R.id.text_for_select);
        //text_header1 = view.findViewById(R.id.text_header1);
        //employee_name = view.findViewById(R.id.employee_name);
        // login_time = view.findViewById(R.id.login_time);
        //  img_profile=view.findViewById(R.id.img_profile);
        my_project_list_list = view.findViewById(R.id.my_project_list_list);
    }

    public void set_on_click_litioner() {

    }

    private void get_my_project_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<MyProjectListResponse>> call1 = apiInterface.get_my_project_lists(user_id);
        call1.enqueue(new Callback<List<MyProjectListResponse>>() {
            @Override
            public void onResponse(Call<List<MyProjectListResponse>> call, Response<List<MyProjectListResponse>> response) {
                List<MyProjectListResponse> my_task_res = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (my_task_res != null && my_task_res.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    task_list = new ArrayList<>();
                    for (int i = 0; i < my_task_res.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        MyProjectListResponse model = new MyProjectListResponse();
                        String res = my_task_res.get(i).getResponse();
                       if (res.equalsIgnoreCase("success")) {
                            Project_Name = my_task_res.get(i).getProject_Name();
                            project_id = my_task_res.get(i).getProject_id();

                            model.setProject_Name(Project_Name);
                            model.setProject_id(project_id);


                            task_list.add(model);
                           task_adapter = new ProjectAdapter(task_list, getActivity());
                           my_project_list_list.setAdapter(task_adapter);
                           my_project_list_list.setLayoutManager(new LinearLayoutManager(getActivity()));

                       } else if (res.equalsIgnoreCase("Fail")) {
                            Toast.makeText(getActivity(), "Not Yet assign task for you", Toast.LENGTH_SHORT).show();
                        }
                    }



                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<List<MyProjectListResponse>> call, Throwable t) {
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
}
