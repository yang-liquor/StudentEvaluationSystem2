package com.studentevaluation.ui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员问卷编辑面板（支持多份问卷，保存为新问卷文件，含教师工号和姓名）
 */
public class AdminEvaluateSettingPanel extends JPanel {
    private JPanel questionListPanel;
    private JScrollPane scrollPane;

    // 新增：问卷文件列表相关
    private DefaultComboBoxModel<String> questionnaireModel = new DefaultComboBoxModel<>();
    private JComboBox<String> questionnaireBox = new JComboBox<>(questionnaireModel);
    private JButton deleteBtn = new JButton("删除问卷");
    private JButton refreshBtn = new JButton("刷新列表");

    public AdminEvaluateSettingPanel(AdminImportExportPanel historyPanel, boolean showSaveButtons) {
        this(); // 调用无参构造器
    }

    public AdminEvaluateSettingPanel() {
        setLayout(new BorderLayout());

        // ========== 新增：顶部问卷列表及操作 ==========
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("已保存问卷："));
        topPanel.add(questionnaireBox);
        topPanel.add(refreshBtn);
        topPanel.add(deleteBtn);

        // 加载列表和按钮事件
        refreshBtn.addActionListener(e -> loadQuestionnaireList());
        deleteBtn.addActionListener(e -> deleteSelectedQuestionnaire());
        questionnaireBox.addActionListener(e -> {
            String file = (String) questionnaireBox.getSelectedItem();
            if (file != null && !file.isEmpty()) {
                loadQuestionnaireFromFile("questionnaires/" + file);
            }
        });

        add(topPanel, BorderLayout.NORTH);

        // ========== 标题 ==========
        JLabel title = new JLabel("评教问卷设置", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 20));
        add(title, BorderLayout.CENTER);

        // ========== 题目编辑面板 ==========
        questionListPanel = new JPanel();
        questionListPanel.setLayout(new BoxLayout(questionListPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(questionListPanel);

        // 用 CENTER 占位
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(title, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // ========== 底部操作按钮 ==========
        JButton addBtn = new JButton("添加题目");
        JButton saveBtn = new JButton("保存为新问卷");
        JButton loadBtn = new JButton("加载问卷文件");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        btnPanel.add(addBtn);
        btnPanel.add(saveBtn);
        btnPanel.add(loadBtn);
        add(btnPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addQuestionPanel(null));
        saveBtn.addActionListener(this::saveAsNewQuestionnaire);
        loadBtn.addActionListener(this::loadQuestionnaireDialog);

        // 初始加载
        loadQuestionnaireList();
        clearAndNew();
    }

    /** 加载问卷文件列表到下拉框 */
    private void loadQuestionnaireList() {
        questionnaireModel.removeAllElements();
        File dir = new File("questionnaires");
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles((f, n) -> n.endsWith(".json"));
            if (files != null) {
                for (File f : files) questionnaireModel.addElement(f.getName());
            }
        }
    }

    /** 删除选中问卷 */
    private void deleteSelectedQuestionnaire() {
        String file = (String) questionnaireBox.getSelectedItem();
        if (file == null || file.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请选择要删除的问卷！");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this, "确定要删除【" + file + "】吗？此操作不可撤销！", "确认", JOptionPane.YES_NO_OPTION);
        if (ok == JOptionPane.YES_OPTION) {
            File f = new File("questionnaires", file);
            if (f.delete()) {
                JOptionPane.showMessageDialog(this, "删除成功！");
                loadQuestionnaireList();
                clearAndNew();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败！");
            }
        }
    }

    public void clearAndNew() {
        questionListPanel.removeAll();
        addQuestionPanel(null);
        questionListPanel.revalidate();
        questionListPanel.repaint();
    }

    private void addQuestionPanel(QuestionData initData) {
        QuestionPanel qp = new QuestionPanel(initData, this::removeQuestionPanel);
        questionListPanel.add(qp);
        questionListPanel.revalidate();
        questionListPanel.repaint();
    }

    private void removeQuestionPanel(QuestionPanel qp) {
        questionListPanel.remove(qp);
        questionListPanel.revalidate();
        questionListPanel.repaint();
    }

    /** 从文件加载问卷（支持新结构和老结构） */
    public void loadQuestionnaireFromFile(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            // 判断是否为对象格式（新结构）
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String firstLine = br.readLine();
            br.close();
            if (firstLine != null && firstLine.trim().startsWith("{")) {
                Questionnaire questionnaire = gson.fromJson(new FileReader(filePath), Questionnaire.class);
                setAllQuestions(questionnaire.questions);
            } else {
                // 老格式兼容
                List<QuestionData> questions = gson.fromJson(reader, new TypeToken<List<QuestionData>>(){}.getType());
                setAllQuestions(questions);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "加载问卷失败：" + ex.getMessage());
        }
    }

    /** 弹窗选择文件加载 */
    private void loadQuestionnaireDialog(ActionEvent e) {
        JFileChooser chooser = new JFileChooser("questionnaires");
        chooser.setDialogTitle("选择问卷文件");
        int ret = chooser.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            loadQuestionnaireFromFile(file.getPath());
        }
    }

    /** 保存为新问卷文件（含工号和姓名） */
    private void saveAsNewQuestionnaire(ActionEvent e) {
        List<QuestionData> questions = getAllQuestions();
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请先添加题目！");
            return;
        }
        for (QuestionData data : questions) {
            if (data == null || data.title == null || data.title.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "有题目信息未填写完整！");
                return;
            }
            if ("单选".equals(data.type) && (data.options == null || data.options.size() < 2)) {
                JOptionPane.showMessageDialog(this, "单选题需至少两个选项！");
                return;
            }
        }
        String title = JOptionPane.showInputDialog(this, "输入问卷标题（如：2025-高数）：");
        if (title == null || title.trim().isEmpty()) return;

        String teacherId = JOptionPane.showInputDialog(this, "输入教师工号：");
        if (teacherId == null || teacherId.trim().isEmpty()) return;

        String teacherName = JOptionPane.showInputDialog(this, "输入教师姓名：");
        if (teacherName == null || teacherName.trim().isEmpty()) return;

        String fileName = JOptionPane.showInputDialog(this, "输入文件名（如：2025-高数.json）：");
        if (fileName == null || fileName.trim().isEmpty()) return;
        if (!fileName.endsWith(".json")) fileName += ".json";
        File folder = new File("questionnaires");
        if (!folder.exists()) folder.mkdir();

        try (Writer writer = new FileWriter(new File(folder, fileName))) {
            Gson gson = new Gson();
            Questionnaire questionnaire = new Questionnaire(title, teacherId, teacherName, questions);
            gson.toJson(questionnaire, writer);
            JOptionPane.showMessageDialog(this, "保存成功！");
            loadQuestionnaireList(); // 保存后自动刷新列表
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "保存失败：" + ex.getMessage());
        }
    }

    /** 获取所有题目 */
    public List<QuestionData> getAllQuestions() {
        List<QuestionData> questions = new ArrayList<>();
        for (Component c : questionListPanel.getComponents()) {
            if (c instanceof QuestionPanel qp) {
                QuestionData data = qp.getQuestionData();
                if (data != null) {
                    questions.add(data);
                }
            }
        }
        return questions;
    }

    /** 设置全部题目（编辑/加载） */
    public void setAllQuestions(List<QuestionData> questions) {
        questionListPanel.removeAll();
        if (questions != null && !questions.isEmpty()) {
            for (QuestionData q : questions) {
                addQuestionPanel(q);
            }
        } else {
            addQuestionPanel(null);
        }
        questionListPanel.revalidate();
        questionListPanel.repaint();
    }

    // =================== 结构体 =====================

    /** 问卷结构体，含工号和姓名 */
    public static class Questionnaire {
        public String title;
        public String teacherId;
        public String teacherName;
        public List<QuestionData> questions;

        public Questionnaire(String title, String teacherId, String teacherName, List<QuestionData> questions) {
            this.title = title;
            this.teacherId = teacherId;
            this.teacherName = teacherName;
            this.questions = questions;
        }
    }

    // 问题结构体
    public static class QuestionData {
        public String title;
        public String type;
        public List<String> options;

        public QuestionData(String title, String type, List<String> options) {
            this.title = title;
            this.type = type;
            this.options = options;
        }
    }

    // =================== 题目编辑面板 =====================
    static class QuestionPanel extends JPanel {
        private final JComboBox<String> typeBox;
        private final JTextField titleField;
        private final JPanel optionPanel;
        private final ArrayList<JTextField> optionFields = new ArrayList<>();
        private final JButton addOptionBtn;

        QuestionPanel(QuestionData init, java.util.function.Consumer<QuestionPanel> onRemove) {
            setBorder(BorderFactory.createTitledBorder("题目"));
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel h1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
            h1.add(new JLabel("题干："));
            titleField = new JTextField(24);
            h1.add(titleField);

            h1.add(new JLabel("类型："));
            typeBox = new JComboBox<>(new String[]{"单选", "打分", "简答"});
            h1.add(typeBox);

            JButton delBtn = new JButton("删除本题");
            delBtn.addActionListener(e -> onRemove.accept(this));
            h1.add(delBtn);

            add(h1);

            optionPanel = new JPanel();
            optionPanel.setLayout(new BoxLayout(optionPanel, BoxLayout.Y_AXIS));
            addOptionBtn = new JButton("添加选项");
            addOptionBtn.addActionListener(e -> addOptionField(null));
            JPanel opBtnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            opBtnPanel.add(addOptionBtn);
            add(optionPanel);
            add(opBtnPanel);

            // 类型切换
            typeBox.addActionListener(e -> updateOptionPanel());

            // 初始化
            if (init != null) {
                titleField.setText(init.title);
                typeBox.setSelectedItem(init.type);
                if ("单选".equals(init.type) && init.options != null) {
                    for (String opt : init.options) addOptionField(opt);
                }
            } else {
                updateOptionPanel(); // 默认
            }
        }

        private void updateOptionPanel() {
            String type = (String) typeBox.getSelectedItem();
            optionPanel.removeAll();
            optionFields.clear();
            if ("单选".equals(type)) {
                addOptionBtn.setEnabled(true);

            } else {
                addOptionBtn.setEnabled(false);
            }
            optionPanel.revalidate();
            optionPanel.repaint();
        }

        private void addOptionField(String text) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JTextField optField = new JTextField(15);
            if (text != null) optField.setText(text);
            JButton delBtn = new JButton("删除");
            delBtn.addActionListener(e -> {
                optionPanel.remove(p);
                optionFields.remove(optField);
                optionPanel.revalidate();
                optionPanel.repaint();
            });
            p.add(optField);
            p.add(delBtn);
            optionPanel.add(p);
            optionFields.add(optField);
            optionPanel.revalidate();
            optionPanel.repaint();
        }

        QuestionData getQuestionData() {
            String title = titleField.getText().trim();
            String type = (String) typeBox.getSelectedItem();
            if (title.isEmpty()) return null;
            if ("单选".equals(type)) {
                ArrayList<String> opts = new ArrayList<>();
                for (JTextField f : optionFields) {
                    String opt = f.getText().trim();
                    if (!opt.isEmpty()) opts.add(opt);
                }
                if (opts.size() < 2) return null;
                return new QuestionData(title, type, opts);
            } else {
                return new QuestionData(title, type, null);
            }
        }
    }
}