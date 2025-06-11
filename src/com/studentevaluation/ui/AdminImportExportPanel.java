package com.studentevaluation.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminImportExportPanel extends JPanel {
    private static final String RECORD_FILE = "records.json";
    private DefaultTableModel tableModel;
    private JTable table;
    private List<ImportExportRecord> records = new ArrayList<>();

    public AdminImportExportPanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"操作", "文件名", "时间"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 自动加载历史
        loadRecordsFromFile(RECORD_FILE);
    }

    public void addRecord(ImportExportRecord record) {
        records.add(record);
        tableModel.addRow(new Object[]{record.action, record.fileName, record.time});
        saveRecordsToFile(RECORD_FILE);
    }

    private void loadRecordsFromFile(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            List<ImportExportRecord> loaded =
                    gson.fromJson(reader, new TypeToken<List<ImportExportRecord>>(){}.getType());
            if (loaded != null) {
                for (ImportExportRecord r : loaded) addRecordNoSave(r);
            }
        } catch (Exception ignored) {}
    }

    private void addRecordNoSave(ImportExportRecord record) {
        records.add(record);
        tableModel.addRow(new Object[]{record.action, record.fileName, record.time});
    }

    private void saveRecordsToFile(String filePath) {
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new Gson();
            gson.toJson(records, writer);
        } catch (Exception ex) {}
    }

    public List<ImportExportRecord> getRecords() {
        return records;
    }
}