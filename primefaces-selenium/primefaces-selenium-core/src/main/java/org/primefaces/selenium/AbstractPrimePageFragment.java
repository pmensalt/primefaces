/*
 * The MIT License
 *
 * Copyright (c) 2009-2021 PrimeTek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.primefaces.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractPrimePageFragment implements WebElement, WrapsElement {

    private ElementLocator elementLocator;
    private WebDriver webDriver;

    public ElementLocator getElementLocator() {
        return elementLocator;
    }

    public void setElementLocator(ElementLocator elementLocator) {
        this.elementLocator = elementLocator;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    @Override
    public WebElement getWrappedElement() {
        return getElementLocator().findElement();
    }

    public WebElement getRoot() {
        return elementLocator.findElement();
    }

    public String getId() {
        return getRoot().getAttribute("id");
    }

    /**
     * Is the Element present on the page?
     *
     * @param by the selector
     * @return true if present
     */
    public boolean isElementPresent(By by) {
        return PrimeSelenium.isElementPresent(webDriver, by);
    }

    /**
     * Is the Element present on the page?
     *
     * @param element the WebElement to check
     * @return true if present
     */
    public boolean isElementPresent(WebElement element) {
        return PrimeSelenium.isElementPresent(element);
    }

    /**
     * Is the Element displayed on the page?
     *
     * @param by the selector
     * @return true if displayed
     */
    public boolean isElementDisplayed(By by) {
        return PrimeSelenium.isElementDisplayed(webDriver, by);
    }

    /**
     * Is the Element displayed on the page?
     *
     * @param element the WebElement to check
     * @return true if displayed
     */    public boolean isElementDisplayed(WebElement element) {
        return PrimeSelenium.isElementDisplayed(element);
    }

    /**
     * Is the Element enabled on the page?
     *
     * @param by the selector
     * @return true if enabled
     */
    public boolean isElementEnabled(By by) {
        return PrimeSelenium.isElementEnabled(webDriver, by);
    }

    /**
     * Is the Element enabled on the page?
     *
     * @param element the WebElement to check
     * @return true if enabled
     */
    public boolean isElementEnabled(WebElement element) {
        return PrimeSelenium.isElementEnabled(element);
    }

    /**
     * Is this element clickable?
     *
     * @param element the WebElement to check for clickable
     * @return true if clickable false if not
     */
    public boolean isElementClickable(WebElement element) {
        return PrimeSelenium.isElementClickable(element);
    }

    /**
     * Guard the HTTP request which means wait until it has completed before returning.
     *
     * @param target the target to guard
     * @param <T> the type
     * @return the type
     */
    public <T> T guardHttp(T target) {
        return PrimeSelenium.guardHttp(webDriver, target);
    }

    /**
     * Guard the AJAX request which means wait until it has completed before returning.
     *
     * @param target the element to guard
     * @param <T> the type of element
     * @return the element
     */
    public <T> T guardAjax(T target) {
        return PrimeSelenium.guardAjax(webDriver, target);
    }

    /**
     * Guard the AJAX request which means wait until it has completed before returning. This introduces a delay because some client side activity uses
     * "setTimeout" Javascript to delay the execution of AJAX.
     *
     * @param target the element to guard
     * @param delayInMilliseconds how long to delay before expecting an AJAX event
     * @param <T> the element type
     * @return the element
     */
    public <T> T guardAjax(T target, int delayInMilliseconds) {
        return PrimeSelenium.guardAjax(webDriver, target, delayInMilliseconds);
    }

    /**
     * Guard the widget script which fires an AJAX request and means wait until it has completed before returning.
     *
     * @param script the script to execute
     * @param args any arguments to the script
     * @param <T> the return type
     * @return the result of running the JavaScript
     */
    public <T> T guardAjax(String script, Object... args) {
        return PrimeSelenium.guardAjax(webDriver, script, args);
    }

    /**
     * Executes JavaScript in the browser.
     *
     * @param script the script to execute
     * @param args any arguments to the script
     * @param <T> the return type
     * @return the result of running the JavaScript
     */
    public <T> T executeScript(String script, Object... args) {
        return PrimeSelenium.executeScript(webDriver, script, args);
    }

    /**
     * Executes JavaScript in the browser and will wait if the request is an AJAX request.
     *
     * @param isAjaxified true if this is an AJAX request, false if regular script
     * @param script the script to execute
     * @param args any arguments to the script
     * @param <T> the return type
     * @return the result of running the JavaScript
     */
    public <T> T executeScript(boolean isAjaxified, String script, Object... args) {
        return PrimeSelenium.executeScript(webDriver, isAjaxified, script, args);
    }

    /**
     * Sets a value to a hidden input.
     *
     * @param input the WebElement input to set
     * @param value the value to set
     * @see <a href="https://stackoverflow.com/questions/11858366/how-to-type-some-text-in-hidden-field-in-selenium-webdriver-using-java">Stack Overflow</a>
     */
    public void setHiddenInput(WebElement input, String value) {
        PrimeSelenium.setHiddenInput(webDriver, input, value);
    }

    /**
     * Clears the input field of text.
     *
     * @param input the WebElement input to set
     * @param isAjaxified true if using AJAX
     * @see <a href="https://stackoverflow.com/a/64067604/502366">Safari Hack</a>
     */
    public void clearInput(WebElement input, boolean isAjaxified) {
        PrimeSelenium.clearInput(webDriver, input, isAjaxified);
    }

    /**
     * Wait will ignore instances of NotFoundException that are encountered (thrown) by default in the 'until' condition, and immediately propagate all others.
     * You can add more to the ignore list by calling ignoring(exceptions to add).
     *
     * @return the {@link WebDriverWait}
     */
    public WebDriverWait waitGui() {
        return PrimeSelenium.waitGui(webDriver);
    }
}
