package object;

import java.util.ArrayList;
import java.util.List;
import tool.FileSystem;

/**
 * 打开多个文件
 */
public class HasOpenMore {

    private List<HasOpen> files;
    private int length; //最多打开的数目

    public HasOpenMore() {  //新建可打开的文件，最多为 FileSystemUtil.num
        files = new ArrayList<HasOpen>(FileSystem.num);//数组存打开的文件数
        length = 0;
    }

    public void addFile(HasOpen hasOpen) { //add
        files.add(hasOpen);
    }//添加打开的文件

    public List<HasOpen> getFiles() {
        return files;
    }//获取打开的文件


}
