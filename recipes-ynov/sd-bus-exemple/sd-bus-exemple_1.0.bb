LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
SRC_URI = " \
 git://github.com/jorisoffouga/sd_bus_example;protocol=https \
 file://sd-server.service \
"
PV = "1.0+git${SRCPV}"
SRCREV = "6aa55c3078b267044c0487a8baa2870e79a1edf5"
S = "${WORKDIR}/git"
DEPENDS = " systemd libgpiod"
inherit cmake systemd
do_install_append(){
 install -d ${D}${systemd_system_unitdir}/
 install -m 0644 ${WORKDIR}/sd-server.service ${D}${systemd_system_unitdir}/
}
SYSTEMD_SERVICE_${PN} = "sd-server.service"
SYSTEMD_AUTO_ENABLE = "enable"