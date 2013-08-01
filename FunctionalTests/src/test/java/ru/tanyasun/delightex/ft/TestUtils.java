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


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestUtils {

    private static int counter = 0;

    public final static String TEST_USER_PREFIX = "TestUser-";

    public final static String MESSAGE_CHAT_NAME = "TestChat";

    public final static String CREATE_CHAT_PREFIX = "CreateChat-";

    public final static String CHAT_TABLE_XPATH = "/html/body/div[2]/div/table/tbody/tr[3]/td/table";

    public final static String CAPTION_DIV_PATH = "/html/body/div[2]/div/table/tbody/tr/td/div";

    public final static String MESSAGE_TABLE_PATH = "/html/body/div[2]/div/table/tbody/tr[3]/td/table";

    public static String makeUserName() {
        return TEST_USER_PREFIX + (counter++) + "-" + System.currentTimeMillis();
    }

    public static String makeChatName() {
        return CREATE_CHAT_PREFIX + (counter++) + "-" + System.currentTimeMillis();
    }


    public static void verifyLoginBox(WebDriver browser) {
        String title = browser.getTitle();
        assertEquals("DelighteX Chat", title);
        WebElement loginTitle = browser.findElement(By.className("Caption"));
        String caption = loginTitle.getText();
        assertEquals("Login", caption);

    }

    public static void doLogin(WebDriver browser, String userName) {
        WebElement loginBox = browser.findElement(By.className("gwt-TextBox"));
        loginBox.sendKeys(userName);
        WebElement loginButton = browser.findElement(By.xpath("//button[@type='button']"));
        String buttonText = loginButton.getText();
        assertEquals("Enter chat", buttonText.trim());
        loginButton.click();
        WebElement label = browser.findElement(By.className("gwt-Label"));
        String labelText = label.getText();
        assertTrue(labelText.contains("Hello"));
        assertTrue(labelText.contains(userName));
    }


    public static void goToChat(WebDriver browser, String chatName) {
        By noChatsCondition = By.xpath("//div[contains(text(), 'There is no chat yet, create new one!')]");

        if (browser.findElements(noChatsCondition).size() != 0) {
            WebElement chatNameEdit = browser.findElement(By.xpath("//input[@type='text']"));
            chatNameEdit.sendKeys(chatName);
            WebElement createButton = browser.findElement(By.xpath("//button[@type='button']"));
            createButton.click();
        } else {
            By chatSearchCondition = By.xpath("//a[contains(text(), '" + chatName + "')]");
            WebElement chatLink = browser.findElement(chatSearchCondition);
            chatLink.click();
        }
    }

    public static Random random = new Random(44448888);

    public static String makeRandomMessage(String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append("  ");
        sb.append(System.currentTimeMillis());
        sb.append("  ");
        sb.append(random.nextInt());
        sb.append("  ");
        sb.append(random.nextDouble());
        sb.append("  ");
        sb.append(System.nanoTime());
        return sb.toString();
    }

    public static void sendChatMessage(WebDriver browser, String messageText) {
        WebElement chatMessageEdit = browser.findElement(By.xpath("//input[@type='text']"));
        chatMessageEdit.sendKeys(messageText);
        WebElement sendMessageButton = browser.findElement(By.xpath("//button[@type='button']"));
        sendMessageButton.click();
    }

    public static void checkMessage(WebDriver browser, String messageText, String userName) {
        WebElement messageTable = browser.findElement(By.xpath(MESSAGE_TABLE_PATH));

        List<WebElement> messages = messageTable.findElements(By.className("chat-message"));

        By messageCondition = By.xpath("//div[contains(text(), '" + messageText + "')]");
        By userCondition = By.xpath("//div[contains(text(), '" + userName + "')]");

        for (WebElement msg : messages) {
            //find div with required message
            try {
                WebElement messageBody = msg.findElement(By.className("chat-message-text"));
                WebElement message = messageBody.findElement(messageCondition);
            } catch (NoSuchElementException ex) {
                //just ignore - next message element
            }
            //check user
            WebElement userBody = msg.findElement(By.className("chat-message-sender"));
            WebElement user = userBody.findElement(userCondition);
        }
    }


}
