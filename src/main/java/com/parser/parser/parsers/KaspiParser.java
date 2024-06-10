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

import java.util.Date;
import org.openqa.selenium.Cookie;

public class KaspiParser {

    private static final String BASIC_URL = "https://kaspi.kz/shop/search/?text=";

    public static String parseByQuery(String query) {
        String result = "";

        WebDriver webDriver = new ChromeDriver();

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