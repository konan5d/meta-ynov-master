LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
 file://lcd.service \
"

DEPENDS = " systemd "
inherit systemd

do_install_append(){
 install -d ${D}${systemd_system_unitdir}/
 install -m 0644 ${WORKDIR}/lcd.service ${D}${systemd_system_unitdir}/
}

SYSTEMD_SERVICE_${PN} = "lcd.service"
SYSTEMD_AUTO_ENABLE = "enable"
