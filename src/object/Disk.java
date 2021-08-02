package object;

/**
 * 新建磁盘对象
 *
 */
public class Disk {


    private String diskName;
    //磁盘名构造函数

    public Disk(String diskName) {
        super();
        this.diskName = diskName;
    }//super类

    @Override
    public String toString() {
        return diskName;
    }//重写toString

}
