package tool;

/**
 * 设置
 */
public class FileSystem {

    public static int num = 5;  //文件可以打开的个数
    public static String folderPath = "img/folder.png";
    public static String folderselectPath = "img/folder_select.png";
    public static String filePath = "img/file.png";
    public static String fileselectPath = "img/file_select.png";

    public static int END = 255;  //结束符
    public static int DISK = 0;   //表示是硬盘
    public static int FOLDER = 1; //表示是文件夹
    public static int FILE = 2; //表示是文件

    public static int ERROR = -1; //错误

    public static int flagRead = 0; //只读
    public static int flagWrite = 1;//读写

    /**
     * 设置面板的动态大小
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
     * 保存文件时写入文件块号
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
