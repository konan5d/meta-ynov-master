DESCRIPTION = "Simple Python application"
SECTION = "examples"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://hour.py"


do_install () {
    install -d ${D}${bindir}
    install -m 0755 ${WORKDIR}/hour.py ${D}${bindir}
}

FILES_${PN} += "${bindir}/hour.py"
RDEPENDS_python-script-1.0 += "python3"