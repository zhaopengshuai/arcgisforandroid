package com.example.renshaole.testarcgis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.renshaole.testarcgis.adper.AddObstacleAdapter;
import com.example.renshaole.testarcgis.adper.ObstacleAdapter;
import com.example.renshaole.testarcgis.bean.MarkCorerBean;
import com.example.renshaole.testarcgis.helper.DatabaseOperation;

import java.util.List;

/**
 * 障碍物
 */
public class ObstacleActivity extends AdaptationActivity {
    private ObstacleAdapter obstacleAdapter;
    private ListView listView;
    private List<MarkCorerBean> listMarkCorerBean;
    private DatabaseOperation databaseOperation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obstacle);
        databaseOperation =new DatabaseOperation(this);
        init();
    }

    private void init() {
        listView =findViewById(R.id.listView);
        listMarkCorerBean= databaseOperation.queryMarkCorer("");
        obstacleAdapter =new ObstacleAdapter(this,listMarkCorerBean);
        listView.setAdapter(obstacleAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.obstacle, menu);
        return true;
    }
    /**
     * 下拉弹框
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                Intent intent=new Intent(ObstacleActivity.this,AddPbstacleActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }
}
