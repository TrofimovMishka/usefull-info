import { Builder, WebDriver } from 'selenium-webdriver';
import 'selenium-webdriver/chrome';
import { testFilter } from '../filter.spec';
import { testFirstLogin } from '../first-login.spec';
import { testImport } from '../import-case-list.spec';
import { testLogout } from '../logout.spec';
import  { environment } from "../environments/local-test";

let driver: WebDriver;

const testUrl = environment.appUrl
const contractNumber = environment.contractNumber_1
const importUrl = environment.importUrl_1

describe('Scenariusz 1', () => {
  beforeAll(async () => {
    driver = await new Builder().forBrowser('chrome').build();
  });

  it('Test Logowania', async () => {
    await testFirstLogin(driver, 'john_superuser_rbi', 'test', testUrl);
  }, 60000);

  it('Test Importu', async () => {
    await testImport(driver, importUrl);
  }, 60000);

  it('Test Filtrowania', async () => {
    await testFilter(driver, contractNumber);
  }, 5000);

  it('Logout', async () => {
    await testLogout(driver);
  }, 5000);

  afterAll( async () => {
    await driver.close()
  })

});
