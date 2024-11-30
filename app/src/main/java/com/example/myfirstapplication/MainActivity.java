package com.example.myfirstapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {
    private static final String validExpression = "^(((-?0|-?[1-9][0-9]*)([.][0-9]+)?)[\\+\\-\\*/])+((-?0|-?[1-9][0-9]*)([.][0-9]+)?)$";
    private static final String validNum = "^(-?0|-?[1-9][0-9]*)([.][0-9]+)?$";
    TextView result;
    TextView currentResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        result = findViewById(R.id.TextViewResult);
        result.setText("");
        currentResult = findViewById(R.id.textViewCurrentResult);
        currentResult.setText("0");


    }

    public void getNum(View view) {
        Button button = (Button) view;
        String dig = button.getText().toString();

        String text = result.getText().toString(); // Current text in the result TextView
         // The digit from the pressed button
        text = text + dig;
        if(isValidDouble(text) || isValidExpression(text)){
            Double num = CalculateExpression.finalCalc(text);
            currentResult.setText(maybeInteger(num));
        }
        else{
            text = text.substring(0,text.length()-2)+dig;
        }
        result.setText(text);
    }

    public void getOperator(View view) {

        Button b = (Button) view;
        char currOp = b.getText().charAt(0);
        String text = result.getText().toString() + currOp;
        text = text.replace("--","+");
        String check = text  + "5";

        if(isValidExpression(check))
            result.setText(text);
    }


    public void getDot(View view) {
        String text = result.getText().toString() + ".";
        String check = text + "0";

        if(isValidExpression(check) || isValidDouble(check))
            result.setText(text);
    }

    public void getEqual(View view) {
        result.setText(currentResult.getText().toString());
    }

    public void getC(View view) {
        String text = result.getText().toString();
        if(text.isEmpty()) return;
        text = text.substring(0,text.length()-1);
        result.setText(text);
        if(text.isEmpty() || isValidDouble(text) || isValidExpression(text)){
            Double num = CalculateExpression.finalCalc(text);
            currentResult.setText(maybeInteger(num));
        }
    }
    public void getCE(View view) {
        result.setText("");
        currentResult.setText("0");
    }


    public static boolean isValidExpression(String expression) {
        return expression.matches(validExpression);
    }
    public boolean isValidDouble(String str) {
        return str.matches(validNum);
    }

    private String maybeInteger(Double num){
        if(num == null) return "∞";
        String text = num.toString();
        double value = Double.parseDouble(text);
        if(value == (int) value)
            text = String.valueOf((int) value);
        else{
            text = String.valueOf(Math.round(value * 10000000.0) / 10000000.0);
        }
        return text;
    }

}