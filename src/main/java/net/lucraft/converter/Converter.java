package net.lucraft.converter;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Converter {

	/**
	 *
	 * @param files
	 * @param output
	 * @throws FileNotFoundException
	 */
	public static void merge(List<File> files, File output) throws FileNotFoundException {
		ArrayList<PdfDocument> pdfDocuments = new ArrayList<>();
		ArrayList<Path> tempFiles = new ArrayList<>();

		// create temp files + load existing pdfs
		for (File file : files) {
			if (FileUtil.isImageFile(file)) {
				try {
					Path tempPath = Files.createTempFile("pdf_converter_", ".temp.pdf");
					tempFiles.add(tempPath);

					convertImageToPdf(convertFileToImage(file), tempPath.toFile());

					pdfDocuments.add(new PdfDocument(new PdfReader(tempPath.toFile())));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (FileUtil.isPdf(file)) {
				try {
					pdfDocuments.add(new PdfDocument(new PdfReader(file)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.err.println("File format not supported: " + FilenameUtils.getExtension(file.getAbsolutePath()) + "\n\tSkipping '" + file.getAbsolutePath() + "'");
			}
		}

		merge(pdfDocuments, output);

		// delete temp files
		for (Path tempFile : tempFiles) {
			try {
				Files.deleteIfExists(tempFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @param pdfDocuments the {@link PdfDocument}s to be merged
	 * @param output the file where the merged {@link PdfDocument} will be stored
	 * @throws FileNotFoundException if the output {@link File} is a directory, the {@link File} cannot be created or the file cannot be opened
	 */
	public static void merge(@NotNull ArrayList<PdfDocument> pdfDocuments, @NotNull File output) throws FileNotFoundException {
		PdfDocument pdfDocument = new PdfDocument(new PdfWriter(output.getAbsolutePath()));
		for (PdfDocument pdf : pdfDocuments) {
			pdf.copyPagesTo(1, pdf.getNumberOfPages(), pdfDocument);
			pdf.close();
		}
		pdfDocument.close();
	}

	/**
	 *
	 * @param image the {@link Image} to be converted to a {@link PdfDocument}
	 * @param outPdf the output file
	 */
	public static void convertImageToPdf(@NotNull Image image, @NotNull File outPdf) {
		try (PdfDocument pdfDocument = new PdfDocument((new PdfWriter(outPdf.getAbsolutePath())));
		            Document document = new Document(pdfDocument, new PageSize(image.getImageWidth(), image.getImageHeight()))) {
			document.setMargins(0, 0, 0, 0);
			image.setMargins(0, 0, 0, 0);
			document.add(image).flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 *
	 * @param file the {@link File} where the {@link Image} is stored
	 * @return the {@link Image} from the {@link File}
	 */
	public static Image convertFileToImage(@NotNull File file) {
		return convertFileToImage(file.getAbsolutePath());
	}

	@Nullable
	public static Image convertFileToImage(@NotNull String filename) {
		try {
			return new Image(ImageDataFactory.create(filename));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
