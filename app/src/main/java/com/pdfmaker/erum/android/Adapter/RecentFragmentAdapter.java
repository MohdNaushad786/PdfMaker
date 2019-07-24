package com.pdfmaker.erum.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pdfmaker.erum.android.ListManager.HomeFragmentList;
import com.pdfmaker.erum.android.ListManager.RecentFragmentList;
import com.pdfmaker.erum.android.R;

import java.util.ArrayList;

public class RecentFragmentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<RecentFragmentList> recentFragmentLists;

    public RecentFragmentAdapter(Context context, ArrayList<RecentFragmentList> recentFragmentLists) {
        this.context = context;
        this.recentFragmentLists = recentFragmentLists;
    }

    @Override
    public int getCount() {
        return recentFragmentLists.size();
    }

    @Override
    public Object getItem(int position) {
        return recentFragmentLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_recent_fragment_layout, parent, false);
        }
        TextView fileName = convertView.findViewById(R.id.recent_file_name);
        TextView filePath = convertView.findViewById(R.id.recent_file_size);
        TextView fileDate = convertView.findViewById(R.id.recent_file_date);

        fileName.setText(recentFragmentLists.get(position).getFileName());
        filePath.setText(recentFragmentLists.get(position).getFileSize());
        fileDate.setText(recentFragmentLists.get(position).getFileDate());

        return convertView;
    }
}
