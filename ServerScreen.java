import java.awt.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.*;

public class ServerScreen extends JPanel implements ActionListener {
  private JTextArea textArea;
  private JButton send;
  private JTextField textfield;
  private JScrollPane scrollPane;
  private String sendText;
  private String messages;
  private boolean toSend;

  public ServerScreen(){
    setLayout(null);
    this.setFocusable(true);

    messages = "";
    toSend = false;
    
    textfield = new JTextField(20);
    textfield.setBounds(280,110,200,30);
    this.add(textfield);
    Action action = new AbstractAction(){
      @Override
      public void actionPerformed(ActionEvent e)
      {
        sendText = textfield.getText();
        textfield.setText("");
        toSend = true;
        System.out.println("toSend is true");
      }
    };
    textfield.addActionListener(action);

    textArea = new JTextArea(messages);
    scrollPane = new JScrollPane(textArea);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    scrollPane.setBounds(20,50,220,530);
    this.add(scrollPane);
  }
  
  public Dimension getPreferredSize() {
    return new Dimension(800,600);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
	}

  public void poll() throws IOException, UnknownHostException{
    String hostName = "localhost"; 
    int portNumber = 1024;
    ServerSocket sSocket = new ServerSocket(portNumber);
    Socket cSocket = sSocket.accept();

    
    try {
      while(true){
        //System.out.println("in while true");
        BufferedReader in = new BufferedReader(new InputStreamReader(cSocket.getInputStream()));
        PushbackInputStream pin = new PushbackInputStream(cSocket.getInputStream());
        PrintWriter out = new PrintWriter(cSocket.getOutputStream(), true);
        if(pin.available() != 0){
          String newMessage = in.readLine();
          messages += "Client:" + newMessage + "\n";
          textArea.setText(messages);

          //in.close();

          repaint();
        }
        else if(toSend){
          messages += "Server: " + sendText + "\n";
          textArea.setText(messages);

          out.println(sendText);
          //out.close();

          toSend = false;
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
  }

  // public void keyPressed(KeyEvent e){}
  // public void keyReleased(KeyEvent e){}

  // public void keyTyped(KeyEvent e){
  //   if(e.getKeyCode() == 13){
  //     toSend = true;
  //   }

  //   repaint();
  // }
}