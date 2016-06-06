package com.zw.yzk.pulltorefresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wei on 2016/2/22.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> itemList;
    private Context context;

    public MyAdapter(List<String> list) {
        itemList = new ArrayList<>();
        if (list != null) {
            itemList.addAll(list);
        }
    }

    public void refresh(List<String> list) {
        if (itemList != null) {
            itemList.clear();
        } else {
            itemList = new ArrayList<>();
        }
        if (list != null) {
            itemList.addAll(list);
        }
    }

    public void add(List<String> list) {
        if (itemList == null) {
            itemList = new ArrayList<>();
        }
        int start = itemList.size();
        if (list != null) {
            itemList.addAll(list);
            notifyItemRangeInserted(start, list.size());
        }
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_test, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, final int position) {
        holder.text.setText(itemList.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "item " + position + " clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
