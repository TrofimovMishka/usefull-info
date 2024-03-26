import { Builder, WebDriver } from 'selenium-webdriver';
import 'selenium-webdriver/chrome';
import { testFilter } from '../filter.spec';
import { testFirstLogin } from '../first-login.spec';
import { testImport } from '../import-case-list.spec';
import { testLogin } from '../login.spec';
import { testLogout } from '../logout.spec';
import { testSelectCase } from '../select-case.spec';

let driver: WebDriver;

const testUrl = 'https://mediator.eydev.net/';
// - https://dev.mediator.rbipl.raiffeisen.pl/
// - https://test.mediator.rbipl.raiffeisen.pl/

const contractNumber = '123';
const importUrl = 'C:/selenium/testowy_plik_wsadowy.xlsx';

describe('Scenariusz 4', () => {
  beforeAll(async () => {
    driver = await new Builder().forBrowser('chrome').build();
  });

  it('Test pierwszego Logowania', async () => {
    await testFirstLogin(driver, 'john_superuser_rbi', 'test', testUrl);
  }, 60000);

  it('Test Importu', async () => {
    await testImport(driver, importUrl);
  }, 60000);

  it('Test Filtrowania', async () => {
    await testFilter(driver, contractNumber);
  }, 50000);

  it('Test Wylogowania', async () => {
    await testLogout(driver);
  }, 60000);

  it('Test Logowania', async () => {
    await testLogin(driver, 'john_senior_std_user_operacje', 'test');
  }, 60000);

  it('Test Filtrowania', async () => {
    await testFilter(driver, contractNumber);
  }, 50000);

  it('Test Wybrania sprawy', async () => {
    await testSelectCase(driver);
  }, 60000);
});
