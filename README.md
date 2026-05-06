# 🍳 MangIA

MangIA is a social cooking Android application focused on helping users discover what they can cook using the ingredients they already have at home.

The app combines recipe discovery, community interaction, and ingredient scanning to create a personalized cooking experience where users can explore new meals, share recipes, and interact with other cooks in real time.

> Developed as my technical high school thesis project and my first large-scale Android application.

---

# 📱 Features

- 📖 Publish step-by-step recipes
- ⭐ Recipe rating system (0–5 stars)
- 💬 Real-time comments and live chat between users
- ❤️ Save favorite recipes
- 👤 User profiles and follow system
- 🖼️ Upload and manage recipe images
- 🥕 Ingredient-based recipe listing
- 📷 Ingredient scanning system
- 🔍 Personalized recipe discovery
- 🔐 Authentication with:
  - Google
  - Email & Password

---

# 🚀 What Makes MangIA Different?

One of MangIA's main features is its ingredient scanning system.

Users can scan or select ingredients they already have in their kitchen, and the app will recommend recipes available with those ingredients.

This approach helps users:
- Reduce food waste
- Discover new recipes
- Decide what to cook quickly
- Create meals based on available resources

---

# 🛠️ Technologies

## Android
- Kotlin
- Android SDK
- XML
- Navigation Components
- ViewBinding
- LiveData
- Coroutines

## Firebase
- Firebase Authentication
- Firebase Firestore
- Firebase Realtime Database
- Firebase Storage
- Firebase Analytics

## Libraries
- Glide
- UCrop
- PhotoView
- Lottie
- Shimmer

---

# 🧠 Firebase Structure

| Service | Purpose |
|---|---|
| Firestore | Recipes and recipe-related data |
| Realtime Database | Users, follows, favorites, ratings, and real-time interactions |
| Firebase Storage | Recipe and profile images |
| Firebase Auth | Authentication system |
| Firebase Analytics | User interaction tracking |

---

# ⚡ Technical Challenge

One of the biggest technical challenges during development was synchronizing real-time user interactions while maintaining a responsive experience across the app.

This included:
- Live chat between users
- Real-time comments
- Instant recipe ratings
- Favorites synchronization
- Follow system updates

Since this was my first mobile development project, handling asynchronous operations, Firebase listeners, and UI updates efficiently became one of the most valuable learning experiences throughout the development process.

---

# 📚 What I Learned

MangIA represented my first contact with Android development and the Kotlin programming language.

Through this project, I learned:
- Mobile application development fundamentals
- Firebase ecosystem integration
- Real-time data handling
- UI/UX design for Android
- Asynchronous programming with Coroutines
- The importance of scalable architectures

One of the biggest lessons I learned was the importance of software architecture and code organization in large applications.

---

# 🔮 Future Improvements

If I rebuild MangIA today, I would implement:

- MVVM Architecture
- Clean Architecture
- Repository Pattern
- Dependency Injection
- Better state management
- Improved modularization
- Offline-first support

---

# 🎯 Project Goal

MangIA was developed as a thesis project for my technical high school degree, where I independently worked as the Android developer and designed the application's core functionality and user experience.

The project allowed me to apply real-world mobile development concepts while building a complete social platform from scratch.

---

# 👨‍💻 Author

Matías Alzu  
Android Developer
