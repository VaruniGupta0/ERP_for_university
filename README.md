# College ERP System (Java + Swing)

A robust, secure desktop Enterprise Resource Planning (ERP) application engineered to manage academic workflows across an institution. Developed over an 8-week cycle, this system provides distinct portals for Students, Instructors, and Administrators. It features a strict layered architecture, a dual-database security model utilizing UNIX shadow-style authentication, and automated data reporting.

##  Demo Video Link :- https://drive.google.com/file/d/1vzzTYRRGU8W79LPxqGZovQdYthBiOP1B/view?usp=sharing

##  System Architecture & Design Patterns

The application enforces a rigid separation of concerns, ensuring UI components never communicate directly with the database. Data flows through a defined API and Service layer boundary.

### Package Hierarchy
* `edu.college.erp.ui`: View layer containing Swing windows, panels, and dialogs (e.g., `ui.student`, `ui.instructor`, `ui.admin`).
* `edu.college.erp.domain`: Plain Old Java Objects (POJOs) representing core entities (`Student`, `Instructor`, `Course`, `Section`, `Enrollment`).
* `edu.college.erp.service`: The "brain" of the application. Contains business logic for course registration, grade computation, and maintenance validation.
* `edu.college.erp.api`: The UI ↔ Services boundary. A facade layer that coordinates requests and returns clean data or unified error messages to the UI.
* `edu.college.erp.data`: Data Access layer for the ERP DB (CRUD operations for academic records).
* `edu.college.erp.auth`: Security module handling login states, password hashing, and session tokens (talks exclusively to the Auth DB).
* `edu.college.erp.util`: Auxiliary helpers for CSV/PDF generation, date/time formatting, and logging.


##  Security: Dual-Database Architecture

To ensure maximum data integrity and prevent unauthorized access, the system splits data across two separate databases:

1. **Auth DB (UNIX "Shadow" Style):** A strictly isolated database storing only `user_id`, `username`, `role`, and `password_hash`. **Plaintext passwords are never stored.** Login flows verify against bcrypt hashes here before issuing a session.
2. **ERP DB:** The operational database managing all academic data (courses, enrollments, grades, system settings). 

*Security Rule:* Profiles in the ERP DB are strictly linked via `user_id` and are only accessible after successful verification by the Auth DB.



## Core Features by Role

### Student Portal
* **Course Catalog:** Browse active courses, viewing real-time capacity and instructor details.
* **Enrollment Engine:** Register for or drop sections. The system actively blocks duplicate enrollments, full-capacity registrations, and actions past deadlines.
* **Academic Dashboard:** View registered timetables and track assessment grades.
* **Transcripts:** One-click download of official academic transcripts in PDF or CSV format.

###  Instructor Portal
* **Section Management:** View assigned classes for the current term and access student rosters.
* **Gradebook:** Input multi-component assessment scores (e.g., quizzes, midterms, end-sem).
* **Final Computation:** Automatically calculate final grades based on dynamic weighting rules (e.g., 20/30/50).
* **Analytics:** View basic class performance statistics and export grade sheets.

### System Administration
* **User Provisioning:** Create new student and instructor accounts, automatically syncing between the Auth DB and ERP DB.
* **Curriculum Control:** Add/edit courses and sections, assign instructors, and set room capacities.
* **Global Maintenance Mode:** A system-wide toggle. When ON, a global banner appears in the UI. Students and instructors are restricted to read-only access, instantly blocking all registration and grading write-operations at the service level.



## Tech Stack & Dependencies

* **Core:** Java (JDK 11+)
* **UI Framework:** Java Swing (Enhanced with FlatLaf for a modern Look & Feel)
* **Database:** MySQL / MariaDB via standard JDBC
* **Connection Pooling:** HikariCP
* **Security/Hashing:** jBCrypt
* **Reporting:** OpenCSV (Grade exports) & OpenPDF (Transcripts)

