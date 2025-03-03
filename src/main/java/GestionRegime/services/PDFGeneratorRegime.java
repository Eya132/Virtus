package GestionRegime.services;

import GestionRegime.entities.Regime;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class PDFGeneratorRegime {

    /**
     * Génère un PDF contenant la liste des régimes.
     *
     * @param regimes   La liste des régimes à inclure dans le PDF.
     * @param filePath  Le chemin du fichier PDF à générer.
     * @throws IOException Si une erreur survient lors de la création du PDF.
     */
    public static void generateRegimeListPDF(List<Regime> regimes, String filePath) throws IOException {
        // Créer un nouveau document PDF
        try (PDDocument document = new PDDocument()) {
            // Créer une nouvelle page
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Ouvrir le contentStream avec un bloc try-with-resources
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Définir les marges
                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float yPosition = yStart;

                // Définir les largeurs des colonnes (ajustées pour tenir sur la page)
                float[] columnWidths = {40, 80, 60, 60, 60, 60, 80, 80, 60};
                float tableWidth = 0;
                for (float width : columnWidths) {
                    tableWidth += width;
                }

                // Commencer le tableau à la marge gauche
                float startX = 10;

                // Définir les en-têtes du tableau
                String[] headers = {"ID", "Objectif", "Calories", "Protéines", "Glucides", "Lipides", "Date Début", "Date Fin", "Statut"};

                // Définir les couleurs
                Color headerColor = new Color(0, 102, 204); // Bleu
                Color rowColor = new Color(255, 255, 255);  // Blanc
                Color borderColor = new Color(0, 0, 0);     // Noir

                // Ajouter le titre en rouge
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(Color.RED); // Rouge
                contentStream.beginText();
                float titleWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("Liste des Régimes") / 1000 * 16; // Largeur du titre
                float titleX = (page.getMediaBox().getWidth() - titleWidth) / 2; // Centrer le titre
                contentStream.newLineAtOffset(titleX, yPosition);
                contentStream.showText("Liste des Régimes");
                contentStream.endText();
                yPosition -= 30;

                // Dessiner le tableau
                drawTable(contentStream, startX, yPosition, tableWidth, columnWidths, headers, regimes, headerColor, rowColor, borderColor);
            } // Le contentStream est fermé ici

            // Sauvegarder le document
            document.save(filePath);
        }
    }

    private static void drawTable(PDPageContentStream contentStream, float startX, float startY, float tableWidth, float[] columnWidths, String[] headers, List<Regime> regimes, Color headerColor, Color rowColor, Color borderColor) throws IOException {
        float margin = 10;
        float rowHeight = 20;
        float tableHeight = rowHeight * (regimes.size() + 1);

        // Dessiner les bordures du tableau
        contentStream.setStrokingColor(borderColor);
        contentStream.addRect(startX, startY - tableHeight, tableWidth, tableHeight);
        contentStream.stroke();

        // Dessiner les lignes horizontales
        float nextY = startY;
        for (int i = 0; i <= regimes.size() + 1; i++) {
            contentStream.moveTo(startX, nextY);
            contentStream.lineTo(startX + tableWidth, nextY);
            contentStream.stroke();
            nextY -= rowHeight;
        }

        // Dessiner les lignes verticales
        float nextX = startX;
        for (float width : columnWidths) {
            contentStream.moveTo(nextX, startY);
            contentStream.lineTo(nextX, startY - tableHeight);
            contentStream.stroke();
            nextX += width;
        }

        // Ajouter les en-têtes
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10); // Taille de police réduite
        contentStream.setNonStrokingColor(headerColor);
        nextX = startX + margin;
        nextY = startY - 15;
        for (int i = 0; i < headers.length; i++) {
            contentStream.beginText();
            contentStream.newLineAtOffset(nextX, nextY);
            contentStream.showText(headers[i]);
            contentStream.endText();
            nextX += columnWidths[i];
        }

        // Ajouter les données des régimes
        contentStream.setFont(PDType1Font.HELVETICA, 8); // Taille de police réduite
        contentStream.setNonStrokingColor(Color.BLACK);
        nextY = startY - rowHeight - 15;
        for (Regime regime : regimes) {
            nextX = startX + margin;
            contentStream.beginText();
            contentStream.newLineAtOffset(nextX, nextY);
            contentStream.showText(String.valueOf(regime.getRegime_id()));
            contentStream.endText();
            nextX += columnWidths[0];

            contentStream.beginText();
            contentStream.newLineAtOffset(nextX, nextY);
            contentStream.showText(regime.getObjectif().toString()); // Convertir en String
            contentStream.endText();
            nextX += columnWidths[1];

            contentStream.beginText();
            contentStream.newLineAtOffset(nextX, nextY);
            contentStream.showText(String.valueOf(regime.getCaloriesJournalieres()));
            contentStream.endText();
            nextX += columnWidths[2];

            contentStream.beginText();
            contentStream.newLineAtOffset(nextX, nextY);
            contentStream.showText(String.valueOf(regime.getProteines()));
            contentStream.endText();
            nextX += columnWidths[3];

            contentStream.beginText();
            contentStream.newLineAtOffset(nextX, nextY);
            contentStream.showText(String.valueOf(regime.getGlucides()));
            contentStream.endText();
            nextX += columnWidths[4];

            contentStream.beginText();
            contentStream.newLineAtOffset(nextX, nextY);
            contentStream.showText(String.valueOf(regime.getLipides()));
            contentStream.endText();
            nextX += columnWidths[5];

            contentStream.beginText();
            contentStream.newLineAtOffset(nextX, nextY);
            contentStream.showText(regime.getDateDebut().toString());
            contentStream.endText();
            nextX += columnWidths[6];

            contentStream.beginText();
            contentStream.newLineAtOffset(nextX, nextY);
            contentStream.showText(regime.getDateFin().toString());
            contentStream.endText();
            nextX += columnWidths[7];

            contentStream.beginText();
            contentStream.newLineAtOffset(nextX, nextY);
            contentStream.showText(regime.getStatus());
            contentStream.endText();

            nextY -= rowHeight;
        }
    }

}