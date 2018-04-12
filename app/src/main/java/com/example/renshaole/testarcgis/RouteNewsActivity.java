package com.example.renshaole.testarcgis;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.renshaole.testarcgis.adper.RouteNewsAdapter;
import com.example.renshaole.testarcgis.bean.RouteNewsBean;
import com.example.renshaole.testarcgis.helper.DatabaseOperation;

import java.util.List;

/**
 * 查看机动路线历史记录
 */
public class RouteNewsActivity extends AdaptationActivity {
    private ListView listView;
    private RouteNewsAdapter routeNewsAdapter;
    DatabaseOperation databaseOperation;
    private List<RouteNewsBean> list;
    private Handler handler;
    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qoute_news);
        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 0x12) {
                        list=databaseOperation.queryRouteNews();
                        routeNewsAdapter.setLists(list);
                        routeNewsAdapter.notifyDataSetChanged();
                }
            }
        };
        databaseOperation =new DatabaseOperation(this);
        list=databaseOperation.queryRouteNews();
        init();
    }

    private void init() {
        listView=findViewById(R.id.listView);
        routeNewsAdapter =new RouteNewsAdapter(RouteNewsActivity.this,list,databaseOperation,handler);
        listView.setAdapter(routeNewsAdapter);
    }
}
