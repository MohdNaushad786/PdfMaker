package com.pdfmaker.erum.android.ListManager;

public class RecentFragmentList {

    private String fileName;
    private String filePath;
    private String fileSize;
    private String fileDate;

    public RecentFragmentList() {
    }

    public RecentFragmentList(String fileName, String filePath, String fileSize, String fileDate) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileDate = fileDate;
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

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileDate() {
        return fileDate;
    }

    public void setFileDate(String fileDate) {
        this.fileDate = fileDate;
    }
}
