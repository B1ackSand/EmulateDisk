package object;

/**
 * �½����̶���
 *
 */
public class Disk {


    private String diskName;
    //���������캯��

    public Disk(String diskName) {
        super();
        this.diskName = diskName;
    }//super��

    @Override
    public String toString() {
        return diskName;
    }//��дtoString

}
