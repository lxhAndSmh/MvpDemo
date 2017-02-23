package com.liu.mvpdemo.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liu.mvpdemo.R;
import com.liu.mvpdemo.adapter.clicklistener.MainRecyclerClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：MvpDemo
 * 类描述：
 * 创建人：liuxuhui
 * 创建时间：2017/2/20 18:50
 * 修改人：liuxuhui
 * 修改时间：2017/2/20 18:50
 * 修改备注：
 */

public class MainViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.complete)
    public CheckBox checkBox;
    @BindView(R.id.title)
    public TextView tvTitle;
    @BindView(R.id.linearLayout)
    public LinearLayout linearLayout;

    public MainViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}
