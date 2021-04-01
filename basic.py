#!/usr/bin/env python3 
import blynklib

#------------Var----------------
BLYNK_AUTH  =  'RuE4Hwml0pEnXtSJ7w5UfO0BUSAyS7U7'  #  insérez votre jeton d'authentification ici 

#-----------INIT---------------

#  blynk init 
blynk  =  blynklib.Blynk ( BLYNK_AUTH )

while True :
    blynk.run()