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

import org.openqa.selenium.*;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.primefaces.selenium.internal.ConfigProvider;
import org.primefaces.selenium.internal.Guard;
import org.primefaces.selenium.spi.PrimePageFactory;
import org.primefaces.selenium.spi.PrimePageFragmentFactory;
import org.primefaces.selenium.spi.DeploymentAdapter;

public final class PrimeSelenium {

    private static final String HEADLESS_MODE_SYSPROP_NAME = "webdriver.headless";

    private static final String HEADLESS_MODE_SYSPROP_VAL_DEFAULT = "false";

    private PrimeSelenium() {
        super();
    }


    /**
     * Creates the PrimeFaces Selenium component for the selector.
     *
     * @param driver the driver to use
     * @param fragmentClass the component class to create like InputText.class
     * @param by the selector to find the component by
     * @param <T> the type of component returned
     * @return the component
     */
    public static <T extends WebElement> T createFragment(WebDriver driver, Class<T> fragmentClass, By by) {
        WebElement element = driver.findElement(by);
        return createFragment(driver, fragmentClass, element);
    }

    /**
     * Creates the PrimeFaces Selenium component for the element.
     *
     * @param driver the driver to use
     * @param fragmentClass the component class to create like InputText.class
     * @param element the WebElement to bind this component class to
     * @param <T> the type of component returned
     * @return the component
     */
    public static <T extends WebElement> T createFragment(WebDriver driver, Class<T> fragmentClass, WebElement element) {
        return PrimePageFragmentFactory.create(driver, fragmentClass, element);
    }

    /**
     * Goto a particular page.
     *
     * @param driver the driver to use
     * @param pageClass the Page class to go to
     * @param <T> the {@link AbstractPrimePage} type
     * @return the {@link AbstractPrimePage} page created
     */
    public static <T extends AbstractPrimePage> T goTo(WebDriver driver, Class<T> pageClass) {
        T page = PrimePageFactory.create(pageClass, driver);
        driver.get(getUrl(page));

        return page;
    }

    /**
     * Goto a particular page.
     *
     * @param driver the driver to use
     * @param page the {@link AbstractPrimePage} to go to
     */
    public static void goTo(AbstractPrimePage page) {
        WebDriver driver = page.getWebDriver();
        driver.get(getUrl(page));
        if (page.isSafari()) {
            /*
             * Safari has sometimes weird timing issues. (At least on Github Actions.) So wait a bit.
             */
            page.wait(500);
        }
    }

    /**
     * Gets the URL of the page.
     *
     * @param page the {@link AbstractPrimePage}
     * @return the URL of the page
     */
    public static String getUrl(AbstractPrimePage page) {
        DeploymentAdapter deploymentAdapter = ConfigProvider.getInstance().getDeploymentAdapter();

        String baseLocation = page.getBaseLocation();
        if (deploymentAdapter != null) {
            baseLocation = getBaseUrl();
        }

        return baseLocation + page.getLocation();
    }

    /**
     * Gets the URL of the page.
     *
     * @param url the URL to construct
     * @return the full URL
     */
    public static String getUrl(String url) {
        return getBaseUrl() + url;
    }

    public static String getBaseUrl() {
        DeploymentAdapter deploymentAdapter = ConfigProvider.getInstance().getDeploymentAdapter();
        if (deploymentAdapter != null) {
            return deploymentAdapter.getBaseUrl();
        }
        return ConfigProvider.getInstance().getDeploymentBaseUrl();
    }

    /**
     * Checks a WebElement if it has a CSS class or classes. If more than one is listed then ALL must be found on the element.
     *
     * @param element the element to check
     * @param cssClass the CSS class or classes to look for
     * @return true if this element has the CSS class
     */
    public static boolean hasCssClass(WebElement element, String... cssClass) {
        String elementClass = element.getAttribute("class");
        if (elementClass == null) {
            return false;
        }

        String[] elementClasses = elementClass.split(" ");

        boolean result = true;
        for (String expected : cssClass) {
            boolean found = false;
            for (String actual : elementClasses) {
                if (actual.equalsIgnoreCase(expected)) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                result = false;
                break;
            }
        }

        return result;
    }

    /**
     * Is the Element present on the page?
     *
     * @param driver the driver to use
     * @param by the selector
     * @return true if present
     */
    public static boolean isElementPresent(WebDriver driver, By by) {
        try {
            driver.findElement(by);
            return true;
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Is the Element present on the page?
     *
     * @param element the WebElement to check
     * @return true if present
     */
    public static boolean isElementPresent(WebElement element) {
        try {
            element.isDisplayed(); // just any method to check if NoSuchElementException will be thrown
            return true;
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Is the Element displayed on the page?
     *
     * @param driver the driver to use
     * @param by the selector
     * @return true if displayed
     */
    public static boolean isElementDisplayed(WebDriver driver, By by) {
        try {
            return driver.findElement(by).isDisplayed();
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Is the Element displayed on the page?
     *
     * @param element the WebElement to check
     * @return true if displayed
     */
    public static boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Is the Element enabled on the page?
     *
     * @param driver the driver to use
     * @param by the selector
     * @return true if enabled
     */
    public static boolean isElementEnabled(WebDriver driver, By by) {
        try {
            return driver.findElement(by).isEnabled();
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Is the Element enabled on the page?
     *
     * @param element the WebElement to check
     * @return true if enabled
     */
    public static boolean isElementEnabled(WebElement element) {
        try {
            return element.isEnabled() && !hasCssClass(element, "ui-state-disabled");
        }
        catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Is this element clickable?
     *
     * @param element the WebElement to check for clickable
     * @return true if clickable false if not
     */
    public static boolean isElementClickable(WebElement element) {
        return isElementDisplayed(element) && isElementEnabled(element) && !hasCssClass(element, "ui-state-disabled");
    }

    /**
     * Guard the HTTP request which means wait until it has completed before returning.
     *
     * @param driver the driver to use
     * @param target the target to guard
     * @param <T> the type
     * @return the type
     */
    public static <T> T guardHttp(WebDriver driver, T target) {
        return Guard.http(driver, target);
    }

    /**
     * Guard the AJAX request which means wait until it has completed before returning.
     *
     * @param driver the driver to use
     * @param driver the driver to use
     * @param target the element to guard
     * @param <T> the type of element
     * @return the element
     */
    public static <T> T guardAjax(WebDriver driver, T target) {
        return Guard.ajax(driver, target);
    }

    /**
     * Guard the AJAX request which means wait until it has completed before returning. This introduces a delay because some client side activity uses
     * "setTimeout" Javascript to delay the execution of AJAX.
     *
     * @param driver the driver to use
     * @param target the element to guard
     * @param delayInMilliseconds how long to delay before expecting an AJAX event
     * @param <T> the element type
     * @return the element
     */
    public static <T> T guardAjax(WebDriver driver, T target, int delayInMilliseconds) {
        return Guard.ajax(driver, target, delayInMilliseconds);
    }

    /**
     * Guard the widget script which fires an AJAX request and means wait until it has completed before returning.
     *
     * @param driver the driver to use
     * @param script the script to execute
     * @param args any arguments to the script
     * @param <T> the return type
     * @return the result of running the JavaScript
     */
    public static <T> T guardAjax(WebDriver driver, String script, Object... args) {
        return Guard.ajax(driver, script, args);
    }

    /**
     * Executes JavaScript in the browser.
     *
     * @param driver the driver to use
     * @param script the script to execute
     * @param args any arguments to the script
     * @param <T> the return type
     * @return the result of running the JavaScript
     */
    public static <T> T executeScript(WebDriver driver, String script, Object... args) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        T t = (T) executor.executeScript(script, args);
        if (isSafari(driver)) {
            /*
             * Safari has sometimes weird timing issues. (At least on Github Actions.) So wait a bit.
             */
            wait(50);
        }
        return t;
    }

    /**
     * Executes JavaScript in the browser and will wait if the request is an AJAX request.
     *
     * @param driver the driver to use
     * @param isAjaxified true if this is an AJAX request, false if regular script
     * @param script the script to execute
     * @param args any arguments to the script
     * @param <T> the return type
     * @return the result of running the JavaScript
     */
    public static <T> T executeScript(WebDriver driver, boolean isAjaxified, String script, Object... args) {
        if (isAjaxified) {
            return guardAjax(driver, script, args);
        }
        else {
            return executeScript(driver, script, args);
        }
    }

    /**
     * Wait will ignore instances of NotFoundException that are encountered (thrown) by default in the 'until' condition, and immediately propagate all others.
     * You can add more to the ignore list by calling ignoring(exceptions to add).
     *
     * @param driver the driver to use
     * @return the {@link WebDriverWait}
     */
    public static WebDriverWait waitGui(WebDriver driver) {
        ConfigProvider config = ConfigProvider.getInstance();
        WebDriverWait wait = new WebDriverWait(driver, config.getTimeoutGui(), 100);
        return wait;
    }

    /**
     * Wait until the document is loaded.
     *
     * @param driver the driver to use
     * @return the {@link WebDriverWait}
     */
    public static WebDriverWait waitDocumentLoad(WebDriver driver) {
        ConfigProvider config = ConfigProvider.getInstance();

        WebDriverWait wait = new WebDriverWait(driver, config.getTimeoutDocumentLoad(), 100);
        wait.until(PrimeExpectedConditions.documentLoaded());

        return wait;
    }

    /**
     * @param driver the driver to use
     * Globally disable all CSS and jQuery animations.
     */
    public static void disableAnimations(WebDriver driver) {
        executeScript(driver, "if (window.PrimeFaces) { $(function() { PrimeFaces.utils.disableAnimations(); }); }");
    }

    /**
     * @param driver the driver to use
     * Globally enable all CSS and jQuery animations.
     */
    public static void enableAnimations(WebDriver driver) {
        executeScript(driver, "if (window.PrimeFaces) { $(function() { PrimeFaces.utils.enableAnimations(); }); }");
    }

    /**
     * Sets a value to a hidden input.
     *
     * @param driver the driver to use
     * @param input the WebElement input to set
     * @param value the value to set
     * @see <a href="https://stackoverflow.com/questions/11858366/how-to-type-some-text-in-hidden-field-in-selenium-webdriver-using-java">Stack Overflow</a>
     */
    public static void setHiddenInput(WebDriver driver, WebElement input, String value) {
        executeScript(driver, " document.getElementById('" + input.getAttribute("id") + "').value='" + value + "'");
    }

    /**
     * Clears the browser console.
     * @param driver the webdrive to use
     */
    public static void clearConsole(WebDriver driver) {
        // https://stackoverflow.com/questions/51404360/how-to-clear-console-errors-using-selenium
        PrimeSelenium.executeScript(driver, "console.clear();");
    }

    /**
     * Clears the input field of text.
     *
     * @param driver the driver to use
     * @param input the WebElement input to set
     * @param isAjaxified true if using AJAX
     * @see <a href="https://stackoverflow.com/a/64067604/502366">Safari Hack</a>
     */
    public static void clearInput(WebDriver driver, WebElement input, boolean isAjaxified) {
        if (isSafari(driver)) {
            // Safari hack https://stackoverflow.com/a/64067604/502366
            String inputText = input.getAttribute("value");
            if (inputText != null && inputText.length() > 0) {
                CharSequence[] clearText = new CharSequence[inputText.length()];
                for (int i = 0; i < inputText.length(); i++) {
                    clearText[i] = Keys.BACK_SPACE;
                }
                if (isAjaxified) {
                    guardAjax(driver, input).sendKeys(clearText);
                }
                else {
                    input.sendKeys(clearText);
                }
            }
        }
        else {
            // CTRL+A then BACKSPACE
            Keys command = PrimeSelenium.isMacOs() ? Keys.COMMAND : Keys.CONTROL;
            input.sendKeys(Keys.chord(command, "a"));
            if (isAjaxified) {
                guardAjax(driver, input).sendKeys(Keys.BACK_SPACE);
            }
            else {
                input.sendKeys(Keys.BACK_SPACE);
            }
        }
    }

    /**
     * Is the current WebDriver a Chrome driver?
     *
     * @param driver the driver to use
     * @return true if Chrome, false if any other browser
     */
    public static boolean isChrome(WebDriver driver) {
        Capabilities cap = ((EventFiringWebDriver) driver).getCapabilities();
        return "Chrome".equalsIgnoreCase(cap.getBrowserName());
    }

    /**
     * Is the current WebDriver a Firefox driver?
     *
     * @param driver the driver to use
     * @return true if Firefox, false if any other browser
     */
    public static boolean isFirefox(WebDriver driver) {
        Capabilities cap = ((EventFiringWebDriver) driver).getCapabilities();
        return "Firefox".equalsIgnoreCase(cap.getBrowserName());
    }

    /**
     * Is the current WebDriver a Safari driver?
     *
     * @param driver the driver to use
     * @return true if Safari, false if any other browser
     */
    public static boolean isSafari(WebDriver driver) {
        Capabilities cap = ((EventFiringWebDriver) driver).getCapabilities();
        return "Safari".equalsIgnoreCase(cap.getBrowserName());
    }

    /**
     * Are we running on MacOS?
     *
     * @return true if MacOS
     */
    public static boolean isMacOs() {
        String os = System.getProperty("os.name").toUpperCase();
        return (os.contains("DARWIN")) || (os.contains("MAC"));
    }

    /**
     * Is this driver running headless? Meaning without a UI.
     *
     * @return true if headless, false if not
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(System.getProperty(HEADLESS_MODE_SYSPROP_NAME, HEADLESS_MODE_SYSPROP_VAL_DEFAULT));
    }

    /**
     * Waits specified amount of milliseconds.
     *
     * @param milliseconds how many milliseconds to wait
     */
    public static void wait(int milliseconds) {
        if (milliseconds > 0) {
            try {
                Thread.sleep(milliseconds);
            }
            catch (InterruptedException ex) {
                System.err.println("Wait was interrupted!");
                // Restore interrupted state...
                Thread.currentThread().interrupt();
            }
        }
    }
}
