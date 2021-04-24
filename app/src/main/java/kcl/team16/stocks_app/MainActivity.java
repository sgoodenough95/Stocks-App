package kcl.team16.stocks_app;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.time.LocalDate;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Date;
import java.util.Hashtable;

import kcl.team16.stocks_app.ui.Graph;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lookForAction();
    }

    /**
     * On click of Search button this method collects, formats and sends user data to
     * CalculationHelper class.
     */
    public void lookForAction() {

        // Creates variables of type checkbox for all UI checkboxes.
        CheckBox SMA_box = (CheckBox) findViewById(R.id.boxSMA);
        CheckBox EMA_box = (CheckBox) findViewById(R.id.boxEMA);
        CheckBox MACD_box = (CheckBox) findViewById(R.id.boxMACD);
        CheckBox AVGMACD_box = (CheckBox) findViewById(R.id.boxAVGMACD);

        // Creates variable of type button for Search.
        Button btnSearch = (Button) findViewById(R.id.btnSearch);

        // Creates variable of type editable text for stockName.
        EditText stockName = (EditText)findViewById(R.id.stockName);

        // Creates variable of type date picker text for both start and end dates.
        DatePicker dpstartDate =(DatePicker)findViewById(R.id.dpStartDate);
        DatePicker dpendDate =(DatePicker)findViewById(R.id.dpEndDate);

        Activity a = this;

        // Waits for user to click "Search" then executes code in onClick().
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {


                // Retrieves user requested stock name.
                String shareSymbol = stockName.getText().toString();

                // Retrieving user specified dates from date spinners.
                LocalDate startDate = LocalDate.of(dpstartDate.getYear(),
                        dpstartDate.getMonth() + 1, dpstartDate.getDayOfMonth());
                LocalDate endDate = LocalDate.of(dpendDate.getYear(),
                        dpendDate.getMonth() + 1, dpendDate.getDayOfMonth());

                // Checks status of all checkboxes and sets dictionary entry.
                boolean isSMAChecked = SMA_box.isChecked();
                boolean isEMAChecked = EMA_box.isChecked();
                boolean isMACDChecked = MACD_box.isChecked();
                boolean isAVGMACDChecked = AVGMACD_box.isChecked();

                // Make sure that the endDate is after the startDate
                if (startDate.isAfter(endDate))
                {
                    showErrorDialog("End Date can not be before Start Date");
                    return;
                }

                // Make sure the endDate is not in the future
                if (endDate.isAfter(LocalDate.now()))
                {
                    showErrorDialog("End date can not be in the future");
                    return;
                }

                // Show an error message if the user does not enter a Ticker
                if (shareSymbol.equals(""))
                {
                    showErrorDialog("Please enter a Ticker");
                    return; // return from the function and do not attempt calculations
                }

                // Show an error message if the user does not select any checkboxes
                if (!isSMAChecked &&
                        !isEMAChecked &&
                        !isMACDChecked &&
                        !isAVGMACDChecked
                )
                {
                    showErrorDialog("Please select either SMA, EMA, MACD or MACDAVG");
                    return; // return from the function and do not attempt calculations
                }

                // Period value set for calculations that do not specify otherwise.
                int period = 12;

                // Creates dictionary for storing operation selection.
                Hashtable<String, Boolean> operationDict = new Hashtable<String, Boolean>();


                operationDict.put("MACD", isMACDChecked);
                operationDict.put("EMA", isEMAChecked);
                operationDict.put("AVGMACD", isAVGMACDChecked);
                operationDict.put("SMA",  isSMAChecked);

                // Passes user selections to calculationHelper class.
                try {
                    new CalculationHelper(a, operationDict, shareSymbol, startDate, endDate, period).Calculate();

                }
                catch (NegativeArraySizeException e)
                {
                    showErrorDialog(
                            "Error while retrieving stock information, please check the ticker and try again"
                    );
                }

            }

        });



    }

    private void showErrorDialog(String msg)
    {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(msg)

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}