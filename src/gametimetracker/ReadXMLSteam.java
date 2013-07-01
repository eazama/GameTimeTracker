/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametimetracker;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReadXMLSteam {

    public static void read(String steamID) {

        
        
        if (steamID == null) {
            return;
        }

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                boolean bName = false;

                public void startElement(String uri, String localName, String qName,
                        Attributes attributes) throws SAXException {

                    //System.out.println("Start Element :" + qName);

                    if (qName.equalsIgnoreCase("name")) {
                        bName = true;
                    }
                }

                public void endElement(String uri, String localName,
                        String qName) throws SAXException {
                    //System.out.println("End Element :" + qName);
                }

                public void characters(char ch[], int start, int length) throws SAXException {

                    if (bName) {
                        //System.out.println(new String(ch, start, length));
                        try {
                            utilities.TextFileManager.appendToFile("GameLists/Clients/Steam.txt", new String(ch, start, length));
                        } catch (Exception ex) {
                        }

                        bName = false;

                    }
                }
            };

            utilities.TextFileManager.clearFile("GameLists/Clients/Steam.txt");
            saxParser.parse("http://steamcommunity.com/id/" + steamID + "/games?tab=all&xml=1", handler);
            utilities.TextFileManager.SortFile("GameLists/Clients/Steam.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
