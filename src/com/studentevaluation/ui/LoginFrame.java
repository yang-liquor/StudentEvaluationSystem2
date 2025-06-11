package com.studentevaluation.ui;

import com.studentevaluation.entity.User;
import com.studentevaluation.service.UserService;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField userIdField;
    private JPasswordField passwordField;
    private JButton loginBtn, registerBtn;
    private UserService userService = new UserService();

    public LoginFrame() {
        setTitle("登录 - 学生评教系统");
        setSize(430, 340);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 背景渐变
        JPanel bg = new JPanel(){
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D)g;
                GradientPaint gp = new GradientPaint(0,0,new Color(244, 186, 221),0,getHeight(),new Color(248, 219, 237));
                g2.setPaint(gp);
                g2.fillRect(0,0,getWidth(),getHeight());
            }
        };
        bg.setLayout(new GridBagLayout());
        setContentPane(bg);

        // 卡片面板
        JPanel card = new JPanel(new GridBagLayout());
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(440, 320));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(236, 210, 227, 255), 1, true),
                BorderFactory.createEmptyBorder(25, 30, 25, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 8, 10, 8);

        // 标题
        JLabel titleLabel = new JLabel("学生评教管理系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 25));
        titleLabel.setForeground(new Color(50,50,50));
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=2; gbc.anchor=GridBagConstraints.CENTER;
        card.add(titleLabel, gbc);

        gbc.gridwidth=1;

        // 用户ID
        gbc.gridy=1; gbc.gridx=0; gbc.anchor=GridBagConstraints.EAST; gbc.fill=GridBagConstraints.NONE; gbc.weightx=0;
        JLabel userLabel = new JLabel("用户ID:");
        userLabel.setPreferredSize(new Dimension(65, 36));
        card.add(userLabel, gbc);

        gbc.gridx=1; gbc.anchor=GridBagConstraints.WEST; gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=1.0;
        userIdField = new JTextField();
        card.add(userIdField, gbc);

        // 密码
        gbc.gridy=2; gbc.gridx=0; gbc.anchor=GridBagConstraints.EAST; gbc.fill=GridBagConstraints.NONE; gbc.weightx=0;
        JLabel passLabel = new JLabel("密  码:");
        passLabel.setPreferredSize(new Dimension(65, 36));
        card.add(passLabel, gbc);

        gbc.gridx=1; gbc.anchor=GridBagConstraints.WEST; gbc.fill=GridBagConstraints.HORIZONTAL; gbc.weightx=1.0;
        passwordField = new JPasswordField();
        card.add(passwordField, gbc);

        // 按钮
        gbc.gridy=3; gbc.gridx=0; gbc.gridwidth=2; gbc.anchor=GridBagConstraints.CENTER;
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 32, 0));
        gbc.insets = new Insets(20, 8, 20, 8);

        btnPanel.setOpaque(false);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        loginBtn = createColorfulButton("登录");
        registerBtn = createColorfulButton("注册");
        loginBtn.setFont(new Font("微软雅黑", Font.BOLD, 18));
        registerBtn.setFont(new Font("微软雅黑", Font.BOLD, 18));
        btnPanel.add(loginBtn);
        btnPanel.add(registerBtn);
        card.add(btnPanel, gbc);

        bg.add(card);

        // 登录逻辑
        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> new RegisterFrame(LoginFrame.this).setVisible(true));
        passwordField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER) loginBtn.doClick();
            }
        });
        userIdField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER) passwordField.requestFocus();
            }
        });
    }

    // Google蓝色圆角按钮
    private JButton createColorfulButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(118, 147, 195));
        button.setForeground(Color.white);
        button.setFocusPainted(false);
        button.setFont(new Font("微软雅黑", Font.BOLD, 18));
        button.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        button.setBorder(new RoundedBorder(18));
        return button;
    }

    // 圆角边框类
    static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private int radius;
        public RoundedBorder(int radius) {
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(new Color(52, 119, 234));
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }

    private void login() {
        String userId = userIdField.getText().trim();
        String pwd = new String(passwordField.getPassword());
        if (userId.isEmpty() || pwd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户ID和密码！");
            return;
        }
        try {
            java.util.List<User> users = userService.getAllUsers();
            for (User u : users) {
                if (u.getUserId().equals(userId) && u.getPassword().equals(pwd)) {
                    String role = u.getRole();
                    String name = u.getUserId();
                    if ("学生".equals(role) || "student".equalsIgnoreCase(role)) {
                        name = com.studentevaluation.util.NameUtil.getStudentNameById(u.getRefId());
                    } else if ("教师".equals(role) || "teacher".equalsIgnoreCase(role)) {
                        name = com.studentevaluation.util.NameUtil.getTeacherNameById(u.getRefId());
                    }
                    JOptionPane.showMessageDialog(this, "登录成功，欢迎" + role + name + "进入评教系统！");
                    MainFrame mainFrame = new MainFrame(u);
                    mainFrame.setVisible(true);
                    this.dispose();
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "用户ID或密码错误！");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "登录异常：" + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        // FlatLaf 美化主题（推荐）
        try { FlatLightLaf.setup(); } catch (Exception ignored) {}

        // 打开一个登录窗口
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}