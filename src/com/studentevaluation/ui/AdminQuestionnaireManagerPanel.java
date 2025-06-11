package com.studentevaluation.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AdminQuestionnaireManagerPanel extends JPanel {
    private static final String DIR = "questionnaires";
    private DefaultListModel<String> fileListModel = new DefaultListModel<>();
    private JList<String> fileList = new JList<>(fileListModel);
    private AdminEvaluateSettingPanel editorPanel;

    public AdminQuestionnaireManagerPanel() {
        setLayout(new BorderLayout());

        // 左侧问卷文件列表
        JPanel left = new JPanel(new BorderLayout());
        left.add(new JLabel("问卷库", SwingConstants.CENTER), BorderLayout.NORTH);
        left.add(new JScrollPane(fileList), BorderLayout.CENTER);
        JButton newBtn = new JButton("新建问卷");
        JButton delBtn = new JButton("删除问卷");
        JPanel btns = new JPanel();
        btns.add(newBtn); btns.add(delBtn);
        left.add(btns, BorderLayout.SOUTH);

        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.addListSelectionListener(e -> loadSelectedFile());

        add(left, BorderLayout.WEST);

        // 右侧问卷编辑器（复用你的原有编辑面板）
        editorPanel = new AdminEvaluateSettingPanel(null, false); // 禁用保存/导入导出按钮，只用保存为
        add(editorPanel, BorderLayout.CENTER);

        refreshFileList();

        newBtn.addActionListener(e -> editorPanel.clearAndNew());
        delBtn.addActionListener(e -> {
            String file = fileList.getSelectedValue();
            if (file != null && JOptionPane.showConfirmDialog(this, "确定要删除？") == 0) {
                new File(DIR, file).delete();
                refreshFileList();
            }
        });
    }

    private void refreshFileList() {
        fileListModel.clear();
        File folder = new File(DIR);
        if (!folder.exists()) folder.mkdir();
        File[] files = folder.listFiles((d, n) -> n.endsWith(".json"));
        if (files != null) for (File f : files) fileListModel.addElement(f.getName());
    }

    private void loadSelectedFile() {
        String file = fileList.getSelectedValue();
        if (file != null) editorPanel.loadQuestionnaireFromFile(new File(DIR, file).getPath());
    }
}