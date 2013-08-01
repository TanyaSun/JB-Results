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
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

/**
 * @author TanyaSun
 */
public class LoginTest {

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
    public void testLogin() {
        String userName = TestUtils.makeUserName();
        TestUtils.verifyLoginBox(browser);
        TestUtils.doLogin(browser, userName);
    }

    @Test
    public void testEmptyLogin() {
        TestUtils.verifyLoginBox(browser);

        WebElement loginBox = browser.findElement(By.className("gwt-TextBox"));
        WebElement loginButton = browser.findElement(By.xpath("//button[@type='button']"));

        String buttonText = loginButton.getText();
        assertEquals("Enter chat", buttonText.trim());

        loginButton.click();

        WebDriverWait wait = new WebDriverWait(browser, 15, 100);
        wait.until(ExpectedConditions.alertIsPresent());

        Alert alert = browser.switchTo().alert();
        String errorText = alert.getText();
        alert.accept();
        assertEquals("Your name cannot be empty", errorText);
    }

}
