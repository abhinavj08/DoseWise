# 💊 DoseWise (MediTrack) – Medicine Reminder & Health Record Organizer

A Java Swing desktop application for managing medicine schedules, setting reminders, and organizing personal health records. **No command-line needed** — just double-click to run!

---

## 📁 Project Structure

```
MediTrack/
├── src/
│   ├── model/          # Data models (POJO classes)
│   ├── dao/            # Data Access Objects (JDBC)
│   ├── service/        # Business logic layer
│   ├── ui/             # Java Swing GUI frames
│   └── Main.java       # Application entry point
├── database/
│   └── meditrack.sql   # MySQL database schema
├── lib/                # Place mysql-connector-j.jar here
├── build.bat           # Compile & package into JAR
├── run.bat             # Double-click to launch app
├── MANIFEST.MF
└── README.md
```

---

## ✨ Features

- **User Authentication** – Register and login with email/password
- **Medicine Management** – Add, edit, delete medicine schedules
- **Reminder System** – Set medicine reminders with popup notifications
- **Health Records** – Track weight, blood pressure, blood sugar, and notes
- **Prescriptions** – Store doctor prescriptions with file attachments
- **Dashboard** – Overview with stats cards and quick navigation
- **Health Summary Report** – View average health metrics

---

## 🛠️ Tech Stack

| Component     | Technology        |
|---------------|-------------------|
| Language      | Java              |
| GUI Framework | Java Swing        |
| Database      | MySQL             |
| JDBC Driver   | MySQL Connector/J |
| Architecture  | MVC (Model-View-Controller) |

---

## 📋 Prerequisites

1. **Java JDK 8+** installed
2. **MySQL Server** installed and running
3. **MySQL Connector/J** — [Download here](https://dev.mysql.com/downloads/connector/j/)

---

## 🚀 How to Run

### Step 1: Setup Database
Open MySQL Workbench or terminal and run:
```sql
source database/meditrack.sql;
```

### Step 2: Add MySQL Driver
1. Download [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
2. Place the `.jar` file in the `lib/` folder
3. Rename it to `mysql-connector-j.jar`

### Step 3: Configure Password
Edit `src/dao/DBConnection.java` and set your MySQL password:
```java
private static final String PASSWORD = "your_password_here";
```

### Step 4: Build & Run
```
1. Double-click  build.bat   →  Compiles & creates MediTrack.jar
2. Double-click  run.bat     →  Launches the application!
```

---

## 📊 Database Schema

| Table           | Description                              |
|-----------------|------------------------------------------|
| `users`         | User accounts (name, email, password)    |
| `medicines`     | Medicine schedules per user              |
| `reminders`     | Reminder times linked to medicines       |
| `health_records`| Health vitals (weight, BP, sugar, notes)  |
| `prescriptions` | Doctor prescriptions with file paths     |

---

## 👤 Author

**Abhinav J** — [github.com/abhinavj08](https://github.com/abhinavj08)
