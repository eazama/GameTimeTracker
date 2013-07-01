/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametimetracker;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ReadXMLDesura {

    public static void read(String login, String pass) {

        if(login == null || pass == null){
            return;
        }
        
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                boolean bName = false;
                boolean bGame = false;
                boolean bPlatforms = false;

                public void startElement(String uri, String localName, String qName,
                        Attributes attributes) throws SAXException {

                    //System.out.println("Start Element :" + qName);


                    if (qName.equalsIgnoreCase("game")) {
                        bGame = true;
                    }
                    if (qName.equalsIgnoreCase("name")&&bGame) {
                        bName = true;
                    }
                    if(qName.equalsIgnoreCase("platforms")){
                        bPlatforms = true;
                    }
                }

                public void endElement(String uri, String localName,
                        String qName) throws SAXException {
                    //System.out.println("End Element :" + qName);
                }

                public void characters(char ch[], int start, int length) throws SAXException {

                    if (bName && bGame&& !bPlatforms) {
                        //System.out.println(new String(ch, start, length));
                        try {
                            utilities.TextFileManager.appendToFile("GameLists/Clients/Desura.txt", new String(ch, start, length));
                        } catch (Exception ex) {
                        }
                        bName = false;
                        bGame = false;
                    }
                }
            };

            utilities.TextFileManager.clearFile("GameLists/Clients/Desura.txt");

            String cookie = "";
            try {
                URL url = new URL("https://secure.desura.com/3/memberlogin");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.connect();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
                writer.write("username=" + URLEncoder.encode(login, "UTF-8"));
                writer.write("&password=" + URLEncoder.encode(pass, "UTF-8"));
                writer.flush();
                writer.close();
                cookie = conn.getHeaderField("Set-Cookie");
                conn.disconnect();
            } catch (Exception ex) {
            }

            URL url = new URL("http://api.desura.com/1/memberdata");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", cookie);
            conn.connect();

            System.out.println(cookie);
            saxParser.parse(conn.getInputStream(), handler);
            utilities.TextFileManager.SortFile("GameLists/Clients/Desura.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
