package com.mtuci.vkr.service;
import com.mtuci.vkr.model.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;

@Service
public class WildBerriesService {

    public Product saveProduct(Product product) {
        return null;
    }

    public HashMap<Long, String> getIdByName(String request, Integer pages) throws InterruptedException {
        // Установка пути к драйверу Chrome
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        // Создание экземпляра WebDriver
        WebDriver driver = new ChromeDriver(options);
        String url = "https://www.wildberries.ru/catalog/0/search.aspx?search=" + request;
        driver.get(url);
        // Явное ожидание элемента

        HashMap<Long, String> hashMap = new HashMap<>();
        List<WebElement> products;
        while (pages!=0){
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            By productSelector = By.className("product-card-list");
            WebElement productElement = wait.until(ExpectedConditions.visibilityOfElementLocated(productSelector));

            // Получение списка товаров
            products = productElement.findElements(By.cssSelector("a.product-card__link"));
            int numberOfProducts = products.size();
            WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(10));
            By productSelector1 = By.cssSelector("#catalog > div > div.pagination > div > a.pagination-next.pagination__next.j-next-page");
            try {
                WebElement nextPageButton = wait1.until(ExpectedConditions.visibilityOfElementLocated(productSelector1));
                // Цикл для прокручивания страницы до последнего элемента списка товаров
                Thread.sleep(1000);
                while (true) {
                    Actions actions = new Actions(driver);
                    actions.moveToElement(products.get(numberOfProducts - 1));
                    actions.perform();
                    Thread.sleep(1000); // замедление прокрутки на 1 секунду
                    List<WebElement> updatedProducts = driver.findElements(By.cssSelector("a.product-card__link"));

                    if (updatedProducts.size() == numberOfProducts) {
                        break;
                    } else {
                        numberOfProducts = updatedProducts.size();
                        products = updatedProducts;
                    }
                }
                // Вывод ссылок на все товары
                for (WebElement product : products) {
                    String href = product.getAttribute("href");
                    String id = (href.substring(href.lastIndexOf("g") + 2, href.lastIndexOf("d") - 1));
                    hashMap.put(Long.parseLong(id), href);
                }
                driver.get(nextPageButton.getAttribute("href"));
            } catch (TimeoutException ex){
                break;
            }
            pages--;
        }

        driver.close();
        return hashMap;
    }
}