/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gicsdr.sismos;

import java.util.GregorianCalendar;
import java.util.LinkedList;

/**
 *
 * @author escuelab
 */
public class SismoProcess {
    LinkedList<Sismo> sismos;
    static final int TTL_MAX = 30;
    private int width;
    private int height;
    private boolean fadeOut;
    float leftLon;
    float rightLon;
    float topLat;
    float bottomLat;
    
    public SismoProcess(int width, int height, float topLat, float bottomLat, float leftLon, float rightLon) {
        this.width = width;
        this.height = height;
        this.leftLon = leftLon;
        this.rightLon = rightLon;
        this.topLat = topLat;
        this.bottomLat = bottomLat;
        fadeOut = true;
        sismos = new LinkedList<>();
    }
    
    public void addSismo(Sismo s) {
        sismos.add(s);
    }
    
    public LinkedList<Sismo> getSismos(){
        return this.sismos;
    } 
    
    public void setFadeOut(boolean activated) {
        fadeOut = activated;
    }
    
    public int getX(Sismo s){
        //TO DO: Correct for extreme longitudes (e.g. 179 and -175)
        return s.getX(width, leftLon, rightLon);
    }
    
    public int getY(Sismo s){
        return s.getY(height, topLat, bottomLat);
    }

    public int getMag(Sismo s) {
        return (int)(s.getMag()*0.015*((width>height)?height:width));
    }

    int getWidth() {
        return width;
    }
    int getHeight() {
        return height;
    }
    
    public void takeStep(GregorianCalendar time){
        for (int i = 0; i < sismos.size(); i++) {
            Sismo s = sismos.get(i);
            if(s.getDateTime().before(time)) {
                s.activate();
            }
            if(fadeOut) {
                if(s.isActive()) {
                    if(s.restLife()==false) {
                        sismos.remove(s);
                    }
                }
            }
        }
    }
}
