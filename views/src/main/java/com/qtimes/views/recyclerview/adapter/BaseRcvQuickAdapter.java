package com.qtimes.views.recyclerview.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRcvQuickAdapter<T, H extends BaseRcvAdapterHelper> extends RecyclerView.Adapter<BaseRcvAdapterHelper> implements View.OnClickListener, View.OnLongClickListener {

    protected static final String TAG = BaseRcvQuickAdapter.class.getSimpleName();

    protected final Context context;

    protected int layoutResId;

    protected final List<T> data;

    private OnItemClickListener mOnItemClickListener = null;

    private OnItemLongClickListener mOnItemLongClickListener = null;

    protected OnItemChildListener mChildItemListener = null;

    private OnItemClickListener2<T> mOnItemClickListener2 = null;

    protected MultiItemRcvTypeSupport<T> mMultiItemTypeSupport;

    protected long lastClickTime = -1;//上次点击时间
    private int OnClkickTime = 500;//默认条目两次点击间隔时间

    //define interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    //define interface
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    //define interface
    public interface OnItemChildListener {
        void onChildClick(int id, int position, Object item);
    }

    public interface OnItemClickListener2<T> {
        void onItemClick(int position, T data);
    }

    /**
     * Create a QuickAdapter.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     */
    protected BaseRcvQuickAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param context     The context.
     * @param layoutResId The layout resource id of each item.
     * @param data        A new list is created out of this one to avoid mutable list
     */
    protected BaseRcvQuickAdapter(Context context, int layoutResId, List<T> data) {
        this.data = new ArrayList<T>();
        this.context = context;
        this.layoutResId = layoutResId;
        addAll(data);
    }

    protected BaseRcvQuickAdapter(Context context, MultiItemRcvTypeSupport<T> multiItemTypeSupport) {
        this(context, multiItemTypeSupport, null);
    }

    protected BaseRcvQuickAdapter(Context context, MultiItemRcvTypeSupport<T> multiItemTypeSupport, List<T> data) {
        this.context = context;
        this.data = new ArrayList<T>();
        this.mMultiItemTypeSupport = multiItemTypeSupport;
        addAll(data);
    }


    protected void setMultiItemRcvTypeSupport(MultiItemRcvTypeSupport<T> multiItemTypeSupport) {
        this.mMultiItemTypeSupport = multiItemTypeSupport;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public T getItem(int position) {
        if (position >= data.size()) return null;
        return data.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getItemViewType(position, getItem(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public BaseRcvAdapterHelper onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        if (mMultiItemTypeSupport != null) {
            int layoutId = mMultiItemTypeSupport.getLayoutId(viewType);
            view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResId, viewGroup, false);
        }
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        BaseRcvAdapterHelper vh = new BaseRcvAdapterHelper(view);
        doOnCreateViewHolde(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(BaseRcvAdapterHelper helper, int position) {
        helper.itemView.setTag(position);
        T item = getItem(position);
        convert((H) helper, position, item);
    }

    /**
     * @param view
     */
    protected void doOnCreateViewHolde(View view) {

    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    protected abstract void convert(H helper, int position, T item);

    @Override
    public void onClick(View v) {
        long clcikTime = System.currentTimeMillis();
        if (clcikTime - lastClickTime < OnClkickTime) {
            return;
        }
        lastClickTime = clcikTime;
        if (mOnItemClickListener != null) {

            mOnItemClickListener.onItemClick(v, v.getTag() != null ? (int) v.getTag() : 0);
        }
        if (mOnItemClickListener2 != null) {
            mOnItemClickListener2.onItemClick((int) v.getTag(), getItem((int) v.getTag()));
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener2<T> listener) {
        this.mOnItemClickListener2 = listener;
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            mOnItemLongClickListener.onItemLongClick(v, (int) v.getTag());
        }
        return true;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }

    public void setOnItemChildListener(OnItemChildListener listener) {
        this.mChildItemListener = listener;
    }

    public void setOnItemListener(OnItemClickListener2<T> listener) {

    }

    public void add(T elem) {
        data.add(elem);
        notifyDataSetChanged();
    }

    /**
     * 局部插入，不会去刷新全部item
     *
     * @param elem
     */
    public void addLight(T elem) {
        data.add(elem);
        notifyItemInserted(getItemCount());
    }

    public void addAll(List<? extends T> elem) {
        if (elem == null) return;
        data.addAll(elem);
//        notifyDataSetChanged();
        notifyItemRangeInserted(data.size(), elem.size());
    }

    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        data.set(index, elem);
        notifyDataSetChanged();
    }

    public void setItem(int index, T elem) {
        data.set(index, elem);
    }

    public void remove(T elem) {
        data.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index) {
        if (data.size() <= index) {
            Log.e(TAG, "拦截");
            return;
        }
        data.remove(index);
        //notifyDataSetChanged();
        notifyItemRemoved(index);
    }

    public void replaceAll(List<? extends T> elem) {
        if (elem == null) return;
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return data.contains(elem);
    }

    /**
     * Clear data list
     */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

}