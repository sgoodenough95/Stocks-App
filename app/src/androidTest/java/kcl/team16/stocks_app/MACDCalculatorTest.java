package kcl.team16.stocks_app;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.number.IsCloseTo;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.InvalidParameterException;
import java.time.LocalDate;

import kcl.team16.stocks_app.indicator.MACDCalculator;
import kcl.team16.stocks_app.indicator.SMACalculator;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MACDCalculatorTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("kcl.team16.stocks_app", appContext.getPackageName());
    }

    @Test
    public void MACDValueTest()
    {
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 27);
        MACDCalculator macd = new MACDCalculator("TSLA", date1, date2);


        Number macdArray[]={(float) 15.611259, (float) 15.500557, (float) 15.961342,
                (float) 15.717987, (float) 15.65963, (float) 16.609695};
        assertArrayEquals(macd.getMACD(), macdArray);

    }

    @Test
    public void MACDCdateArrayTest()
    {
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 27);
        MACDCalculator macd = new MACDCalculator("TSLA", date1, date2);

        String dateArray[]={"2020-04-20", "2020-04-21", "2020-04-22", "2020-04-23", "2020-04-24",
                "2020-04-27"};
        assertArrayEquals(macd.getDateArray(), dateArray);

    }

    @Test
    public void MACDCgetclosepricesTest()
    {
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 27);
        MACDCalculator macd = new MACDCalculator("TSLA", date1, date2);


        float[] prices={(float)149.272,(float)137.344,(float)146.422,(float)141.126,(float)145.03,
                (float)159.75};
        assertArrayEquals(macd.getClosePrices(), prices, 0);

    }

    @Test
    public void calculateMACDTest()
    {
        Number EMA12[] = {10.0f, 11.0f, 12.0f};
        Number EMA26[] = {5.0f, 5.0f, 5.0f};

        Number answer[] = {5.0f, 6.0f, 7.0f};

        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 27);
        MACDCalculator result = new MACDCalculator("TSLA", date1, date2);
        Number macd[] = result.calculateMACD(EMA12, EMA26);

        assertArrayEquals(macd, answer);
    }

    @Test
    public void MACDnegValueTest()
    {
        Number EMA12[] = {5.0f, 5.0f, 5.0f};
        Number EMA26[] = {10.0f, 11.0f, 12.0f};

        Number answer[] = {-5.0f,-6.0f, -7.0f};

        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 27);
        MACDCalculator result = new MACDCalculator("TSLA", date1, date2);
        Number macd[] = result.calculateMACD(EMA12, EMA26);

        assertArrayEquals(macd, answer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void EMAarrayLengthTest()
    {
        Number EMA12[] = {5.0f, 5.0f, 5.0f};
        Number EMA26[] = {10.0f, 11.0f, 12.0f, 12.0f};

        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 27);
        MACDCalculator result = new MACDCalculator("TSLA", date1, date2);
        Number macd[] = result.calculateMACD(EMA12, EMA26);

    }

}
