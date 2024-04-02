package game;

import utils.MathUtils;

public class Robot {
    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;
    public static final double maxVelocity = 0.1;
    public static final double maxAngularVelocity = 0.001;

    public double getM_robotPositionX(){
        return m_robotPositionX;
    }

    public double getM_robotPositionY(){
        return m_robotPositionY;
    }

    public double getM_robotDirection(){
        return m_robotDirection;
    }

    public void moveRobot(double velocity, double angularVelocity, double duration, double xLimit, double yLimit)
    {
        velocity = MathUtils.applyLimits(velocity, 0, maxVelocity);
        angularVelocity = MathUtils.applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);

        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }

        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }

        m_robotPositionX = MathUtils.applyLimits(newX, 0, xLimit);
        m_robotPositionY = MathUtils.applyLimits(newY, 0, yLimit);
        m_robotDirection = MathUtils.asNormalizedRadians(m_robotDirection + angularVelocity * duration);
    }
}
