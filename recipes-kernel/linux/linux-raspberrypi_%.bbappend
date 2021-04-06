FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
file://0001-add-lm75a-sensor-and-mcp23008-gpio-expander.patch \
file://0002-add-LCD-16x2-display.patch \
file://fragment.cfg \
"
