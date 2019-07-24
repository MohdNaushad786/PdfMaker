package com.pdfmaker.erum.android.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pdfmaker.erum.android.Adapter.PdfFileAdapter;
import com.pdfmaker.erum.android.ListManager.PdfFileList;
import com.pdfmaker.erum.android.Methods.Ads;
import com.pdfmaker.erum.android.Methods.KeyStrings;
import com.pdfmaker.erum.android.Methods.Methods;
import com.pdfmaker.erum.android.R;

import java.io.File;
import java.security.Key;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PdfFile extends AppCompatActivity {

    private ArrayList<PdfFileList> pdfFileLists;
    private PdfFileAdapter pdfFileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_file_list);

        // Interstitial 1 Ads Back Pressed
        Ads.Interstitial1(this);

        Intent intent = getIntent();
        File file = new File(intent.getStringExtra(KeyStrings.FILE_PATH));

        ListView listView = findViewById(R.id.pdfFileList);
        pdfFileLists = new ArrayList<>();
        pdfFileAdapter = new PdfFileAdapter(this, pdfFileLists);
        listView.setAdapter(pdfFileAdapter);

        ArrayList<File> pdfFile = getFile(file);
        pdfList(pdfFile);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PdfFile.this, PdfView.class);
                intent.putExtra(KeyStrings.FILE_PATH,pdfFileLists.get(position).getFilePath());
                startActivity(intent);
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

    private void pdfList(ArrayList<File> fileLists) {
        for (int i = 0; i < fileLists.size(); i++) {
            pdfFileLists.add(new PdfFileList(fileLists.get(i).getName(),
                    fileLists.get(i).getAbsolutePath(), Methods.fileSize(fileLists.get(i))
                    ,Methods.fileDate(fileLists.get(i))));
        }
    }

    private ArrayList<File> getFile(File root) {
        ArrayList<File> arrayList = new ArrayList<>();

        try {
            File[] listFile = root.listFiles();
            for (File singleFile : listFile) {
                if (singleFile.getName().endsWith(KeyStrings.PDF_EXTENSION)) {
                    arrayList.add(singleFile.getAbsoluteFile());
                }
            }

        } catch (NullPointerException n) {
            n.printStackTrace();
        }

        return arrayList;
    }
}
