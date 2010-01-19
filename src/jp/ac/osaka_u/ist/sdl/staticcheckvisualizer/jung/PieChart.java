package jp.ac.osaka_u.ist.sdl.staticcheckvisualizer.jung;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
 
import javax.swing.Icon;
 
public class PieChart implements Icon {
    /**
	 * 円グラフの割合．
	 */
    private int percentage;
    /**
	 * 円グラフの横幅．
	 */
    private int width;
    /**
	 * 円グラフの高さ．
	 */
    private int height;
    /**
	 * 円グラフの要素の塗りつぶし色．
	 */
    private Color foregroundColor = Color.GREEN;
 
    /**
	 * 円グラフの要素以外の部分の塗りつぶし色．
	 */
    private Color backgroundColor = Color.WHITE;
    /**
	 * 円グラフの輪郭の色．
	 */
    private Color drawColor = Color.BLACK;
 
    /**
	 * 割合ラベルの色．
	 */
    private Color labelColor = Color.BLACK;
 
    /**
	 * 割合ラベルを表示するかどうか．
	 */
    private boolean isWithLabel = true;
    
    /**
     * 割合ラベルのフォント．
     */
    private Font labelFont;
 
    /**
     * コンストラクタ．
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
	 * 円グラフを描画します．
	 */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        //円全体を背景色で塗りつぶす
        g.setColor(this.backgroundColor);
        g.fillOval(x, y, this.width, this.width);
        //円グラフの要素を扇形に塗りつぶす
        g.setColor(this.foregroundColor);
        int angle = Math.round(360 * percentage / 100);
        g.fillArc(x, y, this.width, this.width, 360-angle+90, angle);
        //輪郭の円を描画
        g.setColor(this.drawColor);
        g.drawOval(x, y, this.width, this.width);
        //割合ラベルを描画
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
