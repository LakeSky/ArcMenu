package com.kzh.direction;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.*;
import com.baidu.mapapi.model.LatLng;
import com.capricorn.ArcMenu;
import com.capricorn.menu.ArcFlexibleMenu;
import com.capricorn.view.StarView;
import com.kzh.direction.util.LocationUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MyActivity extends Activity {

    private SensorManager sm = null;
    private Sensor aSensor = null;
    private Sensor mSensor = null;

    TextView textview = null;
    ArcFlexibleMenu arcFlexibleMenu = null;
    float fromDegree = 0;

    float[] accelerometerValues = new float[3];
    float[] magneticFieldValues = new float[3];
    float[] values = new float[3];
    float[] result = new float[9];

    /*private static final int[] ITEM_DRAWABLES = {R.drawable.composer_camera, R.drawable.composer_music,
            R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with};*/

    private static final int[] ITEM_DRAWABLES = {R.drawable.composer_camera, R.drawable.composer_music,
            R.drawable.composer_place, R.drawable.composer_sleep};

    public LocationClient mLocationClient = null;
    public BDLocationListener myLocListener = new MyLocationListener();

    //----------------------------------
    StarView starView;
    private boolean mStopDrawing;
    protected final Handler mHandler = new Handler();
    private float mDirection;
    private float mTargetDirection;
    private final float MAX_ROATE_DEGREE = 1.0f;
    private AccelerateInterpolator mInterpolator;
    float[] calmValues = new float[2];
    int initFlag = 0;
    int offsetNum = 0;
    float preValue = -9999;
    boolean animateFlag = true;


    float prevalue;
    float prediff;
    int num = 0;
    //----------------------------------

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        textview = (TextView) findViewById(R.id.text1);
        //------------------------------
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        aSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        //-------------------------------
        mLocationClient = new LocationClient(getApplicationContext());     //����LocationClient��
        initLocation();
        mLocationClient.registerLocationListener(myLocListener);    //ע���������
        mLocationClient.start();

        arcFlexibleMenu = (ArcFlexibleMenu) findViewById(R.id.arc_menu);
        initArcMenu(arcFlexibleMenu, ITEM_DRAWABLES);
        initResources();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
        option.setCoorType("bd09ll");//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
        int span = 1000;
        option.setScanSpan(span);//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
        option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
        option.setOpenGps(true);//��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
        option.setLocationNotify(true);//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
        option.setIsNeedLocationDescribe(true);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
        option.setIsNeedLocationPoiList(true);//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
        option.setIgnoreKillProcess(false);//��ѡ��Ĭ��false����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ��ɱ��
        option.SetIgnoreCacheException(false);//��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
        option.setEnableSimulateGps(false);//��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS��λ���
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// ��λ������ÿСʱ
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// ��λ����
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// ��λ��
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps��λ�ɹ�");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// ���綨λ���
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //��Ӫ����Ϣ
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("���綨λ�ɹ�");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// ���߶�λ���
                sb.append("\ndescribe : ");
                sb.append("���߶�λ�ɹ������߶�λ���Ҳ����Ч��");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("��������綨λʧ�ܣ����Է���IMEI�źʹ��嶨λʱ�䵽loc-bugs@baidu.com��������׷��ԭ��");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("���粻ͬ���¶�λʧ�ܣ����������Ƿ�ͨ��");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("�޷���ȡ��Ч��λ���ݵ��¶�λʧ�ܣ�һ���������ֻ���ԭ�򣬴��ڷ���ģʽ��һ���������ֽ�����������������ֻ�");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// λ�����廯��Ϣ
            List<Poi> list = location.getPoiList();// POI����
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());

            mLocationClient.stop();
        }
    }


    private void initArcMenu(ArcFlexibleMenu menu, int[] itemDrawables) {
        final int itemCount = itemDrawables.length;
        /*StarView item = new StarView(this);
        final int i = 0;
        item.setImageResource(itemDrawables[i]);
        item.setDegree(0);
        item.setRadius(300);
        menu.addItem(item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyActivity.this, "position:" + 0, Toast.LENGTH_SHORT).show();
            }
        });*/
        List<LatLng> latLngs=new ArrayList<LatLng>();
        latLngs.add(new LatLng(31.201109,120.618951));//���� ���
        latLngs.add(new LatLng(31.134328,120.650972));//������� ����
        latLngs.add(new LatLng(31.157199,120.606991));//��̫����̬԰ �ص�
        latLngs.add(new LatLng(31.175615,120.670591));//���û�԰ ����
        //����:���,�������:����,��̫����̬԰:�ص�,���û�԰:����
        for (int i = 0; i < itemCount; i++) {
            Random rand = new Random();
            StarView item = new StarView(this);
            item.setImageResource(itemDrawables[i]);
            LatLng latLng=latLngs.get(i);
            double degree= LocationUtil.getAzimuth(31.173838, 120.653092, latLng.latitude, latLng.longitude);
            item.setDegree((float)degree);
            Log.v("degree------",String.valueOf(degree));
            item.setRadius(300);

            final int position = i;
            menu.addItem(item, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(MyActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    //ע��activity��ͣ��ʱ���ͷ�
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        sm.unregisterListener(mySensorListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*if (mLocationProvider != null) {
            updateLocation(mLocationManager.getLastKnownLocation(mLocationProvider));
            mLocationManager.requestLocationUpdates(mLocationProvider, 2000, 10, mLocationListener);
        } else {
            mLocationTextView.setText(R.string.cannot_get_location);
        }*/
        if (aSensor != null && mSensor != null) {
            /*mSensorManager.registerListener(mOrientationSensorEventListener, mOrientationSensor,
                    SensorManager.SENSOR_DELAY_GAME);*/
            sm.registerListener(mySensorListener, aSensor, SensorManager.SENSOR_DELAY_GAME);
            sm.registerListener(mySensorListener, mSensor, SensorManager.SENSOR_DELAY_GAME);
        }
        mStopDrawing = false;
        mHandler.postDelayed(mCompassViewUpdater, 40);
    }

    final SensorEventListener mySensorListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                accelerometerValues = event.values;
            }
            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticFieldValues = event.values;
            }
            //����getRotaionMatrix��ñ任����R[]
            SensorManager.getRotationMatrix(result, null, accelerometerValues, magneticFieldValues);
            //����SensorManager.getOrientation(result, values);�õ���valuesֵΪ����
            SensorManager.getOrientation(result, values);
            //ת��Ϊ�Ƕ�
            values[0] = (float) Math.toDegrees(values[0]);
            float valuex = normalizeDegree(values[0] * -1.0f);
//            mTargetDirection=valuex;
            //mTargetDirection = calmDownSensor(valuex);
            mTargetDirection = calmDown(valuex);
            //textview.setText("����:���,�������:����,��̫����̬԰:�ص�,���û�԰:����  x=" + mTargetDirection);
        }
    };

    public void showAnimation(View mView, float fromDegree, float toDegree) {
        final float centerX = mView.getWidth() / 2.0f;
        final float centerY = mView.getHeight() / 2.0f;
        //�����������Ҫ��ת�ĽǶȣ������õ���180��
        RotateAnimation rotateAnimation = new RotateAnimation(fromDegree, toDegree, centerX, centerY);
        //���������ͨ��ʱ���
        rotateAnimation.setDuration(1000 * 3);
        rotateAnimation.setFillAfter(true);
        mView.startAnimation(rotateAnimation);
    }

    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
            if (arcFlexibleMenu != null && !mStopDrawing) {
                if (mDirection != mTargetDirection) {
                    float to = mTargetDirection;
                    if (to - mDirection > 180) {
                        to -= 360;
                    } else if (to - mDirection < -180) {
                        to += 360;
                    }

                    // limit the max speed to MAX_ROTATE_DEGREE
                    float distance = to - mDirection;
                    if (Math.abs(distance) > MAX_ROATE_DEGREE) {
                        distance = distance > 0 ? MAX_ROATE_DEGREE : (-1.0f * MAX_ROATE_DEGREE);
                    }

                    // need to slow down if the distance is short
                    mDirection = normalizeDegree(mDirection
                            + ((to - mDirection) * mInterpolator.getInterpolation(Math
                            .abs(distance) > MAX_ROATE_DEGREE ? 0.4f : 0.3f)));
                    fromDegree = mDirection;
                }
                if (animateFlag) {
                    showAnimation(arcFlexibleMenu, fromDegree, fromDegree + mDirection);
                    fromDegree = fromDegree + mDirection;
                }
                mHandler.postDelayed(mCompassViewUpdater, 20);
            }
        }
    };

    private void initResources() {
        mDirection = 0.0f;
        mTargetDirection = 0.0f;
        mInterpolator = new AccelerateInterpolator();
        mStopDrawing = true;
    }

    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }

    private Float calmDownTheSensor(float value) {
        if (initFlag < 2) {
            calmValues[initFlag] = value;
            initFlag++;
            return value;
        } else {
            if (calmValues[1] > calmValues[0] && calmValues[1] < value ||
                    calmValues[1] > value && calmValues[1] < calmValues[0]) {
                calmValues[0] = calmValues[1];
                calmValues[1] = value;
                return value;
            } else {
                calmValues[0] = calmValues[1];
                calmValues[1] = value;
                return calmValues[0];
            }
        }
    }

    private Float calmDownSensor(float value) {
        if (animateFlag) {
            preValue = value;
            return value;
        } else {
            float difference = Math.abs(preValue - value);
            if (difference > 3 && offsetNum < 6) {
                offsetNum++;
                animateFlag = false;
                return 0f;
            } else {
                animateFlag = true;
                preValue = value;
                offsetNum = 0;
                return value;
            }
        }
    }

    public float calmDown(float value) {
        float diff = Math.abs(value - prevalue);
        if (diff > 10) {
            if (prediff > 10) {
                num++;
            } else {
                if (num > 5) {
                    prevalue = value;
                }
                num = 0;
            }
            prediff = diff;
            return prevalue;
        } else {
            if (diff < 3) {
                if (prediff < 3) {
                    value = (prevalue + value) / 2;
                }
            }
            prevalue = value;
            prediff = diff;
            return value;
        }
    }
}
