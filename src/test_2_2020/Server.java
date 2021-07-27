package test_2_2020;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;

public class Server extends JFrame {
    public static final int TCP_PORT = 9000;
    public static JTextArea area = new JTextArea();
    public static JScrollPane scrollPane = new JScrollPane(area);

    public Server() {
        super();
        setTitle("SERVER");
        setSize(500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Font font = new Font("Times New Roman", Font.BOLD, 20);
        area.setFont(font);
        add(scrollPane);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.setVisible(true);
        try {
            ServerSocket ss = new ServerSocket(TCP_PORT);
            area.setText("Server je pokrenut.\n");
            while (true) {
                Socket s = ss.accept();
                ServerThread st = new ServerThread(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}