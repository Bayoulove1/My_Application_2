package com.jnu.student.MainFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.student.R;
import com.jnu.student.Task_Reward_Details.ShortTaskFragment;
import com.jnu.student.Task_Reward_Details.TotalRewardFragment;

import java.util.ArrayList;
import java.util.List;

public class RewardFragment extends Fragment {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private List<String> mTitle = new ArrayList<>();
    public RewardFragment() {
        // Required empty public constructor
    }

    public static RewardFragment newInstance() {
        RewardFragment fragment = new RewardFragment();
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
        View rootview =  inflater.inflate(R.layout.fragment_reward, container, false);
        initTitle();
        initView(rootview);
        return  rootview;
    }
    private void initTitle(){
        mTitle.add("奖励一下");
    }
    private void initView(View rootview){
        tabLayout = rootview.findViewById(R.id.tab_layout);
        viewPager2 = rootview.findViewById(R.id.view_pager2);
        RewardFragment.RewardAdapter rewardAdapter = new RewardFragment.RewardAdapter(getActivity(),mTitle);
        ///有笨蛋！RUN的时候点击会闪退！竟然忘记关联适配器！笑拥咧
        viewPager2.setAdapter(rewardAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mTitle.get(position));
            }
        }).attach();
    }
    public  class RewardAdapter extends FragmentStateAdapter{
        private List<String> mTitle;

        public RewardAdapter(FragmentActivity activity, List<String> title) {
            super(activity);
            this.mTitle = title;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            String title = mTitle.get(position);
            if (title.equals("奖励一下")) {
                return TotalRewardFragment.newInstance();
            }
            else {
                return null;
            }
        }

        @Override
        public int getItemCount() {
            return mTitle.size();
        }
    }

}
