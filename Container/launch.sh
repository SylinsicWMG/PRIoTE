#!/bin/bash
docker build -t priote:1.0 -t priote:latest .
docker run -d -p 3330:3330 --cap-drop=all priote:latest
