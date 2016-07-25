package com.couragedigital.peto.Holder;

import android.app.Activity;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avocarrot.androidsdk.CustomModel;
import com.avocarrot.androidsdk.VideoView;
import com.avocarrot.androidsdk.ui.AdChoicesView;
import com.couragedigital.peto.Index;
import com.couragedigital.peto.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.internal.request.StringParcel;

import java.util.List;

public class AdMobViewHolder extends RecyclerView.ViewHolder {
    public NativeExpressAdView adView;
    public View view;
    public RelativeLayout adLayout;
    public ImageView avocarrotAdIcon;
    public TextView avocarrotAdHeading;
    public TextView avocarrotAdDescription;
    public ImageView avocarrotAdImage;
    public Button avocarrotAdButton;
    public AdChoicesView avocarrotAdChoices;
    public VideoView avocarrotAdVideo;

    public String avocarrot_native_api_key;
    public String avocarrot_native_placement_key;

    public AdMobViewHolder(View view) {
        super(view);
        this.view = view;
        //adView = (NativeExpressAdView) view.findViewById(R.id.adView);
        adLayout = (RelativeLayout) view.findViewById(R.id.adLayout);
        avocarrotAdIcon = (ImageView) view.findViewById(R.id.avocarrotAdIcon);
        avocarrotAdHeading = (TextView) view.findViewById(R.id.avocarrotAdHeading);
        avocarrotAdDescription = (TextView) view.findViewById(R.id.avocarrotAdDescription);
        if(view.findViewById(R.id.avocarrotAdImage) != null) {
            avocarrotAdImage = (ImageView) view.findViewById(R.id.avocarrotAdImage);
            //avocarrotAdVideo = (VideoView) view.findViewById(R.id.avocarrotAdVideo);
        }
        avocarrotAdButton = (Button) view.findViewById(R.id.avocarrotAdButton);
        avocarrotAdChoices = (AdChoicesView) view.findViewById(R.id.avocarrotAdChoices);
    }

    public void bindAds() {
        String android_id = Settings.Secure.getString(view.getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        /*AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(android_id)
                .build();

        AdRequest request = new AdRequest.Builder().build();

        adView.loadAd(request);*/

        avocarrot_native_api_key = view.getContext().getString(R.string.avocarrot_native_api_key);
        avocarrot_native_placement_key = view.getContext().getString(R.string.avocarrot_native_placement_key);

        final com.avocarrot.androidsdk.AvocarrotCustom avocarrotCustom = new com.avocarrot.androidsdk.AvocarrotCustom(
            (AppCompatActivity) view.getContext(),
            avocarrot_native_api_key,
            avocarrot_native_placement_key
        );
        avocarrotCustom.setSandbox(false);
        avocarrotCustom.setLogger(true, "ALL");

        avocarrotCustom.setListener(new com.avocarrot.androidsdk.AvocarrotCustomListener() {
                    @Override
                    public void onAdLoaded(List<CustomModel> ads) {
                        super.onAdLoaded(ads);
                        if ((ads == null) || (ads.size() < 1)){
                            return;
                        }
                        final CustomModel ad = ads.get(0);

                        avocarrotAdHeading.setText(ad.getTitle());
                        avocarrotAdDescription.setText(ad.getDescription());
                        avocarrotAdButton.setText(ad.getCTAText());

                        avocarrotCustom.loadIcon(ad, avocarrotAdIcon);

                        if(avocarrotAdImage != null) {
                            /*if(ad.getVideo() != null) {
                                avocarrotCustom.loadMedia(ad, avocarrotAdImage, avocarrotAdVideo);
                            }
                            else {
                                avocarrotCustom.loadImage(ad, avocarrotAdImage);
                            }*/
                            avocarrotCustom.loadImage(ad, avocarrotAdImage);
                        }

                        avocarrotCustom.bindView(ad, adLayout, avocarrotAdChoices);

                        avocarrotAdButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                avocarrotCustom.handleClick(ad);
                            }
                        });
                    }
                });
        avocarrotCustom.loadAd();
    }
}
