package object;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * �ļ�����������
 *
 */
public class Folder {

    private String folderName; //3���ֽ�
    //����2���ֽ� ��д�ո�
    private int diskNum;//1���ֽ�
    //����1���ֽ�δ���� ��д0
    private String type;//�ļ�����

    private boolean hasChild; //�Ƿ�����Ŀ¼�����ļ�
    private int numOfFAT;

    //�鿴������
    private String location; //λ��
    private String size;    //��С
    private String space;    //ռ�ÿռ�
    private Date createTime; //����ʱ��

    public Folder(String folderName, String location, int diskNum) {
        super();
        this.folderName = folderName;
        this.location = location;
        this.size = "0B";
        this.space = "0B";
        this.createTime = new Date();//���ô���ʱ��
        this.diskNum = diskNum;
        this.type = "Folder";//�ļ�����
    }

    public String getFolderName() {
        return folderName;
    }//��ȡ�ļ�����

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }//�����ļ�����

    public String getLocation() {
        return location;
    }//����ļ���λ��

    public void setLocation(String location) {
        this.location = location;
    }//�����ļ���λ��

    public String getCreateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd��  HH:mm:ss");
        return format.format(createTime);
    }//��ô���ʱ��

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return folderName;
    }
}
