from django.db import models

# Create your models here.


class PET(models.Model):
    pet_id = models.AutoField(db_column='PET_ID', primary_key=True)
    nome = models.CharField(max_length=200, null=True, blank=True, db_column='NOME')
    sexo = models.CharField(max_length=1, null=True, blank=True, db_column='SEXO')
    idade = models.IntegerField(null=True, blank=True, db_column='IDADE')
    porte = models.CharField(max_length=1, null=True, blank=True, db_column='PORTE')
    castrado = models.CharField(max_length=1, null=True, blank=True, db_column='CASTRADO')
    data_cadastro = models.DateField(null=True, blank=True, db_column='data_cadastro')
    descricao = models.TextField(null=True, blank=True, db_column='descricao')
    foto = models.ImageField(upload_to='fotos/', null=True, blank=True, db_column='foto')
    usuario_cadastro_id = models.IntegerField(null=True, blank=True, db_column='usuario_cadastro_id')

    def __str__(self):
        return self.nome
