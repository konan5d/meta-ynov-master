LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = " \
 file://lcd-test.sh \
 file://lcd.service \
"

DEPENDS = " systemd"

inherit systemd

do_install(){
 install -d ${D}${sbindir}
 install -m 0755 ${WORKDIR}/lcd-test.sh ${D}${sbindir}
}

do_install(){
 install -d ${D}${systemd_system_unitdir}/
 install -m 0644 ${WORKDIR}/lcd.service ${D}${systemd_system_unitdir}/
}


FILES_${PN} += "${sbindir}/lcd-test.sh"

SYSTEMD_SERVICE_${PN} = "lcd.service"
SYSTEMD_AUTO_ENABLE = "enable"
