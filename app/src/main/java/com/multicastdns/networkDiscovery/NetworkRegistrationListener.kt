package com.multicastdns.networkDiscovery

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo

class NetworkRegistrationListener (_registration: NewtorkRegistration?): NsdManager.RegistrationListener {

    var registration: NewtorkRegistration? = _registration

    interface NewtorkRegistration {
        fun onDeviceRegistration(message: String?)
    }


    override fun onRegistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
        registration?.onDeviceRegistration("Registration failed")
    }

    override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
        registration?.onDeviceRegistration("Unregistration failed")
    }

    override fun onServiceRegistered(serviceInfo: NsdServiceInfo?) {
        registration?.onDeviceRegistration("Service registered")
    }

    override fun onServiceUnregistered(serviceInfo: NsdServiceInfo?) {
        registration?.onDeviceRegistration("Service unregistered")
    }
}
