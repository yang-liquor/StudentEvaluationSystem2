package com.studentevaluation.ui;

import com.studentevaluation.entity.User;
import com.studentevaluation.service.UserService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class UserPanel extends JPanel {
    private UserService userService = new UserService();
    private JTable table;
    private DefaultTableModel tableModel;

    public UserPanel() {
        setLayout(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"用户ID", "密码", "角色", "关联ID"}, 0) {
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

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(178, 198, 216), 2, true)); // 加浅灰色圆角边框
        add(scrollPane, BorderLayout.CENTER);


        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("添加");
        JButton updateBtn = new JButton("更新");
        JButton delBtn = new JButton("删除");
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(delBtn);
        add(btnPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> showUserDialog(null));
        updateBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请先选择要更新的用户！");
                return;
            }
            User u = getUserFromRow(row);
            showUserDialog(u);
        });
        delBtn.addActionListener(e -> deleteUser());
    }

    private void refreshTable() {
        try {
            tableModel.setRowCount(0);
            List<User> list = userService.getAllUsers();
            for (User u : list) {
                tableModel.addRow(new Object[]{u.getUserId(), u.getPassword(), u.getRole(), u.getRefId()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载失败：" + ex.getMessage());
        }
    }

    private void showUserDialog(User u) {
        boolean isUpdate = u != null;
        JTextField idField = new JTextField(isUpdate ? u.getUserId() : "");
        if (isUpdate) idField.setEditable(false);
        JTextField passField = new JTextField(isUpdate ? u.getPassword() : "");
        JTextField roleField = new JTextField(isUpdate ? u.getRole() : "");
        JTextField refIdField = new JTextField(isUpdate ? u.getRefId() : "");

        Object[] msg = {
                "用户ID:", idField,
                "密码:", passField,
                "角色:", roleField,
                "关联ID:", refIdField
        };
        int opt = JOptionPane.showConfirmDialog(this, msg, isUpdate ? "更新用户" : "添加用户", JOptionPane.OK_CANCEL_OPTION);
        if (opt == JOptionPane.OK_OPTION) {
            try {
                User newU = new User(idField.getText(), passField.getText(), roleField.getText(), refIdField.getText());
                if (isUpdate) {
                    userService.updateUser(newU);
                } else {
                    userService.addUser(newU);
                }
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, (isUpdate ? "更新" : "添加") + "失败：" + ex.getMessage());
            }
        }
    }

    private void deleteUser() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的用户！");
            return;
        }
        String id = tableModel.getValueAt(row, 0).toString();
        int opt = JOptionPane.showConfirmDialog(this, "确定要删除该用户吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION) {
            try {
                userService.deleteUser(id);
                refreshTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "删除失败：" + ex.getMessage());
            }
        }
    }

    private User getUserFromRow(int row) {
        String id = tableModel.getValueAt(row, 0).toString();
        String pwd = tableModel.getValueAt(row, 1).toString();
        String role = tableModel.getValueAt(row, 2).toString();
        String refId = tableModel.getValueAt(row, 3).toString();
        return new User(id, pwd, role, refId);
    }
}