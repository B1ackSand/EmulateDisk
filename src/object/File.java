package object;

import java.text.SimpleDateFormat;
import java.util.Date;


public class File {

    /**
     * 文件属性设置
     */
    private String fileName; //仅可以使用字母、数字和除“$”、“.”、“/”以外的字符
    private String type;//文件属性名 2个字节
    private int diskNum;//起始盘块号 1个字节
    private int length;//文件长度 1个字节
    private String content;//文件内容 大小不限

    private int numOfFAT;
    private Folder parent; //父目录

    //查看的属性
    private String location; //位置
    private String size;    //大小
    private String space;    //占用空间
    private Date createTime; //创建时间

    private boolean isReadOnly; //是否只读
    private boolean isHide;  //是否隐藏

    public File(String fileName, String location, int diskNum) {
        super();
        this.fileName = fileName;
        this.location = location;
        this.size = "0B";
        this.space = "0B";
        this.createTime = new Date();//获取本机时间
        this.diskNum = diskNum;
        this.type = "File";//文件属性
        this.isReadOnly = false;
        this.isHide = false;
        this.length = 8;
        this.content = "";
    }//super类

    public String getFileName() {
        return fileName;
    }//获得文件名

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }//设置文件名

    public String getType() {
        return type;
    }//获得文件类型名

    public int getDiskNum() {
        return diskNum;
    }//获得磁盘块号

    public int getLength() {
        return length;
    }//获得文件长度

    public void setLength(int length) {
        this.length = length;
    }//设置文件长度

    public String getContent() {
        return content;
    }//获得文件内容

    public void setContent(String content) {
        this.content = content;
    }//设置文件内容

    public String getLocation() {
        return location;
    }//获得文件位置

    public void setLocation(String location) {
        this.location = location;
    }//设置文件位置

    public String getSize() {
        return size;
    }//获得文件大小

    public void setSize(String size) {
        this.size = size;
    }//设置文件大小

    public String getCreateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日  HH:mm:ss");
        return format.format(createTime);
    }//获得文件创建时间

    public boolean isReadOnly() {
        return isReadOnly;
    }//是否只读

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }//设置只读

    @Override
    public String toString() {
        return fileName;
    }//返回文件名
}
