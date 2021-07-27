package test_2_2020;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client extends JFrame implements ActionListener {
    public static final int TCP_PORT = 9000;
    public int num = 1;
    public String request = ",,";
    public static JTextArea area = new JTextArea();
    public static JScrollPane scrollPane = new JScrollPane(area);
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    public Date nowTime = new Date();
    JLabel label = new JLabel(formatter.format(nowTime));
    JTextField numField = new JTextField();

    ButtonGroup group = new ButtonGroup();
    JRadioButton rbHello = new JRadioButton("Pozdrav", true);
    JRadioButton rbNumbers = new JRadioButton("Brojevi", false);

    JCheckBox cbSum = new JCheckBox("ZBIR");
    JCheckBox cbMultiplication = new JCheckBox("PROIZVOD");

    public Client() {
        super();
        setTitle("KLIJENT");
        setSize(500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        label.setForeground(Color.red);
        Timer();

        JPanel timerPanel = new JPanel();
        timerPanel.setBackground(Color.white);
        timerPanel.add(label);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        add(timerPanel, c);

        JPanel textPanelName = new JPanel();
        JLabel student = new JLabel("ILIJA RADONJIC RT-2/18");
        student.setForeground(Color.YELLOW);
        textPanelName.setBackground(Color.BLUE);
        textPanelName.add(student);
        c.gridx = 0;
        c.gridy = 1;
        add(textPanelName, c);

        JPanel textPanelRequest = new JPanel();
        JLabel request = new JLabel("ZAHTEV");
        request.setForeground(Color.blue);
        textPanelRequest.setBackground(Color.green);
        textPanelRequest.add(request);
        c.gridx = 0;
        c.gridy = 2;
        add(textPanelRequest, c);

        JPanel radioButtonPanel = new JPanel();
        rbHello.addItemListener(new radiobutton());
        rbNumbers.addItemListener(new radiobutton());
        group.add(rbHello);
        group.add(rbNumbers);
        radioButtonPanel.add(rbHello);
        radioButtonPanel.add(rbNumbers);
        c.gridx = 0;
        c.gridy = 3;
        add(radioButtonPanel, c);

        JPanel textPanel = new JPanel();
        JLabel text = new JLabel("NIZ BROJEVA ZA SLANJE NA SERVER");
        textPanel.add(text);
        c.gridx = 0;
        c.gridy = 4;
        add(textPanel, c);

        JPanel numberPanel = new JPanel();
        numField.setPreferredSize(new Dimension(200, 30));
        numberPanel.add(numField);
        c.gridx = 0;
        c.gridy = 5;
        add(numberPanel, c);

        JPanel operationCheckboxPanel = new JPanel();
        cbSum.addItemListener(new checkbox());
        cbMultiplication.addItemListener(new checkbox());
        operationCheckboxPanel.add(cbSum);
        operationCheckboxPanel.add(cbMultiplication);
        c.gridx = 0;
        c.gridy = 6;
        add(operationCheckboxPanel, c);

        JButton submit = new JButton("DUGME ZA SLANJE ZAHTEVA NA SERVER");
        submit.setBackground(Color.red);
        submit.setForeground(Color.YELLOW);
        submit.addActionListener(this);
        c.gridx = 0;
        c.gridy = 8;
        add(submit, c);

        JPanel responsePanel = new JPanel();
        JLabel responseLabel = new JLabel("ODGOVORI SERVERA");
        responsePanel.setBackground(Color.YELLOW);
        responseLabel.setForeground(Color.blue);
        responsePanel.add(responseLabel);
        c.gridx = 0;
        c.gridy = 9;
        add(responsePanel, c);

        c.gridx = 0;
        c.gridy = 10;
        c.ipady = 482;
        add(scrollPane, c);
    }

    public void Timer() {
        Timer timer = new Timer(1000, e -> {
            nowTime = new Date();
            label.setText(formatter.format(nowTime));
        });
        timer.setRepeats(true);
        timer.start();
    }

    class radiobutton implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            if (((AbstractButton) e.getItem()).getText().equals("Pozdrav"))
                num = 1;
            else
                num = 2;
        }
    }

    class checkbox implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource() instanceof JCheckBox) {
                if (cbSum.isSelected())
                    request = "ZBIR,";
                if (cbMultiplication.isSelected())
                    request = ",PROIZVOD,";
                if (cbSum.isSelected() && cbMultiplication.isSelected())
                    request = "ZBIR,PROIZVOD,";
                if (!cbSum.isSelected() && !cbMultiplication.isSelected())
                    request = ",,";
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            InetAddress addr = InetAddress.getByName("127.0.0.1");
            Socket s = new Socket(addr, TCP_PORT);

            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);

            if (num == 1) {
                out.println(":POZDRAV");
            } else {
                out.println(":BROJEVI:" + numField.getText() + ":" + request);
            }

            String response = in.readLine();
            area.setText(area.getText() + response + "\n");

            in.close();
            out.close();
            s.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.setVisible(true);
    }
}