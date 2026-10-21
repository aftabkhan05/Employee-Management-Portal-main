# Deploying to Render (Docker)

Render does not offer managed MySQL (only PostgreSQL), and this app uses MySQL.
You need a MySQL database reachable from the internet. Free/cheap options:
Aiven for MySQL, TiDB Cloud (MySQL-compatible), Railway MySQL, or a
MySQL instance running as a Private Service on Render.

## Steps
1. Create a MySQL database and note the host, port, db name, user, password.
2. Push this project to a GitHub repo (the Dockerfile must be in the repo root).
3. Render dashboard -> New -> Web Service -> select the repo.
   - Runtime / Language: **Docker**
   - Health Check Path: `/login`
4. Add environment variables:
   | Key           | Example                                                        |
   |---------------|----------------------------------------------------------------|
   | DB_URL        | jdbc:mysql://HOST:PORT/DBNAME?useSSL=true&serverTimezone=UTC   |
   | DB_USERNAME   | your_db_user                                                   |
   | DB_PASSWORD   | your_db_password                                               |
   Optional: DDL_AUTO (default `update`), SHOW_SQL (default `false`), DB_POOL_SIZE (default `5`).
5. Deploy. Tables are created automatically on first start (ddl-auto=update).

## Local test
    docker build -t employee-portal .
    docker run -p 8080:8080 \
      -e DB_URL="jdbc:mysql://host.docker.internal:3306/hmp" \
      -e DB_USERNAME=root -e DB_PASSWORD=yourpass employee-portal

## Notes
- Uploaded profile images are stored in the container at ~/employee-images, which
  is wiped on every redeploy/restart on Render. To keep them, attach a Render
  Disk (paid plans) mounted at /home/appuser/employee-images.
- Free web services sleep after ~15 min idle; the first request then takes ~30-60s.
