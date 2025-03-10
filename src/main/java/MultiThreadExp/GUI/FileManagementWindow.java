package MultiThreadExp.GUI;

import MultiThreadExp.*;
import MultiThreadExp.Objects.Doc;
import MultiThreadExp.Objects.User;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class FileManagementWindow extends CancellableWindow {
    public final JTabbedPane tabbedPane = new JTabbedPane();
    public User user;
    public JTable fileTable;
    public DefaultTableModel fileTableModel;

    public FileManagementWindow(User user) {
        this.setLayout(null);
        this.setSize(400, 400);
        this.user = user;
        this.setLocationRelativeTo(null);

        var docs = Utils.toDataVector(getDocs());
        fileTableModel = new DefaultTableModel(docs, new String[]{"文件名", "路径", "描述", "上传者", "上传时间"});
        fileTable = new JTable() {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        fileTable.setModel(fileTableModel);
        fileTable.setFillsViewportHeight(true);
        fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabbedPane.addTab("上传文件", getUploadPanel());
        tabbedPane.addTab("下载文件", getDownloadPanel());

        this.setContentPane(tabbedPane);
    }

    private List<Doc> getDocs() {
        List<Doc> res = Client.request(
                new Request("get-all-doc"),
                d -> {
                    if (!d.ok()) return List.of();
                    return Utils.deserializeDocList(d.data());
                }
        );
        if (res == null) return List.of();
        return res;
    }

    private JPanel getUploadPanel() {
        var panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        var inputPanel = new JPanel();

        var layout = new GroupLayout(inputPanel);

        var label1 = new JLabel("档案号");
        var label2 = new JLabel("档案描述");
        var label3 = new JLabel("档案文件路径");
        var idField = new JTextField(16);
        var descriptionField = new JTextArea(4, 16);
        var pathField = new JTextField(16);
        var openButton = new JButton("打开");

        var fileChooser = new JFileChooser();

        openButton.addActionListener(e -> {
            int flag = fileChooser.showOpenDialog(this);
            if (flag == JFileChooser.APPROVE_OPTION) {
                File selected = fileChooser.getSelectedFile();
                pathField.setText(selected.getAbsolutePath());
            }
        });

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
        final var uploadButton = getUploadButton(pathField, idField, descriptionField);

        controlPanel.add(uploadButton);
        controlPanel.add(cancelButton);

        panel.add(inputPanel);
        panel.add(controlPanel);

        return panel;
    }

    private @NotNull JButton getUploadButton(JTextField pathField, JTextField idField, JTextArea descriptionField) {
        var uploadButton = new JButton("上传");

        uploadButton.addActionListener(e -> {
            var file = new File(pathField.getText());
            if (!file.exists()) {
                Utils.showErrorDialog("文件不存在");
                return;
            }

            var id = idField.getText();

            var res = Client.request(
                    new Request("doc-exist", id),
                    d -> {
                        if (d.ok()) {
                            if (d.data().equals("yes")) {
                                Utils.showWarnDialog("编号 " + id + " 已存在");
                                return false;
                            }
                            return true;
                        }
                        Utils.showWarnDialog("内部错误 " + d.message());
                        return false;
                    }
            );

            if (res == null) return;
            if (!res) return;

            var doc = new Doc(id, user.getName(), Utils.getCurrentTimestamp(), descriptionField.getText(), pathField.getText());

            if (UserActions.uploadDoc(doc)) {
                Utils.showOKDialog("上传成功");
                fileTableModel.addRow(doc.toDataRow());
            } else {
                Utils.showErrorDialog("上传失败");
            }
        });
        return uploadButton;
    }

    private JPanel getDownloadPanel() {
        var panel = new JPanel();
        panel.setLayout(new BorderLayout());

        var scrollPane = new JScrollPane(fileTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        var controlPanel = new JPanel();
        final var downloadButton = getDownloadButton();

        controlPanel.add(downloadButton);
        controlPanel.add(cancelButton);

        panel.add(controlPanel, BorderLayout.SOUTH);

        return panel;
    }

    private @NotNull JButton getDownloadButton() {
        var downloadButton = new JButton("下载");

        var chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        downloadButton.addActionListener(e -> {
            var selected = fileTable.getSelectedRow();
            if (selected == -1) {
                Utils.showWarnDialog("请选择一个文件");
                return;
            }
            var targetDoc = getDocs().get(selected);
            int flag = chooser.showSaveDialog(this);
            if (flag == JFileChooser.APPROVE_OPTION) {
                var targetDir = chooser.getSelectedFile();
                if (!targetDir.exists()) {
                    if (!targetDir.mkdirs()) {
                        Utils.showErrorDialog("无法创建目录");
                        return;
                    }
                }
                if (UserActions.downloadDoc(targetDoc, targetDir.getAbsolutePath())) {
                    Utils.showOKDialog("下载成功");
                } else {
                    Utils.showErrorDialog("下载失败");
                }
            }
        });

        return downloadButton;
    }
}
