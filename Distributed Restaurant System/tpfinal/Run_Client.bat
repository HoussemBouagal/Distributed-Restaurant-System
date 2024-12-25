@echo off
REM الانتقال إلى مسار المجلد الذي يحتوي على ملفات المشروع
cd /d C:\Users\HoussamDev.24\Desktop\Projet_Final\com\bin
REM تعيين CLASSPATH ليشمل المجلد الحالي
set CLASSPATH=.;rmiclient

REM تشغيل مسجل RMI
start rmiregistry

REM تشغيل ClientOperation.class
java rmiclient.ClientOperation

pause
