package Project;


import Project.DataReader.DatabaseLoader;
import Project.classes.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class StudentService {
    private Student student;
    private JTabbedPane studentTabbedPane;
    private JLabel studentTitleLabel;

    public StudentService(Student student, JTabbedPane tabbedPane, JLabel titleLabel) {
        this.student = student;
        this.studentTabbedPane = tabbedPane;
        this.studentTitleLabel = titleLabel;
    }

    // 初始化学生面板内容
    void initializeStudentPanel(JTabbedPane tabbedPane, JLabel titleLabel) {
        // 更新标题
        titleLabel.setText("Student Dashboard - " + student.getName());

        // 清空现有选项卡
        tabbedPane.removeAll();

        // 添加选项卡
        JPanel availableInternshipsPanel = createAvailableInternshipsPanel();
        tabbedPane.addTab("Available Internships", availableInternshipsPanel);

        JPanel myApplicationsPanel = createMyApplicationsPanel();
        tabbedPane.addTab("My Applications", myApplicationsPanel);

        JPanel acceptedInternshipPanel = createAcceptedInternshipPanel();
        tabbedPane.addTab("Accepted Internship", acceptedInternshipPanel);

        // 刷新界面
        tabbedPane.revalidate();
        tabbedPane.repaint();
    }

    // 创建可申请实习面板
    private JPanel createAvailableInternshipsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 顶部：筛选条件
        JPanel filterPanel = new JPanel(new FlowLayout());
        // 可以添加专业、级别等筛选器

        // 中间：实习列表
        JPanel internshipsListPanel = new JPanel();
        internshipsListPanel.setLayout(new BoxLayout(internshipsListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(internshipsListPanel);

        // 加载符合条件的实习
        loadAvailableInternships(internshipsListPanel);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // 创建我的申请面板
    private JPanel createMyApplicationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel applicationsListPanel = new JPanel();
        applicationsListPanel.setLayout(new BoxLayout(applicationsListPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(applicationsListPanel);

        // 加载学生的申请
        loadStudentApplications(applicationsListPanel);

        panel.add(new JLabel("My Applications (" + student.getApplicationCount() + "/3)"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // 创建已接受实习面板
    private JPanel createAcceptedInternshipPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // 显示已接受的实习信息
        if (student.getAcceptedPlacement() != null) {
            InternshipApplication acceptedApp = student.getAcceptedPlacement();
            Internship internship = acceptedApp.getInternship();

            JPanel infoPanel = new JPanel(new GridLayout(5, 1));
            infoPanel.add(new JLabel("Company: " + internship.getCompanyName()));
            infoPanel.add(new JLabel("Position: " + internship.getTitle()));
            infoPanel.add(new JLabel("Status: " + acceptedApp.getStatus()));
            infoPanel.add(new JLabel("Accepted: Yes"));

            panel.add(infoPanel, BorderLayout.CENTER);
        } else {
            panel.add(new JLabel("No internship accepted yet"), BorderLayout.CENTER);
        }

        return panel;
    }

    // 加载可申请的实习
    private void loadAvailableInternships(JPanel panel) {
        try {
            DatabaseLoader loader = new DatabaseLoader();
            loader.loadAllData();

            List<Internship> allInternships = loader.getAllInternships();

            for (Internship internship : allInternships) {
                // 检查实习是否对学生可见和可申请
                if (internship.isOpenFor(student)) {
                    // 额外检查学生是否已经申请过
                    boolean alreadyApplied = checkIfAlreadyApplied(internship);

                    // 只有未申请过的实习才显示
                    if (!alreadyApplied) {
                        panel.add(createInternshipCard(internship, panel));
                        panel.add(Box.createRigidArea(new Dimension(0, 10)));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 加载学生申请 - 修改后版本
    private void loadStudentApplications(JPanel panel) {
        InternshipApplication[] applications = student.getInternshipApplications();

        for (int i = 0; i < student.getApplicationCount(); i++) {
            if (applications[i] != null) {
                // 只显示没有被撤回同意的申请
                if (!applications[i].isWithdrawAccepted()) {
                    panel.add(createApplicationCard(applications[i], panel));
                    panel.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        }

        // 如果没有申请，显示提示信息
        if (panel.getComponentCount() == 0) {
            panel.add(new JLabel("No applications found"));
        }
    }

    // 创建实习卡片（包含申请按钮）
    private JPanel createInternshipCard(Internship internship, JPanel parentPanel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        // 实习信息
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(new JLabel("Title: " + internship.getTitle()));
        infoPanel.add(new JLabel("Company: " + internship.getCompanyName()));
        infoPanel.add(new JLabel("Level: " + internship.getLevel()));
        infoPanel.add(new JLabel("Slots: " + internship.getSlotsAvailable()));

        // 申请按钮
        JButton applyButton = new JButton("Apply");

        // 检查学生是否已经申请过这个实习
        final boolean alreadyApplied = checkIfAlreadyApplied(internship);

        // 设置按钮状态
        boolean canApply = student.getApplicationCount() < 3 &&
                !alreadyApplied &&
                internship.isOpenFor(student) &&
                "Approved".equals(internship.getStatus());

        applyButton.setEnabled(canApply);

        // 如果已经申请过，显示不同的文本
        if (alreadyApplied) {
            applyButton.setText("Already Applied");
        } else if (!"Approved".equals(internship.getStatus())) {
            applyButton.setText("Pending Approval");
        } else if (internship.getSlotsAvailable() <= 0) {
            applyButton.setText("Filled");
        }

        applyButton.addActionListener(e -> {
            // 重新检查申请状态，因为可能在其他地方发生了变化
            boolean currentAlreadyApplied = checkIfAlreadyApplied(internship);

            if (student.getApplicationCount() >= 3) {
                JOptionPane.showMessageDialog(null, "You can only apply to 3 internships maximum");
                return;
            }

            if (currentAlreadyApplied) {
                JOptionPane.showMessageDialog(null, "You have already applied to this internship");
                return;
            }

            if (!"Approved".equals(internship.getStatus())) {
                JOptionPane.showMessageDialog(null, "This internship is still pending approval");
                return;
            }

            if (internship.getSlotsAvailable() <= 0) {
                JOptionPane.showMessageDialog(null, "This internship is already filled");
                return;
            }

            int result = JOptionPane.showConfirmDialog(null,
                    "Apply to " + internship.getTitle() + "?", "Confirm Application",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    InternshipApplication newApp = student.applyTo(internship);
                    if (newApp != null) {
                        DatabaseLoader loader = new DatabaseLoader();
                        loader.insertInternshipApplication(newApp);
                        loader.updateStudent(student);
                        loader.updateInternship(internship);

                        JOptionPane.showMessageDialog(null, "Application submitted successfully");

                        // 刷新整个学生面板，而不仅仅是当前面板
                        initializeStudentPanel(studentTabbedPane, studentTitleLabel);

                        // 切换到"My Applications"选项卡，让用户看到新提交的申请
                        studentTabbedPane.setSelectedIndex(1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Unable to submit application. You may have already applied or the internship is no longer available.");
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error submitting application", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(applyButton, BorderLayout.EAST);

        return card;
    }

    // 辅助方法：检查学生是否已经申请过某个实习
    private boolean checkIfAlreadyApplied(Internship internship) {
        for (int i = 0; i < student.getApplicationCount(); i++) {
            InternshipApplication app = student.getInternshipApplications()[i];
            if (app != null &&
                    app.getInternship().getId().equals(internship.getId())) {
                // 如果申请已被撤回同意，则不算作已申请，学生可以重新申请
                if (app.isWithdrawAccepted()) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }

    // 创建申请卡片（包含状态和操作按钮）
    private JPanel createApplicationCard(InternshipApplication application, JPanel parentPanel) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // 申请信息
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        Internship internship = application.getInternship();
        infoPanel.add(new JLabel("Company: " + internship.getCompanyName()));
        infoPanel.add(new JLabel("Position: " + internship.getTitle()));
        infoPanel.add(new JLabel("Status: " + application.getStatus()));
        infoPanel.add(new JLabel("Applied: " + application.getInternship().getOpenDate()));

        // 操作按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // 接受按钮（仅当状态为Successful且未接受其他实习时）
        JButton acceptButton = new JButton("Accept");
        acceptButton.setEnabled("Successful".equals(application.getStatus()) &&
                !application.isAccepted() &&
                student.getAcceptedPlacement() == null);

        // 撤回请求按钮 - 如果已经撤回同意，则禁用
        JButton withdrawButton = new JButton("Request Withdrawal");
        boolean canWithdraw = !application.isWithdrawalRequested() && !application.isWithdrawAccepted();
        withdrawButton.setEnabled(canWithdraw);

        // 如果已经撤回同意，显示不同的文本
        if (application.isWithdrawAccepted()) {
            withdrawButton.setText("Withdrawn");
            withdrawButton.setEnabled(false);
        } else if (application.isWithdrawalRequested()) {
            withdrawButton.setText("Withdrawal Pending");
            withdrawButton.setEnabled(false);
        }

        acceptButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null,
                    "Accept this internship placement? This will withdraw all other applications.",
                    "Confirm Acceptance", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                try {
                    InternshipApplication acceptedApp = student.acceptInternship(application);
                    if (acceptedApp != null) {
                        DatabaseLoader loader = new DatabaseLoader();
                        loader.updateStudent(student);
                        loader.updateInternshipApplication(acceptedApp);
                        loader.updateInternship(acceptedApp.getInternship());

                        JOptionPane.showMessageDialog(null, "Internship accepted successfully!");

                        // 刷新整个学生面板
                        initializeStudentPanel(studentTabbedPane, studentTitleLabel);

                        // 切换到"Accepted Internship"选项卡
                        studentTabbedPane.setSelectedIndex(2);
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Error accepting internship", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        withdrawButton.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(null,
                    "Request withdrawal from this application?",
                    "Confirm Withdrawal Request", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                boolean success = student.requestWithdrawal(application);
                if (success) {
                    try {
                        DatabaseLoader loader = new DatabaseLoader();
                        loader.updateInternshipApplication(application);

                        JOptionPane.showMessageDialog(null,
                                "Withdrawal request submitted. Waiting for staff approval.");

                        // 刷新整个学生面板
                        initializeStudentPanel(studentTabbedPane, studentTitleLabel);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error submitting withdrawal request", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        buttonPanel.add(acceptButton);
        buttonPanel.add(withdrawButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }


}