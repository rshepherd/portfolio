#!/bin/bash
java -Xms512m -Xmx1024m -cp . -jar portfolio-1.0.0.jar -p 54321 -n 5 -m mode-1 -g true -c /home/rjs471/heuristics/portfolio/favorability.txt
