# Project Plan

A Kannada language learning app named 'Kaagada'. The app should include at least 8 screens: Login, Signup, Home/Dashboard, and 5 others (e.g., Learning Alphabets, Alphabet Identification/Pronunciation, Basic Phrases, Profile, Onboarding). It must use Kotlin and Jetpack Compose. Use Shared Preferences for storage as requested in the CIE-3 marks breakdown. The design should follow the provided color scheme (94061E and FFEB81) and include the mascot from the images. Material Design 3 and Edge-to-Edge are mandatory.

## Project Brief

# Project Brief: Kaagada - Kannada Learning App

Kaagada is a vibrant and interactive Android application designed to make learning the Kannada language accessible and engaging. Named after the Kannada word for 'paper', the app serves as a digital canvas for users to master the alphabets, pronunciation, and basic conversational phrases.

## Features
- **Interactive Alphabet Learning:** Engaging modules to teach the identification and writing of Kannada characters.
- **Audio-Visual Pronunciation Guide:** Integrated audio clips for every alphabet and phrase to ensure accurate native-like pronunciation.
- **Essential Phrases Library:** A curated collection of basic phrases for everyday communication, categorized for easy navigation.
- **Personalized Onboarding & Progress:** A seamless onboarding experience with a dedicated user profile and dashboard to track learning milestones.
- **Gamified Dashboard:** A centralized hub (Home) that provides quick access to lessons and highlights the user's current progress.

## High-Level Technical Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material Design 3)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Persistence:** Shared Preferences (for user state and basic progress tracking)
- **Code Generation:** KSP (Kotlin Symbol Processing)
- **Asynchronous Programming:** Kotlin Coroutines

## UI Design Image
![UI Design](C:/Users/Atsuhiro/AndroidStudioProjects/Kaagada/input_images/image_0.png)
Image path = C:/Users/Atsuhiro/AndroidStudioProjects/Kaagada/input_images/image_0.png

## Implementation Steps

### Task_1_Theme_Base_Fix: Update Theme and MainActivity. Set background and button text to #94061E. Set buttons, icons, and external text to #FFEB81. Set input field background to #FFFFFF with black text inside. Ensure MainActivity launches NavGraph. Remove all mascot references.
- **Status:** COMPLETED
- **Updates:** User requested to skip the mascot removal part of Task 1 and move directly to Task 2. The theme and base setup (including color scheme) will be refined during subsequent tasks. The user has specified a strict color scheme: #94061E (Primary/Background), #FFEB81 (Secondary/Accents/Buttons), and #FFFFFF (Input fields with black text).
- **Acceptance Criteria:**
  - Background is #94061E
  - Buttons are #FFEB81 with #94061E text
  - Input fields are #FFFFFF with black text
  - MainActivity launches NavGraph
  - Mascot assets and references removed

### Task_2_Auth_Flow_Refinement: Refine Login/Signup screens and navigation. Remove 'Forgot Password'. Implement navigation logic to block back button from Home to Login.
- **Status:** COMPLETED
- **Updates:** Implemented Task 2: Auth Flow Refinement.
- Refined Login and Signup screens in Screens.kt with strict adherence to the requested color scheme: #94061E background, #FFEB81 for logos/buttons/labels, and #FFFFFF for input fields with Black text.
- Completely removed any 'Forgot Password' references.
- Updated NavGraph.kt with popUpTo(0) { inclusive = true } for navigation to Home, ensuring the back button from Home does not return to Login/Signup.
- Replaced mascot references with Material Icons.
- Verified project builds successfully.
- **Acceptance Criteria:**
  - Forgot password text removed
  - Back from Home does not return to Login
- **Duration:** N/A

### Task_3_Learning_Modules_Update: Overhaul Quiz logic and Phrases layout. Quiz shows correct answer on fail, has a 'Next' button, and increments score correctly. Phrases screen uses a grid similar to Alphabets. Remove all progress counters and metrics.
- **Status:** IN_PROGRESS
- **Acceptance Criteria:**
  - Quiz logic functional with feedback and 'Next' button
  - Phrases screen matches Alphabet grid style
  - All progress counters removed
- **StartTime:** 2026-04-16 20:01:20 IST

### Task_4_My_Profile_Overhaul: Redesign Profile screen to 'My Profile'. Add functionality to edit name and password. Remove achievements. Add profile picture placeholder.
- **Status:** PENDING
- **Acceptance Criteria:**
  - Name and password editing implemented
  - Achievements removed
  - Profile picture placeholder exists

