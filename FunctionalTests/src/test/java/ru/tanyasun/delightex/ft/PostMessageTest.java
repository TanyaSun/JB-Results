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


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

/**
 * @author TanyaSun
 */
public class PostMessageTest {
    WebDriver browser;
    WebDriver browser2;

    @Before
    public void beforeTest() {
        browser = SeleniumUtils.getFirefoxBrowser();
    }

    @After
    public void afterTest() {
        //browser.close();
        browser.quit();
        if (browser2 != null) {
            browser2.quit();
            browser2 = null;
        }
    }


    @Test
    public void sendMessageSingleCase() {
        //test sending message in single browser window
        String userName = TestUtils.makeUserName();
        TestUtils.verifyLoginBox(browser);
        TestUtils.doLogin(browser, userName);
        TestUtils.goToChat(browser, TestUtils.MESSAGE_CHAT_NAME);
        String msg = TestUtils.makeRandomMessage("PRIVET!!!");
        TestUtils.sendChatMessage(browser, msg);
        TestUtils.checkMessage(browser, msg, userName);
    }

    @Test
    public void sendEmptyMessage() {
        //test sending empty message
        String userName = TestUtils.makeUserName();
        TestUtils.verifyLoginBox(browser);
        TestUtils.doLogin(browser, userName);
        TestUtils.goToChat(browser, TestUtils.MESSAGE_CHAT_NAME);
        String msg = "";
        TestUtils.sendChatMessage(browser, msg);
        TestUtils.checkMessage(browser, msg, userName);
    }

    @Test
    public void sendMessageTwoBrowserTest() {
        //test sending message in first window and check it in second
        String userName = TestUtils.makeUserName();
        TestUtils.verifyLoginBox(browser);
        TestUtils.doLogin(browser, userName);
        TestUtils.goToChat(browser, TestUtils.MESSAGE_CHAT_NAME);

        browser2 = SeleniumUtils.getFirefoxBrowser();
        String userName2 = TestUtils.makeUserName();
        TestUtils.verifyLoginBox(browser2);
        TestUtils.doLogin(browser2, userName2);
        TestUtils.goToChat(browser2, TestUtils.MESSAGE_CHAT_NAME);

        String msg = TestUtils.makeRandomMessage("PING-PONG!!!");

        TestUtils.sendChatMessage(browser, msg);

        browser.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        TestUtils.checkMessage(browser2, msg, userName);

    }
}
