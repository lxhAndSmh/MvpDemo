package com.liu.mvpdemo.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.contracts.AddTaskContract;
import com.liu.mvpdemo.data.TasksDataManager;
import com.liu.mvpdemo.data.TasksDataSource;
import com.liu.mvpdemo.data.local.TasksLocalDataSource;
import com.liu.mvpdemo.presenters.AddTaskPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddTaskActivity extends AppCompatActivity implements AddTaskContract.View{

    private AddTaskContract.Presenet mPresent;
    private TasksDataSource mDataManager;

    @BindView(R.id.toobar)
    Toolbar toolbar;
    @BindView(R.id.ed_title)
    EditText edTitle;
    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.fab_save)
    FloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        toolbar.setTitle("新建任务");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        mDataManager = TasksDataManager.getInstance(TasksLocalDataSource.getInstance(this));
        mPresent = new AddTaskPresenter(mDataManager, this);

        initData();
    }

    public void initData(){

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edTitle.getText().toString();
                String content = edContent.getText().toString();
                mPresent.saveTask(title, content);
            }
        });

    }

    @Override
    public void toTasksList() {
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void showEmptyError() {
        Toast.makeText(this, "请输入标题和内容", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(@NonNull AddTaskContract.Presenet presenter) {
//        mPresent = presenter;
    }
}
