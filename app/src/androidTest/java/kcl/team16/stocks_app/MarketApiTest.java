package kcl.team16.stocks_app;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MarketApiTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("kcl.team16.stocks_app", appContext.getPackageName());
    }

    @Test
    public void correctURL() throws IOException, JSONException {
        String s = MarketApi.getInstance().getMarketData("https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v3/get-historical-data?symbol=AAPL&region=US");
        assertNotEquals("",s);
    }

    @Test
    public void incorrectURL() throws IOException, JSONException {
        String s = MarketApi.getInstance().getMarketData("https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v3/get-historical-data?symbol=FAKETICKER&region=US");
        assertEquals("",s);
    }
}
