#!/bin/bash

EXPECTED_ARGS=1
E_BADARGS=65

if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Usage: `basename $0` /path/to/nell/ontology/csv/file"
  exit $E_BADARGS
fi

nbclasses=`cat $1 | grep "\tmemberofsets\t" | grep category | sed 's/concept://g' | sort | uniq | wc -l`
nbproperties=`cat $1 | grep "\tmemberofsets\t" | grep relation | sed 's/concept://g' | sed 's/rtwrelation/relation/g' | sort | uniq | wc -l`

nbpositiveantireflexive=`cat $1 | grep "\tantireflexive\t" | sed 's/concept://g' | sort | uniq | grep "true" | wc -l`
nbnegativeantireflexive=`cat $1 | grep "\tantireflexive\t" | sed 's/concept://g' | sort | uniq | grep "false" | wc -l`

nbpositiveantisymmetric=`cat $1 | grep "\tantisymmetric\t" | sed 's/concept://g' | sort | uniq | grep "true" | wc -l`
nbnegativeantisymmetric=`cat $1 | grep "\tantisymmetric\t" | sed 's/concept://g' | sort | uniq | grep "false" | wc -l`

nbpositivedwr=`cat $1 | grep "\tdomainwithinrange\t" | sed 's/concept://g' | sort | uniq | grep "true" | wc -l`
nbnegativedwr=`cat $1 | grep "\tdomainwithinrange\t" | sed 's/concept://g' | sort | uniq | grep "false" | wc -l`

nbpositiverwd=`cat $1 | grep "\trangewithindomain\t" | sed 's/concept://g' | sort | uniq | grep "true" | wc -l`
nbnegativerwd=`cat $1 | grep "\trangewithindomain\t" | sed 's/concept://g' | sort | uniq | grep "false" | wc -l`

nbpositivepopulate=`cat $1 | grep "\tpopulate\t" | sed 's/concept://g' | sort | uniq | grep "true" | wc -l`
nbnegativepopulate=`cat $1 | grep "\tpopulate\t" | sed 's/concept://g' | sort | uniq | grep "false" | wc -l`

nbpositivevisible=`cat $1 | grep "\tvisible\t" | sed 's/concept://g' | sort | uniq | grep "true" | wc -l`
nbnegativevisible=`cat $1 | grep "\tvisible\t" | sed 's/concept://g' | sort | uniq | grep "false" | wc -l`

nbvalueone=`cat $1 | grep "\tnrofvalues\t" | sed 's/concept://g' | sort | uniq | grep "1" | wc -l`

nbdescription=`cat $1 | grep "\tdescription\t" | wc -l`

nbdomains=`cat $1 | grep "\tdomain\t" | sed 's/concept://g' | sort | uniq | wc -l`
nbranges=`cat $1 | grep "\trange\t" | sed 's/concept://g' | sort | uniq | wc -l`

nbhumreadable=`cat $1 | grep "\thumanformat\t" | sed 's/concept://g' | sort | uniq | wc -l`
nbsub=`cat $1 | grep "\tgeneralizations\t" | sed 's/concept://g' | sort | uniq | wc -l`
nbinstancetype=`cat $1 | grep "\tinstancetype\t" | sed 's/concept://g' | sort | uniq | wc -l`
nbinverse=`cat $1 | grep "\tinverse\t" | sed 's/concept://g' | sort | uniq | wc -l`
nbmutex=`cat $1 | grep "\tmutexpredicates\t" | sed 's/concept://g' | sort | uniq | wc -l`

echo -e $nbclasses" classes"
echo -e $nbsub" subsumptions"
echo -e $nbmutex" mutexpredicates\n"

echo -e $nbproperties" properties "
echo -e $nbdomains" domains for properties "
echo -e $nbranges" ranges for properties "
echo -e $nbvalueone" functional properties (nrofvalues set to 1) "

echo -e $nbhumreadable" humanreadable formatters "
echo -e $nbdescription" descriptions \n"

echo -e $nbinstancetype" instancetypes "

totantireflexive=$(($nbpositiveantireflexive+$nbnegativeantireflexive))
echo -e $totantireflexive" antireflexive properties :\t "$nbpositiveantireflexive" set to true, and "$nbnegativeantireflexive" set to false"

totantisymmetric=$(($nbpositiveantisymmetric+$nbnegativeantisymmetric))
echo -e $totantisymmetric" antisymmetric properties :\t "$nbpositiveantisymmetric" set to true, and "$nbnegativeantisymmetric" set to false"

totdomawithrange=$(($nbpositivedwr+$nbnegativedwr))
echo -e $totdomawithrange" domainwithinrange properties :\t "$nbpositivedwr" set to true, and "$nbnegativedwr" set to false"

totrangewithdom=$(($nbpositiverwd+$nbnegativerwd))
echo -e $totrangewithdom" rangewithindomain properties :\t "$nbpositiverwd" set to true, and "$nbnegativerwd" set to false"

totpopulate=$(($nbpositivepopulate+$nbnegativepopulate))
echo -e $totpopulate" populate properties :\t "$nbpositivepopulate" set to true, and "$nbnegativepopulate" set to false"

totvisible=$(($nbpositivevisible+$nbnegativevisible))
echo -e $totvisible" visible properties :\t "$nbpositivevisible" set to true, and "$nbnegativevisible" set to false"

echo -e $nbinstancetype" inverse properties "