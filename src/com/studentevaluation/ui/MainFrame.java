package com.studentevaluation.ui;

import com.studentevaluation.entity.User;
import com.studentevaluation.entity.Teacher;
import com.studentevaluation.service.TeacherService;
import com.studentevaluation.util.NameUtil;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private User currentUser;

    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("学生评教管理系统");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        String role = user.getRole();
        String showName;
        if ("学生".equals(role) || "student".equalsIgnoreCase(role)) {
            showName = NameUtil.getStudentNameById(user.getRefId());
        } else if ("教师".equals(role) || "teacher".equalsIgnoreCase(role)) {
            showName = NameUtil.getTeacherNameById(user.getRefId());
        } else {
            showName = "管理员";
        }

        // 顶部面板：淡蓝背景
        JPanel topPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 渐变蓝色背景
                Graphics2D g2d = (Graphics2D) g;
                Color start = new Color(245, 248, 255);
                Color end = new Color(220, 230, 250);
                GradientPaint gp = new GradientPaint(0, 0, start, 0, getHeight(), end);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        JLabel welcomeLabel = new JLabel("欢迎 " + role + " " + showName + " 使用评教系统", JLabel.LEFT);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        JButton logoutBtn = createColorfulButton("退出登录");
        logoutBtn.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        topPanel.setOpaque(false);
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        topPanel.add(logoutBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // 主Tab面板
        JTabbedPane tabPane = new JTabbedPane();
        tabPane.setFont(new Font("微软雅黑", Font.PLAIN, 15));
        tabPane.setBackground(new Color(146, 174, 220)); // 背景淡蓝

        // 学生功能
        if ("学生".equals(role) || "student".equalsIgnoreCase(role)) {
            tabPane.addTab("评教打分", new StudentEvaluatePanel(user));
        }
        // 教师功能
        else if ("教师".equals(role) || "teacher".equalsIgnoreCase(role)) {
            TeacherService teacherService = new TeacherService();
            Teacher teacher = null;
            try {
                teacher = teacherService.findById(user.getRefId()); // refId即teacherId
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "教师信息读取失败！");
                return;
            }
            if (teacher == null) {
                JOptionPane.showMessageDialog(this, "未找到教师信息！");
                return;
            }
            tabPane.addTab("评教结果", new TeacherResultPanel(teacher));
            tabPane.addTab("教学改进", new TeacherImprovePanel(user));
        }
        // 管理员功能
        else {
            AdminImportExportPanel importExportPanel = new AdminImportExportPanel();
            AdminEvaluateSettingPanel evalPanel = new AdminEvaluateSettingPanel();

            tabPane.addTab("评教设置", evalPanel);
            tabPane.addTab("导入导出", importExportPanel);
            tabPane.addTab("用户管理", new UserPanel());
            tabPane.addTab("学生管理", new StudentPanel());
            tabPane.addTab("教师管理", new TeacherPanel());
            tabPane.addTab("课程管理", new CoursePanel());
            tabPane.addTab("班级管理", new ClassInfoPanel());
            tabPane.addTab("数据统计", new AdminStatPanel());
        }
        add(tabPane, BorderLayout.CENTER);

        // 整体主面板背景
        getContentPane().setBackground(new Color(248, 229, 240));
    }

    // Google蓝色圆角按钮
    private JButton createColorfulButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(66, 133, 244));
        button.setForeground(Color.white);
        button.setFocusPainted(false);
        button.setFont(new Font("微软雅黑", Font.BOLD, 15));
        button.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI()); // 防止被LAF覆盖圆角
        button.setBorder(new RoundedBorder(16));
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
            g.setColor(new Color(66, 133, 244));
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}