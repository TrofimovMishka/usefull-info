import { By, until, WebDriver } from 'selenium-webdriver';

export async function testFilter(driver: WebDriver, contractNumber: string) {
  const filterButton = await driver.findElement(By.css('button.ey-mediator-case-list__header-button--icon'));
  await filterButton.click();

  const caseNumberButton = await driver.wait(until.elementLocated(By.xpath('//button[contains(text(), "+ Numer kontraktu")]')), 10000);
  await caseNumberButton.click();

  const caseNumberInput = await driver.wait(until.elementLocated(By.name('numerKontraktu')), 10000);

  await caseNumberInput.sendKeys(contractNumber);

  const saveButton = await driver.findElement(By.xpath('//button[contains(text(), "Zapisz")]'));
  await saveButton.click();
}
