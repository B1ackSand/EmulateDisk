package tool;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * ��ʾ�Ի���ѡ��ͱ���
 *
 */
public class Message {

    public static void showErrorMgs(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "����", JOptionPane.WARNING_MESSAGE);
    }

    public static int showConfirmMgs(Component parent, String message) {
        return JOptionPane.showConfirmDialog(parent, message, "��Ϣ", JOptionPane.YES_NO_OPTION);
    }
}
