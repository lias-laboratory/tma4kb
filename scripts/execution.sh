#!/bin/bash
export jenatdb_repository=/home/adminlias/Louise/db-rep
java -cp target/tma4kb-0.0.1-SNAPSHOT-jar-with-dependencies.jar fr.ensma.lias.tma4kb.execution.TMA4KBLauncher -c dbpcard.config -q queriesDBpediaq9.test -k 100 -m countlimit -a base,bfs,var,full -t jena
/home/adminlias/apache-jean-fuseki-3.14.0/fueski-server --loc=/home/adminlias/Louise/db-rep /dbpedia
export sparqlendpoint_url=http://193.55.163.213:3030/dbpedia
java -cp target/tma4kb-0.0.1-SNAPSHOT-jar-with-dependencies.jar fr.ensma.lias.tma4kb.execution.TMA4KBLauncher -c dbpcard.config -q queriesDBpediaq9.test -k 100 -m countlimit -a base,bfs,var,full -t fuseki
export sparqlendpoint_url=http://193.55.163.213:8890/sparql
export sparqlendpoint_efaultgraphuri=http://dbpedia.org
java -cp target/tma4kb-0.0.1-SNAPSHOT-jar-with-dependencies.jar fr.ensma.lias.tma4kb.execution.TMA4KBLauncher -c dbpcard.config -q queriesDBpediaq9.test -k 100 -m countlimit -a base,bfs,var,full -t virtuoso


