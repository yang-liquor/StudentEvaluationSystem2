package com.studentevaluation.ui;

import com.studentevaluation.entity.EvaluationCriteria;
import com.studentevaluation.service.EvaluationCriteriaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EvaluationCriteriaPanel extends JPanel {
    private EvaluationCriteriaService criteriaService = new EvaluationCriteriaService();
    private JTable table;
    private DefaultTableModel tableModel;

    public EvaluationCriteriaPanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"标准ID", "内容"}, 0) {
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

        addBtn.addActionListener(e -> showCriteriaDialog(null));
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请先选择要更新的标准！");
                return;
            }
            EvaluationCriteria c = getCriteriaFromRow(row);
            showCriteriaDialog(c);
        });
        delBtn.addActionListener(e -> deleteCriteria());
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<EvaluationCriteria> list = criteriaService.getAllCriteria();
            for (EvaluationCriteria c : list) {
                tableModel.addRow(new Object[]{c.getCriteriaId(), c.getContent()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
        }
    }

    private void showCriteriaDialog(EvaluationCriteria c) {
        boolean isUpdate = c != null;
        JTextField idField = new JTextField(isUpdate ? c.getCriteriaId() : "");
        if (isUpdate) idField.setEditable(false);
        JTextField contentField = new JTextField(isUpdate ? c.getContent() : "");

        Object[] msg = {
                "标准ID:", idField,
                "内容:", contentField
        };
        int opt = JOptionPane.showConfirmDialog(this, msg, isUpdate ? "更新标准" : "添加标准", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                EvaluationCriteria newC = new EvaluationCriteria(idField.getText(), contentField.getText());
                if (isUpdate) {
                    criteriaService.updateCriteria(newC);
                } else {
                    criteriaService.addCriteria(newC);
                }
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, (isUpdate ? "更新" : "添加") + "失败：" + ex.getMessage());
            }
        }
    }

    private void deleteCriteria() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的标准！");
            return;
        }
        String id = tableModel.getValueAt(row, 0).toString();
        int opt = JOptionPane.showConfirmDialog(this, "确定要删除该标准吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                criteriaService.deleteCriteria(id);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        }
    }

    private EvaluationCriteria getCriteriaFromRow(int row) {
        String id = tableModel.getValueAt(row, 0).toString();
        String content = tableModel.getValueAt(row, 1).toString();
        return new EvaluationCriteria(id, content);
    }
}