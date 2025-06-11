package com.studentevaluation.ui;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生答题弹窗窗口
 */
public class StudentAnswerDialog extends JDialog {
    private final Map<Integer, Object> answers = new HashMap<>();
    private final StudentEvaluatePanel.Questionnaire questionnaire;
    private final String fileName;

    public StudentAnswerDialog(StudentEvaluatePanel.Questionnaire questionnaire, String fileName) {
        super((Frame) null, "答题：" + questionnaire.title, true);
        this.questionnaire = questionnaire;
        this.fileName = fileName;
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        List<StudentEvaluatePanel.QuestionData> questions = questionnaire.questions;
        for (int i = 0; i < questions.size(); i++) {
            StudentEvaluatePanel.QuestionData data = questions.get(i);
            JPanel qPanel = new JPanel(new BorderLayout());
            qPanel.setBorder(BorderFactory.createTitledBorder("Q" + (i + 1) + " " + data.title));
            if ("单选".equals(data.type)) {
                ButtonGroup bg = new ButtonGroup();
                JPanel opts = new JPanel();
                for (String opt : data.options) {
                    JRadioButton rb = new JRadioButton(opt);
                    bg.add(rb);
                    opts.add(rb);
                    int idx = i;
                    rb.addActionListener(e -> answers.put(idx, opt));
                }
                qPanel.add(opts, BorderLayout.CENTER);
            } else if ("打分".equals(data.type)) {
                JSlider slider = new JSlider(1, 5, 3);
                slider.setMajorTickSpacing(1);
                slider.setPaintTicks(true);
                slider.setPaintLabels(true);
                qPanel.add(slider, BorderLayout.CENTER);
                int idx = i;
                slider.addChangeListener(e -> answers.put(idx, slider.getValue()));
            } else if ("简答".equals(data.type)) {
                JTextArea area = new JTextArea(3, 40);
                qPanel.add(new JScrollPane(area), BorderLayout.CENTER);
                int idx = i;
                // 失去焦点时记录答案
                area.addFocusListener(new FocusAdapter() {
                    public void focusLost(FocusEvent e) {
                        answers.put(idx, area.getText().trim());
                    }
                });
            }
            mainPanel.add(qPanel);
        }

        JButton submitBtn = new JButton("提交");
        submitBtn.addActionListener(e -> submitAnswers());
        JPanel btnPanel = new JPanel();
        btnPanel.add(submitBtn);

        JScrollPane scroll = new JScrollPane(mainPanel);
        getContentPane().add(scroll, BorderLayout.CENTER);
        getContentPane().add(btnPanel, BorderLayout.SOUTH);
    }

    private void submitAnswers() {
        // 检查必答题
        List<StudentEvaluatePanel.QuestionData> qs = questionnaire.questions;
        for (int i = 0; i < qs.size(); i++) {
            StudentEvaluatePanel.QuestionData q = qs.get(i);
            if (!answers.containsKey(i) && !"简答".equals(q.type)) {
                JOptionPane.showMessageDialog(this, "第" + (i + 1) + "题未作答！");
                return;
            }
        }
        // 输入学号
        String studentId = JOptionPane.showInputDialog(this, "请输入学号：");
        if (studentId == null || studentId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "学号不能为空！");
            return;
        }
        // 保存答案到answers目录
        File answersDir = new File("answers");
        if (!answersDir.exists()) answersDir.mkdir();
        File ansFile = new File(answersDir, studentId + "_" + fileName);
        try (Writer writer = new FileWriter(ansFile)) {
            new Gson().toJson(answers, writer);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "答案保存失败：" + ex.getMessage());
            return;
        }
        JOptionPane.showMessageDialog(this, "提交成功！");
        dispose();
    }
}