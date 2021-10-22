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
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractPrimePage {

    private WebDriver webDriver;

    public String getBaseLocation() {
        return null;
    }

    public abstract String getLocation();

    public void goTo() {
        goTo(this);
    }

    public boolean isAt() {
        return webDriver.getCurrentUrl().contains(getLocation());
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    /**
     * Get WebStorage of WebDriver.
     *
     * @return Returns WebStorage of WebDriver when this feature is supported by the browser. Some browsers like Safari (as of january 2021) do not support
     *         WebStorage via WebDriver. In this case null is returned.
     */
    public WebStorage getWebStorage() {
        WebDriver webDriver = this.getWebDriver();

        if (webDriver instanceof EventFiringWebDriver) {
            EventFiringWebDriver driver = (EventFiringWebDriver) webDriver;
            webDriver = driver.getWrappedDriver();
        }

        if (webDriver instanceof WebStorage) {
            return (WebStorage) webDriver;
        }
        else {
            return null;
        }
    }

    /**
     * Creates the PrimeFaces Selenium component for the selector.
     *
     * @param fragmentClass the component class to create like InputText.class
     * @param by the selector to find the component by
     * @param <T> the type of component returned
     * @return the component
     */
    public <T extends WebElement> T createFragment(Class<T> fragmentClass, By by) {
        return PrimeSelenium.createFragment(webDriver, fragmentClass, by);
    }

    /**
     * Creates the PrimeFaces Selenium component for the element.
     *
     * @param fragmentClass the component class to create like InputText.class
     * @param element the WebElement to bind this component class to
     * @param <T> the type of component returned
     * @return the component
     */
    public <T extends WebElement> T createFragment(Class<T> fragmentClass, WebElement element) {
        return PrimeSelenium.createFragment(webDriver, fragmentClass, element);
    }

    /**
     * Goto a particular page.
     *
     * @param pageClass the Page class to go to
     * @param <T> the {@link AbstractPrimePage} type
     * @return the {@link AbstractPrimePage} page created
     */
    public <T extends AbstractPrimePage> T goTo(Class<T> pageClass) {
        return PrimeSelenium.goTo(webDriver, pageClass);
    }

    public String getUrl() {
        return PrimeSelenium.getUrl(this);
    }

    /**
     * Goto a particular page.
     *
     * @param page the {@link AbstractPrimePage} to go to
     */
    public void goTo(AbstractPrimePage page) {
        PrimeSelenium.goTo(page);
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
     */
    public boolean isElementDisplayed(WebElement element) {
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
        return PrimeSelenium.executeScript(webdriver, isAjaxified, script, args);
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

    /**
     * Wait until the document is loaded.
     *
     * @return the {@link WebDriverWait}
     */
    public WebDriverWait waitDocumentLoad() {
        return PrimeSelenium.waitDocumentLoad(webDriver);
    }

    /**
     * Globally disable all CSS and jQuery animations.
     */
    public void disableAnimations() {
        PrimeSelenium.disableAnimations(webDriver);
    }

    /**
     * Globally enable all CSS and jQuery animations.
     */
    public void enableAnimations() {
        PrimeSelenium.enableAnimations(webDriver);
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
     * Is the current WebDriver a Chrome driver?
     *
     * @return true if Chrome, false if any other browser
     */
    public boolean isChrome() {
        return PrimeSelenium.isChrome(webDriver);
    }

    /**
     * Is the current WebDriver a Firefox driver?
     *
     * @return true if Firefox, false if any other browser
     */
    public boolean isFirefox() {
        return PrimeSelenium.isFirefox(webDriver);
    }

    /**
     * Is the current WebDriver a Safari driver?
     *
     * @return true if Safari, false if any other browser
     */
    public boolean isSafari() {
        return PrimeSelenium.isSafari(webDriver);
    }

    /**
     * Waits specified amount of milliseconds.
     *
     * @param milliseconds how many milliseconds to wait
     */
    public void wait(int milliseconds) {
        PrimeSelenium.wait(milliseconds);
    }
}
