# 📚 Library Management System - Backend

This is the backend service for the **Library Management System**, developed using **Spring Boot**. It provides **RESTful APIs** for managing user authentication, book checkouts, payments, reviews, and messaging. This service interacts with a **MySQL database** for data storage and **Stripe** for handling payments.

## 📖 Table of Contents
- [Project Description](#project-description)
- [Technologies Used](#technologies-used)
- [Installation and Running](#installation-and-running)
- [API Usage](#api-usage)
  - [Admin Management](#admin-management)
  - [Book Management](#book-management)
  - [Messaging System](#messaging-system)
  - [Payment Management](#payment-management)
  - [Review System](#review-system)
- [Credits](#credits)
- [License](#license)

---

## 📝 Project Description

The **Library Management Backend** is the core of a full-stack application designed to facilitate online library operations. Users can **browse books, check out books, renew loans, return books, and leave reviews**. Admins can **manage books, approve messages, and oversee transactions**.

### 🔑 Key Functionalities:
- **🔐 User Authentication**: Secure login and user session management.
- **📚 Book Management**: CRUD operations for books, including checkouts and renewals.
- **📊 Loan Tracking**: Track books currently checked out by users.
- **💳 Payment Processing**: Handle fines and payments using Stripe.
- **⭐ Reviews & Ratings**: Users can post and check reviews of books.
- **💬 Messaging System**: Users can send inquiries, and admins can respond.

---

## ⚙️ Technologies Used

- **Java 21** - Backend logic and service implementation.
- **Spring Boot** - Framework for building scalable backend services.
- **Spring Security** - Handles authentication and authorization.
- **MySQL** - Relational database for storing user and book data.
- **Stripe API** - Used for processing payments.
- **Maven** - For dependency management and project builds.

---

## 🚀 Installation and Running

1. **Clone the repository:**
   ```sh
   git clone https://github.com/your-repo/library-management-system.git
   ```
2. **Navigate to the project directory:**
   ```sh
   cd library-management-system
   ```
3. **Install dependencies:**
   ```sh
   mvn clean install
   ```
4. **Configure environment variables** (Database URL, Stripe API key, etc.).
5. **Run the application:**
   ```sh
   mvn spring-boot:run
   ```

---

## 🔌 API Usage

All API requests should be prefixed with the base URL:

```
https://library-management-system.com/api/
```

### 🔹 Admin Management
#### **Admin API Endpoints**
| Method | Endpoint | Description |
|--------|---------|-------------|
| **POST** | `/admin/secure/add/book` | Add a new book |
| **PUT** | `/admin/secure/increase/book/quantity` | Increase book quantity |
| **PUT** | `/admin/secure/decrease/book/quantity` | Decrease book quantity |
| **DELETE** | `/admin/secure/delete/book` | Delete a book |

**🔒 Authorization Required:** `Admin`

---

### 📚 Book Management
#### **Book API Endpoints**
| Method | Endpoint | Description |
|--------|---------|-------------|
| **PUT** | `/books/secure/checkout` | Checkout a book |
| **GET** | `/books/secure/ischeckedout/byuser` | Check if a book is checked out by the user |
| **GET** | `/books/secure/currentloans/count` | Get the count of currently checked-out books |
| **GET** | `/books/secure/currentloans` | Get a list of currently checked-out books |
| **PUT** | `/books/secure/return` | Return a book |
| **PUT** | `/books/secure/renew/loan` | Renew a book loan |

**🔒 Authorization Required:** `User`

---

### 💬 Messaging System
#### **Message API Endpoints**
| Method | Endpoint | Description |
|--------|---------|-------------|
| **POST** | `/messages/secure/add/message` | Send a message to the admin |
| **PUT** | `/messages/secure/admin/message` | Admin responds to a message |

**🔒 Authorization Required:** `User (to send)`, `Admin (to respond)`

---

### 💰 Payment Management
#### **Payment API Endpoints**
| Method | Endpoint | Description |
|--------|---------|-------------|
| **POST** | `/payments/secure/payment-intent` | Create a payment intent using Stripe |
| **PUT** | `/payments/secure/payment-complete` | Mark payment as complete |
| **GET** | `/payments/search/findByUserEmail` | Find payment details for a user |

**🔒 Authorization Required:** `User`

---

### ⭐ Review System
#### **Review API Endpoints**
| Method | Endpoint | Description |
|--------|---------|-------------|
| **POST** | `/reviews/secure` | Post a review for a book |
| **GET** | `/reviews/secure/user/book` | Check if a user has reviewed a book |

**🔒 Authorization Required:** `User`

---

## 👨‍💻 Credits

- **Developers**: *Your Team Name*
- **Tech Stack**: Java, Spring Boot, MySQL, Stripe API

---

## 📜 License

This project is licensed under the **MIT License**.

---

> *Feel free to contribute by submitting issues, pull requests, and feature suggestions!*

---

### 📌 Need any modifications? Let me know! 🚀
```
