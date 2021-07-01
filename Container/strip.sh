#!/bin/bash

./strip-image \
	-f /tmp/mvn \
	-i priote:latest \
	-t priote_stripped:latest \
	-d Dockerfile-Stripped \
	-x 3330 \
	-f /bin/basename \
	-f /bin/bash \
	-f /bin/cd \
	-f /bin/dirname \
	-f /bin/echo \
	-f /bin/expr \
	-f /bin/pwd \
	-f /bin/readlink \
	-f /bin/sh \
	-f /bin/test \
	-f /bin/tr \
	-f /bin/uname \
	-f /bin/which \
	-f /etc/group \
	-f /etc/passwd \
	-f '/lib64/*' \
	-f /root/.m2/ \
	-f /usr/bin/coreutils \
	-f /usr/bin/mvn \
	-f /usr/java/openjdk-15/ \
	-f /usr/lib/locale/ \
	-f /usr/lib/x86_64-linux-gnu/ \
	-f '/usr/lib64' \
	-f /usr/share/maven/ \
	-f /usr/src/mymaven/
#	-f /usr/lib64/libc-2.28.so \
#	-f /usr/lib64/libc.so.6 \
#	-f /usr/lib64/libm-2.28.so \
#	-f /usr/lib64/libm.so.6 \
#	-f '/usr/lib64/libnss*' \
#	-f /usr/lib64/libz.so.1 \
#	-f /usr/lib64/libz.so.1.2.11 \
