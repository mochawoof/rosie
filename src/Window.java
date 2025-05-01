import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

class Window extends JFrame {
    public JTextArea consoleArea;
    public JScrollPane consoleAreaScrollPane;

    public Globals globals;

    public Window() {
        setTitle("Rosie");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLaf("Metal");

        try {
            setIconImage(new ImageIcon(getClass().getResource("icon64.png").toURI().toURL()).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        consoleArea = new JTextArea("");
        consoleAreaScrollPane = new JScrollPane(consoleArea);
        consoleAreaScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(consoleAreaScrollPane, BorderLayout.CENTER);

        globals = JsePlatform.standardGlobals();
        try {
            globals.load(new Scanner(getClass().getResourceAsStream("init.lua")).useDelimiter("\\A").next()).call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        setVisible(true);
    }

    public void setLaf(String lafName) {

        if (lafName.equals("System")) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        for (UIManager.LookAndFeelInfo lafInfo : UIManager.getInstalledLookAndFeels()) {
            if (lafInfo.getName().equals(lafName)) {
                try {
                    UIManager.setLookAndFeel(lafInfo.getClassName());
                    SwingUtilities.updateComponentTreeUI(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }
}