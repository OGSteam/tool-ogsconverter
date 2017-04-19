/*
 * MonFiltre.java
 *
 * Created on 24 septembre 2006, 12:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package fr.ogsteam.ogsconverter;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 *
 * @author MOREAU Benoît
 */
public class MonFiltre extends FileFilter {
    
    String []lesSuffixes;
    String []lesPreffixes = null;
    String  laDescription;
    
    int SUFFIXE = 0;
    int PREFFIXE = 1;
    
    public MonFiltre(String []lesSuffixes, String laDescription){
        this.lesSuffixes = lesSuffixes;
        this.laDescription = laDescription;
    }
    
    public MonFiltre(String []lesPreffixes, String []lesSuffixes, String laDescription){
        this.lesPreffixes = lesPreffixes;
        this.lesSuffixes = lesSuffixes;
        this.laDescription = laDescription;
    }
    
    boolean appartient( String s , int type){
        switch(type) {
            case 0:
                for( int i = 0; i<lesSuffixes.length; ++i){
                    if(s.equals(lesSuffixes[i])) return true;
                }
                break;
                
            case 1:
                if(lesPreffixes == null) return true;
                for( int i = 0; i<lesPreffixes.length; ++i){
                    if(s.length() >= lesPreffixes[i].length()) {
                        if(s.subSequence(0, lesPreffixes[i].length()).equals(lesPreffixes[i])) return true;
                    }
                }
                break;
        }
        return false;
    }
    
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        String suffixe = null;
        String preffixe = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1) {
            suffixe = s.substring(i+1).toLowerCase();
        }
        if (i > 0 &&  i < s.length() - 1) {
            preffixe = s.substring(0, i-1);
        }
        
        return suffixe != null && preffixe != null && appartient(suffixe, SUFFIXE) && appartient(preffixe, PREFFIXE);
    }
    
    // la description du filtre
    public String getDescription() {
        return laDescription;
    }
}