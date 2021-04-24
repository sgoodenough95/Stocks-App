package kcl.team16.stocks_app.indicator;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import android.os.Build;
import androidx.annotation.RequiresApi;

/**
 * This class provides Exponential Moving Average (EMA) calculation function using data retrieved from the SMACalculator Class
 * @author Abdulsamad Usman K20097871
 *
 */

public class EMACalculator extends SMACalculator{
    private final float[] closePrices;
    private final String[] dateArray;
    private Number[] EMA;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public EMACalculator(String shareSymbol, LocalDate startDate, LocalDate endDate, int period)
    {
        super(shareSymbol, startDate.minusDays(period), startDate, period);
        SMACalculator SMACalc2 = new SMACalculator(shareSymbol, startDate, endDate, period);
        
        this.closePrices = SMACalc2.getClosePrices();

        this.interval = closePrices.length;

        this.dateArray = SMACalc2.getDateArray();
        this.EMA = calculateEMA(super.getSMA(), closePrices, closePrices.length, period);
    }

    public Number[] getEMA() {
        return EMA;
    }

    public float[] getClosePrices() {
        return closePrices;
    }

    public String[] getDateArray() {
        return dateArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    /**
     * Calculate the EMA of a given set of data.
     * @param SMA An array of float type data pulled from SMACalculator. It contains the Simple Moving Average for a period.
     * @param closePrices The array of float type data pulled from SMACalculator. It contains the close prices for a period.
     * @param interval An int value which describes the period (number of data) as specified by the user.
     * @param period An int value which describes the period (number of data) by which each average value is calculated.
     * @return Number[] an array of EMA values.
     */
    private Number[] calculateEMA(Number[] SMA, float[] closePrices, int interval, int period) {
        int smoothing = 2;
        float multiplier = (float) smoothing /(period + 1);
        Number[] exponentialMovingAverages = new Number[interval];

        exponentialMovingAverages[0] = SMA[SMA.length - 1];

        for(int i = 1; i < interval; i++) {
            float currentPrice = closePrices[i];
            float previousDayEMA = (float) exponentialMovingAverages[i - 1];
            float EMA = (currentPrice * multiplier) + previousDayEMA * (1 - multiplier);
            exponentialMovingAverages[i] = EMA;
        }

        System.out.println("The EMA is " + Arrays.toString(exponentialMovingAverages));
        return exponentialMovingAverages;
    }


    /**
     * A simple test.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) {
        int period=2;
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 27);

        EMACalculator ema = new EMACalculator("AAPL",date1,date2,period);

        System.out.println("Printing close prices:");
        float[] closePrices = ema.getClosePrices();
        for (int i = 0; i < closePrices.length; i++)
        {
            System.out.print(closePrices[i]+" ");
        }
        System.out.println();

        System.out.println("Printing date array:");
        String[] dateArray= ema.getDateArray();
        for (int i = 0; i < dateArray.length; i++)
        {
            System.out.print(dateArray[i]+" ");
        }
        System.out.println();

        System.out.println("Printing EMA array:");
        Number[] result= ema.getEMA();
        for (int i = 0; i < result.length; i++)
        {
            System.out.printf("%.2f ", result[i]);
        }
        System.out.println();

    }

}




