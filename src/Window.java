import javax.swing.*;
import javax.script.*;
import java.awt.*;
import java.awt.event.*;

class Window extends JFrame {
    public JTextArea textArea;
    public JScrollPane textAreaScrollPane;

    public ScriptEngineManager manager;
    public ScriptEngine engine;
    public ScriptContext context;

    public Window() {
        setTitle("Rosie");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLaf("System");

        manager = new ScriptEngineManager();
        engine = manager.getEngineByExtension("js");
        context = engine.getContext();

        // Init global Math functions
        try {
            engine.eval("var help = 'help cls '; var mProps = Object.getOwnPropertyNames(Math);for (var i = 0; i < mProps.length; i++) {this[mProps[i]] = Math[mProps[i]]; help += mProps[i] + ' ';}", context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        textArea = new JTextArea("Rosie v1.0\nType 'help' to see all functions.\n> ");
        textArea.setCaretPosition(textArea.getText().length());
        textArea.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String[] lines = textArea.getText().split("\n");
                    String rawLine = lines[lines.length - 1];
                    String line = rawLine.replace("> ", "").replace(">", "");
                    
                    if (rawLine.startsWith(">")) {
                        if (line.startsWith("cls")) {
                            lines = new String[0];
                        } else {
                            try {
                                lines[lines.length - 1] = engine.eval(line, context) + "";
                            } catch (Exception ex) {
                                lines[lines.length - 1] = "Syntax Error";
                            }
                        }
                    }

                    textArea.setText("");
                    for (String l : lines) {
                        textArea.append(l + "\n");
                    }
                    textArea.append("> ");
                }
            }

            public void keyPressed(KeyEvent e) {
                e.consume();
            }

            public void keyTyped(KeyEvent e) {}
        });

        textAreaScrollPane = new JScrollPane(textArea);
        textAreaScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(textAreaScrollPane, BorderLayout.CENTER);

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