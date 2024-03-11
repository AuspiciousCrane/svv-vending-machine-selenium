package th.ac.kmitl.se;

import org.graphwalker.java.annotation.AfterExecution;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SystemTest {
    static WebDriver driver;
    
    @BeforeAll
    static public void setUp() {
        WebDriverManager.chromedriver().browserVersion("99").setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get("https://fekmitl.pythonanywhere.com/kratai-bin");
    }

    @AfterAll
    static public void tearDown() {
        driver.quit();
    }

    @Test
    public void startOK() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("start")));
        driver.findElement(By.id("start")).click();

        assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/order", driver.getCurrentUrl());
    }

    @Test
    public void test7UserCheckOrderCheckOutPayCollectSuccess() {
        // Click Start
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("start")));
        driver.findElement(By.id("start")).click();

        assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/order", driver.getCurrentUrl());

        // Click Add Tum Thai
        driver.findElement(By.id("add_tum_thai")).click();

        String tumThaiVal = driver.findElement(By.id("txt_tum_thai")).getAttribute("value");
        assertEquals("1", tumThaiVal);

        // Click Add Tum Poo
        driver.findElement(By.id("add_tum_poo")).click();

        String tumPooVal = driver.findElement(By.id("txt_tum_poo")).getAttribute("value");
        assertEquals("1", tumPooVal);

        // Click Check Out
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("btn_check_out")));
        driver.findElement(By.id("btn_check_out")).click();

        assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/confirm?txt_tum_thai=1&txt_tum_poo=1&btn_check_out=Check+out", driver.getCurrentUrl());

        // Check Num Tum Thai and Tum Poo
        String msgNumTumThai = driver.findElement(By.id("msg_num_tum_thai")).getText();
        String msgNumTumPoo = driver.findElement(By.id("msg_num_tum_poo")).getText();

        assertEquals("1", msgNumTumThai);
        assertEquals("1", msgNumTumPoo);

        // Check Total Tum Thai and Tum Poo
        String msgTotalTumThai = driver.findElement(By.id("msg_total_tum_thai")).getText();
        String msgTotalTumPoo = driver.findElement(By.id("msg_total_tum_poo")).getText();

        assertEquals("100.00", msgTotalTumThai);
        assertEquals("120.00", msgTotalTumPoo);

        // Check Grand Total
        String msgGrandTotal = driver.findElement(By.id("msg_grand_total")).getText();
        assertEquals("220.00", msgGrandTotal);

        // Click Confirm Button
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("btn_confirm")));
        driver.findElement(By.id("btn_confirm")).click();

        assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/pay?btn_confirm=Confirm", driver.getCurrentUrl());

        // Fill In Valid Information
        driver.findElement(By.name("txt_credit_card_num")).sendKeys("1234567890");
        driver.findElement(By.name("txt_name_on_card")).sendKeys("John Doe");

        // Click Pay
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("btn_pay")));
        driver.findElement(By.id("btn_pay")).click();

        assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/check_payment?txt_credit_card_num=1234567890&txt_name_on_card=John+Doe&btn_pay=Pay", driver.getCurrentUrl());

        // Click Tum Thai and Tum Poo
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(By.className("ImgTumThai")));
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(By.className("ImgTumPoo")));

        driver.findElements(By.className("ImgTumThai")).forEach(WebElement::click);
        driver.findElements(By.className("ImgTumPoo")).forEach(WebElement::click);

        assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/check_collect?numTumThaiRemain=0&numTumPooRemain=0", driver.getCurrentUrl());
    }
}