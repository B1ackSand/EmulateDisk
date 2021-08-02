package object;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 文件夹属性设置
 *
 */
public class Folder {

    private String folderName; //3个字节
    //保留2个字节 填写空格
    private int diskNum;//1个字节
    //保留1个字节未利用 填写0
    private String type;//文件属性

    private boolean hasChild; //是否有子目录或者文件
    private int numOfFAT;

    //查看的属性
    private String location; //位置
    private String size;    //大小
    private String space;    //占用空间
    private Date createTime; //创建时间

    public Folder(String folderName, String location, int diskNum) {
        super();
        this.folderName = folderName;
        this.location = location;
        this.size = "0B";
        this.space = "0B";
        this.createTime = new Date();//设置创建时间
        this.diskNum = diskNum;
        this.type = "Folder";//文件属性
    }

    public String getFolderName() {
        return folderName;
    }//获取文件夹名

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }//设置文件夹名

    public String getLocation() {
        return location;
    }//获得文件夹位置

    public void setLocation(String location) {
        this.location = location;
    }//设置文件夹位置

    public String getCreateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        return format.format(createTime);
    }//获得创建时间

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return folderName;
    }
}
