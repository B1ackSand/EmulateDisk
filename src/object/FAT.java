package object;

/**
 * 文件分配表对象
 * 磁盘中的每一块指针部分提取出来，组成文件分配表
 *
 */

public class FAT {

    private int index; //下标里面的内容，数值代表下一跳块号：① 255表示文件结束 ② 0表示空闲，128-254为坏盘块号(位于磁盘区块外)
    private int type;  //类型分为：硬盘，文件，文件夹
    private Object object; //说明对象是目录folder还是文件file

    public FAT(int index, int type, Object object) {
        super();
        this.index = index;
        this.type = type;
        this.object = object;
    }//super类


    public int getIndex() {
        return index;
    }//获得块号

    public void setIndex(int index) {
        this.index = index;
    }//设定块号

    public int getType() {
        return type;
    }//获取类型

    public Object getObject() {
        return object;
    }//获取说明对象

    public void setObject(Object object) {
        this.object = object;
    }//设定说明对象
}
