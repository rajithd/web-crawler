package com.crawler;

import com.crawler.core.ApplicationConfig;
import com.crawler.core.SharedDriver;
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
    }
}
