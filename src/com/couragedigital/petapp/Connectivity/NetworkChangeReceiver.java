package com.couragedigital.petapp.Connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*boolean noConnectivity =
                intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        if (noConnectivity) {
            mState = State.NOT_CONNECTED;
        } else {
            mState = State.CONNECTED;
        }

        Toast.makeText(context, status, Toast.LENGTH_LONG).show();*/
    }
}
