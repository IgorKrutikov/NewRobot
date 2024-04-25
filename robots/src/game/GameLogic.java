package game;

import utils.MathUtils;

import java.awt.Point;
import java.util.Observable;

public class GameLogic extends Observable {
    public static final String ROBOT_CHANGE_POSITION_SIGNAL = "ROBOT CHANGED POSITION";
    private final Robot robot;
    private final Target target;

    private int widthBound = 400;
    private int heightBound = 400;

    public GameLogic() {
        this.robot = new Robot();
        this.target = new Target();
    }

    public void onModelUpdateEvent()
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

        this.robot.moveRobot(velocity, angularVelocity, 10, widthBound, heightBound);

        this.setChanged();
        this.notifyObservers(GameLogic.ROBOT_CHANGE_POSITION_SIGNAL);
        this.clearChanged();

    }

    public void setTargetPosition(Point point) {
        target.setTargetPosition(point);
    }

    public int getM_targetPositionX() {
        return target.getM_targetPositionX();
    }

    public int getM_targetPositionY() {
        return target.getM_targetPositionY();
    }

    public double getM_robotPositionX() {
        return robot.getM_robotPositionX();
    }

    public double getM_robotPositionY() {
        return robot.getM_robotPositionY();
    }

    public double getM_robotDirection() {
        return robot.getM_robotDirection();
    }

    public void correctLimits(int newWidth, int newHeight) {
        target.correctPosition(newWidth, newHeight);
        widthBound = newWidth;
        heightBound = newHeight;
    }
}
