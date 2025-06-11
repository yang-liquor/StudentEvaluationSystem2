package com.studentevaluation.ui;

import com.studentevaluation.entity.User;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.*;

/**
 * 教师改进面板，支持历史记录持久化到本地文件
 */
public class TeacherImprovePanel extends JPanel {
    private User user;
    private JTextArea improvementArea;
    private JButton submitButton;
    private DefaultListModel<String> recordModel;
    private JList<String> recordList;

    private String recordFilePath;

    public TeacherImprovePanel(User user) {
        this.user = user;
        // 建议用工号或唯一标识，避免重名
        String fileSafeName = user.getUserId().replaceAll("[^a-zA-Z0-9_\u4e00-\u9fa5]", "_");
        recordFilePath = "improve_" + fileSafeName + ".txt";

        setLayout(new BorderLayout(10,10));

        // 改进措施输入区
        improvementArea = new JTextArea(4, 30);
        improvementArea.setBorder(BorderFactory.createTitledBorder("请填写本课程的教学改进措施"));
        add(new JScrollPane(improvementArea), BorderLayout.NORTH);

        // 提交按钮
        submitButton = new JButton("提交改进措施");
        submitButton.addActionListener(e -> handleSubmit());
        JPanel btnPanel = new JPanel();
        btnPanel.add(submitButton);
        add(btnPanel, BorderLayout.CENTER);

        // 历史记录展示区
        recordModel = new DefaultListModel<>();
        recordList = new JList<>(recordModel);
        recordList.setBorder(BorderFactory.createTitledBorder("历史改进记录"));
        add(new JScrollPane(recordList), BorderLayout.SOUTH);

        // 启动时读取历史记录
        loadHistory();
    }

    // 处理提交并写入本地文件
    private void handleSubmit() {
        String improvement = improvementArea.getText().trim();
        if (improvement.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先填写改进措施！");
            return;
        }
        // 加上时间戳
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String record = timestamp + " - " + improvement;

        // 先追加到文件
        try (FileWriter fw = new FileWriter(recordFilePath, true)) {
            fw.write(record + System.lineSeparator());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "保存失败: " + e.getMessage());
            return;
        }
        // 再加到界面
        recordModel.addElement(record);
        improvementArea.setText("");
        JOptionPane.showMessageDialog(this, "教学改进措施已提交，谢谢！");
    }

    // 加载历史记录
    private void loadHistory() {
        recordModel.clear();
        File file = new File(recordFilePath);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                recordModel.addElement(line);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "读取历史记录失败: " + e.getMessage());
        }
    }
}