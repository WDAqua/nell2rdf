#!/bin/bash

declare -a annotationformats=(NONE REIFICATION N-ARY SINGLETON-PROPERTY NDFLUENTS QUADS)
declare -a annotationextensions=(nt reif.nt nary.nt sp.nt nd.nt nq)

function n2r { # jarFile serializationFormat ontologyFile dataFile javaArguments
        for i in ${!annotationformats[@]}; do
                java "$5" -jar "$1" "$2" ${annotationformats[${i}]} "$3" "$3".ttl "$4" "$4".${annotationextensions[${i}]}
                LC_ALL=C sort -u -o "$4".${annotationextensions[${i}]} "$4".${annotationextensions[${i}]}
                lzop -Uf9 "$3".ttl
                lzop -Uf9 "$4".${annotationextensions[${i}]}
        done
}

declare jarFile="$1"
declare serializationFormat="$2"
declare ontologyFile="$3"
declare promotedFile="$4"
declare candidatesFile="$5"
shift 5
declare javaArguments="$*"

set -x

case ${ontologyFile##*.} in
	"gz") gzip -dfk "$ontologyFile" ;;
	"lzo") lzop -df "$ontologyFile" ;;
	"bz2") bzip2 -dfk "$ontologyFile" ;;
esac

lzop -fd ${promotedFile}
n2r "$jarFile" "$serializationFormat" ${ontologyFile%.*} ${promotedFile%.*} "$javaArguments"
rm -f ${promotedFile%.*}

lzop -fd ${candidatesFile}
n2r "$jarFile" "$serializationFormat" ${ontologyFile%.*} ${candidatesFile%.*} "$javaArguments"
rm -f ${candidatesFile%.*}

rm -f ${ontologyFile%.*}