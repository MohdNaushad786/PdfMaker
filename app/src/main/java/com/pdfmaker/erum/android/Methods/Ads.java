package com.pdfmaker.erum.android.Methods;

import android.app.Activity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

public class Ads {

    private static final String INTERSTITIAL_1 = "422073971687935_422074965021169";


    public static InterstitialAd interstitialAd1;


    public static void Interstitial1(final Activity activity) {

        interstitialAd1 = new InterstitialAd(activity, INTERSTITIAL_1);
        interstitialAd1.loadAd();
        interstitialAd1.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                interstitialAd1.loadAd();
                activity.finish();
            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });

    }

}
