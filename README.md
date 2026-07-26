# ReadSphere

ReadSphere is an Android document reader app built with Jetpack Compose and modern Android tooling. It provides a customizable reading experience for PDF, Word, and PowerPoint documents, with a rich feature set focused on reading comfort, annotation, and document management.

## Key Features

- Open and view documents from device storage
- Support for PDF, DOCX, and PPTX files
- Customizable reading modes: Light, Dark, Sepia, True Black, High Contrast
- Adjustable appearance settings, including theme mode, accent color, dynamic colors, font size, line spacing, and margin size
- Document search and navigation controls
- Bookmarks, favorites, and recent document tracking
- Onboarding flow and reading statistics
- Text-to-speech support for document playback
- Background work support via WorkManager
- Local preferences storage using DataStore
- Dependency injection with Hilt
- Local database support with Room

## Architecture

ReadSphere follows a modular architecture with clear separation between layers:

- `app/src/main/kotlin/com/readsphere/app/presentation` — UI and screen composables
- `app/src/main/kotlin/com/readsphere/app/domain` — domain models, repositories, and use cases
- `app/src/main/kotlin/com/readsphere/app/data` — repository implementations, local database, and storage
- `app/src/main/kotlin/com/readsphere/app/di` — dependency injection modules
- `app/src/main/kotlin/com/readsphere/app/core` — shared themes, utilities, and common composables

## Tech Stack

- Kotlin
- Jetpack Compose
- Hilt for dependency injection
- Room for local database storage
- DataStore preferences
- WorkManager for background tasks
- AndroidX Navigation Compose
- Coil for image loading and PDF preview
- PDFium for PDF rendering
- Apache POI for Word/PowerPoint parsing
- JSoup for HTML parsing
- ML Kit Translation
- Lottie animations

## Build & Run

1. Clone the repository or use the local project.
2. Open the project in Android Studio.
3. Make sure the Android SDK is installed for API level 35.
4. Sync Gradle.
5. Run the `app` module on an Android device or emulator.

## Notes

- Minimum SDK level: 28
- Target SDK level: 35
- The app uses Kotlin JVM target 17

## Repository

This project is pushed to: https://github.com/kidus00t-arch/ReadSpehre

## License

Add your preferred license here if you want to publish this project publicly.
