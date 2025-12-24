## “BookBuddy” – Personal Book Tracker API

A simple backend for users to track books they’ve read, want to read, or are currently reading.

---

### Core Functional Requirements

1. User Management
    - Users can register, log in, and log out
    - JWT-based authentication
    - Each user has a personal library

2. Book Management 
    - Add and remove an existing book from the global catalog to your personal library
    - Update book status in your library (to-read, reading, finished) 
    - View all books in your personal library 
    - Search the global book catalog by title or author include aggregate rating 

3. Reviews & Ratings (Optional)
   - Users can rate books edit and delete own rating only in their library (1–5)
   - Users can leave optional reviews edit and delete reviews for books in their library
   - Users can view their reviews
   - Users can search global books reviews

---

### Non-Functional Requirements (Backend Best Practices)

1. Performance & Caching
    1. Local Cache (Caffeine)
       - Cache recently accessed books and user profiles 
       - TTL: 5 minutes 
       - Max size: 5,000 entries 
       - Eviction: LRU
    2. Distributed Cache (Redis)
       - Cache user session tokens 
       - Cache user’s book list for fast retrieval 
       - Cache search results temporarily 
       - TTL applied to all keys (e.g., 10 mins)

2. Database & Connection Pooling
   1. Database
      - PostgreSQL or MySQL 
      - Indexed columns: user_id, book_id, status
   2. Connection Pool
      - Max connections: 20 
      - Min idle: 5 
      - Timeout: 2 seconds

3. Rate Limiting
   - Login: 5 requests/min per IP
   - Book add/update/remove: 20 requests/min per user
   - Search API: 50 requests/min per user
   - Implemented via Redis + TTL

4. Security Best Practices
   - Password hashing (bcrypt)
   - Input validation & sanitization
   - JWT expiration
   - Rate limiting to prevent brute force attacks

5. Cache Consistency
   1. On book update/delete:
       - Evict local cache
       - Delete Redis cache for that book/user list
   2. On search:
       - Store search results in Redis temporarily
       - Expire after 10 minutes

6. Error Handling & Resilience
   - Centralized exception handling
   - Graceful fallback if Redis is down
   - Timeouts for all external API calls (if fetching book metadata from external sources)

7. Logging & Monitoring
   - Structured JSON logs
   - Monitor DB pool usage
   - Monitor cache hit/miss ratio
   - Track API response times

---

## API design

### **1. Authentication (AuthController)**

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/auth/register` | POST | Register a new user |
| `/auth/login` | POST | Login and get JWT |

### **2. User & Library Management (UserController / UserBookController)**

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/users/me` | GET | Get current user profile |
| `/users/me/books` | GET | List all books in the user’s library |
| `/users/me/books` | POST | Add an existing global book to user library |
| `/users/me/books/{id}` | DELETE | Remove a book from user library |
| `/users/me/books/{id}/status` | PUT | Update user-specific book status (`to-read`, `reading`, `finished`) |

### **3. Global Book Catalog (BookController)**

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/books/{id}` | GET | Get book details including aggregate rating and optionally top reviews |
| `/books/search` | GET | Search global books by title or author; include aggregate rating |

### **4. Reviews (ReviewController)**

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/books/{id}/review` | POST | Add or update a user review for a book in their library |
| `/books/{id}/review` | DELETE | Delete a user review for a book |
| `/users/me/reviews` | GET | List all reviews written by the authenticated user |
| `/reviews/search` | GET | Search global reviews by book title, author, or keywords |

### **5. Ratings (RatingController, can be merged with ReviewController)**

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/books/{id}/rating` | POST | Add or update a user rating (1–5) for a book in their library |
| `/books/{id}/rating` | DELETE | Remove/undo user rating for a book |

---