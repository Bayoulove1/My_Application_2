package com.jnu.student.MainFragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.jnu.student.DataKeep.GainScoreDataBank;
import com.jnu.student.DataKeep.Reward;
import com.jnu.student.DataKeep.RewardDataBank;
import com.jnu.student.DataKeep.STaskDetailsActivity;
import com.jnu.student.R;
import com.jnu.student.Task_Reward_Details.OnDataChangeListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RewardFragment extends Fragment implements OnDataChangeListener {
    private ArrayList<Reward> rewards = new ArrayList<>();
    private double TotalScore;
    private double GainScore;
    private RewardAdapter rewardAdapter;
    private GainScoreDataBank gainScoreDataBank = new GainScoreDataBank();
    private RewardDataBank rewardDataBank = new RewardDataBank();
    Button add_button;
    Button done_button;
    ActivityResultLauncher<Intent> addLauncher;
    ActivityResultLauncher<Intent> updateLauncher;
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
        RecyclerView recyclerView = rootview.findViewById(R.id.recycle_view_rewards);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        rewards = new RewardDataBank().AddReward(requireActivity());
        TotalScore = new RewardDataBank().AddTotalScore(requireActivity());
        rewardAdapter = new RewardFragment.RewardAdapter(rewards);
        recyclerView.setAdapter(rewardAdapter);
        rewardAdapter.setOnDataChangeListener(this);
        //注册
        registerForContextMenu(recyclerView);
        add_button = rootview.findViewById(R.id.button_add);
        done_button = rootview.findViewById(R.id.button_done);
        boolean isAtLeastOneChecked = false;
        for (Reward r : rewards) {
            if (r.Select()) {
                isAtLeastOneChecked = true;
                break;
            }
        }
        addLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // 检查返回结果是否成功
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // 获取返回的 Intent 数据
                        Intent data = result.getData();
                        if (data != null) {
                            // 从 Intent 中获取名称和分数
                            String name = data.getStringExtra("name");
                            String scoreText = data.getStringExtra("score");
                            // 确保分数文本不为 null，然后将其转换为 double 类型
                            assert scoreText != null;
                            double score = Double.parseDouble(scoreText);
                                rewards.add(new Reward(name, score));
                                rewardAdapter.notifyItemInserted(rewards.size());
                                new RewardDataBank().SaveReward(requireActivity(), rewards);
                        }
                    }
                    // 处理取消操作
                });
        updateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    // 检查返回结果是否成功
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // 获取返回的 Intent 数据
                        Intent data = result.getData();
                        if (data != null) {
                            // 从 Intent 中获取位置、名称和分数
                            int position = data.getIntExtra("position", -1);
                            String name = data.getStringExtra("name");
                            String scoreText = data.getStringExtra("score");

                            // 确保分数文本不为 null，然后将其转换为 double 类型
                            assert scoreText != null;
                            double score = Double.parseDouble(scoreText);
                                // 获取对应位置的奖励项目，并更新其标题和分数
                                Reward r = rewards.get(position);
                                r.setName(name);
                                r.setScore(score);
                                // 通知适配器更新指定位置的奖励项目数据
                                rewardAdapter.notifyItemChanged(position);
                                // 保存更新后的奖励项目数据到本地数据存储
                                new RewardDataBank().SaveReward(requireActivity(), rewards);

                        }
                    }
                });

        done_button.setVisibility(isAtLeastOneChecked ? View.VISIBLE : View.GONE);
        add_button .setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //先这么写着，看看等一下加载效果如何，如果效果不错就依然这样写，不行再尝试添加新的RewardDetails
                Intent intent = new Intent(requireActivity(), STaskDetailsActivity.class);
                addLauncher.launch(intent);
            }
        });
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setMessage("确定完成这个奖励了吗？");
                // 设置确认按钮的点击事件
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Iterator<Reward> iterator = rewards.iterator();
                        boolean daChanged = false;
                        while (iterator.hasNext()) {
                            Reward r = iterator.next();
                            if (r.Select()) {
                                double score = r.getScore(); // 加载当前的分数
                                TotalScore -= score; // 加到总分上去
                                GainScore -= score;
                                iterator.remove(); // 使用迭代器安全删除
                                daChanged = true;
                            }
                        }
                        if(daChanged){
                            gainScoreDataBank = new GainScoreDataBank();
                            gainScoreDataBank.WalletSave(requireActivity(),GainScore);
                            rewardDataBank.SaveTotalScore(requireActivity(),TotalScore);
                            rewardDataBank.SaveReward(requireActivity(),rewards);
                        }

                        rewardAdapter.notifyDataSetChanged(); // 通知适配器数据发生了变化
                        //显示完成的按钮
                        done_button.setVisibility(View.GONE);
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 无需执行任何操作
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return  rootview;
    }

    @Override
    public void onDataChanged() {
        if (isAdded()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    boolean isAtLeastOneChecked = false;
                    for (Reward r : rewards) {
                        if (r.Select()) {
                            isAtLeastOneChecked = true;
                            break;
                        }
                    }

                    // 根据是否有条目被选中来显示或隐藏按钮
                    done_button.setVisibility(isAtLeastOneChecked ? View.VISIBLE : View.GONE);
                }
            });
        }

    }

    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() != 21) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case 0:
                // 创建确认完成的对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("确认完成");
                builder.setMessage("确认完成了吗？");
                // 设置确认按钮的点击事件
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击确认按钮，执行完成任务并更新数据
                        Reward r = rewards.get(item.getOrder());//获取当前实例
                        double score = r.getScore();//加载当前的分数
                        TotalScore += score;//加到总分上去
                        new RewardDataBank().SaveTotalScore(requireActivity(), TotalScore);//存储
                        rewards.remove(item.getOrder());//删除当前实例
                        rewardAdapter.notifyItemRemoved(item.getOrder());//刷新
                        new RewardDataBank().SaveReward(requireActivity(), rewards);///保存
                    }
                });
                // 设置取消按钮的点击事件
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击取消按钮，不执行操作
                        // 可以执行其他适当的操作或返回先前的界面
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case 1:
                // 创建确认删除的对话框
                AlertDialog.Builder builder1 = new AlertDialog.Builder(requireActivity());
                builder1.setTitle("确认删除");
                builder1.setMessage("确认删除数据吗？");
                // 设置确认按钮的点击事件
                builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Reward r = rewards.get(item.getOrder());//获取当前实例
                        double score = r.getScore();//加载当前的分数
                        double nowScore = new GainScoreDataBank().WalletLoad(requireContext());
                        nowScore+=score;
                        new GainScoreDataBank().WalletSave(requireActivity(),nowScore);//回收成就点
                        // 用户点击确认按钮，执行删除数据的操作
                        rewards.remove(item.getOrder());
                        rewardAdapter.notifyItemRemoved(item.getOrder());
                        new RewardDataBank().SaveReward(requireActivity(), rewards);
                    }
                });
                // 设置取消按钮的点击事件
                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击取消按钮，不执行删除数据的操作
                        // 可以执行其他适当的操作或返回先前的界面
                    }
                });
                AlertDialog dialog1 = builder1.create();
                dialog1.show();
                break;
            case 2:
                // 打开编辑详情页面
                Intent intentUpdate = new Intent(requireActivity(), STaskDetailsActivity.class);
                // 传递相关数据到编辑页面
                Reward r = rewards.get(item.getOrder());
                intentUpdate.putExtra("name", r.getName());
                intentUpdate.putExtra("score", r.getScore());
                intentUpdate.putExtra("position", item.getOrder());
                double nowScore = new GainScoreDataBank().WalletLoad(requireContext());
                new GainScoreDataBank().WalletSave(requireActivity(),nowScore+r.getScore());//已有分数加上总分
                updateLauncher.launch(intentUpdate);
                break;
            default:
                return super.onContextItemSelected(item);
        }

        return false;
    }


    public static class RewardAdapter extends RecyclerView.Adapter<RewardFragment.RewardAdapter.ViewHolder>{
        private final ArrayList<Reward> r;
        public RewardAdapter(ArrayList<Reward>  data){
            r = data;
        }

        @NonNull
        @Override
        public RewardFragment.RewardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reward_row,parent,false);
            return new RewardFragment.RewardAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RewardFragment.RewardAdapter.ViewHolder holder, int position) {

            holder.getTextViewName().setText(r.get(position).getName());
            holder.getTextViewScore().setText("- "+r.get(position).getScore());

            Reward dayRewardItem = r.get(position);
            holder.checkBox.setOnCheckedChangeListener(null); // 避免重用视图时触发事件
            holder.checkBox.setChecked(dayRewardItem.Select());    // 根据当前状态设置复选框
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // 更新书本对象的状态
                    dayRewardItem.setSelect(isChecked);
                    new RewardDataBank().SaveReward(buttonView.getContext(),r);
                    // 如果有其他需要在此处处理的逻辑，比如回调，可以在这里实现
                    if (onDataChangeListener != null) {
                        onDataChangeListener.onDataChanged();
                    }

                }
            });
        }

        @Override
        public int getItemCount() {
            return r.size();
        }
        private OnDataChangeListener onDataChangeListener;
        public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
            this.onDataChangeListener = onDataChangeListener;
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
            private final TextView textViewName;
            private final TextView textViewScore;
            private final CheckBox checkBox;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                //绑定控件
                textViewName = itemView.findViewById(R.id.reward_item);
                textViewScore = itemView.findViewById(R.id.reward_score);
                checkBox = itemView.findViewById(R.id.checkbox_reward);
                //增加上下文菜单响应监听
                itemView.setOnCreateContextMenuListener(this);
            }

            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(21, 0, this.getAdapterPosition(), "完成");
                contextMenu.add(21, 1, this.getAdapterPosition(), "删除");
                contextMenu.add(21, 2, this.getAdapterPosition(), "修改");
            }
            public TextView getTextViewName() {
                return textViewName;
            }

            public TextView getTextViewScore() {
                return textViewScore;
            }
        }
    }
}
