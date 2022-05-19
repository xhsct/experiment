package com.example.computer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView result;
    private TextView process;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public static double f(String s)
    {
        double p = 0;
        //百分比计算
        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == '%')
            {
                for(int j = i - 1; j>= 0; j--)
                {
                    if(s.charAt(j) == '+' ||s.charAt(j) == '-' ||s.charAt(j) == '*' ||s.charAt(j) == '/')
                    {
                        String s1 = s.substring(j + 1, i);
                        double num = Double.parseDouble(s1)*0.01;
                        s = s.replace(s.substring(j+1, i+1), num+"");
                        break;
                    }
                    if(j == 0)
                    {
                        String s1 = s.substring(j, i);
                        double num = Double.parseDouble(s1)*0.01;
                        s = s.replace(s.substring(j, i+1), num+"");
                        break;
                    }
                }
            }
        }

        //乘除计算
        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == '*' || s.charAt(i) == '/')
            {
                double num1 = 0, num2 = 0;int len1 = 0, len2 = 0;
                for(int j = i - 1; j >= 0; j--)
                {
                    if(s.charAt(j) == '+' || s.charAt(j) == '-')
                    {
                        String s1 = s.substring(j + 1, i);
                        num1 = Double.parseDouble(s1);
                        len1 = s1.length();
                        break;
                    }
                    if(j == 0)
                    {
                        String s1 = s.substring(j, i);
                        num1 = Double.parseDouble(s1);
                        len1 = s1.length();
                        break;
                    }
                }
                for(int j = i + 1; j < s.length(); j++)
                {
                    if(s.charAt(j) == '+' || s.charAt(j) == '-' || s.charAt(j) == '*' || s.charAt(j) == '/' )
                    {
                        String s1 = s.substring(i + 1, j);

                        num2 = Double.parseDouble(s1);
                        len2 = s1.length();
                        break;
                    }
                    if(j == s.length() - 1)
                    {
                        String s1 = s.substring(i + 1, j+1);
                        num2 = Double.parseDouble(s1);
                        len2 = s1.length();
                        break;
                    }
                }
                if(s.charAt(i) == '*')
                {
                    double res = num1 * num2;
                    String s2 = s.substring(i - len1, i +len2 + 1);
                    String s3 = res + "";
                    s = s.replace(s2, s3);
                    i = i - len1 - 1 + s3.length();
                    continue;
                }
                if(s.charAt(i) == '/')
                {
                    double res = num1 / num2;
                    String s2 = s.substring(i - len1, i +len2 + 1);
                    String s3 = res + "";
                    s = s.replace(s2, s3);
                    i = i - len1 - 1 + s3.length();
                    continue;
                }

            }
        }

        //加减计算
        for(int i = 0; i < s.length(); i++)
        {
            if(s.charAt(i) == '+' || s.charAt(i) == '-')
            {
                double num1 = 0, num2 = 0;int len1 = 0, len2 = 0;
                for(int j = i - 1; j >= 0; j--)
                {
                    if(j == 0)
                    {
                        String s1 = s.substring(j, i);
                        num1 = Double.parseDouble(s1);
                        len1 = s1.length();
                        break;
                    }
                }
                for(int j = i + 1; j < s.length(); j++)
                {
                    if(s.charAt(j) == '+' || s.charAt(j) == '-')
                    {
                        String s1 = s.substring(i + 1, j);
                        num2 = Double.parseDouble(s1);
                        len2 = s1.length();
                        break;
                    }
                    if(j == s.length() - 1)
                    {
                        String s1 = s.substring(i + 1, j+1);
                        num2 = Double.parseDouble(s1);
                        len2 = s1.length();
                        break;
                    }
                }
                if(s.charAt(i) == '+')
                {
                    double res = num1 + num2;
                    String s2 = s.substring(i - len1, i +len2 + 1);
                    String s3 = res + "";
                    s = s.replace(s2, s3);
                    i = i - len1 - 1 + s3.length();
                    continue;
                }
                if(s.charAt(i) == '-')
                {
                    double res = num1 - num2;
                    String s2 = s.substring(i - len1, i +len2 + 1);
                    String s3 = res + "";
                    s = s.replace(s2, s3);
                    i = i - len1 - 1 + s3.length();
                    continue;
                }
            }
        }
        return Double.parseDouble(s);
    }

    public void clear(View view) {
        result = findViewById(R.id.result);
        process = findViewById(R.id.process);
        result.setText("0");
        process.setText("0");
    }

    public void one(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "1";
        process.setText(s);
    }

    public void two(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "2";
        process.setText(s);
    }

    public void three(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "3";
        process.setText(s);
    }

    public void four(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "4";
        process.setText(s);
    }

    public void five(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "5";
        process.setText(s);
    }

    public void six(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "6";
        process.setText(s);
    }

    public void seven(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "7";
        process.setText(s);
    }

    public void eight(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "8";
        process.setText(s);
    }

    public void nine(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "9";
        process.setText(s);
    }

    public void zero(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "0";
        process.setText(s);
    }
    public void yu(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "%";
        process.setText(s);
    }

    public void div(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "/";
        process.setText(s);
    }

    public void chen(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "*";
        process.setText(s);
    }

    public void sub(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "-";
        process.setText(s);
    }

    public void add(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += "+";
        process.setText(s);
    }


    public void dot(View view) {
        String s = process.getText().toString();
        if (s=="0"){
            s="";
        }
        s += ".";
        process.setText(s);
    }

    public void result(View view) {
        String s = process.getText().toString();
        double num = f(s);
        String a = String.valueOf(num);
        result.setText(a);

    }

}