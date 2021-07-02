#!/usr/bin/bash
fullfile=$(readlink -f ${BASH_SOURCE[0]})
current_dir=$(dirname $fullfile)
current_file=$(basename $fullfile)

ln -s /usr/bin /bin
ln -s /usr/lib64 /lib64
ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

cd $current_dir/MagmaCore
mvn exec:java -Dexec.mainClass="uk.gov.gchq.magmacore.MagmaCore"
