package object;

/**
 * �ļ���������
 * �����е�ÿһ��ָ�벿����ȡ����������ļ������
 *
 */

public class FAT {

    private int index; //�±���������ݣ���ֵ������һ����ţ��� 255��ʾ�ļ����� �� 0��ʾ���У�128-254Ϊ���̿��(λ�ڴ���������)
    private int type;  //���ͷ�Ϊ��Ӳ�̣��ļ����ļ���
    private Object object; //˵��������Ŀ¼folder�����ļ�file

    public FAT(int index, int type, Object object) {
        super();
        this.index = index;
        this.type = type;
        this.object = object;
    }//super��


    public int getIndex() {
        return index;
    }//��ÿ��

    public void setIndex(int index) {
        this.index = index;
    }//�趨���

    public int getType() {
        return type;
    }//��ȡ����

    public Object getObject() {
        return object;
    }//��ȡ˵������

    public void setObject(Object object) {
        this.object = object;
    }//�趨˵������
}
