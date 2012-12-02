/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gicsdr.sismos;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.swing.*;
/**
 *
 * @author escuelab
 */
public class Main{
    JFrame frame;
    final String TITLE = "Sismos";
    final String MAP_IMAGE_PATH = "mapa.png";
    SismoProcess sProcess;
    SismoGraph sGraph;
    SismoMidi sMidi;
    Thread updater;
    int width;
    int height;
    
    public Main() throws InvalidMidiDataException {
        frame = new JFrame();        
        frame.setTitle(TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);        
        
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(MAP_IMAGE_PATH));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(img!=null) {
            width = img.getWidth();
            height = img.getHeight();
            frame.setSize(img.getWidth()+5,img.getHeight()+35);
        }
        else {
            width = 800;
            height = 600;
        }
        sProcess = new SismoProcess(width, height, -0.03f, -18.39f, -81.32f, -68.65f);
        sGraph= new SismoGraph(sProcess);
        if(img!=null) {
            sGraph.setBackgroundImage(img);
        }
        sGraph.setCircleFillColor(Color.yellow);
        
        frame.add(sGraph);
        frame.setVisible(true);
        
        sMidi = new SismoMidi(sProcess);        
    }
    
    public void saveState(String name, String format){
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        sGraph.paintAll(g);
        
        File outputfile = new File(name);
        try {
            ImageIO.write(image, format, outputfile);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadSismos(String path){
        Pattern comas= Pattern.compile(",");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(reader!=null){
            String line;
            int i = 0;
            try {
                while((line = reader.readLine())!=null) {
                    
                    String[] values = comas.split(line);
                    sProcess.addSismo(new Sismo(
                            Float.parseFloat(values[1]),
                            Float.parseFloat(values[2]),
                            Float.parseFloat(values[4]),
                            new GregorianCalendar(
                            Integer.parseInt(values[0].substring(0, 4)),
                            Integer.parseInt(values[0].substring(4, 6))-1,
                            Integer.parseInt(values[0].substring(6, 8))-1,
                            Integer.parseInt(values[0].substring(8, 10)),
                            Integer.parseInt(values[0].substring(10, 12)),
                            Integer.parseInt(values[0].substring(12, 14))
                            )));
                    i++;
                }
            } catch (Exception ex) {
                System.err.println(i);
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void timeStamp(GregorianCalendar start, int steps, int months, int days, int hours, int minutes) {
        StamperThread stamperThread = new StamperThread(start,steps,months,days,hours,minutes);
        stamperThread.setPriority(Thread.MAX_PRIORITY);
        stamperThread.start();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidMidiDataException {
        Main main = new Main();
        main.loadSismos("2012_complete.csv");

        main.sProcess.setFadeOut(true);
        
        main.timeStamp(new GregorianCalendar(2012, 0, 6, 0, 0, 0), 360*4, 0, 0, 6, 0);
        
    }
    
    private class StamperThread extends Thread {
        GregorianCalendar now;
        int steps;
        int months;
        int days;
        int hours;
        int minutes;
        public StamperThread(GregorianCalendar start, int steps, int months, int days, int hours, int minutes) {
            super();
            now = (GregorianCalendar) start.clone();
            this.steps = steps;
            this.days = days;
            this.hours = hours;
            this.minutes = minutes;
            this.months = months;
        }
    
        @Override
        public void run() {
            int i;
            for (i = 0; i < steps; i++) {
                sProcess.takeStep(now);
                System.out.println(now.get(GregorianCalendar.YEAR)+" "+now.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.SHORT, Locale.JAPANESE));
                now.add(GregorianCalendar.MONTH, months);
                now.add(GregorianCalendar.DATE, days);
                now.add(GregorianCalendar.HOUR_OF_DAY, hours);
                now.add(GregorianCalendar.MINUTE, minutes);
                sGraph.setDescription(now.get(GregorianCalendar.YEAR)+", "+
                        now.getDisplayName(GregorianCalendar.MONTH, GregorianCalendar.SHORT, Locale.ENGLISH)+" "+
                        now.get(GregorianCalendar.DAY_OF_MONTH));
                                               
                SimpleDateFormat sdfAno = new SimpleDateFormat("yyyy");
                SimpleDateFormat sdfMes = new SimpleDateFormat("MMM", new Locale("es"));
                SimpleDateFormat sdfDia = new SimpleDateFormat("dd");
                
                sGraph.setAno(sdfAno.format(now.getTime()));
                sGraph.setMes(sdfMes.format(now.getTime()).substring(0, 2).toUpperCase());
                sGraph.setDia(sdfDia.format(now.getTime()));
                
                sGraph.paintAll(sGraph.getGraphics());         
                
                saveState(String.format("img%05d.jpg", i+1), "jpg");
                /*
                for (int j = 0; j < sProcess.getSismos().size(); j++) {
                    Sismo sismo = sProcess.getSismos().get(j);
                    if(sismo.isRecent()) {
                        try {
                            sMidi.addNote(i, SismoProcess.TTL_MAX, 0x3C+(int)(sismo.getMag()*5));
                        } catch (InvalidMidiDataException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } */
            }/*
            try {
                sMidi.endAndWrite(i+SismoProcess.TTL_MAX, "midi.mid");
            } catch (InvalidMidiDataException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }*/
        }
    }
}
