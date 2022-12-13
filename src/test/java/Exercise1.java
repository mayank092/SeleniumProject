import helper.Utilities;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;

public class Exercise1 {

    ChromeDriver driver;
    String originalWindow = null;

    @BeforeTest(alwaysRun = true)
    public void setSystemProperties() {
        System.setProperty("webdriver.chrome.driver", "/Users/mayankgupta/Documents/external_Jars_And_Executables/chromedriver");
    }

    @Test
    public void FirstTest() throws IOException, InterruptedException {

        try {
            driver = new ChromeDriver();
            Utilities utils = new Utilities();
            Properties prop = utils.readPropertiesFile("test.properties");
            driver.get(prop.getProperty("link"));
            driver.manage().window().maximize();

            originalWindow = driver.getWindowHandle();

            Duration duration = Duration.ofSeconds(12);
            WebDriverWait wait = new WebDriverWait(driver, duration);

            // close default login dropdown
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='autopop__wrap autopop__wrap__new makeFlex column defaultCursor']")));
            driver.findElement(By.xpath("//li[@data-cy='account']")).click();

            //Click on Round trip
            driver.findElement(By.xpath("//li[@data-cy='oneWayTrip']")).click();

            //Enter "From" city
            driver.findElement(By.xpath("//input[@id='fromCity']")).click();
            driver.findElement(By.xpath("//input[@placeholder='From']")).sendKeys("Chennai");
            //selecting correct suggestion
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
            List<WebElement> fromCitySuggestions = driver.findElements(By.xpath("//ul[@class='react-autosuggest__suggestions-list']/li/div/div/p[1]"));
            for (WebElement element : fromCitySuggestions) {
                if (element.getText().contains("Chennai")) {
                    element.click();
                    break;
                }
            }

            //select Departure date
            driver.findElement(By.xpath("//span[@class='lbl_input latoBold appendBottom10'][1]")).click();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            SimpleDateFormat ariaLabelDateFormat = new SimpleDateFormat("MMM dd yyyy");

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 4);
            String departureDate = dateFormat.format(cal.getTime());
            String ariaLabelDepartureDate = ariaLabelDateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 4);
            String arrivalDate = dateFormat.format(cal.getTime());
            String ariaLabelArrivalDate = ariaLabelDateFormat.format(cal.getTime());
            dateFormat.format(cal.getTime());

            String departureCurrentMonthAndYear = driver.findElement(By.xpath("//div[@class='DayPicker-Month'][1]/div[@class='DayPicker-Caption']/div")).getText().trim();
            String departureCurrentMonth = departureCurrentMonthAndYear.split("(?<=\\D)(?=\\d)")[0].trim();
            String departureCurrentYear = departureCurrentMonthAndYear.split("(?<=\\D)(?=\\d)")[1].trim();

            while(!(departureCurrentMonth.equalsIgnoreCase(departureDate.split(" ")[1]) && departureCurrentYear.equalsIgnoreCase(departureDate.split(" ")[2]))) {

                driver.findElement(By.xpath("(//span[@class='DayPicker-NavButton DayPicker-NavButton--next'])")).click();
                departureCurrentMonth = driver.findElement(By.xpath("//div[@class='DayPicker-Month'][1]/div[@class='DayPicker-Caption']/div")).getText().trim().split("(?<=\\D)(?=\\d)")[0];
                departureCurrentYear = driver.findElement(By.xpath("//div[@class='DayPicker-Month'][1]/div[@class='DayPicker-Caption']/div")).getText().trim().split("(?<=\\D)(?=\\d)")[1];
            }

            boolean departureFlag = false;
            while(!departureFlag) {
                if (driver.findElements(By.xpath("//div[@class='DayPicker-Day'][contains(@aria-label,'" + ariaLabelDepartureDate + "')]")).size() == 1) {
                    driver.findElement(By.xpath("//div[@class='DayPicker-Day'][contains(@aria-label,'" + ariaLabelDepartureDate + "')]")).click();
                    departureFlag = true;
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
                }
            }


            //Enter "To" City
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@id='toCity']")));
            driver.findElement(By.xpath("//input[@id='toCity']")).click();
            driver.findElement(By.xpath("//input[@placeholder='To']")).sendKeys("New Delhi");
            //selecting correct suggestion
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
            List<WebElement> toCitySuggestions = driver.findElements(By.xpath("//ul[@class='react-autosuggest__suggestions-list']/li/div/div/p[1]"));
            for (WebElement element : toCitySuggestions) {
                if (element.getText().contains("New Delhi")) {
                    element.click();
                    break;
                }
            }


            //select return date
/*            driver.findElement(By.xpath("//div[@data-cy='returnArea']/label/span[@class='lbl_input latoBold appendBottom10']")).click();
            String arrivalCurrentMonthAndYear = driver.findElement(By.xpath("//div[@class='DayPicker-Month'][1]/div[@class='DayPicker-Caption']/div")).getText().trim();
            String arrivalCurrentMonth = arrivalCurrentMonthAndYear.split("(?<=\\D)(?=\\d)")[0];
            String arrivalCurrentYear = arrivalCurrentMonthAndYear.split("(?<=\\D)(?=\\d)")[1];

            while(!arrivalCurrentMonth.equalsIgnoreCase(arrivalDate.split(" ")[1]) || (!arrivalCurrentYear.equalsIgnoreCase(arrivalDate.split(" ")[2]))) {

                driver.findElement(By.xpath("(//span[@class='DayPicker-NavButton DayPicker-NavButton--next'])")).click();
                arrivalCurrentMonth = driver.findElement(By.xpath("//div[@class='DayPicker-Month'][1]/div[@class='DayPicker-Caption']/div")).getText().trim();
                arrivalCurrentYear = driver.findElement(By.xpath("//div[@class='DayPicker-Month'][1]/div[@class='DayPicker-Caption']/div/span")).getText().trim();
            }

            boolean arrivalFlag = false;
            while(!arrivalFlag) {
                if (driver.findElements(By.xpath("//div[@class='DayPicker-Day'][contains(@aria-label,'" + ariaLabelArrivalDate + "')]")).size() == 1) {
                    driver.findElement(By.xpath("//div[@class='DayPicker-Day'][contains(@aria-label,'" + ariaLabelArrivalDate + "')]")).click();
                    arrivalFlag = true;
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
                }
            } */

            //select travellers
            driver.findElement(By.xpath("//div[@data-cy='flightTraveller']/label/span")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[@data-cy='adults-2']")));
            driver.findElement(By.xpath("//li[@data-cy='adults-2']")).click();
            driver.findElement(By.xpath("//li[@data-cy='children-2']")).click();
            driver.findElement(By.xpath("//li[@data-cy='infants-1']")).click();
            driver.findElement(By.xpath("//button[@class='primaryBtn btnApply pushRight']")).click();
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='primaryBtn font24 latoBold widgetSearchBtn ']")));

            // click Search button
            driver.findElement(By.xpath("//a[@class='primaryBtn font24 latoBold widgetSearchBtn ']")).click();

            //closing popup window
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='bgProperties icon20 overlayCrossIcon']")));
            driver.findElement(By.xpath("//span[@class='bgProperties icon20 overlayCrossIcon']")).click();

            /*originalWindow = driver.getWindowHandle();
            Set<String> set =driver.getWindowHandles();
            Iterator<String> itr= set.iterator();
            while(itr.hasNext()){
                String childWindow = itr.next();
                if(!originalWindow.equals(childWindow)){
                    driver.switchTo().window(childWindow);
                }
            }
            driver.switchTo().window(originalWindow);*/

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='premEcon']")));


            //sort by Price Descending
            driver.findElement(By.xpath("//div[@class='make-flex sort-price']"));

            //selecting second highest price tag
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='fli-list  simpleow '][2]")));
            driver.findElement(By.xpath("//div[@class='fli-list  simpleow '][2]/div[@class='listingCard']/div[@class='makeFlex simpleow']/div[@class='priceSection']/div/button")).click();

            //clicking on Book Now
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='collapse show']")));
            driver.findElement(By.xpath("//div[@class='fli-list  simpleow '][2]/div[@class='collapse show']/div/div[@class='viewFareRowWrap'][1]/div//div[@class='viewFareBtnCol ']/button")).click();

            Set<String> handles=driver.getWindowHandles();
            for(String actual: handles) {
                if (!actual.equalsIgnoreCase(originalWindow)) {
                    //Switch to the new tab
                    driver.switchTo().window(actual);
                }
            }

            //print total amount
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='fareRow']")));
            System.out.println("Second Highest Fare Total Amount: "+ driver.findElement(By.xpath("//p[@class='fareRow']/span[2]")).getText().trim());

        } catch(NoSuchElementException e){System.out.println(e);}
        finally{
            driver.close();
            driver.switchTo().window(originalWindow);
            driver.close();
            driver.quit();
        }
    }
}
