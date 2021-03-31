 #/bin/bash

POD_NAME=$(oc get pod | grep $(cat env | grep DATABASE_SERVICE_NAME | awk -F \" '{print $2}') | grep -v deploy | awk '{print $1}')

oc rsync schema $POD_NAME:/tmp



oc exec -it $POD_NAME -- bash -c "psql -U $(cat env | grep POSTGRESQL_USER | awk -F \" '{print $2}') $(cat env | grep POSTGRESQL_DATABASE | awk -F \" '{print $2}') < /tmp/schema/schema.sql"

