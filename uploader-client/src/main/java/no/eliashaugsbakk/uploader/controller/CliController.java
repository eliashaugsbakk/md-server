package no.eliashaugsbakk.uploader.controller;

import no.eliashaugsbakk.uploader.config.ConfigManager;
import no.eliashaugsbakk.uploader.exception.UploaderException;
import no.eliashaugsbakk.uploader.model.CliInput;
import no.eliashaugsbakk.uploader.model.NetworkConfig;
import no.eliashaugsbakk.uploader.service.BundleService;
import no.eliashaugsbakk.uploader.service.NetworkService;
import no.eliashaugsbakk.uploader.util.AuthUtils;
import no.eliashaugsbakk.uploader.util.HashUtils;
import java.io.IOException;
import java.util.List;

public class CliController {
  ConfigManager configManager;
  public CliController() throws IOException {
    configManager = new ConfigManager();
  }

  public void execute(String[] args) throws Exception {
    CliInput input = new ArgParser().parse(args);

    if (input.helpRequested()) {
      printHelp();
      return;
    }

    updatePersistentConfig(input);

    if (input.networkTest() || !input.filePaths().isEmpty()) {
      NetworkConfig cfg = getValidatedConfig();
      NetworkService service = new NetworkService(cfg.url(), cfg.token(), cfg.port());

      if (input.networkTest()) {
        service.testConnectivity();
      }

      if (!input.filePaths().isEmpty()) {
        performUpload(service, input.filePaths());
      }
    }
  }

  private NetworkConfig getValidatedConfig() {
    String url = configManager.readUrl();
    String token = configManager.readToken();
    int port = configManager.readPort();

    if (url == null || url.equals("-") || token == null || token.equals("-") || port == 0) {
      throw new UploaderException("Incomplete configuration. Use --setUrl, --setPort, and --setToken first.");
    }
    return new NetworkConfig(url, token, port);
  }

  private void updatePersistentConfig(CliInput input) throws IOException {
    if (input.url() != null) {
      configManager.setUrl(input.url());
      System.out.println("Url has been set: " + input.url());
    }
    if (input.port() != null) {
      configManager.setPort(input.port());
      System.out.println("Port has been set: " + input.port());
    }
    if (input.generateToken()) {
      String token = new AuthUtils().generateAuthKey(32);
      configManager.setToken(token);
      System.out.println("Token has been set: " + token);
    }
  }

  private void performUpload(NetworkService networkService, List<String> filePaths) throws Exception {

    byte[] zippedFiles = new BundleService(filePaths).bundleToZip();

    System.out.println("Uploading bundle...");
    networkService.uploadBundle(zippedFiles, "Upload_" + System.currentTimeMillis(),
        new HashUtils().calculateSHA256(zippedFiles));

    System.out.println("Files has been uploaded.");
  }

  private void printHelp() {
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
