package no.eliashaugsbakk.uploader;

import no.eliashaugsbakk.uploader.controller.CliController;
import no.eliashaugsbakk.uploader.exception.UploaderException;

public class Main {
  static void main(String[] args) {

    try {
      CliController controller = new CliController();
      controller.execute(args);

    } catch (UploaderException e) {
      System.err.println("\n[!] CONFIGURATION ERROR: " + e.getMessage());
      System.exit(1);

    } catch (Exception e) {
      System.err.println("\n[X] CRITICAL ERROR: " + e.getLocalizedMessage());
      System.exit(1);
    }
  }
}
