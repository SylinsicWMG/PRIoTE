#!/bin/bash
fullfile=$(readlink -f ${BASH_SOURCE[0]})
current_dir=$(dirname $fullfile)
current_file=$(basename $fullfile)

cd $current_dir/HQDM
mvn -q clean install

cd $current_dir/MagmaCore
mvn -q clean install
