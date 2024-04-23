CREATE TABLE IF NOT EXISTS ENDERECO
(
    ENDERECO_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    BAIRRO_ID     INTEGER NOT NULL,
    ESTADO_ID   INTEGER,
    CIDADE_ID   INTEGER NOT NULL,
    FOREIGN KEY (CIDADE_ID) REFERENCES CIDADE (CIDADE_ID),
    FOREIGN KEY (ESTADO_ID) REFERENCES Estado (ESTADO_ID),
    FOREIGN KEY (BAIRRO_ID) REFERENCES BAIRRO (BAIRRO_ID)
);



INSERT INTO ENDERECO (BAIRRO_ID, ESTADO_ID, CIDADE_ID)
VALUES
    (1, 1, 1),  -- Exemplo de um endereço na cidade 1, estado 1 e bairro 1
    (2, 2, 2),  -- Exemplo de um endereço na cidade 2, estado 2 e bairro 2
    (3, 3, 3),  -- Exemplo de um endereço na cidade 3, estado 3 e bairro 3
    (4, 1, 2),  -- Exemplo de um endereço na cidade 2, estado 1 e bairro 4
    (5, 2, 3);