package com.pdfmaker.erum.android.Methods;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;

import hotchemi.android.rate.AppRate;
import me.anwarshahriar.calligrapher.Calligrapher;

public class Methods {

    public static boolean isSdCardAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File sdCardPath(){
        return new File("/storage");
    }

    public static File internalPath(){
        return Environment.getExternalStorageDirectory();
    }

    public static String fileSize(File file){
        double length = file.length();

        if (length > 1024 && length <= 1024*1024){
            length = length/1024;
            return new DecimalFormat("##.##").format(length) + " Kb";
        }
        else if (length > 1024*1024 && length <= 1024*1024*1024 ){
            length = length/1048576;
            return new DecimalFormat("##.##").format(length) + " Mb";
        }
        else if (length > 1024*1024*1024){
            length = length/1073741824;
            return new DecimalFormat("##.##").format(length) + " Gb";
        }else {
            return new DecimalFormat("##.##").format(length) + " Bytes";
        }

    }

    public static String fileDate(File file) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(KeyStrings.DATE_FORMAT);
        return simpleDateFormat.format(file.lastModified());
    }

    public static ArrayList<File> getFile(File root) {
        ArrayList<File> arrayList = new ArrayList<>();

        try {
            File[] listFile = root.listFiles();
            for (File singleFile : listFile) {
                if (singleFile.isDirectory() && !singleFile.isHidden()) {
                    arrayList.addAll(getFile(singleFile));
                } else {
                    if (singleFile.getName().endsWith(KeyStrings.PDF_EXTENSION)) {
                        arrayList.add(singleFile.getParentFile());
                        HashSet<File> hashSet = new HashSet<>(arrayList);
                        arrayList.clear();
                        arrayList.addAll(hashSet);

                    }
                }
            }

        } catch (NullPointerException n) {
            n.printStackTrace();
        }


        return arrayList;
    }

    public static void font(Context context, Activity activity) {
        Calligrapher calligrapher = new Calligrapher(context);
        calligrapher.setFont(activity, "font.otf", true);
    }

    public static void rateUs(Activity activity){
        AppRate.with(activity)
                .setInstallDays(0)
                .setLaunchTimes(5)
                .setRemindInterval(1)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(activity);


    }


}
