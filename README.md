# kazCables - Company Database Management App

kazCables is a user-friendly database application designed to store and manage company-related data, including employee and supplier information. Developed using JavaFX and Java SceneBuilder, the app provides essential features for business data management, allowing users to organize, search, and filter entries easily.

## Features
• 	**Data Management**: Add, edit, delete, and manage entries for employees, suppliers, and other business entities.

•	**Categorization**: Organize data into categories for efficient management.

•	**Search and Filter**: Easily search and filter data based on various criteria.

•	**Authentication**: Secure login system with password hashing (MD5 or SHA-256) and OTP-based password recovery.

•	**Email Integration**: Password recovery via OTP sent to the registered email address.
    
•	**Anonymous Testing**: Supports public testing using tools like Mailhog and TempMail.
## Design Architecture
To get a better feel of app's design and functionality, please refer to the directory (`\DesignDiagrams\`) to see Class/Entity and UML diagrams
## Getting Started

### Prerequisites

To run kazCables, ensure you have the following installed:

•	**Java Development Kit (JDK) 11 or later**

•	**NetBeans IDE (or any IDE with JavaFX support)**

•	**MySQL (for database management)**

•	**MySQL Workbench** (optional but useful for database visualization and queries)

## Installation

1.	Clone the Repository:

	```git clone <repository-url>```

2.	Database Setup:

	•	Create mySQL database according to the scheme provided (refer to EER diagaram down below)

	•	Update the database connection settings in the code to match your MySQL credentials.

3.	Configure JavaFX:

	•	Ensure JavaFX libraries are added to your project if not automatically included by your IDE.

4.	Run the Application:

	•	Open the project `CircuitB` in NetBeans or your preferred IDE and run Main.java to launch kazCables.

## Entity Relationship (ER) Diagram 
![EER-employee](https://github.com/user-attachments/assets/9abb22d8-b7a6-4cf0-bc9b-98ffa5c372ba)


## Usage

### Logging In

Upon launching kazCables, you will be prompted to log in. New users will need credentials created in the database by an administrator.

### Managing Data

After logging in, you can:

•	**Add New Entries**: Input new employees, suppliers, and other data.

•	**Edit Existing Entries**: Modify any incorrect or updated data.

•	**Delete Entries**: Remove outdated or redundant entries.

•	**Search and Filter**: Use the search bar or filter options to quickly locate specific entries.

### Security and Authentication

•	**Password Storage**: Passwords are stored in a hashed format (MD5 or SHA-256) in the SQL database.

•	**Password Recovery**: Forgot your password? kazCables sends a random OTP to the registered email for password recovery.

•	For testing, Mailhog and TempMail can be used to simulate email delivery without using a personal email account.


### Technologies Used

•	**JavaFX & Java SceneBuilder**: For a user-friendly and visually organized interface.

•	**Java Swing**: Additional UI components where needed.

•	**MySQL & MySQL Workbench**: For database management.

•	**NetBeans IDE** \ **IntelliJ IDEA** : For development and debugging.

•	**MD5 & SHA-256 Hashing**: For secure password storage.

•	**Mailhog / TempMail**: For anonymous testing of email features.





