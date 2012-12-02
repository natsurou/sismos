/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gicsdr.sismos;

import java.awt.Color;
import java.awt.Font;
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
    private String ano;
    private String dia;
    private String mes;
    private Font anofont;
    private Font mesfont;
    private Font diafont;
    private Color anocolor;
    private Color mescolor;
    private Color diacolor;
            
    public SismoGraph(SismoProcess sismoSource){
        super();
        this.sismoSource = sismoSource;
        circleFillColor = Color.BLACK;
        circleBorderColor = Color.WHITE;
        this.ano = "";
        this.dia = "";
        this.mes = "";
        // this.anofont = new Font("Courier", Font.BOLD | Font.ITALIC ,40);
        this.anofont = new Font("Courier", Font.BOLD, 40);
        this.mesfont = new Font("Courier", Font.BOLD, 40);
        this.diafont = new Font("Courier", Font.BOLD, 40);              
        this.anocolor = new Color(0,255,0,124);
        this.mescolor = new Color(0,200,0,124);
        this.diacolor = new Color(0,150,0,124);

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
        // Pintar Año Mes Día
        g2.setFont(this.anofont);        
        g2.setColor(this.anocolor);        
        g2.drawString(this.ano, (int)(sismoSource.getWidth()*0.03), (int)(sismoSource.getHeight()*0.07));
        g2.setFont(this.mesfont);        
        g2.setColor(this.mescolor);
        g2.drawString(this.mes, (int)(sismoSource.getWidth()*0.03), (int)(sismoSource.getHeight()*0.13));
        g2.setFont(this.diafont);
        g2.setColor(this.diacolor);
        g2.drawString(this.dia, (int)(sismoSource.getWidth()*0.16), (int)(sismoSource.getHeight()*0.13));
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

    void setAno(String ano) {
        this.ano = ano;
    }

    void setMes(String mes) {
        this.mes = mes;
    }

    void setDia(String dia) {
        this.dia = dia;
    }

}
