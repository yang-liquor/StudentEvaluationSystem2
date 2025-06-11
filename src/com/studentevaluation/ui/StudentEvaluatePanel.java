package com.studentevaluation.ui;

import com.studentevaluation.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.List;

public class StudentEvaluatePanel extends JPanel {
    private static final String DIR = "questionnaires";
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> questionnaireList = new JList<>(listModel);
    private JButton startBtn = new JButton("开始答题");
    private User user;

    public StudentEvaluatePanel() {
        initUI();
    }

    // 新增带User参数的构造
    public StudentEvaluatePanel(User user) {
        this.user = user;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        JLabel title = new JLabel("请选择要填写的评教问卷", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        refreshQuestionnaireList();
        questionnaireList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(questionnaireList), BorderLayout.CENTER);

        startBtn.addActionListener(e -> openSelectedQuestionnaire());
        JPanel btnPanel = new JPanel();
        btnPanel.add(startBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void refreshQuestionnaireList() {
        listModel.clear();
        File folder = new File(DIR);
        if (!folder.exists()) folder.mkdir();
        File[] files = folder.listFiles((d, n) -> n.endsWith(".json"));
        if (files != null) {
            for (File f : files) listModel.addElement(f.getName());
        }
    }

    private void openSelectedQuestionnaire() {
        String file = questionnaireList.getSelectedValue();
        if (file == null) {
            JOptionPane.showMessageDialog(this, "请选择一个问卷！");
            return;
        }
        Questionnaire questionnaire = loadQuestionnaireFromFile(new File(DIR, file).getPath());
        if (questionnaire == null || questionnaire.questions == null) {
            JOptionPane.showMessageDialog(this, "问卷加载失败！");
            return;
        }
        // 传递user给答题窗口（如需用到，可在StudentAnswerDialog加构造参数）
        new StudentAnswerDialog(questionnaire, file).setVisible(true);
    }

    private Questionnaire loadQuestionnaireFromFile(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            if (fileContainsQuestionnaireObject(filePath)) {
                return gson.fromJson(reader, Questionnaire.class);
            } else {
                List<QuestionData> questions = gson.fromJson(reader, new TypeToken<List<QuestionData>>(){}.getType());
                Questionnaire q = new Questionnaire();
                q.questions = questions;
                q.title = filePath;
                return q;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private boolean fileContainsQuestionnaireObject(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String firstLine = br.readLine();
            return firstLine != null && firstLine.trim().startsWith("{");
        } catch (Exception ex) {
            return false;
        }
    }

    public static class Questionnaire {
        public String title;
        public List<QuestionData> questions;
    }

    public static class QuestionData {
        public String title;
        public String type;
        public List<String> options;
    }
}