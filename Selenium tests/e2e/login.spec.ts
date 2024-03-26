import { By, until, WebDriver } from 'selenium-webdriver';

export async function testLogin(driver: WebDriver, login: string, password: string) {

  await driver.wait(until.elementLocated(By.id('username')), 10000).click();

  await driver.findElement(By.id('username')).sendKeys(login);
  await driver.findElement(By.id('password')).sendKeys(password);
  await driver.findElement(By.id('kc-login')).click();

  await driver.wait(until.urlContains('/app/cases'), 30000);

  const currentUrl = await driver.getCurrentUrl();
  expect(currentUrl).toContain('/app/cases');
}
