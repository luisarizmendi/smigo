 #/bin/bash

oc process -f postgresql-persistent.yaml --param-file=env | oc create -f -
