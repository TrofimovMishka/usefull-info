package org.example;

import org.example.tests.FirstScenario;
import org.example.tests.SecondScenario;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;


public class App {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        WebDriver driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        driver.manage().window().maximize();

        SecondScenario.run(driver);
//        FirstScenario.run(driver);

        driver.quit();
        long end = System.currentTimeMillis();
        System.out.println("Execution time (s): " + (end - start)/1000);
    }
}

