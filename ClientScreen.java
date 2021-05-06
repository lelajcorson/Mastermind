import java.awt.*;
import java.util.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.Socket;

public class ClientScreen extends JPanel implements ActionListener{
  private JTextArea textArea;
  private JButton send;
  private JTextField textfield;
  private JScrollPane scrollPane;
  private String sendText;
  private String messages;
  private boolean toSend;

  public ClientScreen(){
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

  public void poll() throws IOException, UnknownHostException {
    
    
    try {

        String hostName = "localhost"; 
        int portNumber = 1024;
        
        Socket sSocket = new Socket(hostName, portNumber);

        

      while(true){
        BufferedReader in = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
        PushbackInputStream pin = new PushbackInputStream(sSocket.getInputStream());
        PrintWriter out = new PrintWriter(sSocket.getOutputStream(), true);

        if(pin.available() != 0){
          String newMessage = in.readLine();
          messages += "Server:" + newMessage + "\n";
          textArea.setText(messages);

          //in.close();

          repaint();
        }
        else if(toSend){
          
          messages += "Client: " + sendText + "\n";
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