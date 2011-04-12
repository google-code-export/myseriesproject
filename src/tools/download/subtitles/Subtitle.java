/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.download.subtitles;

import java.net.URL;
import myComponents.MyUsefulFunctions;

/**
 *
 * @author lordovol
 */
public class Subtitle {

    public String title;
    public URL url;
    public int love = 0;
    public int hate = 0;

    /**
     *
     * @param title
     * @param url
     */
    public Subtitle(String title, URL url, int love, int hate) {
        this.title = MyUsefulFunctions.deleteDoubleSpaces(title);
        this.url = url;
        this.love = love;
        this.hate = hate;
    }

    @Override
    public String toString() {
        if(love+hate == 0){
            return title;
        }
        int p = love *100 /(love+hate);
        return title + " " + p +"%";
    }
}
