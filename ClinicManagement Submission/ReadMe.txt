Clinic Management System: Setup & Run Guide
Follow these steps in order to set up the database and connect the Java application.

Step 1: Prepare the Database (MySQL Workbench)
Open MySQL Workbench and connect to your local instance.

Import the Schema & Data:

On the left sidebar, click the Administration tab.

Select Data Import/Restore.

Choose "Import from Self-Contained File" and click the "..." button to select the clinic_db_final.sql file from the project root.

Click Start Import at the bottom right.

Verify the Import:

Switch to the Schemas tab on the left.

Right-click in the white space and select Refresh All.

You should now see the clinic_db schema with all tables and the data we added during testing.

Step 2: Configure the Java Connection (NetBeans)
Open the Project:

Open NetBeans.

Go to File > Open Project and select the folder containing the pom.xml file.

Update Credentials:

In the Projects window on the left, navigate to:
Source Packages -> com.yourcompany.util (or your specific util package).

Open the DatabaseConnector.java (or DatabaseConnection.java) file.

Locate the variables for USER and PASS.

Change them to match your local MySQL credentials:
private static final String USER = "root";       // Your MySQL username
private static final String PASS = "yourpassword"; // Your MySQL password
Check Version Compatibility:

Ensure your MySQL Server version is 8.0.36 or higher (Version 9.x is recommended).

Step 3: Clean, Build, and Run
Download Libraries:

Right-click the project name in the left sidebar and select Clean and Build.

Note: Since we are using Maven, NetBeans will automatically download the MySQL Connector (8.3.0) and other libraries using the pom.xml file.

Run the App:

Right-click the project and select Run, or press F6.

The Login screen should appear.
Username:admin
Password:123
Ensure no spaces before or after

 You can now use the staff or doctor credentials already stored in the database!
