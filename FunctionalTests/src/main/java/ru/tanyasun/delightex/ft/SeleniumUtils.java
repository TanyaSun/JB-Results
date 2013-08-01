/***********************************************
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>
 **********************************************/
package ru.tanyasun.delightex.ft;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.ResourceBundle;

/**
 * @author TanyaSun
 */
public class SeleniumUtils {

    public final static String CHAT_PROPERTIES = "chat";


    public static WebDriver getFirefoxBrowser() {
        ResourceBundle bundle = ResourceBundle.getBundle(CHAT_PROPERTIES);
        String url  = bundle.getString("chat.url");

        FirefoxDriver driver = new FirefoxDriver();
        driver.get(url);
        return driver;
    }

}
