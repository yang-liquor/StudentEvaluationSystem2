package com.studentevaluation.ui;

import com.studentevaluation.entity.Student;
import com.studentevaluation.service.StudentService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class StudentPanel extends JPanel {
    private StudentService studentService = new StudentService();
    private JTable table;
    private DefaultTableModel tableModel;

    public StudentPanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"学号", "姓名", "班级ID"}, 0) {
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

        addBtn.addActionListener(e -> showStudentDialog(null));
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请先选择要更新的学生！");
                return;
            }
            Student s = getStudentFromRow(row);
            showStudentDialog(s);
        });
        delBtn.addActionListener(e -> deleteStudent());
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Student> list = studentService.getAllStudents();
            for (Student s : list) {
                tableModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getClassId()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
        }
    }

    private void showStudentDialog(Student s) {
        boolean isUpdate = s != null;
        JTextField idField = new JTextField(isUpdate ? s.getStudentId() : "");
        if (isUpdate) idField.setEditable(false);
        JTextField nameField = new JTextField(isUpdate ? s.getName() : "");
        JTextField classIdField = new JTextField(isUpdate ? s.getClassId() : "");

        Object[] msg = {
                "学号:", idField,
                "姓名:", nameField,
                "班级ID:", classIdField
        };
        int opt = JOptionPane.showConfirmDialog(this, msg, isUpdate ? "更新学生" : "添加学生", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                Student newS = new Student(idField.getText(), nameField.getText(), classIdField.getText());
                if (isUpdate) {
                    studentService.updateStudent(newS);
                } else {
                    studentService.addStudent(newS);
                }
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, (isUpdate ? "更新" : "添加") + "失败：" + ex.getMessage());
            }
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的学生！");
            return;
        }
        String id = tableModel.getValueAt(row, 0).toString();
        int opt = JOptionPane.showConfirmDialog(this, "确定要删除该学生吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                studentService.deleteStudent(id);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        }
    }

    private Student getStudentFromRow(int row) {
        String id = tableModel.getValueAt(row, 0).toString();
        String name = tableModel.getValueAt(row, 1).toString();
        String classId = tableModel.getValueAt(row, 2).toString();
        return new Student(id, name, classId);
    }
}