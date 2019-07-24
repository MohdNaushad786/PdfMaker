package com.pdfmaker.erum.android.ListManager;

public class BookmarkFragmentList {

    private String pdfName;
    private int pageNo;
    private String path;
    private String date;

    public BookmarkFragmentList() {
    }

    public BookmarkFragmentList(String pdfName, int pageNo, String path, String date) {
        this.pdfName = pdfName;
        this.pageNo = pageNo;
        this.path = path;
        this.date = date;
    }

    public String getPdfName() {
        return pdfName;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
