package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import log.Logger;

import Saver.Saver;
/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();

    private final Saver saver;
    private final GameWindow gameWindow;
    private final LogWindow logWindow;

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int inset = 50;
        setBounds(inset, inset, screenSize.width  - inset *2,screenSize.height - inset *2);

        setContentPane(desktopPane);

        this.saver = new Saver("robotsFrames.properties");

        logWindow = createLogWindow();

        gameWindow = createGameWindow();

        addWindow(logWindow);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeApplicationConfirm();
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private GameWindow createGameWindow() {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);

        saver.fillFrame(gameWindow, "GameWindow");
        return gameWindow;
    }

    private void closeApplicationConfirm() {
        int res = JOptionPane.showConfirmDialog(this, "Выйти из программы?", "Выход", JOptionPane.YES_NO_OPTION);
        if (res == JOptionPane.YES_OPTION) {

            saver.save(gameWindow, "GameWindow");
            saver.save(logWindow, "LogWindow");
            saver.flush();

            this.gameWindow.dispose();
            this.logWindow.dispose();
            this.dispose();

        }
    }

    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");

        saver.fillFrame(logWindow,"LogWindow");

        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    private JMenu createMenu(String title, int mnemonic, String description){
        JMenu menu = new JMenu(title);
        menu.setMnemonic(mnemonic);
        menu.getAccessibleContext().setAccessibleDescription(description);
        return menu;
    }

    private JMenuItem createMenuItem(String title, int mnemonic, ActionListener listener) {
        JMenuItem item = new JMenuItem(title, mnemonic);
        if (listener != null) {
            item.addActionListener(listener);
        }
        return item;
    }
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();


        JMenu lookAndFeelMenu = createMenu("Режим отображения", KeyEvent.VK_V, "Управление режимом отображения приложения");

        lookAndFeelMenu.add(createMenuItem("Системная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        }));
        lookAndFeelMenu.add(createMenuItem("Универсальная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        }));


        JMenu testMenu = createMenu("Тесты", KeyEvent.VK_T, "Тестовые команды");
        testMenu.add(createMenuItem("Сообщение в лог", KeyEvent.VK_S, (event) -> Logger.debug("Новая строка")));


        JMenu file = createMenu("Файл", KeyEvent.VK_Z, "Команды управления состояния программы");
        file.add(createMenuItem("Выход", KeyEvent.VK_Z, (event) -> this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING))));


        menuBar.add(file);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }
    
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
