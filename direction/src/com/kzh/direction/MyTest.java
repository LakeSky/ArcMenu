package com.kzh.direction;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.kzh.direction.util.LocationUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyTest {
    public static void main(String[] args) {

        /*double latitude = 31.172133;
        double lontitude = 120.653334;

        double latitudeB = 30.172133;
        double lontitudeB = 121.653334;
        double angle = LocationUtil.getAzimuth(latitude, lontitude, latitudeB, lontitudeB);

        LatLng a=new LatLng(latitude,lontitude);
        LatLng b=new LatLng(latitudeB,lontitudeB);
        double distance= DistanceUtil.getDistance(a,b);
        System.out.println(angle);
        System.out.println(distance);*/

        long time=1431927928000L;
        Date date=new Date(time);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(simpleDateFormat.format(date));

        List<LatLng> latLngs=new ArrayList<LatLng>();
        latLngs.add(new LatLng(31.201109,120.618951));//永旺 相机
        latLngs.add(new LatLng(31.134328,120.650972));//华邦国际 音乐
        latLngs.add(new LatLng(31.157199,120.606991));//东太湖生态园 地点
        latLngs.add(new LatLng(31.175615, 120.670591));//海悦花园 月亮

        double jc=0.034141;
        double wc=0.027271;
        double c=Math.atan(jc/wc);

        jc= 0.026567;
        wc=0.034312;

        double xx=wc/jc;
        System.out.println("------------"+xx);
        c=Math.atan(wc/jc);

        System.out.println("c"+c);

        System.out.println(LocationUtil.getAzimuth(31.1737930000, 120.6530720000, 31.2003600000, 120.6187600000)); //第一象限
        System.out.println(LocationUtil.getAzimuth(31.1737930000, 120.6530720000, 31.134328, 120.650972)); //第二象限
        System.out.println(LocationUtil.getAzimuth(31.1737930000, 120.6530720000, 31.157199, 120.606991)); //第三象限
        System.out.println(LocationUtil.getAzimuth(31.1737930000, 120.6530720000, 31.175615, 120.670591)); //



    }
}
