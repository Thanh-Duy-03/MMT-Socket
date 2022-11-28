package Client;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.*;
import java.util.Base64;
import java.io.*;

public class ClientMenu extends JFrame implements ActionListener {
    private JButton jbConnect;
    private JButton jbListProcess;
    private JButton jbListApp;
    private JButton jbScreenShot;
    private JButton jbShutdown;
    private JButton jbListen;
    private JTextField jtHost;

    private Socket client = null;
    private InputStreamReader in = null;
    private OutputStreamWriter out = null;
    private BufferedReader br= null;
    private BufferedWriter bw = null;

    public ClientMenu() {
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                if (client != null) {
                    try {
                        bw.write("Disconnect");
                        bw.newLine();
                        bw.flush();

                        client.close();
                        in.close();
                        out.close();
                        br.close();
                        bw.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        this.setLayout(null);
        this.setSize(314, 200);
        this.initComponents();
        this.setVisible(true);
    }

    private void initComponents() {
        JPanel Panel1 = new JPanel();
        Panel1.setBounds(0, 0, 300, 40);

        jbConnect = new JButton("Connect");
        jbConnect.addActionListener(this);
        
        jtHost = new JTextField("Enter host IP");
        jtHost.setPreferredSize(new Dimension(200,27));

        Panel1.add(jbConnect);
        Panel1.add(jtHost);

        JPanel Panel2 = new JPanel();
        Panel2.setBounds(0, 40, 300, 160);

        jbListen = new JButton("Listen");
        jbShutdown = new JButton("Shutdown");
        jbScreenShot = new JButton("Screen shot");
        jbListProcess = new JButton("List Process");
        jbListApp = new JButton("List Application");

        jbListen.setPreferredSize(new Dimension(120, 30));
        jbShutdown.setPreferredSize(new Dimension(120, 30));
        jbScreenShot.setPreferredSize(new Dimension(120, 30));
        jbListProcess.setPreferredSize(new Dimension(120, 30));
        jbListApp.setPreferredSize(new Dimension(120, 30));

        jbListApp.addActionListener(this);
        jbListProcess.addActionListener(this);
        jbListen.addActionListener(this);
        jbScreenShot.addActionListener(this);
        jbShutdown.addActionListener(this);
        
        Panel2.add(jbShutdown);
        Panel2.add(jbScreenShot);
        Panel2.add(jbListProcess);
        Panel2.add(jbListApp);
        Panel2.add(jbListen);

        this.add(Panel1);
        this.add(Panel2);
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == jbConnect)
            Connect();
        else if (evt.getSource() == jbListApp)
            ListApp();
        else if (evt.getSource() == jbListProcess)
            ListProcess();
        else if (evt.getSource() == jbScreenShot)
            ScreenShot();
        else if (evt.getSource() == jbShutdown)
            Shutdown();
        else 
            Listen();
    }

    private void Connect() {
        String host_ip = jtHost.getText();

        try {
            client = new Socket(host_ip, 1234);
            in = new InputStreamReader(client.getInputStream());
            out = new OutputStreamWriter(client.getOutputStream());

            br = new BufferedReader(in, 2048);
            bw = new BufferedWriter(out, 2048);

        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ListApp() {
        try {
            new ViewRunning("Application", br, bw);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ListProcess() {
        try {
            new ViewRunning("Process", br, bw);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void Listen() {
        try {
            new Listener(br, bw);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void Shutdown() {
        try {
            bw.write("Shutdown");
            bw.newLine();
            bw.flush();

            client.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void ScreenShot() {
        try {
            bw.write("Screen shot");
            bw.newLine();
            bw.flush();

            String recv = br.readLine();

            int data_len = Integer.parseInt(recv);
            int len = 0;
            FileOutputStream fout = new FileOutputStream("Screenshot.png");

            while (len < data_len) {
                recv = br.readLine();
                byte[] data = Base64.getDecoder().decode(recv);
                fout.write(data);
                len += recv.length();
            }

            fout.close();
            
            Path path = Paths.get("Screenshot.png");
            path = path.toAbsolutePath();
            String picPath = path.toString();
            
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "powershell \"Start-Process "+picPath + "\"");
            Process p = builder.start();
            //Runtime.getRuntime().exec("powershell \"Start-Process "+picPath + "\"");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
