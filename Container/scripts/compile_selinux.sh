#!/bin/bash
cd ../policies
make -f /usr/share/selinux/devel/Makefile priote.pp 	# Compile the selinux policy
sudo semodule -i priote.pp				# Insert the created module
sudo semanage port -a -t priote_port_in_t -p tcp 3330	# Add the port for our web server
