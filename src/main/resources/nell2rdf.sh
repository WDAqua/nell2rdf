#!/bin/bash

declare -a annotationformats=(NONE REIFICATION N-ARY SINGLETON-PROPERTY NDFLUENTS QUADS)
declare -a annotationextensions=(nt reif.nt nary.nt sp.nt nd.nt nq)

# Defaults
declare jarFile="../../../target/NellConverter-0.0.1-SNAPSHOT.jar"
declare serializationFormat="N-TRIPLE"
declare javaArguments=""
declare compress=""

function showhelp {
	echo
	echo "Script to generate all NELL2RDF datasets"
	echo
	echo "Usage $0 [OPTION]* ONTOLOGY PROMOTED CANDIDATES"
	echo
	echo "  OPTIONS:"
	echo "    -c, --compress  compress the output in the desired format (gzip|bzip|lzop)"
	echo "    -f, --format    serialization format to encode the RDF triples (default N-TRIPLES)""
	echo "                            (RDF/XML | RDF/XML-ABBREV | N-TRIPLE | TURTLE | TTL | N3 )"
	echo "    -h, --help      shows this help and exists"
	echo "    -j, --jar       path to NELL2RDF java jar file (default $jarFile)"
	echo "    -p, --params    parameters for java command (i.e., -Xmx40g)""
	echo "  ONTOLOGY    path to NELL's ontology file"
	echo "  PROMOTED    path to NELL's promoted beliefs file"
	echo "  CANDIDATES  path to NELL's candidates file" 
}

function decompress { 
	case ${1##*.} in
		"gz") gzip -dfk "$1" ;;
		"lzo") lzop -df "$1" ;;
		"bz2") bzip2 -dfk "$1" ;;
	esac
}

function compress {
	case "$compress" in
			"gzip") gzip -f9 "$1" ;;
			"bzip") bzip2 -f9 "$3" ;;
			"lzop") lzop -Uf9 "$3" ;;
	esac	
}

function n2r { # jarFile serializationFormat annotationFormat ontologyFile dataFile javaArguments
                java "$6" -jar "$1" "$2" $3 "$4" "$4".ttl "$5" "$5".${annotationextensions[${i}]}
                LC_ALL=C sort -u -o "$5".${annotationextensions[${i}]} "$5".${annotationextensions[${i}]}                
}



getopt --test > /dev/null
if [[ $? -eq 4 ]]; then
    # enhanced getopt works
    OPTIONS=c::f:hj:p:
	LONGOPTIONS=compress::,format:,help,jar:,params:
    COMMAND=$(getopt -o $OPTIONS -l $LONGOPTIONS -n "$0" -- "$@")
    if [[ $? -ne 0 ]]; then
    	exit 2
    fi
    eval set -- "$COMMAND"
else
	echo "Enhanced getopt not supported. Brace yourself, this is not tested, but it should work :-)"
fi

while true; do
	case "$1" in
		-c|--compress)
			case "$2" in
				""|"gz"|"gzip") compress=gzip ; shift 2 ;;
				"bz"|"bz2"|"bzip"|"bzip2") compress=bzip ; shift 2 ;;
				"lz"|"lzo"|"lzop") compress=lzop ; shift 2 ;;
				*) echo "Wrong compression format" ; exit 3 ;;
			esac
			;;
		-f|--format)
			serializationFormat="$2"
			shift 2
			;;
		-j|--jar)
			jarFile="$2"
			shift 2
			;;
		-p|--params)
			javaArguments="$2"
			shift 2
			;;
		--)
			shift
			break
			;;
		*)
			showhelp
			exit 0;
			;;
	esac
done
		
declare ontologyFile="$1"
declare promotedFile="$2"
declare candidatesFile="$3"

set -x

decompress "$ontologyFile"
decompress "$promotedFile"
decompress "$candidatesFile"

for i in ${!annotationformats[@]}; do
	n2r "$jarFile" ${serializationFormat[$i] "$annotationFormat" ${ontologyFile%.*} ${promotedFile%.*} "$javaArguments" &
	promoted[$i]=$!
	n2r "$jarFile" ${serializationFormat[$i] "$annotationFormat" ${ontologyFile%.*} ${candidatesFile%.*} "$javaArguments" &
	candidates[$i]=$!
	wait ${promoted[$i]} ${candidates[$i]}
	LC_ALL=C sort -u -o ${promotedFile%.*}${candidatesFile%.*}.annotationextensions[i] "$promotedFile".annotationextensions[i] "$candidatesFile".annotationextensions[i]
	compress "$promotedFile".annotationextensions[i] &
	compress "$candidatesFile".annotationextensions[i] &
	compress ${promotedFile%.*}${candidatesFile%.*}.annotationextensions[i] &
done

rm -f ${ontologyFile%.*}
rm -f ${promotedFile%.*}
rm -f ${candidatesFile%.*}
