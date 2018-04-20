package com.example.renshaole.testarcgis.utils;

import android.os.Environment;
import android.util.Log;

import com.example.renshaole.testarcgis.bean.MarkCorerBean;


import java.io.File;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 * @author spl
 *
 */
public class FileUtil {

    /**
     * 获取SDcard根路径
     * @return
     */
    public static String getSDCardPath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
    /**
     * 获取SDcard根路径
     * @return
     */
    public static String getSDCardPotoPath(){
        return Environment.DIRECTORY_PICTURES;
    }


    /**
     * 通过传入的路径,返回该路径下的所有的文件和文件夹列表
     * @param path
     * @return
     */
    public static List<MarkCorerBean> getListData(String path) {

        List<MarkCorerBean> list = new ArrayList<>();

        File pfile = new File(path);// 文件对象
        File[] files = null;// 声明了一个文件对象数组
        if (pfile.exists()) {// 判断路径是否存在
            files = pfile.listFiles();// 该文件对象下所属的所有文件和文件夹列表
        }
        if (files != null && files.length > 0) {// 非空验证
            for (File file : files) {// foreach循环遍历
                MarkCorerBean item = new MarkCorerBean();
                if(file.isHidden()){
                    continue;// 跳过隐藏文件
                }
                if (file.isDirectory()// 文件夹
                        && file.canRead()//是否可读
                        ) {
                    file.isHidden();//  是否是隐藏文件

                } else if(file.isFile()){// 文件

                    Log.i("spl",file.getName());
                    String ext = getFileEXT(file.getName());
                    Log.i("spl", "ext="+ext);
                    // 文件的大小
                    String size = getSize(file.length());
                    if (checkEndsInArray(ext, new String[]{"png","gif","jpg","bmp"})) {
//                        item.setImgNmae(file.getName());// 名称
//                        item.setImgPath(file.getAbsolutePath());// 绝对路径
                        list.add(item);
                    }

                }else{// 其它
                }
//                item.RelativePath=file.getPath();// 相对路径
            }
        }
        files = null;
        return list;
    }

    /**
     * 检查扩展名end 是否在ends数组中
     * @param end
     * @param ends
     * @return
     */
    public static boolean checkEndsInArray(String end, String[] ends) {
        for (String aEnd : ends) {
            if (end.equals(aEnd)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 截取文件的扩展名
     * @param filename 文件全名
     * @return 扩展名(mp3, txt)
     */
    public static String getFileEXT(String filename){
        if (filename.contains(".")) {
            int dot = filename.lastIndexOf(".");// 123.ClassroomVoteNewAdapter.txt
            String ext = filename.substring(dot + 1);
            return ext;
        }else{
            return "";
        }
    }




    /**
     * 格式转换应用大小 单位"B,KB,MB,GB"
     */
    public static String getSize(float length) {

        long kb = 1024;
        long mb = 1024*kb;
        long gb = 1024*mb;
        if(length<kb){
            return String.format("%dB", (int)length);
        }else if(length<mb){
            return String.format("%.2fKB", length/kb);
        }else if(length<gb){
            return String.format("%.2fMB", length/mb);
        }else {
            return String.format("%.2fGB", length/gb);
        }
    }




    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        dir.delete();
    }

    /*
* Java文件操作 获取文件扩展名
*/
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

}
