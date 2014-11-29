package com.crawler;

import com.crawler.core.ApplicationConfig;
import com.crawler.core.SharedDriver;
import com.crawler.domain.Hotel;
import com.crawler.service.HotelService;
import com.crawler.util.CrawlerUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ComponentScan(basePackages = {"com.crawler"})
@EnableAutoConfiguration
public class Application {

    @Autowired
    private ApplicationConfig applicationConfig;
    @Autowired
    private HotelService hotelService;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);
        Application application = (Application) applicationContext.getBean("application");
        application.loader();
    }

    private void loader() {
        SharedDriver sharedDriver = new SharedDriver();
        sharedDriver.get(applicationConfig.getUrl());

        CrawlerUtil.sleep(4000L);

        WebElement element = sharedDriver.findElement(By.cssSelector("#js_querystring"));
        element.sendKeys("colombo");
        CrawlerUtil.sleep(2000L);
        WebElement button = sharedDriver.findElement(By.cssSelector("#js_go"));
        button.click();

        CrawlerUtil.sleep(3000L);

        List<WebElement> webElements = sharedDriver.findElements(By.cssSelector("#js_itemlist > li"));
        for (WebElement element1 : webElements) {
            Hotel hotel = new Hotel();
            String itemId = element1.getAttribute("id");

            String[] splits = itemId.split("_");

            String cssSelector = "#" + itemId + " .item_wrapper .item_prices";
            WebElement info = sharedDriver.findElement(By.cssSelector(cssSelector));

            String hotelName = info.findElement(By.cssSelector("h3")).getText();
            hotel.setName(hotelName);

            String stars = info.findElement(By.cssSelector(".item_main .item_category .stars")).getText();
            hotel.setStarsScore(Integer.parseInt(stars));

            List<WebElement> cityInfos = info.findElements(By.cssSelector(".item_main .item_category .city span"));
            String city = cityInfos.get(1).getText();
            hotel.setCity(city);

            String infoButtonCss = "#js_button_info_" + splits[2];
            WebElement infoButton = sharedDriver.findElement(By.cssSelector(infoButtonCss));
            infoButton.click();

            CrawlerUtil.sleep(3000L);

            List<WebElement> hotelInfos = sharedDriver.findElements(By.cssSelector(".item_info_block.hotel .item_info_hotel_highlight"));
            if (hotelInfos.size() == 2) {
                String typeOfLodging = hotelInfos.get(0).getText();
                hotel.setTypeOfLodging(typeOfLodging);

                String numberOfRooms = hotelInfos.get(1).getText();
                hotel.setNumberOfRooms(Integer.parseInt(numberOfRooms));
            }


        }


    }
}
