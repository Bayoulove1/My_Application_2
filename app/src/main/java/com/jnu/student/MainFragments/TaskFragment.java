package com.jnu.student.MainFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.student.R;
import com.jnu.student.Task_Reward_Details.LongTaskFragment;
import com.jnu.student.Task_Reward_Details.ShortTaskFragment;

import java.util.ArrayList;
import java.util.List;

public class TaskFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private List<String> mTitle = new ArrayList<>();

    public TaskFragment() {
        // Required empty public constructor
    }


    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_task, container, false);
        initTitle();
        initView(rootView);
        return rootView;
    }

    private void initTitle() {
        mTitle.add("计划");
        mTitle.add("长期计划");
    }
 private void initView(View rootView){
        tabLayout = rootView.findViewById(R.id.tab_layout);
        viewPager2 = rootView.findViewById(R.id.view_pager2);
        TaskAdapter taskAdapter = new TaskAdapter(getActivity(),mTitle);
        viewPager2.setAdapter(taskAdapter);
     new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
         @Override
         public void onConfigureTab(TabLayout.Tab tab, int position) {
             tab.setText(mTitle.get(position));
         }
     }).attach();
 }
 private class TaskAdapter extends FragmentStateAdapter{
     private List<String> mTitle;

     public TaskAdapter(FragmentActivity activity, List<String> title) {
         super(activity);
         this.mTitle = title;
     }
     @Override
     public Fragment createFragment(int position) {
         String title = mTitle.get(position);
         if (title.equals("计划")) {
             return ShortTaskFragment.newInstance();
         } else if (title.equals("长期计划")) {
             return LongTaskFragment.newInstance();
         } else {
             return null;
         }
     }
     @Override
     public int getItemCount() {
         return mTitle.size();
     }
 }
}