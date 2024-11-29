package MultiThreadExp.GUI;

import javax.swing.*;

public abstract class CancellableWindow extends JFrame {
    public JButton cancelButton = new JButton("取消");

    public CancellableWindow() {
        this.cancelButton.addActionListener(e -> this.setVisible(false));
    }
}
