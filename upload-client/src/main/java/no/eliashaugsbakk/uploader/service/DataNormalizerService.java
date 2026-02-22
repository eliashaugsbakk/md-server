package no.eliashaugsbakk.uploader.service;

import no.eliashaugsbakk.uploader.exception.UploaderException;
import no.eliashaugsbakk.uploader.model.TextFile;
import no.eliashaugsbakk.uploader.utils.MdToHtml;
import no.eliashaugsbakk.utils.Image;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DataNormalizerService {


  private TextFile textFile = null;
  private final List<Image> imageFiles;
  private final List<Image> normalizedImageFiles;

  public DataNormalizerService(List<String> filePaths) throws IOException {

    imageFiles = new ArrayList<>();
    normalizedImageFiles = new ArrayList<>();

    boolean markdownSeen = false;

    for (String s : filePaths) {
      Path path = Path.of(s);

      byte[] fileData = Files.readAllBytes(path);

      String fileName = path.getFileName().toString();


      if (fileName.endsWith(".md") && !markdownSeen) {
        markdownSeen = true;
        textFile = new TextFile(fileName, new MdToHtml().getHtml(Files.readString(path)));
      } else if (fileName.endsWith(".png")
              || fileName.endsWith(".jpg")
              || fileName.endsWith(".jpeg")
              || fileName.endsWith(".bmp")) {
        imageFiles.add(new Image(fileName, fileData));
      } else {
        throw new UploaderException("Unrecognized file extension: \"" + fileName + "\"");
      }
    }

    if (!markdownSeen) {
      throw new UploaderException("No file with .md extensions found");
    }

    for (Image image : imageFiles) {
      try {
        normalizedImageFiles.add(normalizeImage(image));
      } catch (IOException e) {
        throw new UploaderException("Error reading image file: " + e.getMessage());
      }
    }

    // verify image names are present in the Markdown.
    verifyImageNaming(normalizedImageFiles, textFile.body());
  }

  public TextFile getTextFile() {
    return textFile;
  }

  public List<Image> getImagesFiles() {
    return normalizedImageFiles;
  }

  private void verifyImageNaming(List<Image> images, String markdownFile) {
    for (Image image : images) {

      String name = image.title();

      String regex = "\\b" + Pattern.quote(name) + "\\b";
      boolean found = Pattern.compile(regex).matcher(markdownFile).find();

      if (!found) {
        throw new RuntimeException("Image '" + name + "' is not referenced in the Markdown file.");
      }
    }
  }

  /**
   * Normalizes an image by resizing it to a maximum width, stripping metadata,
   * and converting it to a compressed JPEG format.
   * <p>
   * This method ensures compatibility for slow connections by applying a
   * 0.75 compression quality and forcing a white background for transparent sources.
   * </p>
   * <p><b>Note:</b> This method was AI-generated</p>
   *
   * @param imageToNormalize The original Image object containing raw bytes and title.
   * @return A new Image object with JPEG bytes and a .jpg filename.
   * @throws IOException If the image data is invalid or cannot be encoded.
   */
  private Image normalizeImage(Image imageToNormalize) throws IOException {
    byte[] imageData = imageToNormalize.data();
    BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(imageData));
    if (originalImage == null) throw new IOException("Invalid image data");

    // 1. Calculate new dimensions (keeping aspect ratio)
    int targetWidth = Math.min(originalImage.getWidth(), 800);
    int targetHeight = (int) (originalImage.getHeight() * ((double) targetWidth / originalImage.getWidth()));

    // 2. Create the resized image (RGB for JPEG)
    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = outputImage.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
    g2d.dispose();

    // 3. Manually compress as JPEG
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
      ImageWriteParam param = writer.getDefaultWriteParam();

      // Enable compression and set quality (0.75 is a great balance)
      param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      param.setCompressionQuality(0.60f);

      writer.setOutput(ImageIO.createImageOutputStream(baos));
      writer.write(null, new IIOImage(outputImage, null, null), param);
      writer.dispose();

      System.out.println("Final compressed size: " + baos.size() + " bytes");
      return new Image(getNewImageName(imageToNormalize.title()), baos.toByteArray());
    }
  }


  private String getNewImageName(String oldImageName) {
    String newImageName = oldImageName.replaceAll("(?i)\\.(png|jpg|jpeg|gif)$", ".jpg");

    // Update Markdown references
    updateMarkdownLinks(oldImageName, newImageName);

    return newImageName;
  }


  private void updateMarkdownLinks(String oldName, String newName) {
    textFile = new TextFile(
            textFile.title(),
            textFile.body().replace(oldName, newName));
  }
}
