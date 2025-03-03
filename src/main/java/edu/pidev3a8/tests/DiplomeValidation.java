package edu.pidev3a8.tests;


import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DiplomeValidation {

    public static void main(String[] args) {
        // Chemin vers le fichier image ou PDF
        String filePath = "chemin/vers/diplome.pdf"; // ou "chemin/vers/diplome.png"

        try {
            // Convertir le PDF en image (si nécessaire)
            BufferedImage image;
            if (filePath.endsWith(".pdf")) {
                image = convertPdfToImage(filePath);
            } else {
                image = ImageIO.read(new File(filePath));
            }

            // Extraire le texte avec Tesseract OCR
            String extractedText = extractTextFromImage(image);

            // Valider le texte extrait
            if (isDiplomeValid(extractedText)) {
                System.out.println("Le diplôme est valide. Compte créé.");
            } else {
                System.out.println("Le diplôme est invalide. Compte refusé.");
            }

        } catch (IOException | TesseractException e) {
            e.printStackTrace();
        }
    }

    /**
     * Convertit un PDF en image.
     */
    private static BufferedImage convertPdfToImage(String pdfPath) throws IOException {
        PDDocument document = PDDocument.load(new File(pdfPath));
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        BufferedImage image = pdfRenderer.renderImageWithDPI(0, 300); // 300 DPI pour une meilleure qualité
        document.close();
        return image;
    }

    /**
     * Extrait le texte d'une image avec Tesseract OCR.
     */
    private static String extractTextFromImage(BufferedImage image) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata/fra.traineddata"); // Chemin vers le dossier tessdata
        tesseract.setLanguage("fra"); // Langue française
        return tesseract.doOCR(image);
    }

    /**
     * Valide le texte extrait en recherchant des mots-clés.
     */
    private static boolean isDiplomeValid(String text) {
        // Liste des mots-clés à rechercher
        String[] keywords = {"diplôme", "nutritionniste", "nutrition", "diplômé"};

        // Vérifier si les mots-clés sont présents dans le texte
        for (String keyword : keywords) {
            if (text.toLowerCase().contains(keyword.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}