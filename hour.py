#!/usr/bin/env python3 
import blynklib
from time import sleep
from datetime import datetime

# Initialize Blynk
BLYNK_AUTH  =  'RuE4Hwml0pEnXtSJ7w5UfO0BUSAyS7U7'  #  insérez votre jeton d'authentification ici 
blynk  =  blynklib.Blynk ( BLYNK_AUTH )

currentTime = ""

# Register Virtual Pin
@blynk.handle_event('read V2')  # app send request to target
def read_handler(vpin):
    
    currentTime = datetime.now()
    blynk.virtual_write(2, currentTime.strftime("%d/%m/%Y %H:%M:%S"))
    
print("started!")

while True:
    blynk.run()