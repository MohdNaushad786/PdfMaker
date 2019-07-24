package com.pdfmaker.erum.android.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.pdfmaker.erum.android.Methods.Ads;
import com.pdfmaker.erum.android.Methods.KeyStrings;
import com.pdfmaker.erum.android.Methods.Methods;
import com.pdfmaker.erum.android.R;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // Interstitial 1 Ads Back Pressed
        Ads.Interstitial1(this);

        // Font
        Methods.font(getApplicationContext(),AboutUs.this);

    }

    @Override
    public void onBackPressed() {
        KeyStrings.INTERSTITIAL_ADS_1_CLICK_COUNT++;
        if (Ads.interstitialAd1.isAdLoaded() &&
                KeyStrings.INTERSTITIAL_ADS_1_CLICK_COUNT %
                        KeyStrings.INTERSTITIAL_ADS_1_CLICK_DIVIDED_BY == 0) {

            Ads.interstitialAd1.show();
        }
        else {
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        if (Ads.interstitialAd1 != null) {
            Ads.interstitialAd1.destroy();
        }
        super.onDestroy();
    }
}
