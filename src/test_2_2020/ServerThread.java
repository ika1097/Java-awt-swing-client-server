package test_2_2020;

import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    private Socket sock;
    private String response;
    private BufferedReader in;
    private PrintWriter out;
    private int sum = 0;
    private int productOfMultiplication = 1;

    public ServerThread(Socket sock) throws IOException {
        this.sock = sock;
        try {
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())), true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        start();
    }

    public void run() {
        try {
            String request = in.readLine();
            String[] arr = request.split(":");

            if (arr[1].equals("POZDRAV")) {
                response = "POZDRAV OD SERVERA";
            } else {
                String[] numbers = arr[2].split(" ");
                if (!arr[2].equals("") && arr[3].equals(",,")) {
                    response = "Nema operacije!!!";
                } else if (arr[2].equals("") && !arr[3].equals(",,")) {
                    response = "Nema brojeva!!!";
                } else if (arr[2].equals("")) {
                    response = "Nema brojeva!!!Nema operacije!!!";
                } else if (arr[3].equals("ZBIR,")) {
                    for (String i : numbers) {
                        sum += Integer.parseInt(i);
                    }
                    response = "ZBIR JE " + sum;
                } else if (arr[3].equals(",PROIZVOD,")) {
                    for (String i : numbers) {
                        productOfMultiplication *= Integer.parseInt(i);
                    }
                    response = "PROIZVOD JE " + productOfMultiplication;
                } else if ((arr[3].equals("ZBIR,PROIZVOD,"))) {
                    for (String i : numbers) {
                        sum += Integer.parseInt(i);
                        productOfMultiplication *= Integer.parseInt(i);
                    }
                    response = "ZBIR JE " + sum + " PROIZVOD JE  " + productOfMultiplication;
                }
            }

            Server.area.setText(Server.area.getText() + "Klijentov zahtev:\n" + request + "\n");
            Server.area.setText(Server.area.getText() + "[odgovor]" + response + "\n");
            out.println(response);
            in.close();
            out.close();
            sock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}