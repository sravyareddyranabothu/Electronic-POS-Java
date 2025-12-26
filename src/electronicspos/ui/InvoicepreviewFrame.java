package electronicspos.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.FileOutputStream;

// iText imports
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class InvoicepreviewFrame extends JFrame {

    Color primary = new Color(22, 163, 74);
    Color bgWhite = Color.WHITE;

    DefaultTableModel billModel;
    String customerName, mobile, email, address;
    double subtotal, gst, total;

    public InvoicepreviewFrame(
            String customerName,
            String mobile,
            String email,
            String address,
            DefaultTableModel billModel,
            double subtotal,
            double gst,
            double total
    ) {

        this.customerName = customerName;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.billModel = billModel;
        this.subtotal = subtotal;
        this.gst = gst;
        this.total = total;

        setTitle("Invoice Preview");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(bgWhite);

        add(headerPanel(), BorderLayout.NORTH);
        add(centerPanel(), BorderLayout.CENTER);
        add(bottomPanel(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ================= HEADER =================
    JPanel headerPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bgWhite);
        header.setBorder(BorderFactory.createEmptyBorder(15, 10, 10, 10));

        JLabel shop = new JLabel("SR ELECTRONICS", SwingConstants.CENTER);
        shop.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 26));
        shop.setForeground(primary);

        JLabel sub = new JLabel("TAX INVOICE", SwingConstants.CENTER);
        sub.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));

        header.add(shop, BorderLayout.NORTH);
        header.add(sub, BorderLayout.SOUTH);
        return header;
    }

    // ================= CENTER =================
    JPanel centerPanel() {
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBackground(bgWhite);
        center.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        center.add(topInfoPanel(), BorderLayout.NORTH);
        center.add(itemsPanel(), BorderLayout.CENTER);

        return center;
    }

    // ================= TOP INFO =================
    JPanel topInfoPanel() {
        JPanel top = new JPanel(new BorderLayout(30, 10));
        top.setBackground(bgWhite);

        top.add(billToPanel(), BorderLayout.WEST);
        top.add(invoiceInfoPanel(), BorderLayout.EAST);

        return top;
    }

    JPanel billToPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Bill To"));
        panel.setBackground(bgWhite);

        panel.add(new JLabel("Name"));
        panel.add(new JLabel(customerName));

        panel.add(new JLabel("Mobile"));
        panel.add(new JLabel(mobile));

        panel.add(new JLabel("Email"));
        panel.add(new JLabel(email));

        panel.add(new JLabel("Address"));
        panel.add(new JLabel(address));

        return panel;
    }

    JPanel invoiceInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Invoice Info"));
        panel.setBackground(bgWhite);

        panel.add(new JLabel("Invoice No"));
        panel.add(new JLabel("INV-" + System.currentTimeMillis()));

        panel.add(new JLabel("Date"));
        panel.add(new JLabel(
                new SimpleDateFormat("dd MMM yyyy").format(new Date())
        ));

        return panel;
    }

    // ================= ITEMS =================
    JScrollPane itemsPanel() {
        JTable table = new JTable(copyModel(billModel));
        table.setRowHeight(26);
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createTitledBorder("Items"));
        return sp;
    }

    // ================= BOTTOM =================
    JPanel bottomPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgWhite);

        panel.add(summaryPanel(), BorderLayout.CENTER);

        JButton btnPdf = new JButton("DOWNLOAD PDF");
        btnPdf.setBackground(new Color(37, 99, 235));
        btnPdf.setForeground(Color.WHITE);
        btnPdf.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        btnPdf.addActionListener(e -> generatePDF());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(bgWhite);
        btnPanel.add(btnPdf);

        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    JPanel summaryPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 60, 15, 60));
        panel.setBackground(bgWhite);

        panel.add(new JLabel("Subtotal"));
        panel.add(right("₹ " + subtotal));

        panel.add(new JLabel("GST (18%)"));
        panel.add(right("₹ " + gst));

        JLabel t1 = new JLabel("TOTAL");
        t1.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));

        JLabel t2 = right("₹ " + total);
        t2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 15));
        t2.setForeground(primary);

        panel.add(t1);
        panel.add(t2);
        return panel;
    }

    JLabel right(String text) {
        return new JLabel(text, SwingConstants.RIGHT);
    }

    // ================= PDF GENERATION =================
    void generatePDF() {
        try {
            String fileName = "Invoice_" + System.currentTimeMillis() + ".pdf";

            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(fileName));
            doc.open();

            Font title = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font bold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font normal = new Font(Font.FontFamily.HELVETICA, 11);

            Paragraph heading = new Paragraph("SR ELECTRONICS\n\n", title);
            heading.setAlignment(Element.ALIGN_CENTER);
            doc.add(heading);

            doc.add(new Paragraph("Customer: " + customerName, normal));
            doc.add(new Paragraph("Mobile: " + mobile, normal));
            doc.add(new Paragraph("Email: " + email, normal));
            doc.add(new Paragraph("Address: " + address + "\n\n", normal));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4, 1, 2, 2});

            table.addCell(new PdfPCell(new Phrase("Product", bold)));
            table.addCell(new PdfPCell(new Phrase("Qty", bold)));
            table.addCell(new PdfPCell(new Phrase("Price", bold)));
            table.addCell(new PdfPCell(new Phrase("Total", bold)));

            for (int i = 0; i < billModel.getRowCount(); i++) {
                table.addCell(billModel.getValueAt(i, 0).toString());
                table.addCell(billModel.getValueAt(i, 1).toString());
                table.addCell("₹ " + billModel.getValueAt(i, 2));
                table.addCell("₹ " + billModel.getValueAt(i, 3));
            }

            doc.add(table);

            doc.add(new Paragraph("\nSubtotal: ₹ " + subtotal, bold));
            doc.add(new Paragraph("GST (18%): ₹ " + gst, bold));
            doc.add(new Paragraph("TOTAL: ₹ " + total, bold));

            doc.close();

            JOptionPane.showMessageDialog(this,
                    "PDF Generated Successfully!\n" + fileName);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "PDF Generation Failed");
        }
    }

    // ================= COPY MODEL =================
    DefaultTableModel copyModel(DefaultTableModel original) {
        DefaultTableModel m = new DefaultTableModel(
                new Object[]{"Product", "Qty", "Price", "Total"}, 0
        );

        for (int i = 0; i < original.getRowCount(); i++) {
            m.addRow(new Object[]{
                    original.getValueAt(i, 0),
                    original.getValueAt(i, 1),
                    original.getValueAt(i, 2),
                    original.getValueAt(i, 3)
            });
        }
        return m;
    }
}
