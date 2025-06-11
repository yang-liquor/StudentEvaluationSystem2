package com.studentevaluation.ui;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class AdminStatPanel extends JPanel {
    private static final String QUESTIONNAIRE_DIR = "questionnaires";
    private static final String ANSWER_DIR = "answers";
    private JTextArea statArea;

    public AdminStatPanel() {
        setLayout(new BorderLayout());
        statArea = new JTextArea();
        statArea.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        statArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statArea);

        JButton refreshBtn = new JButton("刷新统计");
        refreshBtn.addActionListener(e -> loadAndShowStats());

        add(refreshBtn, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        loadAndShowStats();
    }

    private void loadAndShowStats() {
        StringBuilder sb = new StringBuilder();
        File qDir = new File(QUESTIONNAIRE_DIR);
        File aDir = new File(ANSWER_DIR);
        if (!qDir.exists() || !aDir.exists()) {
            statArea.setText("未检测到数据目录！");
            return;
        }
        File[] qFiles = qDir.listFiles((d, n) -> n.endsWith(".json"));
        if (qFiles == null || qFiles.length == 0) {
            statArea.setText("未找到问卷数据。");
            return;
        }
        Gson gson = new Gson();

        sb.append("========= 所有问卷的打分题平均分统计 =========\n\n");
        for (File qf : qFiles) {
            try (Reader reader = new FileReader(qf)) {
                QuestionnaireMeta meta = gson.fromJson(reader, QuestionnaireMeta.class);
                sb.append("问卷：").append(meta.title)
                        .append("  (教师:").append(meta.teacherName)
                        .append(", 工号:").append(meta.teacherId).append(")\n");
                File[] ansFiles = aDir.listFiles((d, n) -> n.endsWith("_" + qf.getName()));
                if (meta.questions != null) {
                    boolean hasScore = false;
                    for (int i = 0; i < meta.questions.size(); i++) {
                        QuestionnaireMeta.QuestionData q = meta.questions.get(i);
                        if ("打分".equals(q.type)) {
                            hasScore = true;
                            int sum = 0, cnt = 0;
                            if (ansFiles != null) {
                                for (File af : ansFiles) {
                                    try (Reader ar = new FileReader(af)) {
                                        Map<Integer, Object> ans = gson.fromJson(ar, Map.class);
                                        Object v = ans.get(i);
                                        if (v instanceof Number) {
                                            sum += ((Number) v).intValue();
                                            cnt++;
                                        } else if (v != null) {
                                            try { sum += Integer.parseInt(v.toString()); cnt++; } catch (Exception ignore) {}
                                        }
                                    } catch (Exception ignore) {}
                                }
                            }
                            double avg = cnt > 0 ? (sum * 1.0 / cnt) : 0;
                            sb.append("  打分题【").append(q.title).append("】平均分: ")
                                    .append(cnt > 0 ? String.format("%.2f", avg) : "无数据")
                                    .append(" (答题人数:").append(cnt).append(")\n");
                        }
                    }
                    if (!hasScore) {
                        sb.append("  本问卷无打分题。\n");
                    }
                }
                sb.append("\n");
            } catch (Exception ex) {
                sb.append("  [读取失败: ").append(ex.getMessage()).append("]\n\n");
            }
        }
        statArea.setText(sb.toString());
    }

    // 问卷结构体
    public static class QuestionnaireMeta {
        public String title;
        public String teacherId;
        public String teacherName;
        public List<QuestionData> questions;
        public static class QuestionData {
            public String title;
            public String type;
            public List<String> options;
        }
    }
}