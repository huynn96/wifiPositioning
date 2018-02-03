package com.uet.wifiposition.ui.main.home.scanwifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.uet.wifiposition.R;
import com.uet.wifiposition.remote.model.WifiInfoModel;
import com.uet.wifiposition.ui.base.BaseFragment;
import com.uet.wifiposition.ui.main.home.publicwifiinfo.PublicWifiInfoFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ducnd on 9/22/17.
 */

public class ScanWifiInfoFragment extends BaseFragment implements ScanWifiInfoAdapter.IScanWifiInfoAdapter, View.OnClickListener, PublicWifiInfoFragment.IPublicWifiInfo {

    private static final String TAG = ScanWifiInfoFragment.class.getSimpleName();
    private ScanWifiInfoAdapter mAdapter;
    private List<WifiInfoModel> mWifiInfoModels;
    private ScanBroadCast mBroadcast;
    private WifiManager wifiManager;
    private Disposable mDisposeScan;
    private boolean isClear;
    private boolean isNotification;

    @Override
    public int getLayoutMain() {
        return R.layout.fragment_scan_wifi_info;
    }

    @Override
    public void findViewByIds(View view) {
        setProgress(view.findViewById(R.id.progress));
    }

    @Override
    public void initComponents(View view) {
        initData();
        RecyclerView rcWifi = (RecyclerView) getView().findViewById(R.id.rc_wifi_info);
        rcWifi.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ScanWifiInfoAdapter(this);
        rcWifi.setAdapter(mAdapter);
        initBroadcast();

    }

    @Override
    public void setEvents(View view) {

    }

    private void initData() {
        mWifiInfoModels = new ArrayList<>();
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }


    private void initBroadcast() {
        mBroadcast = new ScanBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        getContext().registerReceiver(mBroadcast, filter);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            if (isNotification) {
                hideProgress();
                mAdapter.notifyDataSetChanged();
                isNotification = false;
            }
        }

        wifiManager.setWifiEnabled(true);
        mDisposeScan = Observable.just("scan")
                .repeatWhen(delay -> delay.delay(2000, TimeUnit.MILLISECONDS))
                .subscribe(response -> {
                    boolean isScan = wifiManager.startScan();
//                    Log.d(TAG, "initComponents: isScan: " + isScan);
//                    if ( isScan  ) {
//                        List<ScanResult> scanResults = wifiManager.getScanResults();
//                        for (ScanResult scanResult : scanResults) {
//                            Log.d(TAG, "initComponents: name: " + scanResult.SSID + "level: " + scanResult.level);
//                        }
//                    }
//                    Log.d(TAG, "initComponents: ---------------------------------");
                });
    }

    @Override
    public void onPause() {
        mDisposeScan.dispose();
        wifiManager.setWifiEnabled(false);
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (isNotification) {
                hideProgress();
                mAdapter.notifyDataSetChanged();
                isNotification = false;
            }
        }
    }

    @Override
    public int getCount() {
        if (mWifiInfoModels.size() == 0) {
            return 0;
        }
        return mWifiInfoModels.size() + 1;
    }

    @Override
    public WifiInfoModel getData(int position) {
        return mWifiInfoModels.get(position);
    }


    private class ScanBroadCast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            getScanWifi();
        }
    }

    private List<WifiInfoModel> wifiInfoModels = new ArrayList<>();
    long currentTime = 0;

    private void getScanWifi() {
        Observable.create((ObservableOnSubscribe<List<WifiInfoModel>>) emitter -> {
            List<ScanResult> scanResults = wifiManager.getScanResults();

//            //debug
//            List<WifiInfoModel> currents = new ArrayList<>();
//            Date date = new Date();
//            currentTime = date.getTime();
//
//
//            for (ScanResult scanResult : scanResults) {
//                WifiInfoModel model = new WifiInfoModel();
//                model.setCount(1);
//                model.setLevel(scanResult.level);
//                model.setFrequency(scanResult.frequency);
//                model.setMacAddress(scanResult.BSSID);
//                model.setName(scanResult.SSID);
//                currents.add(model);
//            }
//            if (wifiInfoModels.size() == 0) {
//                wifiInfoModels = currents;
//            } else {
//                for (WifiInfoModel wifiInfoModel : wifiInfoModels) {
//                    for (WifiInfoModel current : currents) {
//                        if (wifiInfoModel.getMacAddress().equals(current.getMacAddress())) {
//                            Log.d(TAG, "getScanWifi name: " + current.getName() + " , detal: " + (wifiInfoModel.getLevel() - current.getLevel()));
//                            break;
//                        }
//                    }
//                }
//                wifiInfoModels = currents;
//                Log.d(TAG, "---------------------------------");
//            }
//            //end debug

            if (scanResults != null) {
                Log.d(TAG, "getScanWifi scanResults size: " + scanResults.size());
                if (isClear) {
                    mWifiInfoModels.clear();
                }
                List<WifiInfoModel> newList = new ArrayList<>();
                for (ScanResult scanResult : scanResults) {
                    if (isClear) {
                        WifiInfoModel model = new WifiInfoModel();
                        model.setCount(1);
                        model.setLevel(scanResult.level);
                        model.setFrequency(scanResult.frequency);
                        model.setMacAddress(scanResult.BSSID);
                        model.setName(scanResult.SSID);
                        newList.add(model);
                    } else {
                        boolean isSame = false;
                        for (WifiInfoModel wifiInfoModel : mWifiInfoModels) {
                            if (wifiInfoModel.getMacAddress().equals(scanResult.BSSID)) {
                                wifiInfoModel.setLevel(wifiInfoModel.getLevel() * wifiInfoModel.getCount() / (wifiInfoModel.getCount() + 1) +
                                        scanResult.level * 1.0f / (wifiInfoModel.getCount() + 1));
                                wifiInfoModel.setCount(wifiInfoModel.getCount() + 1);
                                wifiInfoModel.getRss().add(scanResult.level * 1.0f);
                                isSame = true;
                                break;
                            }
                        }
                        if (!isSame) {
                            WifiInfoModel model = new WifiInfoModel();
                            model.setCount(1);
                            model.setLevel(scanResult.level);
                            model.setFrequency(scanResult.frequency);
                            model.setMacAddress(scanResult.BSSID);
                            model.setName(scanResult.SSID);
                            model.getRss().add(scanResult.level * 1.0f);
                            newList.add(model);
                        }
                    }
                }
                isClear = false;
                emitter.onNext(newList);

            } else {
                Log.d(TAG, "clickWifi null.......");
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Log.d(TAG, "getScanWifi size: " + response.size());
                    mWifiInfoModels.addAll(response);
                    if (!isResume) {
                        isNotification = true;
                        return;
                    }
                    hideProgress();
                    mAdapter.notifyDataSetChanged();
                }, Throwable::printStackTrace);


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroyView() {
        getContext().unregisterReceiver(mBroadcast);
        super.onDestroyView();
    }

    public void reload() {
        isClear = true;
        showProgress();
    }

    @Override
    public List<WifiInfoModel> getListWifiInfoChoose() {
        if (mWifiInfoModels == null || isClear) {
            return null;
        }
        List<WifiInfoModel> wifiInfoModels = new ArrayList<>();
        for (WifiInfoModel info : mWifiInfoModels) {
            if (info.isCheck()) {
                wifiInfoModels.add(info);
            }
        }
        return wifiInfoModels;
    }

    public void chooseAll() {
        if (mWifiInfoModels != null) {
            for (WifiInfoModel wifi : mWifiInfoModels) {
                wifi.setCheck(true);
                mAdapter.notifyDataSetChanged();
            }
        }

    }
}
