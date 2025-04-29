import javax.swing.*;
import javax.script.*;
import java.awt.*;
import java.awt.event.*;

class Window extends JFrame implements ActionListener {
    public JTextArea historyArea;
    public JScrollPane historyAreaScrollPane;

    public JPanel inputPanel;
    public JTextField inputField;
    public JButton equalsButton;
    public JButton clsButton;

    public ScriptEngineManager manager;
    public ScriptEngine engine;
    public ScriptContext context;

    public Window() {
        setTitle("Rosie");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLaf("Metal");

        historyArea = new JTextArea("Rosie v1.0\nType 'help' for a list of functions.\n");
        historyArea.setEditable(false);

        historyAreaScrollPane = new JScrollPane(historyArea);
        historyAreaScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(historyAreaScrollPane, BorderLayout.CENTER);

        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        add(inputPanel, BorderLayout.PAGE_END);

        inputField = new JTextField();
        inputField.setActionCommand("equals");
        inputField.addActionListener(this);
        inputField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    System.out.println(1);
                }
            }

            public void keyReleased(KeyEvent e) {}

            public void keyTyped(KeyEvent e) {}
        });
        inputPanel.add(inputField);

        equalsButton = new JButton("=");
        equalsButton.setActionCommand("equals");
        equalsButton.addActionListener(this);
        inputPanel.add(equalsButton);

        clsButton = new JButton("cls");
        clsButton.setActionCommand("cls");
        clsButton.addActionListener(this);
        inputPanel.add(clsButton);

        manager = new ScriptEngineManager();
        engine = manager.getEngineByExtension("js");
        context = engine.getContext();

        // Init global Math functions
        try {
            engine.eval("var help = ''; var mProps = Object.getOwnPropertyNames(Math);for (var i = 0; i < mProps.length; i++) {this[mProps[i]] = Math[mProps[i]]; help += mProps[i] + ' ';}", context);
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

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("equals")) {
            try {
                Object evalled = engine.eval(inputField.getText(), context);
                if (evalled != null) {
                    historyArea.append(evalled + "\n");
                }
            } catch (Exception ex) {
                historyArea.append("Syntax Error\n");
            }

            inputField.setText("");
            inputField.requestFocus();
        } else if (e.getActionCommand().equals("cls")) {
            historyArea.setText("");
            inputField.requestFocus();
        }
    }
}