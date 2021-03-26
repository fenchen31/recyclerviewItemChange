package com.study.recycleviewitemchange;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<LocalDataInformation> data;
    private Context context;
    private ItemClickListener listener;
    private int lastPsition = -1;//上一个被选中的item
    private volatile boolean flag = false;//是否选中

    public MyAdapter(Context context, List<LocalDataInformation> data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_main_recyclerview_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        view.setOnClickListener((View v) ->{
            listener.clickListener(holder.getLayoutPosition());
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(data.get(position).getTitle());
        holder.content.setText(data.get(position).getContent());
        if (flag){
            setChangeColor(holder);
            lastPsition = position;
            flag = false;
        }else {
            setColor(holder);
        }
    }


    public int getLastPosition(){
        return lastPsition;
    }

    private void setColor(MyViewHolder holder){
        holder.title.setTextColor(Color.BLUE);
        holder.content.setTextColor(Color.BLUE);
    }

    private void setChangeColor(MyViewHolder holder){
        holder.title.setTextColor(Color.GREEN);
        holder.content.setTextColor(Color.GREEN);
    }

    public boolean getFlag(){
        return flag;
    }

    public void setFlag(){
        flag = true;
    }

    public void setListener(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title, content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
        }
    }

    public interface ItemClickListener{
        void clickListener(int position);
    }
}
