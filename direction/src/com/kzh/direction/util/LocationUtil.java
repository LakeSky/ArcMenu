package com.kzh.direction.util;

public class LocationUtil {

    //得到两点之间的方位角
    public static double getAzimuth(double lat_a, double lng_a, double lat_b, double lng_b) {
        double d = 0;
        /*lat_a = lat_a * Math.PI / 180;
        lng_a = lng_a * Math.PI / 180;
        lat_b = lat_b * Math.PI / 180;
        lng_b = lng_b * Math.PI / 180;

        d = Math.sin(lat_a) * Math.sin(lat_b) + Math.cos(lat_a) * Math.cos(lat_b) * Math.cos(lng_b - lng_a);
        d = Math.sqrt(1 - d * d);
        d = Math.cos(lat_b) * Math.sin(lng_b - lng_a) / d;
        d = Math.asin(d) * 180 / Math.PI;

        d = Math.atan((lng_b - lng_a) * Math.cos(lat_b) / (lat_b - lat_a));*/

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


    //得到两点之间的方位角
    public static double getAzimuth1(double lat_a, double lng_a, double lat_b, double lng_b) {
        double d = 0;
        /*lat_a = lat_a * Math.PI / 180;
        lng_a = lng_a * Math.PI / 180;
        lat_b = lat_b * Math.PI / 180;
        lng_b = lng_b * Math.PI / 180;

        d = Math.sin(lat_a) * Math.sin(lat_b) + Math.cos(lat_a) * Math.cos(lat_b) * Math.cos(lng_b - lng_a);
        d = Math.sqrt(1 - d * d);
        d = Math.cos(lat_b) * Math.sin(lng_b - lng_a) / d;
        d = Math.asin(d) * 180 / Math.PI;

        d = Math.atan((lng_b - lng_a) * Math.cos(lat_b) / (lat_b - lat_a));*/

        double cosc = Math.cos(90 - lat_b) * Math.cos(90 - lat_a) + Math.sin(90 - lat_b) * Math.sin(90 - lat_a) * Math.cos(lng_b - lng_a);
        double sinc = Math.sqrt(1 - cosc * cosc);
        /*d = Math.asin(Math.sin(90 - lat_b) * Math.sin(lng_b - lng_a) / sinc);
        if (lat_b > lat_a && lng_b < lng_a) {
            return d + 360;
        }
        if (lat_b < lat_a) {
            return 180 - d;
        }*/

        d = Math.acos((Math.cos(90 - lat_b) - Math.cos(90 - lat_a) * cosc) / (Math.sin(90 - lat_a) * sinc));

        return d;
    }
}
