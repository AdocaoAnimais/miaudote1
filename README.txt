PRIMEIRO DE TUDO - rodar o virtual environment:
pipenv shell

API urls:

CRUD:
http://127.0.0.1:8000/api/users/

Para acessar usuario especifico: (exemplo)
http://127.0.0.1:8000/api/users/3/

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
