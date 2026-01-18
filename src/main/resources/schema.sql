CREATE TABLE pages (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255),
    slug VARCHAR(255) NOT NULL UNIQUE,
    createdAt TIMESTAMP,
    markdown TEXT NOT NULL,
    html TEXT NOT NULL
);

CREATE TABLE page_image (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    filename VARCHAR(255) NOT NULL,
    contentType VARCHAR(100) NOT NULL,
    uploadedAt TIMESTAMP,
    data BLOB NOT NULL
);