package com.kingbirdle.advertistingjar.utils;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.kingbirdle.advertistingjar.litepal.Parameter;
import com.socks.library.KLog;

import org.litepal.LitePal;

import java.util.List;

import static com.kingbirdle.advertistingjar.base.Base.webInterface;
import static com.kingbirdle.advertistingjar.utils.Config.CHANGE_LOCATION;
import static com.kingbirdle.advertistingjar.utils.Plog.e;

/**
 * 百度定位类
 *
 * @author panyingdao
 * @date 2018-1-22.
 */

public class MyLocationListener extends BDAbstractLocationListener {

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        String longitudeStr, latitudeStr;
        double longitude = bdLocation.getLongitude();
        double latitude = bdLocation.getLatitude();
        //获取定位精度，默认值为0.0f
        float radius = bdLocation.getRadius();
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
        int errorCode = bdLocation.getLocType();

        longitudeStr = String.valueOf(longitude);
        latitudeStr = String.valueOf(latitude);

        longitudeStr = latLong2Str(longitudeStr, 11);
        latitudeStr = latLong2Str(latitudeStr, 10);

        e("获取到的位置：" + longitudeStr, latitudeStr);
        e("当前定位精准度：" + radius);
        e("定位情况信息：" + errorCode);

        String longitudes = SpUtil.readString(Const.LONGITUDE);
        String latitudes = SpUtil.readString(Const.LATITUDE);

        if (!longitudes.equals(longitudeStr) || !latitudes.equals(latitudeStr)) {
            List<Parameter> qureId = LitePal.findAll(Parameter.class);
            for (Parameter deviceId : qureId) {
                String url = CHANGE_LOCATION + deviceId.getDeviceId() + "&longitude=" + longitudes + " &latitude= " + latitudes;
                webInterface(url);
            }
        }

        String failure = "+004.9E-324";
        if (!failure.equals(longitudeStr) && !failure.equals(latitudeStr)) {
            KLog.e("定位正常");
            SpUtil.writeString(Const.LONGITUDE, longitudeStr);
            SpUtil.writeString(Const.LATITUDE, latitudeStr);
        }
    }

    /**
     * 补齐经纬度的长度，不足前面补零
     *
     * @param s   需要补齐长度的对象
     * @param len 期望长度
     */
    private String latLong2Str(String s, int len) {
        if (s.length() != len) {
            if (s.startsWith("-")) {
                int length = s.length();
                String pre = s.substring(0, 1);
                String suf = s.substring(1);
                for (int i = 0; i < len - length; i++) {
                    pre = pre + "0";
                }
                s = pre + suf;
            } else {
                s = "+" + s;
                int length = s.length();
                String pre = s.substring(0, 1);
                String suf = s.substring(1);
                for (int i = 0; i < len - length; i++) {
                    pre = pre + "0";
                }
                s = pre + suf;
            }
        }
        return s;
    }
}
