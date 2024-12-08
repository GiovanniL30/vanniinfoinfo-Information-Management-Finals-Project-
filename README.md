# VanniInfo Java Project

This project demonstrates a Java-based application with separate Admin and Client modules. Ensure the database is set up correctly before running the application.

---

## Steps to Set Up and Run the Project

### 1. Import the Database

- Navigate to resources/data.
- Import the vanniinfoinfo.sql file into your localhost MySQL connection using a tool like phpMyAdmin or MySQL CLI.


Example using MySQL CLI:
```
mysql -u your_username -p your_password your_database_name < resources/data/vanniinfoinfo.sql
```
---

### 2. Run the Admin Application

1. Navigate to the following directory:
   admin/src/vanniinfoinfo
2. Run the AdminApplication.java file:
   java AdminApplication

---

### 3. Run the Client Application

1. Navigate to the following directory:
   client/src/vanniinfoinfo
2. Run the ClientApplication.java file:
   java ClientApplication
