package electronicspos.pdf;

import electronicspos.model.Customer;
import electronicspos.model.Invoice;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.util.List;

public class InvoicePDFGenerator {

    public static void generateInvoice(
            Invoice invoice,
            Customer customer,
            List<Object[]> items
    ) {

        String fileName = "Invoice_" + invoice.getInvoiceId() + ".pdf";

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // ===== Title =====
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Paragraph title = new Paragraph("ELECTRONICS POS INVOICE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));

            // ===== Invoice Info =====
            document.add(new Paragraph("Invoice ID: " + invoice.getInvoiceId()));
            document.add(new Paragraph("Date: " + invoice.getInvoiceDate()));
            document.add(new Paragraph("Customer: " + customer.getName()));
            document.add(new Paragraph("Phone: " + customer.getPhone()));
            document.add(new Paragraph("Email: " + customer.getEmail()));

            document.add(new Paragraph(" "));

            // ===== Table =====
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[]{4, 2, 2, 2});

            addCell(table, "Product", true);
            addCell(table, "Price", true);
            addCell(table, "Qty", true);
            addCell(table, "Total", true);

            for (Object[] row : items) {
                addCell(table, row[0].toString(), false);
                addCell(table, row[1].toString(), false);
                addCell(table, row[2].toString(), false);
                addCell(table, row[3].toString(), false);
            }

            document.add(table);

            document.add(new Paragraph(" "));

            // ===== Totals =====
            document.add(new Paragraph("Total Amount: ₹" + invoice.getTotalAmount()));
            document.add(new Paragraph("GST (18%): ₹" + invoice.getGst()));
            document.add(new Paragraph("Grand Total: ₹" + invoice.getGrandTotal()));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Thank you for shopping with us!"));

            document.close();

            // Auto open PDF
            java.awt.Desktop.getDesktop().open(new java.io.File(fileName));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addCell(PdfPTable table, String text, boolean header) {
        Font font = header
                ? new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)
                : new Font(Font.FontFamily.HELVETICA, 11);

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
    }
}