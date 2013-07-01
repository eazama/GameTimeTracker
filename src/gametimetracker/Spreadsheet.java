/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametimetracker;

import com.google.gdata.client.spreadsheet.*;
import com.google.gdata.data.spreadsheet.*;
import com.google.gdata.util.ServiceException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Spreadsheet {

    private String SPREADSHEETNAME;
    private String WORKSHEETNAME;
    private SpreadsheetService SSservice;
    private SpreadsheetEntry ssentry;
    private WorksheetEntry wsentry;

    Spreadsheet(String googleUser, String googlePass, String spreadsheetName, String worksheetName) throws IOException, ServiceException {

        if (googleUser == null || googlePass == null || spreadsheetName == null || worksheetName == null) {
            throw new ServiceException("parameter error");
        }

        SPREADSHEETNAME = spreadsheetName;
        WORKSHEETNAME = worksheetName;
        String USERNAME = googleUser;
        String PASSWORD = googlePass;
        SSservice = new SpreadsheetService("GameTimeTracker");
        try {
            SSservice.setUserCredentials(USERNAME, PASSWORD);
        } catch (Exception ex) {
        }
        ssentry = getSpreadsheet(SSservice, SPREADSHEETNAME);

        wsentry = getWorksheet(SSservice, ssentry, WORKSHEETNAME);
    }

    public void ResizeSheet(String file) throws Exception {
        SpreadsheetEntry theSpread = getSpreadsheet(SSservice, SPREADSHEETNAME);
        WorksheetEntry theSheet = getWorksheet(SSservice, theSpread, file);

        theSheet.setRowCount(1);
        theSheet.update();
    }
    public void ResizeSheet(){
        wsentry.setRowCount(1);
        try{
        wsentry.update();
        }catch(Exception ex){}
    }

    public void addRow(String gameTitle, long startTime, long endTime) throws IOException, ServiceException {
        URL listFeedUrl = wsentry.getListFeedUrl();

        ListEntry row = new ListEntry();

        long diff = endTime - startTime;
        String diffString = String.format("%02d:%02d:%02d", diff / (1000 * 60 * 60), diff / (1000 * 60) % 60, diff / (1000) % 60);

        java.text.Format format = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        

        String startString = String.format("%02d:%02d:%02d",
                startTime / (1000 * 60 * 60) % 24, startTime / (1000 * 60) % 60, startTime / (1000) % 60);


        // Update the row's data.
        row.getCustomElements().setValueLocal("Game", gameTitle);
        row.getCustomElements().setValueLocal("Start", format.format(startTime).toString());
        System.out.println("start " + format.format(startTime).toString());
        row.getCustomElements().setValueLocal("End", format.format(endTime).toString());
        row.getCustomElements().setValueLocal("Length", diffString);

        // Save the row using the API.
        SSservice.insert(listFeedUrl, row);

    }
    
    public void addRow(TimeEntry ent)throws IOException, ServiceException {
        URL listFeedUrl = wsentry.getListFeedUrl();

        ListEntry row = new ListEntry();
        row.getCustomElements().setValueLocal("Game", ent.gameName);
        row.getCustomElements().setValueLocal("Length", ent.time);
        
        SSservice.insert(listFeedUrl, row);
    }

    private SpreadsheetEntry getSpreadsheet(SpreadsheetService SSservice, String sheetName) throws IOException, ServiceException {
        URL SPREADSHEET_FEED_URL = null;
        try {
            SPREADSHEET_FEED_URL = new URL(
                    "https://spreadsheets.google.com/feeds/spreadsheets/private/full");
        } catch (Exception ex) {
        }
        SpreadsheetFeed feed = SSservice.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
        List<SpreadsheetEntry> spreadsheets = feed.getEntries();

        for (SpreadsheetEntry spreadsheet : spreadsheets) {
            if (spreadsheet.getTitle().getPlainText().equals(sheetName)) {
                new SpreadsheetEntry();
                return spreadsheet;
            }
        }


        return null;
    }

    private WorksheetEntry getWorksheet(SpreadsheetService SSservice, SpreadsheetEntry SSheet, String worksheetName) throws IOException, ServiceException {
        WorksheetFeed worksheetFeed = SSservice.getFeed(
                SSheet.getWorksheetFeedUrl(), WorksheetFeed.class);
        List<WorksheetEntry> worksheets = worksheetFeed.getEntries();

        for (WorksheetEntry worksheet : worksheets) {
            if (worksheet.getTitle().getPlainText().equals(worksheetName)) {
                return worksheet;
            }
        }

        return null;
    }

    public WorksheetEntry returnWorksheetEntry(String letter) {
        try {
            return getWorksheet(SSservice, getSpreadsheet(SSservice, SPREADSHEETNAME), letter);
        } catch (Exception ex) {
            return null;
        }

    }
    
    public WorksheetEntry getWorksheetEntry(){
        return wsentry;
    }
    
    public ListFeed getListFeed(){             
        try{
            return SSservice.getFeed(wsentry.getListFeedUrl(), ListFeed.class);
        } catch(Exception ex){return null;}
            
    }
    
    public void setWorksheetName(String nName){
        WORKSHEETNAME = nName;
        try{
        wsentry = getWorksheet(SSservice, ssentry, WORKSHEETNAME);
        }catch(Exception ex){};
    }
}
