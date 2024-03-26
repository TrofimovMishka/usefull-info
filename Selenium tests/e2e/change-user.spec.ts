import {By, until, WebDriver} from 'selenium-webdriver';

export async function assignUser(driver: WebDriver, dropDownXpath: string, selectUserXpath: string, username: string) {

  const optTimeout = 15000;
  // Find dropdown przypisany uzytkownik
  await driver.wait(until.elementLocated(By.xpath(dropDownXpath)), optTimeout).click();
  // Select user
  await driver.findElement(By.xpath(selectUserXpath)).click();

  const assignedUserDropDown = await driver.wait(until.elementLocated(By.xpath(dropDownXpath)), optTimeout);
  const selectedValue = await assignedUserDropDown.getText();

  if (!selectedValue.includes(username)) {
    throw new Error(`Selected user value "${selectedValue}" does not match expected value "${username}"`);
  }

  // Click Zapisz
  await driver.findElement(By.xpath('//div/div/div/button/span[2]')).click();
  // Successfully
  await driver.wait(until.elementLocated(By.xpath('//div[2]/button/span[2]')), optTimeout).click();
}
