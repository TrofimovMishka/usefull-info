package org.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FirstScenario {
    public static final String IMPORT_URL = "/home/trofimov/Projects/Java/rbi/e2e/src/main/resources/sc1/Wsadowy scenario-1.xlsx";
    public static final String CONTRACT_NUMBER = "MG3830421111A126a";
    public static final String APP_URL = "http://localhost:4200/";
    public static final String SENIOR_OPERACJE = "john_senior_std_user_operacje";
    public static final String SUPERUSER_RBI = "john_superuser_rbi";
    public static final int TIMEOUT_15_SEC = 15000;
    public static final int TIMEOUT_30_SEC = 30000;
    public static final int TIMEOUT_60_SEC = 60000;
    public static final int TIMEOUT_120_SEC = 120000;
    public static final int SECONDS_10 = 10;

    public static void run(WebDriver driver) throws InterruptedException {
        firstLogin(driver, SUPERUSER_RBI, APP_URL);
        importCase(driver, IMPORT_URL);
        filterCase(driver, CONTRACT_NUMBER);
        logout(driver);
    }

    public static void firstLogin(WebDriver driver, String login, String url) throws InterruptedException {
        System.out.println("SC1 - First login test run");
        driver.get(url);

        String acceptButton = "//button[.//span[contains(text(), 'AKCEPTUJĘ')]]";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(acceptButton))).click();

        driver.findElement(By.id("username")).sendKeys(login);
        driver.findElement(By.id("password")).sendKeys("test");
        driver.findElement(By.id("kc-login")).click();

        wait.withTimeout(Duration.ofSeconds(SECONDS_10))
                .until(ExpectedConditions.urlContains("/app/cases"));

        System.out.println("SC1 - First login test end");
    }

    public static void importCase(WebDriver driver, String fileUrl) {
        System.out.println("SC1 - Import case test run");

        By importButtonLocator = By.xpath("//button[contains(@class, 'ey-mediator-case-list__header-button') and contains(text(), 'Importuj excel')]");
        By fileInputLocator = By.xpath(".//div[contains(@class, 'ey-mediator-case-list__header')]//input[@type='file']");
        By modalImportButtonLocator = By.xpath("//button[contains(@class, 'ey-mediator-excel-import-preview-dialog__actions-import-button')]");
        By closeButtonLocator = By.xpath("//div/simple-snack-bar/div[2]/button");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));
        FluentWait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(90))
                .pollingEvery(Duration.ofMillis(500));

        wait.until(ExpectedConditions.presenceOfElementLocated(importButtonLocator));
        wait.until(ExpectedConditions.presenceOfElementLocated(fileInputLocator)).sendKeys(fileUrl);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        WebElement modalImportButton = fluentWait.until(ExpectedConditions.elementToBeClickable(modalImportButtonLocator));
        js.executeScript("arguments[0].scrollIntoView(true);", modalImportButton);
        js.executeScript("arguments[0].click();", modalImportButton);

        wait.until(ExpectedConditions.elementToBeClickable(closeButtonLocator)).click();

        System.out.println("SC1 - Import case test end");
    }

    public static void filterCase(WebDriver driver, String contractNumber) {
        System.out.println("SC1 - Filter case test run");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));

        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.ey-mediator-case-list__header-button--icon"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), '+ Numer kontraktu')]"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("numerKontraktu"))).click();
        driver.findElement(By.xpath("//button[contains(text(), 'Zapisz')]")).click();

        System.out.println("SC1 - Filter case test end");
    }

    public static void logout(WebDriver driver) {
        System.out.println("SC1 - Logout test run");

        driver.findElement(By.cssSelector("ey-mediator-header span.mat-mdc-button-touch-target")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[.//span[contains(text(), 'Wyloguj się')]]"))).click();

        System.out.println("SC1 - Logout test end");
    }

}
