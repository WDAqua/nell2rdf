#!/bin/bash
set -x 
for i in $(seq $2 $3); do 
	wget -c -P "$1" "http://rtw.ml.cmu.edu/resources/results/08m/NELL.08m.$i.esv.csv.gz"
	wget -c -P "$1" "http://rtw.ml.cmu.edu/resources/results/08m/NELL.08m.$i.esv.csv.gz"
	wget -c -P "$1" "http://rtw.ml.cmu.edu/resources/results/08m/NELL.08m.$i.cesv.csv.gz"
done

