from django.db import models

# Create your models here.


class Base(models.Model):
    created_at = models.DateTimeField('Data de criação', auto_now_add=True)
    updated_at = models.DateTimeField('Data de atualização', auto_now=True)
    active = models.BooleanField('Ativo?', default=True)

    class Meta:
        abstract = True


class Pet(Base):
    name = models.CharField(max_length=20)
    age = models.PositiveIntegerField('Age:', default=0)
    type_choices = [
        ('C', 'cachorro'),
        ('G', 'gato'),
    ]
    type = models.CharField(max_length=8, choices=type_choices)
    image = models.ImageField(upload_to='pets/')
    description = models.TextField()

    def __str__(self):
        return self.name
