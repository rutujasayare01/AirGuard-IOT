# AirGuard IoT

### IoT-Enabled Real-Time Industrial Air Pollution Monitoring and Regulatory System

AirGuard IoT is an IoT-based industrial air pollution monitoring system designed to monitor air quality parameters in real time, provide AQI-based insights, generate alerts, and support regulatory monitoring through centralized dashboards.

## 🚀 Features

- Real-time industrial air quality monitoring
- Monitoring using MQ135, MQ4, MQ7 and DHT11 sensors
- ESP32-based data collection
- Firebase Realtime Database for real-time data synchronization
- AQI monitoring and prediction using Machine Learning
- Admin, Industry and Public dashboards
- Automatic pollution alerts
- Regulatory penalty management
- PDF penalty/receipt generation
- Location-based AQI monitoring
- Timestamp-based monitoring and data logging

## 🛠️ Technologies Used

**Hardware**
- ESP32
- MQ135
- MQ4
- MQ7
- DHT11

**Android Application**
- Android Studio
- Java
- XML
- Firebase Realtime Database

**Backend & Database**
- Firebase
- Spring Boot
- MySQL

**Machine Learning**
- Python
- Machine Learning
- AQI Analytics

**Other Technologies**
- React
- Grafana
- InfluxDB
- Maven

## ⚙️ System Workflow

```text
Air Quality Sensors
        ↓
      ESP32
        ↓
Firebase Realtime Database
        ↓
Data Processing & AQI Analysis
        ↓
Machine Learning / AQI Prediction
        ↓
Admin / Industry / Public Dashboards
        ↓
Alerts & Regulatory Actions
```

## 📱 Android Application

The Android application is developed using **Android Studio with Java and XML**.

It provides real-time access to pollution monitoring data and supports role-based dashboards, alerts, AQI information and regulatory monitoring.

## 🌱 Environmental Parameters

The system uses multiple sensors to monitor different environmental parameters:

| Sensor | Purpose |

| MQ135 | Air quality / harmful gases |
| MQ4 | Methane / combustible gases |
| MQ7 | Carbon monoxide |
| DHT11 | Temperature and humidity |

## 🎯 Objective

The main objective of AirGuard IoT is to provide a real-time and centralized solution for industrial air pollution monitoring, helping industries and regulatory authorities monitor pollution levels and respond to threshold violations.

## 👩‍💻 Project

**Project Name:** AirGuard IoT  
**Type:** IoT + Android + Machine Learning  
**Domain:** Environmental Monitoring / IoT

## 📌 Note

This repository contains the software and application components developed as part of the AirGuard IoT project.
