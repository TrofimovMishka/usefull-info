import { By, until, WebDriver } from 'selenium-webdriver';

export async function testLogout(driver: WebDriver) {
  const headerButton = await driver.findElement(By.css('ey-mediator-header span.mat-mdc-button-touch-target'));
  await headerButton.click();

  await driver.wait(until.elementLocated(By.xpath("//button[.//span[contains(text(), 'Wyloguj się')]]")), 10000).click();


}
