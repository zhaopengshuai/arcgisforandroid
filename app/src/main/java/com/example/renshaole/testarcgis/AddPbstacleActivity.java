package com.example.renshaole.testarcgis;

import android.os.Bundle;
import android.widget.ListView;

import com.example.renshaole.testarcgis.adper.AddObstacleAdapter;
import com.example.renshaole.testarcgis.bean.MarkCorerBean;
import com.example.renshaole.testarcgis.helper.DatabaseOperation;
import com.example.renshaole.testarcgis.utils.FileUtil;

import java.util.List;

/**
 * 添加障碍物
 */
public class AddPbstacleActivity extends AdaptationActivity {
    private String sd_path;
    private ListView listView;
    private AddObstacleAdapter addObstacleAdapter;
   private List<MarkCorerBean> listMarkCorerBean;
    private DatabaseOperation databaseOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pbstacle);
        databaseOperation = new DatabaseOperation(this);
        sd_path= FileUtil.getSDCardPath()+ "/DCIM";
        listMarkCorerBean=FileUtil.getListData(sd_path);
        init();
    }

    private void init() {
        listView=findViewById(R.id.listImg);
//        addObstacleAdapter=new AddObstacleAdapter(this,listMarkCorerBean,databaseOperation);
//        listView.setAdapter(addObstacleAdapter);
    }
}
