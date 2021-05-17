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
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class ServerScreen extends JPanel implements ActionListener, MouseListener{
    private DLList<Color> guesses;
    private DLList<Color> feedback;
    private DLList<Color> code;
    private DLList<Color> colorPalette;
    private Color yellow;
    private Color teal;
    private Color orange;
    private Color pink;
    private Color blue;
    private Color brown;
    private JButton submit;
    private JButton startGame;
    private JButton chooseCode;
    private String hostName;
    private int portNumber;
    private boolean feedbackSubmit;
    private int screenSetting;
    private Color currentColor;
    //private ImageIcon rules;

    public ServerScreen() {
        setLayout(null);
        this.setFocusable(true);
        addMouseListener(this);

        hostName = "localhost";
        portNumber = 1024;
        screenSetting = 0;
        currentColor = null;
        //rules = new ImageIcon("Images/serverRules.PNG");

        guesses = new DLList<Color>();
        feedback = new DLList<Color>();
        code = new DLList<Color>();
        colorPalette = new DLList<Color>();
        feedbackSubmit = false;

        yellow = new Color(245, 215, 161);
        teal = new Color(155, 194,189);
        orange = new Color(240, 162, 142);
        pink = new Color(186, 99, 117);
        blue = new Color(0, 75, 90);
        brown = new Color(136, 59, 31);

        colorPalette.add(yellow);
        colorPalette.add(orange);
        colorPalette.add(pink);
        colorPalette.add(brown);
        colorPalette.add(blue);
        colorPalette.add(teal);

        code.add(new Color(242, 242, 242));
        code.add(new Color(242, 242, 242));
        code.add(new Color(242, 242, 242));
        code.add(new Color(242, 242, 242));

        submit = new JButton("Submit");
		submit.setBounds(550, 400, 100, 40);
		submit.addActionListener(this);
		add(submit);
        submit.setVisible(false);

        startGame = new JButton("Start Game");
		startGame.setBounds(350, 500, 100, 40);
		startGame.addActionListener(this);
		add(startGame);

        chooseCode = new JButton("Submit");
		chooseCode.setBounds(350, 500, 100, 40);
		chooseCode.addActionListener(this);
		add(chooseCode);
        chooseCode.setVisible(false);
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 600);
        g.setColor(Color.BLACK);

        int x = 100;
        int y = 75;

        if(screenSetting == 0){//instructions
           // rules.paintIcon(this, g, 0, 0);
        }
        else if(screenSetting == 1){//choosing code
            g.drawString("Choose the Code", 300, 50);

            //drawing the color palette
            g.setColor(Color.BLACK);
            g.fillRoundRect(x + 350, y + 100, 250, 50, 20, 20);
            
            colorPalette.reset();
            Color pColor = colorPalette.next();
            for(int c = 0; c < 240; c += 40){
                if(pColor != null){
                    g.setColor(pColor);
                    g.fillOval(x + 360 + c, y + 110, 30, 30);
                    pColor = colorPalette.next();
                }
                else{
                    g.drawOval(x + 360 + c, y + 110, 30, 30);
                    //pColor = colorPalette.next();
                }
            }

            //drawing the code slots
            g.setColor(Color.BLACK);
            g.fillRoundRect(x, y + 100, 200, 50, 20, 20);

            code.reset();
            Color cColor = code.next();
            for(int c = 0; c < 200; c += 50){
                if(cColor != null){
                    g.setColor(cColor);
                    g.fillOval(x + 10 + c, y + 110, 30, 30);
                    cColor = code.next();
                }
                else{
                    g.setColor(new Color(242, 242, 242));
                    g.fillOval(x + 10 + c, y + 110, 30, 30);
                }
            }
        }
        else if(screenSetting == 2){//regular screen
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

            g.setColor(Color.BLACK);

            for(int i = 50; i < 500; i += 50){
                g.drawLine(x, y + i, x + 300, y + i);
            }

            g.drawLine(x + 240, y, x + 240, y + 500);

            guesses.reset();
            Color color = guesses.next();
            g.setColor(Color.BLACK);
            //drawing the guess circles
            for(int r = 0; r < 500; r += 50){
                for(int c = 0; c < 240; c += 60){
                    if(color != null){
                        g.setColor(color);
                        g.fillOval(c+ x + 10, r + y + 10, 30, 30);
                        color = guesses.next();
                    }
                    else{
                        g.setColor(Color.BLACK);
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
                        g.setColor(Color.BLACK);
                        g.drawOval(c + x + 250, r + y + 10, 10, 10);
                    }
                }
            }
        }

        
    }

    public void poll() throws IOException, UnknownHostException {
        ServerSocket sSocket = new ServerSocket(portNumber);
        Socket cSocket = sSocket.accept();
        ObjectOutputStream out = new ObjectOutputStream(cSocket.getOutputStream());
        System.out.println("opened");
        ObjectInputStream in = new ObjectInputStream(cSocket.getInputStream());
        PushbackInputStream pin = new PushbackInputStream(cSocket.getInputStream());

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
                    System.out.println("sent");
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
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource() == submit){
            screenSetting ++;
            feedbackSubmit = true;
            submit.setVisible(false);
        }
        else if(e.getSource() == startGame){
            screenSetting ++;
            startGame.setVisible(false);
            chooseCode.setVisible(true);
        }
        else if(e.getSource() == chooseCode){
            boolean go = true;
            for(int i = 0; i < code.size(); i ++){
                if(code.get(i).equals(new Color(242, 242, 242))){
                    go = false;
                }
            }

            if(go){
                screenSetting ++;
                chooseCode.setVisible(false);
            }            
        }

        repaint();
    }

    public void mouseReleased(MouseEvent e) {}
 
    public void mouseEntered(MouseEvent e) {}
 
    public void mouseExited(MouseEvent e) {}
 
    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        System.out.println("X: " + e.getX() + " Y: " + e.getY());
        int x = e.getX();
        int y = e.getY();
        
        if(screenSetting == 1){
            if(x > 410 && x < 690 && y > 165 && y < 215){
                int c = (x-460)/40;
                currentColor = colorPalette.get(c);
            }
            else if(x > 100 && x < 300 && y > 175 && y < 225){
                int c = (x-110)/50;
                int centerX = 125 + c * 50;

                if(x - centerX < 15){
                    code.set(c, currentColor);
                }

            }
        }

        repaint();
    }
}