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
package org.primefaces.integrationtests.datatable;

import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.primefaces.integrationtests.datatable.DataTable027Test.Page;
import org.primefaces.selenium.PrimeSelenium;
import org.primefaces.selenium.component.CommandButton;
import org.primefaces.selenium.component.DataTable;

public class DataTable028Test extends AbstractDataTableTest {

    @ParameterizedTest
    @MethodSource("provideXhtmls")
    @Order(1)
    @DisplayName("DataTable: filter + sort + edit with own inputs - wrong manipulation of list elements - https://github.com/primefaces/primefaces/issues/7999")
    public void testFilterSortEdit(Page page, String xhtml) {
        // Arrange
        page.getWebDriver().get(PrimeSelenium.getUrl(xhtml));
        getButtonSave(page.getWebDriver()).click();

        // Assert
        assertInitalState(page.getWebDriver());

        // Act 1 - filter on name with value BB2
        getDataTable(page.getWebDriver()).filter("Name", "bb2");

        // Act 2 - change all BB2 row values to BB3, press Save
        DataTable dataTable = getDataTable(page.getWebDriver());
        for (int row=0; row<=2; row++) {
            WebElement eltName = dataTable.getRow(row).getCell(3).getWebElement().findElement(By.tagName("input"));
            eltName.clear();
            eltName.sendKeys("BB3");
        }
        getButtonSave(page.getWebDriver()).click();

        // Assert
        assertAfterBb3Update(page.getWebDriver());

        // Act 3 - remove filter BB2, press Save
        getDataTable(page.getWebDriver()).filter("Name", "");
        getButtonSave(page.getWebDriver()).click();

        // Assert
        assertAfterBb3Update(page.getWebDriver());

        // Act 4 - sort on code, press Save
        getDataTable(page.getWebDriver()).sort("Code");
        getButtonSave(page.getWebDriver()).click();

        // Assert
        if (xhtml.contains("Without")) {
            assertAfterBb3UpdateSorted(page.getWebDriver());
        }
        else {
            assertAfterBb3Update(page.getWebDriver());
        }

        assertNoJavascriptErrors(page.getWebDriver());
    }

    @ParameterizedTest
    @MethodSource("provideXhtmls")
    @Order(2)
    @DisplayName("DataTable: filter + sort + edit with own inputs - V2")
    public void testFilterSortEditV2(Page page, String xhtml) {
        // Arrange
        page.getWebDriver().get(PrimeSelenium.getUrl(xhtml));
        getButtonSave(page.getWebDriver()).click();

        // Assert
        assertInitalState(page.getWebDriver());

        // Act 1 - sort on code, press Save
        getDataTable(page.getWebDriver()).sort("Code");
        getButtonSave(page.getWebDriver()).click();

        // Act 2 - filter on name with value BB2
        getDataTable(page.getWebDriver()).filter("Name", "bb2");

        // Act 3 - change all BB2 row values to BB3, press Save
        DataTable dataTable = getDataTable(page.getWebDriver());
        for (int row=0; row<=2; row++) {
            WebElement eltName = dataTable.getRow(row).getCell(3).getWebElement().findElement(By.tagName("input"));
            eltName.clear();
            eltName.sendKeys("BB3");
        }
        getButtonSave(page.getWebDriver()).click();

        // Assert
        assertAfterBb3UpdateSorted(page.getWebDriver());

        // Act 4 - remove filter BB2, press Save
        getDataTable(page.getWebDriver()).filter("Name", "");
        getButtonSave(page.getWebDriver()).click();

        // Assert
        assertAfterBb3UpdateSorted(page.getWebDriver());

        assertNoJavascriptErrors(page.getWebDriver());
    }

    @ParameterizedTest
    @MethodSource("provideXhtmls")
    @Order(10)
    @DisplayName("DataTable: sort + edit with own inputs")
    public void testSortEdit(Page page, String xhtml) {
        // Arrange
        page.getWebDriver().get(PrimeSelenium.getUrl(xhtml));
        getButtonSave(page.getWebDriver()).click();

        // Assert
        assertInitalState(page.getWebDriver());

        // Act 1 - change all BB2 row values to BB3, press Save
        DataTable dataTable = getDataTable(page.getWebDriver());
        for (int row=0; row<=2; row++) {
            WebElement eltName = dataTable.getRow(row).getCell(3).getWebElement().findElement(By.tagName("input"));
            eltName.clear();
            eltName.sendKeys("BB3");
        }
        getButtonSave(page.getWebDriver()).click();

        // Assert
        assertAfterBb3Update(page.getWebDriver());

        // Act 2 - sort on code, press Save
        getDataTable(page.getWebDriver()).sort("Code");
        getButtonSave(page.getWebDriver()).click();

        // Assert
        assertAfterBb3UpdateSorted(page.getWebDriver());

        assertNoJavascriptErrors(page.getWebDriver());
    }

    @ParameterizedTest
    @MethodSource("provideXhtmls")
    @Order(11)
    @DisplayName("DataTable: sort + edit with own inputs - V2")
    public void testSortEditV2(Page page, String xhtml) {
        // Arrange
        page.getWebDriver().get(PrimeSelenium.getUrl(xhtml));
        getButtonSave(page.getWebDriver()).click();

        // Assert
        assertInitalState(page.getWebDriver());

        // Act 1 - sort on code, press Save
        getDataTable(page.getWebDriver()).sort("Code");
        getButtonSave(page.getWebDriver()).click();

        // Act 2 - change all BB2 row values to BB3, press Save
        DataTable dataTable = getDataTable(page.getWebDriver());
        for (int row=2; row<=4; row++) {
            WebElement eltName = dataTable.getRow(row).getCell(3).getWebElement().findElement(By.tagName("input"));
            eltName.clear();
            eltName.sendKeys("BB3");
        }
        getButtonSave(page.getWebDriver()).click();

        // Assert
        assertAfterBb3UpdateSorted(page.getWebDriver());

        assertNoJavascriptErrors(page.getWebDriver());
    }

    private void assertInitalState(WebDriver webDriver) {
        String expected = StringUtils.deleteWhitespace("Result:\n" +
                "509, EUR, BB, BB2, A\n" +
                "512, EUR, BB, BB2, B\n" +
                "515, EUR, BB, BB2, C\n" +
                "516, USA, AA, AA, D\n" +
                "517, USA, AA, AA, E");
        Assertions.assertEquals(expected, StringUtils.deleteWhitespace(getEltDebugActual(webDriver).getText()));
    }

    private void assertAfterBb3Update(WebDriver webDriver) {
        String expected = StringUtils.deleteWhitespace("Result:\n" +
                    "509, EUR, BB, BB3, A\n" +
                    "512, EUR, BB, BB3, B\n" +
                    "515, EUR, BB, BB3, C\n" +
                    "516, USA, AA, AA, D\n" +
                    "517, USA, AA, AA, E");
        Assertions.assertEquals(expected, StringUtils.deleteWhitespace(getEltDebugActual(webDriver).getText()));
    }

    private void assertAfterBb3UpdateSorted(WebDriver webDriver) {
        String expected = StringUtils.deleteWhitespace("Result:\n" +
                    "516, USA, AA, AA, D\n" +
                    "517, USA, AA, AA, E\n" +
                    "509, EUR, BB, BB3, A\n" +
                    "512, EUR, BB, BB3, B\n" +
                    "515, EUR, BB, BB3, C");
        Assertions.assertEquals(expected, StringUtils.deleteWhitespace(getEltDebugActual(webDriver).getText()));
    }

    private static Stream<Arguments> provideXhtmls() {
        return Stream.of(
                Arguments.of("datatable/dataTable028.xhtml"),
                Arguments.of("datatable/dataTable028WithoutFilteredValue.xhtml"));
    }

    private DataTable getDataTable(WebDriver webDriver) {
        return PrimeSelenium.createFragment(webDriver, DataTable.class, By.id("form:referenceTable"));
    }

    private CommandButton getButtonSave(WebDriver webDriver) {
        return PrimeSelenium.createFragment(webDriver, CommandButton.class, By.id("form:cmdSave"));
    }

    private WebElement getEltDebugInital(WebDriver webDriver) {
        return PrimeSelenium.createFragment(webDriver, WebElement.class, By.id("debugInitial"));
    }

    private WebElement getEltDebugActual(WebDriver webDriver) {
        return PrimeSelenium.createFragment(webDriver, WebElement.class, By.id("debugActual"));
    }

}
