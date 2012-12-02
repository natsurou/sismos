/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.gicsdr.sismos;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;

/**
 *
 * @author escuelab
 */
public class SismoMidi {
    SismoProcess sismoSource;
    Sequence s;
    Track t;
    public SismoMidi(SismoProcess sismoSource)throws InvalidMidiDataException {
        this.sismoSource = sismoSource;
        
        s = new Sequence(javax.sound.midi.Sequence.PPQ,30);
        t = s.createTrack();
//****  General MIDI sysex -- turn on General MIDI sound set  ****
        byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
        SysexMessage sm = new SysexMessage();
        sm.setMessage(b, 6);
        MidiEvent me = new MidiEvent(sm,(long)0);
        t.add(me);

//****  set tempo (meta event)  ****
        MetaMessage mt = new MetaMessage();
        byte[] bt = {0x02, (byte)0x00, 0x00};
        mt.setMessage(0x51 ,bt, 3);
        me = new MidiEvent(mt,(long)0);
        t.add(me);

//****  set track name (meta event)  ****
        mt = new MetaMessage();
        String TrackName = "sismos";
        mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
        me = new MidiEvent(mt,(long)0);
        t.add(me);

//****  set omni on  ****
        ShortMessage mm = new ShortMessage();
        mm.setMessage(0xB0, 0x7D,0x00);
        me = new MidiEvent(mm,(long)0);
        t.add(me);

//****  set poly on  ****
        mm = new ShortMessage();
        mm.setMessage(0xB0, 0x7F,0x00);
        me = new MidiEvent(mm,(long)0);
        t.add(me);

//****  set instrument to Piano  ****
        mm = new ShortMessage();
        mm.setMessage(0xC0, 0x00, 0x00);
        me = new MidiEvent(mm,(long)0);
        t.add(me);
    }
    
    public void addNote(int tick, int ticks, int note) throws InvalidMidiDataException{
//****  note on  ****
		ShortMessage mm = new ShortMessage();
		mm.setMessage(0x90,note,0x60);
		MidiEvent me = new MidiEvent(mm,(long)tick);
		t.add(me);

//****  note off - "ticks" ticks later  ****
		mm = new ShortMessage();
		mm.setMessage(0x80,note,0x40);
		me = new MidiEvent(mm,(long)(tick+ticks));
		t.add(me);
    }
    
    public void endAndWrite(int finalTick, String filename) throws InvalidMidiDataException, IOException {
        //****  set end of track (meta event) 19 ticks later  ****
        MetaMessage mt = new MetaMessage();
        byte[] bet = {}; // empty array
        mt.setMessage(0x2F,bet,0);
        MidiEvent me = new MidiEvent(mt, (long)finalTick);
        t.add(me);
        
        //****  write the MIDI sequence to a MIDI file  ****
        File f = new File(filename);
        MidiSystem.write(s,1,f);
    }
}
