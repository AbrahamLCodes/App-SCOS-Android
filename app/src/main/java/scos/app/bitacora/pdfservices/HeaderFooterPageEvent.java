package scos.app.bitacora.pdfservices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import scos.app.bitacora.R;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

    private final Context context;
    private String folio;

    public HeaderFooterPageEvent(Context context) {
        this.context = context;
        this.folio = folio;
    }

    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Seguridad electrónica"), 110, 30, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Folio: 1"), 550, 30, 0);

        ByteArrayOutputStream stream3 = new ByteArrayOutputStream();

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.scos_logo_hd);
        bmp.compress(Bitmap.CompressFormat.PNG, 50, stream3);
        Image maimg = null;
        try {
            maimg = Image.getInstance(stream3.toByteArray());
        } catch (BadElementException | IOException e) {
            e.printStackTrace();
        }
        maimg.setAbsolutePosition(30f, 780f);

        maimg.scalePercent(4f);
        try {
            document.add(maimg);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}