package com.pdfmaker.erum.android.ListManager;

public class HomeFragmentList {

    private String fileName;
    private String filePath;
    private String folderLength;

    public HomeFragmentList() {
    }

    public HomeFragmentList(String fileName, String filePath, String folderLength) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.folderLength = folderLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFolderLength() {
        return folderLength;
    }

    public void setFolderLength(String folderLength) {
        this.folderLength = folderLength;
    }
}
