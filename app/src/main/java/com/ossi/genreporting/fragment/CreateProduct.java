package com.ossi.genreporting.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ossi.genreporting.R;

public class CreateProduct extends Fragment implements View.OnClickListener {
View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_create_product, container, false);
        find_view_by_id();
        set_on_click_litioner();


       // text_header1.setText("Assigned Task");


        return view;
    }
    public void find_view_by_id(){

    }

    public void set_on_click_litioner(){

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }
}