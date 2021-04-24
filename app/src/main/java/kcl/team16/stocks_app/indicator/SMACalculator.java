package kcl.team16.stocks_app.indicator;


import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.time.*;

import android.os.Build;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.RequiresApi;
import kcl.team16.stocks_app.MarketApi;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


/**
 * This class provides Simple Moving Average (SMA) calculation function using data retrieved through API.
 * It also parses the .json response from the API for SMA and other indicators.
 * @author Haotian Sun
 *
 */
public class SMACalculator
{
    private int period;
    protected int interval;
    private LocalDate startDate,endDate,actualStartDate;
    private String shareSymbol;
    private Number[] sma;
    private float [] closePrices;
    private String[] dateArray;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    @RequiresApi(api = Build.VERSION_CODES.O)
    /**
     * Thr constructor of SMACalculator class which perform data retrieving and calculation automatically.
     * @param shareSymbol The symbol of the share specified by the user.
     * @param startDate The start date specified by the user.
     * @param endDate The end date specified by the user.
     * @param period The period chosen by the user to calculate SMA.
     */
    public SMACalculator(String shareSymbol, LocalDate startDate, LocalDate endDate, int period)
    {

        this.shareSymbol=shareSymbol;
        this.startDate=startDate;
        this.endDate=endDate;

        if (startDate.isAfter(endDate))
        {
            throw new InvalidParameterException("Start date can not be after end date");
        }

        if (endDate.isAfter(LocalDate.now()))
        {
            throw new InvalidParameterException("End date can not be in the future");
        }

        this.period=period;
        this.actualStartDate=getActualStartDate(startDate,period);
        this.closePrices=pullData(shareSymbol,actualStartDate,endDate,period);
        this.interval=closePrices.length-period+1;
        this.sma=calcSMA(closePrices,interval,period);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    /**
     * Convert the user-specified start date to the date that is actually needed to start retrieve data from.
     * @param shareSymbol The symbol of the share specified by the user.
     * @param startDate The start date specified by the user.
     * @return LocalDate the actual start date for data retrieving.
     */
    private LocalDate getActualStartDate(LocalDate start, int period)
    {
        int extraDataNeeded=period-1;
        LocalDate actualStart=start;
        while (extraDataNeeded>0)
        {
            actualStart=actualStart.minusDays(1);
            if (actualStart.getDayOfWeek() != DayOfWeek.SATURDAY &&
                    actualStart.getDayOfWeek() != DayOfWeek.SUNDAY)
            {
                extraDataNeeded--;
            }

        }
        return actualStart;
    }

    /**
     * The getter method for SMA values.
     * @return float[] an array of SMA values.
     */
    public Number[] getSMA()
    {
        return sma;
    }

    /**
     * A method to retrieve the share prices in the user specified time interval.
     * @return float[] an array of share prices.
     */
    public float[] getClosePrices()
    {
        float[] intervalClosePrices=new float[interval];
        for(int i=0;i<interval;i++)
        {
            intervalClosePrices[i]=closePrices[i+period-1];
        }
        return intervalClosePrices;
    }

    /**
     * The getter method for date array to provide x-axis values for the graph.
     * @return String[] an array of date that have price and indicator data.
     */
    public String[] getDateArray() {return dateArray;}

    @RequiresApi(api = Build.VERSION_CODES.O)
    /**
     * Calculate the SMA of a given set of data.
     * @param input The array of float type data pulled from API. Its length is interval+period-1.
     * @param interval An int value. The length of the time interval specified by the user with two dates.
     * @param period An int value which describes the period (number of data) by which each average value is calculated.
     * @return float[] an array of SMA values.
     */
    private Number[] calcSMA(float[] input, int interval, int period)
    {
        float sum=0;
        Number[] sma=new Number[interval];

        for (int i=0;i<period;i++)
            sum=sum+input[i];
        int index=0;
        sma[index]=sum/period;
        for (int i=period;i<interval+period-1;i++)
        {
            index++;
            sum=sum-input[i-period]+input[i];
            sma[index]=sum/period;
        }
        System.out.println();
        return sma;
    }

    /**
     * Send request through the API and parse the .json response to retrieve data needed for indicator calculation.
     * @param shareSymbol The symbol of the share specified by the user.
     * @param startDate The start date to retrieve data. Obtained through getActualStartDate()
     * @param endDate The end date specified by the user.
     * @param period The period chosen by the user to calculate SMA.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private float[] pullData(String shareSymbol, LocalDate startDate, LocalDate endDate, int period)
    {
        String url="https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-historical-data?period1=START_DATE&period2=END_DATE&symbol=SHARE_SYMBOL&frequency=1d&filter=history";

        ZoneId zoneId = ZoneId.of("GMT");
        long intervalStartEpochSecond=this.startDate.atStartOfDay(zoneId).toEpochSecond();
        url=url.replace("SHARE_SYMBOL",shareSymbol);
        url=url.replace("START_DATE",String.valueOf(startDate.minusDays(30).atStartOfDay(zoneId).toEpochSecond()));
        url=url.replace("END_DATE",String.valueOf(endDate.plusDays(1).atStartOfDay(zoneId).toEpochSecond()));
        float[] closePrices = new float[0];

        try
        {
            String response=MarketApi.getInstance().getMarketData(url);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(String.valueOf(response), JsonObject.class);

            JsonArray dataJsonArray = jsonObject.get("prices").getAsJsonArray();
            JsonObject dataJsonObject;
            int size=dataJsonArray.size();
            ArrayList<Float> closePricesList=new ArrayList<Float>();
            ArrayList<String> dateArrayList=new ArrayList<String>();
            int extraDataNeeded=period-1;
            for (int i=0;i<size;i++)
            {
                dataJsonObject=dataJsonArray.get(i).getAsJsonObject();
                long epochSecond=dataJsonObject.get("date").getAsLong();

                if (extraDataNeeded==0 && epochSecond<intervalStartEpochSecond)
                    break;
                if (dataJsonObject.get("close")!=null)
                {
                    closePricesList.add(0,dataJsonObject.get("close").getAsFloat());
                }
                else
                    continue;
                if (epochSecond>intervalStartEpochSecond)
                {
                    Date date = new Date(epochSecond*1000);
                    dateArrayList.add(0,dateFormat.format(date));
                }
                else
                {
                    extraDataNeeded--;
                }
            }
            dateArray=dateArrayList.toArray(new String[dateArrayList.size()]);
            closePrices=new float[closePricesList.size()];
            for(int i=0;i<closePricesList.size();i++)
            {
                closePrices[i]=closePricesList.get(i);
            }

        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        return closePrices;
    }
}
