# MemoNest

MemoNest is a Kotlin Android journal app built for the `DLBCSEMSE02 Mobile Software Engineering II` project. The app is focused on basic journaling first: writing entries, editing them later, browsing them in a list, attaching one photo, and quickly finding older entries again with search and simple filters.

<p align="center">
  <img src="docs/assets/memonest-app-icon.svg" width="144" alt="MemoNest app icon" />
</p>

## What the app does

- Create, update, view, and delete journal entries
- Show all saved entries in a single overview screen
- Open a full detail screen for each entry
- Attach one image from device storage to an entry
- Search by title, content, or tags
- Filter the list to show all entries, entries with photos, or tagged entries
- Switch between system, light, and dark appearance modes

## Project structure

- `app/src/main/java/com/memonest/app/ui`
  Activities, adapter, theme setup, and screen wiring
- `app/src/main/java/com/memonest/app/data`
  Room entities, DAO, database, and repository
- `app/src/main/java/com/memonest/app/domain`
  Entry validation and save use case
- `app/src/main/java/com/memonest/app/auth`
  Local session/profile handling used to keep entries tied to the current device profile
- `app/src/test`
  Local unit tests
- `app/src/androidTest`
  Instrumented Room test
- `docs/project-report.md`
  Report notes for the course submission

## Tech choices

- Kotlin
- AndroidX + Material 3
- Room for local persistence
- `OpenDocument` activity result API for image selection
- RecyclerView for the entries list
- JUnit4 and AndroidX test libraries

## Running the project

Requirements:

- Android Studio Iguana or newer
- JDK 17
- Android SDK 34

Steps:

1. Open the repository root in Android Studio.
2. Let Gradle sync.
3. Run the app on an emulator or device with API 24+.

The project already includes the Gradle wrapper:

- `gradlew`
- `gradlew.bat`
- `gradle/wrapper/gradle-wrapper.jar`
- `gradle/wrapper/gradle-wrapper.properties`

## Notes about the current implementation

- Entries are stored locally with Room.
- The app currently supports one attached photo per entry, stored as a persisted URI string.
- Search and filter happen on the loaded entry list in the overview screen.
- A Room migration was added when photo attachments were introduced, so older local databases can still open.

## Tests

The project currently includes:

- `ValidateJournalEntryUseCaseTest`
- `SaveJournalEntryUseCaseTest`
- `JournalEntryDaoTest`

Local unit tests can be run with:

```powershell
.\gradlew.bat testDebugUnitTest
```

Debug build:

```powershell
.\gradlew.bat assembleDebug
```


