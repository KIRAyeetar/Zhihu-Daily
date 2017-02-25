package com.example.administrator.internet.ToolClass;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/25 0025.
 */
//获取时间相关的方法
public class GetTime {
    public  static String getDateToString(long time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(new Date(time * 1000L));
    }
    public static String getNowTime(){
        String time=new SimpleDateFormat("yyyyMMdd").format(new Date());
        return time;
    }
    public static String getYesterdayTime(int date){
        int year=(date-1)/10000,month=(date-1)%10000/100,day=(date-1)%100;
        int two;
        if(year%4==0&&year%100!=0){
            two=29;
        } else{
            two=28;
        }
        int months[]={31,two,31,30,31,30,31,31,30,31,30,31};
        if(day==0){
            month--;
            if(month==0){
                month=12;
                year--;
            }
            day=months[month-1];
            return year+"年"+month+"月"+day+"日";
        }else {
            return year+"年"+month+"月"+day+"日";
        }
    }

    public static int timeReduce(int date){
        int year=(date-1)/10000,month=(date-1)%10000/100,day=(date-1)%100;
        int two;
        if(year%4==0&&year%100!=0){
            two=29;
        } else{
            two=28;
        }
        int months[]={31,two,31,30,31,30,31,31,30,31,30,31};
        if(day==0) {
            month--;
            if(month==0){
                month=12;
                year--;
            }
            day=months[month-1];
        }
        return year*10000+month*100+day;
    }
}
