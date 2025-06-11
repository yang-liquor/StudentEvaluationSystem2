package com.studentevaluation.ui;

import com.studentevaluation.entity.Teacher;
import com.studentevaluation.service.TeacherService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class TeacherPanel extends JPanel {
    private TeacherService teacherService = new TeacherService();
    private JTable table;
    private DefaultTableModel tableModel;

    public TeacherPanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"教师ID", "姓名", "部门", "职称"}, 0) {
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

        addBtn.addActionListener(e -> showTeacherDialog(null));
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请先选择要更新的教师！");
                return;
            }
            Teacher t = getTeacherFromRow(row);
            showTeacherDialog(t);
        });
        delBtn.addActionListener(e -> deleteTeacher());
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Teacher> list = teacherService.getAllTeachers();
            for (Teacher t : list) {
                tableModel.addRow(new Object[]{t.getTeacherId(), t.getName(), t.getDepartment(), t.getTitle()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
        }
    }

    private void showTeacherDialog(Teacher t) {
        boolean isUpdate = t != null;
        JTextField idField = new JTextField(isUpdate ? t.getTeacherId() : "");
        if (isUpdate) idField.setEditable(false);
        JTextField nameField = new JTextField(isUpdate ? t.getName() : "");
        JTextField depField = new JTextField(isUpdate ? t.getDepartment() : "");
        JTextField titleField = new JTextField(isUpdate ? t.getTitle() : "");

        Object[] msg = {
                "教师ID:", idField,
                "姓名:", nameField,
                "部门:", depField,
                "职称:", titleField
        };
        int opt = JOptionPane.showConfirmDialog(this, msg, isUpdate ? "更新教师" : "添加教师", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                Teacher newT = new Teacher(idField.getText(), nameField.getText(), depField.getText(), titleField.getText());
                if (isUpdate) {
                    teacherService.updateTeacher(newT);
                } else {
                    teacherService.addTeacher(newT);
                }
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, (isUpdate ? "更新" : "添加") + "失败：" + ex.getMessage());
            }
        }
    }

    private void deleteTeacher() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的教师！");
            return;
        }
        String id = tableModel.getValueAt(row, 0).toString();
        int opt = JOptionPane.showConfirmDialog(this, "确定要删除该教师吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                teacherService.deleteTeacher(id);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        }
    }

    private Teacher getTeacherFromRow(int row) {
        String id = tableModel.getValueAt(row, 0).toString();
        String name = tableModel.getValueAt(row, 1).toString();
        String dep = tableModel.getValueAt(row, 2).toString();
        String title = tableModel.getValueAt(row, 3).toString();
        return new Teacher(id, name, dep, title);
    }
}