package com.pdfmaker.erum.android.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.Toast;

import com.pdfmaker.erum.android.Activities.PdfFile;
import com.pdfmaker.erum.android.Activities.PdfView;
import com.pdfmaker.erum.android.Adapter.BookmarkFragmentAdapter;
import com.pdfmaker.erum.android.Database.BookmarkDatabase;
import com.pdfmaker.erum.android.ListManager.BookmarkFragmentList;
import com.pdfmaker.erum.android.Methods.KeyStrings;
import com.pdfmaker.erum.android.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.zip.Inflater;

public class Bookmark extends Fragment {

    private BookmarkDatabase bookmarkDatabase;
    private ArrayList<BookmarkFragmentList> bookmarkFragmentLists;
    private BookmarkFragmentAdapter bookmarkFragmentAdapter;
    private Cursor cursor;
    private ListView listView;

    public Bookmark() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_bookmark, container, false);

        listView = view.findViewById(R.id.bookmark_fragment_listView);
        bookmarkFragmentLists = new ArrayList<>();
        bookmarkFragmentAdapter = new BookmarkFragmentAdapter(getContext(), bookmarkFragmentLists);
        listView.setAdapter(bookmarkFragmentAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int page = bookmarkFragmentLists.get(position).getPageNo() - 1;
                Intent intent = new Intent(getActivity(), PdfView.class);
                intent.putExtra(KeyStrings.FILE_PATH, bookmarkFragmentLists.get(position).getPath());
                intent.putExtra(KeyStrings.PAGE_NO, page);
                startActivity(intent);
            }
        });

        registerForContextMenu(listView);

        bookmarkDatabase = new BookmarkDatabase(getActivity());
        cursor = bookmarkDatabase.getBookmark();
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No Bookmark", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                bookmarkFragmentLists.add(new BookmarkFragmentList(cursor.getString(1), getPageNo(), cursor.getString(3), cursor.getString(4)));
                bookmarkFragmentAdapter.notifyDataSetChanged();
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

        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        String pdfName = bookmarkFragmentLists.get(info.position).getPdfName();
        String pdfPath = bookmarkFragmentLists.get(info.position).getPath();

        if (item.getItemId() == R.id.long_click_remove) {
            bookmarkFragmentLists.remove(bookmarkFragmentLists.get(info.position));
            bookmarkFragmentAdapter.notifyDataSetChanged();

            bookmarkDatabase.deleteBookmark(pdfName, pdfPath);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void shortListing() {
        Collections.sort(bookmarkFragmentLists, new Comparator<BookmarkFragmentList>() {
            @Override
            public int compare(BookmarkFragmentList o1, BookmarkFragmentList o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        bookmarkFragmentAdapter.notifyDataSetChanged();
    }

    private int getPageNo() {
        int pageNo = cursor.getInt(2);
        pageNo = pageNo + 1;
        return pageNo;
    }

}
