#!/bin/sh

printf "\fService \nstarting... " > /dev/lcd

for i in `seq 0 7`;
do
  	echo 0 > /sys/class/leds/d$i/brightness
done

sleep 0.5

for i in `seq 0 7`;
do
  	echo 255 > /sys/class/leds/d$i/brightness
        sleep 0.5
done

if [ -e /sys/class/hwmon/hwmon1/temp1_input ]
then
    	printf "\fCPU: %s degres\nLM75: %s degres" $(($(cat /sys/class/hwmon/hwmon0/temp1_input)/1000)) $(($(cat /sys/class/hwmon/hwmon1/temp1_input)/1000)) > /dev/lcd
else
    	printf "\fCPU: %s degres\nLM75: %s degres" $(($(cat /sys/class/hwmon/hwmon0/temp1_input)/1000)) $(($(cat /sys/class/hwmon/hwmon2/temp1_input)/1000)) > /dev/lcd
fi

