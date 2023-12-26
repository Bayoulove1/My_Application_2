package com.jnu.student.MainFragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jnu.student.DataKeep.GainScoreDataBank;
import com.jnu.student.R;

public class WalletFragment extends Fragment {
    private double TotalScore;
    private TextView textViewScore;
    Button zero;
    double ToZero;
    private GainScoreDataBank gainScoreDataBank = new GainScoreDataBank();//获得分数的保存
    private Handler handler = new Handler();

    public WalletFragment() {
        // Required empty public constructor
    }


    public static WalletFragment newInstance() {
        WalletFragment fragment = new WalletFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
        textViewScore = rootView.findViewById(R.id.text_wallet_data);//没问题这里，因为要获得的是分数显示的数字
        //简简单单实现归零操作，有时候是需要归零的
        zero = rootView.findViewById(R.id.button_zero);
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TotalScore = 0.0;
                gainScoreDataBank = new GainScoreDataBank();
                gainScoreDataBank.WalletSave(requireActivity(), TotalScore);
                textViewScore.setText(String.valueOf(TotalScore));
            }
        });
        return rootView;
    }

    public void onResume(){
        super.onResume();
        startTextViewUpdate();
    }
    //6,第一次遇到写函数忘记加括号的笨蛋，高，实在是高
    private void startTextViewUpdate(){
        handler.post(updateScore);
    }
    public void onPause(){
        super.onPause();
        stopTextViewUpdate();
    }

    private void stopTextViewUpdate(){
        handler.removeCallbacks(updateScore);
    }

    //通过运行时间更新内容（很巧妙）
    private Runnable updateScore = new Runnable() {
        @Override
        public void run() {
           TotalScore = new GainScoreDataBank().WalletLoad(requireActivity());
           textViewScore.setText(String.valueOf(TotalScore));
           //更新的时间间隔
            handler.postDelayed(this,1000);

        }
    };

}