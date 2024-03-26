import * as fs from 'fs';
import { By, until, WebDriver } from 'selenium-webdriver';

export async function testImport(driver: WebDriver, importUrl: string) {
  const filePath = importUrl;
  const importButtonXPath = "//button[contains(@class, 'ey-mediator-case-list__header-button') and contains(text(), 'Importuj excel')]";
  const importButton = await driver.findElement(By.xpath(importButtonXPath));

  const fileInputXPath = ".//div[contains(@class, 'ey-mediator-case-list__header')]//input[@type='file']";
  const fileInput = await driver.findElement(By.xpath(fileInputXPath));

  if (fs.existsSync(filePath)) {
    await fileInput.sendKeys(filePath);
  } else {
    await importButton.click();
  }

  const modalImportButtonXPath = "//button[contains(@class, 'ey-mediator-excel-import-preview-dialog__actions-import-button')]";
  const modalImportButton = await driver.wait(until.elementLocated(By.xpath(modalImportButtonXPath)), 90000);
  await driver.executeScript("arguments[0].scrollIntoView(true);", modalImportButton);

  await driver.executeScript("arguments[0].click();", modalImportButton);

  const closeButtonXPath = "//div/simple-snack-bar/div[2]/button";
  // const closeButtonXPath = '//div[@id=\'mat-snack-bar-container-live-0\']/div/simple-snack-bar/div[2]/button/span[2]';
  // const closeButtonXPath = '//button[contains(text(), "Zamknij")]';
  const closeButton = await driver.wait(until.elementLocated(By.xpath(closeButtonXPath)), 5000);

  await driver.wait(until.elementIsVisible(closeButton), 5000);
  await closeButton.click();

}
