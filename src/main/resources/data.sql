INSERT INTO pages (id, title, slug, createdAt, markdown, html)
VALUES
    (1, 'Home', 'home', CURRENT_TIMESTAMP, 'Welcome to the home page.', '<p>Welcome to the home page.</p>'),
    (2, 'About', 'about', CURRENT_TIMESTAMP, 'This wiki is about the project.', '<p>This wiki is about the project.</p>');
