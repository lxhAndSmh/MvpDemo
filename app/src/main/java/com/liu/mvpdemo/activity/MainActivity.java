package com.liu.mvpdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.adapter.MainRecyclerAdapter;
import com.liu.mvpdemo.adapter.clicklistener.MainRecyclerClickListener;
import com.liu.mvpdemo.bean.Task;
import com.liu.mvpdemo.constant.TasksFilterType;
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
    private static final String TAG = MainActivity.class.getSimpleName();
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
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    private MainContract.Presenter mPresenter;
    private TasksDataSource mDataManager;

    private MainRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

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
            }

            @Override
            public void onActivateTaskClick(Task activatedTask) {
                mPresenter.activateTask(activatedTask);
            }

            @Override
            public void onOpenTaskDetialClick(Task detailTask) {
                mPresenter.openTaskDetails(detailTask);
            }

            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "=====item====" + position, Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_clear:
                mPresenter.cleanCompletedTasks();
                break;
            case R.id.menu_refresh:
                mPresenter.loadTasks(false);
                break;
        }
        return true;
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
        recyclerView.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        adapter.replaceData(tasks);
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void showTaskDetailsUi(String taskId) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra("taskId", taskId);
        startActivity(intent);
    }

    @Override
    public void showLoadingTasksError() {

    }

    @Override
    public void showNoTasks() {
        showEmptyMessage("暂时没有任何任务哦");
    }

    @Override
    public void showNoActivedTasks() {
        showEmptyMessage("暂时没有待完成的任务哦");
    }

    @Override
    public void showNoCompletedTasks() {
        showEmptyMessage("暂时没有已完成的任务哦");
    }

    /**
     * 过滤条件的Menu弹窗
     */
    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popMenu = new PopupMenu(this, findViewById(R.id.menu_filter));
        popMenu.getMenuInflater().inflate(R.menu.filter_tasks, popMenu.getMenu());

        popMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.all:
                        mPresenter.setFiltering(TasksFilterType.ALL_TASKS);
                        break;
                    case R.id.active:
                        mPresenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
                        break;
                    case R.id.completed:
                        mPresenter.setFiltering(TasksFilterType.COMPLETE_TASKS);
                        break;
                }
                mPresenter.loadTasks(false);
                return true;
            }
        });
        popMenu.show();
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

    private void showEmptyMessage(String emptyMessage){
        recyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
        tvEmpty.setText(emptyMessage);
    }
}
