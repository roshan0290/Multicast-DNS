package com.multicastdns

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.multicastdns.adapters.AdapterScanNetwork
import com.multicastdns.appSupport.PORT
import com.multicastdns.appSupport.SERVICE_NAME
import com.multicastdns.appSupport.SERVICE_TYPE
import com.multicastdns.appSupport.Utility
import com.multicastdns.dataClass.ScannedData
import com.multicastdns.networkDiscovery.NetworkDiscoveryListner
import com.multicastdns.networkDiscovery.NetworkRegistrationListener
import com.multicastdns.networkDiscovery.NetworkResolvedListner
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener,
    NetworkRegistrationListener.NewtorkRegistration,
    NetworkDiscoveryListner.NetworkDiscovery,
    NetworkResolvedListner.ResolveListener
{


    private var mNsdManager: NsdManager? = null
    private var isServicePublished = false
    private var isDisCoveryRunning = false

    var isPublishedClicked = false
    var isScanClicked = false
    var mContext: Context? = null

    private var scanDataAdapter: AdapterScanNetwork? = null

   var disCoveryListener = NetworkDiscoveryListner(this)
    var mRegistrationListener= NetworkRegistrationListener(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        mRegistrationListener = NetworkRegistrationListener(this)
        mNsdManager = Utility.initializeNSDManger(this)


        btn_publish.setOnClickListener(this)
        btn_scan.setOnClickListener(this)

        recycler_view.setLayoutManager(LinearLayoutManager(this))
        recycler_view.setHasFixedSize(true)
        scanDataAdapter = AdapterScanNetwork(this)
        recycler_view.adapter = scanDataAdapter




    }

    override fun onPause() {
        if (mNsdManager != null) {
//            if (isPublishedClicked) {
            unRegisterService()
            //            }
//            if (isScanClicked) {
            stopDisCoverService()
            //            }
        }
        if (countDownTimer != null) {
            stopTimer()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (mNsdManager != null) {
            if (isPublishedClicked) {
                registerService(PORT)
            }
            if (isScanClicked) {
                disCoverService()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /**
     * Discover service
     */
    fun disCoverService() {
        if (!isDisCoveryRunning) {
            isDisCoveryRunning = true
            startTime()
            scanDataAdapter!!.refreshAdapter()
            mNsdManager!!.discoverServices(
                SERVICE_TYPE,
                NsdManager.PROTOCOL_DNS_SD, disCoveryListener
            )
        }
    }

    /**
     * Stop discoverService
     */
    fun stopDisCoverService() {
        if (isDisCoveryRunning) {
            isDisCoveryRunning = false
            mNsdManager!!.stopServiceDiscovery(disCoveryListener)
        }
    }


    /**
     * Register service
     *
     * @param port
     */
    fun registerService(port: Int) {
        val serviceInfo = NsdServiceInfo()
        serviceInfo.serviceName = SERVICE_NAME
        serviceInfo.serviceType = SERVICE_TYPE
        serviceInfo.port = port
        if (!isServicePublished) {
            isServicePublished = true
            mNsdManager!!.registerService(
                serviceInfo, NsdManager.PROTOCOL_DNS_SD,
                mRegistrationListener
            )
        }
    }

    /**
     * Unregister service
     */
    fun unRegisterService() {
        if (isServicePublished) {
            isServicePublished = false
            mNsdManager!!.unregisterService(mRegistrationListener)
        }
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_publish -> {
                isPublishedClicked = true
                isScanClicked = false

                if(Utility.isNetworkAvailable(this)) {
                    registerService(PORT)
                }
                else
                {
                    Utility.showToast(this,"No internet access!")
                }
            }
            R.id.btn_scan -> if (btn_scan.text.toString().equals("SCAN", ignoreCase = true)) {
                isPublishedClicked = false
                isScanClicked = true
                if(Utility.isNetworkAvailable(this)) {
                    disCoverService()
                }
                else
                {
                    Utility.showToast(this,"No internet access!")
                }

            }
        }
    }

    override fun onDeviceRegistration(message: String?) {
        Utility.showToast(this, message)
    }

    override fun onSeriveFound(serviceInfo: NsdServiceInfo?) {
        mNsdManager!!.resolveService(serviceInfo, NetworkResolvedListner(this))
    }

        override fun discoveryStatus(message: String?) {
            Utility.showToast(this, message)
    }

    override fun onDeviceFound(data: ScannedData?) {
        runOnUiThread { scanDataAdapter!!.updateList(data!!) }
    }

    var countDownTimer: CountDownTimer? = null

    fun startTime() {
        countDownTimer = object : CountDownTimer(10000, 1000) {
            // count down for 10seconds
            override fun onTick(millisUntilFinished: Long) {
               // btn_scan.text = "" + millisUntilFinished / 1000
            }

            override fun onFinish() {
                stopDisCoverService()
                btn_scan.text = "SCAN"
            }
        }.start()
    }

    fun stopTimer() {
        btn_scan.text = "SCAN"
        countDownTimer!!.cancel()
    }



}