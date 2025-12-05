
# 🛒 Online Marketplace Microservices – Architecture & Implementation

This document describes the complete architecture, components, authentication flow, and service-level responsibilities of the **Online Marketplace Platform** implemented using Spring Boot microservices.

---

## 📌 **High-Level Architecture**

The platform consists of four microservices:

- **Member Service (PostgreSQL + JWT + Redis for logout blacklist)**
- **Product Service (MongoDB + Redis Caching + Wildcard Search)**
- **Cart Service (MongoDB + Feign Client + JWT Authorization)**
- **API Gateway (JWT Authentication, Token Blacklist Validation, Routing)**

---

## 🖼️ **System Architecture Diagram**

![Architecture Diagram](architecture.png

---

## 📌 **Authentication Rules in Architecture**

### ✔ Token Generation (ONLY in Member Service)
- Users authenticate using username & password.
- Member Service generates **Access Token (1 hour expiry)**.
- Token contains claims:
  - `sub` (userId)
  - `email`
  - `role`

---

### ✔ Product Service Access (Public)
- Endpoints `/api/product/**` require **NO authentication**.
- Gateway **bypasses JWT validation** for Product APIs.
- Product search uses:
  - MongoDB regex search
  - Redis caching with TTL
  - Pagination

---

### ✔ Cart Service Access (Protected)
Gateway **must authenticate** before routing to Cart Service.

Gateway Responsibilities:
- Extract JWT from header
- Validate:
  - token signature  
  - expiration  
  - blacklist check (Redis)
- Extract claims:
  - `X-User-Id`
  - `X-User-Email`
  - `X-User-Roles`
- Forward identity headers to Cart Service

Cart Responsibilities:
- Require `X-User-Id`
- Use Feign to validate productId
- Maintain cart per user (cartId = userId)

---

## 🧩 **Microservice Responsibilities**

---

# 1️⃣ **Member Service**

### Purpose
Handles:
- User registration  
- Login (password hashing)  
- JWT generation  
- Redis logout blacklist  
- Profile retrieval  

### Tech Stack
- Spring Boot
- PostgreSQL
- Redis (blacklist)
- BCrypt
- JJWT

---

# 2️⃣ **Product Service**

### Purpose
Public product listing and search service.

### Tech Stack
- Spring Boot
- MongoDB
- Redis (search caching)

### Features
- Wildcard search with regex
- Pagination
- Redis caching of search results
- Product detail API (used by Cart via Feign)

---

# 3️⃣ **Cart Service**

### Purpose
Handles authenticated cart operations.

### Tech Stack
- Spring Boot
- MongoDB
- Feign Client (Product Service)

### Features
- Add to cart (validates product via Feign)
- Delete from cart
- View cart with computed totals
- Requires JWT-authenticated requests

---

# 4️⃣ **API Gateway**

### Purpose
Centralized request router + Authentication Filter.

### Tech Stack
- Spring Cloud Gateway
- Redis (for blacklist check)

### Responsibilities
- Validate JWT signature & expiration
- Reject blacklisted tokens
- Allow public routes:
  - `/api/member/login`
  - `/api/member/register`
  - `/api/product/**`
- Append identity headers:
  - `X-User-Id`
  - `X-User-Email`
  - `X-User-Roles`

---

## 🗄️ **Databases Overview**

| Service | Database | Purpose |
|--------|----------|---------|
| Member Service | PostgreSQL | Users, credentials |
| Product Service | MongoDB | Product catalog |
| Cart Service | MongoDB | Shopping carts |
| Gateway / Member | Redis | Token blacklist + product search cache |

---

## 🔧 Key Configurations

### JWT
```
jwt.secret=MySuperSecretKeyForJwtWhichShouldBeAtLeast32Bytes
jwt.expiration=3600000  
```

### Redis Cache
```
spring.cache.redis.time-to-live=600000
```

---

## 🔄 **End-to-End Flow**

### Login
```
Client → Member Service → JWT returned
```

### Cart Access (Protected)
```
Client → Gateway → JWT Validated → Cart Service
```

### Product Access (Public)
```
Client → Gateway → Product Service (no JWT required)
```

### Logout
```
Token added to Redis blacklist → Gateway blocks it
```

---

## 🎉 Conclusion

This architecture provides:

- Clean microservice separation  
- Secure JWT authentication  
- Redis-based caching + logout  
- Polyglot persistence: PostgreSQL + MongoDB + Redis  
- Feign-based interservice communication  

A scalable, production-ready marketplace platform foundation.
