from django.contrib.auth.models import AbstractUser
from django.db import models
from django.core.validators import RegexValidator



class CustomUser(AbstractUser):
    # Add custom fields if needed
    username = models.CharField(max_length=30, unique=True, default='')
    bio = models.TextField(max_length=500, blank=True)
    cpf = models.CharField(max_length=14, unique=True, null=True, blank=True)
    email = models.CharField(max_length=30)
    cidade = models.CharField(max_length=40, blank=True)
    telefone_regex = RegexValidator(regex=r'^\d{10,11}$', message="Numero de telefone deve conter DDD e 9 ou 8 digitos a mais.")
    telefone = models.CharField(validators=[telefone_regex], max_length=15, null=True, blank=True)
    
    # Define unique related names for groups and user permissions
    groups = models.ManyToManyField(
        'auth.Group',
        related_name='custom_user_set',  # Unique related name for groups
        blank=True,
        verbose_name='groups',
        help_text='The groups this user belongs to. A user will get all permissions granted to each of their groups.',
    )
    user_permissions = models.ManyToManyField(
        'auth.Permission',
        related_name='custom_user_set',  # Unique related name for user permissions
        blank=True,
        verbose_name='user permissions',
        help_text='Specific permissions for this user.',
        )