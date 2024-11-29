package MultiThreadExp.GUI;

import MultiThreadExp.DataProcessing;
import MultiThreadExp.Objects.Doc;
import MultiThreadExp.Utils;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class FileManagementWindow extends CancellableWindow {
    public final JTabbedPane tabbedPane = new JTabbedPane();

    private List<Doc> getDocs() {
        Enumeration<Doc> docEnumeration;
        try {
            docEnumeration = DataProcessing.getAllFiles();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Collections.list(docEnumeration);
    }

    public FileManagementWindow() {
        this.setLayout(null);
        this.setSize(400, 400);

        tabbedPane.addTab("上传文件", getUploadPanel());
        tabbedPane.addTab("下载文件", getDownloadPanel());

        this.setContentPane(tabbedPane);
    }

    private JPanel getUploadPanel() {
        var panel = new JPanel();

        var inputPanel = new JPanel();

        var layout = new GroupLayout(inputPanel);

        var label1 = new JLabel("档案号");
        var label2 = new JLabel("档案描述");
        var label3 = new JLabel("档案文件路径");
        var idField = new JTextField(16);
        var descriptionField = new JTextArea(4, 16);
        var pathField = new JTextField(16);
        var openButton = new JButton("打开");

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(label1)
                                        .addComponent(label2)
                                        .addComponent(label3)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(idField)
                                        .addComponent(descriptionField)
                                        .addGroup(
                                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(pathField)
                                                        .addComponent(openButton)
                                        )
                        )
        );
        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label1)
                                        .addComponent(idField)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label2)
                                        .addComponent(descriptionField)
                        )
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(label3)
                                        .addComponent(pathField)
                        )
                        .addComponent(openButton)
        );

        inputPanel.setLayout(layout);

        var controlPanel = new JPanel();
        var uploadButton = new JButton("上传");

        controlPanel.add(uploadButton);
        controlPanel.add(cancelButton);

        panel.add(inputPanel);
        panel.add(controlPanel);

        return panel;
    }

    private JPanel getDownloadPanel() {
        var panel = new JPanel();
        panel.setLayout(new BorderLayout());
        var data = getDocs().stream().map(d -> new String[]{d.getFilename(), d.getFilepath(), d.getDescription(), d.getCreator(), Utils.formatTimestamp(d.getTimestamp())}).toList().toArray(new String[0][0]);
        var table = new JTable(data, new String[]{"文件名", "路径", "描述", "上传者", "上传时间"}) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        var scrollPane = new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        var controlPanel = new JPanel();
        var downloadButton = new JButton("下载");

        controlPanel.add(downloadButton);
        controlPanel.add(cancelButton);

        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }
}
