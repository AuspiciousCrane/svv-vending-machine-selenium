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

    @Test
    public void test9UserAddsInvalidNumberOfTumThai() {
        // Click Start
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("start")));
        driver.findElement(By.id("start")).click();

        assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/order", driver.getCurrentUrl());

        // Click Add Tum Thai 4 Times
        for (int i = 0; i < 4; i++) {
            driver.findElement(By.id("add_tum_thai")).click();
        }

        // Check for Alert
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent());

        // Click OK on Alert
        driver.switchTo().alert().accept();

        // Check Current Page
        assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/order", driver.getCurrentUrl());

        // Check Number of Tum Thai
        String tumThaiVal = driver.findElement(By.id("txt_tum_thai")).getAttribute("value");
        assertEquals("3", tumThaiVal);
    }

    @Test
    public void test10UserAddsInvalidNumberOfTumPoo() {
        // Click Start
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("start")));
        driver.findElement(By.id("start")).click();

        assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/order", driver.getCurrentUrl());

        // Click Add Tum Thai 4 Times
        for (int i = 0; i < 4; i++) {
            driver.findElement(By.id("add_tum_poo")).click();
        }

        // Check for Alert
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent());

        // Click OK on Alert
        driver.switchTo().alert().accept();

        // Check Current Page
        assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/order", driver.getCurrentUrl());

        // Check Number of Tum Thai
        String tumThaiVal = driver.findElement(By.id("txt_tum_poo")).getAttribute("value");
        assertEquals("3", tumThaiVal);
    }

    @Test
    public void test11UserNotCollectingGoodOnTime() {
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

        // Wait For Time to Run Out
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.urlToBe("https://fekmitl.pythonanywhere.com/kratai-bin/check_collect?numTumThaiRemain=1&numTumPooRemain=1"));

    }

    @Test
    public void test13UserPayCreditCardWrongThreeTimes() {
        driver.get("https://fekmitl.pythonanywhere.com/kratai-bin");

        new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("start")));
            driver.findElement(By.id("start")).click();

            assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/order", driver.getCurrentUrl());

            // Click Add Tum Thai
            driver.findElement(By.id("add_tum_thai")).click();

            // Click Check Out
            new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("btn_check_out")));
            driver.findElement(By.id("btn_check_out")).click();

            assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/confirm?txt_tum_thai=1&txt_tum_poo=0&btn_check_out=Check+out",
                            driver.getCurrentUrl());

            // Click Confirm Button
            new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("btn_confirm")));
            driver.findElement(By.id("btn_confirm")).click();

            assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/pay?btn_confirm=Confirm",
                            driver.getCurrentUrl());

            new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("btn_pay")));
            driver.findElement(By.id("btn_pay")).click();

            assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/check_payment?txt_credit_card_num=&txt_name_on_card=&btn_pay=Pay",
                            driver.getCurrentUrl());

            String retry2Remaining = driver.findElement(By.id("msg_error")).getText();
            assertEquals("ERROR: Payment failed. 2 retries remaining.", retry2Remaining);

            new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("btn_pay")));
            driver.findElement(By.id("btn_pay")).click();

            String retry1Remaining = driver.findElement(By.id("msg_error")).getText();
            assertEquals("ERROR: Payment failed. 1 retries remaining.", retry1Remaining);

            new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("btn_pay")));
            driver.findElement(By.id("btn_pay")).click();

            // There is a slight problem in the kratai bin website where once you click pay
            // button wrong three times it should go back to the home page which it did, but
            // the url didn't change. So in this step I use the home page url instead of the
            // unchange url
            // assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin",
            // driver.getCurrentUrl());

            new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("start")));
            driver.findElement(By.id("start"));
    }

    @Test
    public void test14UserCancelOrderingGoods() {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("start")));
            driver.findElement(By.id("start")).click();

            assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/order", driver.getCurrentUrl());

            new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("btn_cancel")));
            driver.findElement(By.id("btn_cancel")).click();

            assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin", driver.getCurrentUrl());
    }

    @Test
    public void test15UserChangesOrderWhenOnSummaryPage() {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("start")));
            driver.findElement(By.id("start")).click();

            assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/order", driver.getCurrentUrl());

            // Click Add Tum Thai
            driver.findElement(By.id("add_tum_thai")).click();

            String tumThaiVal = driver.findElement(By.id("txt_tum_thai")).getAttribute("value");
            assertEquals("1", tumThaiVal);

            new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("btn_check_out")));
            driver.findElement(By.id("btn_check_out")).click();

            assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/confirm?txt_tum_thai=1&txt_tum_poo=1&btn_check_out=Check+out",
                            driver.getCurrentUrl());

            new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.elementToBeClickable(By.id("btn_change")));
            driver.findElement(By.id("btn_change")).click();

            assertEquals("https://fekmitl.pythonanywhere.com/kratai-bin/order",
                            driver.getCurrentUrl());

    }
}