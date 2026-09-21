package com.mycompany.clinicmanagement.ui.pages;

import com.mycompany.clinicmanagement.models.ReportModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;

public class ReportsPage extends JPanel implements Refreshable {

    private ReportModel model = new ReportModel();
    private JPanel cardContainer;
    private CardLayout cardLayout;

    public ReportsPage() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 1. Control Bar (Top)
        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        controlBar.setBackground(new Color(245, 247, 250));
        controlBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

        JLabel lblFilter = new JLabel("Select Analytics View:");
        lblFilter.setFont(new Font("SansSerif", Font.BOLD, 12));

        String[] options = {"Doctor Performance", "Department Revenue", "Medication Usage"};
        JComboBox<String> viewSelector = new JComboBox<>(options);
        viewSelector.setPreferredSize(new Dimension(200, 30));

        viewSelector.addActionListener(e -> {
            cardLayout.show(cardContainer, (String) viewSelector.getSelectedItem());
        });

        controlBar.add(lblFilter);
        controlBar.add(viewSelector);

        // 2. Card Container (Center)
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setBackground(Color.WHITE);
        cardContainer.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Add the different "Cards"
        cardContainer.add(createBarChartPanel(), "Doctor Performance");
        cardContainer.add(createEarningsPiePanel(), "Department Revenue");
        cardContainer.add(createMedicationPiePanel(), "Medication Usage");

        add(controlBar, BorderLayout.NORTH);
        add(cardContainer, BorderLayout.CENTER);
    }

    private JPanel createBarChartPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        model.getDoctorAppointmentRankings().forEach((name, count)
                -> dataset.addValue(count, "Appointments", name));

        JFreeChart chart = ChartFactory.createBarChart(
                "Total Appointments by Clinician", "Doctor", "Volume",
                dataset, PlotOrientation.VERTICAL, false, true, false);

        styleChart(chart);
        return new ChartPanel(chart);
    }

    private JPanel createEarningsPiePanel() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        model.getDepartmentEarnings().forEach(dataset::setValue);

        JFreeChart chart = ChartFactory.createPieChart("Revenue Distribution per Department", dataset, true, true, false);
        styleChart(chart);
        return new ChartPanel(chart);
    }

    private JPanel createMedicationPiePanel() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        model.getMedicationUsage().forEach(dataset::setValue);

        JFreeChart chart = ChartFactory.createPieChart("Inventory Utilization (Top 5)", dataset, true, true, false);
        styleChart(chart);
        return new ChartPanel(chart);
    }

    private void styleChart(JFreeChart chart) {
        chart.setBackgroundPaint(Color.WHITE);
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 18));
        chart.getTitle().setPaint(new Color(51, 51, 51));
    }

    @Override
    public void refreshData() {
        // 1. Tell the model to clear any cached data so it fetches fresh from SQL
        // (Ensure your ReportModel has a way to re-fetch data)
        this.model = new ReportModel();

        // 2. Remove the old chart panels from the container
        cardContainer.removeAll();

        // 3. Re-add the panels (this calls your creation methods which fetch new data)
        cardContainer.add(createBarChartPanel(), "Doctor Performance");
        cardContainer.add(createEarningsPiePanel(), "Department Revenue");
        cardContainer.add(createMedicationPiePanel(), "Medication Usage");

        // 4. Tell Swing the UI has changed and needs to be repainted
        cardContainer.revalidate();
        cardContainer.repaint();

        System.out.println("Charts updated with latest clinic data.");
    }
}
