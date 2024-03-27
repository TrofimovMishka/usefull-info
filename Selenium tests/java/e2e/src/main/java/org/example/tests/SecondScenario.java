package org.example.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SecondScenario {
    public static final String IMPORT_URL = "/home/trofimov/Projects/Java/rbi/e2e/src/main/resources/sc2/Wsadowy scenario-2.xlsx";
    public static final String ONE_PAGER_URL = "/home/trofimov/Projects/Java/rbi/e2e/src/main/resources/sc2/TestAttachment.pdf";
    public static final String CONTRACT_NUMBER = "MG3830421111A126b";
    public static final String APP_URL = "http://localhost:4200/";
    public static final String SENIOR_ANALITYK = "john_senior_std_user_analityk_kredytowy";
    public static final String JUNIOR_ANALITYK = "john_junior_std_user_analityk_kredytowy";
    public static final String SENIOR_CONTACT = "john_senior_std_user_contact_center";
    public static final String JUNIOR_CONTACT = "john_junior_std_user_contact_center";
    public static final String SENIOR_OPERACJE = "john_senior_std_user_operacje";
    public static final String JUNIOR_OPERACJE = "john_junior_std_user_operacje";
    public static final String SUPERUSER_RBI = "john_superuser_rbi";
    public static final int TIMEOUT_15_SEC = 15000;
    public static final int TIMEOUT_30_SEC = 30000;
    public static final int TIMEOUT_60_SEC = 60000;
    public static final int TIMEOUT_120_SEC = 120000;
    public static final int SECONDS_10 = 10;
    public static int NUMBER_OF_TESTS = 0;

    public static void run(WebDriver driver) {
        firstLogin(driver, SUPERUSER_RBI, APP_URL);
        importCase(driver, IMPORT_URL);
        filterCase(driver, CONTRACT_NUMBER);
        logout(driver);

        login(driver, SENIOR_OPERACJE);
        filterCase(driver, CONTRACT_NUMBER);
        selectCase(driver);
        assignSeniorOperacje(driver);
        markCheckBoxIfNeedIt(driver);
        fillHouseNumberForEachClient(driver);
        assignJuniorOperacje(driver);
        logout(driver);

        login(driver, JUNIOR_OPERACJE);
        filterCase(driver, CONTRACT_NUMBER);
        selectCase(driver);
        pushCaseStepWAnalizie(driver);
        logout(driver);

        login(driver, SENIOR_ANALITYK);
        filterCase(driver, CONTRACT_NUMBER);
        selectCase(driver);
        assignJuniorAnalityk(driver);
        logout(driver);

        login(driver, JUNIOR_ANALITYK);
        filterCase(driver, CONTRACT_NUMBER);
        selectCase(driver);
        pushCaseAdnGetError(driver);
        addPositiveDecision(driver);
        pushCaseStepPoDecyzji(driver);
        logout(driver);

        login(driver, SENIOR_OPERACJE);
        filterCase(driver, CONTRACT_NUMBER);
        selectCase(driver);
        assignSeniorOperacje(driver);
        addOnePagerToCase(driver);
        fillOnePagerDateSending(driver);
        pushCaseStepDoKontaktu(driver);
        logout(driver);

        login(driver, SENIOR_CONTACT);
        filterCase(driver, CONTRACT_NUMBER);
        selectCase(driver);
        assignJuniorContact(driver);
        logout(driver);

        login(driver, JUNIOR_CONTACT);
        filterCase(driver, CONTRACT_NUMBER);
        selectCase(driver);

        System.out.println("Number of tests: " + NUMBER_OF_TESTS);
    }

    public static void assignJuniorContact(WebDriver driver) {
        System.out.println("SC2 - assign [John_JUNIOR_STD_USER_CONTACT_CENTER] to case test run");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));
        By dropDownXpath = By.xpath("//mat-form-field/div[1]");
        By userXpath = By.xpath("//span[contains(.,'John_JUNIOR_STD_USER_CONTACT_CENTER Test')]");
        By zapiszXpath = By.xpath("//span[contains(.,'Zapisz')]");
        By okXpath = By.xpath("//div/simple-snack-bar/div[2]/button");

        assign(wait, dropDownXpath, userXpath, zapiszXpath, okXpath);

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - assign [John_JUNIOR_STD_USER_CONTACT_CENTER] to case test end");
    }


    public static void firstLogin(WebDriver driver, String login, String url) {
        System.out.println("SC2 - First login test run");
        driver.get(url);

        String acceptButton = "//button[.//span[contains(text(), 'AKCEPTUJĘ')]]";

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(acceptButton))).click();

        driver.findElement(By.id("username")).sendKeys(login);
        driver.findElement(By.id("password")).sendKeys("test");
        driver.findElement(By.id("kc-login")).click();

        wait.withTimeout(Duration.ofSeconds(SECONDS_10))
                .until(ExpectedConditions.urlContains("/app/cases"));

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - First login test end");
    }

    public static void importCase(WebDriver driver, String fileUrl) {
        System.out.println("SC2 - Import case test run");

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

        WebElement modalImportButton = fluentWait.until(ExpectedConditions.presenceOfElementLocated(modalImportButtonLocator));
        js.executeScript("arguments[0].scrollIntoView(true);", modalImportButton);
        js.executeScript("arguments[0].click();", modalImportButton);

        wait.until(ExpectedConditions.presenceOfElementLocated(closeButtonLocator)).click();

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - Import case test end");
    }

    public static void filterCase(WebDriver driver, String contractNumber) {
        System.out.println("SC2 - Filter case test run");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("button.ey-mediator-case-list__header-button--icon"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), '+ Numer kontraktu')]"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("numerKontraktu"))).sendKeys(contractNumber);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[contains(text(), 'Zapisz')]"))).click();

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - Filter case test end");
    }

    public static void logout(WebDriver driver) {
        System.out.println("SC2 - Logout test run");

        driver.findElement(By.cssSelector("ey-mediator-header span.mat-mdc-button-touch-target")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[.//span[contains(text(), 'Wyloguj się')]]"))).click();

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - Logout test end");
    }

    public static void login(WebDriver driver, String login) {
        System.out.println("SC2 - login [ " + login + " ] test run");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username"))).click();

        driver.findElement(By.id("username")).sendKeys(login);
        driver.findElement(By.id("password")).sendKeys("test");
        driver.findElement(By.id("kc-login")).click();
        wait.withTimeout(Duration.ofSeconds(30))
                .until(ExpectedConditions.urlContains("/app/cases"));

        String currentUrl = driver.getCurrentUrl();
        if (!currentUrl.contains("/app/cases")) {
            throw new RuntimeException("Login failed: URL does not contain '/app/cases'");
        }

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - login [ " + login + " ] test end");
    }

    public static void selectCase(WebDriver driver) {
        System.out.println("SC2 - select case test run");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));
        By xpath = By.xpath("/html/body/ey-mediator-gui-root/ey-mediator-shell-route/ey-mediator-shell-component/div/div/ey-mediator-cases-route/ey-mediator-case-list/div/div[2]/div[1]/table/tbody");
        wait.until(ExpectedConditions.presenceOfElementLocated(xpath)).click();

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - select case test end");
    }

    public static void assignSeniorOperacje(WebDriver driver) {
        System.out.println("SC2 - assign [John_SENIOR_STD_USER_OPERACJE] to case test run");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));
        By dropDownXpath = By.xpath("//div/span/span");
        By userXpath = By.xpath("//span[contains(.,'John_SENIOR_STD_USER_OPERACJE Test')]");
        By zapiszXpath = By.xpath("//div/div/div/button/span[2]");
        By okXpath = By.xpath("//div[2]/button/span[2]");

        assign(wait, dropDownXpath, userXpath, zapiszXpath, okXpath);

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - assign [John_SENIOR_STD_USER_OPERACJE] to case test end");
    }

    public static void markCheckBoxIfNeedIt(WebDriver driver) {
        System.out.println("SC2 - Mark checkbox [Umowa indywidualna] if few clients exist test run");

        By clientsContainerButtonLocator = By.xpath("//ey-mediator-client-data/div/div/button");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));
        WebElement clientsContainerButton = wait.until(ExpectedConditions.presenceOfElementLocated(clientsContainerButtonLocator));
        clientsContainerButton.click();

        By clientsContainerLocator = By.xpath("/html/body/ey-mediator-gui-root/ey-mediator-shell-route/ey-mediator-shell-component/div/div/ey-mediator-rbi-operations/ey-mediator-case-data/div/form/ey-mediator-client-data/div/div[2]/div");
        WebElement clientsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(clientsContainerLocator));

        List<WebElement> childElements = clientsContainer.findElements(By.className("ey-mediator-form__subheader-remove"));
        System.out.println("Number of clients = " + childElements.size());

        clientsContainerButton.click(); // Close the container (optional)

        if (childElements.size() == 1) {
            System.out.println("Try mark checkbox [Umowa indywidualna]");
            By checkBoxLocator = By.xpath("/html/body/ey-mediator-gui-root/ey-mediator-shell-route/ey-mediator-shell-component/div/div/ey-mediator-rbi-operations/ey-mediator-case-data/div/form/div/div/div[1]/div[9]/div[1]/mat-checkbox/div/div/input");
            WebElement checkbox = wait.until(ExpectedConditions.presenceOfElementLocated(checkBoxLocator));
            checkbox.click();

            if (!checkbox.isSelected()) {
                throw new Error("Checkbox [Umowa indywidualna] must be selected");
            }
            clickZapisz(driver);
        }

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - Mark checkbox [Umowa indywidualna] if few clients exist test end");
    }

    public static void fillHouseNumberForEachClient(WebDriver driver) {
        System.out.println("SC2 - Fill [Numer budynku] for each client test run");

        String houseNumber = "55";

        By clientsContainerButtonLocator = By.xpath("//ey-mediator-client-data/div/div/button");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));
        WebElement clientsContainerButton = wait.until(ExpectedConditions.presenceOfElementLocated(clientsContainerButtonLocator));
        clientsContainerButton.click();

        By clientsContainerLocator = By.xpath("/html/body/ey-mediator-gui-root/ey-mediator-shell-route/ey-mediator-shell-component/div/div/ey-mediator-rbi-operations/ey-mediator-case-data/div/form/ey-mediator-client-data/div/div[2]/div");
        WebElement clientsContainer = wait.until(ExpectedConditions.presenceOfElementLocated(clientsContainerLocator));

        List<WebElement> childElements = clientsContainer.findElements(By.className("ey-mediator-form__subheader-remove"));
        System.out.println("Number of clients = " + childElements.size());

        for (int i = 1; i <= childElements.size(); i++) {
            System.out.println("Modify [Numer budynku] for client " + i);

            if (i == 1) {
                By inputLocator = By.xpath("//ey-mediator-knf-address-data/div/form/div/div[2]/mat-form-field/div/div/div[2]/input");
                wait.until(ExpectedConditions.presenceOfElementLocated(inputLocator)).sendKeys(houseNumber);

                verifyThatInputCorrect(driver, inputLocator, houseNumber);

                By zapiszXpath = By.xpath(String.format("//div[%d]/div[2]/ey-mediator-client/div/div/div/div/form[2]/button", i));
                wait.until(ExpectedConditions.presenceOfElementLocated(zapiszXpath)).click();

            } else {
                // Expand client section
                By rozwinXpath = By.xpath(String.format("//div[%d]/div/div/button", i));
                wait.until(ExpectedConditions.presenceOfElementLocated(rozwinXpath)).click();

                // Find house number input and fill it
                By inputXpath = By.xpath(String.format("//div[%d]/div[2]/ey-mediator-client/div/div/div/div/ey-mediator-knf-address-data/div/form/div/div[2]/mat-form-field/div/div/div[2]/input", i));
                wait.until(ExpectedConditions.presenceOfElementLocated(inputXpath)).sendKeys(houseNumber);

                verifyThatInputCorrect(driver, inputXpath, houseNumber);

                By zapiszXpath = By.xpath(String.format("//div[%d]/div[2]/ey-mediator-client/div/div/div/div/form[2]/button", i));
                wait.until(ExpectedConditions.presenceOfElementLocated(zapiszXpath)).click();
            }

            By okXpath = By.xpath("//div[2]/button/span[2]");
            new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(TIMEOUT_120_SEC))
                    .until(ExpectedConditions.elementToBeClickable(okXpath))
                    .click();
        }

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - Fill [Numer budynku] for each client test end");
    }

    public static void assignJuniorOperacje(WebDriver driver) {
        System.out.println("SC2 - assign [John_JUNIOR_STD_USER_OPERACJE] to case test run");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));
        By dropDownXpath = By.xpath("//div/span/span");
        By caseState = By.xpath("//p[contains(.,'NOWA')]");
        By userXpath = By.xpath("//span[contains(.,'John_JUNIOR_STD_USER_OPERACJE Test')]");
        By zapiszXpath = By.xpath("//div/div/div/button/span[2]");
        By okXpath = By.xpath("//div[2]/button/span[2]");

        scrollToElement(driver, caseState);
        assign(wait, dropDownXpath, userXpath, zapiszXpath, okXpath);

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - assign [John_JUNIOR_STD_USER_OPERACJE] to case test end");
    }

    public static void pushCaseStepWAnalizie(WebDriver driver) {
        System.out.println("SC2 - push case to [W_ANALIZIE] step test run");

        By caseStateLabelXpath = By.xpath("//p[contains(.,'W_ANALIZIE')]");
        nextCaseStep(driver);
        closeNotYourCase(driver);
        isStatusChanged(driver, caseStateLabelXpath);

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - push case to [W_ANALIZIE] step test end");
    }

    public static void assignJuniorAnalityk(WebDriver driver) {
        System.out.println("SC2 - assign [John_JUNIOR_STD_USER_ANALITYK_KREDYTOWY] to case test run");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(SECONDS_10));
        By dropDownXpath = By.xpath("/html/body/ey-mediator-gui-root/ey-mediator-shell-route/ey-mediator-shell-component/div/div/ey-mediator-credit-decision/ey-mediator-case-data/div/form/ey-mediator-credit-decision/div[1]/div[1]/div[2]/form/div[1]/div[1]/mat-form-field/div[1]");
        By caseState = By.xpath("//p[contains(.,'W_ANALIZIE')]");
        By userXpath = By.xpath("//span[contains(.,'John_JUNIOR_STD_USER_ANALITYK_KREDYTOWY Test')]");
        By zapiszXpath = By.xpath("//ey-mediator-credit-decision/div/div/div[2]/button[2]");
        By okXpath = By.xpath("//div/simple-snack-bar/div[2]/button");

        scrollToElement(driver, caseState);
        assignWithNotYouCase(driver, wait, dropDownXpath, userXpath, zapiszXpath, okXpath);

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - assign [John_JUNIOR_STD_USER_ANALITYK_KREDYTOWY] to case test end");
    }

    public static void pushCaseAdnGetError(WebDriver driver) {
        System.out.println("SC2 - push case to next step and get alert test run");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));

        nextCaseStep(driver);

        By statusDecyzjiXpath = By.xpath("//li[contains(.,'Status decyzji')]");
        By dataDecyzjiXpath = By.xpath("//li[contains(.,'Data decyzji')]");

        wait.until(ExpectedConditions.presenceOfElementLocated(statusDecyzjiXpath)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(dataDecyzjiXpath)).click();

        closeVerificationFieldsAlert(wait);

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - push case to next step and get alert test end");
    }

    public static void addPositiveDecision(WebDriver driver) {
        System.out.println("SC2 - Add positive decision test run");

        By zapiszButtonXpath = By.xpath("//ey-mediator-credit-decision/div/div/div[2]/button[2]");
        By dodajButtonXpath = By.xpath("//ey-mediator-credit-decision/div/div/div[2]/button");
        By calendarXpath = By.xpath("//ey-mediator-decision/div/form/div/div/mat-form-field/div/div/div[3]/mat-datepicker-toggle/button");
        By calendarFieldXpath = By.xpath("//ey-mediator-decision/div/form/div/div/mat-form-field/div/div/div[2]/input");
        By dropDownXpath = By.xpath("/html/body/ey-mediator-gui-root/ey-mediator-shell-route/ey-mediator-shell-component/div/div/ey-mediator-credit-decision/ey-mediator-case-data/div/form/ey-mediator-credit-decision/div[1]/div[1]/div[2]/div/div/div/ey-mediator-decision/div/form/div[2]/div/mat-form-field/div[1]/div");
        By stateXpath = By.xpath("//span[contains(.,'POZYTYWNA')]");

        scrollToElement(driver, dodajButtonXpath);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));
        wait.until(ExpectedConditions.elementToBeClickable(dodajButtonXpath)).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(calendarXpath));

        // Enter today's date
        WebElement calendarInput = wait.until(ExpectedConditions.elementToBeClickable(calendarFieldXpath));
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println("Selected date: " + formattedDate);
        calendarInput.sendKeys(formattedDate);

        wait.until(ExpectedConditions.presenceOfElementLocated(dropDownXpath)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(stateXpath)).click();

        // Click "Zapisz" button
        wait.until(ExpectedConditions.elementToBeClickable(zapiszButtonXpath)).click();

        // Call method for successful button click (assuming successfullyButtonClick() exists)
        successfullyButtonClick(driver);

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - Add positive decision test end");
    }

    public static void pushCaseStepPoDecyzji(WebDriver driver) {
        System.out.println("SC2 - push case to [PO_DECYZJI] step test run");

        By zalButtonXpath = By.xpath("//a[2]");
        By decyzjaButtonXpath = By.xpath("//span[2]/span");

        By caseStateLabelXpath = By.xpath("//p[contains(.,'PO_DECYZJI')]");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));
        wait.until(ExpectedConditions.presenceOfElementLocated(zalButtonXpath)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(decyzjaButtonXpath)).click();

        nextCaseStep(driver);
        closeNotYourCase(driver);
        isStatusChanged(driver, caseStateLabelXpath);

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - push case to [PO_DECYZJI] step test end");
    }

    public static void addOnePagerToCase(WebDriver driver) {
        System.out.println("SC2 - Add One Pager to case test run");

        By attachmentsButtonXpath = By.xpath("//a[3]");
        By inputId = By.id("file-input");
        By addButtonXpath = By.xpath("//span[contains(.,'Dodaj załącznik')]");
        By listOfAttachmentsXpath = By.xpath("//span[contains(.,'TestAttachment.pdf - One Pager')]");
        By dropDownTypeAttachmentsXpath = By.xpath("//mat-form-field/div[1]");
        By onePagerXpath = By.xpath("//span[contains(.,'One Pager')]");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_120_SEC));

        wait.until(ExpectedConditions.elementToBeClickable(attachmentsButtonXpath)).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(dropDownTypeAttachmentsXpath)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(onePagerXpath)).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(inputId)).sendKeys(ONE_PAGER_URL);
        wait.until(ExpectedConditions.presenceOfElementLocated(addButtonXpath)).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(listOfAttachmentsXpath));

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - Add One Pager to case test end");
    }

    public static void fillOnePagerDateSending(WebDriver driver) {
        System.out.println("SC2 - Fill One Pager date sending test run");

        By operationButtonXpath = By.xpath("//span[2]/span");
        By clientInteractionButtonXpath = By.xpath("//ey-mediator-client-interaction/div/div/button");
        By calendarFieldXpath = By.xpath("//div[2]/div/div/form/div/div/mat-form-field/div/div/div[2]/input");
        By zapiszXpath = By.xpath("//ey-mediator-client-interaction/div/div[2]/div/div/button[2]");
        By okXpath = By.xpath("//span[contains(.,'Zamknij')]");
        By calendarXpath = By.xpath("//div[2]/div/div/form/div/div/mat-form-field/div/div/div[3]/mat-datepicker-toggle/button/span[3]");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));

        wait.until(ExpectedConditions.elementToBeClickable(operationButtonXpath)).click();
        wait.until(ExpectedConditions.elementToBeClickable(clientInteractionButtonXpath)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(calendarXpath));

        WebElement calendarInput = wait.until(ExpectedConditions.presenceOfElementLocated(calendarFieldXpath));
        LocalDate today = LocalDate.now();
        String formattedDate = today.format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println("Selected date: " + formattedDate);
        calendarInput.sendKeys(formattedDate);

        wait.until(ExpectedConditions.elementToBeClickable(zapiszXpath)).click();
        wait.until(ExpectedConditions.elementToBeClickable(okXpath));

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - Fill One Pager date sending test end");
    }

    public static void pushCaseStepDoKontaktu(WebDriver driver) {
        System.out.println("SC2 - push case to [DO_KONTAKTU_Z_KLIENTEM] step test run");

        By zalButtonXpath = By.xpath("//a[3]");
        By operationButtonXpath = By.xpath("//span[2]/span");
        By caseStateLabelXpath = By.xpath("//p[contains(.,'DO_KONTAKTU_Z_KLIENTEM')]");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));
        wait.until(ExpectedConditions.presenceOfElementLocated(zalButtonXpath)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(operationButtonXpath)).click();

        nextCaseStep(driver);
        closeNotYourCase(driver);
        isStatusChanged(driver, caseStateLabelXpath);

        NUMBER_OF_TESTS++;
        System.out.println("SC2 - push case to [DO_KONTAKTU_Z_KLIENTEM] step test end");
    }



    private static void closeVerificationFieldsAlert(WebDriverWait wait){
        By zamknijButtonVerifyFieldsXpath = By.xpath("//mat-dialog-actions/button");
        wait.until(ExpectedConditions.elementToBeClickable(zamknijButtonVerifyFieldsXpath)).click();
    }


    private static void assign(WebDriverWait wait, By dropDownXpath, By userXpath, By zapiszXpath, By okXpath) {
        wait.until(ExpectedConditions.elementToBeClickable(dropDownXpath)).click();
        wait.until(ExpectedConditions.elementToBeClickable(userXpath)).click();
        wait.until(ExpectedConditions.elementToBeClickable(zapiszXpath)).click();
        wait.until(ExpectedConditions.elementToBeClickable(okXpath)).click();
    }

    private static void assignWithNotYouCase(WebDriver driver, WebDriverWait wait, By dropDownXpath, By userXpath, By zapiszXpath, By okXpath) {
        wait.until(ExpectedConditions.elementToBeClickable(dropDownXpath)).click();
        wait.until(ExpectedConditions.elementToBeClickable(userXpath)).click();
        wait.until(ExpectedConditions.elementToBeClickable(zapiszXpath)).click();
        closeNotYourCase(driver);
        wait.until(ExpectedConditions.elementToBeClickable(okXpath)).click();
    }

    private static void clickZapisz(WebDriver driver) throws TimeoutException {
        By saveXpath = By.xpath("//div/div/div/button/span[2]");
        driver.findElement(saveXpath).click();

        By savedXpath = By.xpath("//div[@id='mat-snack-bar-container-live-1']/div/simple-snack-bar/div[2]/button/span[2]");
        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(TIMEOUT_15_SEC))
                .until(ExpectedConditions.presenceOfElementLocated(savedXpath))
                .click();
    }

    private static void verifyThatInputCorrect(WebDriver driver, By inputXpath, String textToInput) throws TimeoutException, NoSuchElementException {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));
        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(inputXpath));
        String actualText = inputField.getAttribute("value");

        if (!actualText.equals(textToInput)) {
            throw new RuntimeException(String.format("Error: Entered text does not match, actual = %s, must be = %s", actualText, textToInput));
        }
    }

    private static void scrollToElement(WebDriver driver, By inputXpath) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(inputXpath));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);

    }

    private static void scrollToTop(WebDriver driver) {
//        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, -400);");

        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.PAGE_UP).perform();

//        new Actions(driver)
//                .scrollByAmount(200, -200)
//                .perform();
    }

    public static void nextCaseStep(WebDriver driver) {

        By nextStepButtonXpath = By.xpath("//span[contains(.,'NASTĘPNY KROK')]");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));
        wait.until(ExpectedConditions.presenceOfElementLocated(nextStepButtonXpath)).click();

        By takButtonXpath = By.xpath("//mat-dialog-actions/button[2]");
        WebElement takButton = wait.until(ExpectedConditions.presenceOfElementLocated(takButtonXpath));

        Actions actions = new Actions(driver);
        actions.moveToElement(takButton).click().perform();
    }

    private static void closeNotYourCase(WebDriver driver) {
        By notYourCaseButtonXpath = By.xpath("/html/body/div[3]/div[2]/div/mat-dialog-container/div/div/ey-mediator-user-assignee-alert-dialog/div/mat-dialog-actions/button");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_60_SEC));
        wait.until(ExpectedConditions.presenceOfElementLocated(notYourCaseButtonXpath)).click();
    }

    private static void isStatusChanged(WebDriver driver, By caseStateLabelXpath) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));
        wait.until(ExpectedConditions.presenceOfElementLocated(caseStateLabelXpath));
    }

    private static void successfullyButtonClick(WebDriver driver) {
        By closeButtonXPath = By.xpath( "//div/simple-snack-bar/div[2]/button");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_15_SEC));
        wait.until(ExpectedConditions.presenceOfElementLocated(closeButtonXPath));
    }

}
