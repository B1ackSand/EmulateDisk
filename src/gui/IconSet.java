package gui;


import javax.swing.ImageIcon;
import javax.swing.JLabel;

import tool.FileSystem;

/**
 * …Ë÷√Õº±Í
 *
 */
public class IconSet extends JLabel {
    public boolean type;

    public IconSet(boolean isFile, String content) {
        this.setVerticalTextPosition(JLabel.BOTTOM);
        this.setHorizontalTextPosition(JLabel.CENTER);
        this.setText(content);

        if (isFile) {
            this.setIcon(new ImageIcon(FileSystem.filePath));
            this.type = true;
        } else {
            this.setIcon(new ImageIcon(FileSystem.folderPath));
            this.type = false;
        }
    }
}
