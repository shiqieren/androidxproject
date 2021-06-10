package com.liyiwei.baselibrary.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;


import com.liyiwei.baselibrary.R;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.net.ConnectivityManager.TYPE_MOBILE;
import static android.net.ConnectivityManager.TYPE_WIFI;


public class NetworkChangeReceiver extends BroadcastReceiver {
	private NetworkChangeListener listener;

	public void setListener(NetworkChangeListener listener) {
		this.listener = listener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		ConnectivityManager connectionManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			switch (networkInfo.getType()) {
				case TYPE_MOBILE:
					if (listener != null) {
						listener.onMobile();
					}
					break;
				case TYPE_WIFI:
					if (listener != null) {
						listener.onWifi();
					}
					break;
				default:
					break;
			}
		} else {
			Toast.makeText(context, "not network", Toast.LENGTH_SHORT).show();
		}
	}

	public  interface NetworkChangeListener {
		void onMobile();

		void onWifi();
	}
}