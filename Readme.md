# Sample app for map

#Tools and devices
1. Android studio
2. Emulator Nexus 5X API 30
3. Emulator Pixel XL 4 API 30

#This project includes 
1. MVVM architecture
2. Retrofit
3. Coroutines
4. Google hilt
5. Data binding
6. Live data
7. Navigation
8. Shared view model
9. UI and unit test cases. UI test cases will be green if internet is available.
10. Screen rotation data persists
11. network-security-config inspired from https://developer.android.com/training/articles/security-config
12. Generic error handling
13. Coordinates has been truncated to 6th places after dot.

#Enhancement
1. Can add configuration structure. Also data caching can be added.
2. Can pass base url from host app via configuration. 
3. Use security protocols for network calls.
4. Minor bug fixes and ui changes can be scope for future changes.
5. Release and proguard configuration.

# Note
Architecture source is inspired from android developer community
![App architecture](final-architecture.png "final-architecture")
https://developer.android.com/jetpack/guide


Why to use hilt and not Dagger or Koin?
Hilt is built on top of the Dagger dependency injection library, 
providing a standard way to incorporate Dagger into an Android application.
More details from android developer site - 
https://developer.android.com/training/dependency-injection/hilt-android#hilt-and-dagger

Images
WebP files are created using online photo-editor and are kept in drawable folder only.   

We can ride on one boat at a time. 

This is not a full fledged app. Check enhancements point 4.
The proposed solution can be done in numerous ways. 