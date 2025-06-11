package com.studentevaluation.ui;

public class ImportExportRecord {
    public String action;
    public String fileName;
    public String time;

    public ImportExportRecord(String action, String fileName, String time) {
        this.action = action;
        this.fileName = fileName;
        this.time = time;
    }
}