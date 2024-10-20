import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UI_File_Well{
    public static void main(String[] args) {
        // 创建主框架
        JFrame frame = new JFrame("Login and File Encryption Interface");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // 创建面板
        JPanel panel = new JPanel();
        frame.add(panel);
        placeLoginComponents(panel, frame);

        // 设置框架可见
        frame.setVisible(true);
    }

    private static void placeLoginComponents(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        // 创建用户标签
        JLabel userLabel = new JLabel("User:");
        userLabel.setBounds(50, 50, 80, 25);
        panel.add(userLabel);

        // 创建文本域用于用户输入
        JTextField userText = new JTextField(20);
        userText.setBounds(150, 50, 165, 25);
        panel.add(userText);

        // 创建密码标签
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 100, 80, 25);
        panel.add(passwordLabel);

        // 创建密码域用于用户输入
        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(150, 100, 165, 25);
        panel.add(passwordText);

        // 创建登录按钮
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 150, 80, 25);
        panel.add(loginButton);

        // 登录按钮的点击事件
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userText.getText();
                String password = new String(passwordText.getPassword());

                // 简单验证
                if (user.equals("admin") && password.equals("admin")) {
                    JOptionPane.showMessageDialog(null, "Login Successful!");
                    frame.getContentPane().removeAll();
                    JPanel encryptionPanel = new JPanel();
                    frame.add(encryptionPanel);
                    placeEncryptionComponents(encryptionPanel, frame);
                    frame.revalidate();
                    frame.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static void placeEncryptionComponents(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        // 创建选择文件按钮
        JButton chooseFileButton = new JButton("Choose File");
        chooseFileButton.setBounds(50, 50, 120, 25);
        panel.add(chooseFileButton);

        // 显示选择的文件路径
        JTextField filePathField = new JTextField(20);
        filePathField.setBounds(200, 50, 250, 25);
        filePathField.setEditable(false);
        panel.add(filePathField);

        // 创建加密按钮
        JButton encryptButton = new JButton("Encrypt");
        encryptButton.setBounds(50, 100, 120, 25);
        panel.add(encryptButton);

        // 创建解密按钮
        JButton decryptButton = new JButton("Decrypt");
        decryptButton.setBounds(200, 100, 120, 25);
        panel.add(decryptButton);

        // 创建结果标签
        JLabel resultLabel = new JLabel("Result:");
        resultLabel.setBounds(50, 150, 80, 25);
        panel.add(resultLabel);

        // 创建结果显示区域
        JTextArea resultArea = new JTextArea();
        resultArea.setBounds(50, 180, 400, 150);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setEditable(false);
        panel.add(resultArea);

        // 文件选择器
        JFileChooser fileChooser = new JFileChooser();

        // 选择文件按钮的点击事件
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePathField.setText(selectedFile.getAbsolutePath());
                    System.out.println(filePathField);
                }
            }
        });

        // 加密按钮的点击事件
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File inputFile = new File(filePathField.getText());
                System.out.println(inputFile);
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                if (inputFile.exists()) {
                    try {
                        File encryptedFile = new File(inputFile.getAbsolutePath() + ".enc");
                        encryptFile(inputFile, encryptedFile);
                        resultArea.setText("File encrypted successfully: " + encryptedFile.getAbsolutePath()+date);
                    } catch (IOException ioException) {
                        resultArea.setText("Error encrypting file: " + ioException.getMessage()+date);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    resultArea.setText("File not found!");
                }
            }
        });

        // 解密按钮的点击事件
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File inputFile = new File(filePathField.getText());
                System.out.println(inputFile);
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                Date date = new Date(System.currentTimeMillis());
                System.out.println(formatter.format(date));
                if (inputFile.exists()) {
                    try {
                        File decryptedFile = new File(inputFile.getAbsolutePath().replace(".enc", "") + ".dec");
                        decryptFile(inputFile, decryptedFile);

                        resultArea.setText("File decrypted successfully: " + decryptedFile.getAbsolutePath()+date);
                    } catch (IOException ioException) {

                        resultArea.setText("Error decrypting file: " + ioException.getMessage()+date);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    resultArea.setText("File not found!");
                }
            }
        });
    }

    // 文件加密方法
    private static void encryptFile(File inputFile, File outputFile) throws Exception {
        RSA_UI.main(inputFile.toString(), outputFile.toString());
    }

    // 文件解密方法
    private static void decryptFile(File inputFile, File outputFile) throws Exception {
        RSA_UI.main(inputFile.toString(), outputFile.toString());
    }

}
