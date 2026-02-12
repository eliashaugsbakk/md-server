# Project Roadmap

This roadmap outlines the development stages for the Tor-based web uploader.


## Phase 1: MVP - Core Connectivity (Current)
* [ ] **[Server]** Implement Nginx as a middleman. Nginx should handle anything it can (images, static sites like 404), and forward requests to Spring when needed.
* [x] **[Client]** Implement Tor SOCKS5 proxy integration via OkHttp.
* [ ] **[Server]** Move to a persisteint DB (sqlite should be sufficient)
* [ ] **[Server]** Update application.properties to minimize the footprint of the server.
* [ ] **[Server]** (SECURITY) Create robust errorhandling. Do not leak server information to http requests.
* [ ] **[Server]** (SECURITY) Disable all default Spring/Tomcat headers to prevent fingerprinting.
* [ ] **[Server]** Move images out of the DB. DB is for data, folders are for storage.
* [ ] **[Server]** Basic Spring Boot REST endpoint to receive binary data.
* [ ] **[Client/Server]** Manual Auth Token exchange via SSH/Text-file.
* [x] **[Client]** Basic CLI for uploading a single Markdown file.
* [ ] **[Testing]** Verify end-to-end upload from Client -> Tor -> Local Server.

## Phase 2: The Bundler & Integrity
* [x] **[Client]** ZIP-bundling of Markdown and local image assets.
* [x] **[Client]** SHA-256 fingerprinting of bundles.
* [ ] **[Server]** (SECURITY) Bundle decompression and SHA-256 verification.
* [ ] **[Server]** (SECURITY) Implement verification of the zip and image files before prosessing them further. Make sure the files do not contain any unwanted payload.
* [ ] **[SERVER]** (SECURITY) Implement Path Sanitization for ZIP extraction (Anti-ZipSlip).
* [x] **[Client]** Persistent configuration storage in `~/.config/`.

## Phase 3: Reliability & UX
* [ ] **[Client]** Upload progress bar for slow Tor circuits.
* [x] **[Client]** Image normalization and compression engine (pre-upload).
* [ ] **[Client]** Advanced error handling (Tor proxy status, Auth failure, Server downtime).
* [ ] **[Server]** Improved response codes and server-side validation logs.

## Phase 4: Expansion & Clearnet Readiness
* [ ] **[Client]** Smart-switch logic for Tor vs. Clearnet (HTTPS).
* [ ] **[Server]** Multi-user support (Token management for multiple clients).
* [ ] **[Client]** GUI wrapper or enhanced interactive CLI.
