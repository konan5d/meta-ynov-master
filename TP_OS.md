# TP OS Embarqué

CUISINIER Guillaume  
CHICHERIT-ROUBAUD Marilyn  
LEGEAY Corentin  
<br>

## Mise en place d'un affichage de température sur un écran LCD et sur une application
<br>
**Matériels :**

- Raspberry Pi 3b
- Carte YNOV
  - Capteur de température LM75A
  - LEDs + GPIO expander MCP23008
- Ecran LCD  2x16 lignes + GPIO exepender PCF8574T (I2C)
- Environnement Yocto dans la Raspberry (meta-master-raspberry et meta-master-ynov)
<br>

### 1. Installation de la wifi sur la Raspberry
   
Nous sommes partit du layer meta-ynov-master, auquel nous avons ajouté une recette : "recipe-connectivity" pour ajouter la wifi à la configuration. Puis dans le dossier "file" nous avons créé un fichier .conf afin d'ajouter le SSID et le PSK de la connexion :

**wpa_supplicant-nl80211-wlan0.conf**  
network={  
	ssid="Bbox-14CE719E"  
	#psk="15A69EE4C44F39662F5577ADA9A141"  
	psk=893fc8270409d30a2575c0189412a79aaef931fe8672 [...]  
}  

Le fichier "wpa_supplicant_%.bbappend" a aussi été créé pour permettre la prise en compte dans la layer.

Après compilation, nous avons testé cette nouvelle configuration dans la Raspberry. Avec la commande ifconfig nous avons pu voir que nos connexions étaient bien établies : [capture d'écran à ajouter]
Ainsi que la ligne de commande ping pour vérifier notre accès à internet depuis la Raspberry. Elle est maintenant connectée à la wifi !
<br>

### 2. Driver du capteur de température et des LEDs
<br>
   
### 3. Driver  GPIO expander + de l'écran

Pour réaliser nos essais, nous avons dans un premier temps fait les tests sur le kernel Raspberry puis sur le kernel Yocto.

**Recherche de l'adresse du GPIO expander : 0x27**
[image avec tableau des adresse du device]  

**Activation du port I2C sur la Raspberry dans le fichier bcm2710 :**  

& i2c {
        pcf8574: pcf8574@27 {  #ajout du device et de son adresse 0x27  
        compatible = "nxp,pcf8574";  
        reg = <0x27>;  
        gpio-controller;  
        # gpio-cells = <2>;  
    };  
};  
  
**Trouver le brochage GPIO entre l'écran et l'expandeur grace au noeud "auxdisplay" toujours dans le fichier bcm2710.dts (méthode sans multimètre !) :**

auxdisplay {  
            compatible = "hit,hd44780";  
            data-gpios = <& hc595 0 GPIO_ACTIVE_HIGH>,  
                            <& hc595 4 GPIO_ACTIVE_HIGH>,  
                            <& hc595 5 GPIO_ACTIVE_HIGH>,  
                            <& hc595 6 GPIO_ACTIVE_HIGH>;  
            enable-gpios = <& hc595 7 GPIO_ACTIVE_HIGH>;  
            rs-gpios = <& hc595 5 GPIO_ACTIVE_HIGH>;  

            display-height-chars = <2>;  
            display-width-chars = <16>;  
        };  

*Noeud ajouté dans notre arborescence de périphériques puis configurer avec les broches du PCF8574T.*
Grace a un programme Arduino et l'utilisation précédente de cet écran, nous avons identifier rapidemment les brochages GPIO de l'écran (broches 4 à 7).

Ensuite dans le menuconfig du kernel Raspberry, nous avons activer les pilotes suivants :
+ CONFIG_GPIO_PCF857X = y 
+ CONFIG_AUXDISPLAY = y 
+ CONFIG_HD44780 = y

Cette configuration a ensuite été enregistrée grace a la commande *savedefcongif* pour créer un fichier config et écrire ses modifications dans le kernel Yocto.
Au démarage de la Raspberry, la commmande *i2cdetect -y 1* nous a permis de voir que notre device été bien initialisé à l'adresse 27.
[capture d'écran du code]

**Affichage de la température CPU et du capteur de température :**
Tout d'abord nous avons branché l'écran LCD sur la Raspberry pour récupérer la température du CPU de la Raspberry. Pour cela il fallait récuperer la valeur présente dans le fichier XXXX et l'afficher sur l'écran LCD :  
printf "CPU : %s degres" $(cat(syst/class/xxx)) >dev/lcd

[Capture d'écran de l'affichage de la température sur le LCD]

Ensuite nous avons branché l'écran à la carte Ynov pour récupérer la température du capteur LM75A.
Pour cela nous avons rajouté une valeur a afficher dans le fichier du capteur de température :
printf "CPU : %s degres \n LM : %s degres" $((cat(syst/class/xxx))(syst/class/xxx) >dev/lcd
[capture d'écran de cet affichage sur le LCD]  
<br>

### 4. Application Blynk avec python 3