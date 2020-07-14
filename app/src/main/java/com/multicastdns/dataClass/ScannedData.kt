package com.multicastdns.dataClass

import java.net.InetAddress

data class ScannedData (


     var serviceType: String? = null,
     var serviceName: String? = null,
     var port: Int = 0,
     var hostAddress: InetAddress? = null


)