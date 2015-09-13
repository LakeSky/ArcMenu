package com.kzh.direction;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.*;
import com.baidu.navisdk.util.common.StringUtils;

import java.util.ArrayList;

public class SearchActivity extends Activity {
    private AutoCompleteTextView autotext;
    private ArrayAdapter<String> adapter;
    PoiSearch poiSearch;
    ArrayList<PoiInfo> poiInfos = new ArrayList<PoiInfo>();
    String[] arr_PoiInfo = new String[]{};
    LatLng latLng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        Double lat = bundle.getDouble("lat");
        Double lng = bundle.getDouble("lng");
        latLng=new LatLng(lat,lng);
        SDKInitializer.initialize(getApplicationContext());
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(poiListener);

        autotext = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autotext.addTextChangedListener(new TextWatcher() {
            private ArrayAdapter<String> adapter;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchButtonProcess(autotext);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                    adapter.notifyDataSetInvalidated();
                }
            }
        });

        autotext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
                Toast.makeText(SearchActivity.this, "" + index, 1).show();
            }
        });
    }

    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
        public void onGetPoiResult(PoiResult result) {
            //获取POI检索结果
            try {
                // 错误号可参考MKEvent中的定义
                if (result == null) {
                    poiInfos.clear();
                    show();
                    return;
                }
                // 将地图移动到第一个POI中心点
                if (result.getCurrentPageCapacity() > 0) {
                    poiInfos.clear();
                    for (PoiInfo info : result.getAllPoi()) {
                        System.err.println(info.address + " " + info.address);
                        poiInfos.add(info);
                    }
                    show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onGetPoiDetailResult(PoiDetailResult result) {
            //获取Place详情页检索结果
        }
    };

    public void searchButtonProcess(View v) {
        if (StringUtils.isNotEmpty(autotext.getText().toString())) {
            PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
            nearbySearchOption.radius(10000);
            nearbySearchOption.location(latLng);
            nearbySearchOption.keyword(autotext.getText().toString());
            poiSearch.searchNearby(nearbySearchOption);
        }
    }

    protected void show() {
        if (poiInfos == null || poiInfos.isEmpty()) {
            arr_PoiInfo = new String[]{};
        } else {
            final int size = poiInfos.size();
            arr_PoiInfo = new String[size];
            for (int i = 0; i < poiInfos.size(); i++) {
                arr_PoiInfo[i] = poiInfos.get(i).name;
                Log.d("test", arr_PoiInfo[i]);
            }
        }

        Log.d("test", poiInfos.toString());
        adapter = new ArrayAdapter<String>(SearchActivity.this, android.R.layout.simple_dropdown_item_1line, arr_PoiInfo) {
            private Filter f;

            @Override
            public Filter getFilter() {
                if (f == null) {
                    f = new Filter() {
                        @Override
                        protected synchronized FilterResults performFiltering(CharSequence c) {
                            ArrayList<Object> suggestions = new ArrayList<Object>();
                            for (String adr : arr_PoiInfo) {
                                suggestions.add(adr);
                            }
                            Log.d("test", poiInfos.toString());
                            Log.d("test", arr_PoiInfo.toString());
                            FilterResults filterResults = new FilterResults();
                            filterResults.values = suggestions;
                            filterResults.count = suggestions.size();
                            return filterResults;
                        }

                        @Override
                        protected synchronized void publishResults(CharSequence c, FilterResults results) {
                            if (results.count > 0) {
                                adapter.notifyDataSetChanged();
                            } else {
                                adapter.notifyDataSetInvalidated();
                            }
                        }
                    };
                }
                return f;
            }
        };
        autotext.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetInvalidated();
    }
}
