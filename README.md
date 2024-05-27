# Instruções
  
# Back-end

### Rodar Kotlin 

#### Metodo 1: Utilizando o IntellJ 
 - Baixar o intelliJ: https://www.jetbrains.com/pt-br/idea/
 - Abrir o intelliJ no projeto backend miaudote 
 - Aguardar a IDE fazer o dowload das libs
 - Executar o arquivo MiaudoteApplication em src/main/koltin/com/projeto2/miaudote

## Criar usuario: 
url: http://200.132.38.218:8004/api/usuario/cadastrar
infos: 
```
{
    "nome": "user1",
    "sobrenome": "user",
    "username": "user",
    "cpf": "21223123123",
    "email": "email@user.com",
    "senha": "senha123"
}
```

Retorno: 
username e token autenticação

## Fazer login:
url: http://200.132.38.218:8004/api/auth/login
{
    "username":"PaulinaRehbein",
    "senha": "paulina"
}

Retorno: Token de autenticação
## Listar pet: 
url: http://200.132.38.218:8004/api/pet/obter-pets

### CRIAR PET:
url: http://200.132.38.218:8004/api/pet/
Passar o token de autenticação para criar o pet
```
{
    "nome": "pet1",
    "idade": "1",
    "sexo": "F",
    "porte": "P",
    "tipo": "G",
    "castrado": "N",
    "descricao": "",
}
```
  
# Front-end

```
cd ./front 
npm i 
npm run dev
```
* Geralmente http://localhost:3000/  
