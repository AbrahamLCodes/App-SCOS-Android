package scos.app.bitacora.pdfservices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import scos.app.bitacora.R;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

    private final Context context;
    private String folio;

    public HeaderFooterPageEvent(Context context, String folio) {
        this.context = context;
        this.folio = folio;
    }

    public void onEndPage(PdfWriter writer, Document document) {

        Font headerFont = new Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD);
        Phrase seg = new Phrase("Seguridad electrónica", headerFont);
        Phrase fol = new Phrase("Folio: " + folio, headerFont);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, seg, document.right(), 790, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, fol, document.right() - 55, 778, 0);

        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.scos_logo_hd);
        bmp.compress(Bitmap.CompressFormat.PNG, 50, stream3);
        Image maimg = null;
        try {
            maimg = Image.getInstance(stream3.toByteArray());
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
        maimg.setAbsolutePosition(document.left(), 750f);
        maimg.scaleAbsolute(120f,60f);
        try {
            document.add(maimg);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        float width = writer.getPageSize().getWidth();
        float height = writer.getPageSize().getHeight();

        PdfContentByte canvas = writer.getDirectContent();
        CMYKColor blackColor = new CMYKColor(0f, 0f, 0f, 1f);
        canvas.setColorStroke(blackColor);
        canvas.setLineWidth(0.5f);
        canvas.moveTo(30, 30);
        canvas.lineTo(width - 30, 30);
        canvas.closePathStroke();
        Phrase p = new Phrase("serviciosocs@hotmail.com");
        p.setFont(new Font(Font.FontFamily.HELVETICA, 8f, Font.NORMAL));
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, p, width / 2, 15, 0);

        //Watermark
        Bitmap bmpWatermark = BitmapFactory.decodeResource(context.getResources(), R.drawable.watermark);
        stream3 = new ByteArrayOutputStream();
        bmpWatermark.compress(Bitmap.CompressFormat.PNG, 50, stream3);
        Image maimg2 = null;
        try {
            maimg2 = Image.getInstance(stream3.toByteArray());
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
        maimg2.setAbsolutePosition((width / 2) - 200, (height / 2) - 250);
        maimg2.scaleAbsolute(400,425);
        try {
            document.add(maimg2);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}