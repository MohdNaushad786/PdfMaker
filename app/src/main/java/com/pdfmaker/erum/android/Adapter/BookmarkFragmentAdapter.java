package com.pdfmaker.erum.android.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pdfmaker.erum.android.ListManager.BookmarkFragmentList;
import com.pdfmaker.erum.android.R;

import java.util.ArrayList;

public class BookmarkFragmentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BookmarkFragmentList> bookmarkFragmentList;

    public BookmarkFragmentAdapter(Context context, ArrayList<BookmarkFragmentList> bookmarkFragmentList) {
        this.context = context;
        this.bookmarkFragmentList = bookmarkFragmentList;
    }

    @Override
    public int getCount() {
        return bookmarkFragmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return bookmarkFragmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_bookmark_fragment_layout, parent, false);
        }

        TextView pdfName = convertView.findViewById(R.id.bookmark_file_name);
        TextView pageNo = convertView.findViewById(R.id.bookmark_pageNo);
        TextView dateTime = convertView.findViewById(R.id.bookmark_time_date);

        pdfName.setText(bookmarkFragmentList.get(position).getPdfName());
        pageNo.setText("Page " + bookmarkFragmentList.get(position).getPageNo());
        dateTime.setText(bookmarkFragmentList.get(position).getDate());

        return convertView;
    }
}
