DROP TABLE IF EXISTS t;

CREATE TABLE t(
s VARCHAR(100),
p VARCHAR(100),
o VARCHAR(100)
);
	
insert into t values('fp1', 'type', 'FullProfessor');
insert into t values('fp1', 'age', '45');
insert into t values('fp1', 'nationality', 'US');
insert into t values('fp1', 'advisor', 's1');
insert into t values('fp1', 'advisor', 's2');
insert into t values('fp1', 'teacherOf', 'SW');
insert into t values('fp1', 'teacherOf', 'DB');
insert into t values('fp1', 'teacherOf', 'ES');
insert into t values('fp2', 'type', 'FullProfessor');
insert into t values('fp2', 'age', '52');
insert into t values('fp2', 'nationality', 'FR');
insert into t values('fp2', 'advisor', 's3');
insert into t values('fp2', 'teacherOf', 'PR');
insert into t values('fp2', 'teacherOf', 'CA');
insert into t values('fp2', 'teacherOf', 'IA');
insert into t values('fp3', 'type', 'FullProfessor');
insert into t values('fp3', 'age', '49');
insert into t values('fp3', 'nationality', 'US');
insert into t values('fp3', 'teacherOf', 'ML');
insert into t values('s1', 'type', 'Student');
insert into t values('s1', 'nationality', 'US');
insert into t values('s2', 'type', 'Student');
insert into t values('s2', 'nationality', 'FR');
insert into t values('s3', 'type', 'Student');
insert into t values('s3', 'nationality', 'FR');
insert into t values('s3', 'nationality', 'UK');
