package kiota

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import cinematic.mutableLiveOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AndroidConnectionObserver(
    private val context: Context,
    private val verifier: ConnectionVerifier,
    private val scope: CoroutineScope
) : ConnectionObserver {

    override val status by lazy { mutableLiveOf<Connection>(Connecting) }

    private val manager by lazy { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

    private var callback: ConnectivityManager.NetworkCallback? = null

    //    @androidx.annotation.RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    override fun start() {
        if (callback != null) return
        val cb = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                status.value = Connecting
                scope.launch {
                    status.value = verifier.verify()
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                status.value = Disconnected
            }

            override fun onLosing(network: Network, maxMsToLive: Int) {
                super.onLosing(network, maxMsToLive)
                status.value = Connecting
            }

            override fun onUnavailable() {
                super.onUnavailable()
                status.value = Disconnected
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            manager.registerDefaultNetworkCallback(cb)
        } else {
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) // Interested in internet capability
                .build()
            manager.registerNetworkCallback(networkRequest, cb)
        }

        callback = cb
    }

    override fun stop() {
        val cb = callback ?: return
        manager.unregisterNetworkCallback(cb)
        callback = null
    }
}