package kcl.team16.stocks_app.indicator;

import android.os.Build;
import androidx.annotation.RequiresApi;
import java.time.LocalDate;
import java.util.Arrays;

/**
 * This class provides MACDAVG (AVGMAC) - "siganl line" - calculation function using data retrieved
 * from the MACD, EMA, and SMA calculator classes.
 * @author Sam Goodenough K20106646
 *
 */

public class AVGMACDCalculator {

    private Number[] SMA9, MACD, AVGMACD;
    private float[] closePrices;
    private String[] dateArray;
    private int period = 9, interval;

    public AVGMACDCalculator(){}
    @RequiresApi(api = Build.VERSION_CODES.O)
    public AVGMACDCalculator(String shareSymbol, LocalDate startDate, LocalDate endDate, int period) {

        SMACalculator SMACalc9 = new SMACalculator(shareSymbol, startDate, endDate, 9);
        MACDCalculator MACDCalc = new MACDCalculator(shareSymbol, startDate, endDate);
        this.SMA9 = SMACalc9.getSMA();
        this.MACD = MACDCalc.getMACD();
        this.dateArray = SMACalc9.getDateArray();
        this.closePrices = SMACalc9.getClosePrices();
        this.interval = closePrices.length;
        this.AVGMACD = calculateAVGMACD(SMA9, MACD);
    }

    public Number[] getAVGMACD() { return AVGMACD; }

    public String[] getDateArray() { return dateArray; }

    public float[] getClosePrices() { return closePrices; }

    /**
     * Calculate the EMA of a given set of data.
     * @param SMA9 An array of float type data pulled from SMACalculator. It contains the Simple Moving Average for a period.
     * @param MACD The array of float type data pulled from MACDCalculator. It contains the MACD calculation for a period.
     * @return Number[] an array of outputted values for MACDAVG.
     */

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Number[] calculateAVGMACD(Number[] SMA9, Number[] MACD) {

        int smoothing = 2;
        float multiplier = (float) smoothing /(period + 1);
        Number[] avgMacdOutput = new Number[interval];

        avgMacdOutput[0] = SMA9[SMA9.length - 1];

        for(int i = 1; i < interval; i++) {
            float currentPrice = (float) MACD[i];
            float previousDayMACD = (float) MACD[i-1];
            float SMA = (currentPrice * multiplier) + previousDayMACD * (1 - multiplier);
            avgMacdOutput[i] = SMA;
        }

        System.out.println("The MACDAVG is " + Arrays.toString(avgMacdOutput));
        return avgMacdOutput;
    }

    /**
     * A simple test.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) {
        int period=2;
        LocalDate date1 = LocalDate.of(2020, 4, 20);
        LocalDate date2 = LocalDate.of(2020, 4, 27);

        AVGMACDCalculator avgMacd = new AVGMACDCalculator("TSLA",date1,date2,period);

        System.out.println("Printing close prices:");
        float[] closePrices = avgMacd.getClosePrices();
        for (int i = 0; i < closePrices.length; i++)
        {
            System.out.print(closePrices[i]+" ");
        }
        System.out.println();

        System.out.println("Printing date array:");
        String[] dateArray = avgMacd.getDateArray();
        for (int i = 0; i < dateArray.length; i++)
        {
            System.out.print(dateArray[i]+" ");
        }
        System.out.println();

        System.out.println("Printing AVGMACD array:");
        Number[] result = avgMacd.getAVGMACD();
        for (int i = 0; i < result.length; i++)
        {
            System.out.printf("%.2f ", result[i]);
        }
        System.out.println();

    }

}
