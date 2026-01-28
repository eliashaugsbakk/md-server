package no.eliashaugsbakk.webserver.service.ingestion;

import org.springframework.stereotype.Service;

@Service
public class IngestionService {

  /**
   * Temporary method to verify the end-to-end connectivity.
   * Receives file bytes and metadata, logs the arrival, but defers
   * heavy processing until the communication chain is verified.
   */
  public void ingestBundle(byte[] fileBytes, String fileName) throws Exception {

    // 1. Basic ZIP signature check (The first 4 bytes of a ZIP are always 'PK' 0x50 0x4B)
    if (fileBytes.length < 4 || fileBytes[0] != 0x50 || fileBytes[1] != 0x4B) {
      throw new Exception("Invalid file format: Missing ZIP header (PK).");
    }

    // 2. Log success to the server console for debugging on your Arch machine
    System.out.println("========================================");
    System.out.println("INGESTION TEST: Data received successfully!");
    System.out.println("Filename: " + fileName);
    System.out.println("Size: " + fileBytes.length + " bytes");
    System.out.println("Status: Auth and Integrity verified in Controller.");
    System.out.println("========================================");

    // Future implementation steps:
    // List<RawFile> extractedFiles = bundleUnpacker.unpack(fileBytes);
    // contentIngestor.ingest(extractedFiles);
  }
}
