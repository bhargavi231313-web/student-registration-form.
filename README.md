# Student Registration (Java + MySQL)

A small desktop registration form built with Java Swing, JDBC, and MySQL. Import this Maven project into Eclipse as **Existing Maven Projects**.

## Requirements

- JDK 17 or newer
- Eclipse IDE for Java Developers (Maven support)
- MySQL Server 8 or newer

## Set up the database

1. Open `database/schema.sql` in MySQL Workbench or a MySQL client and run it.
2. Set the database connection environment variables before launching the app:
   - `DB_URL` (optional; defaults to `jdbc:mysql://localhost:3306/student_registration?serverTimezone=UTC`)
   - `DB_USER` (optional; defaults to `root`)
   - `DB_PASSWORD` (recommended; your MySQL password)
3. In Eclipse, choose **File → Import → Maven → Existing Maven Projects**, select this project folder, then run `StudentRegistrationApp.java`.

The app validates required fields and email format, saves using a prepared statement, and prevents duplicate email addresses. The MySQL JDBC driver is downloaded by Maven.

## Run with Maven

After configuring the three variables in your shell, run `mvn clean compile exec:java` from the project folder.

## Security

Do not commit database passwords. Set them in your environment. The default local DB user is `root` with a blank password only to make a fresh local setup easy; create a dedicated MySQL user for shared or production use.
