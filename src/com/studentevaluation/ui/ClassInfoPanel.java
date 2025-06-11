package com.studentevaluation.ui;

import com.studentevaluation.entity.ClassInfo;
import com.studentevaluation.service.ClassInfoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class ClassInfoPanel extends JPanel {
    private ClassInfoService classInfoService = new ClassInfoService();
    private JTable table;
    private DefaultTableModel tableModel;

    public ClassInfoPanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"班级ID", "班级名称"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        table.setShowGrid(true);                                      // 显示所有网格线
        table.setGridColor(Color.BLACK);                              // 设置网格线颜色为黑色
        table.setIntercellSpacing(new Dimension(1, 1));               // 线条宽度（可调，默认1像素）
        JTableHeader header = table.getTableHeader();
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
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

        addBtn.addActionListener(e -> showClassInfoDialog(null));
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请先选择要更新的班级！");
                return;
            }
            ClassInfo c = getClassInfoFromRow(row);
            showClassInfoDialog(c);
        });
        delBtn.addActionListener(e -> deleteClassInfo());
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<ClassInfo> list = classInfoService.getAllClassInfo();
            for (ClassInfo c : list) {
                tableModel.addRow(new Object[]{c.getClassId(), c.getClassName()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
        }
    }

    private void showClassInfoDialog(ClassInfo c) {
        boolean isUpdate = c != null;
        JTextField idField = new JTextField(isUpdate ? c.getClassId() : "");
        if (isUpdate) idField.setEditable(false);
        JTextField nameField = new JTextField(isUpdate ? c.getClassName() : "");

        Object[] msg = {
                "班级ID:", idField,
                "班级名称:", nameField
        };
        int opt = JOptionPane.showConfirmDialog(this, msg, isUpdate ? "更新班级" : "添加班级", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                ClassInfo newC = new ClassInfo(idField.getText(), nameField.getText());
                if (isUpdate) {
                    classInfoService.updateClassInfo(newC);
                } else {
                    classInfoService.addClassInfo(newC);
                }
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, (isUpdate ? "更新" : "添加") + "失败：" + ex.getMessage());
            }
        }
    }

    private void deleteClassInfo() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的班级！");
            return;
        }
        String id = tableModel.getValueAt(row, 0).toString();
        int opt = JOptionPane.showConfirmDialog(this, "确定要删除该班级吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                classInfoService.deleteClassInfo(id);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        }
    }

    private ClassInfo getClassInfoFromRow(int row) {
        String id = tableModel.getValueAt(row, 0).toString();
        String name = tableModel.getValueAt(row, 1).toString();
        return new ClassInfo(id, name);
    }
}