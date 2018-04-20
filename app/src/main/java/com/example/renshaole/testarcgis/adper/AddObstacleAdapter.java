package com.example.renshaole.testarcgis.adper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renshaole.testarcgis.R;
import com.example.renshaole.testarcgis.bean.MarkCorerBean;
import com.example.renshaole.testarcgis.bean.RouteNewsBean;
import com.example.renshaole.testarcgis.helper.DatabaseOperation;
import com.example.renshaole.testarcgis.pop.AddPositionPopWindow;
import com.example.renshaole.testarcgis.pop.ConfirmDialogPopWindow;
import com.example.renshaole.testarcgis.utils.BeanUtil;

import java.util.List;

/**
 *图片列表
 */
public class AddObstacleAdapter extends BaseAdapter {
    private Context context;
    private List<MarkCorerBean> list;
    private ConfirmDialogPopWindow confirmDialogPopWindow;
    private Handler handler;
    private DatabaseOperation databaseOperation;

    public AddObstacleAdapter(Context context, List<MarkCorerBean> list,DatabaseOperation databaseOperation) {
        this.context = context;
        this.list = list;
        this.databaseOperation = databaseOperation;

    }


    @Override
    public int getCount() {
        return   BeanUtil.isEmpty(list) ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_pbstacle, null);
            viewHolder.tvImaName = convertView.findViewById(R.id.tvImaName);
            viewHolder.tvAdd =  convertView.findViewById(R.id.tvAdd);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.tvImaName.setText(list.get(position).getImgNmae()+"");
//        viewHolder.tvAdd.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v) {
//                final AddPositionPopWindow addPositionPopWindow =new AddPositionPopWindow(context);
//                addPositionPopWindow.setBtnQueDingOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (addPositionPopWindow.getEdtY().equals("")||addPositionPopWindow.getEdtX().equals("")) {
//                            Toast.makeText(context,"经纬度不能为空",Toast.LENGTH_SHORT).show();
//                        }else {
//                            MarkCorerBean markCorerBean =new MarkCorerBean();
//                            markCorerBean.setImgPath(list.get(position).imgPath);
//                            markCorerBean.setImgNmae(list.get(position).imgNmae);
//                            markCorerBean.setPoistion_x(Double.parseDouble(addPositionPopWindow.getEdtX()));
//                            markCorerBean.setPoistion_y(Double.parseDouble(addPositionPopWindow.getEdtY()));
//                            databaseOperation.addmarkCorer(markCorerBean);
//                            addPositionPopWindow.dismiss();
//                        }
//                    }
//                });
//                addPositionPopWindow.setBtnQuXiaoOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        addPositionPopWindow.dismiss();
//                    }
//                });
//            }
//        });

        return convertView;
    }

    class ViewHolder {
        TextView tvImaName;
        TextView tvAdd;

    }



}
