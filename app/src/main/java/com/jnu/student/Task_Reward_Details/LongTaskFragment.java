package com.jnu.student.Task_Reward_Details;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.student.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LongTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LongTaskFragment extends Fragment {


    private String mParam1;
    private String mParam2;

    public LongTaskFragment() {
        // Required empty public constructor
    }


    public static LongTaskFragment newInstance() {
        LongTaskFragment fragment = new LongTaskFragment();
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
        return inflater.inflate(R.layout.fragment_long_task, container, false);
    }
}