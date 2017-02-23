package com.liu.mvpdemo.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.liu.mvpdemo.R;
import com.liu.mvpdemo.contracts.TaskDetailContract;
import com.liu.mvpdemo.data.TasksDataManager;
import com.liu.mvpdemo.data.local.TasksLocalDataSource;
import com.liu.mvpdemo.presenters.TaskDetailPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 项目名称：TaskDetailActivity
 * 类描述：任务详情页的View
 * 创建人：liuxuhui
 * 创建时间：2017/2/23 下午10:58
 * 修改人：liuxuhui
 * 修改时间：2017/2/23 下午10:58
 * 修改备注：
 */
public class TaskDetailActivity extends AppCompatActivity implements TaskDetailContract.View{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.task_detail_title)
    TextView tvTitle;
    @BindView(R.id.task_detail_description)
    TextView tvDecription;
    @BindView(R.id.task_detail_complete)
    CheckBox checkBoxComplete;

    private TaskDetailContract.Presenter mPresenter;
    private TasksDataManager manager;

    private String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        ButterKnife.bind(this);

        toolbar.setTitle("任务详情页");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        initData();
    }

    private void initData(){
        taskId = getIntent().getStringExtra("taskId");
        manager = TasksDataManager.getInstance(TasksLocalDataSource.getInstance(this));
        mPresenter = new TaskDetailPresenter(manager, this);
        mPresenter.setTaskId(taskId);
        mPresenter.setDeatil();

        checkBoxComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    mPresenter.setTaskCompleted(taskId);
                }else {
                    mPresenter.setTaskActived(taskId);
                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:
                finish();
                break;
            case R.id.menu_delete:
                mPresenter.deleteTask();
                break;
        }
        return true;
    }

    @Override
    public void showTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void showDescription(String description) {
        tvDecription.setText(description);
    }

    @Override
    public void showCheckBox(boolean isCompleted) {
        checkBoxComplete.setChecked(isCompleted);
    }

    @Override
    public void toTaskLists() {
        finish();
    }

    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {

    }
}
