package com.study.recycleviewitemchange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button button3;
    private RecyclerView recyclerView;
    private List<LocalDataInformation> data;
    private MyAdapter adapter;
    private MyAdapter.ItemClickListener listener;
    private int lastPosition = -1;//上一个被选中item的位置
    private Thread lastThread, currectThread;//恢复上一个被选中的item以及更新当前item的Thread
    private int remotePosition = 0;//模拟接收到远端选中的item位置

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        click();
        setRecyclerView();
    }

    private void initView(){//初始化布局
        button3 = findViewById(R.id.button3);
        recyclerView = findViewById(R.id.recyclerview);
    }

    private void setRecyclerView(){//设置recyclerView
        data = new ArrayList<>();
        LocalDataInformation information;
        for (int i = 0; i < 10; i ++){
            information = new LocalDataInformation("标题" + i, "内容" + i);
            data.add(information);
        }
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        adapter = new MyAdapter(this, data);
        listener = (int position) -> {
            setThread(position);
        };
        adapter.setListener(listener);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void click(){//点击事件
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (remotePosition > 9)
                    remotePosition = 0;
                button3.setText("收到网络数据" + remotePosition);
                setThread(remotePosition);
                remotePosition ++;
            }
        });
    }

    private void setThread(int position){//多线程实现单选效果（注意使用了自旋锁的方式）
        lastPosition = adapter.getLastPosition();
        currectThread = new Thread(new Runnable() {
            @Override
            public void run() {
                adapter.setFlag();
                runOnUiThread(new Runnable() {//更新ui需要在主线程当中
                    @Override
                    public void run() {
                        adapter.notifyItemChanged(position);
                    }
                });
            }
        });
        if (lastPosition != -1 && lastPosition != position){
            lastThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    currectThread.start();
                    try {
                        currectThread.join();//等当前更新线程完成之后再执行上一个item的更新
                        while (adapter.getFlag()){
                            Thread.sleep(10);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyItemChanged(lastPosition);
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            lastThread.start();
        }else {
            currectThread.start();
        }
    }

}