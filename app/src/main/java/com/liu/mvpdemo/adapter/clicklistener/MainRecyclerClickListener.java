package com.liu.mvpdemo.adapter.clicklistener;

import com.liu.mvpdemo.bean.Task;

/**
 * 项目名称：MvpDemo
 * 类描述：
 * 创建人：liuxuhui
 * 创建时间：2017/2/21 12:11
 * 修改人：liuxuhui
 * 修改时间：2017/2/21 12:11
 * 修改备注：
 */

public interface MainRecyclerClickListener extends RecyclerItemClickListener {

    void onCompleteTaskClick(Task completedTask);

    void onActivateTaskClick(Task activatedTask);

    void onOpenTaskDetialClick(Task detailTask);
}
