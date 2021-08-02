package tool;

/**
 * ����
 */
public class FileSystem {

    public static int num = 5;  //�ļ����Դ򿪵ĸ���
    public static String folderPath = "img/folder.png";
    public static String folderselectPath = "img/folder_select.png";
    public static String filePath = "img/file.png";
    public static String fileselectPath = "img/file_select.png";

    public static int END = 255;  //������
    public static int DISK = 0;   //��ʾ��Ӳ��
    public static int FOLDER = 1; //��ʾ���ļ���
    public static int FILE = 2; //��ʾ���ļ�

    public static int ERROR = -1; //����

    public static int flagRead = 0; //ֻ��
    public static int flagWrite = 1;//��д

    /**
     * �������Ķ�̬��С
     */
    public static int getHeight(int n) {
        int a;
        a = n / 4;
        if (n % 4 > 0) {
            a++;
        }
        return a * 125;
    }

    /**
     * �����ļ�ʱд���ļ����
     */
    public static int getNumOfFAT(int length) {
        if (length <= 64) {
            return 1;
        } else {
            int n;
            if (length % 64 == 0) {
                n = length / 64;
            } else {
                n = length / 64;
                n++;
            }
            return n;
        }
    }
}
