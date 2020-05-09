package com.qtimes.utils.rx;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.telephony.TelephonyManager;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

/**
 * author: liutao
 * date: 2016/7/28
 */
public class RxNetUtil {
    /**
     * 枚举网络状态
     * NET_NO：没有网络 ,  NET_2G:2g网络 , NET_3G：3g网络 ,NET_4G：4g网络 ,NET_WIFI：wifi , NET_UNKNOWN：未知网络
     * NET_INIT 初始化状态
     */
    public enum NetState {
        NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN, NET_INIT
    }

    public static IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");

    private static RxNetUtil instance;
    private NetStatus netStatus ;

    public static RxNetUtil getInstance() {
        if (instance == null)
            instance = new RxNetUtil();
        return instance;
    }

    public void init(Context context) {
        netStatus = getCurrentNetStatus(context);
    }

    public void setNetStatus(NetStatus netStatus) {
        this.netStatus = netStatus;
    }
    //获取当前状态，因为是监听广播的实时状态，所以可能有延迟
    public NetStatus getNetStatus() {
        return netStatus;
    }

    /**
     * 监听网络的变化
     *
     * @param context
     * @return
     */
    public Observable<NetStatus> observeNet(final Context context) {
        return Observable
                .create(new Observable.OnSubscribe<NetStatus>() {//注册一个广播进行网络改变监听
                    NetState lastState = NetState.NET_INIT;
                    NetStatus netState = new NetStatus(NetState.NET_INIT, NetState.NET_INIT);

                    @Override
                    public void call(final Subscriber<? super NetStatus> subscriber) {
                        final BroadcastReceiver receiver = new BroadcastReceiver() {
                            @Override
                            public void onReceive(Context context, Intent intent) {
                                NetState state = getCurrentNetStateCode(context);
                                netState.setLastState(lastState);
                                netState.setCurrentState(state);
                                subscriber.onNext(netState);
                                lastState = state;
                            }
                        };
                        context.registerReceiver(receiver, intentFilter);
                        subscriber.add(unsubscribeInUiThread(new Action0() {
                            @Override
                            public void call() {
                                context.unregisterReceiver(receiver);
                            }
                        }));
                    }
                })
                .defaultIfEmpty(new NetStatus(NetState.NET_NO, NetState.NET_NO));
    }

    //确定在UI线程解除广播注册
    private Subscription unsubscribeInUiThread(final Action0 unsubscribe) {
        return Subscriptions.create(new Action0() {
            @Override
            public void call() {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    unsubscribe.call();
                } else {
                    final Scheduler.Worker inner = AndroidSchedulers.mainThread().createWorker();
                    inner.schedule(new Action0() {
                        @Override
                        public void call() {
                            unsubscribe.call();
                            inner.unsubscribe();
                        }
                    });
                }
            }
        });
    }


    //获取当前状态

    private NetState getCurrentNetStateCode(Context context) {
        return getCurrentNetState(context);
    }

    /**
     * 获取当前的网络状态
     * @param context
     * @return
     */
    public static NetStatus getCurrentNetStatus(Context context) {
        NetState state = getCurrentNetState(context);
        NetStatus netStatus = new NetStatus(state, state);
        return netStatus;
    }

    /**
     * 获取网络状态
     * @param context
     * @return
     */
    private static NetState getCurrentNetState(Context context) {
        NetState stateCode = NetState.NET_NO;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            switch (ni.getType()) {
                //wifi
                case ConnectivityManager.TYPE_WIFI:
                    stateCode = NetState.NET_WIFI;
                    break;
                //mobile 网络
                case ConnectivityManager.TYPE_MOBILE:
                    switch (ni.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GPRS: //联通2g
                        case TelephonyManager.NETWORK_TYPE_CDMA: //电信2g
                        case TelephonyManager.NETWORK_TYPE_EDGE: //移动2g
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            stateCode = NetState.NET_2G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_EVDO_A: //电信3g
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            stateCode = NetState.NET_3G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE://4G
                            stateCode = NetState.NET_4G;
                            break;
                        //未知,一般不会出现
                        default:
                            stateCode = NetState.NET_UNKNOWN;
                    }
                    break;
                default:
                    stateCode = NetState.NET_UNKNOWN;
            }
        }
        return stateCode;
    }

    public static class NetStatus {
        private NetState lastState;//上一次网路状态
        private NetState currentState;//当前网络状态

        public NetStatus(NetState lastState, NetState currentState) {
            this.lastState = lastState;
            this.currentState = currentState;
        }

        public boolean isLocalNet() {
            return currentState == NetState.NET_2G || currentState == NetState.NET_3G || currentState == NetState.NET_4G || currentState == NetState.NET_UNKNOWN;
        }

        public boolean is4G() {
            return currentState == NetState.NET_4G;
        }

        public boolean isWifi() {
            return currentState == NetState.NET_WIFI;
        }

        public boolean moreThan4G() {
            return isWifi() || is4G();
        }

        public boolean hasNet() {
            return currentState != NetState.NET_NO;
        }

        public void setCurrentState(NetState currentState) {
            this.currentState = currentState;
        }

        public void setLastState(NetState lastState) {
            this.lastState = lastState;
        }

        public NetState getCurrentState() {
            return currentState;
        }

        public NetState getLastState() {
            return lastState;
        }
    }
}
