import com.formdev.flatlaf.FlatLightLaf;
import com.studentevaluation.ui.AdminEvaluateSettingPanel;
import com.studentevaluation.ui.AdminImportExportPanel;
import com.studentevaluation.ui.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        UIManager.put("Component.arc", 18);         // 控件圆角
        UIManager.put("TextComponent.arc", 16);     // 输入框圆角
        UIManager.put("Button.arc", 20);            // 按钮圆角
        UIManager.put("Component.shadowWidth", 10); // 阴影
        UIManager.put("Button.shadowWidth", 12);
        UIManager.put("Button.hoverBackground", new Color(66, 133, 244, 180));
        FlatLightLaf.setup();

        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });

    }
}