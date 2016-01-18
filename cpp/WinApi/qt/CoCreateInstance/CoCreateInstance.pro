QT += core
QT -= gui

CONFIG += c++11

TARGET = CoCreateInstance
CONFIG += console
CONFIG -= app_bundle

TEMPLATE = app

SOURCES += main.cpp

LIBS += -lole32 -lOleAut32 -luuid
