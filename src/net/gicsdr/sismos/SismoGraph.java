/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gicsdr.sismos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.JComponent;

public class SismoGraph extends JComponent {
    
    private Color circleFillColor;
    private Color circleBorderColor;
    Image backgroundImage;
    SismoProcess sismoSource;
    private String displayName = "";
            
    public SismoGraph(SismoProcess sismoSource){
        super();
        this.sismoSource = sismoSource;
        circleFillColor = Color.BLACK;
        circleBorderColor = Color.WHITE;
    }
    
    public void setBackgroundImage(Image img) {
        backgroundImage = img;
    }
    
    public void setCircleFillColor(Color c){
        circleFillColor = c;
    }
    
    public void setCircleBorderColor(Color c){
        circleBorderColor = c;
    }
    
    @Override
    public void paint (Graphics g){
        if(backgroundImage!=null) {
            g.drawImage(backgroundImage, 0, 0, null);
        }
        Graphics2D g2 = (Graphics2D)g;
        
        //Draw circles
        for (int i = 0; i < sismoSource.getSismos().size(); i++) {
            Sismo sismo = sismoSource.getSismos().get(i);
            if(sismo.isActive()) {
                drawCircle(g,
                        sismoSource.getX(sismo),
                        sismoSource.getY(sismo),
                        sismoSource.getMag(sismo),
                        0.4f*sismo.getTtl()/SismoProcess.TTL_MAX
                        );
            }
        }
        g2.drawString(displayName, (int)(sismoSource.getWidth()*0.05), (int)(sismoSource.getHeight()*0.9));
    }
    
    public void drawCircle(Graphics g, int x, int y, int z, float alpha) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(new Color(
                circleFillColor.getRed(),
                circleFillColor.getGreen(),
                circleFillColor.getBlue(),
                (int)(alpha*255+0.5)));
        g2.fillOval( x-(z/2), sismoSource.getHeight()-y-(z/2), z, z);
        g2.setColor(new Color(
                circleBorderColor.getRed(),
                circleBorderColor.getGreen(),
                circleBorderColor.getBlue(),
                (int)(alpha*255+0.5)));
        g2.drawOval( x-(z/2), sismoSource.getHeight()-y-(z/2), z, z);
    }

    void setDescription(String displayName) {
        this.displayName = displayName;
    }

}
