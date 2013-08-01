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

import com.google.common.base.Predicate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author TanyaSun
 */

public class CreateChatTest {
    WebDriver browser;

    @Before
    public void beforeTest() {
        browser = SeleniumUtils.getFirefoxBrowser();
    }

    @After
    public void afterTest() {
        //browser.close();
        browser.quit();
    }


    @Test
    public void testCreateChat() {
        String userName = TestUtils.makeUserName();
        TestUtils.verifyLoginBox(browser);
        TestUtils.doLogin(browser, userName);
        WebElement chatNameEdit = browser.findElement(By.xpath("//input[@type='text']"));
        String newChatName = TestUtils.makeChatName();
        chatNameEdit.sendKeys(newChatName);

        WebElement createButton = browser.findElement(By.xpath("//button[@type='button']"));
        createButton.click();

        browser.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        //By chatNameCondition = By.xpath("//a[contains(text(), '"+newChatName+"')]");
        //WebElement chatLink = browser.findElement(chatNameCondition);
        //chatLink.click();

        WebElement captionDiv = browser.findElement(By.xpath(TestUtils.CAPTION_DIV_PATH));
        String captionText = captionDiv.getText();

        assertTrue(captionText.contains(userName));
        assertTrue(captionText.contains("you are in"));
        assertTrue(captionText.contains(newChatName));
        assertTrue(captionText.contains("chat room"));

        WebElement chatTable = browser.findElement(By.xpath(TestUtils.CHAT_TABLE_XPATH));

        By greetingCondition = By.xpath("//div[contains(text(), 'Hello! You are in a chat!')]");

        FluentWait<By> messageWait = new FluentWait<By>(greetingCondition);
        messageWait.pollingEvery(100, TimeUnit.MILLISECONDS);
        messageWait.withTimeout(1000, TimeUnit.MILLISECONDS);
        messageWait.until(new Predicate<By>() {
            public boolean apply(By by) {
                try {
                    return browser.findElement(by).isDisplayed();
                } catch (NoSuchElementException ex) {
                    return false;
                }
            }
        });
        WebElement adminGreeting = browser.findElement(greetingCondition);
        String greetingText = adminGreeting.getText();
        assertEquals("Hello! You are in a chat!", greetingText);

    }

    @Test
    public void testCreateEmptyChatName() {
        String userName = TestUtils.makeUserName();

        TestUtils.verifyLoginBox(browser);
        TestUtils.doLogin(browser, userName);
        WebElement chatNameEdit = browser.findElement(By.xpath("//input[@type='text']"));

        WebElement createButton = browser.findElement(By.xpath("//button[@type='button']"));
        createButton.click();

        WebDriverWait wait = new WebDriverWait(browser, 15, 100);
        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = browser.switchTo().alert();
        String errorText = alert.getText();
        alert.accept();
        assertEquals("Chat name cannot be empty", errorText);

    }

    @Test
    public void testCreateDuplicateChatName() {
        String userName = TestUtils.makeUserName();
        String newChatName = TestUtils.makeChatName();

        TestUtils.verifyLoginBox(browser);
        TestUtils.doLogin(browser, userName);

        WebElement chatNameEdit = browser.findElement(By.xpath("//input[@type='text']"));
        chatNameEdit.sendKeys(newChatName);
        WebElement createButton = browser.findElement(By.xpath("//button[@type='button']"));
        createButton.click();

        WebElement captionDiv = browser.findElement(By.xpath(TestUtils.CAPTION_DIV_PATH));
        String captionText = captionDiv.getText();

        assertTrue(captionText.contains(userName));
        assertTrue(captionText.contains("you are in"));
        assertTrue(captionText.contains(newChatName));
        assertTrue(captionText.contains("chat room"));

        browser.navigate().refresh();

        TestUtils.verifyLoginBox(browser);
        TestUtils.doLogin(browser, userName);
        WebElement chatNameEdit2 = browser.findElement(By.xpath("//input[@type='text']"));
        chatNameEdit2.sendKeys(newChatName);
        WebElement createButton2 = browser.findElement(By.xpath("//button[@type='button']"));
        createButton2.click();

        WebDriverWait wait = new WebDriverWait(browser, 15, 100);
        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = browser.switchTo().alert();
        String errorText = alert.getText();
        alert.accept();
        assertEquals("Duplicate chat name", errorText);

    }
}
