package com.pdfmaker.erum.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pdfmaker.erum.android.ListManager.HomeFragmentList;
import com.pdfmaker.erum.android.R;

import java.util.ArrayList;

public class HomeFragmentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<HomeFragmentList> homeFragmentLists;

    public HomeFragmentAdapter(Context context, ArrayList<HomeFragmentList> homeFragmentLists) {
        this.context = context;
        this.homeFragmentLists = homeFragmentLists;
    }

    @Override
    public int getCount() {
        return homeFragmentLists.size();
    }

    @Override
    public Object getItem(int position) {
        return homeFragmentLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_home_fragment_layout, parent, false);
        }
        TextView folderName = convertView.findViewById(R.id.home_fragment_folder_name);
        TextView folderFileLength = convertView.findViewById(R.id.home_fragment_file_length);

        folderName.setText(homeFragmentLists.get(position).getFileName());
        folderFileLength.setText(homeFragmentLists.get(position).getFolderLength());


        return convertView;

    }
}
