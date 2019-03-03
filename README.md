# Bloq
A prompt journal app for Android in Kotlin

### Functionality 
- CRUD: Update, list, delete, create new entries 
- Search and filter by categories/tags 
- Integration with [Material Calendar View](https://github.com/prolificinteractive/material-calendarview)
- Entry fragment pager to swipe through entries
- Image picker

### Model & Persistence
- 3 Models: 
  - `CalendarDate`
  - `JournalEntry`
  - `FirebaseStore`
- Data persistence with Firebase Realtime Database

### UX
- Bottom navigation 
- Use of UI elements like progress bar, overflow menu, search view, material design chips
- Adherence to Material Design [Guidelines](https://material.io/design/guidelines-overview/#addition)
- Up navigation support

### Architecture 
- Jetpack [Navigation Architecture Component](https://developer.android.com/topic/libraries/architecture/navigation.html)
- Jetpack [Data binding](https://developer.android.com/topic/libraries/data-binding/)
- Model View [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) pattern
- Use of fragments
- Use of [KTX](https://developer.android.com/kotlin/ktx) and [AndroidX](https://developer.android.com/jetpack/androidx) extensions
