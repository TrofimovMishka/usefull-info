import {Builder, By, logging, until, WebDriver, WebElement} from 'selenium-webdriver';
import 'selenium-webdriver/chrome';
import {testFilter} from '../filter.spec';
import {testFirstLogin} from '../first-login.spec';
import {testImport} from '../import-case-list.spec';
import {testLogin} from '../login.spec';
import {testLogout} from '../logout.spec';
import {testSelectCase} from '../select-case.spec';
import {environment} from "../environments/local-test";
import {assignUser} from "../change-user.spec"

let driver: WebDriver;

const testUrl = environment.appUrl
const contractNumber = environment.contractNumber_2
const importUrl = environment.importUrl_2

const timeout15sec = 15000;

async function clickZapisz() {
  const saveXpath = '//div/div/div/button/span[2]';
  const savedXpath = "//div[@id='mat-snack-bar-container-live-1']/div/simple-snack-bar/div[2]/button/span[2]"

  await driver.findElement(By.xpath(saveXpath)).click();
  await driver.wait(until.elementLocated(By.xpath(savedXpath)), timeout15sec).click();
}

async function verifyThatInputCorrect(inputXpath: string, textToInput: string) {
  const actualText = await driver.findElement(By.xpath(inputXpath)).getAttribute('value');

  if (actualText !== textToInput) {
    throw new Error("Error: Entered text does not match, actual = " + actualText + ", must be = " + textToInput);
  }
}

async function nextCaseStep() {
  const nextStepButtonXpath = "//span[contains(.,'NASTĘPNY KROK')]";
  const takButtonXpath = "//mat-dialog-actions/button[2]";
  const optTimeout = timeout15sec;

  await driver.wait(until.elementLocated(By.xpath(nextStepButtonXpath)), optTimeout).click();
  const actions = driver.actions({async: true});
  const takButton = await driver.wait(until.elementLocated(By.xpath(takButtonXpath)), optTimeout);
  await actions.move({origin: takButton}).click().perform();
}

async function closeNotYourCase() {
  const notYourCaseButtonXpath = "/html/body/div[3]/div[2]/div/mat-dialog-container/div/div/ey-mediator-user-assignee-alert-dialog/div/mat-dialog-actions/button";
  await driver.wait(until.elementLocated(By.xpath(notYourCaseButtonXpath)), timeout15sec).click();
}

async function isStatusChanged(caseStateLabelXpath: string) {
  await driver.wait(until.elementLocated(By.xpath(caseStateLabelXpath)), timeout15sec);
}

async function closeVerificationFieldsAlertButton() {
  const zamknijButtonVerifyFieldsXpath = "//mat-dialog-actions/button";
  await driver.wait(until.elementLocated(By.xpath(zamknijButtonVerifyFieldsXpath)), timeout15sec).click();
}

const timeout90sec = 90000;

async function successfullyButtonClick() {
  const closeButtonXPath = "//div/simple-snack-bar/div[2]/button";
  await driver.wait(until.elementLocated(By.xpath(closeButtonXPath)), timeout90sec).click();
}

const timeout60Sec = 60000;
const timeout30sec = 30000;
describe('Scenariusz 2', () => {
  beforeAll(async () => {
    driver = await new Builder().forBrowser('chrome').build();
    await driver.manage().window().maximize();
  });

  it('Test pierwszego Logowania', async () => {
    await testFirstLogin(driver, 'john_superuser_rbi', 'test', testUrl);
  }, timeout60Sec);

  it('Test Importu', async () => {
    await testImport(driver, importUrl);
  }, timeout60Sec);

  it('Test Filtrowania', async () => {
    await testFilter(driver, contractNumber);
  }, 50000);

  it('Test Wylogowania', async () => {
    await testLogout(driver);
  }, timeout60Sec);

  it('Login john_senior_std_user_operacje', async () => {
    await testLogin(driver, 'john_senior_std_user_operacje', 'test');
  }, timeout60Sec);

  it('Test Filtrowania', async () => {
    await testFilter(driver, contractNumber);
  }, 50000);

  it('Test Wybrania sprawy', async () => {
    await testSelectCase(driver);
  }, timeout15sec);

  it('Assign case to john_senior_std_user_operacje', async () => {

    const dropDownXpath = '//div/span/span';
    // const userXpath = '//mat-option[@id=\'mat-option-92\']/span';
    const userXpath = '//span[contains(.,\'John_SENIOR_STD_USER_OPERACJE Test\')]';
    const expectedUser = "John_SENIOR_STD_USER_OPERACJE Test";

    await assignUser(driver, dropDownXpath, userXpath, expectedUser)

  }, timeout90sec);

  it('Mark checkbox [Umowa indywidualna] if few clients exist', async () => {
    const optTimeout = timeout15sec;

    // Open container with clients
    const clientsContainerButtonXpath = "//ey-mediator-client-data/div/div/button";
    const clientsContainerButton = driver.wait(until.elementLocated(By.xpath(clientsContainerButtonXpath)), optTimeout);

    // get container with clients
    await clientsContainerButton.click();
    const clientsContainerXpath = "/html/body/ey-mediator-gui-root/ey-mediator-shell-route/ey-mediator-shell-component/div/div/ey-mediator-rbi-operations/ey-mediator-case-data/div/form/ey-mediator-client-data/div/div[2]/div"
    const clientsContainer = driver.wait(until.elementLocated(By.xpath(clientsContainerXpath)), optTimeout);

    // get elements from this container
    const childElements = await clientsContainer.findElements(By.className("ey-mediator-form__subheader-remove"));
    console.log("Number of clients = " + childElements.length)

    await clientsContainerButton.click();

    if (childElements.length === 1) {
      const checkBoxXpath = '//*[@id=\'mat-mdc-checkbox-1-input\']';

      const checkbox: WebElement = driver.wait(until.elementLocated(By.xpath(checkBoxXpath)), optTimeout);
      await checkbox.click();

      if (!await checkbox.isSelected()) {
        throw new Error(`Checkbox [Umowa indywidualna] must be selected`);
      }
      await clickZapisz();
    }
  }, timeout90sec);

  it('Fill [Numer budynku] for each client', async () => {
    const optTimeout = timeout15sec;
    const houseNumber = "55"

    // Open container with clients
    const clientsContainerButtonXpath = "//ey-mediator-client-data/div/div/button";
    const clientsContainerButton = await driver.wait(until.elementLocated(By.xpath(clientsContainerButtonXpath)), optTimeout);

    // get container with clients
    await clientsContainerButton.click();
    const clientsContainerXpath = "/html/body/ey-mediator-gui-root/ey-mediator-shell-route/ey-mediator-shell-component/div/div/ey-mediator-rbi-operations/ey-mediator-case-data/div/form/ey-mediator-client-data/div/div[2]/div"
    const clientsContainer = await driver.wait(until.elementLocated(By.xpath(clientsContainerXpath)), optTimeout);

    // get elements from this container
    const childElements = await clientsContainer.findElements(By.className("ey-mediator-form__subheader-remove"));
    console.log("Number of clients = " + childElements.length)

    // for each client set [Numer budynku]
    for (let i = 1; i <= childElements.length; i++) {
      console.log("Iteration = " + i)
      if (i === 1) {
        const input1Xpath = "//ey-mediator-knf-address-data/div/form/div/div[2]/mat-form-field/div/div/div[2]/input";
        await driver.wait(until.elementLocated(By.xpath(input1Xpath)), optTimeout).sendKeys(houseNumber);

        await verifyThatInputCorrect(input1Xpath, houseNumber);

        const zapiszXpath = `//div[${i}]/div[2]/ey-mediator-client/div/div/div/div/form[2]/button`;
        await driver.wait(until.elementLocated(By.xpath(zapiszXpath)), optTimeout).click();

      } else {
        const rozwinXpath = `//div[${i}]/div/div/button`;
        await driver.wait(until.elementLocated(By.xpath(rozwinXpath)), optTimeout).click();

        const inputXpath = `//div[${i}]/div[2]/ey-mediator-client/div/div/div/div/ey-mediator-knf-address-data/div/form/div/div[2]/mat-form-field/div/div/div[2]/input`;
        await driver.wait(until.elementLocated(By.xpath(inputXpath)), optTimeout).sendKeys(houseNumber);

        // Verify that in input value correct
        await verifyThatInputCorrect(inputXpath, houseNumber);

        const zapiszXpath = `//div[${i}]/div[2]/ey-mediator-client/div/div/div/div/form[2]/button`;
        await driver.wait(until.elementLocated(By.xpath(zapiszXpath)), optTimeout).click();
      }
      const OkXpath = "//div[2]/button/span[2]";
      await driver.wait(until.elementLocated(By.xpath(OkXpath)), timeout60Sec).click();
    }

  }, timeout90sec);

  it('Assign case to john_junior_std_user_operacje', async () => {

    const dokumentacjaButtonXpath = '//a[2]/span[2]/span';
    const operacjeButtonXpath = '//a/span[2]';

    await driver.wait(until.elementLocated(By.xpath(dokumentacjaButtonXpath)), timeout15sec).click();
    await driver.wait(until.elementLocated(By.xpath(operacjeButtonXpath)), timeout15sec).click();

    const dropDownXpath = "//div/span/span";

    await driver.wait(until.elementLocated(By.xpath(dropDownXpath)), timeout30sec);

    const userXpath = '//span[contains(.,\'John_JUNIOR_STD_USER_OPERACJE Test\')]';
    const expectedUser = "John_JUNIOR_STD_USER_OPERACJE Test";

    await assignUser(driver, dropDownXpath, userXpath, expectedUser)

  }, timeout90sec);

  it('Logout', async () => {
    await testLogout(driver);
  }, timeout60Sec);

  it('Login john_junior_std_user_operacje user', async () => {
    await testLogin(driver, 'john_junior_std_user_operacje', 'test');
  }, timeout60Sec);

  it('Find case by filter', async () => {
    await testFilter(driver, contractNumber);
  }, 50000);

  it('Select case', async () => {
    await testSelectCase(driver);
  }, timeout30sec);

  it('Push case to next step', async () => {

    const caseStateLabelXpath = "//p[contains(.,'W_ANALIZIE')]";

    await nextCaseStep();
    await closeNotYourCase();
    await isStatusChanged(caseStateLabelXpath);

  }, timeout60Sec);

  it('Logout', async () => {
    await testLogout(driver);
  }, timeout60Sec);

  it('Login john_senior_std_user_analityk_kredytowy', async () => {
    await testLogin(driver, 'john_senior_std_user_analityk_kredytowy', 'test');
  }, timeout60Sec);

  it('Find case by filter', async () => {
    await testFilter(driver, contractNumber);
  }, 50000);

  it('Select case', async () => {
    await testSelectCase(driver);
  }, timeout30sec);


  it('Assign case to john_junior_std_user_analityk_kredytowy', async () => {

    const dropDownXpath = "/html/body/ey-mediator-gui-root/ey-mediator-shell-route/ey-mediator-shell-component/div/div/ey-mediator-credit-decision/ey-mediator-case-data/div/form/ey-mediator-credit-decision/div[1]/div[1]/div[2]/form/div[1]/div[1]/mat-form-field/div[1]";
    const userXpath = "//span[contains(.,'John_JUNIOR_STD_USER_ANALITYK_KREDYTOWY Test')]";
    const expectedUser = "John_JUNIOR_STD_USER_ANALITYK_KREDYTOWY Test";
    const zapiszXpath = "//ey-mediator-credit-decision/div/div/div[2]/button[2]"

    await driver.wait(until.elementLocated(By.xpath(dropDownXpath)), timeout15sec).click();
    await driver.wait(until.elementLocated(By.xpath(userXpath)), timeout15sec).click();

    const assignedUserDropDown = await driver.wait(until.elementLocated(By.xpath(dropDownXpath)), timeout15sec);
    const selectedValue = await assignedUserDropDown.getText();

    if (!selectedValue.includes(expectedUser)) {
      throw new Error(`Selected user value "${selectedValue}" does not match expected value "${expectedUser}"`);
    }

    await driver.wait(until.elementLocated(By.xpath(zapiszXpath)), timeout15sec).click();
    await closeNotYourCase();

    await successfullyButtonClick();

  }, timeout90sec);

  it('Logout', async () => {
    await testLogout(driver);
  }, timeout60Sec);

  it('Login john_junior_std_user_analityk_kredytowy', async () => {
    await testLogin(driver, 'john_junior_std_user_analityk_kredytowy', 'test');
  }, timeout60Sec);

  it('Find case by filter', async () => {
    await testFilter(driver, contractNumber);
  }, 50000);

  it('Select case', async () => {
    await testSelectCase(driver);
  }, timeout30sec);

  it('Push case to next step and get alert [Status decyzji] and [Data decyzji]', async () => {
    await nextCaseStep();

    const statusDecyzjiXpath = "//li[contains(.,'Status decyzji')]";
    const dataDecyzjiXpath = "//li[contains(.,'Data decyzji')]";

    const status = await driver.wait(until.elementLocated(By.xpath(statusDecyzjiXpath)), timeout15sec);
    const data = await driver.wait(until.elementLocated(By.xpath(dataDecyzjiXpath)), timeout15sec);
    console.log("statusDecyzji = " + await status.getText() + ", dataDecyzji = " + await data.getText());

    await closeVerificationFieldsAlertButton();
  }, timeout60Sec);

  it('Add positive decision', async () => {
    const zapiszButtonXpath = "//ey-mediator-credit-decision/div/div/div[2]/button[2]";
    const dodajButtonXpath = "//ey-mediator-credit-decision/div/div/div[2]/button"
    const calendarXpath = "//ey-mediator-decision/div/form/div/div/mat-form-field/div/div/div[3]/mat-datepicker-toggle/button"
    const calendarFieldXpath = "//ey-mediator-decision/div/form/div/div/mat-form-field/div/div/div[2]/input"

    const dropDownXpath = "/html/body/ey-mediator-gui-root/ey-mediator-shell-route/ey-mediator-shell-component/div/div/ey-mediator-credit-decision/ey-mediator-case-data/div/form/ey-mediator-credit-decision/div[1]/div[1]/div[2]/div/div/div/ey-mediator-decision/div/form/div[2]/div/mat-form-field/div[1]/div";
    const stateXpath = "//span[contains(.,'POZYTYWNA')]";
    //Calendar

    await driver.wait(until.elementLocated(By.xpath(dodajButtonXpath)), timeout15sec).click();
    const calendar = await driver.wait(until.elementLocated(By.xpath(calendarXpath)), timeout15sec);

    const calendarInput = await driver.wait(until.elementLocated(By.xpath(calendarFieldXpath)), timeout15sec);

    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    console.log(`Selected date: ${year}-${month}-${day}`);

    await calendarInput.sendKeys(`${year}-${month}-${day}`);
    // // await calendar.click();

    // Status
    await driver.wait(until.elementLocated(By.xpath(dropDownXpath)), timeout15sec).click();
    await driver.wait(until.elementLocated(By.xpath(stateXpath)), timeout15sec).click();

    //Zapisz
    await driver.wait(until.elementLocated(By.xpath(zapiszButtonXpath)), timeout15sec).click();
    await successfullyButtonClick();

  }, timeout30sec);

  it('Push case to next step', async () => {

    const zalButtonXpath = "//a[2]";
    const decyzjaButtonXpath = "//span[2]/span";
    const caseStateLabelXpath = "//p[contains(.,'PO_DECYZJI')]";

    await driver.wait(until.elementLocated(By.xpath(zalButtonXpath)), timeout15sec).click();
    await driver.wait(until.elementLocated(By.xpath(decyzjaButtonXpath)), timeout15sec).click();

    await nextCaseStep();
    await closeNotYourCase();
    await isStatusChanged(caseStateLabelXpath);

  }, timeout60Sec);

  it('Logout', async () => {
    await testLogout(driver);
  }, timeout60Sec);

  it('Login john_senior_std_user_operacje', async () => {
    await testLogin(driver, 'john_senior_std_user_operacje', 'test');
  }, timeout60Sec);

  it('Find case by filter', async () => {
    await testFilter(driver, contractNumber);
  }, 50000);

  it('Select case', async () => {
    await testSelectCase(driver);
  }, timeout30sec);

  it('Assign case to john_senior_std_user_operacje', async () => {

    const dropDownXpath = "//mat-form-field/div[1]";
    const userXpath = "//span[contains(.,'John_SENIOR_STD_USER_OPERACJE Test')]";
    const expectedUser = "John_SENIOR_STD_USER_OPERACJE Test";
    const zapiszXpath = "//span[contains(.,'Zapisz')]";

    await driver.wait(until.elementLocated(By.xpath(dropDownXpath)), timeout15sec).click();
    await driver.wait(until.elementLocated(By.xpath(userXpath)), timeout15sec).click();

    const assignedUserDropDown = await driver.wait(until.elementLocated(By.xpath(dropDownXpath)), timeout15sec);
    const selectedValue = await assignedUserDropDown.getText();

    if (!selectedValue.includes(expectedUser)) {
      throw new Error(`Selected user value "${selectedValue}" does not match expected value "${expectedUser}"`);
    }

    await driver.wait(until.elementLocated(By.xpath(zapiszXpath)), timeout15sec).click();

    await successfullyButtonClick();
  }, timeout90sec);

  it('Add One Pager to case', async () => {

    const attachmentsButtonXpath = "//a[3]";
    const inputId = "file-input";
    const addButtonXpath = "//span[contains(.,'Dodaj załącznik')]";
    const file = "/home/trofimov/Projects/Java/rbi/rbi-chf-fe/e2e/scenario-2/TestAttachment.pdf";
    const listOfAttachmentsXpath = "//span[contains(.,'TestAttachment.pdf - One Pager')]";

    const dropDownTypeAttachmentsXpath = "//mat-form-field/div[1]";
    const onePagerXpath = "//span[contains(.,'One Pager')]";

    await driver.wait(until.elementLocated(By.xpath(attachmentsButtonXpath)), timeout15sec).click();

    await driver.wait(until.elementLocated(By.xpath(dropDownTypeAttachmentsXpath)), timeout15sec).click();
    await driver.wait(until.elementLocated(By.xpath(onePagerXpath)), timeout15sec).click();

    await driver.wait(until.elementLocated(By.id(inputId)), timeout15sec).sendKeys(file);
    await driver.wait(until.elementLocated(By.xpath(addButtonXpath)), timeout15sec).click();

    await driver.wait(until.elementLocated(By.xpath(listOfAttachmentsXpath)), timeout90sec);

  }, timeout60Sec);

  // //span[2]/span

  it('Fill One Pager date sending', async () => {

    const operationButtonXpath = "//span[2]/span";
    const clientInteractionButtonXpath = "//ey-mediator-client-interaction/div/div/button";
    const calendarFieldXpath = "//div[2]/div/div/form/div/div/mat-form-field/div/div/div[2]/input";
    const zapiszXpath = "//ey-mediator-client-interaction/div/div[2]/div/div/button[2]";
    const okXpath = "//span[contains(.,'Zamknij')]";
    const calendarXpath = "//div[2]/div/div/form/div/div/mat-form-field/div/div/div[3]/mat-datepicker-toggle/button/span[3]";


    await driver.wait(until.elementLocated(By.xpath(operationButtonXpath)), timeout15sec).click();
    await driver.wait(until.elementLocated(By.xpath(clientInteractionButtonXpath)), timeout15sec).click();
    await driver.wait(until.elementLocated(By.xpath(calendarXpath)), timeout15sec);

    const calendarInput = await driver.wait(until.elementLocated(By.xpath(calendarFieldXpath)), timeout15sec);

    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    console.log(`Selected date: ${year}-${month}-${day}`);

    await calendarInput.sendKeys(`${year}-${month}-${day}`);

    await driver.wait(until.elementLocated(By.xpath(zapiszXpath)), timeout90sec).click();
    await driver.wait(until.elementLocated(By.xpath(okXpath)), timeout90sec).click();


  }, timeout60Sec);

  afterAll(async () => {
    await driver.close()
  })

});

