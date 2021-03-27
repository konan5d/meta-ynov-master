require recipes-core/images/core-image-base.bb
#include the wifi distro with linux firmware rpidistro bcm43430

IMAGE_INSTALL_append = " \
 i2c-tools \
 libgpiod-tools \
 spitools \
 linux-firmware-rpidistro-bcm43430 \
"
IMAGE_FEATURES_append = " ssh-server-dropbear "