FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# add cnfig network resolved package
PACKAGECONFIG_append = " networkd resolved"
# add depend repository wpa-supplicant
# PN refers to the Package Name 
# that is extracted from the recipe file name
RDEPENDS_${PN}_append = " wpa-supplicant "

# add file created in systemd-conf folder to load conf
SRC_URI_append = " \
 file://eth0.network \ 
 file://eth.network \
 file://en.network \
 file://wlan.network \
"
 
do_install_append(){
 # installation d'un fichier dans le rootfs
 # ${WORKDIR}/eth0.network : on va chercher le fichier de configuration dans la recette
 # ${D}/${systemd_unitdir}/network/ : on installe le fichier dans le répertoire network sur la cible
 # ${D} : rootfs de notre cible
 
 install -Dm 0644 ${WORKDIR}/eth0.network ${D}/${systemd_unitdir}/network/ 

# Install new create file configuration on target
 install -Dm 0644 ${WORKDIR}/eth.network ${D}/${systemd_unitdir}/network/ 
 install -Dm 0644 ${WORKDIR}/en.network ${D}/${systemd_unitdir}/network/ 
 install -Dm 0644 ${WORKDIR}/wlan.network ${D}/${systemd_unitdir}/network/ 
 
 rm ${D}/${systemd_unitdir}/network/80-wired.network
}