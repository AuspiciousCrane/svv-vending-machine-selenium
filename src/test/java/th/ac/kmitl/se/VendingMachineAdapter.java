package th.ac.kmitl.se;

import java.time.Duration;
import java.util.List;

import org.graalvm.polyglot.Value;
import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


@Model(file  = "VendingMachineV2.json")
public class VendingMachineAdapter extends ExecutionContext {
    WebDriver driver;
    static final float PRICE_TUM_THAI = 100.0f;
    static final float PRICE_TUM_POO = 120.0f;

    @BeforeExecution
    public void setUp() {
        WebDriverManager.chromedriver().browserVersion("99").setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get("https://fekmitl.pythonanywhere.com/kratai-bin");
    }

    @AfterExecution
    public void tearDown() {
        driver.quit();
    }

    @Vertex()
    public void WELCOME() {
        System.out.println("Vertex WELCOME");
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("start")));
    }

    @Edge()
    public void start() {
        System.out.println("Edge start");
        driver.findElement(By.id("start")).click();
    }

    @Vertex()
    public void ORDERING() {
        System.out.println("Vertex ORDERING");
        // Wait for the check-out button to be clickable.
        // Your code here ...

        if (getAttribute("numTumThai").asInt() > 0 || getAttribute("numTumPoo").asInt() > 0) {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(By.id("btn_check_out")));
        }

        // Check that the number of orders is as expected.
        int numTumThaiExpected = getAttribute("numTumThai").asInt();
        int numTumPooExpected = getAttribute("numTumPoo").asInt();
        // Your code here ...
        int realTumThaiNo = Integer.parseInt(driver.findElement(By.id("txt_tum_thai")).getAttribute("value"));
        int realTumPooNo = Integer.parseInt(driver.findElement(By.id("txt_tum_poo")).getAttribute("value"));
    }

    @Edge()
    public void addTumThai() {
        System.out.println("Edge addTumThai");
        // Click add tum thai
        // Your code here ...
        driver.findElement(By.id("add_tum_thai")).click();
        setAttribute("numTumThai", Value.asValue(getAttribute("numTumThai").asInt() + 1));
    }

    @Edge()
    public void addTumPoo() {
        System.out.println("Edge addTumPoo");
        // Click add tum poo
        // Your code here ...
        driver.findElement(By.id("add_tum_poo")).click();
        setAttribute("numTumPoo", Value.asValue(getAttribute("numTumPoo").asInt() + 1));
    }

    @Vertex()
    public void ERROR_ORDER() {
        System.out.println("Vertex ERROR_ORDERING");
        // Wait for the alert dialog to be visible.
        // Your code here ...
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.alertIsPresent());
    }

    @Edge()
    public void ack() {
        System.out.println("Edge ack");
        // Click OK on the alert dialog
        // Your code here ...
        driver.switchTo().alert().accept();
    }

    @Edge()
    public void cancel() {
        System.out.println("Edge cancel");
        // Click cancel button
        // Your code here ...
        driver.findElement(By.id("btn_cancel")).click();

        setAttribute("numTumThai", Value.asValue(0));
        setAttribute("numTumPoo", Value.asValue(0));
    }

    @Edge()
    public void checkOut() {
        System.out.println("Edge checkOut");
        // Click check out button and wait for the confirm button to be clickable.
        // Your code here ...
        driver.findElement(By.id("btn_check_out")).click();
    }

    @Vertex()
    public void CONFIRMING() {
        System.out.println("Vertex CONFIRMING");
        // Wait for the confirm button to be clickable
        // Your code here ...
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("btn_confirm")));

        // Check that the information shown is as expected.
        int numTumThaiExpected = getAttribute("numTumThai").asInt();
        int numTumPooExpected = getAttribute("numTumPoo").asInt();
        // Your code here ...
    }

    @Edge()
    public void change() {
        System.out.println("Edge change");
        // Click change button
        // Your code here ...
        driver.findElement(By.id("btn_change")).click();
    }

    @Edge()
    public void pay() {
        System.out.println("Edge pay");
        // Click Confirm button
        // Your code here ...
        driver.findElement(By.id("btn_confirm")).click();
    }

    @Vertex()
    public void PAYING() {
        System.out.println("Vertex PAYING");
        // Wait for the pay button to be clickable.
        // Your code here ...
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("btn_pay")));

        // Check that the total amount is as expected.
        int numTumThaiExpected = getAttribute("numTumThai").asInt();
        int numTumPooExpected = getAttribute("numTumPoo").asInt();
        // Your code here ...

        // Check that payment error message is properly shown
        // Hint: Use getLastElement().getName() to get the name of the last visited edge.
        // Your code here ...

    }

    @Edge()
    public void paid() {
        System.out.println("Edge paid");
        // Submit valid payment details
        // Your code here ...
        driver.findElement(By.id("txt_credit_card_num")).sendKeys("1234567890");
        driver.findElement(By.id("txt_name_on_card")).sendKeys("John Doe");

        driver.findElement(By.id("btn_pay")).click();
    }

    @Edge()
    public void payError() {
        System.out.println("Edge payError");
        // Submit blank payment details to simulate payment error
        // Your code here ...
        driver.findElement(By.id("btn_pay")).click();
    }

    @Vertex()
    public void ERROR_PAY() {
        System.out.println("Vertex ERROR_PAY");
    }

    @Edge()
    public void payRetry() {
        System.out.println("Edge payRetry");
    }

    @Vertex()
    public void COLLECTING() {
        System.out.println("Vertex COLLECTING");
        // Wait for images to be clickable
        // Your code here ...
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(By.className("ImgTumThai")));
        new WebDriverWait(driver, Duration.ofSeconds(2))
                .until(ExpectedConditions.elementToBeClickable(By.className("ImgTumPoo")));

        // Check that the number of items shown is correct
        int numTumThaiExpected = getAttribute("numTumThai").asInt();
        int numTumPooExpected = getAttribute("numTumPoo").asInt();
        // Your code here ...

    }

    @Edge()
    public void collected() {
        System.out.println("Edge collected");
        // Click on each image to collect all dishes
        // Your code here ...
        driver.findElements(By.className("ImgTumThai")).forEach(WebElement::click);
        driver.findElements(By.className("ImgTumPoo")).forEach(WebElement::click);
    }

    @Edge()
    public void collectError() {
        System.out.println("Edge collectError");
        // Wait until the clearing page is shown
        // Your code here ...
        new WebDriverWait(driver, Duration.ofMillis(500))
                .until(ExpectedConditions.urlMatches("https://fekmitl.pythonanywhere.com/kratai-bin/check_collect*"));
    }

    @Vertex()
    public void ERROR_COLLECT() {
        System.out.println("Vertex ERROR_COLLECT");
    }

    @Edge()
    public void cleared() {
        System.out.println("Edge cleared");
        // Wait until redirection to the welcome page
        // Your code here ...
        new WebDriverWait(driver, Duration.ofMillis(500))
                .until(ExpectedConditions.urlMatches("https://fekmitl.pythonanywhere.com/kratai-bin"));
    }
}
