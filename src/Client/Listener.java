package Client;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;

import java.io.*;

public class Listener extends JFrame implements ActionListener {
    public Listener(BufferedReader br, BufferedWriter bw) {
        this.br = br;
        this.bw = bw;

        this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    if (listening) {
                        listening = false;
    
                        bw.write("Stop Listen");
                        bw.newLine();
                        bw.flush();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.setLayout(null);
        this.setSize(500, 500);
        this.initComponents();
        this.setVisible(true);
    }

    private BufferedReader br = null;
    private BufferedWriter bw = null;
    private JTextArea text = null;
    private JButton jbStart = null;
    private JButton jbStop = null;
    private boolean listening = false;

    private void initComponents() {
        text = new JTextArea();
        text.setEditable(false);
        text.setBounds(0, 0, 500, 300);
        
        jbStart = new JButton("Start listening");
        jbStart.setBounds(150, 330, 200, 30);
        jbStart.addActionListener(this);

        jbStop = new JButton("Stop listening");
        jbStop.setBounds(150, 380, 200, 30);
        jbStop.addActionListener(this);

        add(text);
        add(jbStart);
        add(jbStop);
    }

    private void listen() {
        while (true) {
            try {
                String key = br.readLine();
                if (key.equals("end")) break;
                switch (key) {
                    case "enter":
                        key = "\n";
                        break;
                    case "space":
                        key = " ";
                        break;
                    case "backspace":
                        key = "\b";
                        break;
                    default:
                        break;
                }
                text.append(key);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if (e.getSource() == jbStart) {
            try {
                if (!listening) {
                    bw.write("Start Listen");
                    bw.newLine();
                    bw.flush();

                    listening = true;
                    Thread thread = new Thread() {
                        public void run() {
                            listen();
                        }
                    }; 

                    thread.start();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else if (e.getSource() == jbStop) {
            try {
                if (listening) {
                    listening = false;

                    bw.write("Stop Listen");
                    bw.newLine();
                    bw.flush();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
