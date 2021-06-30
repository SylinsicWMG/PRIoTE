#!/bin/bash
fullfile=$(readlink -f ${BASH_SOURCE[0]})
current_dir=$(dirname $fullfile)
current_file=$(basename $fullfile)

cd $current_dir/MagmaCore
mvn exec:java -Dexec.mainClass="uk.gov.gchq.magmacore.MagmaCore"
