package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.swing.Timer;
import javax.swing.JOptionPane;

/**
 * Created by KLIPST on 06.10.2014.
 */

/*
   Корабли расставляются автоматически для обоих игроков.
   В левом окне - поле игрока, в правом - поле компьютера.
   В случае пропуска хода игроком, ждём пока компьютер сделает свой ход.
 */

public class Main extends Application {

    public static void main(String[] args) {

        final tableSea frame2 = new tableSea(true,  0);
        final tableSea frame1 = new tableSea(false, 1);

        frame1.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame2.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });


        final Random rnd = new Random();
        final Timer timer = new Timer(1000, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if(!frame2.privacy){
                    int x = rnd.nextInt(9);
                    int y = rnd.nextInt(9);
                    int next = frame1.hit(x, y);
                    if (next >= 0){
                        if(next != 1){
                            frame2.changePrivacy();
                        }
                    }
                }
                if (!frame2.aliveCount()) {
                    JOptionPane.showMessageDialog(frame2, "Вы выиграли!");
                    frame2.changePrivacy();
                    timer.stop();
                }
                if (!frame1.aliveCount()) {
                    JOptionPane.showMessageDialog(frame1, "Вы проиграли!");
                    timer.stop();
                }
            }
        }
        );
        timer.start();

        frame1.setLocation(300, 300);
        frame2.setLocation(750, 300);
        frame1.setTitle("Player's field");
        frame2.setTitle("Opponent's field (Computer)");

        frame2.setVisible(true);
        frame1.setVisible(true);
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
    }
}
