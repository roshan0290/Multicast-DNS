package com.multicastdns.networkDiscovery

import android.annotation.SuppressLint
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.multicastdns.dataClass.ScannedData
import java.util.*

import io.reactivex.Observable

import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.observers.DisposableObserver

import io.reactivex.schedulers.Schedulers


class NetworkResolvedListner (resolveListener: ResolveListener?): NsdManager.ResolveListener {
    var myResolveListener: ResolveListener? = resolveListener
    var observable : Observable<ScannedData>? = null

    interface ResolveListener {
        fun onDeviceFound(data: ScannedData?)
    }



    override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {}

    @SuppressLint("CheckResult")
    override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
        var data = ScannedData()
        data.hostAddress = serviceInfo.host
        data.port = serviceInfo.port
        data.serviceName = serviceInfo.serviceName
        data.serviceType= serviceInfo.serviceType
        observable = Observable.just(data)
        (observable as Observable<ScannedData>?)?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ result -> myResolveListener?.onDeviceFound(result) })
    }
}
