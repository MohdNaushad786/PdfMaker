package com.pdfmaker.erum.android.Fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.pdfmaker.erum.android.Activities.PdfView;
import com.pdfmaker.erum.android.Adapter.BookmarkFragmentAdapter;
import com.pdfmaker.erum.android.Adapter.RecentFragmentAdapter;
import com.pdfmaker.erum.android.Database.BookmarkDatabase;
import com.pdfmaker.erum.android.Database.RecentDatabase;
import com.pdfmaker.erum.android.ListManager.BookmarkFragmentList;
import com.pdfmaker.erum.android.ListManager.RecentFragmentList;
import com.pdfmaker.erum.android.Methods.KeyStrings;
import com.pdfmaker.erum.android.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class Recent extends Fragment {

    private RecentDatabase recentDatabase;
    private ArrayList<RecentFragmentList> recentFragmentLists;
    private RecentFragmentAdapter recentFragmentAdapter;
    private Cursor cursor;
    private ListView listView;

    public Recent() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent, container, false);

        listView = view.findViewById(R.id.recent_fragment_listView);
        recentFragmentLists = new ArrayList<>();
        recentFragmentAdapter = new RecentFragmentAdapter(getContext(), recentFragmentLists);
        listView.setAdapter(recentFragmentAdapter);
        registerForContextMenu(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), PdfView.class);
                intent.putExtra(KeyStrings.FILE_PATH,recentFragmentLists.get(position).getFilePath());
                startActivity(intent);
            }
        });

        recentDatabase = new RecentDatabase(getActivity());
        cursor = recentDatabase.getRecent();
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No Recent", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                recentFragmentLists.add(new RecentFragmentList(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
                recentFragmentAdapter.notifyDataSetChanged();
            }
            shortListing();
        }

        return view;
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        Objects.requireNonNull(getActivity()).getMenuInflater().inflate(R.menu.long_click_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String pdfName = recentFragmentLists.get(info.position).getFileName();
        String pdfPath = recentFragmentLists.get(info.position).getFilePath();

        if (item.getItemId() == R.id.long_click_remove) {

            recentFragmentLists.remove(recentFragmentLists.get(info.position));
            recentFragmentAdapter.notifyDataSetChanged();
            recentDatabase.deleteRecent(pdfName, pdfPath);

            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void shortListing() {
        Collections.sort(recentFragmentLists, new Comparator<RecentFragmentList>() {
            @Override
            public int compare(RecentFragmentList o1, RecentFragmentList o2) {
                return o2.getFileDate().compareTo(o1.getFileDate());
            }
        });
        recentFragmentAdapter.notifyDataSetChanged();
    }

}
