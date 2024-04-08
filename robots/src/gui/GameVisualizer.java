package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import utils.MathUtils;

public class GameVisualizer extends JPanel implements Observer
{
    private final Timer m_timer = initTimer();
    private final game.Target target;
    private final game.Robot robot;
    
    private static Timer initTimer() 
    {
        return new Timer("events generator", true);
    }
    
    public GameVisualizer(game.Robot robot, game.Target target)
    {
        this.robot = robot;
        this.target = target;

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

        this.robot.addObserver(this);
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

        drawRobot(g2d, robot);
        drawTarget(g2d, target);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawRobot(Graphics2D g, game.Robot robot)
    {
        double direction = robot.getM_robotDirection();
        int x = MathUtils.round(robot.getM_robotPositionX());
        int y = MathUtils.round(robot.getM_robotPositionY());

        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, x, y, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, x  + 10, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x  + 10, y, 5, 5);
    }
    
    private static void drawTarget(Graphics2D g, game.Target target)
    {
        int x = target.getM_targetPositionX();
        int y = target.getM_targetPositionY();

        AffineTransform t = new AffineTransform();
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);

    }

    @Override
    public void update(Observable observable, Object o) {
        if (o.equals(game.Robot.ROBOT_CHANGE_POSITION_SIGNAL)){
            onRedrawEvent();
        }
    }
}
