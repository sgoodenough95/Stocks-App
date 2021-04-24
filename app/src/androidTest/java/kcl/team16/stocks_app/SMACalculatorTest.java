package kcl.team16.stocks_app;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.InvalidParameterException;
import java.time.LocalDate;

import kcl.team16.stocks_app.indicator.SMACalculator;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SMACalculatorTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("kcl.team16.stocks_app", appContext.getPackageName());
    }

    @Test
    public void getSMATest()
    {
        int period=12;
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 24);
        SMACalculator sma=new SMACalculator("AAPL",date1,date2,period);
        
        Number[] smaArray ={(float)67.363541,(float)67.851875,(float)68.574593,(float)68.836258,(float)69.326675};
        assertArrayEquals(smaArray,sma.getSMA());
    }
    @Test
    public void getDateArrayTest()
    {
        int period=12;
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 24);
        SMACalculator sma=new SMACalculator("AAPL",date1,date2,period);

        String[] dateArray ={"2020-04-20","2020-04-21","2020-04-22","2020-04-23","2020-04-24"};
        assertArrayEquals(dateArray,sma.getDateArray());
    }
    @Test
    public void getClosePricesTest()
    {
        int period=12;
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 24);
        SMACalculator sma=new SMACalculator("AAPL",date1,date2,period);

        float[] closePricesArray ={(float)69.2325,(float)67.0925,(float)69.025,(float)68.7575,(float)70.7425};
        assertArrayEquals(closePricesArray,sma.getClosePrices(),0);
    }

    @Test
    public void stressTest() {
        int period = 12;
        LocalDate date1 = LocalDate.of(2018, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 24);

        SMACalculator sma = new SMACalculator("TSLA", date1, date2, period);

        // No assert statement needed as we are simply testing for crashes
        sma.getClosePrices();
    }

    @Test (expected = InvalidParameterException.class)
    public void invalidDateRange()
    {
        int period=12;
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2019, 4, 24);
        SMACalculator sma=new SMACalculator("AAPL",date1,date2,period);

        sma.getClosePrices();
    }

    @Test (expected = InvalidParameterException.class)
    public void endDateInFuture()
    {
        int period=12;
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2025, 4, 24);
        SMACalculator sma=new SMACalculator("AAPL",date1,date2,period);

        sma.getClosePrices();
    }

}
