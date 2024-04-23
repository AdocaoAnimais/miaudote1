CREATE TABLE IF NOT EXISTS ESTADO
(
    ESTADO_ID INTEGER         NOT NULL PRIMARY KEY AUTOINCREMENT,
    CODIGO_UF  INTEGER         NOT NULL,
    NOME      VARCHAR(50) NOT NULL,
    UF        CHAR(2)     NOT NULL,
    REGIAO    INTEGER         NOT NULL
);


Insert into Estado (CODIGO_UF, Nome, Uf, Regiao) values (41, 'Paraná', 'PR', 4);
Insert into Estado (CODIGO_UF, Nome, Uf, Regiao) values (43, 'Rio Grande do Sul', 'RS', 4);
Insert into Estado (CODIGO_UF, Nome, Uf, Regiao) values (42, 'Santa Catarina', 'SC', 4);