package com.studentevaluation.ui;

import com.studentevaluation.entity.Evaluation;
import com.studentevaluation.service.EvaluationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class EvaluationPanel extends JPanel {
    private EvaluationService evaluationService = new EvaluationService();
    private JTable table;
    private DefaultTableModel tableModel;

    public EvaluationPanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(
                new String[]{"评教ID","学生ID","课程ID","分数","标准ID","评语","时间"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        refreshTable();
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("添加");
        JButton updateBtn = new JButton("更新");
        JButton delBtn = new JButton("删除");
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(delBtn);
        add(btnPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showEvaluationDialog(null));
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请先选择要更新的评教记录！");
                return;
            }
            Evaluation eva = getEvaluationFromRow(row);
            showEvaluationDialog(eva);
        });
        delBtn.addActionListener(e -> deleteEvaluation());
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Evaluation> list = evaluationService.getAllEvaluations();
            for (Evaluation eva : list) {
                tableModel.addRow(new Object[]{
                        eva.getEvaluationId(), eva.getStudentId(), eva.getCourseId(),
                        eva.getScore(), eva.getCriteriaId(), eva.getComment(), eva.getEvalTime()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
        }
    }

    private void showEvaluationDialog(Evaluation eva) {
        boolean isUpdate = eva != null;
        JTextField studentField = new JTextField(isUpdate ? eva.getStudentId() : "");
        JTextField courseField = new JTextField(isUpdate ? eva.getCourseId() : "");
        JTextField scoreField = new JTextField(isUpdate ? String.valueOf(eva.getScore()) : "");
        JTextField criteriaField = new JTextField(isUpdate ? eva.getCriteriaId() : "");
        JTextField commentField = new JTextField(isUpdate ? eva.getComment() : "");
        JTextField timeField = new JTextField(isUpdate && eva.getEvalTime() != null ? eva.getEvalTime().toString().substring(0, 19) : "");

        Object[] msg = {
                "学生ID:", studentField,
                "课程ID:", courseField,
                "分数:", scoreField,
                "标准ID:", criteriaField,
                "评语:", commentField,
                "时间(yyyy-MM-dd HH:mm:ss):", timeField
        };
        int opt = JOptionPane.showConfirmDialog(this, msg, isUpdate ? "更新评教" : "添加评教", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                Evaluation newEva = new Evaluation(
                        isUpdate ? eva.getEvaluationId() : 0,
                        studentField.getText(), courseField.getText(),
                        Double.parseDouble(scoreField.getText()),
                        criteriaField.getText(), commentField.getText(),
                        Timestamp.valueOf(timeField.getText())
                );
                if (isUpdate) {
                    evaluationService.updateEvaluation(newEva);
                } else {
                    evaluationService.addEvaluation(newEva);
                }
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, (isUpdate ? "更新" : "添加") + "失败：" + ex.getMessage());
            }
        }
    }

    private void deleteEvaluation() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的评教记录！");
            return;
        }
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        int opt = JOptionPane.showConfirmDialog(this, "确定要删除该评教记录吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                evaluationService.deleteEvaluation(id);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        }
    }

    private Evaluation getEvaluationFromRow(int row) {
        int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        String studentId = tableModel.getValueAt(row, 1).toString();
        String courseId = tableModel.getValueAt(row, 2).toString();
        double score = Double.parseDouble(tableModel.getValueAt(row, 3).toString());
        String criteriaId = tableModel.getValueAt(row, 4).toString();
        String comment = tableModel.getValueAt(row, 5).toString();
        String timeStr = tableModel.getValueAt(row, 6).toString();
        Timestamp evalTime = Timestamp.valueOf(timeStr.length() > 19 ? timeStr.substring(0, 19) : timeStr);
        return new Evaluation(id, studentId, courseId, score, criteriaId, comment, evalTime);
    }
}