# Back-End
The back end repository for CST438 Project 03
# FoodSwipe

A dating app-style food discovery app for Android. Swipe through foods and restaurants, build a personal taste profile, and see what others like to eat.

**Group 07** — Peter Gloag, Gael Romero, Ivan Martinez, Fernando Alvarado

---

## Overview

FoodSwipe lets users swipe through cards of foods and restaurants — swipe right to like, swipe left to pass. Liked items are saved to your profile. Other users can browse your liked list and vote on your choices.

---

## Features

- **Swipe Interface** — Tinder-style cards for foods and restaurants
- **Like / Pass** — Swipe right to save to your profile; swipe left to skip (recorded so it won't reappear)
- **Undo** — Undo a pass from your profile
- **User Profile** — View and manage all your liked foods and restaurants
- **Browse Users** — See other users' public profiles and what they've liked
- **Groups** — Create a group, invite others, and collaboratively vote on foods/restaurants
- **Admin Panel** — Manage users, foods, and restaurants; grant/revoke admin roles

---

## App Flow

```
Login (Google OAuth2)
  → Discover (swipe cards)
    → My Profile (liked foods & restaurants)
      → Browse Users (view any profile, see their likes)
        → Admin Panel (manage users, foods, restaurants)
```

| Action | Result |
|---|---|
| Swipe Right (Like) | Item saved to profile; appears in liked list; removable anytime |
| Swipe Left (Pass) | Item skipped; recorded so it won't reappear; undo available from profile |
| Browse Users | View other users' public profiles and their liked items |
| Admin Panel | Edit all users, grant/revoke admin role, remove inappropriate content |

---

## Tech Stack

| Layer | Technology |
|---|---|
| Database | Supabase |
| Backend | Spring Boot |
| Mobile | Android |
| Deployment | Render |

---

## Data Model (ERD)

```
User  ──── Group
             │
    ┌────────┴────────┐
Restaurant List    Food List
    │                  │
Restaurant           Food
```

---

## API Endpoints

> Foods and restaurants are pre-populated from a dataset. Users discover and like them. Admin endpoints handle dataset management.

### User-Facing

#### Users
| Method | Endpoint | Description |
|---|---|---|
| GET | `/users` | Browse all users |
| GET | `/users/{id}` | View any profile |
| PATCH | `/users/{id}` | Edit own profile |
| DELETE | `/users/{id}` | Delete own account |

#### Likes (Swipe History)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/users/{id}/likes` | All liked items |
| GET | `/users/{id}/likes/foods` | Liked foods |
| GET | `/users/{id}/likes/restaurants` | Liked restaurants |
| POST | `/users/{id}/likes` | Swipe right — save like |
| DELETE | `/users/{id}/likes/{likeId}` | Unlike / undo |

#### Discover Feed
| Method | Endpoint | Description |
|---|---|---|
| GET | `/foods/discover` | Random unswiped foods |
| GET | `/foods/{id}` | Food detail card |
| GET | `/restaurants/discover` | Random unswiped restaurants |
| GET | `/restaurants/{id}` | Restaurant detail card |

### Admin Only

#### User Management
| Method | Endpoint | Description |
|---|---|---|
| PATCH | `/users/{id}` | Grant / revoke admin role |
| DELETE | `/users/{id}` | Remove user + all likes |

#### Dataset Management
| Method | Endpoint | Description |
|---|---|---|
| GET | `/foods` | List all foods |
| POST | `/foods` | Add to dataset |
| PUT | `/foods/{id}` | Update a food entry |
| DELETE | `/foods/{id}` | Remove from dataset |
| GET | `/restaurants` | List all restaurants |
| POST | `/restaurants` | Add to dataset |
| PUT | `/restaurants/{id}` | Update a restaurant entry |
| DELETE | `/restaurants/{id}` | Remove from dataset |

> All 5 REST verbs covered: GET, POST, PUT, PATCH, DELETE

---

## User Stories

- As a user, I want to easily browse foods and restaurants and add them to my list via a swipe interface.
- As a user, I want my liked items saved to my profile.
- As a user, I want to remove liked items from my profile.
- As a user, the voting system should be fun and easy to use.
- As a user, I want to log in using an existing account (Google OAuth2).
- As a user, I want to create and publish a new food or restaurant to the app.
- As a group member, I want to create an in-app group, invite others, and collaboratively build a food/restaurant list.
- As a group member, I want to see active groups I belong to, who's in them, and the most popular item.
- As an admin, I should be able to view and edit users, groups, restaurants, and foods.
- As an admin, I should be able to grant and revoke admin roles.

---

## MVP Definition

> MVP is complete when a user can **log in**, **discover and save items**, **join a group**, **vote on items**, and **view the most popular choice** on both Android and web.
