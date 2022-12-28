package Client;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.GridBagLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.GridBagConstraints;
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
    private JLabel jlTitle;
    private JLabel label;
    private JLabel jlTitleGroupName;

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
        this.setSize(450, 450);
       
        this.initComponents();
        this.setVisible(true);
    }

    private void initComponents() {
        JPanel Panel0 = new JPanel();
        Panel0.setBounds(0, 0, 450, 120);
        jlTitle = new JLabel("Socket Remote Controller");
        jlTitle.setFont(new Font("Serif", Font.BOLD, 36));
        jlTitleGroupName = new JLabel("21CLC01 - Group 12");
        jlTitleGroupName.setFont(new Font("Serif", Font.BOLD, 24));
        
        Panel0.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        Panel0.add(jlTitle,c);
        
        c.gridx = 0;
        c.gridy = 1;
        Panel0.add(jlTitleGroupName, c);

        // c.gridx = 0;
        // c.gridy = 0;
        // c.fill = GridBagConstraints.BOTH;
        // label = new JLabel();
        // label.setPreferredSize(new Dimension(40, 40));
        // ImageIcon imgIcon = new ImageIcon("logo.png");
        // Image img = imgIcon.getImage();
        // Image imgScaled = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        // label.setIcon(new ImageIcon(imgScaled));
        // Panel0.add(label, c);


        JPanel Panel1 = new JPanel();
        Panel1.setBounds(0, 120, 450, 50);

        ImageIcon IIconnect = new ImageIcon("assets/antenna.png");
        jbConnect = new JButton("Connect", IIconnect);
        jbConnect.addActionListener(this);
        
        jtHost = new JTextField("Enter host IP");
        jtHost.setPreferredSize(new Dimension(300,27));



        Panel1.add(jbConnect);
        Panel1.add(jtHost);

        ImageIcon IIshutdown = new ImageIcon("assets/shutdown.png");
        ImageIcon IIscreenshot = new ImageIcon("assets/screenshot.png");
        ImageIcon IIkeylogger = new ImageIcon("assets/keylogger.png");
        ImageIcon IIprocess = new ImageIcon("assets/process.png");
        ImageIcon IIapp = new ImageIcon("assets/categories.png");
     

        JPanel Panel2 = new JPanel();
        Panel2.setBounds(20, 170, 150, 200);
        Panel2.setLayout(new GridBagLayout());

        jbListen = new JButton("Keylogger", IIkeylogger);
        jbShutdown = new JButton("Shutdown", IIshutdown);
        jbScreenShot = new JButton("Screen shot",IIscreenshot);
        jbListProcess = new JButton("List Process", IIprocess);
        jbListApp = new JButton("List Application", IIapp);

        jbListen.setPreferredSize(new Dimension(150, 30));
        jbShutdown.setPreferredSize(new Dimension(150, 30));
        jbScreenShot.setPreferredSize(new Dimension(150, 30));
        jbListProcess.setPreferredSize(new Dimension(150, 30));
        jbListApp.setPreferredSize(new Dimension(150, 30));
        
        jbListApp.addActionListener(this);
        jbListProcess.addActionListener(this);
        jbListen.addActionListener(this);
        jbScreenShot.addActionListener(this);
        jbShutdown.addActionListener(this);

        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridx = 0;
        c1.weighty = 1.0;
        
        Panel2.add(jbListProcess,c1);
        Panel2.add(jbListApp,c1);
        Panel2.add(jbScreenShot,c1);
        Panel2.add(jbListen,c1);
        Panel2.add(jbShutdown,c1);


        JPanel Panel3 = new JPanel();
        Panel3.setBounds(190, 150, 240, 300);
        label = new JLabel();
        label.setPreferredSize(new Dimension(150, 150));
        ImageIcon imgIcon = new ImageIcon("assets/logo.png");
        Image img = imgIcon.getImage();
        Image imgScaled = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(imgScaled));
        Panel3.add(label);


        JLabel nnq = new JLabel("Nguyễn Nhật Quang - 21127151");
        JLabel ttd = new JLabel("Trần Thành Duy - 21127033");
        JLabel tmk = new JLabel("Trần Minh Khoa - 21127629");
        Panel3.add(nnq);
        Panel3.add(ttd);
        Panel3.add(tmk);

        //Add all panels
        this.add(Panel0);
        this.add(Panel1);
        this.add(Panel2);
        this.add(Panel3);
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
