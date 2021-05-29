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
    private DLList<Color> code;
    private DLList<Color> colorPalette;
    private DLList<Color> feedbackInput;
    private DLList<Color> feedback;
    private Color yellow;
    private Color teal;
    private Color orange;
    private Color pink;
    private Color blue;
    private Color brown;
    private JButton submit;
    private JButton startGame;
    private JButton chooseCode;
    private JButton submitFeedback;
    private JButton restart;
    private String hostName;
    private int portNumber;
    private boolean feedbackSubmit;
    private int screenSetting;
    private Color currentColor;
    private Font f;
    private Font b;
    private boolean feedbackReset;
    private int guessNumber;
    private boolean codeSend;
    //private ImageIcon rules;

    public ServerScreen() {
        setLayout(null);
        this.setFocusable(true);
        addMouseListener(this);

        hostName = "localhost";
        portNumber = 1024;
        screenSetting = 0;
        currentColor = null;
        f = new Font("Courier", Font.BOLD, 22);
        b = new Font("Courier", Font.BOLD, 44);
        feedbackReset = false;
        guessNumber = 0;
        codeSend = false;
        //rules = new ImageIcon("Images/serverRules.PNG");

        guesses = new DLList<Color>();
        code = new DLList<Color>();
        colorPalette = new DLList<Color>();
        feedbackInput = new DLList<Color>();
        feedback = new DLList<Color>();
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

        feedbackInput.add(Color.WHITE);
        feedbackInput.add(Color.WHITE);
        feedbackInput.add(Color.WHITE);
        feedbackInput.add(Color.WHITE);

        code.add(new Color(242, 242, 242));
        code.add(new Color(242, 242, 242));
        code.add(new Color(242, 242, 242));
        code.add(new Color(242, 242, 242));

        submit = new JButton("Submit");
		submit.setBounds(550, 400, 100, 40);
		submit.addActionListener(this);
		add(submit);
        submit.setVisible(false);

        submitFeedback = new JButton("Submit");
		submitFeedback.setBounds(550, 430, 100, 40);
		submitFeedback.addActionListener(this);
		add(submitFeedback);
        submitFeedback.setVisible(false);

        startGame = new JButton("Start Game");
		startGame.setBounds(350, 500, 100, 40);
		startGame.addActionListener(this);
		add(startGame);

        chooseCode = new JButton("Submit");
		chooseCode.setBounds(350, 500, 100, 40);
		chooseCode.addActionListener(this);
		add(chooseCode);

        restart = new JButton("Restart");
		restart.setBounds(350, 500, 100, 40);
		restart.addActionListener(this);
		add(restart);
        restart.setVisible(false);
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
           g.setFont(b);
           g.drawString("Mastermind", 250, 50);
           g.setFont(f);
           g.drawString("Welcome to Mastermind! You are the code maker. Your job", 30, 100);
           g.drawString("is to make a code that the codebreaker can't guess in 10", 30, 125);
           g.drawString("guesses. You will make a code that is 4 colors long. You", 30, 150);
           g.drawString("have 6 colors to choose from for each slot. You can", 30, 175);
           g.drawString("repeat colors if you would like. Then, the codebreaker", 30, 200);
           g.drawString("will guess your code. You will give feedback after each", 30, 225);
           g.drawString("guess. You will send a white circle for each slot of", 30, 250);
           g.drawString("their guess that is the right color but in the wrong", 30, 275);
           g.drawString("spot, a red circle for every slot that is totally", 30, 300);
           g.drawString("correct, and a blank circle for every slot that is", 30, 325);
           g.drawString("totally wrong. Because there are 4 slots in the code,", 30, 350);
           g.drawString("you will send 4 colors (red, white, or blank) as", 30, 375);
           g.drawString("feedback each time. There should be no specific order", 30, 400);
           g.drawString("to your feedback", 30, 425);
        }
        else if(screenSetting == 1){//choosing code
            g.setFont(b);
            g.drawString("Choose the Code", 200, 50);

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
        else if(screenSetting == 2 || screenSetting == 3){//regular screen
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
                    if(fColor == null || fColor.equals(Color.WHITE)){
                        g.setColor(Color.BLACK);
                        g.drawOval(c + x + 250, r + y + 10, 10, 10);

                        if(fColor != null && fColor.equals(Color.WHITE)){
                            fColor = feedback.next();
                        }
                    }
                    else if(fColor != null){
                        g.setColor(fColor);
                        g.fillOval(c + x + 250, r + y + 10, 10, 10);
                        fColor = feedback.next();
                    }
                }
            }

            if(screenSetting == 3){ //giving feedback
                g.setFont(f);
                g.drawString("Enter feedback about the", 440, 100);
                g.drawString("last guess. Red means one", 440, 125);
                g.drawString("slot is totally correct,", 440, 150);
                g.drawString("black means correct color", 440, 175);
                g.drawString("but wrong location, and", 440, 200);
                g.drawString("blank means nothing right.", 440, 225);
                g.drawString("Click to cycle through", 440, 250);
                g.drawString("the colors.", 440, 275);


                g.setColor(Color.BLACK);
                g.drawRoundRect(x + 450, y + 230, 100, 100, 20, 20);

                if(feedbackReset){
                    for(int i = 0; i < 4; i ++){
                        feedbackInput.set(i, Color.WHITE);
                    }

                    feedbackReset = false;
                }

                //drawing the circles for feedback input
                feedbackInput.reset();
                Color fIColor = feedbackInput.next();

                for(int r = 0; r < 100; r += 50){
                    for(int c = 0; c < 100; c += 50){
                        if(fIColor != null){
                            if(fIColor == Color.WHITE){
                                g.setColor(Color.BLACK);
                                g.drawOval(c + x + 460, r + y + 240, 30, 30);
                            }
                            g.setColor(fIColor);
                            g.fillOval(c + x + 460, r + y + 240, 30, 30);
                            fIColor = feedbackInput.next();
                        }
                        else{
                            g.setColor(Color.BLACK);
                            g.drawOval(c + x + 460, r + y + 240, 30, 30);
                        }
                    }
                }
            }
        }
        else if(screenSetting == 4 || screenSetting == 5){
            g.setFont(f);

            if(screenSetting == 4){
                g.drawString("They guessed your code. You lose.", 100, 300);
            }
            else if(screenSetting == 5){
                g.drawString("They didn't guess your code. You win!", 100, 300);
            }

            g.drawString("Code: ", 100, 360);
            g.drawRoundRect(x + 75, y + 250, 200, 50, 20, 20);
            //circles for code
            code.reset();
            Color cColor = code.next();
            for(int c = 0; c < 200; c += 50){
                if(cColor != null){
                    g.setColor(cColor);
                    g.fillOval(x + 85 + c, y + 260, 30, 30);
                    cColor = code.next();
                }
                else{
                    g.drawOval(x + 85 + c, y + 260, 30, 30);
                }
            }
        }
    }

    public void poll() throws IOException, UnknownHostException {
        ServerSocket sSocket = new ServerSocket(portNumber);
        Socket cSocket = sSocket.accept();
        ObjectOutputStream out = new ObjectOutputStream(cSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(cSocket.getInputStream());
        PushbackInputStream pin = new PushbackInputStream(cSocket.getInputStream());

        try {
            while(true){
                if(feedbackSubmit){
                    out.reset();
                    out.writeObject(feedbackInput);

                    feedbackSubmit = false;
                    feedbackReset = true;

                    boolean go = true;
                    for(int i = 0; i < feedbackInput.size(); i ++){
                        if(!feedbackInput.get(i).equals(Color.RED)){
                            go = false;
                            break;
                        }
                    }

                    if(go){
                        screenSetting = 4;
                        restart.setVisible(true);
                    }
                    else{
                        screenSetting = 2;
                    }

                    guessNumber ++;
                    if(guessNumber > 9 && !go){
                        screenSetting = 5;
\                        restart.setVisible(true);
                        submit.setVisible(false);
                    }
                }
                else if(codeSend){
                    codeSend = false;
                    out.reset();
                    out.writeObject(code);
                }
                else if(pin.available() != 0){
\                    DLList inputList = (DLList)in.readObject();

                    guesses = inputList;
                    submitFeedback.setVisible(true);
                    screenSetting = 3;
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

            codeSend = true;
        }
        else if(e.getSource() == submitFeedback){
            screenSetting = 3;
            feedbackSubmit = true;
            submitFeedback.setVisible(false);

            for(int i = 0; i < feedbackInput.size(); i ++){
                feedback.add(feedbackInput.get(i));
            }
        }
        else if(e.getSource() == restart){
            screenSetting = 0;
            currentColor = null;
            feedbackReset = false;
            guessNumber = 0;

            restart.setVisible(false);
            startGame.setVisible(true);

            guesses = new DLList<Color>();
            code = new DLList<Color>();
            feedbackInput = new DLList<Color>();
            feedback = new DLList<Color>();

            feedbackInput.add(Color.WHITE);
            feedbackInput.add(Color.WHITE);
            feedbackInput.add(Color.WHITE);
            feedbackInput.add(Color.WHITE);

            code.add(new Color(242, 242, 242));
            code.add(new Color(242, 242, 242));
            code.add(new Color(242, 242, 242));
            code.add(new Color(242, 242, 242));
        }

        repaint();
    }

    public void mouseReleased(MouseEvent e) {}
 
    public void mouseEntered(MouseEvent e) {}
 
    public void mouseExited(MouseEvent e) {}
 
    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {
        //System.out.println("X: " + e.getX() + " Y: " + e.getY());
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
        else if(screenSetting == 3){
            if(x > 560 && x < 660 && y > 315 && y < 405){
                int c = (x - 560)/50;
                int centerX = 575 + c * 50;

                int r = (y - 305) / 50;
                int centerY = 330 + 50 * r;

                int index = r * 2 + c;

                if(x - centerX < 15 && y - centerY < 15){
                    Color color = feedbackInput.get(index);
                    if(color == Color.WHITE){
                        feedbackInput.set(index, Color.RED);
                    }
                    else if(color == Color.RED){
                       feedbackInput.set(index, Color.BLACK); 
                    }
                    else if(color == Color.BLACK){
                       feedbackInput.set(index, Color.WHITE); 
                    }
                }

            }
        }

        repaint();
    }
}