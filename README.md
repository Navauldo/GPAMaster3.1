# 🎓 GPA Master - Academic Management System

A comprehensive JavaFX desktop application for managing academic institutions with role-based access for Administrators, Teachers, and Students.

## 📋 Table of Contents
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Installation & Setup](#installation--setup)
- [Running the Application](#running-the-application)
- [Default Login Credentials](#default-login-credentials)
- [User Roles & Capabilities](#user-roles--capabilities)
- [Group Members](#group-members)
## ✨ Features

### For All Users
- Modern dark/light theme support (NordDark, Dracula, Cupertino, Primer)
- Persistent data storage with save/load functionality
- Real-time GPA calculation and academic standing

### Administrator Capabilities
- **Student Management**: Add, edit, delete, and search students
- **Teacher Management**: Add, edit, delete, and assign courses to teachers
- **Course Management**: Create, edit, and delete courses
- **Grade Management**: Input and update grades for any enrolled student
- **Enrollment**: Enroll students in courses
- **Data Persistence**: Save entire system state to file and load it back
- **View Statistics**: Real-time counts of students, teachers, courses, and enrollments

### Teacher Capabilities
- **View Students**: See all students in courses they teach
- **Input Grades**: Update grades for students in their courses
- **Academic Info**: Look up GPA, honors, and graduation status of their students
- **Statistics**: View number of students, courses taught, and grades recorded

### Student Capabilities
- **View Grades**: See all their course grades with letter equivalents
- **GPA Tracking**: View cumulative GPA in real-time
- **Academic Standing**: Check honors status and graduation eligibility
- **Course Progress**: Track all enrolled courses and performance

## 🛠 Technology Stack

- **Java**: 23
- **JavaFX**: 25.0.2
- **Build Tool**: Maven
- **UI Framework**: AtlantaFX (Modern JavaFX themes)
- **Styling**: CSS with AtlantaFX variables
- **Data Persistence**: Custom file-based serialization (.txt)

## 📁 Project Structure



## 💻 Installation & Setup

### Prerequisites
- Java 23 or higher
- Maven 3.6+
- Git

### Clone the Repository
```bash
git clone https://github.com/Navauldo/GPAMaster3.1.git
cd GPAMaster3.1


