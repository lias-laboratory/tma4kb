# TMA4KB

The overabundant answers problem refers to a situation where a user querying a knowledge base is faced with more answers than they want. We provide two cooperative notions to help with query rewritting: the set of minimal subqueries that induce failure (MFIS) and the set of maximal subqueries that do not fail (XSS). Our objective is to rapidly compute MFIS and XSS. This project is used to experimentally evaluate our algorithms.

Three main algorithms are provided:
* **Base**: executes all subqueries of a failing query,
* **Var**: does not execute queries that have a succeeding superquery + uses a variable-based property to deduce query failure,
* **Full**: Var + uses a cardinality-based property (with global cardinalities) to deduce query failure.

We have studied three variations on the Full algorithm, regarding various cardinality calculations: 
* **Class**: uses class cardinalities,
* **CS**: uses characteristic sets cardinalities,
* **Any**: uses global cardinalities, but exploits the property for any cardinality value (not just cardinality 1).

We have implemented four execution methods for the queries in a triplestore :
* **All**: executes the basic query, returns all answers to the program which counts them,
* **Count**: adds the SPARQL operator @COUNT@ to the query, so the counting of results is done within the triplestore,
* **Limit**: adds a SPARQL @LIMIT@ operator to the query, with a value of @k+1@, where @k@ is the threshold for overabundant answers. This execution method provides the exact number of results if it is smaller than @k+1@, or informs that the answers are overabundant without the exact number.
* **Count-Limit**: combines methods Count and Limit. The counting is done in the triplestore, and stops after @k+1@ results are found.

Three triplestore implementations are supported: 
* [Jena](https://jena.apache.org): the native version of Jena TDB,
* [Fuseki](https://jena.apache.org/documentation/fuseki2/): the SPARQL endpoint version of Jena TDB,
* [Virtuoso](https://virtuoso.openlinksw.com/): the SPARQL endpoint version of Virtuoso: https://virtuoso.openlinksw.com/.

Four experiments presented in the article can be reproduced using this tutorial:
* Experiment 1: comparing execution methods,
* Experiment 2: algorithm comparison with generated data and queries,
* Experiment 3: algorithm comparison with real data and queries,
* Experiment 4: studying the impact of different cardinalities

## Repeatability instructions

### Software requirements

* Watdiv benchmark: http://dsg.uwaterloo.ca/watdiv/
* Dbpedia data: http://downloads.dbpedia.org/3.9/en/
* JenaTDB (min version 3.14): https://jena.apache.org/
* JenaFuseki (min  version 3.14): https://jena.apache.org/
* Virtuoso: http://virtuoso.openlinksw.com
* Cardinality generation tool : https://forge.lias-lab.fr/projects/ntriplestatistics
* Java 8: https://www.java.com/download/

The experiments using WatDiv or DBpedia are seperate, it is possible to run experiments using only one dataset. Likewise, the implementations using the three triplestores (JenaTDB, Jena Fuseki and Virtuoso) are independent, so the experiments can be run using only one triplestore. The cardinality generation is needed in order to run the **Full** algorithm and it's **Class**, **CS**, and **Any** variations.

### Compilation

* Compile the project.

```console
$ mvn clean package -P deployement
```

### Data Generation

#### A - Jena

1 - Importing WatDiv data into Jenatdb

Go to the _bin/Relase_ directory of **WatDiv**.

```console
$ cd bin/Release
```

To generate a dataset, execute the following command (using scale factor 100). The result will be stored into the _watdiv.nt_ file.

```console
$ ./watdiv -d ../../model/wsdbm-data-model.txt SCALE_FACTOR > ~/watdiv.nt
```

Go to the _bin_ directory of **Jena** directory.

Execute the following command. You need to specify the target repository (--loc parameter) and the dataset source.

```console
$ ./tdbloader2 --loc ~/wdrepository ~/watdiv.nt
```

2 - Importing DBpedia data into Jenatdb

Download DBPedia dataset as _.nt_ files to _~/dbpedia_ folder.

```console
$ ./tdbloader2 --loc ~/dbprepository ~/dbpedia/\*.nt
```

3 - Specifying the path to the repository

```console
$ export jenatdb_repository=~/dbprepository
```

#### B - Fuseki

1 - Starting the server

Go to the **Fuseki** directory

```console
$ ./fuseki-server --loc=~/dbprepository /dbpedia
```

2 - Specifying the path to the endpoint

```console
$ export sparqlendpoint_url=http://IPaddress:listeningPort/datasetname
```

The default listening port is @3030@.

#### C - Virtuoso

1 - Starting the server

```console
$ virtuoso-t +configfile /path/to/the/directory/file.ini +foreground
```

2 - Loading the data

```console
$ isql-vt SPARQLPort
$ ld_dir('/path/to/the/folder', 'files.nt', 'graph.name');
$ rdf_loader_run();
```

The default SPARQL Port is @1111@. 

3 - Specifying the path to the endpoint

```console
$ export sparqlendpoint_url=http://IPaddress:HTTPPort/sparql
$ export sparqlendpoint_defaultgraphuri=graph.name
```

The default HTTP Port is @8890@.

4 - Stopping the server

```console
$ isql-vt SPARQLPort
$ shutdown;
```

### Cardinality Generation

Run the cardinality generation tool (https://forge.lias-lab.fr/projects/ntriplestatistics), according to its documentation. 

For the WatDiv and DBpedia datasets, the global and CS cardinalities must be computed and added to the samples directory as separate files:
* _samples/cardDBpedia_ for DBpedia global cardinalities
* _samples/CSDBpedia_ for DBpedia CS cardinalities
* _samples/cardWatDiv_ for WatDiv global cardinalities
* _samples/CSWatDiv_ for WatDiv CS cardinalities

Additionally, for the DBpedia dataset, the local cardinalities and domains must be computed. The local cardinalities, global cardinalities and domains text files thus obtained should combined and added to the samples directory.

```console
$ copy local+global+domains samples/card_locDBpedia 
```

### Algorithm Execution

Ensure that the path for the repository or endpoint matches the chosen dataset.

Run the following command from the _tma4kb_ file using the appropriate query and cardinality files.

```console
$ java -jar tma4kb.jar [parameters]
```

The following instruction provides the help menu, with indications about the various parameters.

```console
$ java -jar tma4kb.jar -h
```

It returns: 

```console
Core for 'Too Many Answers Problem in Knowledge Bases' algorithms.

Usage: <main class> [-h] [-c=<cardinalitiesFile>] [-e=<numberExecution>]
                    [-k=<k>] [-l=<localFile>] -m=<method> -q=<queriesFile>
                    [-s=<csFile>] [-t=<triplestore>] [-a=<algorithm>[,
                    <algorithm>...]]...

  -a, --algorithm=<algorithm>[,<algorithm>...]
                            The algorithm to run: base, bfs, var, full, local,
                              cs, any
  -c, --cardinalities=<cardinalitiesFile>
                            The file which contains the dataset cardinalities.
  -e, --execution=<numberExecution>
                            The number of executions.
  -h, --help                Print usage help and exit.
  -k, --threshold=<k>       The threshold for overabundant answers.
  -l, --local=<localFile>   The file which contains the dataset local (class)
                              cardinalities.
  -m, --method=<method>     The evaluation method in Jena. Methods possible:
                              all, stopK, count, limit, countlimit
  -q, --queries=<queriesFile>
                            The file which contains the queries of dataset.
  -s, --cs=<csFile>         The file which contains the dataset characteristic
                              sets.
  -t, --triplestore=<triplestore>
                            Choice of triplstore: jena, fuseki, virtuoso
```

Here is an example for an execution using the virtuoso triplestore.

```console
$ java -jar tma4kb.jar -q samples/queriesWatDiv -a full,cs -t virtuoso -m countlimit -c samples/cardWatDiv -s samples/CSWatDiv
```

A CSV file is generated for each algorithm containing for each query: `QueryNumber ExecutionTime NumberOfExecutedQueries`.

## Experiment parameters

The experiments reproduced using the following parameters (with e=5, k=100):
* Experiment 1: Algorithm: Full, Method: All, Count, Limit, CountLimit dataset : DBpedia, triplestore: Jena,
* Experiment 2: Algorithm: Base, Var, Full, Method: CountLimit, dataset : WatDiv, triplestore: Jena,
* Experiment 3: Algorithm: Base, Var, Full, Method: CountLimit, dataset : DBpedia, triplestore: Jena, Fuseki, Virtuoso,
* Experiment 4.1: Algorithm: Full, Local, CS Method: CountLimit, dataset : DBpedia, triplestore: Jena,
* Experiment 4.2: Algorithm: Full, CS Method: CountLimit, dataset : WatDiv, triplestore: Jena,
* Experiment 4.3: Algorithm: Full, Any Method: Count, and Algorithm: Full, method: CountLimit dataset : DBpedia, triplestore: Jena.

When using the DBpedia dataset, the query file is available at samples/queriesDBpedia, the cardinalities file at samples/cardDBpedia, the local cardinalities at samples/card_locDBpedia and the characteristic sets at samples/CSDBpedia

When using the WatDiv dataset, the query file is available at samples/queriesWatDiv, the cardinalities file at samples/cardWatDiv and the characteristic sets at samples/CSWatDiv

## Software licence agreement

Details the license agreement of TMA4KB: [LICENCE](LICENCE)

## Historic Contributors (core developers first and alphabetical order)

* [Louise PARKIN (core developer)](https://www.lias-lab.fr/members/louiseparkin/)
* [Mickael BARON](https://www.lias-lab.fr/members/mickaelbaron/)
* [Brice CHARDIN](https://www.lias-lab.fr/members/bricechardin/)
* [Ibrahim DELLAL](https://www.lias-lab.fr/members/ibrahimdellal/)
* [Allel HADJALI](https://www.lias-lab.fr/members/allelhadjali/)
* [Stéphane JEAN](https://www.lias-lab.fr/members/stephanejean/)

## Code analysis

* Lines of code : 3 000
* Programming language : Java
