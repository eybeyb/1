package com.thundersoft.trash.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thundersoft.trash.Model.TrashResponse;
import com.thundersoft.trash.R;
import com.thundersoft.trash.TrashDetailActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SearchGoodsAdapter extends RecyclerView.Adapter<SearchGoodsAdapter.SearchGoodsViewHolder> {

    private List<TrashResponse.TrashItem> dataList = new ArrayList<>();

    public void setDataList(List<TrashResponse.TrashItem> dataList) {
        if (dataList != null) {
            this.dataList = dataList;
        } else {
            this.dataList.clear();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchGoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trash, parent, false);
        return new SearchGoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchGoodsViewHolder holder, int position) {
        TrashResponse.TrashItem item = dataList.get(position);

        // 设置垃圾名称
        holder.nameTextView.setText("物品名称: " + (item.getName() != null ? item.getName() : "未知"));

        // 设置分类说明
        holder.explainTextView.setText("分类说明: " + (item.getExplain() != null ? item.getExplain() : "暂无说明"));

        // 设置包含内容
        holder.containTextView.setText("包含内容: " + (item.getContain() != null ? item.getContain() : "暂无信息"));

        // 设置投放提示
        holder.tipTextView.setText("投放提示: " + (item.getTip() != null ? item.getTip() : "暂无提示"));

        // 设置分类类型
        String category = "未知分类";
     if (item.getType() >= 0) {
            switch (item.getType()) {
                case 0:
                    category = "可回收物";
                    break;
                case 1:
                    category = "有害垃圾";
                    break;
                case 2:
                    category = "厨余垃圾";
                    break;
                case 3:
                    category = "其他垃圾";
                    break;
                default:
                    category = "未知分类";
                    break;
            }
        }
        holder.typeTextView.setText("分类类型: " + category);
     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(v.getContext(), TrashDetailActivity.class);
             intent.putExtra(TrashDetailActivity.EXTRA_TRASH_ITEM, item);
           v.getContext().startActivity(intent);
         }
     });
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    static class SearchGoodsViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView explainTextView;
        TextView containTextView;
        TextView tipTextView;
        TextView typeTextView;

        public SearchGoodsViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            explainTextView = itemView.findViewById(R.id.explainTextView);
            containTextView = itemView.findViewById(R.id.containTextView);
            tipTextView = itemView.findViewById(R.id.tipTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
        }
    }
}
