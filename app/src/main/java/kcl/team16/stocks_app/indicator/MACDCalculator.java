package kcl.team16.stocks_app.indicator;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.util.Arrays;

/**
 * This class provides Moving Average Convergence Divergence (MACD) calculation function using data
 * retrieved from the EMACalculator Class
 * @author Jack Stevenson Berg k20109732
 *
 */

public class MACDCalculator extends EMACalculator {
    private Number[] EMA12, EMA26, MACD;
    private float[] closePrices;
    private String[] dateArray;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MACDCalculator(String shareSymbol, LocalDate startDate, LocalDate endDate){
        super(shareSymbol, startDate, endDate, 12);

        EMACalculator EMACalc26 = new EMACalculator(shareSymbol, startDate, endDate, 26);
        this.EMA12 = super.getEMA();
        this.EMA26 = EMACalc26.getEMA();
        this.dateArray = super.getDateArray();
        this.closePrices = super.getClosePrices();
        this.MACD = calculateMACD(EMA12, EMA26);
    }

    public Number[] getMACD() { return MACD; }

    public float[] getClosePrices() {
        return closePrices;
    }

    public String[] getDateArray() {
        return dateArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    /**
     * Calculate the MACD of a given set of data.
     * @param EMA12 An array of float type data pulled EMACalculator.
     *  It contains the Exponential Moving Average for a period of 12.
     * @param EMA26 An array of float type data pulled EMACalculator.
     *  It contains the Exponential Moving Average for a period of 26.
     * @return float[] an array of MACD values.
     */
    public Number[] calculateMACD(Number[] EMA12, Number[] EMA26){

        if(EMA12.length != EMA26.length){
            throw new IllegalArgumentException("EMA Arrays must be of the same length.");
        }

        Number[] movingAvgConDiv = new Number[EMA12.length];

        // Performs element wise array subtraction to find MACD.
        for(int i = 0; i < EMA12.length; i++)
        {
            movingAvgConDiv[i] = (float) EMA12[i] - (float) EMA26[i];
        }

        return movingAvgConDiv;
    }

}
