package object;

/**
 * 打开1个文件
 */

public class HasOpen {

    private int flag;// 操作类型 0表示以读操作方式打开文件，1表示以写操作方式打开文件
    private File file;

    public void setFlag(int flag) {
        this.flag = flag;
    }//设置操作方式

    public File getFile() {
        return file;
    }//获取文件类型

    public void setFile(File file) {
        this.file = file;
    }//设置文件类型

}
