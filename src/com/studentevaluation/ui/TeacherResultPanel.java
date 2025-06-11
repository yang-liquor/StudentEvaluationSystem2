package com.studentevaluation.ui;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import com.studentevaluation.entity.Teacher;

/**
 * 教师评教结果界面：可查看自己课程的问卷及学生评价汇总
 */
public class TeacherResultPanel extends JPanel {
    private static final String QUESTIONNAIRE_DIR = "questionnaires";
    private static final String ANSWER_DIR = "answers";

    private Teacher teacher;
    private DefaultListModel<QuestionnaireMeta> questionnaireListModel = new DefaultListModel<>();
    private JList<QuestionnaireMeta> questionnaireJList = new JList<>(questionnaireListModel);
    private JTextArea resultArea = new JTextArea();

    public TeacherResultPanel(Teacher teacher) {
        this.teacher = teacher;
        setLayout(new BorderLayout());
        JLabel title = new JLabel("我的课程评教结果", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("我的问卷/课程：", SwingConstants.CENTER), BorderLayout.NORTH);
        questionnaireJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        leftPanel.add(new JScrollPane(questionnaireJList), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("刷新列表");
        leftPanel.add(refreshBtn, BorderLayout.SOUTH);

        add(leftPanel, BorderLayout.WEST);

        resultArea.setEditable(false);
        resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> refreshQuestionnaireList());
        questionnaireJList.addListSelectionListener(e -> showResultForSelected());

        refreshQuestionnaireList();
    }

    /** 刷新属于自己的问卷/课程列表 */
    private void refreshQuestionnaireList() {
        questionnaireListModel.clear();
        File dir = new File(QUESTIONNAIRE_DIR);
        if (!dir.exists()) return;
        File[] files = dir.listFiles((d, n) -> n.endsWith(".json"));
        if (files == null) return;
        for (File f : files) {
            QuestionnaireMeta meta = readQuestionnaireMeta(f);
            if (meta != null && isMyQuestionnaire(meta)) {
                questionnaireListModel.addElement(meta);
            }
        }
    }

    /** 判断问卷是否属于当前教师（工号或姓名任一相等即可） */
    private boolean isMyQuestionnaire(QuestionnaireMeta meta) {
        if (teacher == null) return false;
        return (meta.teacherId != null && meta.teacherId.equals(teacher.getTeacherId()))
                || (meta.teacherName != null && meta.teacherName.equals(teacher.getName()));
    }

    /** 读取问卷元信息（包含title、teacherId、teacherName、questions等） */
    private QuestionnaireMeta readQuestionnaireMeta(File file) {
        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            QuestionnaireMeta meta = gson.fromJson(reader, QuestionnaireMeta.class);
            meta.fileName = file.getName();
            return meta;
        } catch (Exception e) {
            return null;
        }
    }

    /** 显示所选问卷的学生评价结果 */
    private void showResultForSelected() {
        QuestionnaireMeta meta = questionnaireJList.getSelectedValue();
        if (meta == null) {
            resultArea.setText("");
            return;
        }
        List<Map<String, Object>> answerList = loadAnswersForQuestionnaire(meta.fileName);
        StringBuilder sb = new StringBuilder();
        sb.append("问卷标题：").append(meta.title != null ? meta.title : "").append("\n");
        sb.append("教师工号：").append(meta.teacherId != null ? meta.teacherId : "").append("\n");
        sb.append("教师姓名：").append(meta.teacherName != null ? meta.teacherName : "").append("\n\n");
        sb.append("答题人数：").append(answerList.size()).append(" 人\n\n");
        if (meta.questions == null || meta.questions.isEmpty()) {
            sb.append("本问卷无题目信息。\n");
            resultArea.setText(sb.toString());
            return;
        }

        for (int i = 0; i < meta.questions.size(); i++) {
            QuestionnaireMeta.QuestionData q = meta.questions.get(i);
            sb.append("Q").append(i + 1).append("：").append(q.title).append("\n");
            if ("单选".equals(q.type)) {
                Map<String, Integer> optCount = new LinkedHashMap<>();
                if (q.options != null) {
                    for (String opt : q.options) optCount.put(opt, 0);
                }
                for (Map<String, Object> ans : answerList) {
                    Object val = ans.get(String.valueOf(i));
                    if (val != null && optCount.containsKey(val.toString())) {
                        optCount.put(val.toString(), optCount.get(val.toString()) + 1);
                    }
                }
                if (q.options != null) {
                    for (String opt : q.options) {
                        sb.append("    ").append(opt).append("：").append(optCount.get(opt)).append("票\n");
                    }
                }
            } else if ("打分".equals(q.type)) {
                int sum = 0, cnt = 0;
                for (Map<String, Object> ans : answerList) {
                    Object v = ans.get(String.valueOf(i));
                    if (v instanceof Number) {
                        sum += ((Number) v).intValue();
                        cnt++;
                    } else if (v != null) {
                        try { sum += Integer.parseInt(v.toString()); cnt++; } catch (Exception ignore) {}
                    }
                }
                double avg = cnt > 0 ? (sum * 1.0 / cnt) : 0;
                sb.append("    平均分：").append(String.format("%.2f", avg)).append(" (满分5)\n");
            } else if ("简答".equals(q.type)) {
                sb.append("    简答内容如下：\n");
                int idx = 1;
                for (Map<String, Object> ans : answerList) {
                    Object v = ans.get(String.valueOf(i));
                    if (v != null && !v.toString().isBlank()) {
                        sb.append("      ").append(idx++).append(". ").append(v.toString()).append("\n");
                    }
                }
                if (idx == 1) sb.append("      无简答。\n");
            }
            sb.append("\n");
        }
        resultArea.setText(sb.toString());
    }

    /** 加载该问卷所有答卷 */
    private List<Map<String, Object>> loadAnswersForQuestionnaire(String questionnaireFileName) {
        List<Map<String, Object>> list = new ArrayList<>();
        File dir = new File(ANSWER_DIR);
        if (!dir.exists()) return list;
        File[] files = dir.listFiles((d, n) -> n.endsWith("_" + questionnaireFileName));
        if (files == null) return list;
        Gson gson = new Gson();
        for (File f : files) {
            try (Reader reader = new FileReader(f)) {
                Map<String, Object> ans = gson.fromJson(reader, Map.class);
                if (ans != null) list.add(ans);
            } catch (Exception ignore) {}
        }
        return list;
    }

    // 问卷结构体
    public static class QuestionnaireMeta {
        public String title;
        public String teacherId;
        public String teacherName;
        public List<QuestionData> questions;
        // 非json字段
        public String fileName;

        @Override
        public String toString() {
            if (title != null && !title.isEmpty()) return title;
            if (fileName != null) return fileName;
            return super.toString();
        }

        public static class QuestionData {
            public String title;
            public String type;
            public List<String> options;
        }
    }
}