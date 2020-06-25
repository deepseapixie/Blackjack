package com.example.blackjack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    /* Create variables for widgets */
    private TextView availBalanceText;
    private TextView warningTextView;
    private EditText moneyEditText;
    private Button addMoneyButton;
    private Button removeMoneyButton;
    private Button startGameButton;

    /* Declare variable for SharedPreference object */
    private SharedPreferences savedValues;

    /* Declare variable for bank account */
    private int availableBalance = 0;
    private final int FORMAT_LIMIT = 1_000_000;

    /* Event listener for when the money amount is entered */
    private OnEditorActionListener moneyEditTextListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_UNSPECIFIED ||
                    actionId == EditorInfo.IME_ACTION_NEXT)
            {
                if (availableBalance <= 0)
                {
                    warningTextView.setText(R.string.addLessThanTextWarning);
                }
                else {
                    warningTextView.setText(R.string.addRemoveMoneyGeneral);
                }
            }
            return false;
        }
    };

    /* Event listener for when the Add Money Button is pressed */
    private OnClickListener addMoneyListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            /* Get the money from the moneyEditText widget */
            int money = Integer.parseInt(moneyEditText.getText().toString());
            /* Add the money to the available balance */
            availableBalance += money;
            /* Format the available balance */
            formatMoney();
            /* Reset the warning TextView */
            warningTextView.setText(R.string.warningDefault);
        }
    };

    private OnClickListener removeMoneyListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            /* Get the money from the moneyEditText widget */
            int money = Integer.parseInt(moneyEditText.getText().toString());
            /* remove the money but if the money being removed is greater than what is available,
            *  then set balance to zero */
            if (money > availableBalance)
            {
                availableBalance = 0;
            }
            else {
                availableBalance -= money;
            }
            /* Format the available balance */
            formatMoney();
            /* Reset the warning TextView */
            warningTextView.setText(R.string.warningDefault);
        }
    };

    private OnClickListener startGameListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), BlackjackActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Get the references for each widget */
        availBalanceText = (TextView) findViewById(R.id.availBalanceText);
        warningTextView = (TextView) findViewById(R.id.warningTextView);
        moneyEditText = (EditText) findViewById(R.id.moneyEditText);
        addMoneyButton = (Button) findViewById(R.id.addMoneyButton);
        removeMoneyButton = (Button) findViewById(R.id.removeMoneyButton);
        startGameButton = (Button) findViewById(R.id.startGameButton);

        /* Set widget listeners */
        moneyEditText.setOnEditorActionListener(moneyEditTextListener);
        addMoneyButton.setOnClickListener(addMoneyListener);
        removeMoneyButton.setOnClickListener(removeMoneyListener);
        startGameButton.setOnClickListener(startGameListener);
    }

    private void formatMoney() {
        /* Create a NumberFormat object to format the money less than $999,999 */
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        /* Case: balance is in the negatives but greater than -1_000_000 */
        if (availableBalance < 0 && availableBalance > -(FORMAT_LIMIT))
        {
            availBalanceText.setText(String.format("-%s", currency.format(availableBalance)
                    .toString().substring(
                            0,
                            currency.format(availableBalance).toString().length() - 3)));
        }
        /* Case: balance is in the negative and  */
        else if (availableBalance < 0 && availableBalance < -(FORMAT_LIMIT))
        {
            double shortenedBalance = (double) availableBalance / FORMAT_LIMIT;
            availBalanceText.setText("-$" + shortenedBalance + "M");
        }
        else if (availableBalance > 0 && availableBalance < FORMAT_LIMIT)
        {
            availBalanceText.setText(String.format("%s", currency.format(availableBalance)
                    .toString().substring(
                            0,
                            currency.format(availableBalance).toString().length() - 3)));
        }
        else if (availableBalance > 0 && availableBalance >= FORMAT_LIMIT)
        {
            double shortenedBalance = (double) availableBalance / FORMAT_LIMIT;
            availBalanceText.setText("$" + shortenedBalance + "M");
        }
        else if (availableBalance == 0)
        {
            availBalanceText.setText(R.string.default_balance);
        }
    }
}
