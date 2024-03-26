import {By, until, WebDriver} from 'selenium-webdriver';

export async function testSelectCase(driver: WebDriver) {
  await driver.wait(until.elementLocated(By.xpath("/html/body/ey-mediator-gui-root/ey-mediator-shell-route/ey-mediator-shell-component/div/div/ey-mediator-cases-route/ey-mediator-case-list/div/div[2]/div[1]/table/tbody")), 10000).click();
}
