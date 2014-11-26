package com.crawler.core;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.concurrent.TimeUnit;

public class SharedDriver extends EventFiringWebDriver {


    private static final int IMPLICIT_WAIT = 7;

    private static final WebDriver REAL_DRIVER;
    private static final Thread CLOSE_THREAD = new Thread() {
        @Override
        public void run() {
            System.out.println("Going to close the shared driver");
            try {
                REAL_DRIVER.quit();
            }
            catch( Exception e) {
                // nothing, it seems fairly normal
            }
        }
    };

    static {
        //solve issue when firefox lose focus and doesn't raise change event
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("focusmanager.testmode", true);

        FirefoxBinary binary = new FirefoxBinary();
        binary.setEnvironmentProperty("DISPLAY", ":1");
        REAL_DRIVER = new FirefoxDriver(binary,profile);
        //REAL_DRIVER = new PhantomJSDriver();

        REAL_DRIVER.manage().window().setPosition(new Point(0, 0));
        REAL_DRIVER.manage().window().setSize(new Dimension(1280,800));

        //implicit waiting because we use dynamic html
        REAL_DRIVER.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
        Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
    }

    public SharedDriver() {
        super(REAL_DRIVER);
    }

    public void close() {
        if(Thread.currentThread() != CLOSE_THREAD) {
            throw new UnsupportedOperationException("You shouldn't close this WebDriver. It's shared and will close when the JVM exits.");
        }
        super.close();
    }


}
