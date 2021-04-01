FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append = " \
file://0001-add-lm75a-sensor-and-mcp23008-gpio-expander.patch \
"
