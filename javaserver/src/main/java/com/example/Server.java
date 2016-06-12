package com.example;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

/**
 * Created by Gary on 16/5/28.
 */
public class Server extends JFrame implements Runnable{
    private Thread thread;
    private ServerSocket servSock;

    private JLabel result;
    private BufferedReader reader;
    Socket clntSock;

    public Server(){

        super.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        super.setSize(new Dimension(300, 100));
        super.setResizable(false);
        super.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
        super.setVisible(true);

        this.reader = null;
        this.result = new JLabel();
        super.add(this.result);

        try {
            // Detect server ip
            InetAddress IP = InetAddress.getLocalHost();
            this.result.setText("IP of my system is := "+IP.getHostAddress());
            this.result.setText("Waitting to connect......");

            // Create server socket
            servSock = new ServerSocket(2000);

            // Create socket thread
            thread = new Thread(this);
            thread.start();
        } catch (java.io.IOException e) {
            System.out.println("Socket啟動有問題 !");
            System.out.println("IOException :" + e.toString());
        } finally{

        }
    }

    @Override
    public void run(){
        // Running for waitting multiple client
            try{
                 clntSock = servSock.accept();

                this.result.setText("Connected!!");

                this.reader = new BufferedReader(new InputStreamReader(clntSock.getInputStream()));
                while(true){
                    String msg = this.reader.readLine();
                    this.result.setText(msg);
                }
            }
            catch(Exception e){
                //System.out.println("Error: "+e.getMessage());
            }
    }
}
