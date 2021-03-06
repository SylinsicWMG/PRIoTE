#!/bin/bash
docker build -t priote_i:1.0 -t priote_i:latest .
scripts/strip-image \
	-i priote_i:latest \
	-t priote_stripped_i:latest \
	-d Dockerfile \
	-x 3330 \
	-f /etc/group \
	-f /etc/nsswitch.conf \
	-f /etc/passwd \
	-f /root/.m2/ \
	-f /usr/bin/basename \
	-f /usr/bin/bash \
	-f /usr/bin/cd \
	-f /usr/bin/coreutils \
	-f /usr/bin/curl \
	-f /usr/bin/dirname \
	-f /usr/bin/echo \
	-f /usr/bin/expr \
	-f /usr/bin/ln \
	-f /usr/bin/ls \
	-f /usr/bin/pwd \
	-f /usr/bin/readlink \
	-f /usr/bin/sh \
	-f /usr/bin/test \
	-f /usr/bin/tr \
	-f /usr/bin/uname \
	-f /usr/java/openjdk-15/ \
	-f /usr/lib64/libc.so.6 \
	-f /usr/lib64/libm.so.6 \
	-f '/usr/lib64/libnss*' \
	-f /usr/lib64/libz.so.1 \
	-f /usr/share/maven/ \
	-f /usr/src/mymaven/
scripts/compile_selinux.sh
docker run -it -p 3330:3330 --cap-drop=all --security-opt seccomp=policies/seccomp.json --security-opt label:type:priote_t --name priote_c -v "${PWD}/data":/usr/src/mymaven/data:rw,Z priote_stripped_i:latest
