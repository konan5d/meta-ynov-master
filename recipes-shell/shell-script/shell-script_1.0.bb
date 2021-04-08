DESCRIPTION = "Simple Shell application"
SECTION = "examples"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://start.sh \
           file://start2.sh \
           file://lcd-init.sh \
"

do_install () {
    install -d ${D}${sbindir}
    install -m 0755 ${WORKDIR}/start.sh ${D}${sbindir}
    install -m 0755 ${WORKDIR}/start2.sh ${D}${sbindir}
    install -m 0755 ${WORKDIR}/lcd-init.sh ${D}${sbindir}
}

FILES_${PN} += "${bindir}/start.sh"
FILES_${PN} += "${bindir}/start2.sh"
FILES_${PN} += "${bindir}/lcd-init.sh"
