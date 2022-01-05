package ru.netology.web;

import java.util.List;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class webTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        System.setProperty("webdriver.chrome.driver",
                // "./driver/win/chromedriver.exe"
                "driver/linux/chromedriver"
        );
    }

    @BeforeEach
    void setUp(){
        driver=new ChromeDriver();
    }

    @AfterEach
    void tearDown(){
        driver.quit();
        driver=null;
    }

    @Test
    void shouldWork(){
        driver.get("http://localhost:9999/");
        List<WebElement> textFields;
        textFields = driver.findElements(By.className("input__control"));
        textFields.get(0).sendKeys("Сметанина Екатерина");
        textFields.get(1).sendKeys("+79296190215");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();
        String actualMessage=driver.findElement(By.cssSelector("[data-test-id='order-success']")).getText().trim();
        String expectedMessage="Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        Assertions.assertEquals(expectedMessage, actualMessage, "Ожидаем успешное выполнение");
    }
    @Test
    void shouldFailOnWrongPhone() {
        driver.get("http://localhost:9999/");
        List<WebElement> textFields;
        textFields = driver.findElements(By.className("input__control"));
        textFields.get(0).sendKeys("Иванов Сергей");
        textFields.get(1).sendKeys("9296190215");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String actualMessage = driver.findElements(By.className("input__sub")).get(1).getText();
        Assertions.assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", actualMessage, "Ожидаем сообщение про некорреткно заполненный номер телефона");
    }

    @Test
    void shouldFailOnWrongName() {
        driver.get("http://localhost:9999/");
        List<WebElement> textFields;
        textFields = driver.findElements(By.className("input__control"));
        textFields.get(0).sendKeys("Cthutq Bdfyjd");
        textFields.get(1).sendKeys("+79012345678");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String actualMessage = driver.findElements(By.className("input__sub")).get(0).getText();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualMessage, "Ожидаем сообщение про некорректно заполненное имя");
    }

    /*
    @Test
    void shouldFailOnOneWordName() {
        // Надо писать issue. Ожидается Фамилия Имя, но позволяет ввести одно слово
        driver.get("http://localhost:9999/");
        List<WebElement> textFields;
        textFields = driver.findElements(By.className("input__control"));
        textFields.get(0).sendKeys("Иванов");
        textFields.get(1).sendKeys("+79012345678");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String actualMessage = driver.findElements(By.className("input__sub")).get(0).getText();
        Assertions.assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", actualMessage, "Ожидаем сообщение про некорректно заполненное имя");
    }
    */

    @Test
    void shouldFailOnEmptyName() {
        driver.get("http://localhost:9999/");
        List<WebElement> textFields;
        textFields = driver.findElements(By.className("input__control"));
        textFields.get(0).sendKeys("Иванов Сергей");
        driver.findElement(By.className("checkbox__box")).click();
        driver.findElement(By.tagName("button")).click();

        String actualMessage = driver.findElements(By.className("input__sub")).get(1).getText();
        Assertions.assertEquals("Поле обязательно для заполнения", actualMessage, "Ожидаем сообщение, что имя не заполнено");
    }

    @Test
    void shouldFailOnUnacceptedAgree(){
        driver.get("http://localhost:9999/");
        List<WebElement> textFields;
        textFields = driver.findElements(By.className("input__control"));
        textFields.get(0).sendKeys("Сметанина Екатерина");
        textFields.get(1).sendKeys("+79296190215");
        driver.findElement(By.tagName("button")).click();
        WebElement el = driver.findElement(By.cssSelector("label.checkbox.input_invalid"));
        String actualMessage=el.getText().trim();
        String expectedMessage="Я соглашаюсь с условиями обработки и использования моих персональных данных и разрешаю сделать запрос в бюро кредитных историй";
        Assertions.assertEquals(expectedMessage, actualMessage, "Должно быть сообщение о незаполненном согласии на обработку персональных данных");

    }
}
