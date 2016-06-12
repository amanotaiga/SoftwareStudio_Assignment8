package com.csclab.hc.socketsample;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import android.util.Log;

public class MainActivity extends Activity implements android.view.View.OnClickListener {
    /**
     * Init Variable for IP page
     **/
    EditText inputIP;
    Button ipSend;
    String ipAdd = "";
    String outputStr;
    int destinationPortNum = 2000;
    /**
     * Init Variable for Page 1
     **/
    Button btn;
    /**/
    EditText inputNumTxt1;
    EditText inputNumTxt2;

    Button btnAdd;
    Button btnSub;
    Button btnMult;
    Button btnDiv;
    Button btnMod;
    /**
     * Init Variable for Page 2
     **/
    TextView textResult;

    Button return_button;

    /**
     * Init Variable
     **/
    private PrintWriter writer;
    Socket clientSocket;
    String oper = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ip_page);
        inputIP = (EditText) findViewById(R.id.edIP);
        ipSend = (Button) findViewById(R.id.ipButton);
        super.onCreate(savedInstanceState);

        ipSend.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToMainLayout();
            }
        });
    }
    private void sendMsg(String msg){
        this.writer.println(msg);
        this.writer.flush();
    }
    /**
     * Function for page 1 setup
     */

    public void jumpToMainLayout() {
        //TODO: Change layout to activity_main
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.button);
        if (btn != null) {

            btn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Log.d("Client", "Client Send");
                    Thread t = new thread();
                    t.start();
                    jumpToCalculateLayout();
                }
            });
        }
    }

    public void jumpToCalculateLayout() {
        setContentView(R.layout.calculate);

        inputNumTxt1 = (EditText) findViewById(R.id.etNum1);
        inputNumTxt2 = (EditText) findViewById(R.id.etNum2);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);
        btnMult = (Button) findViewById(R.id.btnMult);
        btnDiv = (Button) findViewById(R.id.btnDiv);
        btnMod = (Button)findViewById(R.id.btnMod);

        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
        btnMult.setOnClickListener(this);
        btnDiv.setOnClickListener(this);
        btnMod.setOnClickListener(this);

    }

    /**
     * Function for onclick() implement
     */
    @Override
    public void onClick(View v) {
        float num1 = 0; // Store input num 1
        float num2 = 0; // Store input num 2
        float result = 0; // Store result after calculating

        // check if the fields are empty
        if (TextUtils.isEmpty(inputNumTxt1.getText().toString())
                || TextUtils.isEmpty(inputNumTxt2.getText().toString())) {
            return;
        }

        num1 = Float.parseFloat(inputNumTxt1.getText().toString());
        num2 = Float.parseFloat(inputNumTxt2.getText().toString());

        switch (v.getId()) {
            case R.id.btnAdd:
                oper = "+";
                result = num1 + num2;
                break;
            case R.id.btnSub:
                oper = "-";
                result = num1 - num2;
                break;
            case R.id.btnMult:
                oper = "*";
                result = num1 * num2;
                break;
            case R.id.btnDiv:
                oper = "/";
                result = num1 / num2;
                break;
            case R.id.btnMod:
                oper = "%";
                result = num1 % num2;
                break;
            default:
                break;
        }
        Log.d("debug", "ANS " + result);
        String resultStr = num1 + " " + oper + " " + num2 + " = " + result;
        this.sendMsg(resultStr);
        jumpToResultLayout(resultStr);
    }

    public void jumpToResultLayout(String resultStr) {
        setContentView(R.layout.result);

        return_button = (Button) findViewById(R.id.return_button);
        textResult = (TextView) findViewById(R.id.textResult);

        if (textResult != null) {
            textResult.setText(resultStr);
        }

        if (return_button != null) {
            return_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    jumpToCalculateLayout();
                }

            });
        }
    }


   class thread extends Thread {
        public void run() {
                try {
                    ipAdd = inputIP.getText().toString();
                    MainActivity.this.clientSocket = new Socket(MainActivity.this.ipAdd, MainActivity.this .destinationPortNum);
                    MainActivity.this.writer = new PrintWriter(new OutputStreamWriter(MainActivity.this.clientSocket.getOutputStream()));
                    sendMsg("Hi ,I'm client");

                } catch (Exception e) {
                    System.out.println("Error" + e.getMessage());
                }
        }
    }
}
