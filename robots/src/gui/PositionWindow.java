package gui;

import game.Robot;

import javax.swing.JTextArea;
import javax.swing.JInternalFrame;

import java.awt.EventQueue;

import java.util.Observable;
import java.util.Observer;


public class PositionWindow extends JInternalFrame implements Observer {

    private final JTextArea textArea = new JTextArea();
    private final game.Robot robot;
    public PositionWindow(game.Robot robot){
        super("Координаты робота", true, true, true, true);

        this.robot = robot;
        this.robot.addObserver(this);

        pack();

        updateCoordinates();
        getContentPane().add(textArea);
    }

    private void updateCoordinates(){
        textArea.setEditable(false);
        String newContent = String.format("(%.2f, %.2f)", robot.getM_robotPositionX(), robot.getM_robotPositionY());
        textArea.setText(newContent);
    }

    @Override
    public void update(Observable observable, Object o){
        if (o.equals(Robot.ROBOT_CHANGE_POSITION_SIGNAL)) {
            EventQueue.invokeLater(this::updateCoordinates);
        }
    }
}
