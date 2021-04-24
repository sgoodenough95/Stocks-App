package kcl.team16.stocks_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.test.filters.MediumTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import kcl.team16.stocks_app.ui.Graph;

import static org.junit.Assert.*;

@MediumTest
@RunWith(AndroidJUnit4.class)
public class TestGraphing
{
    @Rule
    public ActivityTestRule<Graph> rule = new ActivityTestRule<Graph>(
            Graph.class,
            true,
            false
    );

    @Test
    public void testCreateGraph()
    {
        Number[][] yAxis = {{4.7, 2.4, 9.3}, {20.2, 12.5, 30.0}};
        String[] xAxisLabels = {"2020-04-15", "2021-01-08", "2021-02-05"};
        Intent intent = new Intent(Intent.ACTION_MAIN);

        Bundle b = new Bundle();
        b.putSerializable(Graph.yAxis, yAxis);
        b.putStringArray(Graph.xAxis, xAxisLabels);
        intent.putExtras(b);

        rule.launchActivity(intent);
    }
}
