package com.example.renshaole.testarcgis.adper;

import android.annotation.SuppressLint;
import android.content.Context;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.renshaole.testarcgis.MainActivity;
import com.example.renshaole.testarcgis.R;
import com.example.renshaole.testarcgis.bean.RouteNewsBean;
import com.example.renshaole.testarcgis.helper.DatabaseOperation;
import com.example.renshaole.testarcgis.pop.ConfirmDialogPopWindow;
import com.example.renshaole.testarcgis.utils.BeanUtil;

import java.util.List;

/**
 *机动路线
 */
public class RouteNewsAdapter extends BaseAdapter {
    private Context context;
    private List<RouteNewsBean> list;
    private   DatabaseOperation databaseOperation;
    private ConfirmDialogPopWindow confirmDialogPopWindow;
    private Handler handler;

    public RouteNewsAdapter(Context context,List<RouteNewsBean> list,DatabaseOperation databaseOperation,Handler handler) {
        this.context = context;
        this.list = list;
        this.databaseOperation = databaseOperation;
        this.handler = handler;

    }

    public void   setLists(List<RouteNewsBean> list){
        this.list=list;
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
            convertView = View.inflate(context, R.layout.item_qoutenews, null);
            viewHolder.tvTitle = convertView.findViewById(R.id.tvTitle);
            viewHolder.tvTime =  convertView.findViewById(R.id.tvTime);
            viewHolder.imgBtnDelect =  convertView.findViewById(R.id.imgBtnDelect);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvTitle.setText(list.get(position).getPoistion()+"");
        viewHolder.tvTime.setText(list.get(position).getStartTime()+"");
        viewHolder.imgBtnDelect.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                confirmDialogPopWindow =new ConfirmDialogPopWindow(context,"是否要删除");
                confirmDialogPopWindow.setBtnQuXiaoOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        confirmDialogPopWindow.dismiss();
                    }
                });
                confirmDialogPopWindow.setBtnQueDingOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseOperation.deleteRouteNews(list.get(position).getId());
                        Message message =new Message();
                        message.what=0x12;
                        handler.sendMessage(message);
                        confirmDialogPopWindow.dismiss();
                    }
                });
            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;    //消息
        TextView tvTime;    //时间
        ImageButton imgBtnDelect;    //删除

    }



}
