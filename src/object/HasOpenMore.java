package object;

import java.util.ArrayList;
import java.util.List;
import tool.FileSystem;

/**
 * �򿪶���ļ�
 */
public class HasOpenMore {

    private List<HasOpen> files;
    private int length; //���򿪵���Ŀ

    public HasOpenMore() {  //�½��ɴ򿪵��ļ������Ϊ FileSystemUtil.num
        files = new ArrayList<HasOpen>(FileSystem.num);//�����򿪵��ļ���
        length = 0;
    }

    public void addFile(HasOpen hasOpen) { //add
        files.add(hasOpen);
    }//��Ӵ򿪵��ļ�

    public List<HasOpen> getFiles() {
        return files;
    }//��ȡ�򿪵��ļ�


}
