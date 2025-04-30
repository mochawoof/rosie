import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;

class Window extends JFrame {
    public JTextArea textArea;
    public JScrollPane textAreaScrollPane;

    public Globals globals;

    public int selectedLine = 2;

    public Window() {
        setTitle("Rosie");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLaf("System");

        try {
            setIconImage(new ImageIcon(getClass().getResource("icon64.png").toURI().toURL()).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        textArea = new JTextArea("Rosie v1.1\nType 'help' to see all functions.\n> ");

        globals = JsePlatform.standardGlobals();
        try {
            globals.load(new Scanner(getClass().getResourceAsStream("init.lua")).useDelimiter("\\A").next()).call();
        } catch (Exception e) {
            e.printStackTrace();
        }

        textArea.setCaretPosition(textArea.getText().length());
        textArea.addKeyListener(new KeyListener() {
            public void keyReleased(KeyEvent e) {
                String[] lines = textArea.getText().split("\n");
                String rawLine = lines[lines.length - 1];
                String line = rawLine.replace("> ", "").replace(">", "");
                
                int code = e.getKeyCode();

                if (code == KeyEvent.VK_ENTER) {
                    if (rawLine.startsWith(">")) {
                        if (line.equals("cls")) {
                            lines = new String[0];
                        } else {
                            Object evalled = null;
                            try {
                                evalled = globals.load("return " + line).call();
                                if (evalled != null) {
                                    lines[lines.length - 1] = line + " = " + evalled;
                                }
                            } catch (Exception ex) {
                                lines[lines.length - 1] = line + " = Syntax Error";
                            }

                            try {
                                globals.load("ans = '" + evalled + "'").call();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                        selectedLine = lines.length;
                    }

                    textArea.setText("");
                    for (String l : lines) {
                        textArea.append(l + "\n");
                    }
                    textArea.append("> ");
                } else if (code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
                    boolean good = false;
                    if (code == KeyEvent.VK_UP) {
                        if (selectedLine - 1 >= 0) {
                            good = true;
                            selectedLine--;
                        }
                    } else if (code == KeyEvent.VK_DOWN) {
                        if (selectedLine + 1 < lines.length - 1) {
                            good = true;
                            selectedLine++;
                        }
                    }

                    String rawLastLine = lines[selectedLine];
                    if (rawLastLine.lastIndexOf("=") != -1) {
                        rawLastLine = rawLastLine.substring(0, rawLastLine.lastIndexOf("=")).trim();
                    }

                    lines[lines.length - 1] = rawLastLine;
                    if (!rawLastLine.startsWith(">") || !rawLastLine.startsWith("> ")) {
                        lines[lines.length - 1] = "> " + rawLastLine;
                    }

                    textArea.setText("");
                    for (int i = 0; i < lines.length; i++) {
                        String l = lines[i];
                        textArea.append(l + ((i == lines.length - 1) ? "" : "\n"));
                    }
                }
            }

            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
                    e.consume();
                }
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