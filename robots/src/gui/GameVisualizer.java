package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import game.Robot;
import utils.MathUtils;

public class GameVisualizer extends JPanel
{
    private final Timer m_timer = initTimer();
    private final game.Target target = new game.Target();
    private final game.Robot robot = new Robot();
    
    private static Timer initTimer() 
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }
    
    public GameVisualizer() 
    {
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onModelUpdateEvent();
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                target.setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }
    
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }
    
    protected void onModelUpdateEvent()
    {
        int targetX = target.getM_targetPositionX();
        int targetY = target.getM_targetPositionY();

        double robotX = robot.getM_robotPositionX();
        double robotY = robot.getM_robotPositionY();
        double m_robotDirection = robot.getM_robotDirection();

        double distance = MathUtils.distance(targetX, targetY,
            robotX, robotY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = game.Robot.maxVelocity;
        double angleToTarget = MathUtils.angleTo(robotX, robotY, targetX, targetY);

        double angularVelocity = 0;
        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = game.Robot.maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -game.Robot.maxAngularVelocity;
        }


        this.robot.moveRobot(velocity, angularVelocity, 10, this.getWidth(), this.getHeight());
    }

    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;

        drawRobot(g2d, MathUtils.round(robot.getM_robotPositionX()), MathUtils.round(robot.getM_robotPositionY()), robot.getM_robotDirection());
        drawTarget(g2d, target.getM_targetPositionX(), target.getM_targetPositionY());
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private void drawRobot(Graphics2D g, int x, int y, double direction)
    {
        int robotCenterX = MathUtils.round(robot.getM_robotPositionX());
        int robotCenterY = MathUtils.round(robot.getM_robotPositionY());
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY); 
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, 5, 5);
    }
    
    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);

    }

    public void dropTimerSchedule(){
        this.m_timer.cancel();
        this.m_timer.purge();
    }
}
