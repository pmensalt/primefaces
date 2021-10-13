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
package org.primefaces.selenium.spi;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.primefaces.selenium.PrimeSelenium;
import org.primefaces.selenium.internal.ConfigProvider;
import org.primefaces.selenium.internal.OnloadScriptsEventListener;
import org.primefaces.selenium.internal.ScrollElementIntoViewClickListener;

public class WebDriverProvider {

    private static final ThreadLocal<List<WebDriver>> WEB_DRIVERS = ThreadLocal.withInitial(ArrayList::new);
    private static final List<WebDriver> WEB_DRIVER_POOL = new ArrayList<>();

    private static final int CREATE_WEBDRIVER_RETRIES = 3;

    private WebDriverProvider() {

    }

    public static void resetWebDrivers() {
        WEB_DRIVERS.get().forEach(webDriver -> webDriver.manage().deleteAllCookies());
        WEB_DRIVERS.get().forEach(PrimeSelenium::clearConsole);
        synchronized (WEB_DRIVER_POOL) {
            WEB_DRIVER_POOL.addAll(WEB_DRIVERS.get());
            WEB_DRIVERS.remove();
        }
    }

    public static void closeAllWebDrivers() {
        WEB_DRIVERS.get().forEach(WebDriver::quit);
        WEB_DRIVERS.remove();
        WEB_DRIVER_POOL.forEach(WebDriver::quit);
        WEB_DRIVER_POOL.clear();
    }
    
    public static WebDriver getWebDriver() {
        WebDriver driver = null;
        synchronized (WEB_DRIVER_POOL) {
            if (!WEB_DRIVER_POOL.isEmpty()) {
                driver = WEB_DRIVER_POOL.remove(0);
            }
        }
        if (driver == null) {
            driver = createNewWebDriver();
        }
        WEB_DRIVERS.get().add(driver);
        return driver;
    }
    
   public static WebDriver getWebDriverInitializationBeforeAll() {
        synchronized (WEB_DRIVER_POOL) {
            if (WEB_DRIVER_POOL.isEmpty()) {
                WEB_DRIVER_POOL.add(createNewWebDriver());
            }
            return WEB_DRIVER_POOL.get(0);
        }
    }
   
   public static WebDriver getWebDriverInitializationBeforeEach() {
       if (WEB_DRIVERS.get().isEmpty()) {
           WebDriver driver = null;
           synchronized (WEB_DRIVER_POOL) {
               if (!WEB_DRIVER_POOL.isEmpty()) {
                   driver = WEB_DRIVER_POOL.remove(0);
               }
           }
           if (driver == null) {
               driver = createNewWebDriver();
           }
           WEB_DRIVERS.get().add(driver);
           return driver;
       } else {
           return WEB_DRIVERS.get().get(0);
       }
   }


    private static WebDriver createNewWebDriver() {
        WebDriver driver = null;
        WebDriverAdapter adapter = ConfigProvider.getInstance().getWebdriverAdapter();
        int fails = 0;

        do {
            /*
             * Avoid issues like 2021-01-18T20:31:34.5805460Z org.openqa.selenium.WebDriverException:
             * 2021-01-18T20:31:34.5810490Z
             * java.net.ConnectException: Failed to connect to localhost/0:0:0:0:0:0:0:1:27231 which sometimes occur
             * during Github Action jobs.
             */
            try {
                driver = adapter.createWebDriver();
            } catch (WebDriverException ex) {
                fails++;
                if (fails >= CREATE_WEBDRIVER_RETRIES) {
                    throw ex;
                }
            }
        } while (driver == null);

        /*
         * Define window-size for headless-mode. Selenium WebDriver-default seems to be 800x600. This causes issues with
         * modern themes (eg Saga) which use
         * more space for some components. (eg DatePicker-popup)
         */
        if (PrimeSelenium.isHeadless()) {
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } else {
            driver.manage().window().setSize(new Dimension(1280, 1000));
        }

        EventFiringWebDriver eventDriver = new EventFiringWebDriver(driver);
        eventDriver.register(new OnloadScriptsEventListener());

        if (ConfigProvider.getInstance().getScrollElementIntoView() != null) {
            eventDriver.register(
                    new ScrollElementIntoViewClickListener(ConfigProvider.getInstance().getScrollElementIntoView()));
        }

        return eventDriver;
    }
}
