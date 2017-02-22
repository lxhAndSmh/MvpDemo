package com.liu.mvpdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.adapter.MainRecyclerAdapter;
import com.liu.mvpdemo.adapter.clicklistener.MainRecyclerClickListener;
import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.contracts.MainContract;
import com.liu.mvpdemo.data.TasksDataManager;
import com.liu.mvpdemo.data.TasksDataSource;
import com.liu.mvpdemo.data.local.TasksLocalDataSource;
import com.liu.mvpdemo.presenters.MainPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainContract.View{

    private static final int REQUEST_CODE = 1;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.fad_add_task)
    FloatingActionButton fabAddTask;
    @BindView(R.id.filteringLabel)
    TextView tvLabel;
    @BindView(R.id.toobar)
    Toolbar toolbar;

    private MainContract.Presenter mPresenter;
    private TasksDataSource mDataManager;

    private MainRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDataManager = TasksDataManager.getInstance(TasksLocalDataSource.getInstance(this));
        mPresenter = new MainPresenter(mDataManager, this);
        initData();
    }

    public void initData(){
        adapter = new MainRecyclerAdapter(MainActivity.this, new ArrayList<Task>());
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(new MainRecyclerClickListener() {
            @Override
            public void onCompleteTaskClick(Task completedTask) {
                mPresenter.completeTask(completedTask);
                Toast.makeText(MainActivity.this, "item====" + completedTask.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onActivateTaskClick(Task activatedTask) {
                mPresenter.activateTask(activatedTask);
                Toast.makeText(MainActivity.this, "item====" + activatedTask.getTitle(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "item====" + position, Toast.LENGTH_SHORT).show();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTasks(false);
            }
        });

        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.addNewTask();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Toast.makeText(MainActivity.this, "=====刷新====", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showTasks(List<Task> tasks) {
        adapter.replaceData(tasks);
    }

    @Override
    public void showAddTask() {
        Toast.makeText(MainActivity.this, "前往编辑页====", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void showTaskDetailsUi(String taskId) {
        Toast.makeText(MainActivity.this, "前往详情页====", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showNoTasks() {
        showMessage("暂时没有任务");
    }

    @Override
    public void showFilteringPopUpMenu() {

    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    public void showAllFilterLabel() {
        showMessage("全部任务");
    }

    @Override
    public void showActiveFilterLabel() {
        showMessage("待完成的任务");
    }

    @Override
    public void showCompletedFilterLabel() {
        showMessage("已完成的任务");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(@NonNull MainContract.Presenter presenter) {
//        mPresenter = checkNotNull(presenter);
    }

    private void showMessage(String message){
        tvLabel.setText(message);
    }
}
