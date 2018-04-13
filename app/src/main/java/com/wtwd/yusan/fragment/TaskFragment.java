package com.wtwd.yusan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.wtwd.yusan.R;
import com.wtwd.yusan.adapter.TaskAdapter;
import com.wtwd.yusan.base.BaseFragment;
import com.wtwd.yusan.entity.TaskEntity;
import com.wtwd.yusan.util.Constans;
import com.wtwd.yusan.widget.recycler.EasyRefreshLayout;
import com.wtwd.yusan.widget.recycler.LoadModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;

/**
 * Created by Administrator on 2018/4/9 0009.
 */

public class TaskFragment extends BaseFragment {
    private static TaskFragment mInstance;
    private RecyclerView recycler_task;
    private EasyRefreshLayout easy_layout;
    private List<TaskEntity> mTaskEntitys = new ArrayList<>();
    private TaskAdapter mAdapter;

    public TaskFragment() {

    }

    public static TaskFragment getTaskFragment() {
        if (null == mInstance) {
            mInstance = new TaskFragment();
        }
        return mInstance;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_task;
    }

    @Override
    public void initFragmentView(Bundle savedInstanceState, View mView) {
        easy_layout = (EasyRefreshLayout) mView.findViewById(R.id.easy_layout);
        recycler_task = (RecyclerView) mView.findViewById(R.id.recycler_task);
        recycler_task.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration mDi = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        mDi.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.shape_line));
        recycler_task.addItemDecoration(mDi);

        mAdapter = new TaskAdapter(R.layout.item_fragment_task, null);
        recycler_task.setAdapter(mAdapter);
        getData();

        initListener();
    }

    /**
     * 从服务器获取指定的任务信息
     *
     * @param startCount 开始条数
     * @param count      一次获取几条消息
     */
    private void getAllMission(int startCount, int count) {
        Map<String, String> mStartCount = new HashMap<>();
        mStartCount.put("start", startCount + "");
        mStartCount.put("count", count + "");

        OkHttpUtils.get()
                .params(mStartCount)
                .build()
                .connTimeOut(Constans.TIME_OUT)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {



                    }
                });

    }


    private void getData() {
        mTaskEntitys.clear();

        mAdapter.getData().addAll(mTaskEntitys);
        mAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        easy_layout.setLoadMoreModel(LoadModel.ADVANCE_MODEL, 5);
        easy_layout.addEasyEvent(new EasyRefreshLayout.EasyEvent() {
            @Override
            public void onLoadMore() {

                final List<TaskEntity> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    TaskEntity mEn = new TaskEntity();

                    list.add(mEn);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        easy_layout.loadMoreComplete();
//                        easy_layout.closeLoadView();
                        int postion = mAdapter.getData().size();
                        mAdapter.getData().addAll(list);
                        mAdapter.notifyDataSetChanged();
                        recycler_task.scrollToPosition(postion);
                    }
                }, 500);

            }

            @Override
            public void onRefreshing() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<TaskEntity> list = new ArrayList<>();
                        for (int i = 0; i < 20; i++) {
                            TaskEntity mEn = new TaskEntity();

                            list.add(mEn);
                        }
                        mAdapter.setNewData(list);
                        easy_layout.refreshComplete();
//                        Toast.makeText(getApplicationContext(), "refresh success", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);

            }
        });
    }


}
