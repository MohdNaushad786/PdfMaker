package com.pdfmaker.erum.android.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pdfmaker.erum.android.Methods.Ads;
import com.pdfmaker.erum.android.Methods.KeyStrings;
import com.pdfmaker.erum.android.R;

public class Feedback extends AppCompatActivity {

    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Interstitial 1 Ads Back Pressed
        Ads.Interstitial1(this);

        //Declaration
        editText = findViewById(R.id.feedback_edittext_message);
        Button button = findViewById(R.id.feedback_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String subject = "Pdf Viewer";
                String message = editText.getText().toString();

                Intent it = new Intent(Intent.ACTION_SENDTO);
                it.setData(Uri.parse("mailto:"+ "hayatulerum@gmail.com"));
                it.putExtra(Intent.EXTRA_SUBJECT, subject);
                it.putExtra(Intent.EXTRA_TEXT, message);
                if (it.resolveActivity(getPackageManager()) != null) {
                    startActivity(Intent.createChooser(it, "Choose an email client "));
                }

            }
        });

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
