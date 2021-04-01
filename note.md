This branch is create to use python3 to control raspberry with Blynk application

# Set app and lib on python

- Downlaod app for your mobile store
- create new project with general board 

To use blynk you need to activate python3 and pip3 on raspberry to download lib blynklib and run python apps

in your current custom layer open: recipes-core->images->layerCustom.bb

add this line :

    "python3 \
     python3-pip \" in IMAGE_INSTALL_append 

Compile image on sd card and launch your target

In the target shell : 

    $: pip3 install Blynklib

At next copy your python apps in the /home/root sessions and run with :

    $: python3 yourApp.py



--- 

To test conection run the  basic.py program and if "new device connected" is notify on your phone the connection is success !


    
