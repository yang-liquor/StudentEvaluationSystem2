package com.studentevaluation.ui;

import com.studentevaluation.entity.Course;
import com.studentevaluation.service.CourseService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class CoursePanel extends JPanel {
    private CourseService courseService = new CourseService();
    private JTable table;
    private DefaultTableModel tableModel;

    public CoursePanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"课程ID", "课程名称", "教师ID"}, 0) {
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

        addBtn.addActionListener(e -> showCourseDialog(null));
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请先选择要更新的课程！");
                return;
            }
            Course c = getCourseFromRow(row);
            showCourseDialog(c);
        });
        delBtn.addActionListener(e -> deleteCourse());
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<Course> list = courseService.getAllCourses();
            for (Course c : list) {
                tableModel.addRow(new Object[]{c.getCourseId(), c.getName(), c.getTeacherId()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
        }
    }

    private void showCourseDialog(Course c) {
        boolean isUpdate = c != null;
        JTextField idField = new JTextField(isUpdate ? c.getCourseId() : "");
        if (isUpdate) idField.setEditable(false);
        JTextField nameField = new JTextField(isUpdate ? c.getName() : "");
        JTextField teacherField = new JTextField(isUpdate ? c.getTeacherId() : "");

        Object[] msg = {
                "课程ID:", idField,
                "课程名称:", nameField,
                "教师ID:", teacherField
        };
        int opt = JOptionPane.showConfirmDialog(this, msg, isUpdate ? "更新课程" : "添加课程", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                Course newC = new Course(idField.getText(), nameField.getText(), teacherField.getText());
                if (isUpdate) {
                    courseService.updateCourse(newC);
                } else {
                    courseService.addCourse(newC);
                }
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, (isUpdate ? "更新" : "添加") + "失败：" + ex.getMessage());
            }
        }
    }

    private void deleteCourse() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的课程！");
            return;
        }
        String id = tableModel.getValueAt(row, 0).toString();
        int opt = JOptionPane.showConfirmDialog(this, "确定要删除该课程吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                courseService.deleteCourse(id);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        }
    }

    private Course getCourseFromRow(int row) {
        String id = tableModel.getValueAt(row, 0).toString();
        String name = tableModel.getValueAt(row, 1).toString();
        String teacherId = tableModel.getValueAt(row, 2).toString();
        return new Course(id, name, teacherId);
    }
}