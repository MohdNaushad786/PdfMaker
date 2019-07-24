package com.pdfmaker.erum.android.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Filter;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.karan.churi.PermissionManager.PermissionManager;
import com.pdfmaker.erum.android.Fragments.Bookmark;
import com.pdfmaker.erum.android.Fragments.Home;
import com.pdfmaker.erum.android.Fragments.Recent;
import com.pdfmaker.erum.android.Methods.KeyStrings;
import com.pdfmaker.erum.android.Methods.Methods;
import com.pdfmaker.erum.android.R;
import com.pdfmaker.erum.android.Service.FirebaseService;

import java.io.File;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    public static ArrayList<File> allFiles = new ArrayList<>();
    private static final String NOTIFICATION_CHANNEL_ID = "ChannelID";
    private static final String NOTIFICATION_CHANNEL_NAME = "ChannelName";
    private static final String NOTIFICATION_CHANNEL_DESCRIPTION = "ChannelDescription";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendNotification();


        // Declaration
        PermissionManager permissionManager = new PermissionManager() {
        };

        Methods.rateUs(MainActivity.this);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Async
            new FileLoading().execute();
        } else {
            // Permission Manager
            permissionManager.checkAndRequestPermissions(this);

        }

        // Bottom Navigation View
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;

                switch (item.getItemId()) {
                    case R.id.bottom_home:
                        fragment = new Home();
                        break;

                    case R.id.bottom_bookmark:
                        fragment = new Bookmark();
                        break;

                    case R.id.bottom_recent:
                        fragment = new Recent();
                        break;

                }
                return loadFragment(fragment);

            }

        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Async
            new FileLoading().execute();
        }
    }

    // Adding Side Menu Item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    // onClick Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.privacy_policy: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hayatulerum.blogspot.com/p/privacy-policy-pdf-viewer.html"));
                startActivity(intent);
                return true;
            }
            case R.id.terms_condition: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://hayatulerum.blogspot.com/p/terms-condition-pdf-viewer.html"));
                startActivity(intent);
                return true;
            }
            case R.id.about_us: {
                Intent intent = new Intent(MainActivity.this, AboutUs.class);
                startActivity(intent);
                return true;
            }

            case R.id.feedback: {
                Intent intent = new Intent(MainActivity.this, Feedback.class);
                startActivity(intent);
                return true;
            }
            case R.id.rate_us: {
                try{
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                }
                catch (ActivityNotFoundException e){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                }
                return true;
            }
            case R.id.more_apps: {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=7113351486005914962"));
                startActivity(intent);
                return true;
            }
        }

        return true;
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @SuppressLint("StaticFieldLeak")
    class FileLoading extends AsyncTask<String, String, ArrayList<File>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected final ArrayList<File> doInBackground(String... strings) {

            ArrayList<File> internalLists = Methods.getFile(Methods.internalPath());
            ArrayList<File> arrayList = new ArrayList<>(internalLists);

            if (Methods.isSdCardAvailable()) {
                ArrayList<File> sdCardLists = Methods.getFile(Methods.sdCardPath());
                arrayList.addAll(sdCardLists);
            }

            return arrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<File> files) {
            super.onPostExecute(files);
            progressDialog.dismiss();
            allFiles.clear();
            allFiles.addAll(files);
            loadFragment(new Home());
        }
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

        }
    };

    // Notification
    public void sendNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);

            notificationChannel.setDescription(NOTIFICATION_CHANNEL_DESCRIPTION);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this,AboutUs.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        //Get an instance of NotificationManager
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setColor(Color.GREEN)
                .setContentTitle("Hi")
                .setContentText("Hello")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        // Showing Notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

    }
}