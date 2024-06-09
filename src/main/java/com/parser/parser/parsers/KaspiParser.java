package com.parser.parser.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;

import java.util.Date;
import org.openqa.selenium.Cookie;

public class KaspiParser {

    private static final String BASIC_URL = "https://kaspi.kz/shop/search/?text=";

    public static String parseByQuery(String query) {
        String result = "";

        WebDriver webDriver = new ChromeDriver();
        /*
        try {
            webDriver.get(BASIC_URL + query);

            // Добавляем куки перед тем, как страница полностью загрузится
            Cookie cookie = new Cookie.Builder("kaspi.storefront.cookie.city", "710000000")
                    .domain("kaspi.kz")
                    .path("/")
                    .expiresOn(new Date(System.currentTimeMillis() + 31536000000L)) // Устанавливаем время истечения куки
                    .isSecure(false)
                    .build();
            webDriver.manage().addCookie(cookie);

            // После добавления куки перезагружаем страницу, чтобы куки вступили в силу
            webDriver.navigate().refresh();

            // Теперь продолжаем с обработкой страницы как обычно
            String pageTitle = webDriver.getTitle();
            if (pageTitle.contains("404 Not Found")) {
                return result;
            }

            // Для прокрутки вниз страницы
            JavascriptExecutor js = (JavascriptExecutor) webDriver;

            Thread.sleep(1500); // Ждём 1.5 секунд для прогрузки видео

            // Цикл для скролла вниз
            for (int i = 0; i < 10; i++) {
                Thread.sleep(100);
                js.executeScript("window.scrollBy(0,500)");
            }

            Thread.sleep(1000);

            // Ждем, пока элемент с определенным селектором не станет видимым
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofMillis(10000)); // Максимальное время ожидания в секундах

            // После того, как элемент станет видимым, продолжаем выполнение кода
            String html = webDriver.getPageSource();

            // Создаем объект Document из HTML-кода страницы
            Document doc = Jsoup.parse(html);

            // Извлекаем блоки с видео
            Elements videoElements = doc.select("item-card ddl_product ddl_product_link undefined ");

            // Перебираем каждый элемент с видео и извлекаем информацию
            for (Element videoElement : videoElements) {
                // Извлекаем заголовок видео
                Element titleElement = videoElement.selectFirst("item-card__name-link");
                String title = titleElement != null ? titleElement.text() : "Нет заголовка";

                // Извлекаем ссылку на видео
                Element thumbnailElement = videoElement.selectFirst("a.item-card__image-wrapper");
                String videoLink = thumbnailElement != null ? thumbnailElement.attr("href") : "Нет ссылки на товар";

                // Извлекаем превью видео
                Element previewImageElement = videoElement.selectFirst("img.item-card__image");
                String previewImageLink = previewImageElement != null ? previewImageElement.attr("src") : "Нет превью";

                // Извлекаем дату загрузки видео
                Element priceElement = videoElement.select("span.item-card__prices-price").get(1);
                String price = priceElement != null ? priceElement.text() : "Нет данных о дате загрузки";

                System.out.println(title);
                System.out.println(videoLink);
                System.out.println(previewImageLink);
                System.out.println(price);
                System.out.println();
            }

        } catch (NoSuchElementException e) {
            // Если элемент title не найден, обработка ошибки
            e.printStackTrace();
            return result;

        } catch (Exception e) {
            // Ловим остальные исключения
            e.printStackTrace();
            return result;

        } finally {
            webDriver.quit();
        }
         */

        try {
            webDriver.get(BASIC_URL + query);

            Cookie cookie = new Cookie.Builder("kaspi.storefront.cookie.city", "710000000")
                    .domain("kaspi.kz")
                    .path("/")
                    .expiresOn(new Date(System.currentTimeMillis() + 31536000000L))
                    .isSecure(false)
                    .build();
            webDriver.manage().addCookie(cookie);
            webDriver.navigate().refresh();

            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));

            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            for (int i = 0; i < 10; i++) {
                js.executeScript("window.scrollBy(0,500)");
                Thread.sleep(100);
            }

            String html = webDriver.getPageSource();
            Document doc = Jsoup.parse(html);
            Elements videoElements = doc.select("div.item-card"); // Corrected selector to match your HTML structure.

            if (videoElements.isEmpty()) {
                System.out.println("No elements found with the given selector.");
            }

            for (Element videoElement : videoElements) {
                String title = videoElement.select("div.item-card__name").text();
                String price = videoElement.select("span.item-card__prices-price").text();
                String linkToProduct = videoElement.select("a.item-card__image-wrapper").text();
                String linkToPreview = videoElement.select("a.item-card__image").text();

                System.out.println();
                System.out.println("Title: " + title);
                System.out.println("Price: " + price);
                System.out.println("Link to product: " + linkToProduct);
                System.out.println("Link to Preview: " + linkToPreview);
                System.out.println();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }

        return result;
    }

    public static void main(String[] args) {
        parseByQuery("телефон");
    }
}