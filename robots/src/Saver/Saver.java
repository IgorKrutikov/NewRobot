package Saver;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.*;
import java.util.Properties;
import java.util.function.Function;

public class Saver {
    private final File configDir;
    private final File fileName;
    private final File absFilePath;
    private final Properties property = new Properties();

    public Saver(String filename) {

        this.configDir = new File(System.getProperty("user.home") + File.separator + "Robots");

        this.fileName = new File(filename);

        this.absFilePath = new File(this.configDir + File.separator + this.fileName);

        if (!absFilePath.exists()){
            try {
                boolean res = absFilePath.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void save(Container container, String frameName) {

        var bounds = container.getBounds();

        var wrap = getStringWrapper(frameName);


        if (container instanceof JInternalFrame frame) {
            property.setProperty(wrap.apply("isIcon"), String.valueOf(frame.isIcon()));
            property.setProperty(wrap.apply("isMaximum"), String.valueOf(frame.isMaximum()));
        }
        property.setProperty(wrap.apply("xPos"), String.valueOf(bounds.x));
        property.setProperty(wrap.apply("yPos"), String.valueOf(bounds.y));
        property.setProperty(wrap.apply("widthPos"), String.valueOf(bounds.width));
        property.setProperty(wrap.apply("heightPos"), String.valueOf(bounds.height));

    }

    public void flush() {

        try (var fileOutputStream = new BufferedOutputStream(new FileOutputStream(absFilePath))){
            property.store(fileOutputStream, "Stored windows disposition");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static Function<String, String> getStringWrapper(String wrapper) {
        return (label) -> wrapper + "_" + label;
    }

    private void load() {
        try (var fileInputStream = new BufferedInputStream(new FileInputStream(absFilePath))) {
            property.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void fillFrame(Container container, String frameName) {
        if (property.isEmpty()) load();

        var wrap = getStringWrapper(frameName);

        int x = Integer.parseInt(property.getProperty(wrap.apply("xPos"), String.valueOf(container.getX())));
        int y = Integer.parseInt(property.getProperty(wrap.apply("yPos"), String.valueOf(container.getY())));
        int width = Integer.parseInt(property.getProperty(wrap.apply("widthPos"), String.valueOf(container.getWidth())));
        int height = Integer.parseInt(property.getProperty(wrap.apply("heightPos"), String.valueOf(container.getHeight())));

        container.setBounds(x, y, width, height);
        container.setLocation(x, y);

        if (container instanceof JInternalFrame frame){
            Boolean isMaximum = Boolean.parseBoolean( property.getProperty(wrap.apply("isMaximum")));
            Boolean isIcon = Boolean.parseBoolean(property.getProperty(wrap.apply("isIcon")));
            try {
                frame.setMaximum(isMaximum);
                frame.setIcon(isIcon);
            } catch (PropertyVetoException e) {
                throw new RuntimeException(e);
            }
        }

    }
}