package Project;

import Project.DataReader.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

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

        // == student 主页 ==
        JPanel studentPanel = new JPanel();
        studentPanel.setLayout(new BorderLayout());
        studentPanel.add(new JLabel("student", SwingConstants.CENTER), BorderLayout.CENTER);
        JButton logoutstudent = new JButton("quit");
        studentPanel.add(logoutstudent, BorderLayout.SOUTH);


        // ==company 主页==
        JPanel companyPanel = new JPanel();
        companyPanel.setLayout(new BorderLayout());
        companyPanel.add(new JLabel("company", SwingConstants.CENTER), BorderLayout.CENTER);
        JButton logoutcompany = new JButton("quit");
        companyPanel.add(logoutcompany, BorderLayout.SOUTH);

        // == staff 主页==
        JPanel staffPanel = new JPanel();
        staffPanel.setLayout(new BorderLayout());
        staffPanel.add(new JLabel("staff", SwingConstants.CENTER), BorderLayout.CENTER);
        JButton logoutstaff = new JButton("quit");
        staffPanel.add(logoutstaff, BorderLayout.SOUTH);


        // 将三个页面添加到主面板
        mainPanel.add(loginPanel, "login");
        mainPanel.add(adminPanel, "admin");
        mainPanel.add(userPanel, "user");
        mainPanel.add(studentPanel, "student");
        mainPanel.add(companyPanel, "company");
        mainPanel.add(staffPanel, "staff");


        // 登录按钮逻辑
        loginButton.addActionListener(e -> {

            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            UserListReader reader = new UserListReader("data", "userlist.csv");
            List<String> row =reader.find("username",username );
            if(row.isEmpty()){JOptionPane.showMessageDialog(frame, "username or password incorrect", "Error", JOptionPane.ERROR_MESSAGE);}
            if(password.equals(row.get(2))){
                if (row.get(3).equals("admin")){cardLayout.show(mainPanel, "admin");}
                else if (row.get(3).equals("student")){cardLayout.show(mainPanel, "student");}
                else if (row.get(3).equals("company")){cardLayout.show(mainPanel, "company");}
                else if (row.get(3).equals("staff")){cardLayout.show(mainPanel, "staff");}

                else{cardLayout.show(mainPanel, "user");}
            }
            else {JOptionPane.showMessageDialog(frame, "password incorrect", "Error", JOptionPane.ERROR_MESSAGE);}




            //cardLayout.show(mainPanel, "admin");//管理员主页
            //cardLayout.show(mainPanel, "user"); //普通用户主页
            //JOptionPane.showMessageDialog(frame, "", "Error", JOptionPane.ERROR_MESSAGE);//登陆失败
            
            // 清空输入框
            usernameField.setText("");
            passwordField.setText("");
        });

        // 退出按钮（两个主页都返回登录页）
        logoutAdmin.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        logoutUser.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        logoutstudent.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        logoutcompany.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        logoutstaff.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        // 显示主面板
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
