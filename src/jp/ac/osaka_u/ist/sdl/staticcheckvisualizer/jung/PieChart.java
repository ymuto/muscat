package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
 
import javax.swing.Icon;
 
public class PieChart implements Icon {
    /**
	 * �~�O���t�̊����D
	 */
    private int percentage;
    /**
	 * �~�O���t�̉����D
	 */
    private int width;
    /**
	 * �~�O���t�̍����D
	 */
    private int height;
    /**
	 * �~�O���t�̗v�f�̓h��Ԃ��F�D
	 */
    private Color foregroundColor = Color.GREEN;
 
    /**
	 * �~�O���t�̗v�f�ȊO�̕����̓h��Ԃ��F�D
	 */
    private Color backgroundColor = Color.WHITE;
    /**
	 * �~�O���t�̗֊s�̐F�D
	 */
    private Color drawColor = Color.BLACK;
 
    /**
	 * �������x���̐F�D
	 */
    private Color labelColor = Color.BLACK;
 
    /**
	 * �������x����\�����邩�ǂ����D
	 */
    private boolean isWithLabel = true;
    
    /**
     * �������x���̃t�H���g�D
     */
    private Font labelFont;
 
    /**
     * �R���X�g���N�^�D
     * @param percentage
     * @param width
     * @param height
     */
    public PieChart(int percentage, int width, int height) {
        this.percentage = percentage;
        this.width = width;
        this.height = height;
        this.labelFont = new Font("Dialog", Font.PLAIN, 16);
    }
 
    public int getIconHeight() {
        return height;
    }
 
    public int getIconWidth() {
        return width;
    }
 
    /**
	 * �~�O���t��`�悵�܂��D
	 */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        //�~�S�̂�w�i�F�œh��Ԃ�
        g.setColor(this.backgroundColor);
        g.fillOval(x, y, this.width, this.width);
        //�~�O���t�̗v�f���`�ɓh��Ԃ�
        g.setColor(this.foregroundColor);
        int angle = Math.round(360 * percentage / 100);
        g.fillArc(x, y, this.width, this.width, 360-angle+90, angle);
        //�֊s�̉~��`��
        g.setColor(this.drawColor);
        g.drawOval(x, y, this.width, this.width);
        //�������x����`��
        if (this.isWithLabel) {
        	g.setFont(this.labelFont);
            String str = percentage + "%";
            Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(str, g);
            int stringWidth = (int)stringBounds.getWidth();
            int stringHeight = (int)stringBounds.getHeight();
            int centerX = x + this.width / 2;
            int centerY = y + this.height / 2;
            int labelX = centerX - stringWidth / 2;
            int labelY = centerY + stringHeight / 2;
            g.setColor(this.labelColor);
            g.drawString(str, labelX, labelY);
        }
    }

	public void setLabelFont(Font labelFont) {
		this.labelFont = labelFont;
	}
    
    
 
}
