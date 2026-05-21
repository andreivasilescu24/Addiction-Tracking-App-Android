# Project requirements

## Introduction

The assignment is an individual project in which you will apply the concepts learned during the labs, in order to develop an Android application.

## Requirements
Your task is to develop an app with any topic/purpose of your choice, which has the following components and functionalities:

- Use Kotlin programming language
- Use the Jetpack Compose toolkit
- Include at least two screens
- Navigate between the screens using Jetpack Navigation
- Follow the recommended application architecture
- Integrate with at least one API (e.g. Retrofit) from the two below
  - Online API servers (e.g. Google APIs)
  - Local API server on a separate PC/laptop
 
## Bonus Points
  - Using a database (e.g. SQLite, Room) - 0.5 points
  - Input sanitization for database storage (to prevent SQLite injection) - 0.5 points
  - Encrypt the data for sensitive communication (e.g. over network / Bluetooth) - 0.5 points
  - Settings screen (customize colors, language) - 0.5 points
  - Unit testing - 0.5 points
  - Code readability, modularization, clean code - 0.5 points
  - Coroutines / Dispatcher - 0.5 points

# Project design

# Project design - Addiction Tracker

## 1. Idee

Aplicatie de monitorizare a dependentelor, conceputa pentru a ajuta utilizatorii sa renunte la obiceiuri nocive. Aplicatia ofera un sistem de "time-tracking" si "positive reinforcement", permitand utilizatorului sa urmareasca mai multe obiective simultan (ex: Social Media, Fumat, Alcool, etc.).

Spre deosebire de un simplu cronometru, aplicatia integreaza elemente de jurnalizare a recidivelor pentru a identifica factorii declansatori (triggers), afiseaza vizual progresul catre un obiectiv setat si ofera suport motivational prin citate dinamice si  milestones.

## 2. Principalele functionalitati

- **Gestionare Multi-Tracker:** Utilizatorul poate adauga si monitoriza mai multe adictii simultan. Fiecare tracker are propriul obiectiv, categorie si istoric.
- **Monitorizarea Progresului (Sobriety Counters):** Dashboard-ul afiseaza o lista cu toate trackerele active. Fiecare element include timpul scurs de la ultima recidiva, un "streak badge" si o bara de progres (LinearProgressIndicator) catre obiectivul setat (ex: 30 de zile).
- **Jurnalizarea Recidivelor (Relapse Logging):** In cazul unei abateri, utilizatorul poate reseta cronometrul direct din pagina specifica a tracker-ului, adaugand data, ora si o nota detaliata cu motivul (trigger-ul).
- **Sistem de Recompense (Milestones):** Aplicatia calculeaza automat realizarile si deblocheaza milestones (ex: 1 zi, 1 saptamana, 1 luna) direct in pagina de istoric a adictiei.
- **Filtrare si Organizare:** Posibilitatea de a filtra trackerele pe Dashboard pe baza starii lor (ex: "All", "On track", "Struggling").
- **Suport Motivational Dinamic:** Un card dedicat pe ecranul principal ("Daily Note") preia automat un citat motivational printr-un API extern.

## 3. Structura Ecranelor, Fluxul de Navigare si Elemente Jetpack Compose

Proiectul este structurat in 3 fluxuri principale de ecrane:

1.  **Dashboard Screen (Home):**
  * **TopAppBar & Navigation:** Titlul zilei curente si iconita pentru setari globale. O bara de navigatie inferioara (BottomNavigationBar) cu Home, History, Goals.
  * **Filter Row:** Un rand de tip LazyRow cu `FilterChip` pentru sortarea listei.
  * **Tracker List:** Un `LazyColumn` populat cu date din Room DB.
  * **Tracker Card:** Fiecare element (ElevatedCard) contine iconita categoriei, texte pentru nume si target, o eticheta (Badge) pentru streak-ul curent si un `LinearProgressIndicator`.
  * **Motivation Card:** Card static ce preia un string de la un API (Retrofit).
  * **Floating Action Button (FAB):** Buton circular cu iconita "+" care deschide ecranul de "Add Tracker".

2.  **Add Tracker Screen:**
  * **TopAppBar:** Buton de back (`navController.popBackStack()`).
  * **Text Input:** `OutlinedTextField` pentru numele tracker-ului (ex: "Social media") - cu logica de Input Sanitization.
  * **Selection Chips:** Componente de tip `FilterChip` sau `SegmentedButton` pentru alegerea Categoriei (cu iconite) si a Obiectivului de zile (7d, 14d, 30d, 90d).
  * **Date Picker:** Un camp selectabil care deschide dialogul standard `DatePicker` din Material 3 pentru setarea datei de inceput.
  * **Primary Action:** Un buton lat ("Start Tracking") care executa salvarea in Room Database.

3.  **Tracker Details & History Screen:**
  * **Header:** Afiseaza numele tracker-ului selectat si un rezumat cu streak-ul total de zile.
  * **Milestones Section:** Un rand orizontal (`Row` sau `LazyRow`) cu carduri mici de tip recompensa. Foloseste opacitate si iconite de lacat pentru a face diferenta intre milestone-urile atinse si cele blocate.
  * **Relapse Log List:** `LazyColumn` ce randeaza intrarile din istoric sub forma de carduri intunecate, afisand data, ora si notita salvata la recidiva.
  * **Actiune:** Un `OutlinedButton` la finalul listei ("+ Log a relapse") care va deschide un BottomSheet sau un Dialog pentru inregistrarea unui esec.

