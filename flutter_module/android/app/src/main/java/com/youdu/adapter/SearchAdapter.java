package com.youdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.youdu.R;
import com.youdu.module.BaseModel;
import com.youdu.module.search.ProductModel;

import java.util.ArrayList;


/**********************************************************
 * @文件名称：FundListAdapter.java
 * @文件作者：renzhiqiang
 * @创建时间：2015年6月11日 下午2:21:33
 * @文件描述：基金列表适配器
 * @修改历史：2015年6月11日创建初始版本
 **********************************************************/
public class SearchAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<BaseModel> mListData;
    private ViewHolder holder;
    private LayoutInflater inflater;

    public SearchAdapter(Context context, ArrayList<BaseModel> listData) {
        this.mContext = context;
        this.mListData = listData;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_search_layout, null);
            holder.fundNameView = (TextView) convertView.findViewById(R.id.fund_name_view);
            holder.fundCodeView = (TextView) convertView.findViewById(R.id.fund_code_view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 根据数据初始化item
        ProductModel fundSearch = (ProductModel) getItem(position);
        holder.fundNameView.setText(fundSearch.abbrev);
        holder.fundCodeView.setText(fundSearch.fdcode);
        return convertView;
    }

    public void updateData(ArrayList<BaseModel> listData) {
        this.mListData = listData;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private TextView fundNameView;
        private TextView fundCodeView;
    }
}
