package game;

import java.awt.*;

public class Target {

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    public void setTargetPosition(Point p)
    {
        this.m_targetPositionX = p.x;
        this.m_targetPositionY = p.y;
    }

    public int getM_targetPositionX(){
        return m_targetPositionX;
    }

    public int getM_targetPositionY(){
        return m_targetPositionY;
    }
}
