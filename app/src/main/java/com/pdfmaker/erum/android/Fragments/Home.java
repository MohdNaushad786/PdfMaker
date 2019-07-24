package com.pdfmaker.erum.android.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pdfmaker.erum.android.Activities.MainActivity;
import com.pdfmaker.erum.android.Adapter.HomeFragmentAdapter;
import com.pdfmaker.erum.android.ListManager.HomeFragmentList;
import com.pdfmaker.erum.android.Activities.PdfFile;
import com.pdfmaker.erum.android.Methods.KeyStrings;
import com.pdfmaker.erum.android.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Home extends Fragment {

    private ArrayList<HomeFragmentList> homeFragmentLists;
    private HomeFragmentAdapter homeFragmentAdapter;

    public Home() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);


        ListView listView = view.findViewById(R.id.fragment_home_listView);
        homeFragmentLists = new ArrayList<>();
        homeFragmentAdapter = new HomeFragmentAdapter(getContext(), homeFragmentLists);
        listView.setAdapter(homeFragmentAdapter);

        showList(MainActivity.allFiles);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getActivity(), PdfFile.class);
                intent.putExtra(KeyStrings.FILE_PATH, homeFragmentLists.get(position).getFilePath());
                startActivity(intent);
            }
        });

        return view;
    }

    public void showList(ArrayList<File> fileLists) {
        for (int i = 0; i < fileLists.size(); i++) {
            homeFragmentLists.add(new HomeFragmentList(fileLists.get(i).getName(),
                    fileLists.get(i).getAbsolutePath(), folderLength(fileLists.get(i))));
        }
        sortListing();
    }

    private String folderLength(File files) {
        int a = 0;
        File[] listFile = files.listFiles();
        for (File file : listFile) {
            if (file.getName().endsWith(KeyStrings.PDF_EXTENSION)) {
                a++;
            }
        }
        return a + " pdf files";
    }

    private void sortListing() {
        Collections.sort(homeFragmentLists, new Comparator<HomeFragmentList>() {
            @Override
            public int compare(HomeFragmentList o1, HomeFragmentList o2) {
                return o1.getFileName().compareTo(o2.getFileName());
            }
        });
        homeFragmentAdapter.notifyDataSetChanged();
    }

}
