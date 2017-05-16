#!/bin/bash

EXPECTED_ARGS=1
E_BADARGS=65

if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Usage: `basename $0` /path/to/nell/ontology/csv/file"
  exit $E_BADARGS
fi

nbclasses=`cat $1 | egrep '^.*a       rdfs:Class ;.*$' | wc -l`



nbproperties=`cat $1 | grep "memberofset" | grep relation | wc -l`

nbpositiveantireflexive=`cat $1 | grep "antireflexive" | grep "true" | wc -l`
nbnegativeantireflexive=`cat $1 | grep "antireflexive" | grep "false" | wc -l`

nbpositiveantisymmetric=`cat $1 | grep "antisymmetric" | grep "true" | wc -l`
nbnegativeantisymmetric=`cat $1 | grep "antisymmetric" | grep "false" | wc -l`

nbpositivedwr=`cat $1 | grep "domainwithinrange" | grep "true" | wc -l`
nbnegativedwr=`cat $1 | grep "domainwithinrange" | grep "false" | wc -l`

nbpositiverwd=`cat $1 | grep "rangewithindomain" | grep "true" | wc -l`
nbnegativerwd=`cat $1 | grep "rangewithindomain" | grep "false" | wc -l`

nbpositivepopulate=`cat $1 | grep "populate" | grep "true" | wc -l`
nbnegativepopulate=`cat $1 | grep "populate" | grep "false" | wc -l`

nbpositivevisible=`cat $1 | grep "visible" | grep "true" | wc -l`
nbnegativevisible=`cat $1 | grep "visible" | grep "false" | wc -l`

nbvalueone=`cat $1 | grep "nrofvalues" | grep "1" | wc -l`
nbvaluemulti=`cat $1 | grep "nrofvalues" | grep "any" | wc -l`

nbdescription=`cat $1 | awk '{print $2}' | grep "description" | wc -l`
nbdomains=`cat $1 | awk '{print $2}' | grep "domain" | wc -l`
nbranges=`cat $1 | awk '{print $2}' | grep "range" | wc -l`
nbhumreadable=`cat $1 | awk '{print $2}' | grep "humanformat" | wc -l`
nbsub=`cat $1 | awk '{print $2}' | grep "generalizations" | wc -l`
nbinstancetype=`cat $1 | awk '{print $2}' | grep "instancetype" | wc -l`
nbinverse=`cat $1 | awk '{print $2}' | grep "inverse" | wc -l`
nbmutex=`cat $1 | awk '{print $2}' | grep "mutexpredicates" | wc -l`

echo -e "Number of classes : "$nbclasses
echo -e "Number of subsumption"$nbsub
echo -e "Number of mutexpredicates"$nbmutex"\n"

echo -e "Number of property : "$nbproperties
echo -e "Number of domain for properties : "$nbdomains
echo -e "Number of range for properties : "$nbranges
echo -e "Number of nrofvalues set to 1 : "$nbvalueone
echo -e "Number of nrofvalues set to any : "$nbvaluemulti"\n"

echo -e "Number of humanreadable formatter : "$nbhumreadable
echo -e "Number of description : "$nbdescription"\n"

echo -e "Number of instancetype : "$nbinstancetype

echo -e "Number of antireflexive property set to true : "$nbpositiveantireflexive
echo -e "Number of antireflexive property set to false : "$nbnegativeantireflexive"\n"

echo -e "Number of antisymmetric property set to true : "$nbpositiveantisymmetric
echo -e "Number of antisymmetric property set to false : "$nbnegativeantisymmetric"\n"

echo -e "Number of domainwithinrange property set to true : "$nbpositivedwr
echo -e "Number of domainwithinrange property set to false : "$nbnegativedwr"\n"

echo -e "Number of rangewithindomain property set to true : "$nbpositiverwd
echo -e "Number of rangewithindomain property set to false : "$nbnegativerwd"\n"

echo -e "Number of populate property set to true : "$nbpositivepopulate
echo -e "Number of populate property set to false : "$nbnegativepopulate"\n"

echo -e "Number of visible property set to true : "$nbpositivevisible
echo -e "Number of visible property set to false : "$nbnegativevisible"\n"
