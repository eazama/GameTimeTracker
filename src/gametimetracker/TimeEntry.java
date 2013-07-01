/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gametimetracker;

public class TimeEntry {
    
    String gameName;
    String time = "0:00:00";
    
    public TimeEntry(String name){
        gameName = name;
    }
    
    public void addTime(String plusTime){
        System.out.println(String.format("adding %s to %s in %s", plusTime, time, gameName));
        String[] timeArray = time.split(":");
        String[] plusArray = plusTime.split(":");
        
        int hour = Integer.parseInt(timeArray[0]);
        int min = Integer.parseInt(timeArray[1]);
        int sec = Integer.parseInt(timeArray[2]);
        System.out.println(String.format("%dh, %dm, %ds", hour, min,sec));
        
        hour+=Integer.parseInt(plusArray[0]);
        min+=Integer.parseInt(plusArray[1]);
        sec+=Integer.parseInt(plusArray[2]);
        
        min+=(sec/60);
        sec=(sec%60);
        hour+=(min/60);
        min=(min%60);
        System.out.println(String.format("%dh, %dm, %ds", hour, min,sec));
        
        time = String.format("%d:%d:%d", hour,min,sec);
        
        
    }
    
}
