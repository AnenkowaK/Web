package ru.netology.web;

import java.util.List;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeOptions;


public class WebTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(chromeOptions);
        driver.get("http://localhost:9999/");
    }

    @AfterEach
    void tearDown(){
        driver.quit();
        driver=null;
    }

    @Test
    void shouldWork(){
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Сметанина Екатерина");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79296190215");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String actualMessage=driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        String expectedMessage="Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        Assertions.assertEquals(expectedMessage, actualMessage, "Ожидаем успешное выполнение");
    }
    @Test
    void shouldFailOnWrongPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Сметанина Екатерина");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("9296190215");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"))
                .getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                actualMessage, "Ожидаем сообщение про некорреткно заполненный номер телефона");
    }

    @Test
    void shouldFailOnWrongName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Cthutq Bdfyjd");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79296190215");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"))
                .getText();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                actualMessage, "Ожидаем сообщение про некорректно заполненное поле имя");
    }


    @Test
    void shouldFailOnOneWordName() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79296190215");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String actualMessage=driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        Assertions.assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                actualMessage, "Ожидаем сообщение про некорректно заполненное имя");
    }


    @Test
    void shouldFailOnEmptyName() {
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79296190215");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub"))
                .getText();
        Assertions.assertEquals("Поле обязательно для заполнения", actualMessage, "Ожидаем сообщение, что имя не заполнено");
    }
    @Test
    void shouldFailOnEmptyPhone() {
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Иванов Алексей");
        driver.findElement(By.cssSelector("[data-test-id='agreement']")).click();
        driver.findElement(By.tagName("button")).click();
        String actualMessage = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub"))
                .getText();
        Assertions.assertEquals("Поле обязательно для заполнения", actualMessage, "Ожидаем сообщение, что поле телефона не заполнено");
    }


    @Test
    void shouldFailOnUnacceptedAgree(){
        driver.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Сметанина Екатерина");
        driver.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79296190215");
        driver.findElement(By.tagName("button")).click();
        WebElement el = driver.findElement(By.cssSelector("label.checkbox.input_invalid"));
        String actualMessage=el.getText().trim();
        String expectedMessage="Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        Assertions.assertEquals(expectedMessage, actualMessage, "Должно быть сообщение о незаполненном согласии на обработку персональных данных");

    }
}
