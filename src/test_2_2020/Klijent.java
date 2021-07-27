package test_2_2020;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Klijent extends JFrame implements ActionListener {
    public static final int TCP_PORT = 9000;
    public int num = 1;
    public String name = ",,";
    public static JTextArea area = new JTextArea();
    public static JScrollPane scrollPane = new JScrollPane(area);
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
    public Date nowTime = new Date();
    JLabel label = new JLabel(formatter.format(nowTime));
    JTextField field = new JTextField();

    ButtonGroup group = new ButtonGroup();
    JRadioButton rb1 = new JRadioButton("Pozdrav", true);
    JRadioButton rb2 = new JRadioButton("Brojevi", false);

    JCheckBox cb1 = new JCheckBox("ZBIR");
    JCheckBox cb2 = new JCheckBox("PROIZVOD");

    public Klijent() {
        super();
        setTitle("KLIJENT");
        setSize(500, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        label.setForeground(Color.red);
        Tajmer();

        JPanel panel1 = new JPanel();
        panel1.setBackground(Color.white);
        panel1.add(label);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        add(panel1, c);

        JPanel panel2 = new JPanel();
        JLabel student = new JLabel("ILIJA RADONJIC RT-2/18");
        student.setForeground(Color.YELLOW);
        panel2.setBackground(Color.BLUE);
        panel2.add(student);
        c.gridx = 0;
        c.gridy = 1;
        add(panel2, c);

        JPanel panel3 = new JPanel();
        JLabel request = new JLabel("ZAHTEV");
        request.setForeground(Color.blue);
        panel3.setBackground(Color.green);
        panel3.add(request);
        c.gridx = 0;
        c.gridy = 2;
        add(panel3, c);

        JPanel panel4 = new JPanel();
        rb1.addItemListener(new radiobutton());
        rb2.addItemListener(new radiobutton());
        group.add(rb1);
        group.add(rb2);
        panel4.add(rb1);
        panel4.add(rb2);
        c.gridx = 0;
        c.gridy = 3;
        add(panel4, c);

        JPanel panel5 = new JPanel();
        JLabel text = new JLabel("NIZ BROJEVA ZA SLANJE NA SERVER");
        panel5.add(text);
        c.gridx = 0;
        c.gridy = 4;
        add(panel5, c);

        JPanel panel6 = new JPanel();
        field.setPreferredSize(new Dimension(200, 30));
        panel6.add(field);
        c.gridx = 0;
        c.gridy = 5;
        add(panel6, c);

        JPanel panel7 = new JPanel();
        cb1.addItemListener(new checkbox());
        cb2.addItemListener(new checkbox());
        panel7.add(cb1);
        panel7.add(cb2);
        c.gridx = 0;
        c.gridy = 6;
        add(panel7, c);

        JButton btn = new JButton("DUGME ZA SLANJE ZAHTEVA NA SERVER");
        btn.setBackground(Color.red);
        btn.setForeground(Color.YELLOW);
        btn.addActionListener(this);
        c.gridx = 0;
        c.gridy = 8;
        add(btn, c);

        JPanel panel8 = new JPanel();
        JLabel responseLabel = new JLabel("ODGOVORI SERVERA");
        panel8.setBackground(Color.YELLOW);
        responseLabel.setForeground(Color.blue);
        panel8.add(responseLabel);
        c.gridx = 0;
        c.gridy = 9;
        add(panel8, c);

        c.gridx = 0;
        c.gridy = 10;
        c.ipady = 482;
        add(scrollPane, c);
    }

    public void Tajmer() {
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
                if (cb1.isSelected())
                    name = "ZBIR,";
                if (cb2.isSelected())
                    name = ",PROIZVOD,";
                if (cb1.isSelected() && cb2.isSelected())
                    name = "ZBIR,PROIZVOD,";
                if (!cb1.isSelected() && !cb2.isSelected())
                    name = ",,";
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
                out.println(":BROJEVI:" + field.getText() + ":" + name);
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
        Klijent klijent = new Klijent();
        klijent.setVisible(true);
    }
}