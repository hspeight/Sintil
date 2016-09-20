package uk.co.sintildate.sintil;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

public class DateTimeDialogFragment extends DialogFragment {

    private ViewPager viewPager;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow() .setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.date_time_pager, container);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        //adapter.addFragment(new OneFragment(), "ONE");
        //adapter.addFragment(new TwoFragment(), "TWO");
        //adapter.addFragment(new ThreeFragment(), "THREE");

        // Set up the ViewPager with the sections adapter.
        viewPager = (ViewPager)view.findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        return view;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        //private final List<Fragment> mFragmentList = new ArrayList<>();
       // private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                // find first fragment...
                DatePickerFragment ft1 = new DatePickerFragment();
                return ft1;
            }
            if (position == 1) {
                // find first fragment...
                TimePickerFragment ft2 = new TimePickerFragment();
                return ft2;
            }

            return null;
        }

        @Override
        public int getCount() {
            //return mFragmentList.size();
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Date";
                case 1:
                    return "Time";
            }
            return null;
        }
    }

}