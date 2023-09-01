package com.ossi.genreporting.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ossi.genreporting.R;
import com.ossi.genreporting.api.APIClient;
import com.ossi.genreporting.api.APIInterface;
import com.ossi.genreporting.model.EventResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EventFragment extends Fragment {


    private int identifier;
    private int colour;
    private APIInterface apiInterface;
    private ProgressDialog mProgressDialog;
    View v;
    String Event_Name,Emp_Event;
    TextView event;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        colour = args.getInt("colour");
        identifier = args.getInt("identifier");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_event, container, false);
        event=v.findViewById(R.id.event);
        get_event();


       /* TextView v = new TextView(getActivity());
        v.setGravity(Gravity.CENTER);
        v.setTextSize(40);
        v.setTextColor(Color.BLACK);
        v.setBackgroundColor(colour);
        v.setText("Fragment ID: " + identifier);*/

        return v;

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("dummy", true);
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

                    for (int i = 0; i < EventResponseItem.size(); i++) {
                        if (mProgressDialog.isShowing())
                            mProgressDialog.dismiss();

                        Event_Name = EventResponseItem.get(i).getEmpName();
                         Emp_Event = EventResponseItem.get(i).getEvent();


                    }

                    if (Emp_Event.equalsIgnoreCase("DOB")) {
                        event.setText("It is "+Event_Name+" Birthday Today");
                        //It is Utkarsh's Birthday Today
                    }
                    else if (Emp_Event.equalsIgnoreCase("Married")){
                        event.setText("It is "+Event_Name+" Marriage Aniversary Today");
                    }else if (Emp_Event.equalsIgnoreCase("HoliDay")){

                        String[] dateParts = Event_Name.split(",");
                        String eventname = dateParts[0];
                        String date = dateParts[1];
                        event.setText("It is "+eventname+" You have a Holiday on the "+date);
                    }
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                }

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

}