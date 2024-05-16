PRIMEIRO DE TUDO - dentro do backend: 
pip freeze > requirements.txt (atualizar dependencias da aplicação)

pip install -r requirements.txt (instalar dependencias)

API urls:

CRUD Users:
http://127.0.0.1:8000/api/users/

Para acessar usuario especifico: (exemplo)
http://127.0.0.1:8000/api/users/3/

CRUD Pets:
http://127.0.0.1:8000/api/pets/

LOGIN:
http://127.0.0.1:8000/api-token-auth/

Rodar o Django:

1. fazer migraçoes necessarias
python manage.py makemigrations
python manage.py migrate

2. rodar o server
python manage.py runserver

3. acessar os urls acima

Criar novo app:
python manage.py startapp <name of app>


### Rodar Kotlin 

#### Metodo 1: Utilizando o IntellJ 
 - Baixar o intelliJ: https://www.jetbrains.com/pt-br/idea/
 - Abrir o intelliJ no projeto backend miaudote 
 - Aguardar a IDE fazer o dowload das libs
 - Executar o arquivo MiaudoteApplication em src/main/koltin/com/projeto2/miaudote




