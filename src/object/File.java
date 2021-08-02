package object;

import java.text.SimpleDateFormat;
import java.util.Date;


public class File {

    /**
     * �ļ���������
     */
    private String fileName; //������ʹ����ĸ�����ֺͳ���$������.������/��������ַ�
    private String type;//�ļ������� 2���ֽ�
    private int diskNum;//��ʼ�̿�� 1���ֽ�
    private int length;//�ļ����� 1���ֽ�
    private String content;//�ļ����� ��С����

    private int numOfFAT;
    private Folder parent; //��Ŀ¼

    //�鿴������
    private String location; //λ��
    private String size;    //��С
    private String space;    //ռ�ÿռ�
    private Date createTime; //����ʱ��

    private boolean isReadOnly; //�Ƿ�ֻ��
    private boolean isHide;  //�Ƿ�����

    public File(String fileName, String location, int diskNum) {
        super();
        this.fileName = fileName;
        this.location = location;
        this.size = "0B";
        this.space = "0B";
        this.createTime = new Date();//��ȡ����ʱ��
        this.diskNum = diskNum;
        this.type = "File";//�ļ�����
        this.isReadOnly = false;
        this.isHide = false;
        this.length = 8;
        this.content = "";
    }//super��

    public String getFileName() {
        return fileName;
    }//����ļ���

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }//�����ļ���

    public String getType() {
        return type;
    }//����ļ�������

    public int getDiskNum() {
        return diskNum;
    }//��ô��̿��

    public int getLength() {
        return length;
    }//����ļ�����

    public void setLength(int length) {
        this.length = length;
    }//�����ļ�����

    public String getContent() {
        return content;
    }//����ļ�����

    public void setContent(String content) {
        this.content = content;
    }//�����ļ�����

    public String getLocation() {
        return location;
    }//����ļ�λ��

    public void setLocation(String location) {
        this.location = location;
    }//�����ļ�λ��

    public String getSize() {
        return size;
    }//����ļ���С

    public void setSize(String size) {
        this.size = size;
    }//�����ļ���С

    public String getCreateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd��  HH:mm:ss");
        return format.format(createTime);
    }//����ļ�����ʱ��

    public boolean isReadOnly() {
        return isReadOnly;
    }//�Ƿ�ֻ��

    public void setReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }//����ֻ��

    @Override
    public String toString() {
        return fileName;
    }//�����ļ���
}
