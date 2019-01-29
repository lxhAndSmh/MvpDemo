package com.liu.mvpdemo.adapter.clicklistener;

import com.liu.mvpdemo.bean.Task;

/**
 * @author liuxuhui
 * @date 2019/1/28
 */
public interface MainRecyclerClickListener extends RecyclerItemClickListener {

    void onCompleteTaskClick(Task completedTask);

    void onActivateTaskClick(Task activatedTask);

    void onOpenTaskDetialClick(Task detailTask);
}
