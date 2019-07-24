package com.pdfmaker.erum.android.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pdfmaker.erum.android.ListManager.PdfFileList;
import com.pdfmaker.erum.android.R;

import java.util.ArrayList;

public class PdfFileAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<PdfFileList> pdfFileLists;

    public PdfFileAdapter(Context context, ArrayList<PdfFileList> pdfFileLists) {
        this.context = context;
        this.pdfFileLists = pdfFileLists;
    }

    @Override
    public int getCount() {
        return pdfFileLists.size();
    }

    @Override
    public Object getItem(int position) {
        return pdfFileLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_pdf_file_layout, parent, false);
        }
        TextView fileName = convertView.findViewById(R.id.file_name);
        TextView filePath = convertView.findViewById(R.id.file_size);
        TextView fileDate = convertView.findViewById(R.id.file_date);

        fileName.setText(pdfFileLists.get(position).getFileName());
        filePath.setText(pdfFileLists.get(position).getFileSize());
        fileDate.setText(pdfFileLists.get(position).getFileDate());

        return convertView;
    }
}
