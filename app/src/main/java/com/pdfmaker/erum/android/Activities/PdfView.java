package com.pdfmaker.erum.android.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.pdfmaker.erum.android.Database.BookmarkDatabase;
import com.pdfmaker.erum.android.Database.RecentDatabase;
import com.pdfmaker.erum.android.Methods.Ads;
import com.pdfmaker.erum.android.Methods.KeyStrings;
import com.pdfmaker.erum.android.Methods.Methods;
import com.pdfmaker.erum.android.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class PdfView extends AppCompatActivity {

    private PDFView pdfView;
    private int pageNo;
    private File file;
    private BookmarkDatabase bookmarkDatabase;
    private String password;
    private Dialog dialog;
    private String filePath;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);

        // Interstitial 1 Ads Back Pressed
        Ads.Interstitial1(this);

        //Declaration
        Intent intent = getIntent();
        pdfView = findViewById(R.id.pdfView);
        bookmarkDatabase = new BookmarkDatabase(this);

        // Intent
        String action = intent.getAction();
        String type = intent.getType();
        pageNo = intent.getIntExtra(KeyStrings.PAGE_NO, 0);
        filePath = intent.getStringExtra(KeyStrings.FILE_PATH);
        uri = intent.getData();

        // getData From saved Instance
        if (savedInstanceState != null) {
            pageNo = savedInstanceState.getInt(KeyStrings.SAVED_PAGE_NO);
        }

        // Pdf File Opener
        if (Intent.ACTION_VIEW.equals(action) && Objects.requireNonNull(type).endsWith("pdf")) {
            if (uri != null) {
                filePath = uri.getPath();
                file = new File(Objects.requireNonNull(filePath));
            } else {
                filePath = "No file";
            }
        } else if (Intent.ACTION_SEND.equals(action) && Objects.requireNonNull(type).endsWith("pdf")) {
            uri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
            filePath = uri.getPath();
            file = new File(Objects.requireNonNull(filePath));
        } else {
            file = new File(filePath);
        }

        pdfStart();
        insertRecent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.pdf_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.pdf_bookmark:
                bookmarkDatabase.insertBookmark(file.getName(), pdfView.getCurrentPage(), file.getAbsolutePath(), getDate());
                Toast.makeText(getApplicationContext(), "Page " + currentPage() + " Bookmark Added", Toast.LENGTH_SHORT).show();
                break;

            case R.id.pdf_goto:
                showGoToPageDialog();
                break;
        }

        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KeyStrings.SAVED_PAGE_NO, pdfView.getCurrentPage());
    }

    @Override
    public void onBackPressed() {
        KeyStrings.INTERSTITIAL_ADS_1_CLICK_COUNT++;
        if (Ads.interstitialAd1.isAdLoaded() &&
                KeyStrings.INTERSTITIAL_ADS_1_CLICK_COUNT %
                        KeyStrings.INTERSTITIAL_ADS_1_CLICK_DIVIDED_BY == 0) {

            Ads.interstitialAd1.show();
        } else {
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

    private String getDate() {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatter = new SimpleDateFormat(KeyStrings.DATE_FORMAT);
        Date date = new Date();
        return formatter.format(date);
    }

    private void pdfStart() {
        // Pdf viewer
        pdfView.fromFile(file)
                .defaultPage(pageNo)
                .enableAnnotationRendering(true)
                .enableAntialiasing(true)
                .spacing(8)
                .password(password)
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                        pdfView.fitToWidth(pageNo);
                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        if (t.toString().contains("Password required or incorrect password.")) {
                            passwordProtected();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Loading", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }
                })
                .swipeHorizontal(false)
                .enableSwipe(true)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();

    }

    private void showGoToPageDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_go_to_page_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText getPageNo = dialog.findViewById(R.id.dialog_pageNo_edit_text);
        getPageNo.setHint("1 - " + pdfView.getPageCount());
        TextView cancel = dialog.findViewById(R.id.dialog_pageNo_cancel);
        TextView ok = dialog.findViewById(R.id.dialog_pageNo_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = getPageNo.getText().toString();
                if (no.matches("")) {
                    getPageNo.setHintTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Write Page No", Toast.LENGTH_SHORT).show();
                } else {
                    int pageNumber = Integer.parseInt(no);
                    pageNo = pageNumber - 1;
                    pdfStart();
                    dialog.dismiss();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void passwordProtected() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_password_popup);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText getPassword = dialog.findViewById(R.id.dialog_password_edit_text);

        TextView cancel = dialog.findViewById(R.id.dialog_password_cancel);
        TextView ok = dialog.findViewById(R.id.dialog_password_ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = getPassword.getText().toString();
                if (pass.matches("")) {
                    getPassword.setTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
                } else {
                    password = pass;
                    pdfStart();
                    dialog.dismiss();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private int currentPage() {
        return pdfView.getCurrentPage() + 1;
    }

    private void insertRecent() {
        RecentDatabase recentDatabase = new RecentDatabase(this);
        if (recentDatabase.isRecentDataInserted(file.getName(), file.getPath())) {
            recentDatabase.updateRecent(file.getName(), file.getPath(), getDate());
        } else {
            recentDatabase.insertRecent(file.getName(), file.getPath(), Methods.fileSize(file), getDate());
        }
    }
}

