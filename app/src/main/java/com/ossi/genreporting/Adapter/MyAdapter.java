package com.ossi.genreporting.Adapter;


import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ossi.genreporting.fragment.ApplyWorkFromHomeFragment;
import com.ossi.genreporting.fragment.ApplyExpenseFragment;
import com.ossi.genreporting.fragment.LeaveViewFragment;

public class MyAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public MyAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                LeaveViewFragment leaveFragment = new LeaveViewFragment();
                return leaveFragment;
            case 1:
                ApplyWorkFromHomeFragment applyWorkFromHomeFragment = new ApplyWorkFromHomeFragment();
                return applyWorkFromHomeFragment;
            case 2:
                ApplyWorkFromHomeFragment applyWorkFromHomeFragment2 = new ApplyWorkFromHomeFragment();
                return applyWorkFromHomeFragment2;
            case 3:
                ApplyExpenseFragment applyExpenseFragment = new ApplyExpenseFragment();
                return applyExpenseFragment;
            case 4:
                ApplyWorkFromHomeFragment applyWorkFromHomeFragment3 = new ApplyWorkFromHomeFragment();
                return applyWorkFromHomeFragment3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
