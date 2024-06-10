package com.parser.parser.parsers;

import com.parser.parser.models.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.Cookie;
import org.springframework.stereotype.Service;

@Service
public class KaspiParser {

    private static final String BASIC_URL = "https://kaspi.kz/shop/search/?text=";

    public String parseByQuery(String query) {
        StringBuilder result = new StringBuilder();

        WebDriver webDriver = new ChromeDriver();

        try {
            webDriver.get(BASIC_URL + query);

            // set city code to cookies
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
            Elements videoElements = doc.select("div.item-card");

            if (videoElements.isEmpty()) {
                System.out.println("No elements found with the given selector.");
            }

            for (Element videoElement : videoElements) {
                String title = videoElement.select("div.item-card__name").text();
                String price = videoElement.select("span.item-card__prices-price").text();

                // todo fix bug with links
                String linkToProduct = videoElement.select("a.item-card__image-wrapper").text();
                String linkToPreview = videoElement.select("a.item-card__image").text();

                Product product = new Product();
                product.setTitle(title);
                product.setPrice(price);
                product.setLink(linkToProduct);
                product.setPreview(linkToPreview);

                result.append(product.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }

        return result.toString();
    }

    public String parseByProductId(String productId) {
        String result = "";

        WebDriver webDriver = new ChromeDriver();

        try {
            // todo check with path
            webDriver.get(BASIC_URL + productId);

            // set city code to cookies
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
            Elements videoElement = doc.select("div.item-card");

            // if query is wrong
            if (videoElement.isEmpty()) {
                return null;
            }

            String title = videoElement.select("div.item-card__name").text();
            String price = videoElement.select("span.item-card__prices-price").text();
            String linkToProduct = videoElement.select("a.item-card__image-wrapper").text();
            String linkToPreview = videoElement.select("a.item-card__image").text();

            // set attributes to product model & return
            Product product = new Product();
            product.setTitle(title);
            product.setPrice(price);
            product.setLink(linkToProduct);
            product.setPreview(linkToPreview);

            result += product.toString();

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            webDriver.quit();
        }
        return result;
    }
}
