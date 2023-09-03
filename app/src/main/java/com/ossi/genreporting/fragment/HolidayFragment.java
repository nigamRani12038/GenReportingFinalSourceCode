package com.ossi.genreporting.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ossi.genreporting.Adapter.ExpenseViewAdapter;
import com.ossi.genreporting.Adapter.HolidayAdapter;
import com.ossi.genreporting.R;
import com.ossi.genreporting.Util;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.ExpenseViewResponse;
import com.ossi.genreporting.model.HolidayListResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HolidayFragment extends Fragment {
    RecyclerView holiday_recyclerview;
    View view;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    private String Holiday_date,Holiday_name;

    private ArrayList<HolidayListResponse> holiday_list;
    HolidayAdapter holiday_adapter;
    String current_month;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_holiday, container, false);
        holiday_recyclerview=view.findViewById(R.id.holiday_recyclerview);

          if (Util.isNetworkAvailable(getActivity())){
              get_holiday_list();
          }
         else {
              Toast.makeText(getActivity(), "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
          }


        return view;
    }


    private void get_holiday_list() {
        apiInterface = APIClient.getClient().create(APIInterface.class);
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        Call<List<HolidayListResponse>> call1 = apiInterface.get_holiday_list();
        call1.enqueue(new Callback<List<HolidayListResponse>>() {
            @Override
            public void onResponse(Call<List<HolidayListResponse>> call, Response<List<HolidayListResponse>> response) {
                List<HolidayListResponse> EventResponseItem = response.body();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();


                if (EventResponseItem != null && EventResponseItem.size() > 0) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    holiday_list = new ArrayList<>();
                    for (int i = 0; i < EventResponseItem.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();
                        HolidayListResponse   model=new HolidayListResponse();

                        Holiday_name = EventResponseItem.get(i).getHoliName();
                        Holiday_date = EventResponseItem.get(i).getHoliDate();

                        model.setHoliName(Holiday_name);
                        model.setHoliDate(Holiday_date);

                        holiday_list.add(model);

                    }


                    holiday_adapter = new HolidayAdapter(holiday_list, getActivity());
                    holiday_recyclerview.setAdapter(holiday_adapter);
                    holiday_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));

                       if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();


                }

            }

            @Override
            public void onFailure(Call<List<HolidayListResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Server Not Responde", Toast.LENGTH_SHORT).show();
                call.cancel();
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
            }
        });
    }

}