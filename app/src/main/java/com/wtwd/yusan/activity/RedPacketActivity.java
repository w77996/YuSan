package com.wtwd.yusan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wtwd.yusan.R;
import com.wtwd.yusan.base.CommonToolBarActivity;
import com.wtwd.yusan.entity.ResultEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2018/4/14 0014.
 */

public class RedPacketActivity extends CommonToolBarActivity {
    public static final int RED_PACKET_RESULT_CODE = 0x02;

    private RecyclerView recycler_red_packet;

    private Button btn_red_packet_commit;
    private TextView text_task_cost;

    private int[] mDrawables = {R.mipmap.redpacket_yue, R.mipmap.redpacket_zhifubao, R.mipmap.redpacket_wechat};

    //    private String[] mRedPacketName = {"钱包余额(%1.2f)", "支付宝", "微信"};
    private String[] mRedPacketName;

    private List<RedPacketRecyclerEntity> mList = new ArrayList<>();

    private RedPacketAdapter mRedPacketAdapter;


    @Override
    public void onCreateCommonView(Bundle saveInstanceState) {
        mRedPacketName = getResources().getStringArray(R.array.payment);
        initView();
        getDate();
    }

    private void getDate() {
        for (int i = 0; i < mRedPacketName.length; i++) {
            RedPacketRecyclerEntity redPacketRecyclerEntity = new RedPacketRecyclerEntity();
            if (0 == i) {

                redPacketRecyclerEntity.setmRedPacketName(String.format(mRedPacketName[0], new Random().nextInt(10) + new Random().nextDouble()));
            } else {
                redPacketRecyclerEntity.setmRedPacketName(mRedPacketName[i]);
            }
            redPacketRecyclerEntity.setmDrawableId(mDrawables[i]);
            mList.add(redPacketRecyclerEntity);
        }
//        recycler_red_packet.removeItemDecorationAt(mList.size()-1);
        mList.get(0).setCheck(true);
        mRedPacketAdapter.notifyDataSetChanged();
    }

    private void initView() {
        text_tool_bar_title.setText(R.string.red_packet_title);
        text_task_cost = (TextView) findViewById(R.id.text_task_cost);


        btn_red_packet_commit = (Button) findViewById(R.id.btn_red_packet_commit);
        recycler_red_packet = (RecyclerView) findViewById(R.id.recycler_red_packet);
        recycler_red_packet.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRedPacketAdapter = new RedPacketAdapter(R.layout.item_red_packet_type, mList);
        recycler_red_packet.setAdapter(mRedPacketAdapter);
        DividerItemDecoration mDi = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mDi.setDrawable(ContextCompat.getDrawable(this, R.drawable.shape_line));
        recycler_red_packet.addItemDecoration(mDi);

        mRedPacketAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("dd", "ddd" + position);
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setCheck(false);
                }
                mList.get(position).setCheck(true);
//                recycler_red_packet.removeItemDecorationAt(mList.size()-1);
                mRedPacketAdapter.notifyDataSetChanged();
            }
        });

        addListener();
    }

    private void addListener() {
        btn_red_packet_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("money", text_task_cost.getText().toString());
                intent.putExtras(bundle);
                setResult(RED_PACKET_RESULT_CODE, intent);
                finish();
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_red_packet;
    }

    @Override
    public View getSnackView() {
        return null;
    }


    private class RedPacketRecyclerEntity {
        private int mDrawableId;
        private String mRedPacketName;
        private boolean IsCheck;

        public int getmDrawableId() {
            return mDrawableId;
        }

        public void setmDrawableId(int mDrawableId) {
            this.mDrawableId = mDrawableId;
        }

        public String getmRedPacketName() {
            return mRedPacketName;
        }

        public void setmRedPacketName(String mRedPacketName) {
            this.mRedPacketName = mRedPacketName;
        }

        public boolean isCheck() {
            return IsCheck;
        }

        public void setCheck(boolean check) {
            IsCheck = check;
        }
    }

    private class RedPacketAdapter extends BaseQuickAdapter<RedPacketRecyclerEntity, BaseViewHolder> {


        public RedPacketAdapter(int layoutResId, @Nullable List<RedPacketRecyclerEntity> data) {
            super(layoutResId, data);

        }

        @Override
        protected void convert(BaseViewHolder helper, RedPacketRecyclerEntity item) {
            helper.setImageDrawable(R.id.img_redpacket_head, ContextCompat.getDrawable(RedPacketActivity.this, item.getmDrawableId()))
                    .setText(R.id.tv_redpacket_text, item.getmRedPacketName())
                    .setChecked(R.id.ck_redpacket, item.isCheck());
        }
    }
}
