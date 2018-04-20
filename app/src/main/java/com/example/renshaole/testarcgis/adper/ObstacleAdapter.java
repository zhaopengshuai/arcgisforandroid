package com.example.renshaole.testarcgis.adper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renshaole.testarcgis.R;
import com.example.renshaole.testarcgis.bean.MarkCorerBean;
import com.example.renshaole.testarcgis.helper.DatabaseOperation;
import com.example.renshaole.testarcgis.pop.AddPositionPopWindow;
import com.example.renshaole.testarcgis.pop.ConfirmDialogPopWindow;
import com.example.renshaole.testarcgis.utils.BeanUtil;

import java.util.List;

/**
 *图片列表
 */
public class ObstacleAdapter extends BaseAdapter {
    private Context context;
    private List<MarkCorerBean> list;
    private Handler handler;

    public ObstacleAdapter(Context context, List<MarkCorerBean> list) {
        this.context = context;
        this.list = list;

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
            convertView = View.inflate(context, R.layout.item_obstacle, null);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            viewHolder.tvTime =  convertView.findViewById(R.id.tvTime);
            viewHolder.imgBtnDelect =  convertView.findViewById(R.id.imgBtnDelect);
            convertView.setTag(viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//        viewHolder.tvTitle.setText(list.get(position).getImgNmae());
        viewHolder.tvTime.setText(list.get(position).getPoistion_x()+"/"+list.get(position).getPoistion_y());


        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;    //消息
        TextView tvTime;    //时间
        ImageButton imgBtnDelect;    //删除

    }



}
