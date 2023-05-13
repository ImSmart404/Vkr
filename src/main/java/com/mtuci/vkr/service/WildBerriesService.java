package com.mtuci.vkr.service;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mtuci.vkr.model.ExtendedInfo;
import com.mtuci.vkr.model.MainInfo;
import com.mtuci.vkr.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class WildBerriesService {

    ProductRepository productRepository;
    public List<MainInfo> getProductsInfo(String request, Integer pages) throws InterruptedException, IOException {
        List<Long> idList = getId(request,pages);
        List<MainInfo> mainInfoList = getMainInfoListByProductsId(idList);
        List<ExtendedInfo> extendedInfoList = getExtendedInfoListByProductsId(idList);
        productRepository.saveAll(mainInfoList);
        return mainInfoList;
    }

    public List<ExtendedInfo> getProductsExtendedInfo() throws InterruptedException {
        return null;
    }


    public List<ExtendedInfo> getExtendedInfoListByProductsId(List<Long> idList){
        List<ExtendedInfo> extendedInfoList = new ArrayList<>();
        List<Long> idListTemp = new ArrayList<>(idList);
        Long id = 0L;
        while (!idListTemp.isEmpty()){
            ExtendedInfo extendedInfo = new ExtendedInfo();
            id = idListTemp.get(0);
            Long vol = Long.parseLong(Long.toString(id).substring(0, 4));
            Long part = Long.parseLong(Long.toString(id).substring(0, 6));
            String url = String.format("https://basket-10.wb.ru/vol%d/part%d/%d/info/ru/card.json", vol, part, id);
            log.info(url);
            idListTemp.remove(0);
        }
        return extendedInfoList;
    }

    // Метод который возвращает преобразованные в обьект Product продукты по их id
    public List<MainInfo> getMainInfoListByProductsId(List<Long> idList) throws IOException {
        List<MainInfo> mainInfoList = new ArrayList<>();
        List<Long> idListTemp = new ArrayList<>(idList);
        Long id = 0L;
        int i =0;
        while (!idListTemp.isEmpty()){
            MainInfo mainInfo = new MainInfo();
            id = idListTemp.get(0);
            String url = "https://card.wb.ru/cards/detail?appType=1&curr=rub&dest=-1257786&regions=80,64,38,4,115,83,33,68,70,69,30,86,75,40,1,66,48,110,31,22,71,114&spp=0&nm=" + id;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
            mainInfo.setId(id);
            mainInfo.setBrand(jsonObject.getAsJsonObject("data").getAsJsonArray("products").get(0).getAsJsonObject().get("brand").getAsString());
            mainInfo.setPriceU(jsonObject.getAsJsonObject("data").getAsJsonArray("products").get(0).getAsJsonObject().get("priceU").getAsLong()/100);
            mainInfo.setSalePrice(jsonObject.getAsJsonObject("data").getAsJsonArray("products").get(0).getAsJsonObject().get("salePriceU").getAsLong()/100);
            mainInfo.setRating(jsonObject.getAsJsonObject("data").getAsJsonArray("products").get(0).getAsJsonObject().get("rating").getAsInt());
            mainInfo.setPics(jsonObject.getAsJsonObject("data").getAsJsonArray("products").get(0).getAsJsonObject().get("pics").getAsInt());
            mainInfoList.add(mainInfo);
            idListTemp.remove(0);
        }
        return mainInfoList;
    }

    //Метод который по запросу пользователя парсит по указанному количеству страниц id всех товаров
    public List<Long> getId(String request, Integer pages) throws InterruptedException {
        // Установка пути к драйверу Chrome
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        // Создание экземпляра WebDriver
        WebDriver driver = new ChromeDriver(options);
        String url = "https://www.wildberries.ru/catalog/0/search.aspx?search=" + request;
        driver.get(url);
        // Явное ожидание элемента
        List<Long> idList = new ArrayList<>();
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
            // Цикл для прокручивания страницы до последнего элемента списка товаров
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
            // Запись ссылок и id всех товаров в hashMap
            for (WebElement product : products) {
                String href = product.getAttribute("href");
                String id = (href.substring(href.lastIndexOf("g") + 2, href.lastIndexOf("d") - 1));
                idList.add(Long.parseLong(id));
            }
            try {
                WebElement nextPageButton = wait1.until(ExpectedConditions.visibilityOfElementLocated(productSelector1));
                driver.get(nextPageButton.getAttribute("href"));
            } catch (TimeoutException ex){
                log.error("Ошибка: {}", ex.getMessage());
                break;
            }
            pages--;
        }
        driver.close();
        return idList;
    }
}