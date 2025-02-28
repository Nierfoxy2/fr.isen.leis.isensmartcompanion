ISEN Smart Companion Your Personal Student Assistant

Project Description :
The ISEN Smart Companion is a personal assistant application designed for ISEN Toulon students. It helps students manage their academic and associative life (such as BDE, BDS, etc.) by providing personalized advice. The app utilizes an AI model to help students balance their schedules, manage courses, participate in events, and optimize their free time.

Features :
AI-powered assistant to answer student queries.
Event management: View and track ISEN events.
*History: Save and view past interactions with the AI.
Notifications: Set reminders for events you're interested in.
*Student Agenda: View your courses and planned events.

Technologies Used :
Jetpack Compose: For building the UI.
*Google Gemini AI: For text-based AI interactions.
*Room Database: For saving interaction history.
Retrofit: To fetch dynamic event data.
Firebase: For retrieving event data.

Setup :
Clone the repository:
git clone https://github.com/Nierfoxy2/fr.isen.leis.isensmartcompanion.git

Install dependencies:
Open the project in Android Studio, and it should automatically sync with Gradle to download required dependencies.

Run the app:
Connect an Android device or start an emulator.
Run the application directly from Android Studio.

*API Key Configuration:
*To enable Gemini AI functionalities, ensure you have set up an API key from Google Gemini AI SDK. For more details on this, consult Google Gemini AI Client SDK documentation.

Features Walkthrough :
1. Main Screen
Displays the app title, logo, a text input field for user questions, and a button to submit the question.
The AI's response (currently a simulated response) will appear below the input field.
2. Navigation Bar
The bottom navigation bar allows the user to navigate between the following screens:
Home (MainScreen): The main interface of the app.
Events (EventsScreen): A list of upcoming events at ISEN.
History (HistoryScreen): A history of previous questions asked to the AI.
3. Event Management
Display a list of ISEN events using the LazyColumn component.
Upon selecting an event, users are taken to a detailed event page, showing more information such as the event date, description, and location.
4. AI Integration
Gemini AI is used to answer user questions and provide advice based on input. The AI responses are saved in the local history database.
5. Notifications
Users can set reminders for events, and notifications will be triggered at a set time (e.g., 10 seconds before the event starts).
*6. History
*The application saves all interactions with the AI to a local Room database. Users can view, delete, or clear their history.
*Local Database (Room)

*The application uses Room to store the interactions between the student and the AI, enabling users to access their past questions and answers. Each record includes:
*Question: The query asked by the student.
*Answer: The response provided by the AI.
*Timestamp: The date and time when the interaction occurred.

Event Data
Events are retrieved dynamically via a Retrofit call to an external database:
https://isen-smart-companion-default-rtdb.europe-west1.firebasedatabase.app/events.json
The data includes event information such as:

Title: Name of the event.
Description: Brief event details.
Date: Event date.
Location: Event location.
Category: Type of event (e.g., Gala, BDE, etc.).

Future Improvements
Integration with a calendar to synchronize events with the student’s academic schedule.
Enhanced AI capabilities with natural language processing for more accurate responses.
Customization options for the user interface to tailor the app to individual preferences.

Disclaimer : Lines with "*" means that the function is not implemented yet
