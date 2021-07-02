#!/bin/bash
docker run -it -p 3330:3330 --cap-drop=all --security-opt seccomp=policies/seccomp.json --security-opt label:type:priote_t priote_stripped:latest
