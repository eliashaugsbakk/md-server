package no.eliashaugsbakk.uploader;

import no.eliashaugsbakk.uploader.service.NetworkService;
import no.eliashaugsbakk.uploader.util.HashUtils;

import java.nio.charset.StandardCharsets;

public class Main {
  static void main() throws Exception {
    int TOR_PORT = 9050;
    String onionUrl = "http://p2r7lchnztd2vs5c6uc4eh4skrxv467ya6ewwrny4rdknmozwkbcouid.onion/upload";
    String token = "";

    NetworkService network = new NetworkService(onionUrl, token, TOR_PORT);


    network.testConnectivity();
  }
}
