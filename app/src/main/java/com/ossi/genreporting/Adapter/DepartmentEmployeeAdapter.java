package com.ossi.genreporting.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ossi.genreporting.R;
import com.ossi.genreporting.model.DepartmentEmployeeListResponse;

import java.util.ArrayList;
import java.util.List;

public class DepartmentEmployeeAdapter extends ArrayAdapter<DepartmentEmployeeListResponse> {
    private Context mContext;
    private ArrayList<DepartmentEmployeeListResponse> listState;
    private DepartmentEmployeeAdapter DepartmentEmployeeAdapter;
    private boolean isFromView = false;
    private Departmentinterface departmentinterface;

    public DepartmentEmployeeAdapter(Context context, int resource, List<DepartmentEmployeeListResponse> objects,Departmentinterface departmentinterface) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<DepartmentEmployeeListResponse>) objects;
        this.DepartmentEmployeeAdapter = this;
        this.departmentinterface=departmentinterface;

    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.name_with_check_box, null);
            holder = new ViewHolder();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            holder.selectAllCheckBox = (CheckBox) convertView
                    .findViewById(R.id.selectAll);
            holder.okayButton = (Button) convertView
                    .findViewById(R.id.okayButton);
            holder.dialog_selected_with_ok_button=(LinearLayout) convertView.findViewById(R.id.dialog_selected_with_ok_button);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.selectAllCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setAllCheckBox(b);

            }
        });
        if(position==0){
            holder.selectAllCheckBox.setVisibility(View.VISIBLE);
        }else {
            holder.selectAllCheckBox.setVisibility(View.GONE);
        }
        if(position==listState.size()-1){
            holder.okayButton.setVisibility(View.VISIBLE);
        }else {
            holder.okayButton.setVisibility(View.GONE);
        }

        holder.mTextView.setText(listState.get(position).getEmpName());

        // To check weather checked event fire from getview() or user input
        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        if ((position == -1)) {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
        }

        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();


                if (!isFromView) {
                    listState.get(position).setSelected(isChecked);
                }
            }

        });


        holder.okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSelectedData();
               // holder.dialog_selected_with_ok_button.setVisibility(View.INVISIBLE);

            }
        });

        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
        private CheckBox selectAllCheckBox;
        private Button okayButton;
        private LinearLayout dialog_selected_with_ok_button;
    }

    private void setAllCheckBox(Boolean isTrue){
        for(int i=0;i<listState.size();i++){
            listState.get(i).setSelected(isTrue);
        }
        notifyDataSetChanged();
    }
    private void getSelectedData(){
        String name="";
        String id="";
        for(int i=0;i<listState.size();i++){
            if(listState.get(i).isSelected()){
                name=name+","+listState.get(i).getEmpName();
                id=id+","+listState.get(i).getId();
            }
        }
        name=name.replaceFirst(",","");
        Log.i("size","name size:: "+listState.size());
        Log.i("selectedName","name:: "+name);

        id=id.replaceFirst(",","");
        Log.i("size","id size:: "+listState.size());
        Log.i("Id","id:: "+id);
        departmentinterface.clickCallback(name, id);

    }

    public interface Departmentinterface{
         void clickCallback(String key, String Value);}

}