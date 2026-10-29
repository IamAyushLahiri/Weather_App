# 🌦️ Weather App

**A Modern Android Weather Application Built with Kotlin, Jetpack Compose & Retrofit**

Weather App is an Android application designed to retrieve and display weather information through a REST API. Built with Kotlin and Jetpack Compose, the application demonstrates modern Android UI development and API integration.

The project focuses on building a clean mobile experience while practising native Android development, network communication, and user interaction.

---

## 📱 Screenshots

Explore the application's interface and core screens.

<table>
  <tr>
    <td align="center" width="33%">
      <b>Home Screen</b>
    </td>
    <td align="center" width="33%">
      <b>Weather Details</b>
    </td>
    <td align="center" width="33%">
      <b>City Search</b>
    </td>
  </tr>
  <tr>
    <td>
      <img src="./WhatsApp%20Image%202026-10-29%20at%2011.39.41%20AM.jpeg" alt="Weather App Home Screen" width="100%">
    </td>
    <td>
      <img src="./WhatsApp%20Image%202026-10-29%20at%2011.39.42%20AM.jpeg" alt="Weather Details Screen" width="100%">
    </td>
    <td>
      <img src="./WhatsApp%20Image%202026-10-29%20at%2011.39.43%20AM.jpeg" alt="City Search Screen" width="100%">
    </td>
  </tr>
</table>

---

## ✨ Features

* **Weather Information:** Retrieves weather data from a remote REST API.
* **City Search:** Provides a search interface for finding weather information by city, as implemented in the application.
* **Modern Android UI:** Uses Jetpack Compose to build the application's user interface.
* **API Integration:** Uses Retrofit for communication with the weather service.
* **Native Android Experience:** Designed to run on Android devices.

> The feature list should reflect the functionality available in the current version of the application.

---

## 🛠️ Tech Stack

| Technology      | Purpose                                    |
| --------------- | ------------------------------------------ |
| Kotlin          | Primary programming language               |
| Jetpack Compose | Declarative Android UI development         |
| Retrofit 2      | REST API communication                     |
| Android SDK     | Android application development            |
| Gradle          | Build automation and dependency management |
| Android Studio  | Development environment                    |

---

## 🏗️ Project Overview

The application combines a native Android interface with remote weather data retrieval.

At a high level, the application follows this flow:

1. The user interacts with the application interface.
2. The application processes the user's search input, where supported.
3. Retrofit communicates with the configured weather API.
4. The application receives the API response.
5. Weather information is presented through the Android UI.

This workflow illustrates the integration of network services into an Android application.

---

## 🚀 Getting Started

Follow these steps to run the project locally.

### Prerequisites

Make sure you have the following installed:

* Android Studio
* Android SDK compatible with the project
* JDK version compatible with the project's Gradle configuration
* Internet connection
* A valid weather API key, if required by the configured service

### 1. Clone the Repository

Open a terminal and run:

```bash
git clone https://github.com/IamAyushLahiri/Weather_App.git
```

### 2. Open the Project

Open Android Studio and select **Open**.

Navigate to the cloned `Weather_App` directory.

### 3. Sync Gradle

Allow Android Studio to download the required dependencies and complete Gradle synchronisation.

Resolve any dependency or SDK configuration issues before proceeding.

### 4. Configure the Weather API

If the application requires an API key, configure it according to the project's existing implementation.

Do not publish private credentials or commit real API keys to GitHub.

### 5. Run the Application

Connect an Android device with USB debugging enabled or start an emulator.

Click **Run** in Android Studio to build and launch the application.

---

## 🔐 API Key Security

API credentials should not be committed to a public repository.

For local development:

* Keep private credentials out of tracked source files.
* Use an appropriate local configuration mechanism.
* Add sensitive local configuration files to `.gitignore` where applicable.
* Provide setup instructions without exposing the actual key.

**Important:** API keys embedded in a distributed Android application may be recoverable from the application package. Use the weather provider's restrictions and appropriate backend protection for sensitive credentials.

If a real secret has already been committed publicly, removing it from the latest commit alone may not be sufficient. Rotate or revoke the exposed credential.

---

## 📂 Project Structure

The project uses the standard Android Gradle project layout.

```text
Weather_App/
├── app/
├── gradle/
├── .gitignore
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
└── README.md
```

The structure above is illustrative. The actual files and directories may differ depending on the current project configuration.

---

## 🎯 Learning Outcomes

This project provides practical experience with:

* Developing Android applications using Kotlin.
* Building user interfaces with Jetpack Compose.
* Integrating REST APIs using Retrofit.
* Handling remote data in a mobile application.
* Managing dependencies and builds with Gradle.
* Documenting and maintaining an Android project using GitHub.

---

## 🔮 Future Improvements

Potential areas for further development include:

* Add dedicated loading, success, empty, and error states.
* Improve network failure handling and input validation.
* Introduce a structured MVVM architecture if not already implemented.
* Add automated tests for important application logic.
* Improve accessibility and support for different screen sizes.
* Add weather history or saved cities.
* Document API configuration and supported Android versions.
* Add a demonstration video showing the application's workflow.

These are possible enhancements, not claims about features already implemented.

---

## 👨‍💻 Author

**Ayush Lahiri**

IT Undergraduate | Android Developer

* GitHub: [@IamAyushLahiri](https://github.com/IamAyushLahiri)
* Repository: [Weather_App](https://github.com/IamAyushLahiri/Weather_App)

Interested in Android development, Kotlin, modern mobile UI development, and building practical software applications.

---

## 📄 License

No license is specified in this README.

If you intend to distribute the project for reuse, consider adding an appropriate `LICENSE` file to the repository.

---

<div align="center">

**Built with Kotlin and Jetpack Compose**

*Learning, building, and improving one project at a time.*

</div>
