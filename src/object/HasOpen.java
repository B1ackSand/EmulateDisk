package object;

/**
 * ��1���ļ�
 */

public class HasOpen {

    private int flag;// �������� 0��ʾ�Զ�������ʽ���ļ���1��ʾ��д������ʽ���ļ�
    private File file;

    public void setFlag(int flag) {
        this.flag = flag;
    }//���ò�����ʽ

    public File getFile() {
        return file;
    }//��ȡ�ļ�����

    public void setFile(File file) {
        this.file = file;
    }//�����ļ�����

}
