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

import game.GameLogic;
import utils.GraphicsUtils;
import utils.MathUtils;

public class GameVisualizer extends JPanel implements Observer
{
    private final Timer m_timer = initTimer();
    private final game.GameLogic gameLogic;
    
    private static Timer initTimer() 
    {
        return new Timer("events generator", true);
    }
    
    public GameVisualizer(GameLogic gameLogic)
    {
        this.gameLogic = gameLogic;
        this.gameLogic.addObserver(this);

        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                gameLogic.onModelUpdateEvent(getWidth(), getHeight());
            }
        }, 0, 10);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                gameLogic.setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }
    
    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;

        drawRobot(g2d, gameLogic);
        drawTarget(g2d, gameLogic);
    }
    
    private static void drawRobot(Graphics2D g, GameLogic gameLogic)
    {
        double direction = gameLogic.getM_robotDirection();

        int x = MathUtils.round(GraphicsUtils.getDPICorrectCoordinate(gameLogic.getM_robotPositionX()));
        int y = MathUtils.round(GraphicsUtils.getDPICorrectCoordinate(gameLogic.getM_robotPositionY()));

        AffineTransform t = AffineTransform.getRotateInstance(direction, x, y);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        GraphicsUtils.fillOval(g, x, y, 30, 10);
        g.setColor(Color.BLACK);
        GraphicsUtils.drawOval(g, x, y, 30, 10);
        g.setColor(Color.WHITE);
        GraphicsUtils.fillOval(g, x  + 10, y, 5, 5);
        g.setColor(Color.BLACK);
        GraphicsUtils.drawOval(g, x  + 10, y, 5, 5);
    }
    
    private static void drawTarget(Graphics2D g, GameLogic gameLogic)
    {
        int x = gameLogic.getM_targetPositionX();
        int y = gameLogic.getM_targetPositionY();

        x = MathUtils.round(GraphicsUtils.getDPICorrectCoordinate(x));
        y = MathUtils.round(GraphicsUtils.getDPICorrectCoordinate(y));

        AffineTransform t = AffineTransform.getRotateInstance(0,0,0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        GraphicsUtils.fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        GraphicsUtils.drawOval(g, x, y, 5, 5);

    }

    @Override
    public void update(Observable observable, Object o) {
        if (o.equals(game.GameLogic.ROBOT_CHANGE_POSITION_SIGNAL)){
            onRedrawEvent();
        }
    }
}
