package Project;

import Project.DataReader.DatabaseLoader;
import Project.classes.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CompanyService {
    private CompanyRepresentative company;
    private JTabbedPane companyTabbedPane;
    private JLabel companyTitleLabel;

    public CompanyService(CompanyRepresentative company, JTabbedPane tabbedPane, JLabel titleLabel) {
        this.company = company;
        this.companyTabbedPane = tabbedPane;
        this.companyTitleLabel = titleLabel;
    }

    // 初始化公司代表面板内容
    public void initializeCompanyPanel(JTabbedPane tabbedPane, JLabel titleLabel) {
        // 更新标题
        titleLabel.setText("Company Representative Dashboard - " + company.getName());

        // 清空现有选项卡
        tabbedPane.removeAll();

        // 添加选项卡
        JPanel createInternshipPanel = createCreateInternshipPanel();
        tabbedPane.addTab("Create Internship", createInternshipPanel);

        JPanel myInternshipsPanel = createMyInternshipsPanel();
        tabbedPane.addTab("My Internships", myInternshipsPanel);

        JPanel applicationsPanel = createCompanyApplicationsPanel();
        tabbedPane.addTab("Applications", applicationsPanel);

        // 刷新界面
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

    // 创建实习面板
    private JPanel createCreateInternshipPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 添加 null 检查
        if (company == null) {
            panel.add(new JLabel("Company representative information not loaded."), BorderLayout.CENTER);
            return panel;
        }

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // 表单字段
        JTextField titleField = new JTextField();
        JTextArea descArea = new JTextArea(3, 20);
        JScrollPane descScroll = new JScrollPane(descArea);
        JComboBox<String> levelCombo = new JComboBox<>(new String[]{"Basic", "Intermediate", "Advanced"});
        JTextField majorField = new JTextField();

        // 修复日期字段 - 使用文本字段
        JTextField openDateField = new JTextField();
        openDateField.setText(java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE));
        JTextField closeDateField = new JTextField();
        closeDateField.setText(java.time.LocalDate.now().plusDays(30).format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE));

        JSpinner slotsSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));

        // 添加日期格式提示
        openDateField.setToolTipText("YYYY-MM-DD (e.g., 2025-01-15)");
        closeDateField.setToolTipText("YYYY-MM-DD (e.g., 2025-02-15)");

        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descScroll);
        formPanel.add(new JLabel("Level:"));
        formPanel.add(levelCombo);
        formPanel.add(new JLabel("Preferred Major:"));
        formPanel.add(majorField);
        formPanel.add(new JLabel("Open Date (YYYY-MM-DD):"));
        formPanel.add(openDateField);
        formPanel.add(new JLabel("Close Date (YYYY-MM-DD):"));
        formPanel.add(closeDateField);
        formPanel.add(new JLabel("Available Slots:"));
        formPanel.add(slotsSpinner);

        JButton createButton = new JButton("Create Internship");

        createButton.addActionListener(e -> {
            if (company.getInternshipCount() >= 5) {
                JOptionPane.showMessageDialog(null, "You can only create up to 5 internships");
                return;
            }

            String title = titleField.getText().trim();
            String description = descArea.getText().trim();
            String level = (String) levelCombo.getSelectedItem();
            String preferredMajor = majorField.getText().trim();
            String openDateStr = openDateField.getText().trim();
            String closeDateStr = closeDateField.getText().trim();
            int slots = (Integer) slotsSpinner.getValue();

            if (title.isEmpty() || description.isEmpty() || preferredMajor.isEmpty() ||
                    openDateStr.isEmpty() || closeDateStr.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please fill in all fields");
                return;
            }

            try {
                // 解析日期
                java.time.LocalDate openDate = java.time.LocalDate.parse(openDateStr);
                java.time.LocalDate closeDate = java.time.LocalDate.parse(closeDateStr);

                if (closeDate.isBefore(openDate)) {
                    JOptionPane.showMessageDialog(null, "Close date must be after open date");
                    return;
                }

                if (openDate.isBefore(java.time.LocalDate.now())) {
                    JOptionPane.showMessageDialog(null, "Open date cannot be in the past");
                    return;
                }

                DatabaseLoader loader = new DatabaseLoader();
                loader.loadAllData();

                Internship newInternship = company.createInternship(
                        title, description, level, preferredMajor,
                        openDate, closeDate, slots
                );

                if (newInternship != null) {
                    loader.insertInternship(newInternship);
                    loader.updateCompanyRep(company);

                    JOptionPane.showMessageDialog(null, "Internship created successfully! Waiting for staff approval.");

                    // 清空表单
                    titleField.setText("");
                    descArea.setText("");
                    levelCombo.setSelectedIndex(0);
                    majorField.setText("");
                    openDateField.setText(java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE));
                    closeDateField.setText(java.time.LocalDate.now().plusDays(30).format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE));
                    slotsSpinner.setValue(1);

                    // 刷新实习列表
                    initializeCompanyPanel(companyTabbedPane, companyTitleLabel);
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to create internship. You may have reached the limit of 5 internships.");
                }
            } catch (java.time.format.DateTimeParseException ex) {
                JOptionPane.showMessageDialog(null, "Invalid date format. Please use YYYY-MM-DD format (e.g., 2025-01-15)");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error creating internship: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        panel.add(new JLabel("Create New Internship (" + company.getInternshipCount() + "/5)"), BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(createButton, BorderLayout.SOUTH);

        return panel;
    }

    // 我的实习面板
    private JPanel createMyInternshipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 添加 null 检查
        if (company == null) {
            panel.add(new JLabel("Company representative information not loaded."), BorderLayout.CENTER);
            return panel;
        }

        JPanel internshipsListPanel = new JPanel();
        internshipsListPanel.setLayout(new BoxLayout(internshipsListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(internshipsListPanel);

        loadCompanyInternships(internshipsListPanel);

        panel.add(new JLabel("My Internships"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // 申请管理面板 - 修改后版本
    private JPanel createCompanyApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 添加 null 检查
        if (company == null) {
            panel.add(new JLabel("Company representative information not loaded."), BorderLayout.CENTER);
            return panel;
        }

        // 创建选项卡面板
        JTabbedPane applicationsTabbedPane = new JTabbedPane();

        // 待处理申请面板 - 使用原有方法
        JPanel pendingApplicationsPanel = createPendingApplicationsPanel();

        // 所有申请面板 - 使用原有方法
        JPanel allApplicationsPanel = new JPanel(new BorderLayout());
        JPanel allApplicationsListPanel = new JPanel();
        allApplicationsListPanel.setLayout(new BoxLayout(allApplicationsListPanel, BoxLayout.Y_AXIS));
        JScrollPane allScrollPane = new JScrollPane(allApplicationsListPanel);

        loadCompanyApplications(allApplicationsListPanel);

        allApplicationsPanel.add(new JLabel("All Applications"), BorderLayout.NORTH);
        allApplicationsPanel.add(allScrollPane, BorderLayout.CENTER);

        // 添加选项卡
        applicationsTabbedPane.addTab("Pending Review", pendingApplicationsPanel);
        applicationsTabbedPane.addTab("All Applications", allApplicationsPanel);

        panel.add(applicationsTabbedPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createPendingApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel pendingApplicationsListPanel = new JPanel();
        pendingApplicationsListPanel.setLayout(new BoxLayout(pendingApplicationsListPanel, BoxLayout.Y_AXIS));
        JScrollPane pendingScrollPane = new JScrollPane(pendingApplicationsListPanel);

        // 加载待处理申请 - 使用原有逻辑
        loadPendingCompanyApplications(pendingApplicationsListPanel);

        panel.add(new JLabel("Pending Applications (Awaiting Your Review)"), BorderLayout.NORTH);
        panel.add(pendingScrollPane, BorderLayout.CENTER);

        return panel;
    }

    // 加载公司实习
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

    // 加载公司申请
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

    // 创建实习管理卡片
    private JPanel createInternshipManagementCard(Internship internship, JPanel parentPanel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // 实习信息
        JPanel infoPanel = new JPanel(new GridLayout(5, 1));
        infoPanel.add(new JLabel("Title: " + internship.getTitle()));
        infoPanel.add(new JLabel("Status: " + internship.getStatus()));
        infoPanel.add(new JLabel("Level: " + internship.getLevel()));
        infoPanel.add(new JLabel("Slots: " + internship.getSlotsAvailable() + "/10"));
        infoPanel.add(new JLabel("Applications: " + internship.getApplicationCount()));

        // 操作按钮
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

        // 只有已批准的实习才能切换可见性
        toggleVisibilityBtn.setEnabled("Approved".equals(internship.getStatus()));

        buttonPanel.add(toggleVisibilityBtn);
        buttonPanel.add(viewApplicationsBtn);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    // 创建申请管理卡片
    private JPanel createApplicationManagementCard(InternshipApplication application, JPanel parentPanel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // 申请信息
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.add(new JLabel("Student: " + application.getStudent().getName()));
        infoPanel.add(new JLabel("Internship: " + application.getInternship().getTitle()));
        infoPanel.add(new JLabel("Major: " + application.getStudent().getMajor()));

        // 操作按钮
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

                    // 获取对应的实习
                    Internship internship = application.getInternship();

                    // 检查是否还有名额
                    if (internship.getSlotsAvailable() <= 0) {
                        JOptionPane.showMessageDialog(null, "No available slots for this internship");
                        return;
                    }

                    // 更新申请状态
                    application.setStatus("Successful");

                    // 减少实习名额
                    internship.setSlotsAvailable(internship.getSlotsAvailable() - 1);

                    // 更新数据文件
                    loader.updateInternshipApplication(application);
                    loader.updateInternship(internship);

                    JOptionPane.showMessageDialog(null, "Application approved successfully");

                    // 刷新申请列表
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

    // 显示实习申请详情
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

    // 加载待处理的公司申请 - 复用原有逻辑
    private void loadPendingCompanyApplications(JPanel panel) {
        try {
            DatabaseLoader loader = new DatabaseLoader();
            loader.loadAllData();

            panel.removeAll();

            Internship[] internships = company.getInternships();
            boolean hasPendingApplications = false;

            // 复用原有逻辑，但只显示当前公司的申请
            List<InternshipApplication> pendingApplications = loader.getApplicationsByStatus("Pending");

            for (InternshipApplication application : pendingApplications) {
                // 只显示当前公司的申请
                if (application.getInternship().getCompanyName().equals(company.getCompanyName())) {
                    panel.add(createApplicationManagementCard(application, panel));
                    panel.add(Box.createRigidArea(new Dimension(0, 10)));
                    hasPendingApplications = true;
                }
            }

            if (!hasPendingApplications) {
                panel.add(new JLabel("No pending applications for review"));
            }

            panel.revalidate();
            panel.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}