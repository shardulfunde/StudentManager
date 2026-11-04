# StudentManager

A Java command-line application for managing student records in PostgreSQL.
The Maven artifact retains its original name, `StudentRecordSystem`.

## Features

- List students and retrieve a student by ID.
- Add students with a name and marks.
- Update marks and delete students by ID.
- Validate marks between 0 and 100 in the Java application.
- Display command help.

## Technology Stack

Java 21, Maven, PostgreSQL, JDBC, and PostgreSQL JDBC driver 42.7.4.
The existing Maven Shade plugin creates an executable JAR containing dependencies.

## Project Structure

```text
StudentRecordSystem/
  .mvn/wrapper/maven-wrapper.properties
  src/main/java/
    Main.java           # CLI entry point
    Student.java        # Student model and marks validation
    StudentDAO.java     # JDBC CRUD operations
    DatabaseTest.java   # Standalone connection check
    practice.java       # Standalone connection check
  database/schema.sql
  .env.example
  .gitignore
  LICENSE
  README.md
  mvnw
  mvnw.cmd
  pom.xml
```

## Prerequisites

- JDK 21.
- PostgreSQL running locally or on an accessible server.
- Maven, or the included Maven wrapper (requires network access on first use).
- A database role with permission to connect and use the `students` table and
  its ID sequence.

## Database Setup

Connect as a PostgreSQL administrator or a role allowed to create databases:

```sh
psql -U your_database_user -d postgres
```

Create the database, then exit `psql`:

```sql
CREATE DATABASE student_manager;
\q
```

Apply the schema as the role that will run the application:

```sh
psql -U your_database_user -d student_manager -f database/schema.sql
```

The DAO uses only `students(id, name, marks)` and expects an automatically
generated integer ID. The provided SQL is a minimal compatible setup inferred
from the existing queries; the original database DDL was not supplied.
It does not add database-level marks validation. An existing table is not
modified by this script.

## Configuration

All three JDBC entry points read `DB_URL`, `DB_USER`, and `DB_PASSWORD` from
the process environment. Set them before running database commands.
`.env.example` documents placeholder values only; Java does not automatically
load `.env` files.

In PowerShell:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/student_manager"
$env:DB_USER = "your_database_user"
$credential = Get-Credential -UserName $env:DB_USER -Message "PostgreSQL credentials"
$env:DB_PASSWORD = $credential.GetNetworkCredential().Password
```

In a POSIX shell:

```sh
export DB_URL='jdbc:postgresql://localhost:5432/student_manager'
export DB_USER='your_database_user'
printf 'PostgreSQL password: '
read -r -s DB_PASSWORD
printf '\n'
export DB_PASSWORD
```

The password prompt example requires a shell supporting `read -s`, such as Bash.
Never commit real credentials. Local `.env` files are ignored.

## Build

With installed Maven:

```sh
mvn clean package
```

Or use the wrapper:

```powershell
.\mvnw.cmd clean package
```

```sh
sh ./mvnw clean package
```

The executable output is `target/StudentRecordSystem-1.0-SNAPSHOT.jar`.
The Shade plugin also generates `dependency-reduced-pom.xml`; it is ignored.

## Run And Command Reference

```sh
java -jar target/StudentRecordSystem-1.0-SNAPSHOT.jar help
```

Replace the trailing `help` with any command below:

| Command | Example arguments | Action |
| --- | --- | --- |
| `list` | `list` | Display all students as `id \| name \| marks`. |
| `add` | `add --name "Rahul Funde" --marks 85.5` | Add a student and print the generated ID. |
| `get` | `get 3` | Retrieve a student, or report that the ID was not found. |
| `update` | `update 3 --marks 91` | Update marks for an existing ID. |
| `delete` | `delete 3` | Delete an existing ID. |
| `help` | `help` | Display CLI help. |

Full examples:

```sh
java -jar target/StudentRecordSystem-1.0-SNAPSHOT.jar list
java -jar target/StudentRecordSystem-1.0-SNAPSHOT.jar add --name "Rahul Funde" --marks 85.5
java -jar target/StudentRecordSystem-1.0-SNAPSHOT.jar get 3
java -jar target/StudentRecordSystem-1.0-SNAPSHOT.jar update 3 --marks 91
java -jar target/StudentRecordSystem-1.0-SNAPSHOT.jar delete 3
```

Running without arguments also displays help. The standalone connection check
can be run with:

```sh
java -cp target/StudentRecordSystem-1.0-SNAPSHOT.jar DatabaseTest
```

## Limitations

- The help text uses `student-manager`, but no launcher with that name is
  included. Use `java -jar` as shown above. `mvnw.cmd` is the Windows-specific
  build launcher; `mvnw` is the shell equivalent.
- No GUI, authentication, name editing, sorting, or pagination is implemented.
- Malformed numeric arguments and database errors can produce exceptions.
- Updating or deleting a nonexistent ID raises an exception.
- There is no automated test suite in the supplied project.

## License

See the existing repository's [LICENSE](LICENSE).
