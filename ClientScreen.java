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
import javax.swing.ImageIcon;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class ClientScreen extends JPanel implements ActionListener, MouseListener{
    private DLList<Color> guesses;
    private DLList<Color> feedback;
    private DLList<Color> colorPalette;
    private DLList<Color> code;
    private JButton submit;
    private JButton startGame;
    private JButton restart;
    private boolean guessSubmit;
    private Color yellow;
    private Color teal;
    private Color orange;
    private Color pink;
    private Color blue;
    private Color brown;
    private int screenSetting;
    private int currentRowIndex;
    private Color currentColor;
    private int guessNumber;
    private Font f;
    //private ImageIcon rules;

    public ClientScreen() {
        setLayout(null);
        this.setFocusable(true);
        addMouseListener(this);
        
        guessSubmit = false;
        screenSetting = 0;
        currentRowIndex = 0;
        currentColor = null;
        guessNumber = 0;
        f = new Font("Courier", Font.BOLD, 22);
        //rules = new ImageIcon("Images/clientRules.PNG");

        yellow = new Color(245, 215, 161);
        teal = new Color(155, 194,189);
        orange = new Color(240, 162, 142);
        pink = new Color(186, 99, 117);
        blue = new Color(0, 75, 90);
        brown = new Color(136, 59, 31);

        guesses = new DLList<Color>();
        feedback = new DLList<Color>();
        colorPalette = new DLList<Color>();
        code = new DLList<Color>();

        colorPalette.add(yellow);
        colorPalette.add(orange);
        colorPalette.add(pink);
        colorPalette.add(brown);
        colorPalette.add(blue);
        colorPalette.add(teal);

        startGame = new JButton("Start Game");
        startGame.setBounds(350, 500, 100, 40);
		startGame.addActionListener(this);
		add(startGame);

        submit = new JButton("Submit");
		submit.setBounds(525, 500, 100, 40);
		submit.addActionListener(this);
		add(submit);
        submit.setVisible(false);

        restart = new JButton("Restart");
		restart.setBounds(350, 500, 100, 40);
		restart.addActionListener(this);
		add(restart);
        restart.setVisible(false);
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        int x = 50;
        int y = 75;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 800, 600);

        g.setColor(Color.BLACK);
        if(screenSetting == 0){ //instructions
            //rules.paintIcon(this, g, 0, 0);
        }
        else if(screenSetting == 4){ //losing
            g.setFont(f);
            g.drawString("You lose because you ran out of guesses.", 100, 300);

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
        }
        else if(screenSetting == 5){//winning
            g.setFont(f);
            g.drawString("You guessed the code! You win!", 100, 300);

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
        }
        else{ //screens with board drawn

            int stringX = x - 15;
            int stringY = y + 20;

            if(screenSetting == 1){
                g.setFont(f);
                g.drawString("Enter your guess by clicking", stringX + 350, stringY + 100);
                g.drawString("on the colors from the color", stringX + 350, stringY + 125);
                g.drawString("pallete and then clicking on", stringX + 350, stringY + 150);
                g.drawString("the circles in the first row", stringX + 350, stringY + 175);
                g.drawString("of the guess board. When you", stringX + 350, stringY + 200);
                g.drawString("are ready, hit submit.", stringX + 350, stringY + 225);
            }
            else if(screenSetting == 3){
                g.setFont(f);
                g.drawString("You've recieved feedback on", stringX + 350, stringY + 100);
                g.drawString("your guess. A red circle means", stringX + 350, stringY + 125);
                g.drawString("one of your guesses is in", stringX + 350, stringY + 150);
                g.drawString("the right place and is the ", stringX + 350, stringY + 175);
                g.drawString("right color. A black circle", stringX + 350, stringY + 200);
                g.drawString("means one of your guesses is", stringX + 350, stringY + 225);
                g.drawString("the right color but is in the", stringX + 350, stringY + 250);
                g.drawString("wrong place. A blank circle", stringX + 350, stringY + 275);
                g.drawString("means one of your guesses is", stringX + 350, stringY + 300);
                g.drawString("the wrong color and place.", stringX + 350, stringY + 325);
                g.drawString("Now, make a new guess.", stringX + 350, stringY + 350);
            }

            g.drawRoundRect(x, y, 300, 500, 20, 20);

            for(int i = 50; i < 500; i += 50){
                g.drawLine(x, y + i, x + 300, y + i);
            }

            g.drawLine(x + 240, y, x + 240, y + 500);

            guesses.reset();
            Color color = guesses.next();

            //System.out.println("DLList: " + guesses);
            //drawing the guess circles
            for(int r = 0; r < 500; r += 50){
                for(int c = 0; c < 240; c += 60){
                    //System.out.println(color);
                    if(color != null){
                        g.setColor(color);
                        g.fillOval(c + x + 15, r + y + 10, 30, 30);
                        color = guesses.next();
                    }
                    else{
                        g.setColor(Color.BLACK);
                        g.drawOval(c+ x + 15, r + y + 10, 30, 30);
                        color = guesses.next();
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


            if(screenSetting != 2){
                //drawing the color palette
                g.setColor(Color.BLACK);
                g.fillRoundRect(x + 400, y, 250, 50, 20, 20);
                
                colorPalette.reset();
                Color pColor = colorPalette.next();
                for(int c = 0; c < 240; c += 40){
                    if(pColor != null){
                        g.setColor(pColor);
                        g.fillOval(x + 410 + c, y + 10, 30, 30);
                        pColor = colorPalette.next();
                    }
                    else{
                        g.drawOval(x + 410 + c, y + 10, 30, 30);
                        //pColor = colorPalette.next();
                    }
                }
            }
        }
        
    }


    public void poll() throws IOException, UnknownHostException {
        String hostName = "localhost"; 
        int portNumber = 1024;

        Socket sSocket = new Socket(hostName, portNumber);
        PushbackInputStream pin = new PushbackInputStream(sSocket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(sSocket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(sSocket.getInputStream());

        try {

            while(true){
                if(pin.available() != 0){
                    if(code.get(0) == null){
                        code = (DLList)in.readObject();
                    }
                    else{
                        DLList inputList = (DLList)in.readObject();

                        boolean go = true;

                        for(int i = 0; i < inputList.size(); i ++){
                            feedback.add((Color)inputList.get(i));
                            if(!inputList.get(i).equals(Color.RED)){
                                go = false;
                            }
                        }

                        if(go){
                            screenSetting = 5;
                        }
                        else{
                            screenSetting = 3;
                            submit.setVisible(true);
                            guessNumber ++;

                            if(guessNumber > 1){
                                screenSetting = 4;
                                submit.setVisible(false);
                                restart.setVisible(true);
                            }
                        }
                    }
                    
                    repaint();
                }
                else if(guessSubmit){
                    out.reset();
                    out.writeObject(guesses);

                    guessSubmit = false;

                    submit.setVisible(false);
                }
            }
        }catch (UnknownHostException e) {
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
        if(e.getSource() == startGame){
            screenSetting ++;
            startGame.setVisible(false);
            submit.setVisible(true);
        }
        else if(e.getSource() == submit){
            if(guesses.size() != 0 && guesses.get(currentRowIndex + 3) != null){
                guessSubmit = true;
                submit.setVisible(false);
                screenSetting = 2;
            }  
        }
        else if(e.getSource() == restart){
            guessSubmit = false;
            screenSetting = 0;
            currentRowIndex = 0;
            currentColor = null;
            guessNumber = 0;

            guesses = new DLList<Color>();
            feedback = new DLList<Color>();

            startGame.setVisible(true);
            restart.setVisible(false);
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
        if(x > 460 && x < 690 && y > 65 && y < 115){
            int c = (x-460)/40;
            currentColor = colorPalette.get(c);
        }
        else if(x > 65 && x < 275 && y > 75 && y < 565){
            int tempX = (x-65)/30;
            int c = 0;
            if(tempX % 2 == 0){
                c = (tempX/2);
            }

            int r = (y - 75)/50;

            int index = r * 4 + c;

            System.out.println("R: " + r + "C: " + c);

            int centerX = 80 + 60 * c;
            int centerY = 100 + 50 * r;

            if(x - centerX < 15 && y - centerY < 15 && y > 85 + 50 * guessNumber && y < 115 + 50 * guessNumber){
                if(index < guesses.size()){
                    guesses.set(index, currentColor);
                }
                else{
                    for(int i = guesses.size(); i <= index; i ++){
                        guesses.add(null);
                    }
                    guesses.set(index, currentColor);
                }
            }

            // System.out.println("tempX: " + tempX);
            // System.out.println("c: " + c);
            // System.out.println("tempY: " + tempY);
            // System.out.println(index);
        }

        repaint();
    }
}