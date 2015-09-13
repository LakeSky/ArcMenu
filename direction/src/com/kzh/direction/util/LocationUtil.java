package com.kzh.direction.util;

public class LocationUtil {

    //得到两点之间的方位角
    public static double getAzimuth(double lat_a, double lng_a, double lat_b, double lng_b) {
        double d = 0;
        double cosc = Math.cos(90 - lat_b) * Math.cos(90 - lat_a) + Math.sin(90 - lat_b) * Math.sin(90 - lat_a) * Math.cos(lng_b - lng_a);
        double sinc = Math.sqrt(1 - cosc * cosc);
        d = Math.asin(Math.sin(90 - lat_b) * Math.sin(lng_b - lng_a) / sinc);
        d = d * 180 / Math.PI;
        if (lat_b > lat_a && lng_b < lng_a) {
            return d + 360;
        }
        if (lat_b < lat_a) {
            return 180 - d;
        }

        return d;
    }
}
