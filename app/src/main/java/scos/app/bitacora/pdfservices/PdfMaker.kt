package scos.app.bitacora.pdfservices

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.itextpdf.text.*
import com.itextpdf.text.pdf.*
import com.itextpdf.text.pdf.draw.DottedLineSeparator
import scos.app.bitacora.mainactivities.ReporteActivity
import scos.app.bitacora.modelos.Registro
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class PdfMaker(
    private val context: Context,
    private val contentResolver: ContentResolver,
    private val folio: String,
    private val asunto: String,
    private val admin: String,
    private val tecnico: String,
    private val cel: String,
    private val fracc: String,
    private val isFalla: Boolean,
    private val pdfName: String,
    private val date: String
) {

    fun makePDF() {
        try {
            val document = Document(PageSize.A4, 36f, 36f, 100f, 115f)
            document.top(30f)

            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "/$pdfName.pdf"
            )
            val writer = PdfWriter.getInstance(document, FileOutputStream(file))

            document.open()
            writer.pageEvent = HeaderFooterPageEvent(context, folio)

            addStartingContent(document, writer)
            //addContent
            for (falla in ReporteActivity.fallasList) {
                addFalla(document, falla)
            }
            addLastContent(writer)
            document.close()

            val activity = context as Activity
            activity.runOnUiThread {
                Toast.makeText(context, "PDF Creado", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: Exception) {
            val activity = context as Activity
            activity.runOnUiThread {
                Toast.makeText(context, "Error al crear PDF", Toast.LENGTH_SHORT).show()
            }
            ex.printStackTrace()
        }
    }

    private fun addFalla(document: Document, registro: Registro) {

        // Poniendo Bitmaps
        val bmpList = mutableListOf<Bitmap>()


        for (imageUri in registro.getUris()) {
            val bitmap = MediaStore.Images.Media.getBitmap(
                this.contentResolver,
                Uri.parse(imageUri)
            )
            bmpList.add(bitmap)
        }

        val tableImage = PdfPTable(1)
        val cel = PdfPCell()

        for (imagenBmp in bmpList) {
            val stream = ByteArrayOutputStream()
            imagenBmp.compress(Bitmap.CompressFormat.JPEG, 50, stream)
            val image = Image.getInstance(stream.toByteArray())
            //image.scaleToFit(100f, 100f)
            image.widthPercentage = 100f
            cel.addElement(image)
            cel.addElement(Paragraph("\n"))
        }

        cel.border = Rectangle.NO_BORDER
        cel.setPadding(5f)
        tableImage.addCell(cel)

        val font = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD)
        val p = Paragraph(registro.getFalla(), font)
        val p1 = Paragraph(registro.getFallaDesc() + "\n\n")
        p1.alignment = Element.ALIGN_JUSTIFIED

        val tableFalla = PdfPTable(floatArrayOf(70f, 30f))

        val celltext = PdfPCell()
        val cellTable = PdfPCell()

        celltext.addElement(p)
        celltext.addElement(p1)
        celltext.setPadding(1f)
        celltext.border = Rectangle.NO_BORDER
        celltext.borderWidthBottom = 0.5f

        cellTable.addElement(tableImage)
        cellTable.setPadding(1f)
        cellTable.border = Rectangle.NO_BORDER
        cellTable.borderWidthBottom = 0.5f

        tableFalla.addCell(celltext)
        tableFalla.addCell(cellTable)
        tableFalla.widthPercentage = 100f
        document.add(tableFalla)
    }

    private fun addStartingContent(document: Document, writer: PdfWriter) {
        document.add(Paragraph("\n\n$admin"))
        document.add(Paragraph("Administrador del fracc. $fracc"))
        document.add(Paragraph("Asunto: $asunto"))
        document.add(Paragraph("Presente.-\n"))
        if (isFalla) {
            document.add(Paragraph("Por medio del presente informo los hallazgos encontrados en el $fracc"))
        } else {
            document.add(
                Paragraph(
                    "Por medio del presente informo las correciones aplicadas " +
                            "para los hallazgos del reporte con folio: $folio"
                )
            )
        }
        document.add(Chunk(DottedLineSeparator()))

        ColumnText.showTextAligned(
            writer.directContent,
            Element.ALIGN_RIGHT,
            Phrase("Chihuahua, Chih; a $date"),// falta la fecha
            document.right(),
            document.top() - 20,
            0f
        )
    }

    private fun addLastContent(writer: PdfWriter) {

        val width = writer.pageSize.width
        val p = Phrase("Atentamente")
        val font = Font(Font.FontFamily.HELVETICA, 14f, Font.BOLD)
        p.font = font

        ColumnText.showTextAligned(
            writer.directContent,
            Element.ALIGN_CENTER,
            p,
            width / 2,
            100f,
            0f
        )

        ColumnText.showTextAligned(
            writer.directContent,
            Element.ALIGN_CENTER,
            Paragraph(tecnico),
            (width / 2) + 5,
            55f,
            0f
        )

        ColumnText.showTextAligned(
            writer.directContent,
            Element.ALIGN_CENTER,
            Paragraph("Cel. ${formatPhoneNumber(cel)}"),
            (width / 2) - 2,
            42f,
            0f
        )
        val canvas = writer.directContent
        val magentaColor = CMYKColor(0f, 0f, 0f, 1f)
        canvas.setColorStroke(magentaColor)
        canvas.setLineWidth(1f)
        canvas.moveTo((width / 2) - 120, 70f)
        canvas.lineTo((width / 2) + 120, 70f)
        canvas.closePathStroke()
    }

    private fun formatPhoneNumber(number: String): String {
        var number = number
        number = number.substring(0, number.length - 4) + "-" + number.substring(
            number.length - 4,
            number.length
        )
        number = number.substring(0, number.length - 8) + ")" + number.substring(
            number.length - 8,
            number.length
        )
        number = number.substring(0, number.length - 12) + "(" + number.substring(
            number.length - 12,
            number.length
        )
        return number
    }
}