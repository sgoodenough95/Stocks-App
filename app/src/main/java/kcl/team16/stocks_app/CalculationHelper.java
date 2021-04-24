package kcl.team16.stocks_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import kcl.team16.stocks_app.indicator.*;
import kcl.team16.stocks_app.ui.Graph;

public class CalculationHelper {

    // Sets attributes of class CalculationHelper.
    private Number[] smaValues, emaValues, macdValues, avgMacdValues;
    private Hashtable<String, Boolean> toCalculate;
    private String shareSymbol;
    private String[] emaXaxis, smaXaxis, macdXaxis, avgMacdXaxis;
    private LocalDate startDate, endDate;
    private int period;
    private Activity parent;

    public CalculationHelper(Activity a, Hashtable<String, Boolean> toCalculate, String shareSymbol,
                             LocalDate startDate, LocalDate endDate, int period)
    {
        // Sets objects attributes to incoming values from mainActivity.
        this.toCalculate = toCalculate;
        this.shareSymbol = shareSymbol;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = period;
        this.parent = a;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Calculate() {
        HashMap<String, Number[]> yAxis = new HashMap<>();

        String[] xAxis = {""};

        // Executes selected operations and sends to Graph class.
        if (toCalculate.get("SMA")) {
            SMACalculator smaCalc = new SMACalculator(shareSymbol, startDate, endDate, period);
            smaValues = smaCalc.getSMA();
            smaXaxis = smaCalc.getDateArray();

            // add the line to the yAxis values of the Graph
            yAxis.put("SMA", smaValues);
            xAxis = smaXaxis;
        }
        if (toCalculate.get("EMA")) {
            EMACalculator emaCalc = new EMACalculator(shareSymbol, startDate, endDate, period);
            emaValues = emaCalc.getEMA();
            emaXaxis = emaCalc.getDateArray();
            // send result to Graph result.

            yAxis.put("EMA", emaValues);
            xAxis = emaXaxis;
        }
        if (toCalculate.get("MACD")) {
            MACDCalculator macdCalc = new MACDCalculator(shareSymbol, startDate, endDate);
            macdValues = macdCalc.getMACD();
            macdXaxis = macdCalc.getDateArray();
            // send result to Graph result.

            yAxis.put("MACD", macdValues);
            xAxis = macdXaxis;
        }
        if (toCalculate.get("AVGMACD")) {
            AVGMACDCalculator avgMacdCalc = new AVGMACDCalculator(shareSymbol, startDate, endDate, period);
            avgMacdValues = avgMacdCalc.getAVGMACD();
            avgMacdXaxis = avgMacdCalc.getDateArray();
            // calculate AVGMACD
            // send result to Graph result.

            yAxis.put("AVGMACD", avgMacdValues);
            xAxis = avgMacdXaxis;
        }

            Intent myintent = new Intent(parent.getApplicationContext(), Graph.class);
            Bundle b = new Bundle();

            b.putSerializable(Graph.yAxis, (Serializable) yAxis);
            b.putStringArray(Graph.xAxis, xAxis);
            myintent.putExtras(b);
            parent.startActivity(myintent);
    }
}
