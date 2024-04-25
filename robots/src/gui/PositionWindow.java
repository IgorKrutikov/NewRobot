package gui;

import game.GameLogic;
import game.Robot;

import javax.swing.JTextArea;
import javax.swing.JInternalFrame;

import java.awt.EventQueue;

import java.util.Observable;
import java.util.Observer;


public class PositionWindow extends JInternalFrame implements Observer {

    private final JTextArea textArea = new JTextArea();
    private final GameLogic gameLogic;
    public PositionWindow(GameLogic gameLogic){
        super("Координаты робота", true, true, true, true);

        this.gameLogic = gameLogic;
        this.gameLogic.addObserver(this);

        pack();

        updateCoordinates();
        getContentPane().add(textArea);
    }

    private void updateCoordinates(){
        textArea.setEditable(false);
        String newContent = String.format("(%.2f, %.2f)", gameLogic.getM_robotPositionX(), gameLogic.getM_robotPositionY());
        textArea.setText(newContent);
    }

    @Override
    public void update(Observable observable, Object o){
        if (o.equals(GameLogic.ROBOT_CHANGE_POSITION_SIGNAL)) {
            EventQueue.invokeLater(this::updateCoordinates);
        }
    }
}
