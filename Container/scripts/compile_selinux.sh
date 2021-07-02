#!/bin/bash
cd ../policies
sudo make -f /usr/share/selinux/devel/Makefile priote.pp
sudo semodule -i priote.pp
