package com.jnu.student.Task_Reward_Details;

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

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.jnu.student.DataKeep.GainScoreDataBank;
import com.jnu.student.DataKeep.SDataBank;
import com.jnu.student.DataKeep.STaskDetailsActivity;
import com.jnu.student.DataKeep.ShortTask;
import com.jnu.student.MainFragments.TaskFragment;
import com.jnu.student.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class ShortTaskFragment extends Fragment implements OnDataChangeListener{

    private  double TotalScore;
    private double gainScore;
    private ArrayList<ShortTask> tasks = new ArrayList<>();
    private TaskAdapter taskAdapter;
    private SDataBank  sDataBank = new SDataBank();//短期目标的保存
    private GainScoreDataBank gainScoreDataBank = new GainScoreDataBank();//获得分数的保存
    ActivityResultLauncher<Intent> addLauncher;
    ActivityResultLauncher<Intent> updateLauncher;
    Button add_button;
    Button done_button;
    public ShortTaskFragment() {
        // Required empty public constructor
    }

    public static ShortTaskFragment newInstance() {
        ShortTaskFragment fragment = new ShortTaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View rootView =  inflater.inflate(R.layout.fragment_short_task, container, false);
       RecyclerView recyclerView = rootView.findViewById(R.id.recycle_view_tasks);
       //布局管理器
       recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
       tasks = sDataBank.addTasks(requireActivity());
       TotalScore = sDataBank.addTotalScore(requireActivity()) ;
       taskAdapter = new TaskAdapter(tasks);
       recyclerView.setAdapter(taskAdapter);//配置适配器
        taskAdapter.setOnDataChangeListener(this);
        registerForContextMenu(recyclerView);//注册上下文菜单
        add_button = rootView.findViewById(R.id.button_add);
        done_button = rootView.findViewById(R.id.button_done);
        // ////////////////////更新/////////////////////
        updateLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            int position = data.getIntExtra("position", -1);
                            String name = data.getStringExtra("name");
                            String scoreText = data.getStringExtra("score");
                            assert scoreText != null;
                            double score = Double.parseDouble(scoreText);
                            ///////////////////////////////////////////
                            //获取数据
                            ShortTask shortTask = tasks.get(position);
                            shortTask.setTitle(name);
                            shortTask.setScore(score);
                            taskAdapter.notifyItemChanged(position);
                            //存一下
                            new SDataBank().SaveTasks(requireActivity(), tasks);
                        }
                    }
                });

        ///////////添加////////////////////
        addLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String name = data.getStringExtra("name");
                            String scoreText = data.getStringExtra("score");

                            assert scoreText != null;
                            double score = Double.parseDouble(scoreText);
                            tasks.add(new ShortTask(name, score));
                            //更新存储的数据
                            taskAdapter.notifyItemInserted(tasks.size());
                            new SDataBank().SaveTasks(requireActivity(), tasks);
                        }
                    }
                });
        /////////////////////点击添加按钮事件响应/////////
        boolean isAtLeastOneChecked = false;
        for (ShortTask s : tasks) {
            if (s.isSelected()) {
                isAtLeastOneChecked = true;
                break;
            }
        }
        //如果点击添加按钮，就跳转到短期计划添加界面
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), STaskDetailsActivity.class);
                addLauncher.launch(intent);
            }
        });

        /////////处理最后去掉已完成项的按钮点击响应//////////
        done_button.setVisibility(isAtLeastOneChecked ? View.VISIBLE : View.GONE);
        done_button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setMessage("确定完成这项任务了吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击确认按钮，执行删除数据的操作
                        Iterator<ShortTask> iterator = tasks.iterator();
                        boolean dataChanged = false;
                        while (iterator.hasNext()) {
                            ShortTask shortTask = iterator.next();//获取当前某项
                            if (shortTask.isSelected()) {
                                double score = shortTask.getScore();
                                TotalScore += score;
                                gainScore += score;
                                iterator.remove(); // 在使用Iterator遍历时移除元素
                                dataChanged = true;
                            }
                        }

                        if (dataChanged) {
                            // 更新成就点数和总得分
                            gainScoreDataBank = new GainScoreDataBank();
                            gainScoreDataBank.WalletSave(requireActivity(), gainScore);
                            sDataBank.SaveTotalScore(requireActivity(), TotalScore);
                            sDataBank.SaveTasks(requireActivity(), tasks);
                            // 通知适配器数据发生了变化
                        }
                        taskAdapter.notifyDataSetChanged();
                        done_button.setVisibility(View.GONE);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击取消按钮，不执行删除数据的操作
                        // 可以执行其他适当的操作或返回先前的界面

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });



                return rootView;
            }

            @Override
            //重写监听函数
            public void onDataChanged() {
                if (isAdded()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boolean isAtLeastOneChecked = false;
                            for (ShortTask s : tasks) {
                                if (s.isSelected()) {
                                    isAtLeastOneChecked = true;
                                    break;
                                }
                            }

                            // 只有当用户完成这个任务的时候才会问是否要删除，如果没有完成也没有必要问
                            done_button.setVisibility(isAtLeastOneChecked ? View.VISIBLE : View.GONE);
                        }
                    });
                }

            }
    public boolean onContextItemSelected(MenuItem item) {//上下文菜单添加
        if (item.getGroupId() != 11) {
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case 0:

                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("确认完成");
                builder.setMessage("确认完成了吗？");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ShortTask s = tasks.get(item.getOrder());
                        double score = s.getScore();
                        TotalScore += score;
                        //更新钱包中的数据
                        gainScoreDataBank = new GainScoreDataBank();
                        gainScore = gainScoreDataBank.WalletLoad(requireActivity());
                        gainScore += score;
                        gainScoreDataBank.WalletSave(requireActivity(),gainScore);
                        //更新task
                        tasks.remove(item.getOrder());
                        taskAdapter.notifyItemRemoved(item.getOrder());
                        sDataBank.SaveTotalScore(requireActivity(), TotalScore);
                        sDataBank.SaveTasks(requireActivity(), tasks);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击取消按钮，不执行删除数据的操作
                        // 可以执行其他适当的操作或返回先前的界面

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case 1:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(requireActivity());
                builder1.setTitle("确认删除");
                builder1.setMessage("确认删除数据吗？");
                builder1.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 用户点击确认按钮，执行删除数据的操作
                        tasks.remove(item.getOrder());
                        taskAdapter.notifyItemRemoved(item.getOrder());
                        new SDataBank().SaveTasks(requireActivity(), tasks);

                    }
                });
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
            // 可以继续添加其他的菜单项处理逻辑

            case 2:
                Intent intentUpdate = new Intent(requireActivity(), STaskDetailsActivity.class);
                ShortTask s = tasks.get(item.getOrder());
                intentUpdate.putExtra("name", s.getTitle());
                intentUpdate.putExtra("score", s.getScore());
                intentUpdate.putExtra("position", item.getOrder());

                updateLauncher.launch(intentUpdate);
                break;
            default:
                return super.onContextItemSelected(item);
        }

        return false;
    }


            public static class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
                private OnDataChangeListener onDataChangeListener;
                private final ArrayList<ShortTask> t;
                public TaskAdapter(ArrayList<ShortTask> _t){
                    t = _t;
                }

                public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {

                    this.onDataChangeListener = onDataChangeListener;
                }

                @NonNull
                @Override
                public TaskAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.short_task_row, parent, false);

                    return new TaskAdapter.ViewHolder(view);
                }

                @Override//数据绑定
                public void onBindViewHolder(@NonNull TaskAdapter.ViewHolder holder, int position) {
                    holder.getTextViewTitle().setText(t.get(position).getTitle());
                    holder.getTextViewScore().setText("+ "+t.get(position).getScore());

                    ShortTask task = t.get(position);
                    holder.checkBox.setOnCheckedChangeListener(null); // 避免重用视图时触发事件
                    holder.checkBox.setChecked(task.isSelected());    // 根据当前状态设置复选框
                    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            task.setSelected(isChecked);
                            //wflbb,又看错变量
                            new SDataBank().SaveTasks(buttonView.getContext(),t);
                            if (onDataChangeListener != null) {
                                onDataChangeListener.onDataChanged();
                            }

                        }
                    });
                }

                @Override
                public int getItemCount() {
                    return t.size();
                }

                public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
                    private final CheckBox checkBox;
                    private final TextView textViewTitle;
                    private final TextView textViewScore;
                    public ViewHolder(@NonNull View itemView) {
                        super(itemView);
                        checkBox = itemView.findViewById(R.id.checkbox_task);
                        textViewTitle = itemView.findViewById(R.id.task_title);
                        textViewScore = itemView.findViewById(R.id.task_score);
                        itemView.setOnCreateContextMenuListener(this);
                    }

                    @Override
                    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                        contextMenu.setHeaderTitle("具体操作");
                        contextMenu.add(11, 0, this.getAdapterPosition(), "完成");
                        contextMenu.add(11, 1, this.getAdapterPosition(), "删除");
                        contextMenu.add(11, 2, this.getAdapterPosition(), "修改");
                    }

                    public TextView getTextViewTitle() {
                        return textViewTitle;
                    }

                    public TextView getTextViewScore() {
                        return textViewScore;
                    }
                }
            }
}