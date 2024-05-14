### instalar o docker: 
https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-20-04-pt


### cria imagem e container:
  cria imagem do mysql/mysql-server e cria o container com 
  o nome `mysql` e configura a senha do usuario root como `projeto2` 

```dockerfile
sudo docker run -p 3306:3306 --name mysql -e "MYSQL_ROOT_PASSWORD=projeto2" -d mysql/mysql-server
```

### Acessar terminal bash do container mysql
```dockerfile
sudo docker exec -it mysql /bin/bash
```

### Acessar acessar mysql do container
```dockerfile
mysql -u root -p -A
```

#### exibe databases existentes
```sql
show databases;
```

SELECT user, host from mysql.user;

#### atualiza acesso remoto ao usuario root 
```sql
UPDATE mysql.user set host='%' where user='root';
```
#### atualiza privilegios
```sql
flush privileges;
```

#### sai do mysql
```sql
exit
```

#### sai do container
```dockerfile
exit
```

#### acessa o mysql de forma remota
```
sudo mysql -u root -p -P3306 -h 200.132.38.218
```