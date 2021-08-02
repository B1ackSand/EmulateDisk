package tool;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * 显示对话框选项和报错
 *
 */
public class Message {

    public static void showErrorMgs(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "错误", JOptionPane.WARNING_MESSAGE);
    }

    public static int showConfirmMgs(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "信息", JOptionPane.YES_NO_OPTION);
    }
}
