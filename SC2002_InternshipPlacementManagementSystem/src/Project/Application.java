package Project;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import Project.classes.*;
import Project.DataReader.*;

public class Application {
    private AppUser user;
    private Student student;
    private CompanyRepresentative company;
    private CareerStaff staff;
    private JTabbedPane studentTabbedPane;
    private JLabel studentTitleLabel;
    private JTabbedPane companyTabbedPane;
    private JLabel cTitleLabel;

    public static void main(String[] args) throws IOException {
        new Application().createUI();
    }

    private void createUI() throws IOException {
        JFrame frame = new JFrame("test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width * 3 / 4;
        int height = screenSize.height * 5 / 6;
        frame.setSize(width, height);

        // Create the main panel managed by CardLayout
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        /** === Login Page === */
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        Font font40 = new Font("SansSerif", Font.PLAIN, 40);
        Font font30 = new Font("SansSerif", Font.PLAIN, 30);
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JLabel userLabel = new JLabel("username:");
        JLabel passLabel = new JLabel("password:");
        JLabel emptyLabel = new JLabel("");

        usernameField.setFont(font30);
        passwordField.setFont(font30);
        loginButton.setFont(font40);
        userLabel.setFont(font40);
        passLabel.setFont(font40);
        emptyLabel.setFont(font40);

        loginPanel.add(userLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passLabel);
        loginPanel.add(passwordField);
        loginPanel.add(emptyLabel);
        loginPanel.add(loginButton);

        // Login button logic
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            UserListReader reader = new UserListReader("data", "userlist.csv");
            List<String> row = reader.find("username", username);
            if (row.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "username or password incorrect", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            user = new AppUser(row);
            if (user.checkPassword(password)) {
                //to student
                if (row.get(3).equals("student")) {
                    cardLayout.show(mainPanel, "student");
                    frame.setTitle("student");
                    //  use DatabaseLoader to load complete student data
                    DatabaseLoader loader = new DatabaseLoader();
                    try {
                        loader.loadAllData();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    // Get the student object from the loader
                    student = loader.getStudentById(row.get(0));
                    // If it is empty, create a basic student object
                    if (student == null) {
                        StudentListReader studentListReader = new StudentListReader("data", "student_list.csv");
                        List<String> studentRow = studentListReader.find("StudentID", row.get(0));
                        if (!studentRow.isEmpty()) {
                            student = new Student(studentRow.get(0), studentRow.get(1), Integer.parseInt(studentRow.get(3)), studentRow.get(2), studentRow.get(4));
                        }
                    }
                    StudentService studentService = new StudentService(student, studentTabbedPane, studentTitleLabel);
                    studentService.initializeStudentPanel(studentTabbedPane, studentTitleLabel);
                }

                //to company
                else if (row.get(3).equals("company")) {
                    cardLayout.show(mainPanel, "company");
                    frame.setTitle("company");
                    CompanyListReader companyListReader = new CompanyListReader("data", "company_list.csv");
                    List<String> companyRow = companyListReader.find("CompanyRepID", row.get(0));
                    company = new CompanyRepresentative(companyRow.get(0), companyRow.get(2), companyRow.get(1), companyRow.get(3), companyRow.get(4));
                    // 使用 CompanyService 初始化公司代表面板
                    CompanyService companyService = new CompanyService(company, companyTabbedPane, cTitleLabel);
                    companyService.initializeCompanyPanel(companyTabbedPane, cTitleLabel);
                }

                //to staff
                else if (row.get(3).equals("staff")) {
                    cardLayout.show(mainPanel, "staff");
                    frame.setTitle("staff");
                    StaffListReader staffListReader = new StaffListReader("data", "staff_list.csv");
                    List<String> staffRow = staffListReader.find("StaffID", row.get(0));
                    staff = new CareerStaff(staffRow.get(0), staffRow.get(1), staffRow.get(2), staffRow.get(3));
                }
            } else {
                JOptionPane.showMessageDialog(frame, "password incorrect", "Error", JOptionPane.ERROR_MESSAGE);
            }
            // Clear the input box
            usernameField.setText("");
            passwordField.setText("");
        });



        //========================================
        /** == student page ==  */
        JPanel studentPanel = new JPanel();
        studentPanel.setLayout(new BorderLayout());

        // Use member variables instead of local variables
        studentTitleLabel = new JLabel("Student", SwingConstants.CENTER);
        studentTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        studentPanel.add(studentTitleLabel, BorderLayout.NORTH);

        // Use member variables instead of local variables
        studentTabbedPane = new JTabbedPane();
        studentPanel.add(studentTabbedPane, BorderLayout.CENTER);

        // ===== Bottom Buttons =====
        JPanel studentBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton logoutStudent = new JButton("quit");
        studentBottomPanel.add(logoutStudent);
        JButton changePassword = new JButton("Change Password");
        changePassword.addActionListener(e -> changePasswordUI(frame));
        studentBottomPanel.add(changePassword);
        studentPanel.add(studentBottomPanel, BorderLayout.SOUTH);


        /** ==company page== */
        JPanel companyPanel = new JPanel();
        companyPanel.setLayout(new BorderLayout());

        // create an empty title tag
        cTitleLabel = new JLabel("Company Representative", SwingConstants.CENTER);  // 移除前面的 JLabel
        cTitleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        companyPanel.add(cTitleLabel, BorderLayout.NORTH);

        // Create an empty tab panel
        companyTabbedPane = new JTabbedPane();
        companyPanel.add(companyTabbedPane, BorderLayout.CENTER);

        // ===== Bottom button =====
        JPanel companyBottomPanel = new JPanel(new FlowLayout());
        JButton logoutCompany = new JButton("quit");
        JButton companyChangePasswordButton = new JButton("Change Password");
        companyChangePasswordButton.addActionListener(e -> changePasswordUI(frame));
        companyBottomPanel.add(logoutCompany);
        companyBottomPanel.add(companyChangePasswordButton);
        companyPanel.add(companyBottomPanel, BorderLayout.SOUTH);


        /** == staff page== */
        JPanel staffPanel = new JPanel();
        staffPanel.setLayout(new BorderLayout());
        JButton logoutStaff = new JButton("quit");

        JLabel topLabel = new JLabel("", SwingConstants.CENTER);
        topLabel.setFont(new Font("Arial", Font.BOLD, 18));
        staffPanel.add(topLabel, BorderLayout.NORTH);

        // top
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(new JLabel("Wait List", SwingConstants.CENTER));
        topPanel.add(new JLabel("Internship Reports", SwingConstants.CENTER));
        // Change password button logic
        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.addActionListener(e -> changePasswordUI(frame));
        staffPanel.add(topPanel, BorderLayout.NORTH);

        // ==== Middle: Scrollable content =====
        JPanel leftContent = new JPanel();
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.Y_AXIS));
        JPanel rightContent = new JPanel();
        rightContent.setLayout(new BorderLayout()); // 改为BorderLayout以更好地组织报告界面
        JScrollPane leftScroll = new JScrollPane(leftContent);
        leftScroll.getVerticalScrollBar().setUnitIncrement(16);
        JScrollPane rightScroll = new JScrollPane(rightContent);
        rightScroll.getVerticalScrollBar().setUnitIncrement(16);
        JPanel staffCenterPanel = new JPanel(new GridLayout(1, 2));
        staffCenterPanel.add(leftScroll);
        staffCenterPanel.add(rightScroll);

        //
        initializeReportPanel(rightContent);

        // Loading waitlist data
        DatabaseLoader databaseLoader = new DatabaseLoader();
        databaseLoader.loadAllData();

        List<CompanyRepresentative> pendingCompanies = databaseLoader.getCompanyRepsByStatus("Pending");
        List<Internship> pendingInternships = databaseLoader.getInternshipsByStatus("Pending");
        List<InternshipApplication> withdrawalRequests = databaseLoader.getApplicationWithdrawal(true);
        List<InternshipApplication> pendingApplications = databaseLoader.getApplicationsByStatus("Pending");

        // Remove applications with withdrawal requests from pendingApplications
        List<InternshipApplication> filteredPendingApplications = new ArrayList<>();
        for (InternshipApplication app : pendingApplications) {
            if (!app.isWithdrawalRequested()) {
                filteredPendingApplications.add(app);
            }
        }

        //
        leftContent.removeAll();

        // Add a company representative pending approval
        if (!pendingCompanies.isEmpty()) {
            leftContent.add(new JLabel("Pending Company Representatives:"));
            leftContent.add(Box.createRigidArea(new Dimension(0, 10)));

            for (CompanyRepresentative company : pendingCompanies) {
                leftContent.add(createCompanyApprovalRow(company, leftContent));
                leftContent.add(Box.createRigidArea(new Dimension(0, 5)));
            }
            leftContent.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        // Add internship opportunities pending approval
        if (!pendingInternships.isEmpty()) {
            leftContent.add(new JLabel("Pending Internships:"));
            leftContent.add(Box.createRigidArea(new Dimension(0, 10)));

            for (Internship internship : pendingInternships) {
                leftContent.add(createInternshipApprovalRow(internship, leftContent));
                leftContent.add(Box.createRigidArea(new Dimension(0, 5)));
            }
            leftContent.add(Box.createRigidArea(new Dimension(0, 20)));
        }



        // Add pending withdrawal request
        if (!withdrawalRequests.isEmpty()) {
            leftContent.add(new JLabel("Withdrawal Requests:"));
            leftContent.add(Box.createRigidArea(new Dimension(0, 10)));

            for (InternshipApplication application : withdrawalRequests) {
                leftContent.add(createWithdrawalRequestRow(application, leftContent));
                leftContent.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        // If there is no pending project, display a prompt message
        if (pendingCompanies.isEmpty() && pendingInternships.isEmpty() && withdrawalRequests.isEmpty() && filteredPendingApplications.isEmpty()) {
            leftContent.add(new JLabel("No pending items in wait list"));
        }

        leftContent.revalidate();
        leftContent.repaint();

        staffPanel.add(staffCenterPanel, BorderLayout.CENTER);


        // =====  Bottom button  =====
        JPanel stuffBottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        stuffBottomPanel.add(logoutStaff);
        stuffBottomPanel.add(changePasswordButton);
        staffPanel.add(stuffBottomPanel, BorderLayout.SOUTH);


        //=================================







        // Add all the pages to the main panel
        mainPanel.add(loginPanel, "login");
        mainPanel.add(studentPanel, "student");
        mainPanel.add(companyPanel, "company");
        mainPanel.add(staffPanel, "staff");

        // Exit button
        logoutStudent.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        logoutCompany.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        logoutStaff.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        // Show main panel
        frame.add(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

    }

    private void changePasswordUI(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Change Password", true);
        dialog.setTitle("Change Password");
        dialog.setSize(300, 200);
        dialog.setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(2, 2));

        JTextField password = new JTextField();
        JTextField password2 = new JTextField();

        JButton button = new JButton("Confirm");
        panel.add(new JLabel("New Password:"));
        panel.add(password);
        panel.add(new JLabel("New Password confirm:"));
        panel.add(password2);
        dialog.add(panel, BorderLayout.CENTER);

        button.addActionListener(e -> {
            String newPassword = password.getText();
            String newPasswordConfirm = password2.getText();
            if (newPassword.isEmpty() || newPasswordConfirm.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "Please fill all the fields");
            } else if (!newPassword.equals(newPasswordConfirm)) {
                JOptionPane.showMessageDialog(parent, "Passwords do not match");
            } else {
                user.changePassword(newPassword);
                JOptionPane.showMessageDialog(parent, "Password changed successfully");
                dialog.dispose();
            }
            password.setText("");
            password2.setText("");
        });

        dialog.add(button, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }

    // Create Company Approval Line
    private JPanel createCompanyApprovalRow(CompanyRepresentative company, JPanel leftContent) throws IOException {
        DatabaseLoader databaseLoader = new DatabaseLoader();
        databaseLoader.loadAllData();
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // 公司信息
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.add(new JLabel("Company: " + company.getCompanyName()));
        infoPanel.add(new JLabel("Representative: " + company.getName() + " (" + company.getId() + ")"));

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton approveBtn = new JButton("Approve");
        JButton rejectBtn = new JButton("Reject");

        approveBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null,
                    "Approve company representative: " + company.getName() + "?",
                    "Confirm Approval", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                company.setAccountStatus("Approved");
                try {
                    databaseLoader.updateCompanyRep(company);
                    refreshWaitList(leftContent);
                    JOptionPane.showMessageDialog(null, "Company representative approved successfully");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error updating company status", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        rejectBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null,
                    "Reject company representative: " + company.getName() + "?",
                    "Confirm Rejection", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                company.setAccountStatus("Rejected");
                try {
                    databaseLoader.updateCompanyRep(company);
                    refreshWaitList(leftContent);
                    JOptionPane.showMessageDialog(null, "Company representative rejected");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error updating company status", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);

        row.add(infoPanel, BorderLayout.CENTER);
        row.add(buttonPanel, BorderLayout.EAST);

        return row;
    }

    // Create internship approval entry
    private JPanel createInternshipApprovalRow(Internship internship, JPanel leftContent) throws IOException {
        DatabaseLoader databaseLoader = new DatabaseLoader();
        databaseLoader.loadAllData();
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // 实习信息
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(new JLabel("Title: " + internship.getTitle()));
        infoPanel.add(new JLabel("Company: " + internship.getCompanyName()));
        infoPanel.add(new JLabel("Major: " + internship.getPreferredMajor()));

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton approveBtn = new JButton("Approve");
        JButton rejectBtn = new JButton("Reject");

        approveBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null,
                    "Approve internship: " + internship.getTitle() + "?",
                    "Confirm Approval", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                internship.setStatus("Approved");
                try {
                    databaseLoader.updateInternship(internship);
                    refreshWaitList(leftContent);
                    JOptionPane.showMessageDialog(null, "Internship approved successfully");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error updating internship status", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        rejectBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null,
                    "Reject internship: " + internship.getTitle() + "?",
                    "Confirm Rejection", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                internship.setStatus("Rejected");
                try {
                    databaseLoader.updateInternship(internship);
                    refreshWaitList(leftContent);
                    JOptionPane.showMessageDialog(null, "Internship rejected");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error updating internship status", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);

        row.add(infoPanel, BorderLayout.CENTER);
        row.add(buttonPanel, BorderLayout.EAST);

        return row;
    }

    // Create a withdrawal request line
    private JPanel createWithdrawalRequestRow(InternshipApplication application, JPanel leftContent) throws IOException {
        DatabaseLoader databaseLoader = new DatabaseLoader();
        databaseLoader.loadAllData();
        JPanel row = new JPanel(new BorderLayout());
        row.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        // 申请信息
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(new JLabel("Student: " + application.getStudent().getName()));
        infoPanel.add(new JLabel("Internship: " + application.getInternship().getTitle()));
        infoPanel.add(new JLabel("Company: " + application.getInternship().getCompanyName()));

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton approveBtn = new JButton("Approve");
        JButton rejectBtn = new JButton("Reject");

        approveBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null,
                    "Approve withdrawal request from " + application.getStudent().getName() + "?",
                    "Confirm Approval", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                try {
                    CareerStaff staffObj = new CareerStaff(staff.getId(), staff.getName(), staff.getRole(), staff.getDepartment());
                    staffObj.approveWithdrawal(application.getStudent(), application);

                    // 更新状态为 "Withdrawn"
                    application.setStatus("Withdrawn");

                    // 更新学生、实习和申请
                    databaseLoader.updateStudent(application.getStudent());
                    databaseLoader.updateInternship(application.getInternship());
                    databaseLoader.updateInternshipApplication(application);

                    refreshWaitList(leftContent);
                    JOptionPane.showMessageDialog(null, "Withdrawal approved successfully");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error processing withdrawal", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        rejectBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null,
                    "Reject withdrawal request from " + application.getStudent().getName() + "?",
                    "Confirm Rejection", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                application.setWithdrawalRequested(false);
                try {
                    databaseLoader.updateInternshipApplication(application);
                    refreshWaitList(leftContent);
                    JOptionPane.showMessageDialog(null, "Withdrawal request rejected");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error updating application", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);

        row.add(infoPanel, BorderLayout.CENTER);
        row.add(buttonPanel, BorderLayout.EAST);

        return row;
    }

    // Refresh the waiting list
    private void refreshWaitList(JPanel leftContent) {
        try {
            DatabaseLoader databaseLoader = new DatabaseLoader();
            databaseLoader.loadAllData();

            List<CompanyRepresentative> pendingCompanies = databaseLoader.getCompanyRepsByStatus("Pending");
            List<Internship> pendingInternships = databaseLoader.getInternshipsByStatus("Pending");
            List<InternshipApplication> withdrawalRequests = databaseLoader.getApplicationWithdrawal(true);
            List<InternshipApplication> pendingApplications = databaseLoader.getApplicationsByStatus("Pending");

            // 从pendingApplications中移除有撤回请求的申请
            List<InternshipApplication> filteredPendingApplications = new ArrayList<>();
            for (InternshipApplication app : pendingApplications) {
                if (!app.isWithdrawalRequested()) {
                    filteredPendingApplications.add(app);
                }
            }

            leftContent.removeAll();

            // 重新添加内容
            if (!pendingCompanies.isEmpty()) {
                leftContent.add(new JLabel("Pending Company Representatives:"));
                leftContent.add(Box.createRigidArea(new Dimension(0, 10)));

                for (CompanyRepresentative company : pendingCompanies) {
                    leftContent.add(createCompanyApprovalRow(company, leftContent));
                    leftContent.add(Box.createRigidArea(new Dimension(0, 5)));
                }
                leftContent.add(Box.createRigidArea(new Dimension(0, 20)));
            }

            if (!pendingInternships.isEmpty()) {
                leftContent.add(new JLabel("Pending Internships:"));
                leftContent.add(Box.createRigidArea(new Dimension(0, 10)));

                for (Internship internship : pendingInternships) {
                    leftContent.add(createInternshipApprovalRow(internship, leftContent));
                    leftContent.add(Box.createRigidArea(new Dimension(0, 5)));
                }
                leftContent.add(Box.createRigidArea(new Dimension(0, 20)));
            }

            if (!withdrawalRequests.isEmpty()) {
                leftContent.add(new JLabel("Withdrawal Requests:"));
                leftContent.add(Box.createRigidArea(new Dimension(0, 10)));

                for (InternshipApplication application : withdrawalRequests) {
                    leftContent.add(createWithdrawalRequestRow(application, leftContent));
                    leftContent.add(Box.createRigidArea(new Dimension(0, 5)));
                }
            }

            if (pendingCompanies.isEmpty() && pendingInternships.isEmpty() && withdrawalRequests.isEmpty() && filteredPendingApplications.isEmpty()) {
                leftContent.add(new JLabel("No pending items in wait list"));
            }

            leftContent.revalidate();
            leftContent.repaint();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error refreshing wait list", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load company internship
    private void loadCompanyInternships(JPanel panel) {
        try {
            DatabaseLoader loader = new DatabaseLoader();
            loader.loadAllData();
            panel.removeAll();

            Internship[] internships = company.getInternships();
            boolean hasInternships = false;

            for (Internship internship : internships) {
                if (internship != null) {
                    panel.add(createInternshipManagementCard(internship, panel));
                    panel.add(Box.createRigidArea(new Dimension(0, 10)));
                    hasInternships = true;
                }
            }

            if (!hasInternships) {
                panel.add(new JLabel("No internships created yet"));
            }

            panel.revalidate();
            panel.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Load company application
    private void loadCompanyApplications(JPanel panel) {
        try {
            DatabaseLoader loader = new DatabaseLoader();
            loader.loadAllData();

            panel.removeAll();

            Internship[] internships = company.getInternships();
            boolean hasApplications = false;

            for (Internship internship : internships) {
                if (internship != null) {
                    InternshipApplication[] applications = internship.getInternshipApplications();
                    for (InternshipApplication application : applications) {
                        if (application != null && "Pending".equals(application.getStatus())) {
                            panel.add(createApplicationManagementCard(application, panel));
                            panel.add(Box.createRigidArea(new Dimension(0, 10)));
                            hasApplications = true;
                        }
                    }
                }
            }

            if (!hasApplications) {
                panel.add(new JLabel("No pending applications"));
            }

            panel.revalidate();
            panel.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Create an internship management card
    private JPanel createInternshipManagementCard(Internship internship, JPanel parentPanel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        JPanel infoPanel = new JPanel(new GridLayout(5, 1));
        infoPanel.add(new JLabel("Title: " + internship.getTitle()));
        infoPanel.add(new JLabel("Status: " + internship.getStatus()));
        infoPanel.add(new JLabel("Level: " + internship.getLevel()));
        infoPanel.add(new JLabel("Slots: " + internship.getSlotsAvailable() + "/10"));
        infoPanel.add(new JLabel("Applications: " + internship.getApplicationCount()));


        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton toggleVisibilityBtn = new JButton(internship.isVisible() ? "Hide" : "Show");
        JButton viewApplicationsBtn = new JButton("View Applications");

        toggleVisibilityBtn.addActionListener(e -> {
            try {
                DatabaseLoader loader = new DatabaseLoader();
                loader.loadAllData();

                internship.toggleVisibility();
                loader.updateInternship(internship);

                JOptionPane.showMessageDialog(null, "Internship visibility updated");
                loadCompanyInternships(parentPanel);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error updating internship", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        viewApplicationsBtn.addActionListener(e -> {
            showInternshipApplications(internship);
        });


        toggleVisibilityBtn.setEnabled("Approved".equals(internship.getStatus()));

        buttonPanel.add(toggleVisibilityBtn);
        buttonPanel.add(viewApplicationsBtn);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    // Create an application management card
    private JPanel createApplicationManagementCard(InternshipApplication application, JPanel parentPanel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // Application Information
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(new JLabel("Student: " + application.getStudent().getName()));
        infoPanel.add(new JLabel("Internship: " + application.getInternship().getTitle()));
        infoPanel.add(new JLabel("Major: " + application.getStudent().getMajor()));

        // Control button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton approveBtn = new JButton("Approve");
        JButton rejectBtn = new JButton("Reject");

        approveBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null,
                    "Approve application from " + application.getStudent().getName() + "?",
                    "Confirm Approval", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    DatabaseLoader loader = new DatabaseLoader();
                    loader.loadAllData();

                    // Obtain the corresponding internship
                    Internship internship = application.getInternship();

                    // Check if there are still spots available
                    if (internship.getSlotsAvailable() <= 0) {
                        JOptionPane.showMessageDialog(null, "No available slots for this internship");
                        return;
                    }
                    application.setStatus("Successful");
                    internship.setSlotsAvailable(internship.getSlotsAvailable() - 1);
                    loader.updateInternshipApplication(application);
                    loader.updateInternship(internship);
                    JOptionPane.showMessageDialog(null, "Application approved successfully");
                    loadCompanyApplications(parentPanel);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error approving application", "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        rejectBtn.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null,
                    "Reject application from " + application.getStudent().getName() + "?",
                    "Confirm Rejection", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    application.setStatus("Unsuccessful");
                    DatabaseLoader loader = new DatabaseLoader();
                    loader.updateInternshipApplication(application);
                    JOptionPane.showMessageDialog(null, "Application rejected");
                    loadCompanyApplications(parentPanel);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error rejecting application", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(approveBtn);
        buttonPanel.add(rejectBtn);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    // Show internship application details
    private void showInternshipApplications(Internship internship) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Applications for: " + internship.getTitle());
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());

        JPanel applicationsPanel = new JPanel();
        applicationsPanel.setLayout(new BoxLayout(applicationsPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(applicationsPanel);

        InternshipApplication[] applications = internship.getInternshipApplications();
        boolean hasApplications = false;

        for (InternshipApplication app : applications) {
            if (app != null) {
                JPanel appPanel = new JPanel(new BorderLayout());
                appPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                appPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                JPanel infoPanel = new JPanel(new GridLayout(3, 1));
                infoPanel.add(new JLabel("Student: " + app.getStudent().getName()));
                infoPanel.add(new JLabel("Email: " + app.getStudent().getEmail()));
                infoPanel.add(new JLabel("Status: " + app.getStatus()));

                appPanel.add(infoPanel, BorderLayout.CENTER);
                applicationsPanel.add(appPanel);
                applicationsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
                hasApplications = true;
            }
        }

        if (!hasApplications) {
            applicationsPanel.add(new JLabel("No applications yet"));
        }

        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dialog.dispose());
        dialog.add(closeButton, BorderLayout.SOUTH);

        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void initializeReportPanel(JPanel reportPanel) {
        reportPanel.removeAll();
        reportPanel.setLayout(new BorderLayout());

        // 筛选条件面板
        JPanel filterPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 筛选条件组件
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"All", "Pending", "Approved", "Rejected", "Filled"});
        JTextField majorField = new JTextField();
        JComboBox<String> levelCombo = new JComboBox<>(new String[]{"All", "Basic", "Intermediate", "Advanced"});
        JComboBox<String> companyCombo = new JComboBox<>();

        // 初始化公司下拉框
        initializeCompanyCombo(companyCombo);

        filterPanel.add(new JLabel("Status:"));
        filterPanel.add(statusCombo);
        filterPanel.add(new JLabel("Preferred Major:"));
        filterPanel.add(majorField);
        filterPanel.add(new JLabel("Internship Level:"));
        filterPanel.add(levelCombo);
        filterPanel.add(new JLabel("Company:"));
        filterPanel.add(companyCombo);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton generateReportBtn = new JButton("Generate Report");


        buttonPanel.add(generateReportBtn);


        // 报告结果显示区域
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        JScrollPane resultsScroll = new JScrollPane(resultsPanel);

        // 生成报告按钮事件
        generateReportBtn.addActionListener(e -> {
            generateInternshipReport(resultsPanel,
                    (String) statusCombo.getSelectedItem(),
                    majorField.getText().trim(),
                    (String) levelCombo.getSelectedItem(),
                    (String) companyCombo.getSelectedItem());
        });



        // 组装面板
        JPanel topControlPanel = new JPanel(new BorderLayout());
        topControlPanel.add(filterPanel, BorderLayout.CENTER);
        topControlPanel.add(buttonPanel, BorderLayout.SOUTH);

        reportPanel.add(topControlPanel, BorderLayout.NORTH);
        reportPanel.add(resultsScroll, BorderLayout.CENTER);

        // 默认生成全部报告
        generateInternshipReport(resultsPanel, "All", "", "All", "All");
    }

    // Initialize company dropdown
    private void initializeCompanyCombo(JComboBox<String> companyCombo) {
        companyCombo.addItem("All");
        try {
            DatabaseLoader loader = new DatabaseLoader();
            loader.loadAllData();

            List<CompanyRepresentative> companyReps = loader.getAllCompanyReps();
            java.util.Set<String> companies = new java.util.HashSet<>();

            for (CompanyRepresentative rep : companyReps) {
                if (rep.getCompanyName() != null && !rep.getCompanyName().isEmpty()) {
                    companies.add(rep.getCompanyName());
                }
            }

            for (String company : companies) {
                companyCombo.addItem(company);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Generate an internship report
    private void generateInternshipReport(JPanel resultsPanel, String status, String major,
                                          String level, String company) {
        resultsPanel.removeAll();

        try {
            DatabaseLoader loader = new DatabaseLoader();
            loader.loadAllData();

            List<Internship> allInternships = loader.getAllInternships();
            List<Internship> filteredInternships = new ArrayList<>();

            // Apply filter criteria
            for (Internship internship : allInternships) {
                boolean statusMatch = "All".equals(status) || internship.getStatus().equalsIgnoreCase(status);
                boolean majorMatch = major.isEmpty() || internship.getPreferredMajor().equalsIgnoreCase(major);
                boolean levelMatch = "All".equals(level) || internship.getLevel().equalsIgnoreCase(level);
                boolean companyMatch = "All".equals(company) || internship.getCompanyName().equalsIgnoreCase(company);

                if (statusMatch && majorMatch && levelMatch && companyMatch) {
                    filteredInternships.add(internship);
                }
            }

            // Display report title and statistics
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.add(new JLabel("Internship Opportunities Report (" + filteredInternships.size() + " found)"),
                    BorderLayout.NORTH);

            // Show filter options
            JPanel filterInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            filterInfoPanel.add(new JLabel("Filters: "));
            if (!"All".equals(status)) filterInfoPanel.add(new JLabel("Status=" + status));
            if (!major.isEmpty()) filterInfoPanel.add(new JLabel("Major=" + major));
            if (!"All".equals(level)) filterInfoPanel.add(new JLabel("Level=" + level));
            if (!"All".equals(company)) filterInfoPanel.add(new JLabel("Company=" + company));

            headerPanel.add(filterInfoPanel, BorderLayout.SOUTH);
            resultsPanel.add(headerPanel);
            resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Show the list of internship opportunities
            if (filteredInternships.isEmpty()) {
                resultsPanel.add(new JLabel("No internship opportunities match the selected criteria."));
            } else {
                for (Internship internship : filteredInternships) {
                    resultsPanel.add(createInternshipReportCard(internship));
                    resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }

            resultsPanel.revalidate();
            resultsPanel.repaint();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error generating report: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // Create an internship report card
    private JPanel createInternshipReportCard(Internship internship) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        // Internship Basic Information
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        infoPanel.add(new JLabel("Title: " + internship.getTitle()));
        infoPanel.add(new JLabel("Company: " + internship.getCompanyName()));
        infoPanel.add(new JLabel("Status: " + internship.getStatus()));
        infoPanel.add(new JLabel("Level: " + internship.getLevel()));
        infoPanel.add(new JLabel("Preferred Major: " + internship.getPreferredMajor()));
        infoPanel.add(new JLabel("Open Date: " + internship.getOpenDate()));
        infoPanel.add(new JLabel("Close Date: " + internship.getCloseDate()));
        infoPanel.add(new JLabel("Available Slots: " + internship.getSlotsAvailable() + "/10"));
        infoPanel.add(new JLabel("Applications: " + internship.getApplicationCount()));
        infoPanel.add(new JLabel("Visibility: " + (internship.isVisible() ? "Visible" : "Hidden")));

        card.add(infoPanel, BorderLayout.CENTER);

        return card;
    }

}