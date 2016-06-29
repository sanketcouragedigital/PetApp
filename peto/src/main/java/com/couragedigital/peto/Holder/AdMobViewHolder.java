package com.couragedigital.peto.Holder;

import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.couragedigital.peto.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

public class AdMobViewHolder extends RecyclerView.ViewHolder {
    public NativeExpressAdView adView;
    public View view;

    public AdMobViewHolder(View view) {
        super(view);
        this.view = view;
        adView = (NativeExpressAdView) view.findViewById(R.id.adView);
    }

    public void bindAds() {
        String android_id = Settings.Secure.getString(view.getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

//        AdRequest request = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice(android_id)
//                .build();

        AdRequest request = new AdRequest.Builder().build();

        adView.loadAd(request);
    }
}
