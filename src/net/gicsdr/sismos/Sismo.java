/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gicsdr.sismos;

import java.util.GregorianCalendar;

/**
 *
 * @author escuelab
 */
public class Sismo {
    private float latitude;
    private float longitude;
    private float magnitude;
    private GregorianCalendar datetime;
    private boolean active;
    
    private int ttl;
    
    public Sismo(float latitude,float longitude,float magnitude,GregorianCalendar datetime){        
        this.latitude = latitude;
        this.longitude = longitude;
        this.magnitude = magnitude;
        this.datetime = datetime;
        ttl = SismoProcess.TTL_MAX;
        active = false;
    }
    
    public int getX(int width, float leftLon, float rightLon){
        //TO DO: Correct for extreme longitudes (e.g. 179 and -175)
        return (int)(width*(longitude - leftLon)/(rightLon - leftLon));
    }
    
    public int getY(int height, float topLat, float bottomLat){
        return (int)(height*(latitude - bottomLat)/(topLat - bottomLat));
    }
    
    public float getTtl() {
        return ttl;
    }
    
    public float getMag() {
        return magnitude;
    }
    
    public void activate() {
        active = true;
    }
    
    public boolean restLife() {
        ttl-=1;
        if(ttl<=0) {
            return false;
        }
        return true;
    }
    
    public GregorianCalendar getDateTime() {
        return datetime;
    }

    public boolean isActive() {
        return active;
    }
}
