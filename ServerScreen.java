import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.*;
import javax.swing.ImageIcon;

public class ServerScreen extends JPanel implements ActionListener{
    //   private JTextArea textArea;
    //   private JButton send;
    //   private JTextField textfield;
    //   private JScrollPane scrollPane;
    //   private String sendText;
    //   private String messages;
    //   private boolean toSend;

    private DLList<Color> guesses;
    private DLList<Color> feedback;
    private DLList<Color> code;
    private JButton submit;
    private String hostName;
    private int portNumber;
    private boolean feedbackSubmit;
    private int screenSetting;

    public ServerScreen() {
        setLayout(null);
        this.setFocusable(true);

        hostName = "localhost";
        portNumber = 1024;
        screenSetting = 0;

        guesses = new DLList<Color>();
        feedback = new DLList<Color>();
        code = new DLList<Color>();
        feedbackSubmit = false;

        submit = new JButton("Start Game");
		submit.setBounds(350, 500, 100, 40);
		submit.addActionListener(this);
		add(submit);

        // messages = "";
        // toSend = false;

        // textfield = new JTextField(20);
        // textfield.setBounds(280,110,200,30);
        // this.add(textfield);
        // Action action = new AbstractAction(){
        //   @Override
        //   public void actionPerformed(ActionEvent e)
        //   {
        //     sendText = textfield.getText();
        //     textfield.setText("");
        //     toSend = true;
        //     System.out.println("toSend is true");
        //   }
        // };
        // textfield.addActionListener(action);

        // textArea = new JTextArea(messages);
        // scrollPane = new JScrollPane(textArea);
        // scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        // scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        // scrollPane.setBounds(20,50,220,530);
        // this.add(scrollPane);
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(screenSetting == 0){//instructions

        }
        else if(screenSetting == 1){//choosing code

        }
        else if(screenSetting == 2){//regular screen
            int x = 100;
            int y = 75;

            g.drawRoundRect(x, y, 300, 500, 20, 20);
            g.drawRoundRect(x + 50, y - 50, 200, 50, 20, 20);

            //circles for code
            code.reset();
            Color cColor = code.next();
            for(int c = 0; c < 200; c += 50){
                if(cColor != null){
                    g.setColor(cColor);
                    g.fillOval(x + 60 + c, y - 40, 30, 30);
                    cColor = code.next();
                }
                else{
                    g.drawOval(x + 60 + c, y - 40, 30, 30);
                }
            }

            for(int i = 50; i < 500; i += 50){
                g.drawLine(x, y + i, x + 300, y + i);
            }

            g.drawLine(x + 240, y, x + 240, y + 500);

            guesses.reset();
            Color color = guesses.next();
            //drawing the guess circles
            for(int r = 0; r < 500; r += 50){
                for(int c = 0; c < 240; c += 60){
                    if(color != null){
                        g.setColor(color);
                        g.fillOval(c+ x + 10, r + y + 10, 30, 30);
                        color = guesses.next();
                    }
                    else{
                        g.drawOval(c+ x + 10, r + y + 10, 30, 30);
                    }

                    
                }
            }

            //drawing the feedback circles
            feedback.reset();
            Color fColor = feedback.next();

            for(int r = 0; r < 500; r += 25){
                for(int c = 0; c < 60; c += 30){
                    if(fColor != null){
                        g.setColor(fColor);
                        g.fillOval(c + x + 250, r + y + 10, 10, 10);
                        fColor = feedback.next();
                    }
                    else{
                        g.drawOval(c + x + 250, r + y + 10, 10, 10);
                    }
                }
            }
        }

        
    }

    public void poll() throws IOException, UnknownHostException {
        ServerSocket sSocket = new ServerSocket(portNumber);
        Socket cSocket = sSocket.accept();
        PushbackInputStream pin = new PushbackInputStream(cSocket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(cSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(cSocket.getInputStream());

        try {
            while(true){
                if(feedbackSubmit){
                    out.reset();
                    out.writeObject(feedback);

                    feedbackSubmit = false;
                }
                else if(pin.available() != 0){
                    DLList inputList = (DLList)in.readObject();

                    guesses = inputList;
                }

            repaint();
          }
        }
        catch (UnknownHostException e) {
          System.out.println("Unknown host");
        }
        catch (IOException e) {
          System.out.println("IO Exception: " + e);
        }
        catch(ClassNotFoundException e){
            System.out.println(e);
        }


        //     //System.out.println("in while true");
        //     BufferedReader in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
        //     PushbackInputStream pin = new PushbackInputStream(cSocket.getInputStream());
        //     PrintWriter out = new PrintWriter(cSocket.getOutputStream(), true);
        //     if(pin.available() != 0){
        //       String newMessage = in.readLine();
        //       messages += "Client:" + newMessage + "\n";
        //       textArea.setText(messages);

        //       //in.close();

        //       repaint();
        //     }
        //     else if(toSend){
        //       messages += "Server: " + sendText + "\n";
        //       textArea.setText(messages);

        //       out.println(sendText);
        //       //out.close();

        //       toSend = false;
        //     }
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == submit){
            if(screenSetting == 0){
                screenSetting ++;
                submit.setText("Submit");
            }
            else if(screenSetting == 1){
                screenSetting ++;
                submit.setBounds(550, 400, 100, 40);
            }
            else if(screenSetting == 2){
                feedbackSubmit = true;
                submit.setVisible(false);
            }
        }

        repaint();
    }
}