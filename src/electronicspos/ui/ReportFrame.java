package electronicspos.ui;

import electronicspos.dao.ReportDAO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.io.FileOutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ReportFrame extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JRadioButton rbWeekly, rbMonthly, rbYearly;
    private JComboBox<Integer> cmbMonth, cmbYear;
    private JButton btnExportPDF;

    private ReportDAO reportDAO = new ReportDAO();

    // Colors same as ProductFrame
    private Color sidebarBg = new Color(32, 36, 48);
    private Color primary = new Color(108, 92, 231);
    private Color hoverColor = new Color(45, 50, 65);
    private Color tableHeaderBg = new Color(108, 92, 231);
    private Color tableHeaderFg = Color.WHITE;

    public ReportFrame() {
        setTitle("Sales Reports");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        // Top panel (styled)
        JPanel top = new JPanel();
        top.setBackground(sidebarBg);
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));

        rbWeekly = new JRadioButton("Weekly", true);
        rbMonthly = new JRadioButton("Monthly");
        rbYearly = new JRadioButton("Yearly");

        rbWeekly.setForeground(Color.WHITE);
        rbMonthly.setForeground(Color.WHITE);
        rbYearly.setForeground(Color.WHITE);
        rbWeekly.setBackground(sidebarBg);
        rbMonthly.setBackground(sidebarBg);
        rbYearly.setBackground(sidebarBg);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbWeekly); bg.add(rbMonthly); bg.add(rbYearly);

        cmbMonth = new JComboBox<>(); for(int i=1;i<=12;i++) cmbMonth.addItem(i);
        cmbYear = new JComboBox<>(); for(int y=2023;y<=2035;y++) cmbYear.addItem(y);

        btnExportPDF = new JButton("Export PDF");
        btnExportPDF.setBackground(primary);
        btnExportPDF.setForeground(Color.WHITE);
        btnExportPDF.setFocusPainted(false);

        top.add(rbWeekly); top.add(rbMonthly); top.add(rbYearly);
        top.add(new JLabel("Month"){ { setForeground(Color.WHITE); } }); top.add(cmbMonth);
        top.add(new JLabel("Year"){ { setForeground(Color.WHITE); } }); top.add(cmbYear);
        top.add(btnExportPDF);

        add(top, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel();
        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(tableHeaderBg);
        table.getTableHeader().setForeground(tableHeaderFg);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setSelectionBackground(primary);
        table.setSelectionForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Event listeners
        rbWeekly.addActionListener(e -> loadReport());
        rbMonthly.addActionListener(e -> loadReport());
        rbYearly.addActionListener(e -> loadReport());
        cmbMonth.addActionListener(e -> { if(rbMonthly.isSelected()) loadReport(); });
        cmbYear.addActionListener(e -> { if(rbMonthly.isSelected() || rbYearly.isSelected()) loadReport(); });
        btnExportPDF.addActionListener(e -> exportReportToPDF());

        loadReport();
        setVisible(true);
    }

    private void loadReport() {
        model.setRowCount(0);
        model.setColumnCount(0);
        model.setColumnIdentifiers(new String[]{"Cashier", "Product Name", "Quantity Sold", "Total Amount"});

        List<Object[]> data;

        if(rbWeekly.isSelected()) {
            LocalDate now = LocalDate.now();
            LocalDate start = now.minusDays(7);
            data = reportDAO.getWeeklyProductSales(Date.valueOf(start), Date.valueOf(now));
        } else if(rbMonthly.isSelected()) {
            int month = (int)cmbMonth.getSelectedItem();
            int year = (int)cmbYear.getSelectedItem();
            data = reportDAO.getMonthlyProductSales(month, year);
        } else {
            int year = (int)cmbYear.getSelectedItem();
            data = reportDAO.getYearlyProductSales(year);
        }

        for(Object[] row : data) model.addRow(row);
    }

    private void exportReportToPDF() {
        if(table.getRowCount()==0) { JOptionPane.showMessageDialog(this,"No data to export!"); return; }

        String filePath = JOptionPane.showInputDialog(this,"Enter PDF path:", "report.pdf");
        if(filePath==null || filePath.isEmpty()) return;

        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream(filePath));
            doc.open();
            doc.add(new Paragraph("Sales Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD,16)));
            doc.add(new Paragraph("Generated on: "+LocalDate.now()));
            doc.add(new Paragraph(" "));

            PdfPTable pdfTable = new PdfPTable(model.getColumnCount());
            pdfTable.setWidthPercentage(100);

            for(int i=0;i<model.getColumnCount();i++){
                PdfPCell cell = new PdfPCell(new Phrase(model.getColumnName(i)));
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfTable.addCell(cell);
            }

            for(int r=0;r<model.getRowCount();r++){
                for(int c=0;c<model.getColumnCount();c++){
                    pdfTable.addCell(model.getValueAt(r,c).toString());
                }
            }

            doc.add(pdfTable);
            doc.close();
            JOptionPane.showMessageDialog(this,"PDF generated successfully at:\n"+filePath);

        } catch(Exception ex){
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,"Error generating PDF!");
        }
    }

    public static void main(String[] args){
        new ReportFrame();
    }
}
