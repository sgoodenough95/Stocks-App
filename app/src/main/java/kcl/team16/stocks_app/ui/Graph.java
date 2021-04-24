package kcl.team16.stocks_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import kcl.team16.stocks_app.*;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import com.androidplot.xy.*;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is an Activity responsible for drawing the graph based
 * on data received from the Indicator classes
 * @author James Legge K20088859
 *
 */

public class Graph extends AppCompatActivity {

    public static final String yAxis = "yAxisValues";
    public static final String xAxis = "xAxisValues";

    private HashMap<String, Number[]> yAxisValues;
    private String[] xAxisLabels;
    private XYPlot plot;
    private int[] lineColours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        // Get the Intent that started this activity and extract the values
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        xAxisLabels = data.getStringArray(xAxis);
        yAxisValues = (HashMap<String, Number[]>) data.getSerializable(yAxis);

        lineColours = new int[]{
                Color.BLUE,
                Color.RED,
                Color.GREEN,
                Color.MAGENTA,
        };

        drawGraph();
    }

    private void drawGraph()
    {
        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);

        // array of XYSeries lines to plot.
        // length depends on amount of items selected in UI (SMA, EMA, etc)
        XYSeries[] series = new XYSeries[yAxisValues.size()];

        /*
            Iterate through each set of yAxis values in the matrix
            and instantiate a new SimpleXYSeries plot using Y axis values only
         */
        int i = 0;
        for (Map.Entry<String, Number[]> entry : yAxisValues.entrySet())
        {
            // create formatters to use for drawing a series using LineAndPointRenderer
            // and configure them from xml:
            LineAndPointFormatter series1Format = new LineAndPointFormatter(lineColours[i], lineColours[i], null, null);

            series1Format.setInterpolationParams(
                    new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

            series[i] = new SimpleXYSeries(
                    Arrays.asList(entry.getValue()),
                    SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                    entry.getKey()
            );
            plot.addSeries(series[i], series1Format);

            i++;
        }

        plot.getGraph().setPaddingBottom(100);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(xAxisLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });
    }
}