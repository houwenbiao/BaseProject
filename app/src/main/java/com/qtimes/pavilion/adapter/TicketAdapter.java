package com.qtimes.pavilion.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qtimes.pavilion.R;
import com.qtimes.pavilion.base.adapter.ListBaseAdapter;
import com.qtimes.pavilion.base.adapter.SuperViewHolder;
import com.qtimes.pavilion.bean.TicketBean;
import com.qtimes.pavilion.views.swipe.SwipeMenuView;

/**
 * Author: JackHou
 * Date: 2019/3/4.
 */
public class TicketAdapter extends ListBaseAdapter<TicketBean> {

    private Context mContext;

    public TicketAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.ticket_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, final int position) {
        View contentView = holder.getView(R.id.swipe_ticket);
        TextView ticketId = holder.getView(R.id.ticket_id);
        TextView ticketType = holder.getView(R.id.ticket_type);
        TextView ticketStatus = holder.getView(R.id.ticket_status);
        Button btnDelete = holder.getView(R.id.btnDelete);

        TicketBean ticketBean = getDataList().get(position);

        //这句话关掉IOS阻塞式交互效果 并依次打开左滑右滑
        ((SwipeMenuView) holder.itemView).setIos(false).setLeftSwipe(true);
        ticketId.setText("No" + ticketBean.getTicketId());

        btnDelete.setOnClickListener(v -> {
            if (null != mOnSwipeListener) {
                //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                //((CstSwipeDelMenu) holder.itemView).quickClose();
                mOnSwipeListener.onDel(position);
            }
        });
        //注意事项，设置item点击，不能对整个holder.itemView设置咯，只能对第一个子View，即原来的content设置，这算是局限性吧。
        contentView.setOnClickListener(v -> {
            if (null != mOnSwipeListener) {
                mOnSwipeListener.onItemClick(position);
            }
        });
    }

    /**
     * 和Activity通信的接口
     */
    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);

        void onItemClick(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public void setOnSwipeListener(onSwipeListener mOnSwipeListener) {
        this.mOnSwipeListener = mOnSwipeListener;
    }
}
