package com.kzh.direction;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.kzh.direction.util.LocationUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

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



    }
}
