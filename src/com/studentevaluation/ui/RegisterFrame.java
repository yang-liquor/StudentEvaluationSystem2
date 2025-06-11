package com.studentevaluation.ui;

import com.studentevaluation.entity.User;
import com.studentevaluation.service.UserService;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private JTextField userIdField, nameField, roleField, refIdField;
    private JPasswordField passwordField;
    private JButton confirmBtn;
    private UserService userService = new UserService();

    public RegisterFrame(JFrame parent) {
        setTitle("用户注册");
        setSize(420, 420);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Font labelFont = new Font("微软雅黑", Font.PLAIN, 16);
        Font titleFont = new Font("微软雅黑", Font.BOLD, 28);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(24, 40, 24, 40),
                BorderFactory.createLineBorder(new Color(230,230,230), 1, true))
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 8, 12, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // 标题
        JLabel titleLabel = new JLabel("用户注册");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(40,40,40));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        wrapper.add(titleLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        // 用户ID
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel userIdLabel = new JLabel("用户ID:");
        userIdLabel.setFont(labelFont);
        wrapper.add(userIdLabel, gbc);
        gbc.gridx = 1;
        userIdField = new JTextField();
        userIdField.setPreferredSize(new Dimension(180, 32));
        wrapper.add(userIdField, gbc);

        // 姓名
        gbc.gridx = 0; gbc.gridy = 2;
        JLabel nameLabel = new JLabel("姓名:");
        nameLabel.setFont(labelFont);
        wrapper.add(nameLabel, gbc);
        gbc.gridx = 1;
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(180, 32));
        wrapper.add(nameField, gbc);

        // 密码
        gbc.gridx = 0; gbc.gridy = 3;
        JLabel pwdLabel = new JLabel("密码:");
        pwdLabel.setFont(labelFont);
        wrapper.add(pwdLabel, gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(180, 32));
        wrapper.add(passwordField, gbc);

        // 角色
        gbc.gridx = 0; gbc.gridy = 4;
        JLabel roleLabel = new JLabel("角色:");
        roleLabel.setFont(labelFont);
        wrapper.add(roleLabel, gbc);
        gbc.gridx = 1;
        roleField = new JTextField();
        roleField.setPreferredSize(new Dimension(180, 32));
        wrapper.add(roleField, gbc);

        // 关联ID
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel refLabel = new JLabel("关联ID:");
        refLabel.setFont(labelFont);
        wrapper.add(refLabel, gbc);
        gbc.gridx = 1;
        refIdField = new JTextField();
        refIdField.setPreferredSize(new Dimension(180, 32));
        wrapper.add(refIdField, gbc);

        // 注册按钮
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        confirmBtn = new JButton("注册");
        confirmBtn.setFont(new Font("微软雅黑", Font.BOLD, 18));
        confirmBtn.setPreferredSize(new Dimension(160, 40));
        confirmBtn.setBackground(new Color(66, 133, 244));
        confirmBtn.setForeground(Color.WHITE);
        confirmBtn.setFocusPainted(false);
        confirmBtn.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 1, true));
        wrapper.add(confirmBtn, gbc);

        add(wrapper);

        confirmBtn.addActionListener(e -> register());
    }

    private void register() {
        String userId = userIdField.getText().trim();
        String name = nameField.getText().trim();
        String pwd = new String(passwordField.getPassword());
        String role = roleField.getText().trim();
        String refId = refIdField.getText().trim();

        if (userId.isEmpty() || name.isEmpty() || pwd.isEmpty() || role.isEmpty() || refId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整信息！");
            return;
        }

        try {
            User u = new User(userId, pwd, role, refId); // 你可扩展User类增加name属性
            // u.setName(name);
            userService.addUser(u);
            JOptionPane.showMessageDialog(this, "注册成功！");
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "注册失败：" + ex.getMessage());
        }
    }
}