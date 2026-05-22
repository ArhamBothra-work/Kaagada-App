# Kaagada (ಕಾಗದ) - Interactive Kannada Learning Application

Kaagada is a feature-rich, native Android application built from scratch to gamify and simplify the process of learning the Kannada language. The platform combines modern declarative UI frameworks with cloud backends, asynchronous web service integration, and on-device Machine Learning (NLP) processing. 

The project follows the standard **MVVM (Model-View-ViewModel)** architectural pattern to ensure clean separation of concerns, scalability, and robust state management.

---

## 🚀 Key Features

* **Cloud User Management:** Secure account registration and login orchestration.
* **Persistent Progress Syncing:** Real-time synchronization of user profiles and alphabet learning progress in the cloud.
* **Comprehensive Educational Modules:** Includes structured sub-pages covering Kannada Alphabets, Flashcards, Character Quizzes, and Common Conversational Phrases.
* **Dynamic Proverbs Feed:** Asynchronously fetches authentic Kannada cultural proverbs with meanings over the web.
* **On-Device AI Text Translator:** Real-time English-to-Kannada text translation powered by natural language processing models operating completely offline.
* **Device Preference Memory:** Remembers client configuration flags locally so users navigate seamlessly past onboarding screens on returning visits.

---

## 🛠️ Tech Stack & Architecture

The application is structured to demonstrate fluency in both cutting-edge and foundational Android engineering concepts:

### 1. Presentation Layer (View)
* **Jetpack Compose:** Constructed 100% of the core app navigation, dashboards, input forms, and cards using state-driven `@Composable` functions following Material 3 design rules.
* **Legacy Interoperability (XML Wrapper):** Uses an `AndroidView` wrapper to inflate a traditional XML `<androidx.recyclerview.widget.RecyclerView>` and custom `ViewHolder` layouts to prove backward compatibility and hybrid layout proficiency.

### 2. Business Logic Layer (ViewModel)
* **AuthViewModel:** Acts as the unified architectural bridge. It isolates operational calculations from rendering, coordinates authentication, and flows data states downstream to the views via reactive `StateFlow` and `MutableStateFlow` pipelines.

### 3. Data & Networking Layer (Model)
* **Firebase Authentication:** Validates and registers application accounts via secure cloud protocols.
* **Cloud Firestore Database:** Utilizes a NoSQL document/collection model (`/users/{userId}`) to read, update, and persist active learner state records globally.
* **Retrofit & Gson Converter:** Configures an asynchronous HTTP client to capture text vectors from a remote REST API web host and automatically maps JSON text streams into strongly-typed Kotlin data models.
* **Shared Preferences:** Stores small key-value local persistent flags onto the device hardware framework.

### 4. Advanced AI / Natural Language Processing
* **Google ML Kit (NLP Translation):** Features a pre-trained language translation engine. The module fetches a lightweight machine learning translation model on first use, allowing subsequent tokenization, parsing, and translation processing to run directly on-device without internet latency or backend compute costs.

---

## 📂 Repository Package Structure

```text
com.example.kaagada/
│
├── data/                    # Data Layer (Model)
│   ├── local/               # Shared Preferences & static array assets
│   ├── model/               # Strongly-typed Kotlin Data Classes (e.g., Proverb.kt)
│   └── remote/              # RetrofitClient & ProverbApiService API endpoints
│
├── ui/                      # Presentation Layer (View)
│   ├── adapter/             # Legacy XML RecyclerView Adapters & ViewHolders
│   ├── navigation/          # NavGraph traffic routing controller
│   ├── screens/             # Jetpack Compose UI Screens (.kt)
│   └── theme/               # Material 3 typography and custom brand colors
│
├── viewmodel/               # Architecture Bridge (ViewModel)
│   └── AuthViewModel.kt     # Handles state streams & background thread requests
│
└── MainActivity.kt          # Application entry point & environment setup
