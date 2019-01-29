package com.liu.mvpdemo.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liu.mvpdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author liuxuhui
 * @date 2019/1/28
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
