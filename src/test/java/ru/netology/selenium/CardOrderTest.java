package ru.netology.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static java.awt.SystemColor.text;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardOrderTest {

    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();// настраиваем вебдрайыер под хром
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        //опции для управления работы с памятью, будут полезны при запуске тестов в CI
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless"); //опция для включения хэдлесс режима, обязательно для запуска тестов в CI
        driver = new ChromeDriver(options); // передача опций в конструктор
        driver.get("http://localhost:9999");
    }

    @AfterEach
    public void tearDown() {
        driver.quit();
        driver = null;
        //закрываем страницу и обнуляем драйвер
    }

    @Test
    public void shouldSubmitValidForm(){

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Анна Асафьева");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__content")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=order-success]"));

        assertTrue(result.isDisplayed());
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", result.getText().trim());

    }

    @Test
    public void shouldNotAcceptEmptyName(){

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__content")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=name] .input__sub"));

        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения", result.getText().trim());

    }

    @Test
    public void shouldNotAcceptLatinName(){

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Anna Asafieva");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79998887766");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__content")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=name] .input__sub"));

        assertTrue(result.isDisplayed());
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", result.getText().trim());

    }

    @Test
    public void shouldNotAcceptEmptyPhone(){

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Анна Асафьева");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__content")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub"));

        assertTrue(result.isDisplayed());
        assertEquals("Поле обязательно для заполнения", result.getText().trim());

    }

    @Test
    public void shouldNotAcceptShortPhone(){

        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Анна Асафьева");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7999");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__content")).click();

        WebElement result = driver.findElement(By.cssSelector("[data-test-id=phone] .input__sub"));

        assertTrue(result.isDisplayed());
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", result.getText().trim());

    }








}
