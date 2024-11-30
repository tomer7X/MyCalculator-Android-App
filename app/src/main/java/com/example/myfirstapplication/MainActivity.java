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

    TextView result;
    Double num;
    boolean isCalc = false;
    boolean op_locked = false;
    View lastButton = null;
    char op = '$';
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
        System.out.println(CalculateExpression.finalCalc("1+5+6"));



    }

    public void getNum(View view) {
        op_locked = false;
        if (lastButton != null)
            lastButton.setBackgroundResource(R.drawable.buttomdesign);
        if (isCalc) {
            result.setText(""); // Clear result if starting a new calculation
            isCalc = false;
        }

        String text = result.getText().toString(); // Current text in the result TextView
        Button button = (Button) view;
        String dig = button.getText().toString(); // The digit from the pressed button

        if (text.equals("0") || text.equals("-0"))
            text = dig.equals("0") ? text : (text.equals("0") ? dig : "-" + dig);
        else text += dig;

        result.setText(text); // Update the TextView with the new text
    }
//8+9*
    public void getOperator(View view) {
        if(op_locked) return;
        Button b = (Button) view;
        char currOp = b.getText().charAt(0);
        String text = result.getText().toString();
        if (text.isEmpty()){
            if(currOp == '-') result.setText("-");
            return;
        }
        if (isValidDouble(text)){
            operButtomPress(view);
            eval(text);
            op = currOp;
        }
    }


    public void getDot(View view) {
        if (lastButton != null)
            lastButton.setBackgroundResource(R.drawable.buttomdesign);
        String text = result.getText().toString();
        if (text.isEmpty() || text.contains(".")) return;
        Button button = (Button) view;
        result.append(button.getText().toString());
    }

    public void getEqual(View view) {
        if (lastButton != null)
            lastButton.setBackgroundResource(R.drawable.buttomdesign);
        String text = result.getText().toString();
        if (text.isEmpty() || num == null) return;
        if (isValidDouble(text)){
            eval(text);
            num = null;
            isCalc = false;
        }
    }

    public void getC(View view) {
        if (lastButton != null)
            lastButton.setBackgroundResource(R.drawable.buttomdesign);
        String text = result.getText().toString();
        if (text.isEmpty() || op_locked) {
            op_locked = false;
            op = '$';
            if(num != null) result.setText(maybeInteger(num.toString()));
            num = null;
            return;
        }
        result.setText(text.subSequence(0, text.length() - 1));
    }
    public void getCE(View view) {
        if (lastButton != null)
            lastButton.setBackgroundResource(R.drawable.buttomdesign);
        result.setText("");
        op = '$';
        num = null;
    }

    private void operButtomPress(View view) {
        if(!op_locked) {
            if (lastButton != null)
                lastButton.setBackgroundResource(R.drawable.buttomdesign);
            lastButton = view;
            view.setBackgroundResource(R.drawable.buttompressdesign);
            op_locked = true;
        }
    }

    private void eval(String text) {
        Double current_num = Double.parseDouble(text);
        if(num == null){
            num = current_num;
            result.setText("");
        }
        else{
            calculate(current_num);
        }
    }

    public boolean isValidDouble(String str) {
        // Regular expression for a valid double number
        String regex = "^([+-]?0|[+-]?[1-9][0-9]*)([.][0-9]+)?$";
        // Return true if the string matches the regex, false otherwise
        return str.matches(regex) || str.equals("-0");
    }

    private String maybeInteger(String text){
        double value = Double.parseDouble(text);
        if(value == (int) value)
            text = String.valueOf((int) value);
        else{
            text = String.valueOf(Math.round(value * 10000000.0) / 10000000.0);
        }
        return text;
    }
    private void calculate(Double num2) {
        double output = 0;
        isCalc = true;
        switch (op) {
            case '+':
                output = num + num2;
                break;
            case '-':
                output = num - num2;
                break;
            case '*':
                output = num * num2;
                break;
            case '/':
                if (num2 != 0) {
                    output = num / num2;
                } else {
                    result.setText("Error"); // Handle division by zero
                    num = null;
                    num2 = null;
                    return;
                }
                break;
        }

        num = output; // Store the result for chaining operations
        result.setText(maybeInteger(String.valueOf(output))); // Display the result
    }


}