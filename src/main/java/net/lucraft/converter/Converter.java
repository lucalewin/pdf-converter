package net.lucraft.converter;

import com.itextpdf.io.image.ImageDataFactory;
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
import java.util.List;

public class Converter {
//
//	/**
//	 *
//	 * @param first the first {@link PdfDocument}
//	 * @param second the second {@link PdfDocument}
//	 * @return the first and second {@link PdfDocument} merged into a single {@link PdfDocument}
//	 */
//	public static PdfDocument merge(PdfDocument first, PdfDocument second) {
//		String outFileName = getOutputFileName();
//		if (outFileName.equals("")) {
//			return null; // abort
//		}
//		PdfDocument root = null;
//		try {
//			root = new PdfDocument(new PdfWriter(outFileName));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		PdfMerger merger = new PdfMerger(Objects.requireNonNull(root));
//
//		merger.merge(first, 1, first.getNumberOfPages());
//		merger.merge(second, 1, second.getNumberOfPages());
//
//		merger.close();
//
//		return root;
//	}
//
//	/**
//	 *
//	 * @param pdfFiles
//	 * @throws IOException
//	 */
//	public static void merge(String[] pdfFiles) throws IOException {
//		merge(Stream.of(pdfFiles).map(s -> {
//			try {
//				return new PdfDocument(new PdfReader(s));
//			} catch (IOException e) {
//				e.printStackTrace();
//				return null;
//			}
//		}).toList());
//	}

	/**
	 *
	 * @param files
	 * @param output
	 * @throws FileNotFoundException
	 */
	public static void merge(List<File> files, File output) throws FileNotFoundException {
		PdfDocument pdfDocument = new PdfDocument(new PdfWriter(output.getAbsolutePath()));
		Document document = new Document(pdfDocument);

		files.forEach(file -> {
			if (FileUtil.isImageFile(file)) {
				document.add(Converter.convertFileToImage(file));
			} else if (FileUtil.isPdf(file)) {
				try {
					PdfDocument pdf = new PdfDocument(new PdfReader(file.getAbsolutePath()));
					pdf.copyPagesTo(1, pdf.getNumberOfPages(), pdfDocument);
					pdf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.err.println("File format not supported: " + FilenameUtils.getExtension(file.getAbsolutePath()) + "\n\tSkipping '" + file.getAbsolutePath() + "'");
			}
		});

		document.close();
		pdfDocument.close();
	}

	public static void convertImageToPdf(@NotNull Image image, @NotNull File outPdf) {
		try (PdfDocument pdfDocument = new PdfDocument((new PdfWriter(outPdf.getAbsolutePath()))); Document document = new Document(pdfDocument)) {
			document.add(image).flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
//
//	/**
//	 *
//	 * @param pdfs
//	 */
//	public static void merge(List<PdfDocument> pdfs) {
//		String outFileName = getOutputFileName();
//		if (outFileName.equals("")) {
//			return; // abort
//		}
//
//		PdfDocument root = null;
//		try {
//			root = new PdfDocument(new PdfWriter(outFileName));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		PdfMerger merger = new PdfMerger(Objects.requireNonNull(root));
//
//		for (PdfDocument doc : pdfs) {
//			merger.merge(doc, 1, doc.getNumberOfPages());
//		}
//
//		root.close();
//	}
//
//	/**
//	 *
//	 * @param filename
//	 * @return
//	 * @throws MalformedURLException
//	 */
//	public static PdfDocument convertImageToPdf(String filename) throws MalformedURLException {
//		return convertImageToPdf(new Image(ImageDataFactory.create(filename)));
//	}
//
//	/**
//	 *
//	 * @param image
//	 * @return
//	 */
//	public static PdfDocument convertImageToPdf(Image image) {
//		PdfDocument pdf = null;
//		try {
//			pdf = new PdfDocument(new PdfWriter(getOutputFileName()));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		Document document = new Document(Objects.requireNonNull(pdf));
//		document.add(image);
//
//		document.flush();
//		document.close();
//
//		pdf.close();
//
//		return pdf;
//	}
//
//	public static PdfDocument convertImageToPdf(File imageFile, File out) {
//		return convertImageToPdf(imageFile.getAbsolutePath());
//	}
//
//	/**
//	 *
//	 * @param imageFiles
//	 */
//	public static void convertImagesToPdfs(String[] imageFiles) {
//		Stream.of(imageFiles)
//				.map(Converter::convertToImage)
//				.forEach(Converter::convertImageToPdf);
//	}
//
//	/**
//	 *
//	 * @param images
//	 */
//	public static void convertImagesToPdfs(List<File> images) {
//		convertImagesToPdfs((String[]) images.stream().map(File::getAbsolutePath).toArray());
//	}
//
//	/**
//	 *
//	 * @param imageFileNames
//	 */
//	public static void convertImagesToSinglePdf(String[] imageFileNames) {
//		merge(Stream.of(imageFileNames)
//				.map(Converter::convertToImage)
//				.map(Converter::convertImageToPdf).toList());
//	}
//
//	/**
//	 *
//	 * @param imageFiles
//	 */
//	public static void convertImagesToSinglePdf(List<File> imageFiles) {
//		convertImagesToSinglePdf((String[]) imageFiles.stream().map(File::getAbsolutePath).toArray());
//	}

	/**
	 *
	 * @param file
	 * @return
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
