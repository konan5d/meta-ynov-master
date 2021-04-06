# TP OS Embarqué

CUISINIER Guillaume  
CHICHERIT-ROUBAUD Marilyn  
LEGEAY Corentin  
<br>

# Mise en place d'un affichage de température sur un écran LCD et sur une application

Matériels :

- Raspberry Pi 3 B
- Expansion Board WaRP7: 
  - Capteur de température LM75A
  - LEDs + GPIO expander MCP23008
- Ecran LCD 2x16 lignes + GPIO exepender PCF8574T (I2C)
<br>

# Introduction

Le but de ce TP est de mettre en oeuvre :
* La personnalisation et la compilation d'une image pour une cible spécifique avec Yocto
* La création d'une layer
* La création et la ersonnalisation des recettes, ou la personnalisation de recettes existantes
* L'intégration de plusieurs composants électroniques
* La configuration d'un service pour se connecter automatiquement au wifi
* La communication avec une application externe, grâce à un programme Python (avec les libraires nécessaires)

Pour personnaliser notre image comme nous le souhaitons, nous avons créé une layer "meta-ynov-master". Les différentes recettes décrivent les étapes pour récupérer les sources, pour les compiler et les installer sur notre image. Elles sont par la suite accessible sur notre cible. 

# 1. Initialisation et connexion automatique à un réseau local sans fil :

Il faut tout d'abord créer une nouvelle recette : "recipe-connectivity". Dans cette recette ,nous ajourtons la configuration de notre réseau sans fil(SSID + clé). 

**wpa_supplicant-nl80211-wlan0.conf**  

```conf
  network={  
    ssid="Bbox-14CE719E"  
    #psk="15A69EE4C44F39662F5577ADA9A141"  
    psk=893fc8270409d30a2575c0189412a79aaef931fe8672 [...]  
  }  
```

Dans le fichier "**wpa_supplicant_%.bbappend**", nous retrouvons plusieurs informations.

Nous avons ajouté le chemin d'accès au fichier de configuration du wifi dans la variable "SRC_URI". 

Puis, il faut spécifier le nom de notre service, et qu'il doit être actif au démarrage de la cible :

```
SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN}_append = " wpa_supplicant-nl80211@wlan0.service  "
```

Enfin, la fonction "**do_install_append ()**" permet d'installer toute la configuration lors du processus de compilation de l'image.

Après compilation de notre image, il faut tester que notre nouvelle configuration a bien été prise en compte et que la connexion s'est bien effectué, avec notre réseau local.

Tout d'abord, on vérifie que la configuration a bien été chargée :

```
  $: ifconfig

  => 
```
Puis, on vérifie que notre cible a bien accès à internet. On peut par exemple envoyer un ping à www.google.fr :

```
  $: ping www.google.fr

  => 
```

Notre cible est désormais connecté au réseau local, mais aussi à internet.

<br>

## 2. Intégration d'un capteur de température et de LEDS :

Tout d'abord Le capteur de température (LM75A) et les leds sont intégrés à la carte d'extension "**Expansion Board WaRP7**.

Pour communiquer avec notre cible (la RPi 3 B), il faut relier cette carte via le bus i2c1.

<br>
   
## 3. Intégration d'un écran LCD 16x2

Pour faciliter la communication avec cet écran, il dispose d'un GPIO expander, utilisable via l'i2c.

Dans un premier temps, afin de ne pas recompiler notre image à chaque fois, nous avons uniquement recompiler le device tree, dans le kernel "raspberry".

Néanmoins, il faut flasher une première fois la carte SD de notre cible avec une image (compilé avec Yocto). 

L'intégration de l'écran nécessite plusieurs étapes pour que nous puissions l'utiliser sur notre cible :

* 1: Recherche de l'adresse i2c du GPIO expander 
* 2: Intégration d'un noeud i2c dans le device tree pour l'utiliser
* 3: Intégration d'un noeud auxdisplay pour utiliser l'écran en lui même
* 4: Affichage simple de la température du CPU et du LM75A

**1 : Recherche de l'adresse i2c du GPIO expander**

D'après la datasheet ....   

**2: Intégration d'un noeud i2c dans le device tree pour l'utiliser**  

```dts
&i2c1 {
        pcf8574: pcf8574@27 {  #ajout du device et de son adresse 0x27  
        compatible = "nxp,pcf8574";  
        reg = <0x27>;  
        gpio-controller;  
        # gpio-cells = <2>;  
    };  
};  
```
  
**3: Intégration d'un noeud auxdisplay pour utiliser l'écran en lui même :**

Il faut initialiser l'état par défaut des broches de l'écran. Pour trouver la configuration, nous avons utilisé la librairie Arduino "LiquidCrystal I2C".

```dts
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
```

Ensuite dans le menu de configuration du kernel Raspberry, nous avons activé les pilotes suivants :
+ CONFIG_GPIO_PCF857X = y 
+ CONFIG_AUXDISPLAY = y 
+ CONFIG_HD44780 = y

Une fois l'intégration finie, il faut compiler le device tree :
```
  $: make ARCH=arm CROSS_COMPILE=arm-linux-gnueabihf- bcm2710-rpi-3-b.dtb -j$(nproc)
```

Lorsque la compilation est terminée, on remplace le device tree dans la partition "\boot" de la carte SD.

Au démarage de la Raspberry, il faut tester la nouvelle configuration.

Tout d'abord, on vérifie que notre composant est bien détecté :
```
  $: i2cdetect -y 1

  =>
```

Cette commande nous montre bien que le GPIO Expader est bien détecté, et il est prêt à ếtre utilisé (présence de "**UU**" à l'adresse 0x27).


**4: Affichage simple de la température du CPU et du LM75A, sur l'écran LCD**

L'intéraction avec notre écran peut se faire à l'aide d'une simple ligne de commande :

```
  $: printf "data" > dev/lcd"
```

Cette commande permet d'écrire une chaine de caractère dans "lcd". Elle sera par la suite affiché à l'écran. 

Nous voulons afficher la température CPU à l'écran. Il faut lire la température dans **syst/class/xxx**, et concaténer le contenu dans la sortie standard. 

```
 $: printf "CPU : %s degres" $(cat(syst/class/xxx)) > dev/lcd
```

Ensuite nous avons branché l'écran à la carte Ynov pour récupérer la température du capteur LM75A.
Pour cela nous avons rajouté une valeur à afficher dans le fichier du capteur de température :

```
  $: printf "CPU : %s degres \n LM : %s degres" $((cat(syst/class/xxx))(syst/class/xxx) >dev/lcd
```

## 4. Application Blynk avec python 3