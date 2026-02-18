## Rewrite webserver without spring
The scope of the should be limited by design. The web server wil only serve html+css sites over http. When being deployed, nginx or some other webserver would probably live in between this webserver and the user. Nginx would serve all static content, like images and error pages, while the java web server would handle displaying different sides from a DB.

* [x] Get a simple static webserver running
* [X] Implement routing
* [X] Let different sides to be served depending on the request using a String variable containing the html.
* [X] JDBC, SQLite
* [ ] Allow sites to be added with markdown syntax

## Security consernce
This web server will be designed to host sites over the Tor-network. The scope of the server would be small to limit the attack surface.
* [ ] SQL injection
* [ ] Cross-site scripting
* [ ] DOS
* [ ] Other potential security issues?
