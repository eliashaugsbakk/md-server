package no.eliashaugsbakk.uploader.controller;

import no.eliashaugsbakk.uploader.config.ConfigManager;
import no.eliashaugsbakk.uploader.exception.UploaderException;
import no.eliashaugsbakk.uploader.service.BundleService;
import no.eliashaugsbakk.uploader.service.NetworkService;
import no.eliashaugsbakk.uploader.util.AuthUtils;
import no.eliashaugsbakk.uploader.util.HashUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CliController {
  private final ConfigManager configManager;
  private final List<String> filePaths = new ArrayList<>();

  public CliController() throws Exception {
    this.configManager = new ConfigManager();
  }

  public void execute(String[] args) throws Exception {
    if (args.length == 0) {
      printHelpMessage();
      return;
    }

    parseArguments(args);

    if (!filePaths.isEmpty()) {
      processUpload();
    }
  }

  private void parseArguments(String[] args) throws IOException {
    boolean markdownPassed = false;

    for (int i = 0; i < args.length; i++) {
      String arg = args[i];
      if (arg.startsWith("-")) {
        switch (arg) {
          case "--setToken", "-t" -> handleToken();

          case "--setUrl", "-u" -> {
            validateNextArg(args, i);
            handleUrl(args[++i]);
          }
          case "--setPort", "-p" -> {
            validateNextArg(args, i);
            handlePort(args[++i]);
          }
          case "--help", "-h" -> {
            printHelpMessage();
            System.exit(0);
          }
          default -> throw new UploaderException("Unknown argument: " + arg);
        }
      } else {
        if (arg.endsWith(".md")) {
          if (markdownPassed) throw new UploaderException("Only one markdown file allowed.");
          markdownPassed = true;
        }
        filePaths.add(arg);
      }
    }
  }

  private void validateNextArg(String[] args, int i) {
    if (i + 1 >= args.length) {
      throw new UploaderException("The flag " + args[i] + " requires a value after it.");
    }
  }

  private void processUpload() throws Exception {
    // Validation logic
    String url = configManager.readUrl();
    String token = configManager.readToken();
    int port = configManager.readPort();

    if (url.equals("-") || token.equals("-") || port == 0) {
      System.out.println("Current configuration: URL=" + url + ", Port=" + port);
      throw new UploaderException("Program not configured correctly. Use --help.");
    }

    // Logic flow
    byte[] zippedFiles = new BundleService(filePaths).bundleToZip();
    NetworkService networkService = new NetworkService(url, token, port);

    System.out.println("Uploading bundle...");
    networkService.uploadBundle(zippedFiles, "Upload_" + System.currentTimeMillis(),
        new HashUtils().calculateSHA256(zippedFiles));
  }

  private void handleToken() throws IOException {
    String token = new AuthUtils().generateAuthKey(32);
    configManager.setToken(token);
    System.out.println("Token has been sat to: " + "'" + token + "'");
  }

  private void handleUrl(String url) throws IOException {
    configManager.setUrl(url);
    System.out.println("Url has been sat to: " + "'" + url + "'");
  }

  private void handlePort(String portStr) {
    try {
      int p = Integer.parseInt(portStr);

      if (p < 1 || p > 65535) {
        throw new UploaderException("Port " + p + " is out of range. Use 1-65535.");
      }

      configManager.setPort(p);
      System.out.println("Port has been set to: " + p);

    } catch (NumberFormatException | IOException e) {
      throw new UploaderException("'" + portStr + "' is not a valid number.");
    }
  }

  private void printHelpMessage() {
    System.out.println("""
        Help:
        uploadClient [-h | --help]
        
        General use:
        Pass in relative or absolute file paths for files to upload.
          (Only supports one .md file at a time. Supported images: png, jpg, jpeg, bmp, webp)
        
        Options:
        uploadClient [option] [choice]
        
        Options:
        [-t | --setToken]             - generates a new Authentication Token.
        [-u | --setUrl]   <url>       - sets the host url.
        [-p | --setPort]  <port>      - sets the port to the local Tor Daemon you have running.
                                        (Defaults: Browser=9150, System=9050)
        """);
  }
}
