package Project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Application {
    public static void main(String[] args) {
        new Application().createUI();
    }

    private void createUI() {
        JFrame frame = new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 250);

        // 创建 CardLayout 管理的主面板
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // === 登录页 ===
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginPanel.add(new JLabel("username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("password:"));
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel(""));
        loginPanel.add(loginButton);

        // === 管理员主页 ===
        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new BorderLayout());
        adminPanel.add(new JLabel("admin", SwingConstants.CENTER), BorderLayout.CENTER);
        JButton logoutAdmin = new JButton("quit");
        adminPanel.add(logoutAdmin, BorderLayout.SOUTH);

        // === 普通用户主页 ===
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        userPanel.add(new JLabel("user", SwingConstants.CENTER), BorderLayout.CENTER);
        JButton logoutUser = new JButton("quit");
        userPanel.add(logoutUser, BorderLayout.SOUTH);

        // 将三个页面添加到主面板
        mainPanel.add(loginPanel, "login");
        mainPanel.add(adminPanel, "admin");
        mainPanel.add(userPanel, "user");

        // 登录按钮逻辑
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // 简单模拟登录判断（你可以改成从数据库验证）
            if (username.equals("admin") && password.equals("123")) {
                cardLayout.show(mainPanel, "admin"); // 管理员主页
            } else if (username.equals("user") && password.equals("123")) {
                cardLayout.show(mainPanel, "user"); // 普通用户主页
            } else {
                JOptionPane.showMessageDialog(frame, " ", "login failed", JOptionPane.ERROR_MESSAGE);
            }

            // 清空输入框
            usernameField.setText("");
            passwordField.setText("");
        });

        // 退出按钮（两个主页都返回登录页）
        logoutAdmin.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        logoutUser.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        // 显示主面板
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
