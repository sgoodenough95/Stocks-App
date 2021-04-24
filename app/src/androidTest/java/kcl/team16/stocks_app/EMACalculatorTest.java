package kcl.team16.stocks_app;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;

import kcl.team16.stocks_app.indicator.SMACalculator;
import kcl.team16.stocks_app.indicator.EMACalculator;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class EMACalculatorTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("kcl.team16.stocks_app", appContext.getPackageName());
    }

    @Test
    public void getEMATest()
    {
        int period=12;
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 24);
        EMACalculator ema = new EMACalculator("AAPL",date1,date2,period);

        Number emaArray[]={(float)67.363541,(float)67.321846,(float)67.583870,(float)67.764427,(float)68.222595};
        assertArrayEquals(emaArray,ema.getEMA());
    }
    @Test
    public void getDateArrayTest()
    {
        int period=12;
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 24);
        EMACalculator ema = new EMACalculator("AAPL",date1,date2,period);

        String dateArray[]={"2020-04-20","2020-04-21","2020-04-22","2020-04-23","2020-04-24"};
        assertArrayEquals(dateArray,ema.getDateArray());
    }
    @Test
    public void getClosePricesTest()
    {
        int period=12;
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 24);
        EMACalculator ema = new EMACalculator("AAPL",date1,date2,period);

        float closePricesArray[]={(float)69.2325,(float)67.0925,(float)69.025,(float)68.7575,(float)70.7425};
        assertArrayEquals(closePricesArray,ema.getClosePrices(),0);
    }

}
