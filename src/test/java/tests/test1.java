package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.apache.log4j.Logger;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import org.openqa.selenium.support.ui.Select;

import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.Random;


public class test1 {

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver","drivers/chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        final Logger logger = Logger.getLogger(test1.class);

        //site açılması
        String url = "https://www.gittigidiyor.com/";
        driver.get(url);

        String URL = driver.getCurrentUrl();
        Assert.assertEquals(URL, "https://www.gittigidiyor.com/" );
        logger.info("Anasayfa açıldı. " + url);

        //login sayfası açılması
        driver.get("https://www.gittigidiyor.com/uye-girisi");

        //login işlemi
        try {
            driver.findElement(By.name("kullanici")).sendKeys("azberka@gmail.com");
            driver.findElement(By.name("sifre")).sendKeys("azberka2580");
            driver.findElement(By.id("gg-login-enter")).click();
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            String URL2 = driver.getCurrentUrl();
            Assert.assertEquals(URL2, "https://www.gittigidiyor.com/" );
            logger.info("Login işlemi oldu.");
        }
    catch (AssertionError e){
        logger.error("Login işlemi olmadı. Hata="+e.toString());
    }

        //arama çubuğuna bilgisayar yazılması ve çıkan sayfada 2. sayfaya geçilmesi
        try {
        driver.findElement(By.name("k")).sendKeys("bilgisayar");
        driver.findElement(new By.ByCssSelector("button.qjixn8-0[type=\"submit\"]")).click();

        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[contains(@class,'pager')]/ul/li[2]/a")));
        logger.info("2. sayfa açıldı.");
        }
        catch (AssertionError e){
            logger.error("Login olunamadı. Hata="+e.toString());
        }

        //rastgele bir ürün seçilmesi ve sepete eklenmesi
        try {
            Random rand = new Random();
            List<WebElement> allProducts = driver.findElements(By.xpath("//ul[contains(@class,'catalog-view')]/li"));

            JavascriptExecutor executor = (JavascriptExecutor) driver;
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            executor.executeScript("arguments[0].click();", driver.findElement(By.xpath("//ul[contains(@class,'catalog-view')]/li[" + rand.nextInt(allProducts.size()) + "]/a")));

            executor.executeScript("arguments[0].click();", driver.findElement(By.id("add-to-basket")));
            executor.executeScript("arguments[0].click();", driver.findElement(new By.ByCssSelector(".basket-title")));
            logger.info("Ürün sepete eklendi .");
        }
        catch (AssertionError e){
            logger.error("Ürün sepete eklenemedi. Hata="+e.toString());
        }

        //sepetteki ürünün fiyatı ile toplam fiyatın eşitiği
        try {
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
            WebElement new_price = driver.findElement(new By.ByCssSelector(".new-price"));
            WebElement total_price = driver.findElement(new By.ByCssSelector(".total-price"));
            String n = new_price.getText();
            String t = total_price.getText();

            Assert.assertEquals(n, t);
            logger.error("Ürün sayfasındaki fiyat ile sepette yer alan ürün fiyatı aynı.");
        }catch (Exception e){
            logger.error("Ürün sayfasındaki fiyat ile sepette yer alan ürün fiyatı aynı değil. Hata="+e.toString());
        }

        //ürün adedi arttırılması
        try{
            Select drpAmount= new Select(driver.findElement(By.xpath("//select[contains(@class,amount)][1]")));
            drpAmount.selectByIndex(1);
            Assert.assertEquals(drpAmount.getFirstSelectedOption().getText(), "2" );
            logger.info("Ürünün adedi arttırıldı.");
    }
        catch (AssertionError e){
            logger.error("Ürünün adedi arttırılmadı. Hata="+e.toString());
        }

        /*try {
            List<WebElement> Sil = driver.findElements(By.xpath("//div[contains(@class,'btn-delete')]"));
        }
        catch (AssertionError e){
             logger.error("Ürünün adedi arttırılmadı. Hata="+e.toString());
        }*/
    }
}
