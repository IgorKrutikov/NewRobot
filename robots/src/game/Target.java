package game;

import utils.MathUtils;

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

    public void correctPosition(int width, int height){
        int newX = MathUtils.round(MathUtils.applyLimits(m_targetPositionX, 0, width));
        m_targetPositionX = newX;
        int newY =  MathUtils.round(MathUtils.applyLimits(m_targetPositionY, 0, height));
        m_targetPositionY = newY;
    }
}
